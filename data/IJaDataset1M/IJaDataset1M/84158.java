package org.endeavour.mgmt.model.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

@SuppressWarnings("unchecked")
public class BinaryBlobType implements UserType {

    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    public Class returnedClass() {
        return byte[].class;
    }

    public boolean equals(Object aX, Object aY) {
        return (aX == aY) || (aX != null && aY != null && java.util.Arrays.equals((byte[]) aX, (byte[]) aY));
    }

    public Object nullSafeGet(ResultSet aRs, String[] aNames, Object aOwner) throws HibernateException, SQLException {
        return aRs.getBytes(aNames[0]);
    }

    public void nullSafeSet(PreparedStatement aSt, Object aValue, int aIndex) throws HibernateException, SQLException {
        aSt.setBytes(aIndex, (byte[]) aValue);
    }

    public Object deepCopy(Object aValue) {
        Object theResult = null;
        if (aValue != null) {
            byte[] theBytes = (byte[]) aValue;
            theResult = new byte[theBytes.length];
            System.arraycopy(theBytes, 0, theResult, 0, theBytes.length);
        }
        return theResult;
    }

    public boolean isMutable() {
        return true;
    }

    public int hashCode(Object aObject) {
        return 0;
    }

    public Object assemble(Serializable aSerializable, Object aObject) {
        return null;
    }

    public Object replace(Object aArg1, Object aArg2, Object aArg3) {
        return null;
    }

    public Serializable disassemble(Object aArg1) {
        return null;
    }
}
