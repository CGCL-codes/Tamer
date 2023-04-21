package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.Time;

/**
 * A user type that maps an SQL TIME to a NOF Time value.
 */
public class TimeType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException, SQLException {
        final java.sql.Time time = rs.getTime(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Time(time);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.TIME.sqlType());
        } else {
            final Time nofTime = (Time) value;
            final java.sql.Time sqlTime = new java.sql.Time(nofTime.dateValue().getTime());
            st.setTime(index, sqlTime);
        }
    }

    public Class<Time> returnedClass() {
        return Time.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.TIME.sqlType() };
    }
}
