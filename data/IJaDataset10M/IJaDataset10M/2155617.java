package ro.wpcs.traser.client.ws.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import net.sf.traser.client.minimalist.Minimalist;
import net.sf.traser.databinding.base.Item;
import net.sf.traser.databinding.base.ItemList;
import net.sf.traser.databinding.base.PropertyValueResponse;
import net.sf.traser.databinding.base.PropertyValueUpdate;
import net.sf.traser.databinding.base.PropertyValuesReport;
import net.sf.traser.databinding.base.PropertyValueResponse.Value;
import org.apache.axiom.om.OMText;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import ro.wpcs.traser.controllers.LoginController;
import ro.wpcs.traser.controllers.MenuController;
import ro.wpcs.traser.controllers.NewVersionController;
import ro.wpcs.traser.logging.LoggingAdapter;
import ro.wpcs.traser.model.File;
import ro.wpcs.traser.model.FileConstants;
import ro.wpcs.traser.model.ItemConstants;
import ro.wpcs.traser.model.Project;
import ro.wpcs.traser.model.ProjectConstants;
import ro.wpcs.traser.model.User;
import ro.wpcs.traser.model.UserConstants;
import ro.wpcs.traser.model.Version;
import ro.wpcs.traser.model.VersionConstants;

/**
 * An helper adapter for working with the web service functions
 * 
 * @author Tomita Militaru, Alina Hila
 * 
 */
public class DTAdapter {

    /** Class logger. */
    private static final Logger logger = Logger.getLogger(DTAdapter.class);

    /** Action logger */
    private static final LoggingAdapter logAdapter = new LoggingAdapter();

    /** An adapter used for wrapping the basic service functions */
    private static WSAdapter adapter;

    /**
	 * Hashmap that keeps version metadata from the ws in the form of
	 * (property,value)
	 */
    private HashMap<String, String> userInfo = new HashMap<String, String>();

    public DTAdapter() {
        adapter = new WSAdapter();
    }

    /**
	 * Gets an ItemList form the server which has the requested property and
	 * requested value
	 * 
	 * @param String
	 *            property the property that exist for the requested items
	 * @param String
	 *            value the value for the property
	 * @return an ItemList containing the items that have the specific value for
	 *         the specific property
	 */
    public ItemList getItemList(final String property, final String value, Calendar from, Calendar to) {
        String[] properties = new String[1];
        String[] values = new String[1];
        properties[0] = property;
        values[0] = value;
        return getItemList(properties, values, from, to);
    }

    /**
	 * Gets an ItemList form the server which have the requested properties and
	 * requested values
	 * 
	 * @param String
	 *            [] properties the properties that exist for the requested
	 *            items
	 * @param String
	 *            [] values the values for the properties
	 * @return an ItemList containing the items that have the specific values
	 *         for the specific properties
	 */
    public ItemList getItemList(final String[] properties, final String[] values, Calendar from, Calendar to) {
        PropertyValueUpdate[] pvu = new PropertyValueUpdate[properties.length];
        for (int i = 0; i < properties.length; i++) {
            pvu[i] = adapter.createPropValueUpdate(properties[i], values[i]);
        }
        return adapter.getItemList(pvu, from, to);
    }

    /**
	 * Verifies if an item with specific properties and values already exists on
	 * the WS
	 * 
	 * @param properties
	 *            the properties of the queried item
	 * @param values
	 *            the values of the queried item
	 * @return true if item already exists, or false otherwise
	 */
    public boolean itemExists(final String[] properties, final String[] values) {
        Calendar from = Minimalist.normalize(Calendar.getInstance());
        Calendar to = Minimalist.normalize(Calendar.getInstance());
        boolean exists = false;
        ItemList itemList = getItemList(properties, values, from, to);
        if (itemList.sizeItemList() > 0) {
            exists = true;
        }
        return exists;
    }

    /**
	 * 
	 * Status change on TraSer server for selected file(s): set to UNLOCKED
	 * 
	 * @param file
	 *            the file for check in
	 */
    public boolean checkIn(File file) {
        Item item = file.getFileBO().getWSItem();
        setPropertyValue(file.getFileBO().getWSItem(), FileConstants.DESCRIPTION, NewVersionController.getDescription());
        setPropertyValue(item, FileConstants.LOCKED, String.valueOf(Boolean.FALSE));
        setPropertyValue(item, FileConstants.REMOTEPATH, ((Project) file.getParent()).getStorage() + "/" + file.getParent().getNodeText() + "/" + file.getRemoteFSFilename());
        MenuController.getInstance().setCheckinAllowed(false);
        return true;
    }

    /**
	 * Status change on TraSer server for selected file(s): set to LOCKED.
	 * 
	 * @param file
	 *            the checked out file
	 */
    public boolean checkOut(File file) {
        setPropertyValue(file.getFileBO().getWSItem(), FileConstants.LOCKED, String.valueOf(true));
        setPropertyValue(file.getFileBO().getWSItem(), FileConstants.CHECKOUT_USER, String.valueOf(file.getCheckoutUser()));
        MenuController.getInstance().setCheckinAllowed(true);
        MenuController.getInstance().setCheckoutSerialAllowed(false);
        return true;
    }

    /**
	 * Creates and returns the new created project.
	 * 
	 * @param name
	 *            the name of the project
	 * @return the new created project
	 */
    public Item newPrj(Project project) {
        String id = WSInit.calculateItemId();
        Item prjItem = adapter.createItem(id, project.getStorage());
        try {
            adapter.requestID(prjItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String properties[] = new String[8];
        properties[0] = ProjectConstants.ITEM_NAME;
        properties[1] = ProjectConstants.DESCRIPTION;
        properties[2] = ProjectConstants.PRJID;
        properties[3] = ProjectConstants.NAME;
        properties[4] = ProjectConstants.STORAGE;
        properties[5] = ProjectConstants.STATUS;
        properties[6] = ProjectConstants.TYPE;
        properties[7] = ItemConstants.HISTORY;
        adapter.createProperties(prjItem, properties);
        String[] values = new String[8];
        values[0] = project.getNodeText();
        values[1] = project.getDescription();
        values[2] = id;
        values[3] = project.getNodeText();
        values[4] = project.getStorage();
        values[5] = ItemConstants.ACTIVE;
        values[6] = String.valueOf(Project.TYPE);
        values[7] = null;
        setPropertiesValues(prjItem, properties, values);
        return prjItem;
    }

    /**
	 * Sets the status of TraSer record to DELETED.
	 * 
	 * @param prj
	 *            the deleted project.
	 */
    public boolean delete(Project prj) {
        setPropertyValue(prj.getProjectBO().getWSItem(), ProjectConstants.STATUS, ItemConstants.DELETED);
        return true;
    }

    /**
	 * Deletes file from FS. Sets status of TraSer record to DELETED.
	 * 
	 * @param file
	 *            the file to be deleted.
	 */
    public boolean delete(File file) {
        setPropertyValue(file.getFileBO().getWSItem(), FileConstants.STATUS, ItemConstants.DELETED);
        return true;
    }

    /**
	 * Sets the status of TraSer record to DELETED.
	 * 
	 * @param versions
	 *            the version of file to be deleted
	 */
    public boolean delete(Version v) {
        setPropertyValue(v.getVersionBO().getWSItem(), VersionConstants.STATUS, ItemConstants.DELETED);
        return true;
    }

    /**
	 * Sets deleted status on ws
	 * 
	 * @param user
	 *            the user to be deleted
	 */
    public void delete(User user) {
        Calendar from = Minimalist.normalize(Calendar.getInstance());
        Calendar to = Minimalist.normalize(Calendar.getInstance());
        Item userItem = getWSUser(user.getName(), user.getPassword(), from, to);
        setPropertyValue(userItem, UserConstants.STATUS, ItemConstants.DELETED);
    }

    /**
	 * Provides information about an item editing history.
	 * 
	 * @param file
	 * @param prop
	 *            the property
	 * @param startDate
	 *            the date from which we want to see the history
	 * @param endDate
	 *            the date to which we want to see the history
	 * @return the information message.
	 */
    public String getHistory(Item item, String prop, Calendar startDate, Calendar endDate) {
        String[] properties = new String[1];
        properties[0] = prop;
        PropertyValuesReport rez = adapter.getPropertyHistory(item, properties, startDate, endDate);
        String response = new String();
        for (PropertyValueResponse pvResp : rez.getProperties()) {
            System.out.println(pvResp.getPropertyName() + ":");
            if (pvResp.sizeValue() > 0) {
                for (Value val : pvResp.getValues()) {
                    try {
                        response += XMLUtils.toOM(val.getAny()).getText() + " --- " + val.getFrom().toString() + "\n";
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(DTAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return response;
    }

    /**
	 * Provides information about project.
	 * 
	 * @return the information HashMap.
	 */
    public HashMap<String, String> getPrjInfo(Item item) {
        logger.info("Getting project property values from web service ...");
        HashMap<String, String> prjMetadata = new HashMap<String, String>();
        String property[] = new String[6];
        property[0] = ProjectConstants.NAME;
        property[1] = ProjectConstants.ITEM_NAME;
        property[2] = ProjectConstants.PRJID;
        property[3] = ProjectConstants.STORAGE;
        property[4] = ProjectConstants.DESCRIPTION;
        property[5] = ProjectConstants.STATUS;
        String[] propertyValues = null;
        try {
            propertyValues = getPropertyValues(item, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        prjMetadata.put(ProjectConstants.NAME, propertyValues[0]);
        prjMetadata.put(ProjectConstants.ITEM_NAME, propertyValues[1]);
        prjMetadata.put(ProjectConstants.PRJID, propertyValues[2]);
        prjMetadata.put(ProjectConstants.STORAGE, propertyValues[3]);
        prjMetadata.put(ProjectConstants.DESCRIPTION, propertyValues[4]);
        prjMetadata.put(ProjectConstants.STATUS, propertyValues[5]);
        return prjMetadata;
    }

    public List<HashMap<String, String>> getPrjsInfo(Item[] items) {
        logger.info("Getting project property values from web service ...");
        List<HashMap<String, String>> prjMetadataValues = new ArrayList<HashMap<String, String>>();
        String property[] = new String[6];
        property[0] = ProjectConstants.NAME;
        property[1] = ProjectConstants.ITEM_NAME;
        property[2] = ProjectConstants.PRJID;
        property[3] = ProjectConstants.STORAGE;
        property[4] = ProjectConstants.DESCRIPTION;
        property[5] = ProjectConstants.STATUS;
        List<String[]> propertyValuesList = null;
        try {
            propertyValuesList = getBulkPropertyValues(items, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        HashMap<String, String> prjMetadata;
        for (String[] propertyValues : propertyValuesList) {
            prjMetadata = new HashMap<String, String>();
            prjMetadata.put(ProjectConstants.NAME, propertyValues[0]);
            prjMetadata.put(ProjectConstants.ITEM_NAME, propertyValues[1]);
            prjMetadata.put(ProjectConstants.PRJID, propertyValues[2]);
            prjMetadata.put(ProjectConstants.STORAGE, propertyValues[3]);
            prjMetadata.put(ProjectConstants.DESCRIPTION, propertyValues[4]);
            prjMetadata.put(ProjectConstants.STATUS, propertyValues[5]);
            prjMetadataValues.add(prjMetadata);
        }
        return prjMetadataValues;
    }

    /**
	 * Provides information about file
	 * 
	 * @return the information HashMap.
	 */
    public HashMap<String, String> getFileInfo(Item item) {
        logger.info("Getting file property values for file from web service...");
        HashMap<String, String> fileMetadata = new HashMap<String, String>();
        String property[] = new String[17];
        property[0] = FileConstants.ITEM_NAME;
        property[1] = FileConstants.FILEID;
        property[2] = FileConstants.NAME;
        property[3] = FileConstants.DESCRIPTION;
        property[4] = FileConstants.FILETYPE;
        property[5] = FileConstants.FILEVERSION;
        property[6] = FileConstants.STATUS;
        property[7] = FileConstants.LOCKED;
        property[8] = FileConstants.MAINFILEID;
        property[9] = FileConstants.PRJID;
        property[10] = FileConstants.OWNER;
        property[11] = FileConstants.EDITORS;
        property[12] = FileConstants.COLLABORATORS;
        property[13] = FileConstants.REMOTEPATH;
        property[14] = FileConstants.CHECKOUT_USER;
        property[15] = FileConstants.REMOTE_NAME;
        property[16] = FileConstants.ENCRYPTION_KEY;
        String[] propertyValues = null;
        try {
            propertyValues = getPropertyValues(item, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 16; i++) {
            logger.debug(property[i] + ": " + propertyValues[i]);
        }
        fileMetadata.put(FileConstants.ITEM_NAME, propertyValues[0]);
        fileMetadata.put(FileConstants.FILEID, propertyValues[1]);
        fileMetadata.put(FileConstants.NAME, propertyValues[2]);
        fileMetadata.put(FileConstants.DESCRIPTION, propertyValues[3]);
        fileMetadata.put(FileConstants.FILETYPE, propertyValues[4]);
        fileMetadata.put(FileConstants.FILEVERSION, propertyValues[5]);
        fileMetadata.put(FileConstants.STATUS, propertyValues[6]);
        fileMetadata.put(FileConstants.LOCKED, propertyValues[7]);
        fileMetadata.put(FileConstants.MAINFILEID, propertyValues[8]);
        fileMetadata.put(FileConstants.PRJID, propertyValues[9]);
        fileMetadata.put(FileConstants.OWNER, propertyValues[10]);
        fileMetadata.put(FileConstants.EDITORS, propertyValues[11]);
        fileMetadata.put(FileConstants.COLLABORATORS, propertyValues[12]);
        fileMetadata.put(FileConstants.REMOTEPATH, propertyValues[13]);
        fileMetadata.put(FileConstants.CHECKOUT_USER, propertyValues[14]);
        fileMetadata.put(FileConstants.REMOTE_NAME, propertyValues[15]);
        fileMetadata.put(FileConstants.ENCRYPTION_KEY, propertyValues[16]);
        return fileMetadata;
    }

    /**
	 * Provides information about multiple files
	 * 
	 * @return the information HashMap.
	 */
    public List<HashMap<String, String>> getFilesInfo(Item[] items) {
        logger.info("Getting file property values for file from web service...");
        List<HashMap<String, String>> fileMetadataResult = new ArrayList<HashMap<String, String>>();
        String property[] = new String[17];
        property[0] = FileConstants.ITEM_NAME;
        property[1] = FileConstants.FILEID;
        property[2] = FileConstants.NAME;
        property[3] = FileConstants.DESCRIPTION;
        property[4] = FileConstants.FILETYPE;
        property[5] = FileConstants.FILEVERSION;
        property[6] = FileConstants.STATUS;
        property[7] = FileConstants.LOCKED;
        property[8] = FileConstants.MAINFILEID;
        property[9] = FileConstants.PRJID;
        property[10] = FileConstants.OWNER;
        property[11] = FileConstants.EDITORS;
        property[12] = FileConstants.COLLABORATORS;
        property[13] = FileConstants.REMOTEPATH;
        property[14] = FileConstants.CHECKOUT_USER;
        property[15] = FileConstants.REMOTE_NAME;
        property[16] = FileConstants.ENCRYPTION_KEY;
        List<String[]> propertyValuesList = null;
        try {
            propertyValuesList = getBulkPropertyValues(items, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        HashMap<String, String> fileMetadata;
        for (String[] propertyValues : propertyValuesList) {
            fileMetadata = new HashMap<String, String>();
            fileMetadata.put(FileConstants.ITEM_NAME, propertyValues[0]);
            fileMetadata.put(FileConstants.FILEID, propertyValues[1]);
            fileMetadata.put(FileConstants.NAME, propertyValues[2]);
            fileMetadata.put(FileConstants.DESCRIPTION, propertyValues[3]);
            fileMetadata.put(FileConstants.FILETYPE, propertyValues[4]);
            fileMetadata.put(FileConstants.FILEVERSION, propertyValues[5]);
            fileMetadata.put(FileConstants.STATUS, propertyValues[6]);
            fileMetadata.put(FileConstants.LOCKED, propertyValues[7]);
            fileMetadata.put(FileConstants.MAINFILEID, propertyValues[8]);
            fileMetadata.put(FileConstants.PRJID, propertyValues[9]);
            fileMetadata.put(FileConstants.OWNER, propertyValues[10]);
            fileMetadata.put(FileConstants.EDITORS, propertyValues[11]);
            fileMetadata.put(FileConstants.COLLABORATORS, propertyValues[12]);
            fileMetadata.put(FileConstants.REMOTEPATH, propertyValues[13]);
            fileMetadata.put(FileConstants.CHECKOUT_USER, propertyValues[14]);
            fileMetadata.put(FileConstants.REMOTE_NAME, propertyValues[15]);
            fileMetadata.put(FileConstants.ENCRYPTION_KEY, propertyValues[16]);
            fileMetadataResult.add(fileMetadata);
        }
        return fileMetadataResult;
    }

    /**
	 * Provides information about version
	 * 
	 * @return the information HashMap.
	 */
    public HashMap<String, String> getVersInfo(Item item) {
        logger.info("Getting property values for versions from web service...");
        HashMap<String, String> versMetadata = new HashMap<String, String>();
        String property[] = new String[9];
        property[0] = VersionConstants.NAME;
        property[1] = VersionConstants.VERSID;
        property[2] = VersionConstants.TYPE;
        property[3] = VersionConstants.SYMBOL;
        property[4] = VersionConstants.STATUS;
        property[5] = VersionConstants.FILEID;
        property[6] = VersionConstants.DESCRIPTION;
        property[7] = VersionConstants.ENCRYPTION_KEY;
        property[8] = FileConstants.REMOTE_NAME;
        String[] propertyValues = null;
        try {
            propertyValues = getPropertyValues(item, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        versMetadata.put(VersionConstants.NAME, propertyValues[0]);
        versMetadata.put(VersionConstants.VERSID, propertyValues[1]);
        versMetadata.put(VersionConstants.TYPE, propertyValues[2]);
        versMetadata.put(VersionConstants.SYMBOL, propertyValues[3]);
        versMetadata.put(VersionConstants.STATUS, propertyValues[4]);
        versMetadata.put(VersionConstants.FILEID, propertyValues[5]);
        versMetadata.put(VersionConstants.DESCRIPTION, propertyValues[6]);
        versMetadata.put(VersionConstants.ENCRYPTION_KEY, propertyValues[7]);
        versMetadata.put(FileConstants.REMOTE_NAME, propertyValues[8]);
        return versMetadata;
    }

    /**
	 * Provides information about version
	 * 
	 * @return the information HashMap.
	 */
    public List<HashMap<String, String>> getVersionsInfo(Item[] items) {
        logger.info("Getting property values for versions from web service...");
        List<HashMap<String, String>> versMetadataResult = new ArrayList<HashMap<String, String>>();
        String property[] = new String[9];
        property[0] = VersionConstants.NAME;
        property[1] = VersionConstants.VERSID;
        property[2] = VersionConstants.TYPE;
        property[3] = VersionConstants.SYMBOL;
        property[4] = VersionConstants.STATUS;
        property[5] = VersionConstants.FILEID;
        property[6] = VersionConstants.DESCRIPTION;
        property[7] = VersionConstants.ENCRYPTION_KEY;
        property[8] = FileConstants.REMOTE_NAME;
        List<String[]> propertyValuesList = null;
        try {
            propertyValuesList = getBulkPropertyValues(items, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        HashMap<String, String> versMetadata;
        for (String[] propertyValues : propertyValuesList) {
            versMetadata = new HashMap<String, String>();
            versMetadata.put(VersionConstants.NAME, propertyValues[0]);
            versMetadata.put(VersionConstants.VERSID, propertyValues[1]);
            versMetadata.put(VersionConstants.TYPE, propertyValues[2]);
            versMetadata.put(VersionConstants.SYMBOL, propertyValues[3]);
            versMetadata.put(VersionConstants.STATUS, propertyValues[4]);
            versMetadata.put(VersionConstants.FILEID, propertyValues[5]);
            versMetadata.put(VersionConstants.DESCRIPTION, propertyValues[6]);
            versMetadata.put(VersionConstants.ENCRYPTION_KEY, propertyValues[7]);
            versMetadata.put(FileConstants.REMOTE_NAME, propertyValues[8]);
            versMetadataResult.add(versMetadata);
        }
        return versMetadataResult;
    }

    /**
	 * @param user
	 *            the user queried for it's information
	 * @return Returns a HashMap with web service values for a
	 *         user(property,value)
	 */
    public HashMap<String, String> getInfo(User user) {
        logger.info("Getting property values for user from web service...");
        Item item = user.getUserBO().getWSItem();
        String property[] = new String[6];
        property[0] = UserConstants.ITEM_NAME;
        property[1] = UserConstants.USERID;
        property[2] = UserConstants.NAME;
        property[3] = UserConstants.PASS;
        property[4] = UserConstants.FLAG;
        property[5] = UserConstants.EMAIL;
        String[] propertyValues = null;
        try {
            propertyValues = getPropertyValues(item, property);
        } catch (NotAPropertyException e) {
            e.printStackTrace();
        }
        userInfo.put(UserConstants.ITEM_NAME, propertyValues[0]);
        userInfo.put(UserConstants.USERID, propertyValues[1]);
        userInfo.put(UserConstants.NAME, propertyValues[2]);
        userInfo.put(UserConstants.PASS, propertyValues[3]);
        userInfo.put(UserConstants.FLAG, propertyValues[4]);
        userInfo.put(UserConstants.EMAIL, propertyValues[5]);
        return userInfo;
    }

    /**
	 * Creates a new file. This generates a new item on TraSer server.
	 * 
	 * @param file
	 *            the filename
	 */
    public Item newFile(File file) {
        String id = WSInit.calculateItemId();
        Item fileItem = adapter.createItem(id, ((Project) (file.getParent())).getStorage());
        try {
            adapter.requestID(fileItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] properties = new String[20];
        properties[0] = FileConstants.ITEM_NAME;
        properties[1] = FileConstants.DESCRIPTION;
        properties[2] = FileConstants.FILEID;
        properties[3] = FileConstants.FILETYPE;
        properties[4] = FileConstants.FILEVERSION;
        properties[5] = FileConstants.LOCKED;
        properties[6] = FileConstants.MAINFILEID;
        properties[7] = FileConstants.NAME;
        properties[8] = FileConstants.PRJID;
        properties[9] = FileConstants.STATUS;
        properties[10] = FileConstants.REMOTEPATH;
        properties[11] = FileConstants.TYPE;
        properties[12] = FileConstants.NO_OF_VERSIONS;
        properties[13] = FileConstants.OWNER;
        properties[14] = FileConstants.EDITORS;
        properties[15] = FileConstants.COLLABORATORS;
        properties[16] = FileConstants.CHECKOUT_USER;
        properties[17] = FileConstants.REMOTE_NAME;
        properties[18] = FileConstants.ENCRYPTION_KEY;
        properties[19] = ItemConstants.HISTORY;
        adapter.createProperties(fileItem, properties);
        String[] values = new String[20];
        values[0] = file.getNodeText();
        values[1] = file.getDescription();
        values[2] = String.valueOf(id);
        values[3] = file.getFileType();
        values[4] = file.getFileVersion();
        values[5] = String.valueOf(file.isLocked());
        values[6] = String.valueOf(file.getMainFileID());
        values[7] = file.getNodeText();
        values[8] = String.valueOf(((Project) file.getParent()).getId());
        values[9] = file.getStatus();
        values[10] = file.getRemotePath();
        values[11] = String.valueOf(File.TYPE);
        values[12] = "1";
        values[13] = String.valueOf(LoginController.getLoggedUser().getId());
        values[14] = null;
        values[15] = null;
        values[16] = null;
        values[17] = file.getRemoteFSFilename();
        values[18] = file.getEncryptionKey();
        values[19] = null;
        setPropertiesValues(fileItem, properties, values);
        return fileItem;
    }

    /**
	 * Offers capability to create new versions. Versions can be added for any
	 * existing file items
	 * 
	 * @param versioname
	 *            the name of the new version.
	 * @return the new created version.
	 */
    public Item newVersion(Version version) {
        String id = WSInit.calculateItemId();
        Item versionItem = adapter.createItem(id, ((Project) (((File) (version.getParent())).getParent())).getStorage());
        try {
            adapter.requestID(versionItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] properties = new String[12];
        properties[0] = VersionConstants.NAME;
        properties[1] = VersionConstants.DESCRIPTION;
        properties[2] = VersionConstants.FILEID;
        properties[3] = VersionConstants.SYMBOL;
        properties[4] = VersionConstants.TYPE;
        properties[5] = VersionConstants.VERSID;
        properties[6] = VersionConstants.STATUS;
        properties[7] = VersionConstants.TYPE;
        properties[8] = VersionConstants.COUNTER;
        properties[9] = VersionConstants.OWNER;
        properties[10] = VersionConstants.ENCRYPTION_KEY;
        properties[11] = FileConstants.REMOTE_NAME;
        adapter.createProperties(versionItem, properties);
        String[] values = new String[12];
        values[0] = version.getName();
        values[1] = version.getDescription();
        values[2] = String.valueOf(version.getFileId());
        values[3] = version.getNodeText();
        values[4] = ((File) (version.getParent())).getFileType();
        values[5] = id;
        values[6] = ItemConstants.ACTIVE;
        values[7] = String.valueOf(Version.TYPE);
        values[8] = null;
        values[9] = LoginController.getLoggedWSUserItem().getItemId();
        values[10] = version.getEncryptionKey();
        values[11] = version.getRemoteFSFilename();
        setPropertiesValues(versionItem, properties, values);
        return versionItem;
    }

    /**
	 * Creates a user on the WS and returns the Item
	 * 
	 * @param user
	 *            the user to be created
	 * @return the user Item
	 */
    public Item createUser(User user) {
        String id = String.valueOf(user.getId());
        Item userItem = adapter.createItem(id, "user");
        try {
            adapter.requestID(userItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] properties = new String[8];
        properties[0] = UserConstants.ITEM_NAME;
        properties[1] = UserConstants.USERID;
        properties[2] = UserConstants.NAME;
        properties[3] = UserConstants.PASS;
        properties[4] = UserConstants.FLAG;
        properties[5] = UserConstants.STATUS;
        properties[6] = UserConstants.EMAIL;
        properties[7] = UserConstants.TYPE;
        adapter.createProperties(userItem, properties);
        String values[] = new String[8];
        values[0] = user.getName();
        values[1] = id;
        values[2] = user.getName();
        values[3] = user.getPassword();
        values[4] = user.getFlag();
        values[5] = ItemConstants.ACTIVE;
        values[6] = user.getEmailAdress();
        values[7] = String.valueOf(User.TYPE);
        setPropertiesValues(userItem, properties, values);
        logger.info("Have created user: " + user);
        return userItem;
    }

    /**
	 * Enables updating of File definition data, on TraSer server.
	 * 
	 * @param file
	 *            the new file definition data
	 * @return true if successful, false if else
	 */
    public boolean update(File file) {
        Item fileItem = file.getFileBO().getWSItem();
        String[] properties = new String[20];
        properties[0] = FileConstants.ITEM_NAME;
        properties[1] = FileConstants.DESCRIPTION;
        properties[2] = FileConstants.FILEID;
        properties[3] = FileConstants.FILETYPE;
        properties[4] = FileConstants.FILEVERSION;
        properties[5] = FileConstants.LOCKED;
        properties[6] = FileConstants.MAINFILEID;
        properties[7] = FileConstants.NAME;
        properties[8] = FileConstants.PRJID;
        properties[9] = FileConstants.STATUS;
        properties[10] = FileConstants.REMOTEPATH;
        properties[11] = FileConstants.TYPE;
        properties[12] = FileConstants.NO_OF_VERSIONS;
        properties[13] = FileConstants.OWNER;
        properties[14] = FileConstants.EDITORS;
        properties[15] = FileConstants.COLLABORATORS;
        properties[16] = FileConstants.CHECKOUT_USER;
        properties[17] = FileConstants.REMOTE_NAME;
        properties[18] = FileConstants.ENCRYPTION_KEY;
        properties[19] = ItemConstants.HISTORY;
        String values[] = new String[20];
        values[0] = null;
        values[1] = file.getDescription();
        values[2] = String.valueOf(file.getFileID());
        values[3] = file.getFileType();
        values[4] = file.getFileVersion();
        values[5] = String.valueOf(file.isLocked());
        values[6] = String.valueOf(file.getMainFileID());
        values[7] = file.getNodeText();
        values[8] = String.valueOf(((Project) file.getParent()).getId());
        values[9] = file.getStatus();
        values[10] = file.getRemotePath();
        values[11] = String.valueOf(File.TYPE);
        values[12] = null;
        values[13] = file.getOwner();
        values[14] = null;
        values[15] = null;
        values[16] = null;
        values[17] = null;
        values[18] = file.getEncryptionKey();
        values[19] = null;
        setPropertiesValues(fileItem, properties, values);
        return true;
    }

    /**
	 * It changes the server's configuration from an external .properties file.
	 * 
	 * @param prop
	 *            the properties file.
	 */
    public boolean updateRegistration(Properties p) {
        return true;
    }

    /**
	 * Sets the property of an item.
	 * 
	 * @param item
	 *            the item to be changed
	 * @param prop
	 *            the property
	 * @param value
	 *            the value
	 */
    public void setPropertyValue(Item item, String prop, String value) {
        adapter.createEvent(item, prop, value);
    }

    /**
	 * Sets the property of an item.
	 * 
	 * @param item
	 *            the item to be changed
	 * @param prop
	 *            the property
	 * @param value
	 *            the value
	 */
    public void setPropertiesValues(Item item, String[] props, String[] values) {
        adapter.createEventForMultipleProps(item, props, values);
    }

    /**
	 * Returns a value for a property
	 * 
	 * @param item
	 *            the item to be interrogated
	 * @param property
	 *            the property name
	 * @return the property value
	 * @throws NotAPropertyException
	 */
    public String getPropertyValue(Item item, String property) {
        String[] properties = new String[1];
        String[] values = new String[1];
        properties[0] = property;
        try {
            values = getPropertyValues(item, properties);
        } catch (NotAPropertyException e) {
            logger.info("Property exception: " + e.getMessage());
        }
        return values[0];
    }

    /**
	 * Returns the property values of an item
	 * 
	 * @param item
	 *            the item
	 * @param property
	 *            the property
	 * @throws NotAPropertyException
	 */
    public String[] getPropertyValues(Item item, String property[]) throws NotAPropertyException {
        String[] values = new String[20];
        int k = 0;
        PropertyValueResponse[] val = adapter.getPropertyValuesAt(item, property);
        if ((val == null) || (val.length == 0)) {
            throw new NotAPropertyException();
        }
        for (int i = 0; i < val.length; i++) {
            if (val[i].sizeValue() > 0) {
                try {
                    Iterator<OMText> iterator = XMLUtils.toOM(val[i].getValue(0).getAny()).getChildren();
                    if (iterator.hasNext()) {
                        String numeric = iterator.next().getText();
                        values[k++] = numeric;
                    } else {
                        String value = XMLUtils.toOM(val[i].getValue(0).getAny()).getLocalName();
                        values[k++] = value;
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(DTAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                values[k++] = null;
            }
        }
        return values;
    }

    /**
	 * Returns the property values for multiple items
	 * 
	 * @param item
	 *            the item
	 * @param property
	 *            the property
	 * @throws NotAPropertyException
	 */
    public List<String[]> getBulkPropertyValues(Item items[], String property[]) throws NotAPropertyException {
        String[] values;
        List<String[]> result = new ArrayList<String[]>();
        int k;
        PropertyValuesReport[] vals = adapter.bulk(items, property);
        for (int setIndex = 0; setIndex < vals.length; setIndex++) {
            PropertyValueResponse[] val = vals[setIndex].getProperties().toArray(new PropertyValueResponse[] {});
            if ((val == null) || (val.length == 0)) {
                throw new NotAPropertyException();
            }
            values = new String[20];
            k = 0;
            for (int pIndex = 0; pIndex < val.length; pIndex++) {
                if (val[pIndex].sizeValue() > 0) {
                    try {
                        Iterator<OMText> iterator = XMLUtils.toOM(val[pIndex].getValue(0).getAny()).getChildren();
                        if (iterator.hasNext()) {
                            String numeric = iterator.next().getText();
                            values[k++] = numeric;
                        } else {
                            String value = XMLUtils.toOM(val[pIndex].getValue(0).getAny()).getLocalName();
                            values[k++] = value;
                        }
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(DTAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    values[k++] = null;
                }
            }
            result.add(values);
        }
        return result;
    }

    /**
	 * Returns the user with the given name and password
	 * 
	 * @param name
	 *            the user name
	 * @param pass
	 *            the user password
	 * @param from
	 *            current date
	 * @param to
	 *            current date
	 * @return the user as an item
	 */
    public Item getWSUser(String name, String pass, Calendar from, Calendar to) {
        String[] properties = new String[2];
        String[] values = new String[2];
        properties[0] = UserConstants.NAME;
        properties[1] = UserConstants.PASS;
        values[0] = name;
        values[1] = pass;
        ItemList items = getItemList(properties, values, from, to);
        if (items.sizeItemList() > 0) {
            logger.info("Found valid user!");
            return items.getItemList(0);
        } else {
            logger.info("The user does not exist!");
            return null;
        }
    }
}
