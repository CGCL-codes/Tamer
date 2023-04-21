package org.nakedobjects.nos.store.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.MultilineString;

/**
 * A user type that maps an SQL String to a NOF MultilineString value.
 */
public class MultilineStringType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException, SQLException {
        String string = rs.getString(names[0]);
        if (rs.wasNull()) return null;
        return new MultilineString(string);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.STRING.sqlType());
        } else {
            st.setString(index, ((MultilineString) value).getString());
        }
    }

    public Class returnedClass() {
        return MultilineString.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.STRING.sqlType() };
    }
}
