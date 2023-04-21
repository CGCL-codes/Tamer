package uk.ac.lkl.migen.system.server.manipulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.reflect.StaticInstanceMap;
import uk.ac.lkl.common.util.reflect.StaticInstanceMapManager;
import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.common.util.restlet.server.LookupTableManipulator;
import uk.ac.lkl.migen.system.ai.um.AbstractLearnerModelAttribute;
import uk.ac.lkl.migen.system.ai.um.AttributeValue;

public abstract class AbstractAttributeValueTableManipulator<A extends AbstractLearnerModelAttribute, V extends AttributeValue<A>> extends LookupTableManipulator<V> {

    private StaticInstanceMap<A> attributeMap;

    public AbstractAttributeValueTableManipulator(Connection connection, Class<A> attributeClass, Class<V> attributeValueClass, String tableName) throws SQLException, RestletException {
        super(connection, GenericClass.getSimple(attributeValueClass), tableName);
        try {
            attributeMap = StaticInstanceMapManager.getStaticInstanceMap(attributeClass);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Can't create static instance maps");
        }
    }

    @Override
    public final String[] getColumnNames() {
        String attributeIdColumnName = getAttributeIdColumnName();
        return new String[] { attributeIdColumnName, "value" };
    }

    protected abstract String getAttributeIdColumnName();

    @Override
    protected V extractObject(ResultSet resultSet, EntityMapper mapper) throws SQLException {
        String attributeIdColumnName = getAttributeIdColumnName();
        int attributeId = resultSet.getInt(attributeIdColumnName);
        A attribute = attributeMap.getInstance(attributeId);
        double value = resultSet.getDouble("value");
        return createNewAttributeValue(attribute, value);
    }

    protected abstract V createNewAttributeValue(A attribute, double value);

    @Override
    protected void populateInsertStatement(PreparedStatement statement, V attributeValue, EntityMapper mapper) throws SQLException {
        int attributeId = attributeValue.getAttribute().getId();
        double value = attributeValue.getValue();
        statement.setInt(1, attributeId);
        statement.setDouble(2, value);
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, V object) throws SQLException {
    }
}
