package com.domainlanguage.time;

import com.domainlanguage.time.MinuteOfHour;
import org.hibernate.HibernateException;
import org.hibernate.type.ImmutableType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Hibernate type for {@link com.domainlanguage.time.MinuteOfHour}.
 *
 * Backed by {@link java.sql.Types.TINYINT}.
 *
 */
public class MinuteOfHourType extends ImmutableType {

    public Object get(final ResultSet rs, final String name) throws HibernateException, SQLException {
        return MinuteOfHour.value(rs.getByte(name));
    }

    public void set(final PreparedStatement st, final Object value, final int index) throws HibernateException, SQLException {
        final MinuteOfHour minuteOfHour = (MinuteOfHour) value;
        st.setByte(index, (byte) minuteOfHour.value());
    }

    public int sqlType() {
        return Types.TINYINT;
    }

    public String toString(final Object value) throws HibernateException {
        final MinuteOfHour minuteOfHour = (MinuteOfHour) value;
        return String.valueOf(minuteOfHour.value());
    }

    public Object fromStringValue(final String xml) throws HibernateException {
        return MinuteOfHour.value(Integer.parseInt(xml));
    }

    public Class getReturnedClass() {
        return MinuteOfHour.class;
    }

    public String getName() {
        return "minute";
    }
}
