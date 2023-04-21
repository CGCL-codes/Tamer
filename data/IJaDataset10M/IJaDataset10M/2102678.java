package org.blueoxygen.cimande;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * @author Umar Khatab
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ActiveFlag implements UserType {

    private final int code;

    private ActiveFlag(int code) {
        this.code = code;
    }

    public static final ActiveFlag INACTIVE = new ActiveFlag(0);

    public static final ActiveFlag ACTIVE = new ActiveFlag(1);

    public int toInt() {
        return code;
    }

    public static ActiveFlag fromInt(int code) {
        switch(code) {
            case 0:
                return INACTIVE;
            case 1:
                return ACTIVE;
            default:
                throw new RuntimeException("Unknown Active Flag");
        }
    }

    public int[] sqlTypes() {
        return null;
    }

    public Class returnedClass() {
        return null;
    }

    public boolean equals(Object arg0, Object arg1) throws HibernateException {
        return false;
    }

    public int hashCode(Object arg0) throws HibernateException {
        return 0;
    }

    public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2) throws HibernateException, SQLException {
        return null;
    }

    public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2) throws HibernateException, SQLException {
    }

    public Object deepCopy(Object arg0) throws HibernateException {
        return null;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object arg0) throws HibernateException {
        return null;
    }

    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return null;
    }

    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return null;
    }
}
