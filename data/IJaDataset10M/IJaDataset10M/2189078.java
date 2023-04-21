package com.manning.gwtip.user.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.manning.gwtip.user.client.User;
import com.manning.gwtip.user.client.UserService;
import com.manning.gwtip.user.client.UserServiceException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class UserServiceServlet extends RemoteServiceServlet implements UserService {

    private transient EntityManagerFactory factory;

    public UserServiceServlet() {
        super();
        factory = Persistence.createEntityManagerFactory("user-service");
    }

    public void createUser(User user) throws UserServiceException {
        if (user.getUsername().equals("root")) {
            throw new UserServiceException("You can't be root!");
        }
        try {
            EntityManager mgr = factory.createEntityManager();
            mgr.getTransaction().begin();
            mgr.persist(user);
            mgr.getTransaction().commit();
        } catch (javax.persistence.RollbackException e) {
            throw new UserServiceException("That username is taken. Try another!");
        } catch (PersistenceException p) {
            throw new UserServiceException("An unexpected error occurred: " + p.toString());
        }
    }
}
