/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local_testdbnew;

import entity.Actor;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

/**
 *
 * @author LemonIV
 */
public class Local_TestSPDVDRental {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        final EntityManagerFactory emfOther = Persistence.createEntityManagerFactory("TestDBdvdrentalPU");
        EntityManager em = emfOther.createEntityManager();

        getAllActors(em);
        getActorById(em);
        getLikeActors(em);
        System.out.println("");
        System.out.println("=============================================");
        System.out.println("============= Stored Procedure ==============");
        System.out.println("=============== ONLY JPA 2.1 ================");
        System.out.println("=============================================");
        getAllActorsSP(em);
        getActorByIdSP(em);
        getLikeActorsSP(em);
        //getActorByIdNQ(em);
        getLikeActorsSPNew(em);
        getLikeActorsSPNewOther(em);
        //getCountActorsSPNew(em);
        getCountActorsSPNewOther(em);
        getCountActorsSPNew(em);
        emfOther.close();

    }

    public static void getAllActors(EntityManager em) {
        System.out.println("############  getAllActors()  #############");

        Query query = em.createNamedQuery("Actor.findAll");
        List<Actor> actorsList = query.getResultList();
        System.out.println("Total " + actorsList.size());
        System.out.println("id " + actorsList.get(0).getActorId());
        System.out.println("name " + actorsList.get(0).getFirstName());
    }

    public static void getActorById(EntityManager em) {
        System.out.println("############  getActorById()  #############");

        Query query = em.createNamedQuery("Actor.findById");
        query.setParameter("id", 11);
        List<Actor> actorsList = query.getResultList();
        System.out.println("Total " + actorsList.size());
        for (Actor myActor : actorsList) {
            System.out.println("id " + myActor.getActorId() + " first name " + myActor.getFirstName() + " last name " + myActor.getLastName());
        }
    }

    public static void getLikeActors(EntityManager em) {
        System.out.println("############  getLikeActors()  #############");

        Query query = em.createNamedQuery("Actor.findLikeLastName");
        query.setParameter("pattern", "%ra%");
        List<Actor> actorsList = query.getResultList();
        System.out.println("Total " + actorsList.size());
        for (Actor myActor : actorsList) {
            System.out.println("id " + myActor.getActorId() + " first name " + myActor.getFirstName() + " last name " + myActor.getLastName());

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    public static void getAllActorsSP(EntityManager em) {
        System.out.println("############  getAllActorsSP()  #############");

        StoredProcedureQuery stQuery = em.createNamedStoredProcedureQuery("Actor.getActorAll");
        List<Actor> actorsList = stQuery.getResultList();
        System.out.println("Total " + actorsList.size());
        System.out.println("id " + actorsList.get(0).getActorId());
        System.out.println("name " + actorsList.get(0).getFirstName());
    }

    public static void getActorByIdSP(EntityManager em) {
        System.out.println("############  getActorByIdSP()  #############");

        StoredProcedureQuery stQuery = em.createNamedStoredProcedureQuery("Actor.getActorById");
        stQuery.setParameter(2, 11);
        List<Actor> actorsList = stQuery.getResultList();
        System.out.println("Total " + actorsList.size());
        for (Actor myActor : actorsList) {
            System.out.println("id " + myActor.getActorId() + " first name " + myActor.getFirstName() + " last name " + myActor.getLastName());
        }
    }

    public static void getLikeActorsSP(EntityManager em) {
        System.out.println("############  getLikeActorsSP()  #############");

        StoredProcedureQuery stQuery = em.createNamedStoredProcedureQuery("Actor.getActorLikeLastName");
        stQuery.setParameter(2, "ra");
        List<Actor> actorsList = stQuery.getResultList();
        System.out.println("Total " + actorsList.size());
        for (Actor myActor : actorsList) {
            System.out.println("id " + myActor.getActorId() + " first name " + myActor.getFirstName() + " last name " + myActor.getLastName());

        }
    }

    /*
    public static void getActorByIdNQ(EntityManager em) {
        System.out.println("############  getActorByIdNQ()  #############");

        //Query query = em.createNativeQuery("Actor.getActorByIdNQ", Actor.class);
        //query.setParameter(1,11);
        
        Query q = em.createNativeQuery("{call get_actor_by_id_not_cursor(?)};",Actor.class);
        q.setParameter(1,11);
        
        List<Actor> actorsList = q.getResultList();
        System.out.println("Total " + actorsList.size());
        for (Actor myActor : actorsList) {
            System.out.println("id " + myActor.getActorId() + " first name " + myActor.getFirstName() + " last name " + myActor.getLastName());
        }
    }*/
    public static void getLikeActorsSPNew(EntityManager em) {
        System.out.println("############  getLikeActorsSPNew()  #############");

        StoredProcedureQuery stQuery = em.createStoredProcedureQuery("get_actor_like_lastname");
        stQuery.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
        stQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        stQuery.setParameter(2, "ra");

        List<Object[]> actorsList = stQuery.getResultList();
        System.out.println("Salida " + actorsList.size() + " a lo bestia");
        for (Object[] myObject : actorsList) {
            System.out.println("id " + myObject[0] + " first name " + myObject[1] + " last name " + myObject[2]);

        }
    }

    public static void getLikeActorsSPNewOther(EntityManager em) {
        System.out.println("############  getLikeActorsSPNewOther()  #############");

        Session session = em.unwrap(Session.class);

        ProcedureCall call = session.createStoredProcedureCall("get_actor_like_lastname");

        call.registerParameter(1, void.class, ParameterMode.REF_CURSOR);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue("ra");

        Output output = call.getOutputs().getCurrent();

        if (output.isResultSet()) {
            List<Object[]> actorsList = ((ResultSetOutput) output).getResultList();
            System.out.println("Salida " + actorsList.size() + " a lo bestia other");
            for (Object[] myObject : actorsList) {
                System.out.println("id " + myObject[0] + " first name " + myObject[1] + " last name " + myObject[2]);

            }
        }
    }

    public static void getCountActorsSPNew(EntityManager em) {
        System.out.println("############  getCountActorsSPNew()  #############");

        StoredProcedureQuery stQuery = em.createStoredProcedureQuery("get_actor_in_table");
        stQuery.registerStoredProcedureParameter("countTable", Long.class, ParameterMode.OUT);

        stQuery.execute();
        Long actorsCount = (Long) stQuery.getOutputParameterValue("countTable");
        System.out.println("Salida count " + actorsCount);
    }

    public static void getCountActorsSPNewOther(EntityManager em) {
        System.out.println("############  getCountActorsSPNewOther()  #############");

        Session session = em.unwrap(Session.class);

        Long actorsCount = session.doReturningWork(connection -> {
            try (CallableStatement function = connection.prepareCall(
                    "{ ? = call get_actor_in_table() }")) {
                function.registerOutParameter(1, Types.BIGINT);
                function.execute();
                return function.getLong(1);
            }
        });
        System.out.println("Salida count " + actorsCount);
    }
}
