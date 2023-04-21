package org.oclc.da.ndiipp.fileinfo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.common.guid.GUIDGenerator;
import org.oclc.da.ndiipp.common.pvt.storage.db.SimpleRDBMS;
import org.oclc.da.ndiipp.common.pvt.storage.db.SimpleRDBMSConst;
import org.oclc.da.ndiipp.spider.FileInfo;
import org.oclc.da.ndiipp.spider.viewer.ContentViewer;
import org.oclc.da.ndiipp.struts.core.util.WebsiteBean;

/**
 * This class helps with populating and querying file info rows from the database.
 * 
 * @author MJT
 *
 */
public class FileInfoHelper {

    /** Logger instance. */
    private Logger logger = Logger.newInstance();

    private SimpleRDBMS srdbms = null;

    private String harvestLocation = null;

    private ContentViewer viewer = null;

    /** Constructor
	 * @throws DAException
	 */
    public FileInfoHelper() throws DAException {
        srdbms = new SimpleRDBMS();
    }

    /** Constructor
	 * @param harvestLocation 
	 * @throws DAException
	 */
    public FileInfoHelper(String harvestLocation) throws DAException {
        srdbms = new SimpleRDBMS();
        this.harvestLocation = harvestLocation;
        File location = new File(harvestLocation);
        viewer = new ContentViewer(location);
    }

    /**
	 * PRECONDITIONS:
	 * 1.	A website bean with a  not null harvestRef
	 * 2.   ............................... parent
	 * POSTCONDITIONS:
	 * 1.	An Array List is returned (could be empty).
	 *
	 * DESCRIPTION
	 * 1.	A UI request will invoke this method.
	 * 2.	Query and populate each bean with information about the file info.
	 * 3.	Return the array list
	 * 
	 * @param parentWB <CODE>WebsiteBean</CODE> with a not null 
	 * @return list of children
	 * @throws DAException
	 */
    @Deprecated
    public ArrayList<String> getChildren(WebsiteBean parentWB) throws DAException {
        String harvestGUID = parentWB.getHarvestGUID();
        String parent = parentWB.getWebsite();
        ArrayList<String> ret = new ArrayList<String>();
        String[] whatColumns = new String[] { FileInfoConst.GUID };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGUID + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.PARENT + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(parent) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.GUID;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        try {
            while (rs.next()) {
                ret.add(nullStr(rs.getString(FileInfoConst.GUID)));
            }
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getChildren", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * This will give all the Children of the parentWB. 
	 * sorted by their url.
	 * @param parentWB
	 * @return list of children
	 * @throws DAException
	 */
    public ArrayList<WebsiteBean> getChildrenBeans(WebsiteBean parentWB) throws DAException {
        String harvestGUID = parentWB.getHarvestGUID();
        String parent = parentWB.getWebsite();
        ArrayList<WebsiteBean> ret = new ArrayList<WebsiteBean>();
        String[] whatColumns = new String[] { FileInfoConst.GUID, FileInfoConst.HARVEST_GUID, FileInfoConst.FAST_LINK, FileInfoConst.PARENT, FileInfoConst.URL, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGUID + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.PARENT + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(parent) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        try {
            while (rs.next()) {
                WebsiteBean tmp = new WebsiteBean();
                tmp.setHarvestGUID(nullStr(rs.getString(FileInfoConst.HARVEST_GUID)));
                tmp.setGuid(nullStr(rs.getString(FileInfoConst.GUID)));
                tmp.setLocalLink(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                tmp.setParent(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                tmp.setWebsite(nullStr(rs.getString(FileInfoConst.URL)));
                tmp.setExclude(nullStr(rs.getString(FileInfoConst.EXCLUDE)).equals("true"));
                ret.add(tmp);
            }
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getChildrenBean", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * Gets the root of the harvest tree for the UI. 
	 * 
	 * @param websiteGUID the ref of the harvest
	 * @return String
	 */
    @Deprecated
    public WebsiteBean getWebsiteBean(String websiteGUID) {
        WebsiteBean ret = new WebsiteBean();
        String[] whatColumns = new String[] { FileInfoConst.GUID, FileInfoConst.HARVEST_GUID, FileInfoConst.FAST_LINK, FileInfoConst.PARENT, FileInfoConst.URL, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY };
        String condition = FileInfoConst.GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + websiteGUID + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = null;
        try {
            rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
            if (rs.next()) {
                ret.setHarvestGUID(nullStr(rs.getString(FileInfoConst.HARVEST_GUID)));
                ret.setGuid(nullStr(rs.getString(FileInfoConst.GUID)));
                ret.setLocalLink(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                ret.setParent(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                ret.setWebsite(nullStr(rs.getString(FileInfoConst.URL)));
                ret.setExclude(nullStr(rs.getString(FileInfoConst.EXCLUDE)).equals("true"));
                ret.setChildren(getChildren(ret));
            }
        } catch (DAException ex) {
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getWebsiteBean", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * Gets the root of the harvest tree for the UI. 
	 * 
	 * @param harvestGuid the ref of the harvest
	 * @param harvestPath the harvest location
	 * @param session  cached session
	 * @return String
	 */
    @Deprecated
    public WebsiteBean getRedirectRoot(String harvestGuid, String harvestPath, HttpSession session) {
        WebsiteBean ret = new WebsiteBean();
        String[] whatColumns = new String[] { FileInfoConst.GUID, FileInfoConst.HARVEST_GUID, FileInfoConst.FAST_LINK, FileInfoConst.PARENT, FileInfoConst.URL, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGuid + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.URL + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + "http://" + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = null;
        try {
            rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
            while (rs.next()) {
                ret.setHarvestGUID(nullStr(rs.getString(FileInfoConst.HARVEST_GUID)));
                ret.setGuid(nullStr(rs.getString(FileInfoConst.GUID)));
                ret.setLocalLink(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                ret.setParent(nullStr(rs.getString(FileInfoConst.PARENT)));
                ret.setWebsite(nullStr(rs.getString(FileInfoConst.URL)));
                ret.setExclude(nullStr(rs.getString(FileInfoConst.EXCLUDE)).equals("true"));
                ret.setChildren(getChildren(ret));
            }
        } catch (DAException ex) {
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getWebsiteBean", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * This will return just the root element of the tree view. No children will be populated
	 * @param harvestGuid
	 * @param session
	 * @return redirected root.
	 */
    public WebsiteBean getRedirectRoot(String harvestGuid, HttpSession session) {
        WebsiteBean ret = new WebsiteBean();
        String[] whatColumns = new String[] { FileInfoConst.GUID, FileInfoConst.HARVEST_GUID, FileInfoConst.FAST_LINK, FileInfoConst.PARENT, FileInfoConst.URL, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGuid + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.URL + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + "http://" + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = null;
        try {
            rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
            while (rs.next()) {
                ret.setHarvestGUID(nullStr(rs.getString(FileInfoConst.HARVEST_GUID)));
                ret.setGuid(nullStr(rs.getString(FileInfoConst.GUID)));
                ret.setLocalLink(nullStr(rs.getString(FileInfoConst.FAST_LINK)));
                ret.setParent(nullStr(rs.getString(FileInfoConst.PARENT)));
                ret.setWebsite(nullStr(rs.getString(FileInfoConst.URL)));
                ret.setExclude(nullStr(rs.getString(FileInfoConst.EXCLUDE)).equals("true"));
            }
        } catch (DAException ex) {
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getWebsiteBean", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * Gets the harvest guid from the harvest location
	 * @param fileLocation
	 * @return harvest guid from location
	 */
    public String getHarvestGuidFromLocation(String fileLocation) {
        String ret = "";
        String[] whatColumns = new String[] { FileInfoConst.GUID, FileInfoConst.HARVEST_GUID };
        String condition = FileInfoConst.FILE_LOCATION + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(fileLocation) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.HARVEST_GUID;
        ResultSet rs = null;
        try {
            rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
            if (rs.next()) {
                ret = nullStr(rs.getString(FileInfoConst.HARVEST_GUID));
            }
        } catch (DAException ex) {
        } catch (SQLException ex) {
            logger.log(DAExceptionCodes.ERROR_READING, this, "getWebsiteBean", "readig the result list", ex);
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * This method takes a guid and an exclude flag. 
	 * It updates the excluded file_info and its coresponding children in the database.
	 *  
	 * @param guid - guid to find the file info.
	 * @param exclude - the exclude flag.
	 */
    public void updateExcludeInclude(String guid, boolean exclude) {
        String condition = FileInfoConst.GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + guid + SimpleRDBMSConst.CLOSE_QUOTE;
        try {
            srdbms.update(FileInfoConst.TABLE_NAME, new String[] { FileInfoConst.EXCLUDE }, new String[] { exclude ? "true" : "false" }, condition);
            WebsiteBean wb = getWebsiteBean(guid);
            ArrayList<String> children = getChildren(wb);
            if (!children.isEmpty()) {
                for (int ii = 0; ii < children.size(); ii++) {
                    updateExcludeInclude(children.get(ii), exclude);
                }
            }
        } catch (DAException ex) {
        }
    }

    /**
	 * !!! Always use after you start a transaction !!!
	 * 
	 * This method populates the file info table from a given file info. 
	 * 
	 * @param fileInfo - fileInfo to insert
	 * @param harvestGUID - related harvest 
	 * @param sequence - the sequence
	 * @throws DAException - thrown if populate fails
	 */
    public void populateFileInfoTable(FileInfo fileInfo, String harvestGUID, int sequence) throws DAException {
        if (harvestLocation == null) {
            return;
        }
        String addURL = fileInfo.getOriginalURL().toString();
        String link = viewer.createLink(fileInfo);
        String contentType = fileInfo.getContentType();
        String fileLocation = (fileInfo.getFileLocation()).toString();
        String newGuid = (String) new GUIDGenerator().generate();
        String parent = calculateParent(addURL);
        if (!exists(parent, harvestGUID)) {
            createDummyParent(parent, harvestGUID);
        }
        String[] fields = new String[] { FileInfoConst.GUID, FileInfoConst.FAST_LINK, FileInfoConst.FILE_LOCATION, FileInfoConst.CONTENT_TYPE, FileInfoConst.URL, FileInfoConst.HARVEST_GUID, FileInfoConst.PARENT, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY, FileInfoConst.SEQUENCE };
        String[] values = new String[] { newGuid, link, fileLocation, contentType, addURL, harvestGUID, parent, "false", "false", (new Integer(sequence)).toString() };
        srdbms.add(FileInfoConst.TABLE_NAME, fields, values);
    }

    /**
	 * !!! Always use after you start a transaction !!!
	 *
	 * This method deletes all harvestGUID's related file infos
	 * @param harvestGUID - corresponding harvestGUID
	 * @throws DAException 
	 */
    public void deleteFileInfos(String harvestGUID) throws DAException {
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGUID + SimpleRDBMSConst.CLOSE_QUOTE;
        srdbms.delete(FileInfoConst.TABLE_NAME, condition);
    }

    /**
	 * Creates a file info based on a file location and a harvestGUID. 
	 * Returns null if nothing is found.
	 * 
	 * @param fileLocation - specifies the file location search parameter
	 * @return a FileInfo object.
	 * @throws DAException 
	 * 		1. Thrown if the query fails.
	 * 		2. If the data from the result set is not translated properly
	 * 			throws the translated SQLException.
	 * 		3. If the url was not defined properly in the database 
	 * 			translates the thrown MalformedURLException to a DAException 
	 * 			and	throws it
	 */
    public FileInfo getFileInfo(String fileLocation) throws DAException {
        String[] whatColumns = new String[] { FileInfoConst.URL, FileInfoConst.CONTENT_TYPE, FileInfoConst.EXCLUDE };
        String condition = FileInfoConst.FILE_LOCATION + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(fileLocation) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        FileInfo ret = null;
        try {
            if (rs.next()) {
                URL newURL = new URL(nullStr(rs.getString(FileInfoConst.URL)));
                ret = new FileInfo(newURL);
                ret.setContentType(nullStr(rs.getString(FileInfoConst.CONTENT_TYPE)));
                ret.setFileLocation(new File(fileLocation));
                String excl = rs.getString(FileInfoConst.EXCLUDE);
                excl = (excl == null) ? "" : excl.trim();
                ret.setExcluded(Boolean.parseBoolean(excl));
            }
        } catch (SQLException e) {
            throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
        } catch (MalformedURLException e) {
            throw new DAException(DAExceptionCodes.ERROR_CREATING, new String[] { e.getMessage() });
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * This method returns an array list of all file infos that are included 
	 * 		and are related to the harvest(harvestGuid.) 
	 * 
	 * @param harvestGuid - the harvest guid
	 * @return - the included file infos in the harvest guid
	 * @throws DAException 
	 */
    public ArrayList<FileInfo> getIncludedFileInfos(String harvestGuid) throws DAException {
        ArrayList<FileInfo> ret = new ArrayList<FileInfo>();
        String[] whatColumns = new String[] { FileInfoConst.URL, FileInfoConst.CONTENT_TYPE, FileInfoConst.FILE_LOCATION, FileInfoConst.SEQUENCE };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGuid + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.EXCLUDE + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + "false" + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.DUMMY + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + "false" + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.SEQUENCE;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        FileInfo tmp = null;
        try {
            while (rs.next()) {
                URL newURL = new URL(nullStr(rs.getString(FileInfoConst.URL)));
                tmp = new FileInfo(newURL);
                tmp.setContentType(nullStr(rs.getString(FileInfoConst.CONTENT_TYPE)));
                String location = rs.getString(FileInfoConst.FILE_LOCATION);
                if (location != null) {
                    tmp.setFileLocation(new File(location));
                }
                ret.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DAException(DAExceptionCodes.ERROR_CREATING, new String[] { e.getMessage() });
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * Get the count
	 * @param harvestGuid
	 * @return int of the count
	 * @throws DAException
	 */
    public int getFileInfoCount(String harvestGuid) throws DAException {
        int ret = 0;
        ResultSet rs = null;
        try {
            rs = srdbms.find("SELECT COUNT(*) FROM " + FileInfoConst.TABLE_NAME + " WHERE " + FileInfoConst.HARVEST_GUID + "='" + harvestGuid + "' AND NOT " + FileInfoConst.SEQUENCE + " IS NULL");
            if (rs.next()) {
                ret = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
        } finally {
            srdbms.closeObject(rs);
        }
        return ret;
    }

    /**
	 * The local link for a clickable url.
	 * @param harvestGuid
	 * @param url
	 * @return local link for the URL
	 * @throws DAException
	 */
    public String getLocalLinkFromURL(String harvestGuid, String url) throws DAException {
        String ret = "";
        String[] whatColumns = new String[] { FileInfoConst.FAST_LINK };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGuid + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.URL + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(url) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        try {
            if (rs.next()) {
                ret = rs.getString(FileInfoConst.FAST_LINK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
        } finally {
            srdbms.closeObject(rs);
        }
        if (("").equals(ret)) {
            condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestGuid + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.URL + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(url) + "/" + SimpleRDBMSConst.CLOSE_QUOTE;
            ResultSet rs2 = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
            try {
                if (rs2.next()) {
                    ret = rs2.getString(FileInfoConst.FAST_LINK);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
            } finally {
                srdbms.closeObject(rs2);
            }
        }
        return ret;
    }

    /**
	 * Creates dummy parents recursively.
	 * 
	 * @param url - url to create
	 * @param harvestGUID - related harvest
	 * @throws DAException - failed to create the dummy
	 */
    private void createDummyParent(String url, String harvestGUID) throws DAException {
        if (url.equals("")) {
            return;
        }
        String newGuid = (String) new GUIDGenerator().generate();
        String parent = calculateParent(url);
        if (!exists(parent, harvestGUID)) {
            createDummyParent(parent, harvestGUID);
        }
        String[] fields = new String[] { FileInfoConst.GUID, FileInfoConst.URL, FileInfoConst.HARVEST_GUID, FileInfoConst.PARENT, FileInfoConst.EXCLUDE, FileInfoConst.DUMMY };
        String[] values = new String[] { newGuid, url, harvestGUID, parent, "false", "true" };
        srdbms.add(FileInfoConst.TABLE_NAME, fields, values);
    }

    /**
	 * Checks if the URL is present in the table
	 * 
	 * @param url - url to check
	 * @param harvestRef - harvestRef it belongs to
	 * @return - true if the url exists false if it does not.
	 * @throws DAException - thrown if a sql error occurs.
	 */
    private boolean exists(String url, String harvestRef) throws DAException {
        String[] whatColumns = new String[] { FileInfoConst.GUID };
        String condition = FileInfoConst.HARVEST_GUID + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + harvestRef + SimpleRDBMSConst.CLOSE_QUOTE + SimpleRDBMSConst.AND + FileInfoConst.URL + SimpleRDBMSConst.EQ + SimpleRDBMSConst.OPEN_QUOTE + srdbms.encodeSQLValue(url) + SimpleRDBMSConst.CLOSE_QUOTE;
        String sort = FileInfoConst.URL;
        ResultSet rs = srdbms.find(whatColumns, FileInfoConst.TABLE_NAME, condition, sort);
        try {
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DAException(DAExceptionCodes.SQL_ERROR, new String[] { e.getMessage() });
        } finally {
            srdbms.closeObject(rs);
        }
    }

    /**
	 * All parents are going to start with an http:// even thought 
	 * the url doesn't change.
	 * 
	 * e.g. url https://www.123.gov/example
	 * parent http://www.123.gov/
	 * 
	 * @param url starting url.
	 * @return urls parent
	 */
    private String calculateParent(String url) {
        if (url.equals("http://") || url.equals("https://") || url.equals("ftp://")) {
            return "";
        }
        String ret = "";
        if (url.endsWith("/")) {
            ret = url.substring(0, url.length() - 1);
        } else {
            ret = url;
        }
        if (ret.startsWith("https")) {
            ret = "http" + ret.substring(5);
        }
        if (ret.startsWith("ftp")) {
            ret = "http" + ret.substring(3);
        }
        if (ret.lastIndexOf("/") > 0) {
            if (ret.lastIndexOf("/") == 6) {
                return "http://";
            }
            ret = ret.substring(0, ret.lastIndexOf("/"));
        } else {
            ret = "";
        }
        return ret;
    }

    /**
	 * makes sure the string is never a null pointer. Changes null string
	 * pointers to 0-length strings
	 * @param string to check
	 * @return original string or a zero-length string
	 */
    private static String nullStr(Object string) {
        if (string == null) return (String) "";
        String newString = (String) string;
        return (newString.trim());
    }
}
