package com.sync.extractor.mysql;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Enumeration;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.sync.commons.parsing.bytes.MySQLStatementTranslator;
import com.sync.extractor.mysql.conversion.LittleEndianConversion;

/**
 * @author <a href="mailto:seppo.jaakola@continuent.com">Seppo Jaakola</a>
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class QueryLogEvent extends LogEvent {

    static Logger logger = Logger.getLogger(MySQLExtractor.class);

    /**
     * Fixed data part:
     * <ul>
     * <li>4 bytes. The ID of the thread that issued this statement. Needed for
     * temporary tables. This is also useful for a DBA for knowing who did what
     * on the master.</li>
     * <li>4 bytes. The time in seconds that the statement took to execute. Only
     * useful for inspection by the DBA.</li>
     * <li>1 byte. The length of the name of the database which was the default
     * database when the statement was executed. This name appears later, in the
     * variable data part. It is necessary for statements such as INSERT INTO t
     * VALUES(1) that don't specify the database and rely on the default
     * database previously selected by USE.</li>
     * <li>2 bytes. The error code resulting from execution of the statement on
     * the master. Error codes are defined in include/mysqld_error.h. 0 means no
     * error. How come statements with a non-zero error code can exist in the
     * binary log? This is mainly due to the use of non-transactional tables
     * within transactions. For example, if an INSERT ... SELECT fails after
     * inserting 1000 rows into a MyISAM table (for example, with a
     * duplicate-key violation), we have to write this statement to the binary
     * log, because it truly modified the MyISAM table. For transactional
     * tables, there should be no event with a non-zero error code (though it
     * can happen, for example if the connection was interrupted (Control-C)).
     * The slave checks the error code: After executing the statement itself, it
     * compares the error code it got with the error code in the event, and if
     * they are different it stops replicating (unless --slave-skip-errors was
     * used to ignore the error).</li>
     * <li>2 bytes (not present in v1, v3). The length of the status variable
     * block.</li>
     * </ul>
     * Variable part:
     * <ul>
     * <li>Zero or more status variables (not present in v1, v3). Each status
     * variable consists of one byte code identifying the variable stored,
     * followed by the value of the variable. The format of the value is
     * variable-specific, as described later.</li>
     * <li>The default database name (null-terminated).</li>
     * <li>The SQL statement. The slave knows the size of the other fields in
     * the variable part (the sizes are given in the fixed data part), so by
     * subtraction it can know the size of the statement.</li>
     * </ul>
     * Source : http://forge.mysql.com/wiki/MySQL_Internals_Binary_Log
     */
    protected String query;

    protected byte[] queryAsBytes;

    protected String charsetName;

    protected String databaseName;

    private int queryLength;

    protected int errorCode;

    protected long threadId;

    private int catalogLength;

    protected boolean charset_inited;

    protected byte[] charset;

    protected int clientCharsetId;

    protected int clientCollationId;

    protected int serverCollationId;

    private boolean flagAutocommit = true;

    private boolean flagForeignKeyChecks = true;

    private boolean flagAutoIsNull = true;

    private boolean flagUniqueChecks = true;

    private long sql_mode;

    private String sqlModeAsString;

    private int timeZoneLength;

    private String timeZoneName;

    private static HashMap<String, MySQLStatementTranslator> translators = new HashMap<String, MySQLStatementTranslator>();

    protected boolean parseStatements;

    private String sessionVariables;

    private int autoIncrementIncrement = -1;

    private int autoIncrementOffset = -1;

    public String getQuery() {
        return query;
    }

    public String getDefaultDb() {
        return databaseName;
    }

    public long getSessionId() {
        return threadId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    protected QueryLogEvent(byte[] buffer, FormatDescriptionLogEvent descriptionEvent, int eventType) throws MySQLExtractException {
        super(buffer, descriptionEvent, eventType);
    }

    public QueryLogEvent(byte[] buffer, int eventLength, FormatDescriptionLogEvent descriptionEvent, boolean parseStatements, boolean useBytesForString) throws MySQLExtractException {
        this(buffer, descriptionEvent, MysqlBinlog.QUERY_EVENT);
        this.parseStatements = parseStatements;
        int dataLength;
        int commonHeaderLength, postHeaderLength;
        int start;
        int end;
        boolean catalog_nz = true;
        int databaseNameLength;
        commonHeaderLength = descriptionEvent.commonHeaderLength;
        postHeaderLength = descriptionEvent.postHeaderLength[type - 1];
        if (logger.isDebugEnabled()) logger.debug("event length: " + eventLength + " common header length: " + commonHeaderLength + " post header length: " + postHeaderLength);
        if (eventLength < commonHeaderLength + postHeaderLength) {
            logger.warn("query event length is too short");
            throw new MySQLExtractException("too short query event");
        }
        dataLength = eventLength - (commonHeaderLength + postHeaderLength);
        short statusVariablesLength = 0;
        try {
            threadId = LittleEndianConversion.convert4BytesToLong(buffer, commonHeaderLength + MysqlBinlog.Q_THREAD_ID_OFFSET);
            execTime = LittleEndianConversion.convert4BytesToLong(buffer, commonHeaderLength + MysqlBinlog.Q_EXEC_TIME_OFFSET);
            databaseNameLength = LittleEndianConversion.convert1ByteToInt(buffer, commonHeaderLength + MysqlBinlog.Q_DB_LEN_OFFSET);
            errorCode = LittleEndianConversion.convert2BytesToInt(buffer, commonHeaderLength + MysqlBinlog.Q_ERR_CODE_OFFSET);
            boolean isMinimumMySQL5 = postHeaderLength - MysqlBinlog.QUERY_HEADER_MINIMAL_LEN > 0;
            if (isMinimumMySQL5) {
                statusVariablesLength = (short) LittleEndianConversion.convert2BytesToInt(buffer, commonHeaderLength + MysqlBinlog.Q_STATUS_VARS_LEN_OFFSET);
                dataLength -= statusVariablesLength;
                if (logger.isDebugEnabled()) logger.debug("QueryLogEvent has statusVariablesLength : " + statusVariablesLength);
            }
        } catch (IOException e) {
            throw new MySQLExtractException("query event header parsing failed");
        }
        start = commonHeaderLength + postHeaderLength;
        end = start + statusVariablesLength;
        extractStatusVariables(buffer, start, end);
        if (catalogLength > 0) {
            if (catalog_nz == true) {
            } else {
            }
        }
        databaseName = new String(buffer, end, databaseNameLength);
        queryLength = dataLength - databaseNameLength - 1;
        if (charset_inited) {
            try {
                clientCharsetId = LittleEndianConversion.convert2BytesToInt(charset, 0);
                clientCollationId = LittleEndianConversion.convert2BytesToInt(charset, 2);
                serverCollationId = LittleEndianConversion.convert2BytesToInt(charset, 4);
                charsetName = MysqlBinlog.getJavaCharset(clientCharsetId);
            } catch (IOException e) {
                logger.error("failed to use character id: " + charset);
            }
        }
        if (useBytesForString) {
            queryAsBytes = new byte[queryLength];
            System.arraycopy(buffer, end + databaseNameLength + 1, queryAsBytes, 0, queryLength);
        } else if (charset_inited) {
            try {
                String DBcharset = MysqlBinlog.getJavaCharset(clientCharsetId);
                if (Charset.isSupported(DBcharset)) {
                    String charsetJava = MysqlBinlog.getJavaCharset(clientCharsetId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("using charset: " + DBcharset + " java: " + charsetJava + " ID: " + clientCharsetId);
                    }
                    if (this.parseStatements) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Statement translation enabled");
                        }
                        MySQLStatementTranslator translator = getTranslator(DBcharset);
                        query = translator.toJavaString(buffer, end + databaseNameLength + 1, queryLength);
                    } else {
                        query = new String(buffer, end + databaseNameLength + 1, queryLength, DBcharset);
                    }
                } else {
                    logger.error("unsupported character set in query: " + DBcharset);
                    query = new String(buffer, end + databaseNameLength + 1, queryLength);
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("failed to use character set: " + charset);
            } catch (IllegalCharsetNameException e) {
                logger.debug("bad character set name: " + charset);
            } finally {
                if (query == null) {
                    logger.warn("Encoding query with default character set");
                    query = new String(buffer, end + databaseNameLength + 1, queryLength);
                }
            }
        } else {
            query = new String(buffer, end + databaseNameLength + 1, queryLength);
        }
    }

    protected int extractStatusVariables(byte[] buffer, int start, int end) throws MySQLExtractException {
        int flags2;
        int pos;
        for (pos = start; pos < end; ) {
            try {
                int variableCode = LittleEndianConversion.convert1ByteToInt(buffer, pos);
                pos++;
                switch(variableCode) {
                    case MysqlBinlog.Q_FLAGS2_CODE:
                        flags2 = LittleEndianConversion.convert4BytesToInt(buffer, pos);
                        if (logger.isDebugEnabled()) logger.debug("In QueryLogEvent, flags2 = " + flags2 + " - row data : " + hexdump(buffer, pos, 4));
                        flagAutocommit = (flags2 & MysqlBinlog.OPTION_NOT_AUTOCOMMIT) != MysqlBinlog.OPTION_NOT_AUTOCOMMIT;
                        flagAutoIsNull = (flags2 & MysqlBinlog.OPTION_AUTO_IS_NULL) == MysqlBinlog.OPTION_AUTO_IS_NULL;
                        flagForeignKeyChecks = (flags2 & MysqlBinlog.OPTION_NO_FOREIGN_KEY_CHECKS) != MysqlBinlog.OPTION_NO_FOREIGN_KEY_CHECKS;
                        flagUniqueChecks = (flags2 & MysqlBinlog.OPTION_RELAXED_UNIQUE_CHECKS) != MysqlBinlog.OPTION_RELAXED_UNIQUE_CHECKS;
                        sessionVariables = "set @@session.foreign_key_checks=" + (flagForeignKeyChecks ? 1 : 0) + ", @@session.sql_auto_is_null=" + (flagAutoIsNull ? 1 : 0) + ", @@session.unique_checks=" + (flagUniqueChecks ? 1 : 0) + ", @@session.autocommit=" + (flagAutocommit ? 1 : 0);
                        if (logger.isDebugEnabled()) {
                            logger.debug(sessionVariables);
                        }
                        pos += 4;
                        break;
                    case MysqlBinlog.Q_SQL_MODE_CODE:
                        {
                            sql_mode = LittleEndianConversion.convert8BytesToLong(buffer, pos);
                            StringBuffer sqlMode = new StringBuffer("");
                            Enumeration<Long> keys = MysqlBinlog.sql_modes.keys();
                            while (keys.hasMoreElements()) {
                                Long mode = keys.nextElement();
                                if ((sql_mode & mode) == mode) {
                                    if (sqlMode.length() > 0) sqlMode.append(",");
                                    sqlMode.append(MysqlBinlog.sql_modes.get(mode));
                                }
                            }
                            if (sql_mode != 0) sqlModeAsString = "'" + sqlMode.toString() + "'"; else sqlModeAsString = "''";
                            if (logger.isDebugEnabled()) logger.debug("In QueryLogEvent, sql_mode = " + sql_mode + (sql_mode != 0 ? " - " : "") + sqlModeAsString);
                            pos += 8;
                            break;
                        }
                    case MysqlBinlog.Q_CATALOG_NZ_CODE:
                        catalogLength = LittleEndianConversion.convert1ByteToInt(buffer, pos);
                        pos++;
                        if (catalogLength > 0) {
                            pos += catalogLength;
                        }
                        break;
                    case MysqlBinlog.Q_AUTO_INCREMENT:
                        autoIncrementIncrement = LittleEndianConversion.convert2BytesToInt(buffer, pos);
                        pos += 2;
                        autoIncrementOffset = LittleEndianConversion.convert2BytesToInt(buffer, pos);
                        pos += 2;
                        break;
                    case MysqlBinlog.Q_CHARSET_CODE:
                        {
                            charset_inited = true;
                            charset = new byte[6];
                            System.arraycopy(buffer, pos, charset, 0, 6);
                            pos += 6;
                            break;
                        }
                    case MysqlBinlog.Q_TIME_ZONE_CODE:
                        {
                            timeZoneLength = LittleEndianConversion.convert1ByteToInt(buffer, pos);
                            pos++;
                            if (timeZoneLength > 0) {
                                timeZoneName = new String(buffer, pos, timeZoneLength);
                                if (logger.isDebugEnabled()) logger.debug("Using time zone : " + timeZoneName);
                                pos += timeZoneLength;
                            }
                            break;
                        }
                    case MysqlBinlog.Q_CATALOG_CODE:
                        break;
                    case MysqlBinlog.Q_LC_TIME_NAMES_CODE:
                        pos += 2;
                        break;
                    case MysqlBinlog.Q_CHARSET_DATABASE_CODE:
                        pos += 2;
                        break;
                    default:
                        logger.debug("QueryLogEvent has unknown status variable +" + "(first has code: " + (pos + 1) + " ), skipping the rest of them");
                        pos = end;
                }
            } catch (IOException e) {
                logger.error("IO exception while reading query event parameters");
                throw new MySQLExtractException("query event reading failed");
            }
        }
        return pos;
    }

    private MySQLStatementTranslator getTranslator(String charset) throws UnsupportedEncodingException {
        MySQLStatementTranslator translator = translators.get(charset);
        if (translator == null) {
            translator = new MySQLStatementTranslator(charset);
            translators.put(charset, translator);
        }
        return translator;
    }

    /**
     * Returns the sqlModeAsString value.
     *
     * @return Returns the sqlModeAsString.
     */
    public String getSqlMode() {
        return sqlModeAsString;
    }

    /**
     * Returns the flagAutoIsNull value.
     *
     * @return Returns the flagAutoIsNull.
     */
    public String getAutoIsNullFlag() {
        return (flagAutoIsNull ? "1" : "0");
    }

    /**
     * Returns the flagForeignKeyChecks value.
     *
     * @return Returns the flagForeignKeyChecks.
     */
    public String getForeignKeyChecksFlag() {
        return (flagForeignKeyChecks ? "1" : "0");
    }

    /**
     * Returns the flagAutocommit value.
     *
     * @return Returns the flagAutocommit.
     */
    public String getAutocommitFlag() {
        return (flagAutocommit ? "1" : "0");
    }

    /**
     * Returns the flagUniqueChecks value.
     *
     * @return Returns the flagUniqueChecks.
     */
    public String getUniqueChecksFlag() {
        return (flagUniqueChecks ? "1" : "0");
    }

    /**
     * Returns the charsetID value.
     *
     * @return Returns the charsetID.
     */
    public int getClientCharsetId() {
        return clientCharsetId;
    }

    /**
     * Returns the clientCollationId value.
     *
     * @return Returns the clientCollationId.
     */
    public int getClientCollationId() {
        return clientCollationId;
    }

    /**
     * Returns the serverCollationId value.
     *
     * @return Returns the serverCollationId.
     */
    public int getServerCollationId() {
        return serverCollationId;
    }

    public int getAutoIncrementIncrement() {
        return autoIncrementIncrement;
    }

    public int getAutoIncrementOffset() {
        return autoIncrementOffset;
    }

    public byte[] getQueryAsBytes() {
        return queryAsBytes;
    }

    /** Return the native charset used to store query as bytes. */
    public String getCharsetName() {
        return charsetName;
    }
}
