package gruntspud.connection;

import gruntspud.CVSRoot;
import gruntspud.CVSUtil;
import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.SortCriteria;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public class ConnectionProfileModel extends AbstractTableModel {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.connection.ResourceBundle");

    private Vector profiles;

    private ConnectionProfile defaultProfile;

    private GruntspudContext context;

    private SortCriteria sortCriteria;

    /**
   * Constructor
   */
    public ConnectionProfileModel(GruntspudContext context) {
        profiles = new Vector();
        this.context = context;
        sortCriteria = new SortCriteria(context.getHost().getIntegerProperty(Constants.CONNECTION_PROFILE_TABLE_SORT_COLUMN, 0), context.getHost().getIntegerProperty(Constants.CONNECTION_PROFILE_TABLE_SORT_DIRECTION, SortCriteria.SORT_ASCENDING), false, true);
        Properties p = context.getHost().getProperties();
        try {
            HashMap map = new HashMap();
            for (Enumeration e = p.keys(); e.hasMoreElements(); ) {
                String n = (String) e.nextElement();
                if (n.startsWith(Constants.CONNECTION_PROFILE_PREFIX)) {
                    int idx = n.indexOf('.', Constants.CONNECTION_PROFILE_PREFIX.length());
                    String number = n.substring(Constants.CONNECTION_PROFILE_PREFIX.length(), idx);
                    String key = n.substring(Constants.CONNECTION_PROFILE_PREFIX.length() + number.length());
                    String val = (String) p.getProperty(n);
                    ConnectionProfile profile = (ConnectionProfile) map.get(number);
                    if (profile == null) {
                        profile = new ConnectionProfile();
                        profile.setCVSRoot(new CVSRoot());
                        map.put(number, profile);
                        addProfile(profile, false);
                    }
                    if (key.equals(Constants.CONNECTION_PROFILE_NAME_SUFFIX)) {
                        profile.setName(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_TYPE_SUFFIX)) {
                        profile.getCVSRoot().setConnectionType(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_USER_SUFFIX)) {
                        profile.getCVSRoot().setUser(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_HOST_SUFFIX)) {
                        profile.getCVSRoot().setHost(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_REPOSITORY_SUFFIX)) {
                        profile.getCVSRoot().setRepository(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_PORT_SUFFIX)) {
                        profile.getCVSRoot().setPort(Integer.parseInt(val));
                    } else if (key.equals(Constants.CONNECTION_PROFILE_DEFAULT_SUFFIX)) {
                        setDefaultProfile(profile);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_ACCESS_SUFFIX)) {
                        profile.setAccess(Integer.parseInt(val));
                    } else if (key.equals(Constants.CONNECTION_PROFILE_COMPRESSION_SUFFIX)) {
                        profile.setCompression(Integer.parseInt(val));
                    } else if (key.equals(Constants.CONNECTION_PROFILE_LINE_ENDINGS_SUFFIX)) {
                        profile.setLineEndings(Integer.parseInt(val));
                    } else if (key.equals(Constants.CONNECTION_PROFILE_WEB_CVS_URL_SUFFIX)) {
                        profile.setWebCVSURL(val);
                    } else if (key.equals(Constants.CONNECTION_PROFILE_ENCODING_SUFFIX)) {
                        profile.setEncoding(val.equals("") ? null : val);
                    } else {
                        profile.setProperty(key.substring(1), val);
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        resort();
    }

    /**
   * DOCUMENT ME!
   */
    public void resort() {
        Collections.sort(profiles, new ConnectionProfileComparator());
        fireTableDataChanged();
    }

    /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
    public SortCriteria getSortCriteria() {
        return sortCriteria;
    }

    /**
   * DOCUMENT ME!
   * 
   * @param root DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
    public ConnectionProfile getProfileForCVSRoot(String root) {
        CVSRoot r = new CVSRoot(root);
        return getProfileForCVSRoot(r);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param r DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
    public ConnectionProfile getProfileForCVSRoot(CVSRoot r) {
        if (r != null) {
            for (int i = 0; i < getRowCount(); i++) {
                boolean ignoreRepositoryCase = (r.getRepository().indexOf('\\') != -1) || (new File(r.getRepository()).isAbsolute() && (r.getRepository().length() > 2) && (r.getRepository().charAt(1) == ':') && ((r.getRepository().charAt(2) == '/') || (r.getRepository().charAt(2) == '\\')));
                ConnectionProfile p = getConnectionProfileAt(i);
                String alias = CVSUtil.getAliasForConnectionType(r.getConnectionType());
                String username = r.getUser() == null ? null : r.getUser();
                if (username != null) {
                    int idx = username.indexOf(':');
                    if (idx != -1) {
                        username = username.substring(0, idx);
                    }
                }
                if ((p.getCVSRoot().getConnectionType().equals(r.getConnectionType()) || p.getCVSRoot().getConnectionType().equals(alias)) && ((username == null) || (username.equals(p.getCVSRoot().getUser()))) && ((r.getHost() == null) || (r.getHost().equalsIgnoreCase(p.getCVSRoot().getHost()))) && ((r.getRepository() == null) || (ignoreRepositoryCase ? r.getRepository().equalsIgnoreCase(p.getCVSRoot().getRepository()) : r.getRepository().equals(p.getCVSRoot().getRepository())))) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
   * Save the current model
   */
    public void apply() {
        Properties p = context.getHost().getProperties();
        for (Enumeration e = p.keys(); e.hasMoreElements(); ) {
            String n = (String) e.nextElement();
            if (n.startsWith(Constants.CONNECTION_PROFILE_PREFIX)) {
                p.remove(n);
            }
        }
        for (int i = 0; i < getRowCount(); i++) {
            ConnectionProfile profile = getConnectionProfileAt(i);
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_TYPE_SUFFIX, profile.getCVSRoot().getConnectionType());
            if (profile.getName() != null) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_NAME_SUFFIX, profile.getName());
            }
            if (profile.getCVSRoot().getUser() != null) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_USER_SUFFIX, profile.getCVSRoot().getUser());
            }
            if (profile.getCVSRoot().getHost() != null) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_HOST_SUFFIX, profile.getCVSRoot().getHost());
            }
            if (profile.getCVSRoot().getRepository() != null) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_REPOSITORY_SUFFIX, profile.getCVSRoot().getRepository());
            }
            if (profile.getCVSRoot().getPort() != -1) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_PORT_SUFFIX, String.valueOf(profile.getCVSRoot().getPort()));
            }
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_ACCESS_SUFFIX, String.valueOf(profile.getAccess()));
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_COMPRESSION_SUFFIX, String.valueOf(profile.getCompression()));
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_LINE_ENDINGS_SUFFIX, String.valueOf(profile.getLineEndings()));
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_ENCODING_SUFFIX, profile.getEncoding() == null ? "" : profile.getEncoding());
            context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_WEB_CVS_URL_SUFFIX, profile.getWebCVSURL());
            if (profile == getDefaultProfile()) {
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + Constants.CONNECTION_PROFILE_DEFAULT_SUFFIX, "true");
            }
            for (Enumeration e = profile.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                context.getHost().setProperty(Constants.CONNECTION_PROFILE_PREFIX + i + "." + key, profile.getProperty(key));
            }
        }
        context.getHost().setIntegerProperty(Constants.CONNECTION_PROFILE_TABLE_SORT_COLUMN, sortCriteria.getSortType());
        context.getHost().setIntegerProperty(Constants.CONNECTION_PROFILE_TABLE_SORT_DIRECTION, sortCriteria.getSortDirection());
    }

    /**
   * Return the <code>ConnectionProfile</code> at the specified index
   * 
   * @param
   * @return profile
   */
    public ConnectionProfile getConnectionProfileAt(int r) {
        return (ConnectionProfile) profiles.elementAt(r);
    }

    /**
   * Gets number of connections
   * 
   * @return The rowCount value
   */
    public int getRowCount() {
        return profiles.size();
    }

    /**
   * Gets the columnCount attribute of the CVSFileNodeTableModel object
   * 
   * @return The columnCount value
   */
    public int getColumnCount() {
        return 5;
    }

    /**
   * Gets the columnName attribute of the CVSFileNodeTableModel object
   * 
   * @param columnIndex Description of the Parameter
   * @return The columnName value
   */
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return res.getString("profileTypeTableHeading");
            case 1:
                return res.getString("profileNameTableHeading");
            case 2:
                return res.getString("profileUserTableHeading");
            case 3:
                return res.getString("profileHostTableHeading");
            default:
                return res.getString("profileRepositoryTableHeading");
        }
    }

    /**
   * Gets the columnClass attribute of the CVSFileNodeTableModel object
   * 
   * @param columnIndex Description of the Parameter
   * @return The columnClass value
   */
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
   * Gets the valueAt attribute of the CVSFileNodeTableModel object
   * 
   * @param rowIndex Description of the Parameter
   * @param columnIndex Description of the Parameter
   * @return The valueAt value
   */
    public Object getValueAt(int rowIndex, int columnIndex) {
        ConnectionProfile f = getConnectionProfileAt(rowIndex);
        return getValueAt(f, columnIndex);
    }

    private Object getValueAt(ConnectionProfile profile, int columnIndex) {
        CVSRoot r = profile.getCVSRoot();
        switch(columnIndex) {
            case 0:
                return (r == null) ? null : r.getConnectionType();
            case 1:
                return profile.getName();
            case 2:
                return (r == null) ? null : r.getUser();
            case 3:
                return (r == null) ? null : r.getHost();
            default:
                return (r == null) ? null : r.getRepository();
        }
    }

    /**
   * Return the default profile
   * 
   * @return default profile
   */
    public ConnectionProfile getDefaultProfile() {
        return (defaultProfile != null) ? defaultProfile : ((getRowCount() == 0) ? null : getConnectionProfileAt(0));
    }

    /**
   * Return the default profile
   * 
   * @return default profile
   */
    public void setDefaultProfile(ConnectionProfile defaultProfile) {
        this.defaultProfile = defaultProfile;
        fireTableDataChanged();
    }

    /**
   * Remove a connection profile at a specified index
   * 
   * @param columnIndex Description of the Parameter
   * @return The columnClass value
   */
    public void removeProfileAt(int r) {
        profiles.removeElementAt(r);
        resort();
    }

    /**
   * Add a connection profile
   * 
   * @param columnIndex Description of the Parameter
   * @return The columnClass value
   */
    public void addProfile(ConnectionProfile profile) {
        addProfile(profile, true);
    }

    private void addProfile(ConnectionProfile profile, boolean resort) {
        int i = getRowCount();
        profiles.addElement(profile);
        if (resort) {
            resort();
        }
    }

    class ConnectionProfileComparator implements Comparator {

        public boolean equals(Object other) {
            return (this.equals(other));
        }

        public int compare(Object o1, Object o2) {
            Object val1 = getValueAt((ConnectionProfile) o1, sortCriteria.getSortType());
            Object val2 = getValueAt((ConnectionProfile) o2, sortCriteria.getSortType());
            int c = (val1 == null ? "" : val1.toString()).compareTo(val2 == null ? "" : val2.toString());
            return (sortCriteria.getSortDirection() == SortCriteria.SORT_ASCENDING) ? (c * -1) : c;
        }
    }

    /**
   * @param profile
   */
    public void removeProfile(ConnectionProfile profile) {
        profiles.remove(profile);
        resort();
    }

    /**
   * @param profile
   * @param index
   */
    public void insertProfileAt(ConnectionProfile profile, int index) {
        profiles.insertElementAt(profile, index);
        resort();
    }
}
