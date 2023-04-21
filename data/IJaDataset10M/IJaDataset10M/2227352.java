package self.micromagic.eterna.sql.impl;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import self.micromagic.coder.Base64;
import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.digester.FactoryManager;
import self.micromagic.eterna.model.AppData;
import self.micromagic.eterna.sql.PreparedStatementWrap;
import self.micromagic.eterna.sql.SQLAdapter;
import self.micromagic.eterna.sql.SpecialLog;
import self.micromagic.eterna.sql.preparer.BooleanPreparer;
import self.micromagic.eterna.sql.preparer.BytePreparer;
import self.micromagic.eterna.sql.preparer.BytesPreparer;
import self.micromagic.eterna.sql.preparer.DatePreparer;
import self.micromagic.eterna.sql.preparer.DoublePreparer;
import self.micromagic.eterna.sql.preparer.FloatPreparer;
import self.micromagic.eterna.sql.preparer.IntegerPreparer;
import self.micromagic.eterna.sql.preparer.LongPreparer;
import self.micromagic.eterna.sql.preparer.NullPreparer;
import self.micromagic.eterna.sql.preparer.ObjectPreparer;
import self.micromagic.eterna.sql.preparer.ReaderPreparer;
import self.micromagic.eterna.sql.preparer.ShortPreparer;
import self.micromagic.eterna.sql.preparer.StreamPreparer;
import self.micromagic.eterna.sql.preparer.StringPreparer;
import self.micromagic.eterna.sql.preparer.TimePreparer;
import self.micromagic.eterna.sql.preparer.TimestampPreparer;
import self.micromagic.util.FormatTool;
import self.micromagic.util.Utility;

public class SQLAdapterImpl extends AbstractSQLAdapter {

    protected static int SQL_LOG_TYPE = 0;

    protected int sqlLogType = 0;

    static {
        try {
            Utility.addMethodPropertyManager(SQL_LOG_PROPERTY, SQLAdapterImpl.class, "setSQLLogType");
        } catch (Throwable ex) {
            log.warn("Error in init sql log type.", ex);
        }
    }

    public int getLogType() {
        return this.sqlLogType | SQL_LOG_TYPE;
    }

    public void setLogType(String logType) {
        this.sqlLogType = parseLogType(logType);
    }

    protected static void setSQLLogType(String type) {
        SQL_LOG_TYPE = parseLogType(type);
    }

    private static int parseLogType(String logType) {
        try {
            return Integer.parseInt(logType);
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("SQL log type [" + logType + "] isn't a number.", ex);
            }
            StringTokenizer token = new StringTokenizer(logType, ", ");
            int tmpType = 0;
            while (token.hasMoreTokens()) {
                String tmp = token.nextToken().trim();
                if ("".equals(tmp)) {
                    continue;
                }
                if ("save".equals(tmp)) {
                    tmpType |= SQL_LOG_TYPE_SAVE;
                } else if ("print".equals(tmp)) {
                    tmpType |= SQL_LOG_TYPE_PRINT;
                } else if ("special".equals(tmp)) {
                    tmpType |= SQL_LOG_TYPE_SPECIAL;
                } else if ("none".equals(tmp)) {
                    tmpType |= SQL_LOG_TYPE_NONE;
                }
            }
            return tmpType;
        }
    }

    private static void logSQL(SQLAdapter sql, long usedTime, Throwable exception, Element logNode) throws SQLException, ConfigurationException {
        logNode.addAttribute("name", sql.getName());
        logNode.addAttribute("time", FormatTool.formatDatetime(new java.util.Date(System.currentTimeMillis())));
        logNode.addAttribute("usedTime", String.valueOf(usedTime));
        if (exception != null) {
            logNode.addElement("error").addText(exception.toString());
        }
        logNode.addElement("prepared-sql").addText(sql.getPreparedSQL());
        Element params = logNode.addElement("parameters");
        ReadPreparedValue rpv = new ReadPreparedValue(params);
        sql.prepareValues(rpv);
    }

    /**
    * ��¼sql��־
    *
    * @param sql           Ҫ��¼��sql����������
    * @param adapterType   sql����������, ��: query, update
    * @param usedTime      sqlִ�����õ�ʱ��, ��λΪ����
    * @param exception     ִ��ʱ���ֵ��쳣
    * @param conn          ִ�д�sqlʹ�õ���ݿ�����
    * @return              �Ƿ񱣴���sql��־
    */
    protected static boolean logSQL(SQLAdapter sql, String adapterType, long usedTime, Throwable exception, Connection conn) throws SQLException, ConfigurationException {
        int logType = sql.getLogType();
        if (logType == 0 || logType == -1) {
            return false;
        }
        Element theLog;
        if ((logType & SQL_LOG_TYPE_SAVE) != 0) {
            theLog = FactoryManager.createLogNode(adapterType);
            if (AppData.getAppLogType() == 1) {
                Element nowNode = AppData.getCurrentData().getCurrentNode();
                if (nowNode != null) {
                    logSQL(sql, usedTime, exception, nowNode.addElement(adapterType));
                }
            }
        } else {
            theLog = DocumentHelper.createElement(adapterType);
        }
        logSQL(sql, usedTime, exception, theLog);
        if ((logType & SQL_LOG_TYPE_SPECIAL) != 0) {
            SpecialLog sl = sql.getFactory().getSpecialLog();
            if (sl != null) {
                sl.logSQL(sql, adapterType, theLog, usedTime, exception, conn);
            }
        }
        if ((logType & SQL_LOG_TYPE_PRINT) != 0) {
            log.info("sql log:\n" + theLog.asXML());
        }
        return (logType & SQL_LOG_TYPE_SAVE) != 0;
    }

    /**
    * ��¼sql��־
    *
    * @param adapterType   sql����������, ��: query, update
    * @param usedTime      sqlִ�����õ�ʱ��, ��λΪ����
    * @param exception     ִ��ʱ���ֵ��쳣
    * @param conn          ִ�д�sqlʹ�õ���ݿ�����
    * @return              �Ƿ񱣴���sql��־
    */
    protected boolean logSQL(String adapterType, long usedTime, Throwable exception, Connection conn) throws SQLException, ConfigurationException {
        return logSQL(this, adapterType, usedTime, exception, conn);
    }

    public void execute(Connection conn) throws ConfigurationException, SQLException {
        this.logSQL("sql", 0L, null, conn);
    }

    protected Object clone() {
        SQLAdapterImpl other = (SQLAdapterImpl) super.clone();
        return other;
    }

    public void setNull(int parameterIndex, int sqlType) throws ConfigurationException {
        this.setValuePreparer(new NullPreparer(parameterIndex, sqlType));
    }

    public void setNull(String parameterName, int sqlType) throws ConfigurationException {
        this.setValuePreparer(new NullPreparer(this.getIndexByParameterName(parameterName), sqlType));
    }

    public void setBoolean(int parameterIndex, boolean x) throws ConfigurationException {
        this.setValuePreparer(new BooleanPreparer(parameterIndex, x));
    }

    public void setBoolean(String parameterName, boolean x) throws ConfigurationException {
        this.setValuePreparer(new BooleanPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setByte(int parameterIndex, byte x) throws ConfigurationException {
        this.setValuePreparer(new BytePreparer(parameterIndex, x));
    }

    public void setByte(String parameterName, byte x) throws ConfigurationException {
        this.setValuePreparer(new BytePreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setShort(int parameterIndex, short x) throws ConfigurationException {
        this.setValuePreparer(new ShortPreparer(parameterIndex, x));
    }

    public void setShort(String parameterName, short x) throws ConfigurationException {
        this.setValuePreparer(new ShortPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setInt(int parameterIndex, int x) throws ConfigurationException {
        this.setValuePreparer(new IntegerPreparer(parameterIndex, x));
    }

    public void setInt(String parameterName, int x) throws ConfigurationException {
        this.setValuePreparer(new IntegerPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setLong(int parameterIndex, long x) throws ConfigurationException {
        this.setValuePreparer(new LongPreparer(parameterIndex, x));
    }

    public void setLong(String parameterName, long x) throws ConfigurationException {
        this.setValuePreparer(new LongPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setFloat(int parameterIndex, float x) throws ConfigurationException {
        this.setValuePreparer(new FloatPreparer(parameterIndex, x));
    }

    public void setFloat(String parameterName, float x) throws ConfigurationException {
        this.setValuePreparer(new FloatPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setDouble(int parameterIndex, double x) throws ConfigurationException {
        this.setValuePreparer(new DoublePreparer(parameterIndex, x));
    }

    public void setDouble(String parameterName, double x) throws ConfigurationException {
        this.setValuePreparer(new DoublePreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setString(int parameterIndex, String x) throws ConfigurationException {
        this.setValuePreparer(new StringPreparer(parameterIndex, x));
    }

    public void setString(String parameterName, String x) throws ConfigurationException {
        this.setValuePreparer(new StringPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setBytes(int parameterIndex, byte[] x) throws ConfigurationException {
        this.setValuePreparer(new BytesPreparer(parameterIndex, x));
    }

    public void setBytes(String parameterName, byte[] x) throws ConfigurationException {
        this.setValuePreparer(new BytesPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setDate(int parameterIndex, Date x) throws ConfigurationException {
        this.setValuePreparer(new DatePreparer(parameterIndex, x));
    }

    public void setDate(String parameterName, Date x) throws ConfigurationException {
        this.setValuePreparer(new DatePreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setTime(int parameterIndex, Time x) throws ConfigurationException {
        this.setValuePreparer(new TimePreparer(parameterIndex, x));
    }

    public void setTime(String parameterName, Time x) throws ConfigurationException {
        this.setValuePreparer(new TimePreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws ConfigurationException {
        this.setValuePreparer(new TimestampPreparer(parameterIndex, x));
    }

    public void setTimestamp(String parameterName, Timestamp x) throws ConfigurationException {
        this.setValuePreparer(new TimestampPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setObject(int parameterIndex, Object x) throws ConfigurationException {
        this.setValuePreparer(new ObjectPreparer(parameterIndex, x));
    }

    public void setObject(String parameterName, Object x) throws ConfigurationException {
        this.setValuePreparer(new ObjectPreparer(this.getIndexByParameterName(parameterName), x));
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws ConfigurationException {
        this.setValuePreparer(new StreamPreparer(parameterIndex, x, length));
    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws ConfigurationException {
        this.setValuePreparer(new StreamPreparer(this.getIndexByParameterName(parameterName), x, length));
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws ConfigurationException {
        this.setValuePreparer(new ReaderPreparer(parameterIndex, reader, length));
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws ConfigurationException {
        this.setValuePreparer(new ReaderPreparer(this.getIndexByParameterName(parameterName), reader, length));
    }

    private static class ReadPreparedValue implements PreparedStatementWrap {

        private Element paramsRoot;

        private Base64 base64;

        public ReadPreparedValue(Element paramsRoot) {
            this.paramsRoot = paramsRoot;
        }

        private String getStr(byte[] buf) {
            if (this.base64 == null) {
                this.base64 = new Base64();
            }
            return this.base64.byteArrayToBase64(buf);
        }

        private void addParameterName(Element parameter, String parameterName) {
            if (parameterName != null && parameterName.length() > 0) {
                parameter.addAttribute("name", parameterName);
            }
        }

        public void setNull(String parameterName, int parameterIndex, int sqlType) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("sqlType", sqlType + "");
            parameter.addAttribute("isNull", "true");
        }

        public void setBoolean(String parameterName, int parameterIndex, boolean x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "boolean");
            parameter.addText(x ? "true" : "false");
        }

        public void setByte(String parameterName, int parameterIndex, byte x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "byte");
            parameter.addText(x + "");
        }

        public void setShort(String parameterName, int parameterIndex, short x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "short");
            parameter.addText(x + "");
        }

        public void setInt(String parameterName, int parameterIndex, int x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "int");
            parameter.addText(x + "");
        }

        public void setLong(String parameterName, int parameterIndex, long x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "long");
            parameter.addText(x + "");
        }

        public void setFloat(String parameterName, int parameterIndex, float x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "float");
            parameter.addText(x + "");
        }

        public void setDouble(String parameterName, int parameterIndex, double x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "double");
            parameter.addText(x + "");
        }

        public void setString(String parameterName, int parameterIndex, String x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "String");
            if (x != null) {
                parameter.addText(x);
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setBytes(String parameterName, int parameterIndex, byte x[]) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Bytes");
            if (x != null) {
                parameter.addText(this.getStr(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setDate(String parameterName, int parameterIndex, Date x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Date");
            if (x != null) {
                parameter.addText(FormatTool.formatDate(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setDate(String parameterName, int parameterIndex, Date x, Calendar cal) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Date");
            if (cal != null) {
                parameter.addAttribute("calendar", cal.toString());
            }
            if (x != null) {
                parameter.addText(FormatTool.formatDate(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setTime(String parameterName, int parameterIndex, Time x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Time");
            if (x != null) {
                parameter.addText(FormatTool.formatTime(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setTime(String parameterName, int parameterIndex, Time x, Calendar cal) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Time");
            if (cal != null) {
                parameter.addAttribute("calendar", cal.toString());
            }
            if (x != null) {
                parameter.addText(FormatTool.formatTime(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setTimestamp(String parameterName, int parameterIndex, Timestamp x) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Datetime");
            if (x != null) {
                parameter.addText(FormatTool.formatDatetime(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setTimestamp(String parameterName, int parameterIndex, Timestamp x, Calendar cal) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Datetime");
            if (cal != null) {
                parameter.addAttribute("calendar", cal.toString());
            }
            if (x != null) {
                parameter.addText(FormatTool.formatDatetime(x));
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setBinaryStream(String parameterName, int parameterIndex, InputStream x, int length) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Stream");
            parameter.addAttribute("length", length + "");
            if (x != null) {
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setCharacterStream(String parameterName, int parameterIndex, Reader reader, int length) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Reader");
            parameter.addAttribute("length", length + "");
            if (reader != null) {
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setObject(String parameterName, int parameterIndex, Object x, int targetSqlType, int scale) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Object");
            parameter.addAttribute("sqlType", targetSqlType + "");
            parameter.addAttribute("scale", scale + "");
            if (x != null) {
                parameter.addText(x.toString());
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setObject(String parameterName, int parameterIndex, Object x, int targetSqlType) {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Object");
            parameter.addAttribute("sqlType", targetSqlType + "");
            if (x != null) {
                parameter.addText(x.toString());
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }

        public void setObject(String parameterName, int parameterIndex, Object x) throws SQLException {
            Element parameter = this.paramsRoot.addElement("parameter");
            this.addParameterName(parameter, parameterName);
            parameter.addAttribute("index", parameterIndex + "");
            parameter.addAttribute("type", "Object");
            if (x != null) {
                parameter.addText(x.toString());
            } else {
                parameter.addAttribute("isNull", "true");
            }
        }
    }
}
