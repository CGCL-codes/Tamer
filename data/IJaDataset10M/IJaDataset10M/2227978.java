package org.datanucleus.samples.jdo.many_many_attributed;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

public class Main {

    public static void main(String[] args) {
        System.out.println("DataNucleus Samples : M-N Relation (attributed)");
        System.out.println("===============================================");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        Customer cust1 = null;
        Customer cust2 = null;
        Supplier supp1 = null;
        Supplier supp2 = null;
        Supplier supp3 = null;
        System.out.println(">> Persisting some Customers and Suppliers");
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            cust1 = new Customer("DFG Stores");
            cust2 = new Customer("Kevins Cards");
            supp1 = new Supplier("Stationery Direct");
            supp2 = new Supplier("Grocery Wholesale");
            supp3 = new Supplier("Makro");
            pm.makePersistent(cust1);
            pm.makePersistent(cust2);
            pm.makePersistent(supp1);
            pm.makePersistent(supp2);
            pm.makePersistent(supp3);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println(">> Adding relationships between Customers and Suppliers");
        BusinessRelation rel1 = new BusinessRelation(cust1, supp2, "Friendly", "Hilton Hotel, London");
        cust1.addRelation(rel1);
        supp2.addRelation(rel1);
        BusinessRelation rel2 = new BusinessRelation(cust2, supp1, "Frosty", "M61 motorway service station junction 23");
        cust2.addRelation(rel2);
        supp1.addRelation(rel2);
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(rel1);
            pm.makePersistent(rel2);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println(">> Customer1 has " + cust1.getNumberOfRelations() + " relations");
        System.out.println(">> Customer2 has " + cust2.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier1 has " + supp1.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier2 has " + supp2.getNumberOfRelations() + " relations");
    }
}
