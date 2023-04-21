package org.dctmutils.common;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfRelation;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.wcm.IWcmAppContext;
import com.documentum.wcm.WcmAppContext;
import com.documentum.wcm.WcmFolderMapUtil;
import com.documentum.wcm.services.IWcmChangeSetService;
import com.documentum.wcm.services.IWcmContentCreationService;
import com.documentum.wcm.services.IWcmFolderCreationService;
import com.documentum.wcm.services.IWcmStartWorkflow;
import com.documentum.wcm.services.IWcmStartWorkflowConfig;
import com.documentum.wcm.type.IWcmChangeSet;
import com.documentum.wcm.type.IWcmChannelFolder;
import com.documentum.wcm.type.IWcmContentTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dctmutils.common.constant.DctmConstants;
import org.dctmutils.common.exception.DctmUtilsException;
import org.dctmutils.common.exception.MissingParameterException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Helper class for working with Web Publisher (Web Content Management).
 *
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 * @author <a href="mailto:dbingham@users.sourceforge.net">Daniel Bingham</a>
 */
public class WcmHelper {

    private static Log log = LogFactory.getLog(WcmHelper.class);

    private static IWcmAppContext wcmAppContext = null;

    /**
     * Create an <code>IWcmChangeSet</code>.
     *
     * @param sessionManager
     * @param docbase
     * @param changeSetName
     * @return a <code>IWcmChangeSet</code>
     * @throws DfException
     */
    public static IWcmChangeSet createChangeSet(IDfSessionManager sessionManager, String docbase, String changeSetName) throws DfException {
        IWcmChangeSet changeSet = null;
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmChangeSetService changeSetService = (IWcmChangeSetService) getWcmAppContext(sessionManager, docbase).getService((com.documentum.wcm.services.IWcmChangeSetService.class).getName());
            if (changeSetService != null) {
                IDfId changeSetId = changeSetService.createChangeSet(changeSetName, false);
                changeSet = (IWcmChangeSet) getWcmAppContext(sessionManager, docbase).getObject(changeSetId, (com.documentum.wcm.type.IWcmChangeSet.class).getName(), dfSession);
                changeSet.setTitle(changeSetName);
                changeSet.save();
            }
        } finally {
            sessionManager.release(dfSession);
        }
        return changeSet;
    }

    /**
     * Delete an <code>IWcmChangeSet</code>.
     *
     * @param sessionManager
     * @param docbase
     * @param changeSetId
     * @throws DfException
     */
    public static void deleteChangeSet(IDfSessionManager sessionManager, String docbase, IDfId changeSetId) throws DfException {
        log.debug("sessionManager = " + sessionManager + ", docbase = " + docbase + ", changeSetId " + changeSetId);
        if (sessionManager == null || StringUtils.isBlank(docbase) || changeSetId == null) {
            log.warn("deleteChangeSet() called with invalid params");
            throw new MissingParameterException("sessionManager, docbase and changeSetId");
        }
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmChangeSetService changeSetService = (IWcmChangeSetService) getWcmAppContext(sessionManager, docbase).getService((com.documentum.wcm.services.IWcmChangeSetService.class).getName());
            if (changeSetService != null) {
                IWcmChangeSet changeSet = getChangeSet(sessionManager, docbase, changeSetId);
                if (changeSet != null) {
                    try {
                        changeSet.restart();
                    } catch (Exception ignore) {
                    }
                    changeSet.removeProcessWorkflowId(changeSet.getProcessWorkflowId());
                    List changeSetFileIds = getChangeSetFileIds(dfSession, changeSet.getObjectId());
                    Iterator ii = changeSetFileIds.iterator();
                    IDfId docId = null;
                    while (ii.hasNext()) {
                        docId = (IDfId) ii.next();
                        changeSet.removeFile(docId);
                    }
                    IDfFolder supportingDocs = changeSet.getSupportingDocFolderObject();
                    List changeSetSupportingFileIds = getChangeSetFileIds(dfSession, supportingDocs.getObjectId());
                    ii = changeSetSupportingFileIds.iterator();
                    docId = null;
                    while (ii.hasNext()) {
                        docId = (IDfId) ii.next();
                        changeSet.removeSupportingFile(docId);
                    }
                    changeSetService.deleteChangeSet(changeSetId);
                }
            }
        } catch (DfException dfe) {
            log.warn(dfe.getMessage());
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
    }

    /**
     * Get a list of the <code>IDfId</code>s of the change set's files or supporting files based on which
     * <code>IDfId</code> is passed to this method.
     *
     * @param dfSession
     * @param folderId  - the change set id or the change set's supporting docs folder id.
     * @return a <code>List</code> of <code>IDfId</code> objects.
     * @throws DfException
     */
    public static List getChangeSetFileIds(IDfSession dfSession, IDfId folderId) throws DfException {
        if (dfSession == null || folderId == null) {
            throw new MissingParameterException("dfSession and folderId");
        }
        List fileIds = new ArrayList();
        IDfCollection coll = null;
        try {
            StringBuffer dql = new StringBuffer();
            dql.append("SELECT r_object_id ").append("FROM dm_document ").append("WHERE FOLDER (ID('").append(folderId.toString()).append("'))");
            dql.append(" AND a_is_hidden=0 AND a_archive=0 ORDER BY 1");
            coll = DqlHelper.readQuery(dfSession, dql.toString());
            while (coll.next()) {
                fileIds.add(coll.getId("r_object_id"));
            }
        } finally {
            DqlHelper.cleanup(coll);
        }
        return fileIds;
    }

    /**
     * Get an <code>IWcmChangeSet</code>.
     *
     * @param sessionManager
     * @param docbase
     * @param changeSetName
     * @return an <code>IWcmChangeSet</code> value
     * @throws DfException
     */
    public static IWcmChangeSet getChangeSet(IDfSessionManager sessionManager, String docbase, String changeSetName) throws DfException {
        IWcmChangeSet changeSet = null;
        IDfSession dfSession = sessionManager.getSession(docbase);
        StringBuffer dql = new StringBuffer();
        dql.append("SELECT r_object_id FROM wcm_change_set ").append("WHERE object_name ='").append(changeSetName).append("'");
        IDfCollection coll = null;
        IDfId changeSetId = null;
        try {
            coll = DqlHelper.readQuery(dfSession, dql.toString());
            if (coll.next()) {
                changeSetId = coll.getId("r_object_id");
            }
        } finally {
            DqlHelper.cleanup(coll);
        }
        changeSet = getChangeSet(sessionManager, docbase, changeSetId);
        return changeSet;
    }

    /**
     * Get an <code>IWcmChangeSet</code>.
     *
     * @param sessionManager
     * @param docbase
     * @param changeSetId
     * @return an <code>IWcmChangeSet</code> value
     * @throws DfException
     */
    public static IWcmChangeSet getChangeSet(IDfSessionManager sessionManager, String docbase, IDfId changeSetId) throws DfException {
        IWcmChangeSet changeSet = null;
        IDfSession dfSession = sessionManager.getSession(docbase);
        try {
            if (changeSetId != null) {
                changeSet = (IWcmChangeSet) getWcmAppContext(sessionManager, docbase).getObject(changeSetId, (com.documentum.wcm.type.IWcmChangeSet.class).getName(), dfSession);
            }
        } finally {
            sessionManager.release(dfSession);
        }
        return changeSet;
    }

    /**
     * Add a document to a change set.
     *
     * @param changeSet
     * @param document
     * @throws DfException
     */
    public static void addDocument(IWcmChangeSet changeSet, IDfDocument document) throws DfException {
        if (changeSet != null && document != null) {
            changeSet.addFile(document.getObjectId());
        }
    }

    /**
     * Add a supporting document to a change set.
     *
     * @param changeSet
     * @param document
     * @throws DfException
     */
    public static void addSupportingDocument(IWcmChangeSet changeSet, IDfDocument document) throws DfException {
        if (changeSet != null && document != null) {
            changeSet.addSupportingFile(document.getObjectId());
        }
    }

    /**
     * Start the specified WCM Workflow with a new ChangeSet with the specified name.
     *
     * @param sessionManager
     * @param docbase
     * @param workflowName
     * @param changeSetName
     * @return a <code>String</code> value
     * @throws DfException
     */
    public static String startWcmWorkflow(IDfSessionManager sessionManager, String docbase, String workflowName, String changeSetName) throws DfException {
        IDfSession dfSession = sessionManager.getSession(docbase);
        IWcmStartWorkflow swService = (IWcmStartWorkflow) getWcmAppContext(sessionManager, docbase).getService((com.documentum.wcm.services.IWcmStartWorkflow.class).getName());
        log.debug("swService = " + swService);
        IWcmChangeSet changeSet = getChangeSet(sessionManager, docbase, changeSetName);
        if (changeSet == null) {
            changeSet = createChangeSet(sessionManager, docbase, changeSetName);
        }
        String processId = WorkflowHelper.getProcessId(dfSession, workflowName);
        IWcmStartWorkflowConfig config = swService.getConfig(new DfId(processId), changeSet.getObjectId(), docbase);
        IDfId workflowId = swService.startWorkflow(config);
        return workflowId.toString();
    }

    /**
     * Get a <code>WcmAppContext</code> for the en_US locale.  This will always return the same wcmAppContext until such
     * time as getNewWcmAppContext is called.
     *
     * @param sessionManager
     * @param docbase
     * @return an <code>IWcmAppContext</code> value
     * @throws DfException
     */
    public static synchronized IWcmAppContext getWcmAppContext(IDfSessionManager sessionManager, String docbase) throws DfException {
        return getWcmAppContext(sessionManager, docbase, new Locale("en", "US"));
    }

    /**
     * Get a <code>WcmAppContext</code> for the specified locale.  This will always return the same wcmAppContext until
     * such time as getNewWcmAppContext is called.
     *
     * @param sessionManager
     * @param docbase
     * @param locale
     * @return an <code>IWcmAppContext</code> value
     * @throws DfException
     */
    public static synchronized IWcmAppContext getWcmAppContext(IDfSessionManager sessionManager, String docbase, Locale locale) throws DfException {
        if (wcmAppContext == null) {
            wcmAppContext = new WcmAppContext(sessionManager, docbase, locale, new Hashtable(), "/tmp", null);
            log.debug("wcmAppContext = " + wcmAppContext);
        }
        return wcmAppContext;
    }

    /**
     * Get a new <code>WcmAppContext</code> for the en_US locale.  This is necessary because some of the WCM Services
     * call <code>IDfSessionManager.clearIdentities()</code> on the <code>WcmAppContext</code> you were using.  And
     * there is no way to just set a new <code>IDfSessionManager</code> on an <code>IWcmAppContext</code> object.
     *
     * @param sessionManager
     * @param docbase
     * @return an <code>IWcmAppContext</code> value
     * @throws DfException
     */
    public static IWcmAppContext getNewWcmAppContext(IDfSessionManager sessionManager, String docbase) throws DfException {
        return getNewWcmAppContext(sessionManager, docbase, new Locale("en", "US"));
    }

    /**
     * Get a new <code>WcmAppContext</code> for the specified locale.  This is necessary because some of the WCM
     * Services call <code>IDfSessionManager.clearIdentities()</code> on the <code>WcmAppContext</code> you were using.
     * And there is no way to just set a new <code>IDfSessionManager</code> on an <code>IWcmAppContext</code> object.
     *
     * @param sessionManager
     * @param docbase
     * @param locale
     * @return an <code>IWcmAppContext</code> value
     * @throws DfException
     */
    public static IWcmAppContext getNewWcmAppContext(IDfSessionManager sessionManager, String docbase, Locale locale) throws DfException {
        wcmAppContext = new WcmAppContext(sessionManager, docbase, locale, new Hashtable(), "/tmp", null);
        log.debug("wcmAppContext = " + wcmAppContext);
        return wcmAppContext;
    }

    /**
     * Link the IDfSysObject to the specified wcm folder.
     *
     * @param document
     * @param folderPath - the path in which to file the <code>IDfSysObject</code>
     * @param unlinkOld  - remove the object from its current location
     * @throws DfException
     */
    public static void linkToFolder(IDfSysObject document, String folderPath, boolean unlinkOld) throws DfException {
        if (document == null || StringUtils.isBlank(folderPath)) {
            throw new MissingParameterException("document and folderPath");
        }
        log.debug("linkToFolder(): start");
        IDfSession dfSession = document.getSession();
        StringTokenizer pathTokens = new StringTokenizer(folderPath, "/");
        String parentId = null;
        StringBuffer pathName = new StringBuffer();
        while (pathTokens.hasMoreTokens()) {
            String objectName = (String) pathTokens.nextElement();
            log.debug("objectName = " + objectName);
            pathName.append("/");
            pathName.append(objectName);
            log.debug("pathName = " + pathName);
            String folderId = DmObjectHelper.getObjectIdByPath(dfSession, pathName.toString());
            if (folderId == null) {
                if (parentId == null) {
                    log.info("linkToFolder: Creating Cabinet " + objectName);
                    folderId = DmObjectHelper.createObject(dfSession, objectName, DctmConstants.TYPE_WCM_CHANNEL, parentId);
                } else {
                    log.info("linkToFolder: Creating Folder " + objectName);
                    folderId = DmObjectHelper.createObject(dfSession, objectName, DctmConstants.TYPE_WCM_CHANNEL_FLD, parentId);
                }
            }
            parentId = folderId;
        }
        DmObjectHelper.setObjectLink(document, parentId, unlinkOld);
    }

    /**
     * Create wcm_channel folder(s). This method is used to create folders within the web cabinets.
     *
     * @param sessionManager
     * @param docbase
     * @param locale
     * @param path
     * @throws DfException
     */
    public static void createWebCabinetFolders(IDfSessionManager sessionManager, String docbase, Locale locale, String path) throws DfException {
        IWcmAppContext appContext = getWcmAppContext(sessionManager, docbase, locale);
        WcmFolderMapUtil.createFoldersByPath(appContext, path);
    }

    /**
     * Create the specified path for a generic category if it does not already exist (will create the taxonomy if it
     * doesn't yet exist).
     *
     * Used to create additional generic taxonomies and categories in WP.
     *
     * @param dfSession    - required
     * @param categoryPath - required
     * @param webCabinet   - must be provided if the taxonomy doesn't yet exist, not used if the taxonomy already
     *                     exists
     * @param aclName      - defaults to "WebPublisher User Default ACL" if not provided
     * @throws DfException
     */
    public static void createCategoryPath(IDfSession dfSession, String categoryPath, String webCabinet, String aclName) throws DfException {
        if (dfSession == null || StringUtils.isBlank(categoryPath)) {
            throw new MissingParameterException("dfSession and categoryPath");
        }
        log.debug("categoryPath(): start");
        StringTokenizer pathTokens = new StringTokenizer(categoryPath, "/");
        String parentId = null;
        StringBuffer categoryPathName = new StringBuffer();
        String objectName = null;
        if (StringUtils.isBlank(aclName)) {
            aclName = "WebPublisher User Default ACL";
        }
        while (pathTokens.hasMoreTokens()) {
            objectName = (String) pathTokens.nextElement();
            categoryPathName.append("/");
            categoryPathName.append(objectName);
            if (StringUtils.isBlank(parentId)) {
                parentId = createDmTaxonomyObject(dfSession, objectName, webCabinet, aclName);
            } else {
                parentId = createDmCategoryObject(dfSession, objectName, parentId, aclName);
            }
        }
        log.debug("categoryPath(): end");
    }

    /**
     * Create a taxonomy object (dm_taxonomy) in the docbase. create the wcm_taxonomy_link relation to associate the web
     * cabinet and taxonomy if the taxonomy already exists, it simply return the taxonmy ID
     *
     * @param dfSession    - required
     * @param taxonomyName - required
     * @param webCabinet   - must be provided if the taxonomy doesn't exist, won't be used if the taxonomy already
     *                     exists
     * @param aclName      - defaults to "WebPublisher User Default ACL" if not specified
     * @throws DfException
     */
    public static String createDmTaxonomyObject(IDfSession dfSession, String taxonomyName, String webCabinet, String aclName) throws DfException {
        log.debug("createTaxonomy(): start");
        if (dfSession == null || StringUtils.isBlank(taxonomyName)) {
            throw new MissingParameterException("dfSession and taxonomyName");
        }
        if (StringUtils.isBlank(aclName)) {
            aclName = "WebPublisher User Default ACL";
        }
        String taxonomyId = getTaxonomyIdByName(dfSession, taxonomyName);
        if (DmObjectHelper.isIDfId(taxonomyId)) {
            log.debug("createTaxonomy(): end");
            return taxonomyId;
        }
        if (StringUtils.isBlank(webCabinet)) {
            throw new MissingParameterException("webCabinet");
        }
        String folderId = DmObjectHelper.getObjectIdByPath(dfSession, "/Categories");
        IDfSysObject taxonomyObj = DmObjectHelper.createObject(dfSession, taxonomyName, "dm_taxonomy", folderId, false);
        if (taxonomyObj != null) {
            taxonomyId = taxonomyObj.getObjectId().getId();
            IDfSysObject classObj = (IDfSysObject) dfSession.getObjectByQualification("dm_category_class where object_name='Generic'");
            if (classObj == null) {
                throw new DctmUtilsException("Generic category class is not in the docbase. please repair and try again.");
            }
            taxonomyObj.setString("taxonomy_version", "1.0");
            taxonomyObj.setInt("active_doc_count", 0);
            taxonomyObj.setInt("candidate_threshold", 25);
            taxonomyObj.setId("class_id", classObj.getObjectId());
            taxonomyObj.setString("definition_type", "none");
            taxonomyObj.setInt("on_target_threshold", 75);
            taxonomyObj.setInt("active_doc_count", 0);
            taxonomyObj.setInt("test_doc_count", 0);
            taxonomyObj.setInt("child_count", 0);
            log.info("new taxonomy '" + taxonomyName + "' created. Id = " + taxonomyId);
            DmObjectHelper.setACL(dfSession, taxonomyObj, aclName);
            taxonomyObj.save();
            IDfRelation relationObj = (IDfRelation) dfSession.getObjectByQualification("dm_relation where relation_name='wcm_taxonomy_link' and child_id='" + taxonomyId + "'");
            if (relationObj == null) {
                log.debug("Taxonomy - Web Cabinet relationship did not exist; creating it. . .");
                String cabinetId = DmObjectHelper.getObjectIdByPath(dfSession, "/" + webCabinet);
                DmObjectHelper.createRelation(dfSession, "wcm_taxonomy_link", taxonomyId, cabinetId, "Relationship between web cabinet and taxonomy structure.");
            }
        } else {
            log.error("Could not fetch taxonomy object with id: " + taxonomyId);
            throw new DctmUtilsException("Could not fetch taxonomy object with id: " + taxonomyId);
        }
        log.debug("createTaxonomy(): end");
        return taxonomyId;
    }

    /**
     * Get the specified taxonomy id by name.
     *
     * @param dfSession
     * @param taxonomyName
     * @throws DfException
     */
    public static String getTaxonomyIdByName(IDfSession dfSession, String taxonomyName) throws DfException {
        log.debug("getTaxonomyByObjectName(): start");
        if (dfSession == null || StringUtils.isBlank(taxonomyName)) {
            throw new MissingParameterException("dfSession and taxonomyName");
        }
        String taxonomyId = null;
        IDfSysObject taxonomyObj = (IDfSysObject) dfSession.getObjectByQualification("dm_taxonomy where object_name ='" + taxonomyName + "'");
        if (taxonomyObj != null) {
            taxonomyId = taxonomyObj.getObjectId().getId();
            log.debug("getTaxonomyIdByObjectName(): Object Found. Id = " + taxonomyId);
        }
        log.debug("getTaxonomyByObjectName(): end");
        return taxonomyId;
    }

    /**
     * Create a category object (dm_category) in the docbase.
     *
     * If the category already exists, it simply return the category ID
     *
     * @param dfSession    - required
     * @param categoryName - required
     * @param parentId     - must be provided if the category doesn't exist. Won't be used if the category already
     *                     exists
     * @param aclName      - defaults to "WebPublisher User Default ACL" if not specified
     * @throws DfException
     */
    protected static String createDmCategoryObject(IDfSession dfSession, String categoryName, String parentId, String aclName) throws DfException {
        log.debug("createDmCategoryObject(): start");
        if (dfSession == null || StringUtils.isBlank(categoryName)) {
            throw new MissingParameterException("dfSession and categoryName");
        }
        if (StringUtils.isBlank(aclName)) {
            aclName = "WebPublisher User Default ACL";
        }
        String catId = getCategoryIdByName(dfSession, categoryName);
        if (DmObjectHelper.isIDfId(catId)) {
            log.debug("createDmCategoryObject(): end");
            return catId;
        }
        if (StringUtils.isBlank(parentId)) {
            throw new MissingParameterException("parentId");
        }
        IDfSysObject catObj = DmObjectHelper.createObject(dfSession, categoryName, "dm_category", parentId, false);
        if (catObj != null) {
            catId = catObj.getObjectId().getId();
            IDfSysObject classObj = (IDfSysObject) dfSession.getObjectByQualification("dm_category_class where object_name='Generic'");
            if (classObj == null) {
                throw new DfException("Generic category class is not in the docbase. please repair and try again.");
            }
            catObj.setInt("active_doc_count", 0);
            catObj.setInt("candidate_threshold", 25);
            catObj.setId("class_id", classObj.getObjectId());
            catObj.setString("definition_type", "none");
            catObj.setInt("on_target_threshold", 75);
            catObj.setInt("active_doc_count", 0);
            catObj.setInt("test_doc_count", 0);
            catObj.setInt("child_count", 0);
            log.info("new category created. Id = " + catId);
            DmObjectHelper.setACL(dfSession, catObj, aclName);
            catObj.save();
            ensureChildCountOnParent(dfSession, parentId);
            IDfRelation relationObj = (IDfRelation) dfSession.getObjectByQualification("dm_relation where relation_name='dm_category_assign' and child_id='" + catId + "'");
            if (relationObj == null) {
                log.debug("Taxonomy - Category (dm_category_assign) relationship did not exist; creating it. . .");
                DmObjectHelper.createRelation(dfSession, "dm_category_assign", catId, parentId, "Relationship between child category and its parent.");
            }
        } else {
            log.error("Could not fetch category object with id: " + catId);
            throw new DctmUtilsException("Could not fetch category object with id: " + catId);
        }
        log.debug("createDmCategoryObject(): end");
        return catId;
    }

    /**
     * get the specified category by name.
     *
     * @param dfSession
     * @param categoryName
     * @throws DfException
     */
    protected static String getCategoryIdByName(IDfSession dfSession, String categoryName) throws DfException {
        log.debug("getTaxonomyByObjectName(): start");
        if (dfSession == null || StringUtils.isBlank(categoryName)) {
            throw new MissingParameterException("dfSession and categoryName");
        }
        String catId = null;
        IDfSysObject taxonomyObj = (IDfSysObject) dfSession.getObjectByQualification("dm_category where object_name ='" + categoryName + "'");
        if (taxonomyObj != null) {
            catId = taxonomyObj.getObjectId().getId();
            log.debug("getCategoryIdByObjectName(): Object Found. Id = " + catId);
        }
        return catId;
    }

    /**
     * Utility method to set the child count on dm_taxonomy and dm_category objects
     *
     * @param dfSession
     * @param parentId
     * @throws DfException
     */
    private static void ensureChildCountOnParent(IDfSession dfSession, String parentId) throws DfException {
        IDfCollection collection = null;
        int childCount = 0;
        String dql = "select r_object_id, child_count from dm_category where any i_folder_id='" + parentId + "'";
        try {
            collection = DqlHelper.execQuery(dfSession, dql);
            while (collection.next()) {
                childCount++;
            }
        } finally {
            DqlHelper.cleanup(collection);
        }
        IDfSysObject parentObj = (IDfSysObject) dfSession.getObjectByQualification("dm_category where r_object_id='" + parentId + "'");
        if (parentObj.getInt("child_count") != childCount) {
            parentObj.setInt("child_count", childCount);
            parentObj.save();
        }
    }

    /**
     * Create wcm_category folder(s). This method is used to create folders for templates and functional taxonomy.
     *
     * @param sessionManager
     * @param docbase
     * @param locale
     * @param path
     * @param aclName
     * @return the r_object_id of the lowest leaf wcm_category that was created in the folder tree.
     * @throws DfException
     */
    public static IDfId createWcmCategoryFolders(IDfSessionManager sessionManager, String docbase, Locale locale, String path, String aclName) throws DfException {
        IDfId categoryId = null;
        IWcmAppContext appContext = getWcmAppContext(sessionManager, docbase, locale);
        IDfSession dfSession = sessionManager.getSession(docbase);
        try {
            if (StringUtils.isBlank(aclName)) {
                aclName = "WebPublisher User Default ACL";
            }
            IWcmFolderCreationService folderCreationService = (IWcmFolderCreationService) appContext.getService(com.documentum.wcm.services.IWcmFolderCreationService.class.getName());
            StringTokenizer pathElements = new StringTokenizer(path, "/");
            StringBuffer parentPath = new StringBuffer();
            String pathElement = null;
            IDfSysObject pathElementObject = null;
            IDfSysObject parentElementObject = null;
            while (pathElements.hasMoreTokens()) {
                pathElement = pathElements.nextToken();
                parentPath.append("/").append(pathElement);
                pathElementObject = DmObjectHelper.getSysObjectByPath(dfSession, parentPath.toString());
                if (pathElementObject == null) {
                    categoryId = folderCreationService.createCategoryFolder("wcm_category", pathElement, parentElementObject.getObjectId(), aclName);
                    pathElementObject = (IDfSysObject) dfSession.getObject(categoryId);
                }
                parentElementObject = pathElementObject;
            }
            log.debug("categoryId = " + categoryId);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            SessionHelper.cleanup(sessionManager, dfSession);
            throw new DfException(t);
        }
        return categoryId;
    }

    /**
     * Get a wcm_channel folder using its path.
     *
     * @param sessionManager
     * @param docbase
     * @param locale
     * @param path
     * @return an <code>IWcmChannelFolder</code> object
     * @throws DfException
     */
    public static IWcmChannelFolder getChannelFolderByPath(IDfSessionManager sessionManager, String docbase, Locale locale, String path) throws DfException {
        IWcmAppContext appContext = getWcmAppContext(sessionManager, docbase, locale);
        IWcmChannelFolder folder = WcmFolderMapUtil.getChannelFolderByPath(appContext, path, sessionManager.getSession(docbase));
        return folder;
    }

    /**
     * Get an instance of the <code>IWcmContentCreationService</code>
     *
     * @param sessionManager
     * @param docbase
     * @return an <code>IWcmContentCreationService</code> object
     * @throws DfException
     */
    public static IWcmContentCreationService getIWcmContentCreationService(IDfSessionManager sessionManager, String docbase) throws DfException {
        IWcmContentCreationService service = (IWcmContentCreationService) getWcmAppContext(sessionManager, docbase).getService((com.documentum.wcm.services.IWcmContentCreationService.class).getName());
        return service;
    }

    /**
     * Create a WCM template. This method can be used to create content templates, rules files, presentations, etc.
     *
     * @param sessionManager
     * @param docbase
     * @param lifecycleName
     * @param templateFolderPath
     * @param localTemplateFilePath
     * @param templateName
     * @param templateObjectType
     * @return the <code>IDfId</code> of the new template
     * @throws DfException
     */
    public static IDfId createTemplate(IDfSessionManager sessionManager, String docbase, String lifecycleName, String templateFolderPath, String localTemplateFilePath, String templateName, String templateObjectType) throws DfException {
        IDfId templateId = null;
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmContentCreationService service = WcmHelper.getIWcmContentCreationService(sessionManager, docbase);
            IDfId lifecycleId = dfSession.getIdByQualification("dm_policy where object_name = '" + lifecycleName + "'");
            if (lifecycleId == null) {
                throw new DfException("Unable to find lifecycle: " + lifecycleName);
            }
            WcmHelper.createWebCabinetFolders(sessionManager, docbase, null, templateFolderPath);
            IDfSysObject templateFolder = DmObjectHelper.getSysObjectByPath(dfSession, templateFolderPath);
            if (templateFolder == null) {
                throw new DfException("Unable to create tempalteFolderPath: " + templateFolderPath);
            }
            IDfId templateFolderId = templateFolder.getObjectId();
            service.validateImportTemplate(templateName, templateFolderId);
            templateId = service.importTemplate(templateObjectType, lifecycleId, templateFolderId, templateName, localTemplateFilePath, FileHelper.getFileExtension(localTemplateFilePath));
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
        return templateId;
    }

    /**
     * Associate the rules file with the content template
     *
     * @param sessionManager
     * @param docbase
     * @param contentTemplateId
     * @param rulesId
     * @param locale
     * @throws DfException
     */
    public static void createRulesRelation(IDfSessionManager sessionManager, String docbase, IDfId contentTemplateId, IDfId rulesId, String locale) throws DfException {
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmContentTemplate template = (IWcmContentTemplate) WcmHelper.getWcmAppContext(sessionManager, docbase).getObject(contentTemplateId, com.documentum.wcm.type.IWcmContentTemplate.class.getName(), dfSession);
            if (StringUtils.isBlank(locale)) {
                locale = "";
            }
            template.setRulesTemplateId(rulesId, locale);
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
    }

    /**
     * Associate the presentation with the content template
     *
     * @param sessionManager
     * @param docbase
     * @param contentTemplateId
     * @param presentationId
     * @param locale
     * @param description
     * @throws DfException
     */
    public static void createPresentationRelation(IDfSessionManager sessionManager, String docbase, IDfId contentTemplateId, IDfId presentationId, String locale, String description) throws DfException {
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmContentTemplate template = (IWcmContentTemplate) WcmHelper.getWcmAppContext(sessionManager, docbase).getObject(contentTemplateId, com.documentum.wcm.type.IWcmContentTemplate.class.getName(), dfSession);
            if (StringUtils.isBlank(locale)) {
                locale = "";
            }
            template.setLayoutTemplateId(presentationId, "html", locale, description);
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
    }

    /**
     * Make the specified template available
     *
     * @param sessionManager
     * @param docbase
     * @param contentTemplateId
     * @throws DfException
     */
    public static void makeTemplateAvailable(IDfSessionManager sessionManager, String docbase, IDfId contentTemplateId) throws DfException {
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmContentTemplate template = (IWcmContentTemplate) WcmHelper.getWcmAppContext(sessionManager, docbase).getObject(contentTemplateId, com.documentum.wcm.type.IWcmContentTemplate.class.getName(), dfSession);
            template.makeAvailable();
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
    }

    /**
     * Delete the specified template
     *
     * @param sessionManager
     * @param docbase
     * @param contentTemplateId
     * @throws DfException
     */
    public static void purgeTemplate(IDfSessionManager sessionManager, String docbase, IDfId contentTemplateId) throws DfException {
        IDfSession dfSession = null;
        try {
            dfSession = sessionManager.getSession(docbase);
            IWcmContentTemplate template = (IWcmContentTemplate) WcmHelper.getWcmAppContext(sessionManager, docbase).getObject(contentTemplateId, com.documentum.wcm.type.IWcmContentTemplate.class.getName(), dfSession);
            List layoutInfo = template.getAllLayoutTemplatesInfo();
            List publishingInfo = template.getAllPublishingTemplatesInfo();
            List rulesInfo = template.getAllRulesTemplatesInfo();
            log.debug("layoutInfo: " + layoutInfo.toString());
            log.debug("publishingInfo: " + publishingInfo.toString());
            log.debug("rulesInfo: " + rulesInfo.toString());
            template.removeAllLayoutTemplateIds();
            template.removeAllRulesTemplateIds();
            template.removeAllPublishingTemplateIds();
            template.deleteAllVersions();
        } finally {
            SessionHelper.cleanup(sessionManager, dfSession);
        }
    }

    public static String getWcmLCState(IDfSysObject obj) throws DfException {
        int numLabels = obj.getVersionLabelCount();
        StringBuffer versionLabel = new StringBuffer();
        for (int i = 0; i < numLabels; i++) {
            String label = obj.getVersionLabel(i);
            if (StringUtils.isNotBlank(label)) {
                if (versionLabel.length() > 0) versionLabel.append(",");
                versionLabel.append(label);
            }
        }
        log.debug("r_version_label: " + versionLabel);
        String[] vlabels = StringUtils.split(versionLabel.toString(), ",");
        if (ArrayUtils.contains(vlabels, DctmConstants.WCM_EXPIRED_VERSION_LABEL)) return DctmConstants.WCM_EXPIRED_VERSION_LABEL;
        if (ArrayUtils.contains(vlabels, DctmConstants.WCM_ACTIVE_VERSION_LABEL)) return DctmConstants.WCM_ACTIVE_VERSION_LABEL;
        if (ArrayUtils.contains(vlabels, DctmConstants.WCM_APPROVED_LC_STATE)) return DctmConstants.WCM_APPROVED_LC_STATE;
        if (ArrayUtils.contains(vlabels, DctmConstants.WCM_STAGING_LC_STATE)) return DctmConstants.WCM_STAGING_LC_STATE;
        if (ArrayUtils.contains(vlabels, DctmConstants.WCM_WIP_LC_STATE)) return DctmConstants.WCM_WIP_LC_STATE;
        return null;
    }
}
