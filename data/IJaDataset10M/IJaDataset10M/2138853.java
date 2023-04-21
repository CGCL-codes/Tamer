package net.sourceforge.listel.model.group.dao;

import java.sql.Connection;
import java.util.List;
import net.sourceforge.listel.model.group.Group;
import net.sourceforge.listel.model.util.exception.DataAccessException;

public interface GroupDao {

    public Group create(Connection connection, Group group) throws DataAccessException;

    public Group get(Connection connection, long id) throws DataAccessException;

    public Group getByName(Connection connection, String name) throws DataAccessException;

    public void update(Connection connection, Group group) throws DataAccessException;

    public void delete(Connection connection, long id) throws DataAccessException;

    public List<Group> findAll(Connection connection) throws DataAccessException;
}
