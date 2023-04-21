package de.dao;

public class PersonDaoFactory {

    public static IPersonDao getInstance() throws DaoException {
        return new PersonDaoMySqlImpl();
    }
}
