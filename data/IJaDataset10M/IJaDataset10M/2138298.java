package datadog.services.mappings;

import datadog.exceptions.DatadogRuntimeException;
import datadog.services.mappings.attribute.Attribute;
import datadog.services.values.ObjectRowsBuilder;
import datadog.services.values.ObjectUpdateBuilder;
import datadog.sessions.QueryContext;
import datadog.sessions.SessionProvider;
import datadog.sql.TableAliasMap;
import datadog.util.Validate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * All mappings that place a value from a table directly into the object should
 * extend DirectMappingService.<p>
 *
 * @author Justin Tomich
 * @version $Id: DirectMappingService.java,v 1.7 2006/03/14 05:45:38 tomichj Exp
 *          $
 */
public abstract class DirectMapping extends BaseMapping {

    protected final TypeService typeService;

    protected DirectMapping(Attribute attribute, TypeService typeService) {
        super(attribute);
        this.typeService = typeService;
    }

    /**
     * @param instance
     * @return Set of ColumnValue instances
     */
    private Set getColumnValues(Object instance) {
        Object value = getAttributeValue(instance);
        return typeService.getColumnValues(value);
    }

    public void getColumnValues(Object obj, ObjectRowsBuilder builder) {
        Object value = getAttributeValue(obj);
        builder.add(typeService.getColumnValues(value));
    }

    public void setAttribute(Object instance, ResultSet resultSet, SessionProvider session, TableAliasMap taMap, QueryContext context) {
        try {
            Object value = typeService.getAttributeValue(taMap, resultSet);
            setAttributeValue(value, instance);
        } catch (SQLException e) {
            throw new DatadogRuntimeException(e);
        }
    }

    public void cloneAttribute(Object source, Object target) {
        Object value = getAttributeValue(source);
        if (typeService.isMutable()) {
            value = typeService.cloneObject(value);
        }
        setAttributeValue(value, target);
    }

    public void diff(Object original, Object updated, ObjectUpdateBuilder builder) {
        Validate.notNull(updated);
        Validate.notNull(builder);
        Set originalVals = original != null ? getColumnValues(original) : null;
        Set updatedVals = getColumnValues(updated);
        if (original != null) {
            builder.addOriginalValues(originalVals);
            updatedVals.removeAll(originalVals);
        }
        builder.addUpdatedValues(updatedVals);
    }
}
