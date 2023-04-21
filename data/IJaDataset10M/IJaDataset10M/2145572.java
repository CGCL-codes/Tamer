package com.mycorp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.click.util.ClickUtils;
import com.mycorp.domain.Customer;
import com.mycorp.service.CustomerService;

public class DatabaseInitListener implements ServletContextListener {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {
            loadDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating database", e);
        }
    }

    /**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /**
	 * Load data files into the database
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
    private void loadDatabase() throws IOException {
        CustomerService service = new CustomerService();
        if (service.getCustomers().size() > 0) {
            return;
        }
        loadCustomers();
        System.out.println("Database loaded");
    }

    private void deleteDatabase() {
        EntityManager em = null;
        try {
            CustomerService service = new CustomerService();
            List<Customer> customers = service.getCustomers();
            for (Customer customer : customers) {
                EntityManager localEm = EMF.getEMF().createEntityManager();
                try {
                    localEm.remove(localEm.find(Customer.class, customer.getId()));
                } finally {
                    localEm.close();
                }
            }
            EMF.setEM(null);
            System.out.println("Database deleted.");
        } finally {
            try {
                if (em != null) {
                    if (em.isOpen()) {
                        if (em.getTransaction().isActive()) {
                            System.out.println("Rolling back the transaction");
                            em.getTransaction().rollback();
                        }
                        em.close();
                    } else {
                        System.out.println("EntityManager is already closed!");
                    }
                }
            } catch (Throwable t) {
                System.out.println("Error closing EntityManager");
                t.printStackTrace();
            }
        }
    }

    private static void loadFile(String filename, LineProcessor lineProcessor) throws IOException {
        InputStream is = ClickUtils.getResourceAsStream(filename, DatabaseInitListener.class);
        if (is == null) {
            throw new RuntimeException("classpath file not found: " + filename);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) {
                    lineProcessor.processLine(line);
                }
                line = reader.readLine();
            }
        } finally {
            ClickUtils.close(reader);
        }
    }

    private static void loadCustomers() throws IOException {
        loadFile("customers.txt", new LineProcessor() {

            public void processLine(String line) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                Customer customer = new Customer();
                customer.setName(next(tokenizer));
                if (tokenizer.hasMoreTokens()) {
                    customer.setEmail(next(tokenizer));
                }
                if (tokenizer.hasMoreTokens()) {
                    customer.setAge(Integer.valueOf(next(tokenizer)));
                }
                if (tokenizer.hasMoreTokens()) {
                    customer.setInvestments(next(tokenizer));
                }
                if (tokenizer.hasMoreTokens()) {
                    customer.setHoldings(Double.valueOf(next(tokenizer)));
                }
                if (tokenizer.hasMoreTokens()) {
                    customer.setDateJoined(createDate(next(tokenizer)));
                }
                if (tokenizer.hasMoreTokens()) {
                    customer.setActive(Boolean.valueOf(next(tokenizer)));
                }
                EntityManager em = EMF.getEM();
                try {
                    em.persist(customer);
                } finally {
                    em.close();
                }
            }
        });
    }

    private static String next(StringTokenizer tokenizer) {
        String token = tokenizer.nextToken().trim();
        if (token.startsWith("\"")) {
            token = token.substring(1);
        }
        if (token.endsWith("\"")) {
            token = token.substring(0, token.length() - 1);
        }
        return token;
    }

    private static Date createDate(String pattern) {
        try {
            return FORMAT.parse(pattern);
        } catch (ParseException pe) {
            return null;
        }
    }

    private static interface LineProcessor {

        public void processLine(String line);
    }
}
