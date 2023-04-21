package fedora.server.search;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import fedora.server.ReadOnlyContext;
import fedora.server.Server;
import fedora.server.errors.ObjectIntegrityException;
import fedora.server.errors.RepositoryConfigurationException;
import fedora.server.errors.ServerException;
import fedora.server.errors.StorageDeviceException;
import fedora.server.errors.StreamIOException;
import fedora.server.errors.UnknownSessionTokenException;
import fedora.server.errors.UnrecognizedFieldException;
import fedora.server.storage.ConnectionPool;
import fedora.server.storage.BMechReader;
import fedora.server.storage.DOReader;
import fedora.server.storage.RepositoryReader;
import fedora.server.storage.types.DatastreamXMLMetadata;
import fedora.server.storage.types.Disseminator;
import fedora.server.utilities.DateUtility;
import fedora.server.utilities.SQLUtility;
import fedora.server.utilities.DCFields;

/**
 * A FieldSearch implementation that uses a relational
 * database as a backend.
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: FieldSearchSQLImpl.java 5268 2006-12-01 09:24:53Z cwilper $
 */
public class FieldSearchSQLImpl implements FieldSearch {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(FieldSearchSQLImpl.class.getName());

    /** Whether DC fields are being indexed or not. */
    private boolean m_indexDCFields = true;

    private ConnectionPool m_cPool;

    private RepositoryReader m_repoReader;

    private int m_maxResults;

    private int m_maxSecondsPerSession;

    public static String[] DB_COLUMN_NAMES = new String[] { "pid", "label", "fType", "cModel", "state", "ownerId", "cDate", "mDate", "dcmDate", "bDef", "bMech", "dcTitle", "dcCreator", "dcSubject", "dcDescription", "dcPublisher", "dcContributor", "dcDate", "dcType", "dcFormat", "dcIdentifier", "dcSource", "dcLanguage", "dcRelation", "dcCoverage", "dcRights" };

    private static boolean[] s_dbColumnNumeric = new boolean[] { false, false, false, false, false, false, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

    public static String[] DB_COLUMN_NAMES_NODC = new String[] { "pid", "label", "fType", "cModel", "state", "ownerId", "cDate", "mDate", "dcmDate", "bDef", "bMech" };

    private static boolean[] s_dbColumnNumericNoDC = new boolean[] { false, false, false, false, false, false, true, true, true, false, false };

    private HashMap m_currentResults = new HashMap();

    /**
     * Construct a FieldSearchSQLImpl that indexes DC fields.
     *
     * @param cPool the ConnectionPool with connections to the db containing
     *        the fields
     * @param repoReader the RepositoryReader to use when getting the original
     *        values of the fields
     * @param maxResults the maximum number of results to return at a time,
     *        regardless of what the user might request
     * @param maxSecondsPerSession maximum number of seconds per session.
     */
    public FieldSearchSQLImpl(ConnectionPool cPool, RepositoryReader repoReader, int maxResults, int maxSecondsPerSession) {
        this(cPool, repoReader, maxResults, maxSecondsPerSession, true);
    }

    /**
     * Construct a FieldSearchSQLImpl that indexes DC fields only if specified.
     *
     * @param cPool the ConnectionPool with connections to the db containing
     *        the fields
     * @param repoReader the RepositoryReader to use when getting the original
     *        values of the fields
     * @param maxResults the maximum number of results to return at a time,
     *        regardless of what the user might request
     * @param maxSecondsPerSession maximum number of seconds per session.
     * @param indexDCFields whether DC field values should be examined and
     *        updated in the database.  If false, queries will behave as
     *        if no values had been specified for the DC fields.
     */
    public FieldSearchSQLImpl(ConnectionPool cPool, RepositoryReader repoReader, int maxResults, int maxSecondsPerSession, boolean indexDCFields) {
        LOG.debug("Entering constructor");
        m_cPool = cPool;
        m_repoReader = repoReader;
        m_maxResults = maxResults;
        m_maxSecondsPerSession = maxSecondsPerSession;
        m_indexDCFields = indexDCFields;
        LOG.debug("Exiting constructor");
    }

    public void update(DOReader reader) throws ServerException {
        LOG.debug("Entering update(DOReader)");
        String pid = reader.GetObjectPID();
        Connection conn = null;
        Statement st = null;
        try {
            conn = m_cPool.getConnection();
            String[] dbRowValues;
            if (m_indexDCFields) {
                dbRowValues = new String[DB_COLUMN_NAMES.length];
            } else {
                dbRowValues = new String[DB_COLUMN_NAMES_NODC.length];
            }
            dbRowValues[0] = reader.GetObjectPID();
            String v;
            v = reader.GetObjectLabel();
            if (v != null) v = v.toLowerCase();
            dbRowValues[1] = v;
            dbRowValues[2] = reader.getFedoraObjectType().toLowerCase();
            v = reader.getContentModelId();
            if (v != null) v = v.toLowerCase();
            dbRowValues[3] = v;
            dbRowValues[4] = reader.GetObjectState().toLowerCase();
            v = reader.getOwnerId();
            if (v != null) v = v.toLowerCase();
            dbRowValues[5] = v;
            Date date = reader.getCreateDate();
            if (date == null) {
                date = new Date();
            }
            dbRowValues[6] = "" + date.getTime();
            date = reader.getLastModDate();
            if (date == null) {
                date = new Date();
            }
            dbRowValues[7] = "" + date.getTime();
            Disseminator[] disses = reader.GetDisseminators(null, null);
            ArrayList bDefs = new ArrayList();
            ArrayList bMechs = new ArrayList();
            for (int i = 0; i < disses.length; i++) {
                bDefs.add(disses[i].bDefID);
                bMechs.add(disses[i].bMechID);
            }
            if (dbRowValues[2].equals("m")) {
                BMechReader mechReader = m_repoReader.getBMechReader(Server.USE_DEFINITIVE_STORE, ReadOnlyContext.EMPTY, reader.GetObjectPID());
                bDefs.add(mechReader.getServiceDSInputSpec(null).bDefPID);
            }
            dbRowValues[9] = getDbValueCaseSensitive(bDefs);
            dbRowValues[10] = getDbValueCaseSensitive(bMechs);
            DatastreamXMLMetadata dcmd = null;
            try {
                dcmd = (DatastreamXMLMetadata) reader.GetDatastream("DC", null);
            } catch (ClassCastException cce) {
                throw new ObjectIntegrityException("Object " + reader.GetObjectPID() + " has a DC datastream, but it's not inline XML.");
            }
            if (dcmd == null) {
                LOG.debug("Did not have DC Metadata datastream for this object.");
            } else {
                LOG.debug("Had DC Metadata datastream for this object.");
                dbRowValues[8] = "" + dcmd.DSCreateDT.getTime();
                if (m_indexDCFields) {
                    InputStream in = dcmd.getContentStream();
                    DCFields dc = new DCFields(in);
                    dbRowValues[11] = getDbValue(dc.titles());
                    dbRowValues[12] = getDbValue(dc.creators());
                    dbRowValues[13] = getDbValue(dc.subjects());
                    dbRowValues[14] = getDbValue(dc.descriptions());
                    dbRowValues[15] = getDbValue(dc.publishers());
                    dbRowValues[16] = getDbValue(dc.contributors());
                    dbRowValues[17] = getDbValue(dc.dates());
                    List wellFormedDates = null;
                    for (int i = 0; i < dc.dates().size(); i++) {
                        if (i == 0) {
                            wellFormedDates = new ArrayList();
                        }
                        Date p = DateUtility.parseDateAsUTC((String) dc.dates().get(i));
                        if (p != null) {
                            wellFormedDates.add(p);
                        }
                    }
                    if (wellFormedDates != null && wellFormedDates.size() > 0) {
                        st = conn.createStatement();
                        st.executeUpdate("DELETE FROM dcDates WHERE pid='" + pid + "'");
                        for (int i = 0; i < wellFormedDates.size(); i++) {
                            Date dt = (Date) wellFormedDates.get(i);
                            st.executeUpdate("INSERT INTO dcDates (pid, dcDate) " + "values ('" + pid + "', " + dt.getTime() + ")");
                        }
                    }
                    dbRowValues[18] = getDbValue(dc.types());
                    dbRowValues[19] = getDbValue(dc.formats());
                    dbRowValues[20] = getDbValue(dc.identifiers());
                    dbRowValues[21] = getDbValue(dc.sources());
                    dbRowValues[22] = getDbValue(dc.languages());
                    dbRowValues[23] = getDbValue(dc.relations());
                    dbRowValues[24] = getDbValue(dc.coverages());
                    dbRowValues[25] = getDbValue(dc.rights());
                    LOG.debug("Formulating SQL and inserting/updating WITH DC...");
                    SQLUtility.replaceInto(conn, "doFields", DB_COLUMN_NAMES, dbRowValues, "pid", s_dbColumnNumeric);
                } else {
                    LOG.debug("Formulating SQL and inserting/updating WITHOUT DC...");
                    SQLUtility.replaceInto(conn, "doFields", DB_COLUMN_NAMES_NODC, dbRowValues, "pid", s_dbColumnNumericNoDC);
                }
            }
        } catch (SQLException sqle) {
            throw new StorageDeviceException("Error attempting FieldSearch " + "update of " + pid, sqle);
        } finally {
            try {
                if (st != null) st.close();
                if (conn != null) m_cPool.free(conn);
            } catch (SQLException sqle2) {
                throw new StorageDeviceException("Error closing statement " + "while attempting update of object" + sqle2.getMessage());
            } finally {
                st = null;
                LOG.debug("Exiting update(DOReader)");
            }
        }
    }

    public boolean delete(String pid) throws ServerException {
        LOG.debug("Entering delete(String)");
        Connection conn = null;
        Statement st = null;
        try {
            conn = m_cPool.getConnection();
            st = conn.createStatement();
            st.executeUpdate("DELETE FROM doFields WHERE pid='" + pid + "'");
            st.executeUpdate("DELETE FROM dcDates WHERE pid='" + pid + "'");
            return true;
        } catch (SQLException sqle) {
            throw new StorageDeviceException("Error attempting delete of " + "object with pid '" + pid + "': " + sqle.getMessage());
        } finally {
            try {
                if (st != null) st.close();
                if (conn != null) m_cPool.free(conn);
            } catch (SQLException sqle2) {
                throw new StorageDeviceException("Error closing statement " + "while attempting update of object" + sqle2.getMessage());
            } finally {
                st = null;
                LOG.debug("Exiting delete(String)");
            }
        }
    }

    public FieldSearchResult findObjects(String[] resultFields, int maxResults, FieldSearchQuery query) throws UnrecognizedFieldException, ObjectIntegrityException, RepositoryConfigurationException, StreamIOException, ServerException, StorageDeviceException {
        closeAndForgetOldResults();
        int actualMax = maxResults;
        if (m_maxResults < maxResults) {
            actualMax = m_maxResults;
        }
        try {
            return stepAndRemember(new FieldSearchResultSQLImpl(m_cPool, m_repoReader, resultFields, actualMax, m_maxSecondsPerSession, query));
        } catch (SQLException sqle) {
            throw new StorageDeviceException("Error querying sql db: " + sqle.getMessage());
        }
    }

    public FieldSearchResult resumeFindObjects(String sessionToken) throws UnrecognizedFieldException, ObjectIntegrityException, RepositoryConfigurationException, StreamIOException, ServerException, UnknownSessionTokenException {
        closeAndForgetOldResults();
        FieldSearchResultSQLImpl result = (FieldSearchResultSQLImpl) m_currentResults.remove(sessionToken);
        if (result == null) {
            throw new UnknownSessionTokenException("Session is expired " + "or never existed.");
        }
        return stepAndRemember(result);
    }

    private FieldSearchResult stepAndRemember(FieldSearchResultSQLImpl result) throws UnrecognizedFieldException, ObjectIntegrityException, RepositoryConfigurationException, StreamIOException, ServerException, UnrecognizedFieldException {
        result.step();
        if (result.getToken() != null) {
            m_currentResults.put(result.getToken(), result);
        }
        return result;
    }

    private void closeAndForgetOldResults() {
        Iterator iter = m_currentResults.values().iterator();
        ArrayList toRemove = new ArrayList();
        while (iter.hasNext()) {
            FieldSearchResultSQLImpl r = (FieldSearchResultSQLImpl) iter.next();
            if (r.isExpired()) {
                LOG.debug("listSession " + r.getToken() + " expired; will forget it.");
                toRemove.add(r.getToken());
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            String token = (String) toRemove.get(i);
            m_currentResults.remove(token);
        }
    }

    /**
     * Get the string that should be inserted for a repeating-value column,
     * given a list of values.  Turn each value to lowercase and separate them
     * all by space characters.  If the list is empty, return null.
     *
     * @param dcItem a list of dublin core values
     * @return String the string to insert
     */
    private static String getDbValue(List dcItem) {
        if (dcItem.size() == 0) {
            return null;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < dcItem.size(); i++) {
            String val = (String) dcItem.get(i);
            out.append(" ");
            out.append(val.toLowerCase());
        }
        out.append(" .");
        return out.toString();
    }

    public static String getDbValueCaseSensitive(List dcItem) {
        if (dcItem.size() == 0) {
            return null;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < dcItem.size(); i++) {
            String val = (String) dcItem.get(i);
            out.append(" ");
            out.append(val);
        }
        out.append(" .");
        return out.toString();
    }
}
