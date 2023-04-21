package edu.upmc.opi.caBIG.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import oracle.sql.CLOB;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import com.mchange.v2.c3p0.dbms.OracleUtils;

/**
 * Implementation of Oracle's CLOB handling.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: OraclePlainTextType.java,v 1.2 2010/05/27 20:50:05 girish_c1980
 *          Exp $
 * @since 1.4.2_04
 */
public class OraclePlainTextType implements UserType {

    protected static Log logger = LogFactory.getLog(OraclePlainTextType.class);

    public int[] sqlTypes() {
        return new int[] { Types.CLOB };
    }

    /**
	 * Null safe get.
	 * 
	 * @param owner
	 *            the owner
	 * @param names
	 *            the names
	 * @param rs
	 *            the rs
	 * 
	 * @return the object
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * 
	 * @see net.sf.hibernate.UserType#nullSafeGet(java.sql.ResultSet,
	 *      java.lang.String[], java.lang.Object)
	 */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException {
        String result = rs.getString(names[0]);
        return result;
    }

    /**
	 * oracleClasses independent (at compile time); based on
	 * http://forum.hibernate.org/viewtopic.php?p=2173150, changes: changed
	 * line: Connection conn = ps.getConnection(); to: Connection conn =
	 * dbMetaData.getConnection();
	 * 
	 * @param index
	 *            the index
	 * @param ps
	 *            the ps
	 * @param value
	 *            the value
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws HibernateException
	 *             the hibernate exception
	 * 
	 * @see net.sf.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement,
	 *      java.lang.Object, int)
	 */
    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws SQLException, HibernateException {
        if (value == null) {
            ps.setNull(index, sqlTypes()[0]);
            return;
        }
        logger.info("Called with value of type " + value.getClass().getName());
        ps.setString(index, String.valueOf(value));
    }

    public Object deepCopy(Object value) {
        if (value == null) {
            return null;
        }
        return new String((String) value);
    }

    public boolean isMutable() {
        return false;
    }

    public Class returnedClass() {
        return String.class;
    }

    public boolean equals(Object x, Object y) {
        return ObjectUtils.equals(x, y);
    }

    public int hashCode(Object arg0) throws HibernateException {
        return 0;
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
