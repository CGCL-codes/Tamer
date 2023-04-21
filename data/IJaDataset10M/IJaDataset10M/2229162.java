package net.persister.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.persister.mediator.Mediator;

/**
 * @author Park, chanwook
 *
 */
public interface Type {

    /**
	 * Type name In specification
	 *
	 * @return
	 */
    public String getName();

    /**
	 * Type class in java
	 *
	 * @return type class
	 */
    public Class getClassOfRealType();

    /**
	 * Type name of using Database in now
	 *
	 * @param mediator
	 * @return type name
	 */
    public String getSqlTypeForDB(Mediator mediator);

    public int getSqlType();

    public void set(PreparedStatement statement, int index, Object value) throws SQLException;

    public Object valueOf(Object value);

    public Object deepCopy(Object value);

    public Object get(ResultSet resultSet, String columnName, Mediator mediator);

    public Object get(ResultSet resultSet, int index, Mediator mediator);

    public Object initialValue();
}
