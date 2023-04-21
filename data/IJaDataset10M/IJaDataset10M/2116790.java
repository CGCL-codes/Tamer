package org.mariella.persistence.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mariella.persistence.query.Literal;

public interface Converter<T> {

    public void setObject(PreparedStatement ps, int index, int type, T value) throws SQLException;

    public T getObject(ResultSet rs, int index) throws SQLException;

    public Literal<T> createLiteral(Object value);

    public String toString(T value);
}
