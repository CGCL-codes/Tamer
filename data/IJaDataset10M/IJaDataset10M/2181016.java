package org.jf.lib.jdbchelper.dbobjects.annotation;

import org.jf.lib.jdbchelper.api.IResultSetHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.jf.lib.jdbchelper.api.HelperObjectException;
import org.jf.lib.jdbchelper.api.util.IObjectConstructionHelper;

/**
 * <p>The <code>DefaultResultSetHelper </code>will create a result object from a ResultSet row.  Classes are cached, so that the constructor is only looked up
 * the first time that <code>getResultSet</code> is called for a particular object.</p>
 * <p>Objects of this class are not internally synchronised, hence not thread-safe.</p>
 * 
 * @author Jim Foster <jdfcm73uk@gmail.com>
 */
public class AnnotationResultSetHelper implements IResultSetHelper {

    /**The reverse lookup for enums.*/
    private Map<Class<?>, Method> enumReverseLookupMap;

    private ResourceBundle helperBundle;

    /**Keep track of the argument ordinal of the result tuple, as object parameters entail recursive behaviour.*/
    private int resultArgumentCounter;

    private boolean infGather = true;

    private SerialisationInf inf = new SerialisationInf();

    /**
   * <p>Construct a new <code>DefaultResultSetHelper </code>object.  The new object will maintain its own constructor cache.</p>
   */
    public AnnotationResultSetHelper() {
        enumReverseLookupMap = new HashMap<Class<?>, Method>();
        ResourceBundle.clearCache();
        helperBundle = ResourceBundle.getBundle("org/jf/lib/jdbchelper/dbobjects/annotation/resultsethelper");
    }

    private Object getReverseLookupEnum(Class<?> enumClass, ResultSet rs, int columnIndex) throws SQLException {
        Method reverseLookup = enumReverseLookupMap.get(enumClass);
        int reverseLookupMethods = 0;
        if (reverseLookup == null) {
            for (Method m : enumClass.getDeclaredMethods()) {
                if (m.getAnnotation(ResultEnumLookup.class) != null) {
                    reverseLookup = m;
                    reverseLookupMethods++;
                }
            }
            if (reverseLookup == null) {
                throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_REVERSELOOKUPNOMETHOD"), enumClass.getName()));
            }
            if (reverseLookupMethods > 1) {
                throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_REVERSELOOKUPMULTIMETHODS"), enumClass.getName(), reverseLookupMethods));
            }
            if (reverseLookup.getParameterTypes().length != 1) {
                throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_REVERSELOOKUPSIGNATUREERROR"), enumClass.getName(), reverseLookup.getName(), reverseLookup.getParameterTypes().length));
            }
            enumReverseLookupMap.put(enumClass, reverseLookup);
        }
        Object lookupKey = rs.getObject(columnIndex);
        try {
            return reverseLookup.invoke(null, lookupKey);
        } catch (IllegalAccessException ex) {
            throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_ACCESS"), enumClass.getName(), reverseLookup.getName()), ex);
        } catch (IllegalArgumentException ex) {
            throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_ILLEGALARG"), enumClass.getName(), reverseLookup.getName(), columnIndex, rs.getMetaData().getColumnName(columnIndex), lookupKey), ex);
        } catch (InvocationTargetException ex) {
            throw new HelperObjectException(MessageFormat.format(helperBundle.getString("ENUM_INVOKEEXCEPTION"), enumClass.getName(), reverseLookup.getName()), ex);
        }
    }

    /**
   * <p>Go through the columns from the start column to create an object of the given type.</p>
   * 
   * @param <T> The parameterised type of the return object.
   * @param rs The underlying result set to get the object data from.
   * @param resultObjectClass The <code>Class</code> of the result object.
   * @param startColumn the <code>ResultSet</code> column to start getting result data from.  This is used if we need to start deserialising at a column other than the first one.
   * @return 
   */
    @SuppressWarnings("unchecked")
    private <T> T getObject(ResultSet rs, Class<T> resultObjectClass, int startColumn, int level, IObjectConstructionHelper helper) throws SQLException {
        List<Object> constructorArgs = new ArrayList<Object>();
        if (infGather) {
            inf.startObject(resultObjectClass, level);
        }
        for (Class<?> cl : helper.getConstructionArguments(resultObjectClass)) {
            int columnIndex = resultArgumentCounter + startColumn;
            if (cl.isEnum()) {
                if (infGather) {
                    inf.objectParameter(rs.getMetaData().getColumnName(columnIndex), columnIndex, level);
                }
                constructorArgs.add(getReverseLookupEnum(cl, rs, columnIndex));
                resultArgumentCounter++;
            } else if (DBObjectUtil.isConcreteClass(cl)) {
                if (infGather) {
                    inf.objectParameter(rs.getMetaData().getColumnName(columnIndex), columnIndex, level);
                }
                constructorArgs.add(rs.getObject(columnIndex));
                resultArgumentCounter++;
            } else {
                constructorArgs.add(getObject(rs, cl, startColumn, level + 1, helper));
            }
        }
        return (T) helper.createObject(resultObjectClass, constructorArgs);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public <T> T getResultSetObject(ResultSet rs, Class<T> resultObjectClass, IObjectConstructionHelper constructionHelper) throws SQLException, HelperObjectException {
        return getResultSetObject(rs, resultObjectClass, 1, constructionHelper);
    }

    /**
   * {@inheritDoc}
   */
    public <T> T getResultSetObject(ResultSet rs, Class<T> resultObjectClass, int columnStart, IObjectConstructionHelper constructionHelper) throws SQLException, HelperObjectException {
        inf.reset();
        resultArgumentCounter = 0;
        return getObject(rs, resultObjectClass, columnStart, 0, constructionHelper);
    }
}
