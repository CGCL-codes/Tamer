package jmodnews.db.mckoi;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import jmodnews.db.Server;
import jmodnews.db.ServerChangedEvent;
import jmodnews.logging.Logger;
import jmodnews.nntp.NNTPController;
import jmodnews.nntp.NNTPThread;

/**
 * Implementation of the {@link jmodnews.db.Server} interface for 
 * the McKoi database.
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class ServerImpl extends SQLData implements Server {

    private static final String[] FIELDS = new String[] { "id", "name", "host", "port", "username", "password", "maxconn", "pipelining", "networkid", "lastupdate", "reconnectcount", "idletimeout" };

    private static final SQLFieldType[] TYPES = new SQLFieldType[] { SQLFieldType.TYPE_INT, SQLFieldType.TYPE_STRING, SQLFieldType.TYPE_STRING, SQLFieldType.TYPE_INT, SQLFieldType.TYPE_STRING, SQLFieldType.TYPE_STRING, SQLFieldType.TYPE_INT, SQLFieldType.TYPE_BOOLEAN, SQLFieldType.TYPE_STRING, SQLFieldType.TYPE_DATE, SQLFieldType.TYPE_INT, SQLFieldType.TYPE_INT };

    public ServerImpl(DatabaseImpl db, ResultSet rs) {
        super("jmodnews_servers", db, rs, FIELDS, TYPES, "id");
    }

    public String getName() {
        return getString("name");
    }

    public String getHostName() {
        return getString("host");
    }

    public String getUsername() {
        return getString("username");
    }

    public String getPassword() {
        return getString("password");
    }

    public int getMaxConnections() {
        int maxconn = getInt("maxconn");
        return (maxconn < 1) ? 1 : maxconn;
    }

    public int getPort() {
        return getInt("port");
    }

    public String getNetworkID() {
        return getString("networkid");
    }

    public Date getLastGroupListUpdate() {
        return (Date) getObject("lastupdate");
    }

    private List groupList = null, fetchGroupList = null;

    public List getGroupList() {
        return Collections.unmodifiableList(db.getGroupList(this, false));
    }

    public List getGroupListForFetch(NNTPController c) {
        if (fetchGroupList == null) {
            fetchGroupList = Collections.unmodifiableList(db.getPGroupList(this));
        }
        return fetchGroupList;
    }

    public int getReconnectCount() {
        return getInt("reconnectcount");
    }

    public long getIdleTimeout() {
        return getInt("idletimeout");
    }

    public boolean getPipelining() {
        return ((Boolean) getObject("pipelining")).booleanValue();
    }

    public void setLastGroupUpdate(Date date, NNTPController controller) {
        Logger.log(Logger.DEBUG, "LastGroupUpdate: " + date);
        setObject("lastupdate", date);
    }

    public void addGroups(NNTPThread t, List groups) {
        if (groups.size() == 0) return;
        db.addGroups(getId(), groups);
        groupList = null;
        db.getController().fireListeners(new ServerChangedEvent(true));
    }

    public int getId() {
        return getInt("id");
    }

    public void setServerParameters(String name, String host, int port, String username, String password, int connections, String networkID, int reconnectCount, long idleTimeout) {
        setObjects(new String[] { "name", "host", "port", "username", "password", "maxconn", "networkid", "reconnectcount", "idletimeout" }, new Object[] { name, host, new Integer(port), username, password, new Integer(connections), networkID, new Integer(reconnectCount), new Integer((int) idleTimeout) });
        db.invalidateServers(false);
    }

    public boolean delete() {
        List gl = db.getGroupList(this, true);
        for (Iterator it = gl.iterator(); it.hasNext(); ) {
            ServerGroupImpl sg = (ServerGroupImpl) it.next();
            if (sg.isSubscribed()) {
                sg.subscribeGroup(false, 0);
            } else {
                throw new RuntimeException();
            }
        }
        db.deleteAllGroups(this);
        deleteMe();
        db.invalidateServers(true);
        return true;
    }

    /**
	 * 
	 */
    public void invalidateFetchGroupList() {
        fetchGroupList = null;
    }

    public boolean equals(Object o) {
        return (o instanceof ServerImpl) && (((ServerImpl) o).getId() == getId());
    }
}
