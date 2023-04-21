package com.sun.j2ee.blueprints.signon.dao;

import com.sun.j2ee.blueprints.test.MockHolder;
import com.sun.j2ee.blueprints.util.dao.DAOFactory;

/**
 * This class delegates to a mock registered in the {@link MockHolder} that way
 * we can bypass the instantiation strategy used by the {@link DAOFactory}.
 * 
 * In the TestCase 1. Create a Mock of the UserDAO 2. Register it with the
 * MockHolder 3. record the expected behavior 4. Execute the test method 5.
 * cleanup
 * 
 * Note: For testing with mocks only.
 * 
 * @author Marten Deinum
 * 
 */
public class MockDelegatingUserDao implements UserDAO {

    @Override
    public void createUser(String userName, String password) throws SignOnDAODupKeyException {
        UserDAO delegate = MockHolder.getMock(UserDAO.class);
        delegate.createUser(userName, password);
    }

    @Override
    public boolean matchPassword(String userName, String password) throws SignOnDAOFinderException, InvalidPasswordException {
        UserDAO delegate = MockHolder.getMock(UserDAO.class);
        return delegate.matchPassword(userName, password);
    }
}
