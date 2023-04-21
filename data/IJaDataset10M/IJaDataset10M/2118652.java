package org.exolab.jms.tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.jms.persistence.PersistenceException;
import org.exolab.jms.persistence.SQLHelper;

/**
 * A helper class for managing the set of types supported by an RDBMS
 *
 * @version     $Revision: 1.1 $ $Date: 2004/11/26 01:51:16 $
 * @author      <a href="mailto:tima@intalio.com">Tim Anderson</a>
 */
class TypeSet {

    /**
     * A map of type identifiers to an ArrayList of corresponding RDBMS types
     */
    private HashMap _types = new HashMap();

    /**
     * The logger
     */
    private static final Log _log = LogFactory.getLog(TypeSet.class);

    /**
     * Construct a new instance
     *
     * @param connection the database connection to obtain meta-data from
     * @throws PersistenceException if meta-data cannot be accessed
     */
    public TypeSet(Connection connection) throws PersistenceException {
        ResultSet set = null;
        try {
            set = connection.getMetaData().getTypeInfo();
            while (set.next()) {
                int type = set.getInt("DATA_TYPE");
                String name = set.getString("TYPE_NAME");
                long precision = set.getLong("PRECISION");
                String createParams = set.getString("CREATE_PARAMS");
                Descriptor descriptor = Descriptor.getDescriptor(type);
                if (descriptor != null) {
                    addType(type, name, precision, createParams);
                } else {
                    _log.debug("TypeSet: skipping unknown type, type id=" + type + ", name=" + name + ", precision=" + precision + ", create params=" + createParams);
                }
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Failed to get type meta-data", exception);
        } finally {
            SQLHelper.close(set);
        }
    }

    /**
     * Return the closest type matching the requested type id and precision
     *
     * @param type the type identifier
     * @param precision the requested precision
     * @return the closest matching type, or null if none exists
     */
    public Type getType(int type, long precision) {
        Type result = null;
        ArrayList types = (ArrayList) _types.get(new Integer(type));
        if (types != null) {
            Iterator iter = types.iterator();
            while (iter.hasNext()) {
                Type option = (Type) iter.next();
                if (precision == -1 && (option.getPrecision() != -1 && option.getParameters())) {
                    result = new Type(type, option.getName(), option.getPrecision(), option.getParameters());
                    break;
                } else if (precision <= option.getPrecision()) {
                    result = new Type(type, option.getName(), precision, option.getParameters());
                    break;
                } else {
                    _log.debug("TypeSet: requested type=" + type + " exceeds precision for supported " + option);
                }
            }
        } else {
            _log.debug("TypeSet: no types matching type id=" + type + ", type=" + Descriptor.getDescriptor(type).getName());
        }
        return result;
    }

    /**
     * Return the near type matching the supplied type id and precision.
     * This should only be invoked if the requested precision exceeds that
     * supported by the database.
     *
     * @param type the type identifier
     * @return the type, or null, if none exists
     */
    public Type getNearestType(int type, long precision) {
        Type result = null;
        ArrayList types = (ArrayList) _types.get(new Integer(type));
        if (types != null) {
            Iterator iter = types.iterator();
            Type nearest = null;
            while (iter.hasNext()) {
                Type option = (Type) iter.next();
                if (precision <= option.getPrecision()) {
                    result = new Type(type, option.getName(), precision, option.getParameters());
                    break;
                } else {
                    nearest = option;
                }
            }
            if (result == null && nearest != null) {
                result = new Type(type, nearest.getName(), nearest.getPrecision(), nearest.getParameters());
                _log.warn("TypeSet: requested type=" + type + ", precision=" + precision + " exceeds precision supported by database. " + "Falling back to " + nearest);
            }
        } else {
            _log.debug("TypeSet: no types matching type id=" + type + ", type=" + Descriptor.getDescriptor(type).getName());
        }
        return result;
    }

    /**
     * Returns true if the type is supported
     *
     * @param type the type identifier
     * @return <code>true</code> if the type is supported
     */
    public boolean exists(int type) {
        return _types.containsKey(new Integer(type));
    }

    private void addType(int type, String name, long precision, String createParams) {
        Descriptor descriptor = Descriptor.getDescriptor(type);
        boolean parameters = false;
        if (createParams != null && createParams.trim().length() != 0) {
            parameters = true;
        }
        Integer key = new Integer(type);
        ArrayList types = (ArrayList) _types.get(key);
        if (types == null) {
            types = new ArrayList();
            _types.put(key, types);
        }
        _log.debug("TypeSet: type id=" + type + ", type=" + descriptor.getName() + ", name=" + name + ", precision=" + precision + ", createParams=" + createParams);
        types.add(new Type(type, name, precision, parameters));
    }
}
