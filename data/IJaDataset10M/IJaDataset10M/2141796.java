package com.f3p.dataprovider.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.f3p.dataprovider.DataProvider;
import com.f3p.dataprovider.DataProviderConst;
import com.f3p.dataprovider.DataProviderException;

public class JDBCDataProvider extends AbstractDataProvider implements DataProvider {

    private Log m_log = LogFactory.getLog(JDBCDataProvider.class);

    private ResultSet m_resultset;

    private List<String> m_lstFieldNames;

    private List<String> m_lstDates;

    private boolean m_bDontCloseConn = false;

    public JDBCDataProvider(JDBCDataProviderConnection dp, ResultSet rs) throws SQLException {
        super(dp);
        m_resultset = rs;
        ResultSetMetaData meta = rs.getMetaData();
        int iC = meta.getColumnCount();
        m_lstFieldNames = new ArrayList<String>();
        m_lstDates = new ArrayList<String>();
        for (int i = 0; i < iC; i++) {
            String sName = meta.getColumnLabel(i + 1).toLowerCase();
            int iType = meta.getColumnType(i + 1);
            m_lstFieldNames.add(sName);
            if (iType == Types.DATE || iType == Types.TIME || iType == Types.TIMESTAMP) m_lstDates.add(sName);
        }
        Collections.sort(m_lstFieldNames);
        Collections.sort(m_lstDates);
        m_bDontCloseConn = dp.getConfig().hasFlag(DataProviderConst.FLAG_DONT_CLOSE_CONNECTION);
    }

    public void release() throws DataProviderException {
        m_log.trace("[" + getName() + "] release");
        try {
            super.release();
            Statement stmt = m_resultset.getStatement();
            Connection con = stmt.getConnection();
            m_resultset.close();
            stmt.close();
            if (!m_bDontCloseConn) con.close();
        } catch (Exception e) {
            throw new DataProviderException(this, "JDBCDataProvider.release", e);
        }
    }

    public boolean next() throws DataProviderException {
        try {
            super.next();
            boolean bRet = m_resultset.next();
            setExhausted(!bRet);
            m_log.trace("[" + getName() + "] next: " + bRet);
            return bRet;
        } catch (SQLException e) {
            m_log.trace("[" + getName() + "] JDBCDataProvider.next", e);
            throw new DataProviderException(this, e);
        }
    }

    public String verifyField(String sField) {
        if (m_relationSource == null) return null;
        if (sField.startsWith(DataProvider.PARENT_PREFIX)) return sField.substring(PARENT_PREFIX.length());
        if (Collections.binarySearch(m_lstFieldNames, sField.toLowerCase()) >= 0) return null; else return sField;
    }

    protected Object getObjectInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                if (Collections.binarySearch(m_lstDates, sName.toLowerCase()) >= 0) {
                    return m_resultset.getTimestamp(sName);
                } else return m_resultset.getObject(sName);
            } else return m_relationSource.getObject(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Boolean getBooleanInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                boolean v = m_resultset.getBoolean(sName);
                if (m_resultset.wasNull()) return null; else return new Boolean(v);
            } else return m_relationSource.getBoolean(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Double getDoubleInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                double v = m_resultset.getDouble(sName);
                if (m_resultset.wasNull()) return null; else return new Double(v);
            } else return m_relationSource.getDouble(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Float getFloatInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                float v = m_resultset.getFloat(sName);
                if (m_resultset.wasNull()) return null; else return new Float(v);
            } else return m_relationSource.getFloat(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Integer getIntegerInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                int v = m_resultset.getInt(sName);
                if (m_resultset.wasNull()) return null; else return new Integer(v);
            } else return m_relationSource.getInteger(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Long getLongInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                long v = m_resultset.getLong(sName);
                if (m_resultset.wasNull()) return null; else return new Long(v);
            } else return m_relationSource.getLong(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Short getShortInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                short v = m_resultset.getShort(sName);
                if (m_resultset.wasNull()) return null; else return new Short(v);
            } else return m_relationSource.getShort(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected Calendar getCalendarInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) {
                Timestamp t = m_resultset.getTimestamp(sName);
                if (t != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(t.getTime());
                    return cal;
                } else return null;
            } else return m_relationSource.getCalendar(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    public InputStream getInputStream(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) return m_resultset.getBinaryStream(sName); else return m_relationSource.getInputStream(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    protected String getStringInt(String sName) throws DataProviderException {
        try {
            String sPF = verifyField(sName);
            if (sPF == null) return m_resultset.getString(sName); else return m_relationSource.getString(sPF);
        } catch (SQLException e) {
            throw new DataProviderException(this, e);
        }
    }

    public List<String> getColumnsName() throws DataProviderException {
        return null;
    }
}
