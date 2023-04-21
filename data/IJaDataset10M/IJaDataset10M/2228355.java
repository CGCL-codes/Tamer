package jaxlib.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import jaxlib.lang.Longs;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.usertype.UserVersionType;

/**
 * @author  jw
 * @version $Id: VersionLongType.java 3051 2012-02-13 01:37:48Z joerg_wassmer $
 * @since   JaXLib 1.0
 */
public class VersionLongType extends Object implements UserVersionType, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static final String NAME = "jaxlib.hibernate.type.VersionLongType";

    private static final VersionLongType instance = new VersionLongType();

    public static VersionLongType getInstance() {
        return instance;
    }

    public VersionLongType() {
        super();
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private Object readResolve() {
        return (getClass() == VersionLongType.class) ? instance : this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final int compare(final Object a, final Object b) {
        if (a == b) return 0; else if (a == null) return -1; else if (b == null) return 1; else return ((Long) a).compareTo((Long) b);
    }

    @Override
    public final boolean equals(final Object o) {
        return (o == this) || ((o != null) && (getClass() == o.getClass()));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public final Object assemble(final Serializable cached, final Object owner) {
        return cached;
    }

    @Override
    public final Object deepCopy(final Object v) {
        return v;
    }

    @Override
    public final Serializable disassemble(final Object value) {
        return (Serializable) value;
    }

    @Override
    public final boolean equals(final Object a, final Object b) {
        return (a == b) || ((a != null) && a.equals(b));
    }

    @Override
    public final int hashCode(final Object o) {
        return (o != null) ? o.hashCode() : 0;
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public Object next(final Object object, final SessionImplementor sessionImplementor) {
        return null;
    }

    @Override
    public final Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws SQLException {
        final Object x = rs.getObject(names[0]);
        if (x == null) return null;
        if (x instanceof Long) return (Long) x;
        if (x instanceof Integer) return Longs.box(((Integer) x).longValue());
        return Longs.box(rs.getLong(names[0]));
    }

    @Override
    public final void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws SQLException {
        st.setObject(index, value);
    }

    @Override
    public final Object replace(final Object original, final Object target, final Object owner) {
        return original;
    }

    @Override
    public final Class returnedClass() {
        return Long.class;
    }

    @Override
    public Object seed(final SessionImplementor sessionImplementor) {
        return null;
    }

    @Override
    public final int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }
}
