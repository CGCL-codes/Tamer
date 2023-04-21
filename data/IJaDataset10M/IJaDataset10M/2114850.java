package net.sourceforge.cruisecontrol.sourcecontrols;

import com.starbase.starteam.File;
import com.starbase.starteam.Folder;
import com.starbase.starteam.Item;
import com.starbase.starteam.TypeNotFoundException;
import com.starbase.starteam.PropertyNames;
import com.starbase.starteam.Server;
import com.starbase.starteam.ServerException;
import com.starbase.starteam.StarTeamFinder;
import com.starbase.starteam.TypeNames;
import com.starbase.starteam.User;
import com.starbase.starteam.UserAccount;
import com.starbase.starteam.View;
import com.starbase.starteam.ViewConfiguration;
import com.starbase.starteam.PropertyEnums;
import com.starbase.util.OLEDate;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Modification;
import net.sourceforge.cruisecontrol.SourceControl;
import net.sourceforge.cruisecontrol.util.ValidationHelper;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class logs into StarTeam and collects information on any modifications
 * made since the last successful build.
 *
 * @author Christopher Charlier -- ThoughtWorks Inc. 2001
 * @author <a href="mailto:jcyip@thoughtworks.com">Jason Yip</a>
 * @author Neill
 * @author Ben Burgess
 */
public class StarTeam implements SourceControl {

    private static final Logger LOG = Logger.getLogger(StarTeam.class);

    private String userName;

    private String password;

    private String folder;

    private String url;

    private List modifications = new ArrayList();

    private OLEDate nowDate;

    private Folder lastBuildRoot;

    private PropertyNames stPropertyNames;

    private PropertyEnums stPropertyEnums;

    private Server server;

    private TypeNames stTypeNames;

    private SourceControlProperties properties = new SourceControlProperties();

    private String property;

    private String propertyOnDelete;

    private boolean preloadFileInformation = true;

    private boolean canLookupEmails = true;

    private boolean canLookupDeletions = true;

    /**
     * Set StarTeam user name
     */
    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
     * Set password for StarTeam user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set repository folder
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setPreloadFileInformation(boolean preloadFileInformation) {
        this.preloadFileInformation = preloadFileInformation;
    }

    public void setStarteamurl(String url) {
        this.url = url;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setPropertyOnDelete(String propertyOnDelete) {
        this.propertyOnDelete = propertyOnDelete;
    }

    public Map getProperties() {
        return properties.getPropertiesAndReset();
    }

    public void validate() throws CruiseControlException {
        ValidationHelper.assertIsSet(folder, "folder", this.getClass());
        ValidationHelper.assertIsSet(url, "url", this.getClass());
        ValidationHelper.assertIsSet(userName, "username", this.getClass());
        ValidationHelper.assertIsSet(password, "password", this.getClass());
    }

    /**
     * Populates the modification set with all appropriate information based on
     * the changes since the last successful build.
     */
    public List getModifications(Date lastBuild, Date now) {
        modifications.clear();
        nowDate = new OLEDate(now.getTime());
        OLEDate lastBuildDate = new OLEDate(lastBuild.getTime());
        server = null;
        try {
            View view = StarTeamFinder.openView(userName + ":" + password + "@" + url);
            server = view.getServer();
            View snapshotAtNow = new View(view, ViewConfiguration.createFromTime(nowDate));
            View snapshotAtLastBuild = new View(view, ViewConfiguration.createFromTime(lastBuildDate));
            Map nowFiles = new HashMap();
            Map lastBuildFiles = new HashMap();
            Folder nowRoot = StarTeamFinder.findFolder(snapshotAtNow.getRootFolder(), folder);
            stPropertyNames = server.getPropertyNames();
            stTypeNames = server.getTypeNames();
            stPropertyEnums = server.getPropertyEnums();
            final String[] propertiesToCache = new String[] { stPropertyNames.FILE_CONTENT_REVISION, stPropertyNames.MODIFIED_TIME, stPropertyNames.FILE_FILE_TIME_AT_CHECKIN, stPropertyNames.COMMENT, stPropertyNames.MODIFIED_USER_ID, stPropertyNames.FILE_NAME };
            if (preloadFileInformation) {
                nowRoot.populateNow(stTypeNames.FILE, propertiesToCache, -1);
            }
            addFolderModsToList(nowFiles, nowRoot);
            try {
                lastBuildRoot = StarTeamFinder.findFolder(snapshotAtLastBuild.getRootFolder(), folder);
                if (preloadFileInformation) {
                    lastBuildRoot.populateNow(stTypeNames.FILE, propertiesToCache, -1);
                }
                addFolderModsToList(lastBuildFiles, lastBuildRoot);
            } catch (ServerException se) {
                LOG.error(url + ": Server Exception occurred visiting last build view: ", se);
            }
            compareFileLists(nowFiles, lastBuildFiles);
            snapshotAtNow.getRootFolder().discardItems(stTypeNames.FILE, -1);
            try {
                snapshotAtLastBuild.getRootFolder().discardItems(stTypeNames.FILE, -1);
            } catch (ServerException se) {
                LOG.error(url + ": Server Exception occurred discarding last build file cache: ", se);
            }
            LOG.info(url + ": " + modifications.size() + " modifications in " + nowRoot.getFolderHierarchy());
            return modifications;
        } catch (Exception e) {
            LOG.error(url + ": Problem looking up modifications in StarTeam.", e);
            modifications.clear();
            return modifications;
        } finally {
            if (server != null) {
                server.disconnect();
            }
        }
    }

    /**
     * Compare old and new file lists to determine what happened
     */
    private void compareFileLists(Map nowFiles, Map lastBuildFiles) {
        for (Iterator iter = nowFiles.keySet().iterator(); iter.hasNext(); ) {
            Integer currentItemID = (Integer) iter.next();
            File currentFile = (File) nowFiles.get(currentItemID);
            if (lastBuildFiles.containsKey(currentItemID)) {
                File lastBuildFile = (File) lastBuildFiles.get(currentItemID);
                if (fileHasBeenModified(currentFile, lastBuildFile)) {
                    addRevision(currentFile, "modified");
                } else if (fileHasBeenMoved(currentFile, lastBuildFile)) {
                    addRevision(currentFile, "moved");
                }
                lastBuildFiles.remove(currentItemID);
            } else {
                addRevision(currentFile, "new");
            }
        }
        examineOldFiles(lastBuildFiles);
    }

    /**
     * Now examine old files.  They have to have been deleted as we know they
     * are not in the new list from the processing above.
     */
    private void examineOldFiles(Map lastBuildFiles) {
        if (canLookupDeletions && preloadFileInformation && !lastBuildFiles.isEmpty()) {
            try {
                final String[] propertiesToCacheForDeletes = new String[] { stPropertyNames.AUDIT_CLASS_ID, stPropertyNames.AUDIT_OBJECT_ID, stPropertyNames.AUDIT_EVENT_ID, stPropertyNames.MODIFIED_TIME, stPropertyNames.AUDIT_USER_ID };
                lastBuildRoot.populateNow(stTypeNames.AUDIT, propertiesToCacheForDeletes, -1);
            } catch (TypeNotFoundException tnfx) {
                LOG.debug(url + ": Error caching Audit information (StarTeam 4.2 SP2 SDK or older in use)." + "  Deletions will be reported as by Unknown.", tnfx);
                canLookupDeletions = false;
            }
        }
        for (Iterator iter = lastBuildFiles.values().iterator(); iter.hasNext(); ) {
            File currentLastBuildFile = (File) iter.next();
            addRevision((File) currentLastBuildFile.getFromHistoryByDate(nowDate), "deleted");
        }
    }

    private boolean fileHasBeenModified(File currentFile, File lastBuildFile) {
        return currentFile.getContentVersion() != lastBuildFile.getContentVersion();
    }

    private boolean fileHasBeenMoved(File currentFile, File lastBuildFile) {
        return !currentFile.getParentFolder().getFolderHierarchy().equals(lastBuildFile.getParentFolder().getFolderHierarchy());
    }

    private void addFolderModsToList(Map fileList, Folder folder) {
        Item[] files = folder.getItems(stTypeNames.FILE);
        for (int i = 0; i < files.length; i++) {
            File file = (File) files[i];
            fileList.put(new Integer(file.getItemID()), file);
        }
        Folder[] folders = folder.getSubFolders();
        for (int i = 0; i < folders.length; i++) {
            addFolderModsToList(fileList, folders[i]);
        }
    }

    /**
     * Adds the revision to the modification set.
     *
     * @param revision
     */
    private void addRevision(File revision, String status) {
        Modification mod = new Modification();
        mod.type = "StarTeam";
        String fileName = revision.getName();
        Folder parentFolder = revision.getParentFolder();
        String folderName = parentFolder.getFolderHierarchy();
        if (folderName.length() > 0 && (folderName.endsWith("\\") || folderName.endsWith("/"))) {
            folderName = folderName.substring(0, folderName.length() - 1);
        }
        Modification.ModifiedFile modFile = mod.createModifiedFile(fileName, folderName);
        modFile.action = status;
        User user = null;
        if (property != null) {
            properties.put(property, "true");
        }
        if (status.equals("deleted")) {
            if (propertyOnDelete != null) {
                properties.put(propertyOnDelete, "true");
            }
            boolean foundAudit = false;
            if (canLookupDeletions) {
                try {
                    Item[] audits = parentFolder.getItems(stTypeNames.AUDIT);
                    for (int i = 0; i < audits.length && !foundAudit; i++) {
                        com.starbase.starteam.Audit audit = (com.starbase.starteam.Audit) (audits[i]);
                        if (audit.getItemDescriptor().equals(fileName) && audit.getInt(stPropertyNames.AUDIT_EVENT_ID) == stPropertyEnums.AUDIT_EVENT_ID_DELETED) {
                            foundAudit = true;
                            mod.modifiedTime = audit.getModifiedTime().createDate();
                            user = server.getUser(audit.getInt(stPropertyNames.AUDIT_USER_ID));
                            mod.userName = user.getName();
                        }
                    }
                } catch (TypeNotFoundException tnfx) {
                    LOG.debug(url + ": Error looking up Audit information (StarTeam 4.2 SP2 SDK or older in use)." + "  Deletions will be reported as by Unknown.", tnfx);
                    canLookupDeletions = false;
                }
            }
            if (!foundAudit) {
                mod.modifiedTime = revision.getModifiedTime().createDate();
                mod.userName = "Unknown";
            }
        } else {
            mod.modifiedTime = revision.getModifiedTime().createDate();
            user = server.getUser(revision.getModifiedBy());
            mod.userName = user.getName();
            mod.comment = revision.getComment();
            mod.revision = "" + revision.getContentVersion();
        }
        if (user != null && canLookupEmails) {
            try {
                UserAccount useracct = server.getAdministration().findUserAccount(user.getID());
                if (useracct == null) {
                    LOG.warn(url + ": User account for " + user.getName() + " with ID " + user.getID() + " not found.");
                } else {
                    mod.emailAddress = useracct.getEmailAddress();
                }
            } catch (ServerException sx) {
                LOG.debug(url + ": Error looking up user email address.", sx);
                canLookupEmails = false;
            }
        }
        modifications.add(mod);
    }
}
