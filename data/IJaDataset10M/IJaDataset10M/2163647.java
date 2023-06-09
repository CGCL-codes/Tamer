package org.wicketrad.samples;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.wicket.protocol.http.WicketServlet;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.wicketrad.jpa.PersistenceManagerFactory;
import org.wicketrad.jpa.PersistenceUnit;
import org.wicketrad.samples.domain.Country;

public class SampleServer {

    public static void main(String[] args) throws Exception {
        PersistenceUnit.unitName = "wicketrad-samples";
        EntityManager em = PersistenceManagerFactory.getInstance().getEntityManager(PersistenceUnit.unitName);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        createCountries(em);
        tx.commit();
        em.close();
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });
        Context root = new Context(server, "/", Context.SESSIONS);
        ServletHolder holder = new ServletHolder();
        holder.setServlet(new WicketServlet());
        holder.setInitParameter("applicationClassName", "org.wicketrad.samples.SampleApplication");
        root.addServlet(holder, "/*");
        server.start();
        server.join();
    }

    private static void createCountries(EntityManager em) {
        Country c = new Country();
        c.setCurrency("Swedish Crown");
        c.setShortCurrency("SEK");
        c.setName("Sweden");
        c.setShortName("Sweden");
        em.persist(c);
        c = new Country();
        c.setCurrency("British Pound");
        c.setShortCurrency("GBP");
        c.setName("United Kingdom");
        c.setShortName("UK");
        em.persist(c);
        c = new Country();
        c.setCurrency("Elbonian Mud");
        c.setShortCurrency("EMD");
        c.setName("Elbonia");
        c.setShortName("Elbonia");
        em.persist(c);
        c = new Country();
        c.setCurrency("US Dollar");
        c.setShortCurrency("USD");
        c.setName("United States of America");
        c.setShortName("USA");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("France");
        c.setShortName("France");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Luxembourg");
        c.setShortName("Luxembourg");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Germany");
        c.setShortName("Germany");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Finland");
        c.setShortName("Finland");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Italy");
        c.setShortName("Italy");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Spain");
        c.setShortName("Spain");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("Belgium");
        c.setShortName("Belgium");
        em.persist(c);
        c = new Country();
        c.setCurrency("European Euros");
        c.setShortCurrency("EUR");
        c.setName("The Netherlands");
        c.setShortName("Holland");
        em.persist(c);
    }
}
