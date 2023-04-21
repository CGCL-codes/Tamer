package com.titan.clients;

import com.titan.domain.*;
import java.util.*;
import javax.persistence.*;

public class IsNull {

    public static void main(String[] args) throws Exception {
        HashMap map = new HashMap();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("titan", map);
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            System.out.println("Initialize DB");
            InitializeDB.initialize(entityManager);
            System.out.println();
            System.out.println();
            isNull(entityManager);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
            factory.close();
        }
    }

    public static void isNull(EntityManager manager) {
        System.out.println("THE WHERE CLAUSE AND IS NULL");
        System.out.println("--------------------------------");
        System.out.println("SELECT c FROM Customer c");
        System.out.println("WHERE c.address IS NULL");
        Query query;
        query = manager.createQuery("SELECT c FROM Customer c " + "WHERE c.address IS NULL");
        List customers = query.getResultList();
        Iterator it = customers.iterator();
        while (it.hasNext()) {
            Customer cust = (Customer) it.next();
            System.out.println("   " + cust.getFirstName());
        }
        System.out.println("");
        System.out.println("SELECT c FROM Customer c");
        System.out.println("WHERE c.address IS NOT NULL");
        query = manager.createQuery("SELECT c FROM Customer c " + "WHERE c.address IS NOT NULL");
        customers = query.getResultList();
        it = customers.iterator();
        while (it.hasNext()) {
            Customer cust = (Customer) it.next();
            System.out.println("   " + cust.getFirstName());
        }
        System.out.println("");
    }
}
