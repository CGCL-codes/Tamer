package net.sf.jpasecurity.samples.elearning.jsf.service;

import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arne Limburg
 */
@WebServlet("/entityManagerFactoryReset")
public class EntityManagerFactoryReset extends GenericServlet implements Servlet {

    public static final int OK = 200;

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EntityManagerFactory newEntityManagerFactory = Persistence.createEntityManagerFactory("elearning");
        EntityManagerFactory oldEntityManagerFactory = ElearningTransactionService.entityManagerFactory;
        ElearningTransactionService.entityManagerFactory = newEntityManagerFactory;
        oldEntityManagerFactory.close();
        ((HttpServletResponse) res).setStatus(OK);
    }
}
