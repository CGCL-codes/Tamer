package hendrey.shades.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ghendrey
 */
public class BeanORMHelper {

    private Class beanClass;

    /** Creates a new instance of BeanORMHelper */
    public BeanORMHelper(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getTableName() {
        return beanClass.getSimpleName().toUpperCase();
    }

    public String[] getColumnNames() {
        Set columns = new LinkedHashSet();
        Method[] methods = beanClass.getMethods();
        Class returnType = null;
        for (Method m : methods) {
            returnType = m.getReturnType();
            if (m.getName().startsWith("get") || m.getName().startsWith("set")) {
                if (isImmutable(returnType)) {
                    String columnName = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
                    columns.add(columnName);
                }
            }
        }
        return (String[]) columns.toArray(new String[] {});
    }

    private static boolean isImmutable(Class returnType) {
        return returnType.isPrimitive() || java.lang.String.class.isAssignableFrom(returnType) || java.lang.Number.class.isAssignableFrom(returnType) || java.util.Date.class.isAssignableFrom(returnType);
    }

    public Object getColumn(String columnName, ResultSet rs) throws SQLException {
        return rs.getObject(columnName);
    }

    public void loadColumnIntoObject(String columnName, Object columnVal, Object pojo) {
        Method setter = getSetter(columnName, columnVal);
        try {
            setter.invoke(pojo, columnVal);
        } catch (IllegalArgumentException ex) {
            System.out.println("columnName:" + columnName + " columnVal:" + columnVal + " of type " + columnVal.getClass().getName());
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Object getColumnFromObject(String columnName, Object pojo) {
        Method getter = getGetter(columnName);
        try {
            return getter.invoke(pojo, (Object[]) null);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public String getSQLDatatype(String columnName) {
        Class type = getGetter(columnName).getReturnType();
        if (!isImmutable(type)) throw new RuntimeException("column named " + columnName + " is not of an immutable Class. It's Class is: " + type);
        if (String.class.isAssignableFrom(type)) return "VARCHAR";
        if (Integer.TYPE.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) return "INTEGER";
        if (Double.TYPE.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) return "DOUBLE";
        if (Float.TYPE.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) return "FLOAT";
        if (Short.TYPE.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) return "INTEGER";
        if (Long.TYPE.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) return "BIGINT";
        if (Boolean.TYPE.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) return "VARCHAR";
        if (Date.class.isAssignableFrom(type)) return "DATE";
        throw new RuntimeException("unable to getSQLDatatype for column named " + columnName + "  It's Class is: " + type);
    }

    private Method getSetter(String columnName, Object val) {
        columnName = unalias(columnName);
        for (Method m : beanClass.getMethods()) {
            if (m.getName().equalsIgnoreCase("set" + columnName)) {
                return m;
            }
        }
        throw new RuntimeException("no setter method could be found in " + beanClass.getName() + " for column " + columnName);
    }

    private Method getGetter(String columnName) {
        columnName = unalias(columnName);
        for (Method m : beanClass.getMethods()) {
            if (m.getName().equalsIgnoreCase("get" + columnName)) {
                return m;
            }
        }
        throw new RuntimeException("no setter method could be found in " + beanClass.getName() + " for column " + columnName);
    }

    public static String unalias(String columnName) {
        int idx = columnName.lastIndexOf(".");
        if (-1 != idx) {
            return columnName.substring(idx + 1);
        } else {
            return columnName;
        }
    }
}
