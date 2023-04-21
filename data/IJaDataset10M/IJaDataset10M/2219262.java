package xmlsync2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.LinkedList;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import datawave.util.IOUtil;
import datawave.util.LoggerCache;

/**
 * @author jinwoo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Procedure {

    private short type;

    private int paramcount;

    private LinkedList metaparams;

    private CallableStatement statement;

    private String catalog;

    private String encoding = null;

    private static Logger log = LoggerCache.get(Procedure.class.getName());

    private Connection connection;

    /**
	 * Constructor Procedure.
	 * @param resultSet
	 * @param resultSet1
	 */
    public Procedure(final String sp, final Connection connection, final ResultSet procedure, final ResultSet parameter, final int timeout, final String encoding) throws SQLException, XmlsyncException {
        this.connection = connection;
        this.encoding = encoding;
        if (procedure.next()) {
            type = procedure.getShort("PROCEDURE_TYPE");
            String catalog = procedure.getString("PROCEDURE_CAT");
            String spname = procedure.getString("PROCEDURE_NAME");
            String schemaname = procedure.getString("PROCEDURE_SCHEM");
            metaparams = getmetaparam(parameter);
            paramcount = countparam(metaparams);
            statement = connection.prepareCall(Str.sql(fullname(catalog, schemaname, spname), type, paramcount), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(timeout);
            log.debug("stored procedure : " + Str.sql(fullname(catalog, schemaname, spname), type, paramcount));
        } else {
            procedure.close();
            parameter.close();
            Object[] args = { sp };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0004"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        procedure.close();
        parameter.close();
    }

    /**
	 * Method fullname.
	 * @param catalog
	 * @param spname
	 * @return String
	 */
    private String fullname(final String catalog, final String schemaname, final String spname) {
        if (catalog == null || catalog.length() == 0) if (schemaname == null || schemaname.length() == 0) return spname; else return schemaname + "." + spname; else return catalog + "." + spname;
    }

    /**
	 * Method countparam.
	 * @param metaparam
	 * @return int
	 */
    private int countparam(final LinkedList metaparam) {
        switch(type) {
            case DatabaseMetaData.procedureNoResult:
                return metaparam.size();
            default:
                return metaparam.size() - 1;
        }
    }

    /**
	 * Method getmetaparam.
	 * @param parameter
	 * @return LinkedList
	 */
    private LinkedList getmetaparam(final ResultSet parameter) throws SQLException {
        LinkedList metaparams = new LinkedList();
        while (parameter.next()) metaparams.addLast(new MetaParameter(parameter, encoding));
        return metaparams;
    }

    public void setparameter(final LinkedList param) throws SQLException, XmlsyncException {
        setinparameter(param);
        registeroutparameter();
    }

    /**
	 * Method registeroutparameter.
	 */
    private void registeroutparameter() throws SQLException, XmlsyncException {
        for (int i = 0; i < metaparams.size(); i++) switch(((MetaParameter) metaparams.get(i)).getcolumntype()) {
            case DatabaseMetaData.procedureColumnInOut:
            case DatabaseMetaData.procedureColumnResult:
            case DatabaseMetaData.procedureColumnOut:
            case DatabaseMetaData.procedureColumnReturn:
                MetaParameter metaparam = (MetaParameter) metaparams.get(i);
                short type = metaparam.getdatatype();
                if (type == Types.OTHER) registeroutparam4oracle(i + 1, metaparam.gettypename()); else statement.registerOutParameter(i + 1, type);
                break;
        }
    }

    /**
	 * Method registeroutparam4oracle.
	 * @param i
	 * @param string
	 * oracle.jdbc.driver.OracleTypes.CURSOR : -10
	 */
    private void registeroutparam4oracle(final int i, final String typename) throws SQLException, XmlsyncException {
        if (typename.equalsIgnoreCase("FLOAT")) statement.registerOutParameter(i, Types.FLOAT); else if (typename.equalsIgnoreCase("INTEGER")) statement.registerOutParameter(i, Types.INTEGER); else if (typename.equalsIgnoreCase("REF CURSOR")) statement.registerOutParameter(i, oracle.jdbc.driver.OracleTypes.CURSOR); else if (typename.equalsIgnoreCase("PL/SQL RECORD")) statement.registerOutParameter(i, oracle.jdbc.driver.OracleTypes.CURSOR); else if (typename.equalsIgnoreCase("BLOB")) statement.registerOutParameter(i, Types.BLOB); else {
            Object[] args = { typename };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0005"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    /**
	 * Method setinparameter.
	 * @param param
	 */
    private void setinparameter(LinkedList param) throws SQLException, XmlsyncException {
        if (type == DatabaseMetaData.procedureNoResult) for (int i = 0; i < param.size(); i++) {
            MetaParameter metaparam = (MetaParameter) metaparams.get(i);
            if (metaparam.getcolumntype() == DatabaseMetaData.procedureColumnIn || metaparam.getcolumntype() == DatabaseMetaData.procedureColumnInOut) assign(i + 1, (String) param.get(i), metaparam.getdatatype(), metaparam.gettypename());
        } else for (int i = 0; i < param.size(); i++) {
            MetaParameter metaparam = (MetaParameter) metaparams.get(i + 1);
            if (metaparam.getcolumntype() == DatabaseMetaData.procedureColumnIn || metaparam.getcolumntype() == DatabaseMetaData.procedureColumnInOut) assign(i + 2, (String) param.get(i), metaparam.getdatatype(), metaparam.gettypename());
        }
    }

    /**
	 * Method assign.
	 * @param i
	 * @param object
	 * @param s
	 */
    private void assign(final int i, final String value, final short type, final String typename) throws SQLException, XmlsyncException {
        log.debug("param index : " + (i - 1));
        log.debug("param type  : " + typename);
        log.debug("param value : " + value);
        if (value != null) switch(type) {
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
                if (this.encoding == null) statement.setString(i, value); else try {
                    statement.setString(i, new String(value.getBytes("EUC-KR"), this.encoding));
                } catch (UnsupportedEncodingException e) {
                    Object[] args = { this.encoding };
                    String msg = MessageFormat.format(Messages.getString("XMLSYNC0007"), args);
                    log.error(msg);
                    throw new XmlsyncException(msg);
                } catch (SQLException e) {
                }
                break;
            default:
                if (value.length() > 0) switch(type) {
                    case Types.TINYINT:
                    case Types.NUMERIC:
                    case Types.INTEGER:
                        statement.setInt(i, Integer.parseInt(value));
                        break;
                    case Types.REAL:
                    case Types.DECIMAL:
                    case Types.FLOAT:
                        statement.setFloat(i, (new Float(value)).floatValue());
                        break;
                    case Types.BIT:
                        statement.setBoolean(i, (new Boolean(value)).booleanValue());
                        break;
                    case Types.DOUBLE:
                        statement.setDouble(i, (new Double(value)).doubleValue());
                        break;
                    case Types.SMALLINT:
                        if (!value.equals("")) statement.setShort(i, (new Short(value)).shortValue());
                        break;
                    case Types.OTHER:
                        assignother(i, value, typename);
                        break;
                    case Types.TIME:
                        statement.setTime(i, Str.gettime(value));
                        break;
                    case Types.TIMESTAMP:
                        statement.setTimestamp(i, Str.gettimestamp(value));
                        break;
                    case Types.DATE:
                        statement.setDate(i, Str.getdate(value));
                        break;
                    default:
                        Object[] args = { typename };
                        String msg = MessageFormat.format(Messages.getString("XMLSYNC0005"), args);
                        log.error(msg);
                        throw new XmlsyncException(msg);
                } else statement.setNull(i, type);
        } else statement.setNull(i, type);
    }

    /**
	 * Method assignother.
	 * @param statement
	 * @param i
	 * @param typename
	 */
    private void assignother(final int i, final String value, final String typename) throws SQLException, XmlsyncException {
        if (typename.equalsIgnoreCase("FLOAT")) statement.setFloat(i, (new Float(value)).floatValue()); else if (typename.equalsIgnoreCase("INTEGER")) statement.setInt(i, Integer.parseInt(value)); else if (typename.equalsIgnoreCase("DATE")) statement.setDate(i, Str.getdate(value)); else if (typename.equalsIgnoreCase("CLOB")) {
            Clob clob = readasclob(value);
            statement.setClob(i, clob);
        } else if (typename.equalsIgnoreCase("BLOB")) {
            Blob blob = readasblob(value);
            statement.setBlob(i, blob);
        } else statement.setString(i, value);
    }

    /**
	 * Method readasblob.
	 * @param value
	 * @return Blob
	 */
    private Blob readasblob(final String filepath) throws XmlsyncException {
        BLOB b = null;
        try {
            File file = new File(filepath);
            b = BLOB.createTemporary(connection, true, BLOB.DURATION_CALL);
            b.open(BLOB.MODE_READWRITE);
            OutputStream writer = b.getBinaryOutputStream();
            byte[] buf;
            buf = IOUtil.file2ByteArray(filepath);
            writer.write(buf, 0, (int) file.length());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            Object[] args = { filepath, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0025"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (IOException e) {
            Object[] args = { "BLOB", e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0039"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (SQLException e) {
            Object[] args = { e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0002"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        return b;
    }

    /**
 * Method readasclob.
 * @param value
 */
    private Clob readasclob(final String filepath) throws XmlsyncException {
        CLOB c = null;
        try {
            File file = new File(filepath);
            c = CLOB.createTemporary(connection, true, CLOB.DURATION_CALL);
            c.open(CLOB.MODE_READWRITE);
            Writer writer = c.getCharacterOutputStream();
            char[] buf;
            buf = IOUtil.file2CharArray(filepath);
            if (encoding == null) writer.write(buf, 0, (int) file.length()); else {
                String value = new String(buf);
                writer.write(new String(value.getBytes("EUC-KR"), this.encoding));
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            Object[] args = { filepath, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0025"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (IOException e) {
            Object[] args = { "CLOB", e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0039"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (SQLException e) {
            Object[] args = { e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0002"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        return c;
    }

    /**
	 * Method execute.
	 * @return QueryResult
	 */
    public QueryResult execute() throws SQLException, XmlsyncException {
        statement.execute();
        int t = statement.getQueryTimeout();
        QueryResult result = new QueryResult(encoding);
        appendrecordsets(result, statement);
        appendparams(result, statement);
        return result;
    }

    /**
	 * Method appendparams.
	 * @param result
	 * @param statement
	 */
    private void appendparams(QueryResult result, CallableStatement statement) throws SQLException, XmlsyncException {
        for (int i = 0; i < metaparams.size(); i++) {
            result.addparam(((MetaParameter) metaparams.get(i)).createparameter(statement, i + 1));
        }
    }

    /**
	 * Method appendrecordsets.
	 * @param result
	 * @param statement
	 */
    private void appendrecordsets(QueryResult result, CallableStatement statement) throws SQLException, XmlsyncException, XmlsyncException {
        ResultSet resultset = statement.getResultSet();
        if (resultset != null) {
            result.addrecordset(resultset);
            while (statement.getMoreResults()) {
                resultset = statement.getResultSet();
                result.addrecordset(resultset);
            }
        }
    }

    /**
	 * Method close.
	 */
    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
