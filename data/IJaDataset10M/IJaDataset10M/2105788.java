package net.sourceforge.squirrel_sql.plugins.sqlval;

import java.io.Serializable;
import net.sourceforge.squirrel_sql.fw.sql.ISQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;

public class WebServiceSessionProperties implements Cloneable, Serializable {

    private static final long serialVersionUID = -2947538669142176792L;

    private static final String UNSUPPORTED = "Unsupported";

    /** If <TT>true</TT> use anonymous DBMS and connection technology. */
    private boolean _useAnonymousDBMS = false;

    /** Target DBMS. */
    private String _targetDBMS;

    /** Target DBMS version. */
    private String _targetDBMSVersion;

    /** Connection technology. */
    private String _connTechnology;

    /** Connection technology version. */
    private String _connTechnologyVersion;

    /** Web service session. */
    private transient WebServiceSession _webServiceSession;

    public WebServiceSessionProperties(WebServicePreferences prefs) {
        super();
        _webServiceSession = new WebServiceSession(prefs, this);
    }

    /**
	 * Return a copy of this object.
	 */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    /**
	 * Retrieve the web service session. If <TT>null</TT> we haven't yet logged
	 * onto the web service.
	 * 
	 * @return	Web Service Session.
	 */
    public WebServiceSession getWebServiceSession() {
        return _webServiceSession;
    }

    /**
	 * If <TT>true</TT> use anonymous client database and connection
	 * technology.
	 *
	 * @return	<TT>true</TT> if anonymous client to be used.
	 */
    public boolean getUseAnonymousDBMS() {
        return _useAnonymousDBMS;
    }

    /**
	 * Set if using anonymous client database and connection
	 * technology.
	 *
	 * @param	value	<TT>true</TT> if anonymous client to be used.
	 */
    public void setUseAnonymousDBMS(boolean value) {
        _useAnonymousDBMS = value;
    }

    /**
	 * Retrieve the target DBMS. This is only
	 * used if <TT>useAnonymousDBMS</TT> is false.
	 *
	 * @return	Target DBMS name.
	 */
    public String getTargetDBMSName() {
        return _targetDBMS;
    }

    /**
	 * Retrieve the target DBMS version. This is only
	 * used if <TT>useAnonymousDBMS</TT> is false.
	 *
	 * @return	Target DBMS version.
	 */
    public String getTargetDBMSVersion() {
        return _targetDBMSVersion;
    }

    /**
	 * Retrieve the connection technology. This is only
	 * used if <TT>useAnonymousDBMS</TT> is false.
	 *
	 * @return	Connection technology
	 */
    public String getConnectionTechnology() {
        return _connTechnology;
    }

    /**
	 * Retrieve the connection technology version. This is only
	 * used if <TT>useAnonymousDBMS</TT> is false.
	 *
	 * @return	Target DBMS version.
	 */
    public String getConnectionTechnologyVersion() {
        return _connTechnologyVersion;
    }

    /**
	 * Set the DBMS and connection technology info.
	 * 
	 * @param	conn	SQLConnection to DBMS.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if null SQLConnection passed.
	 */
    public void setSQLConnection(ISQLConnection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("SQLConnection == null");
        }
        SQLDatabaseMetaData md = conn.getSQLMetaData();
        try {
            _targetDBMS = md.getDatabaseProductName();
        } catch (Throwable ignore) {
            _targetDBMS = UNSUPPORTED;
        }
        try {
            _targetDBMSVersion = md.getDatabaseProductVersion();
            if (_targetDBMSVersion.length() > 30) {
                _targetDBMSVersion = _targetDBMSVersion.substring(0, 30);
            }
        } catch (Throwable ignore) {
            _targetDBMSVersion = UNSUPPORTED;
        }
        _connTechnology = "JDBC";
        try {
            _connTechnologyVersion = String.valueOf(md.getJDBCVersion());
        } catch (Throwable ignore) {
            _connTechnologyVersion = UNSUPPORTED;
        }
    }
}
