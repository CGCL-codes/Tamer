package net.sf.webwarp.util.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * Enables the usage of an arbitrary enum class for being mapped with hibernate.
 * 
 * @author mos
 * 
 * <pre>
 * 
 * &lt;typedef name=&quot;suit&quot; class='EnumUserType'&gt;
 *   &lt;param name=&quot;enumClassName&quot;&gt;com.company.project.Suit&lt;/param&gt;
 * &lt;/typedef&gt;
 * 
 * &lt;class ...&gt;
 *   &lt;property name='suit' type='suit'/&gt;
 * &lt;/class&gt;
 * 
 * </pre>
 */
@SuppressWarnings("unchecked")
public class EnumUserType extends AbstractUserType implements UserType, ParameterizedType {

    private Class<? extends Enum> clazz = null;

    public void setParameterValues(Properties params) {
        String enumClassName = params.getProperty("enumClassName");
        if (enumClassName == null) {
            throw new MappingException("enumClassName parameter not specified");
        }
        try {
            this.clazz = (Class<? extends Enum>) Class.forName(enumClassName);
        } catch (java.lang.ClassNotFoundException e) {
            throw new MappingException("enumClass " + enumClassName + " not found", e);
        }
    }

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return clazz;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);
        Object result = null;
        if (!resultSet.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, ((Enum) value).name());
        }
    }
}
