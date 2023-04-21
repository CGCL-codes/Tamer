package com.adserversoft.flexfuse.server.api;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.SortedSet;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 */
public class ByteArrayType implements UserType {

    /**
     * {@inheritDoc}
     */
    public Object nullSafeGet(ResultSet rs, String names[], Object owner) throws SQLException {
        byte[] bbs = (byte[]) Hibernate.BINARY.nullSafeGet(rs, names);
        Object countries = new Object();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bbs);
            ObjectInputStream ois = new ObjectInputStream(bais);
            countries = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return countries;
    }

    /**
     * {@inheritDoc}
     */
    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws SQLException, HibernateException {
        byte[] bbs = new byte[] {};
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            oos.close();
            baos.close();
            bbs = baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Hibernate.BINARY.nullSafeSet(ps, bbs, index);
    }

    /**
     * {@inheritDoc}
     */
    public Object deepCopy(Object value) {
        Object countries = value;
        Object clone = new Object();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(countries);
            oos.close();
            baos.close();
            byte[] bbs = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(bbs);
            ObjectInputStream ois = new ObjectInputStream(bais);
            clone = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMutable() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    /**
     * {@inheritDoc}
     */
    public Class returnedClass() {
        return byte[].class;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object x, Object y) {
        if (x == null || y == null) return false;
        return x.equals(y);
    }

    /**
     * {@inheritDoc}
     */
    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Serializable disassemble(Object arg0) throws HibernateException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode(Object arg0) throws HibernateException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return deepCopy(arg0);
    }
}
