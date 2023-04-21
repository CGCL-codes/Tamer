package com.sun.j2ee.blueprints.signon.dao;

/**
 * This interface provides methods to view and modify sign on information for a
 * particular user. All the data source access code will be encapsulated in the
 * concrete implementations of this interface following the Data Access Object
 * pattern.
 */
public interface UserDAO {

    public void createUser(String userName, String password) throws SignOnDAODupKeyException;

    public boolean matchPassword(String userName, String password) throws SignOnDAOFinderException, InvalidPasswordException;
}
