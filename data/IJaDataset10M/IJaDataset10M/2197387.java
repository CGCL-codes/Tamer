package net.sourceforge.listel.model.contact.dao;

import java.sql.Connection;
import java.util.List;
import net.sourceforge.listel.model.contact.Contact;
import net.sourceforge.listel.model.util.exception.DataAccessException;

public interface ContactDao {

    public Contact create(Connection connection, Contact contact) throws DataAccessException;

    public Contact get(Connection connection, long id) throws DataAccessException;

    public Contact getByAlias(Connection connection, String alias) throws DataAccessException;

    public void update(Connection connection, Contact contact) throws DataAccessException;

    public void delete(Connection connection, long id) throws DataAccessException;

    public List<Contact> findAll(Connection connection) throws DataAccessException;
}
