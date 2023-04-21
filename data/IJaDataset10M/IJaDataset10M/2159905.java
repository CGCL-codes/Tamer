package net.nothinginteresting.datamappers2.rdbms.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.nothinginteresting.datamappers2.DatamappersException;
import net.nothinginteresting.datamappers2.DomainObject;
import net.nothinginteresting.datamappers2.DomainObjectValueSetter;
import net.nothinginteresting.datamappers2.Field;
import net.nothinginteresting.datamappers2.models.ClassField;
import net.nothinginteresting.datamappers2.models.SchemaClass;
import net.nothinginteresting.datamappers2.query.Query;
import net.nothinginteresting.datamappers2.rdbms.RDBMS;
import org.apache.log4j.Logger;

/**
 * @author Dmitriy Gorbenko
 * 
 */
public class OracleRDBMS extends RDBMS {

    private static final Logger logger = Logger.getLogger(OracleRDBMS.class);

    private final Connection connection;

    private final Formatter formatter;

    public OracleRDBMS(Connection connection) {
        super();
        this.connection = connection;
        this.formatter = new Formatter();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public <T extends DomainObject> List<T> select(Query<? super T> query, SchemaClass schemaClass, Class<T> klass, DomainObjectValueSetter valueSetter) throws DatamappersException {
        ClassReferenceMapper<T> mapper = new ClassReferenceMapper<T>();
        String sql = formatter.formatQuery(query, schemaClass, mapper);
        ResultSet rs = getResultSet(sql);
        try {
            List<T> result = new ArrayList<T>();
            while (rs.next()) {
                T object = klass.newInstance();
                Map<String, ClassField> allFields = schemaClass.getAllFields();
                for (ClassField classField : allFields.values()) {
                    Field<T, Object> field = new Field<T, Object>(classField);
                    String columnAlias = mapper.getFieldSqlAlias(field);
                    Object value = getFieldValue(columnAlias, rs);
                    logger.debug(columnAlias + " = " + value);
                    valueSetter.setValue(object, field, value);
                }
                result.add(object);
            }
            return result;
        } catch (Exception e) {
            throw new DatamappersException("Error doing select", e);
        } finally {
            Statement stmt;
            try {
                stmt = rs.getStatement();
                rs.close();
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    private Object getFieldValue(String selectName, ResultSet rs) throws SQLException {
        return rs.getObject(selectName);
    }

    private ResultSet getResultSet(String sql) throws DatamappersException {
        logger.debug(sql);
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet result = null;
            try {
                result = stmt.executeQuery(sql);
            } catch (SQLException e) {
                stmt.close();
                throw e;
            }
            return result;
        } catch (SQLException e) {
            throw new DatamappersException(e);
        }
    }
}
