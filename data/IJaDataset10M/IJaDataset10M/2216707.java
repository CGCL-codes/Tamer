package com.jvantage.ce.facilities.viewinstance.ejb;

import com.jvantage.ce.acl.ACLGroup;
import com.jvantage.ce.acl.ACLGroupEnum;
import com.jvantage.ce.acl.ejb.ACLServiceLocal;
import com.jvantage.ce.common.Constants;
import com.jvantage.ce.common.DatabaseConstants;
import com.jvantage.ce.common.ObjectAttribute;
import com.jvantage.ce.common.ObjectAttributeSet;
import com.jvantage.ce.common.URLCreator;
import com.jvantage.ce.facilities.FacilitiesException;
import com.jvantage.ce.facilities.application.ejb.ApplicationFacilitiesLocal;
import com.jvantage.ce.facilities.persistence.ejb.PersistenceFacilitiesLocal;
import com.jvantage.ce.facilities.system.ejb.SystemFacilitiesLocal;
import com.jvantage.ce.logging.LogConstants;
import com.jvantage.ce.persistence.DataSourceHelper;
import com.jvantage.ce.persistence.TableAgent;
import com.jvantage.ce.persistence.ejb.TableAgentManagerLocal;
import com.jvantage.ce.presentation.ViewTypesEnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Brent Clay
 */
@EJBs({ @EJB(name = "jvantage/SystemFacilitiesBean", beanInterface = SystemFacilitiesLocal.class) })
@Stateless
public class ViewInstanceFacilitiesBean implements ViewInstanceFacilitiesRemote, ViewInstanceFacilitiesLocal {

    @EJB
    private ApplicationFacilitiesLocal applicationFacilitiesBean;

    @EJB
    private ACLServiceLocal aclServiceBean;

    @EJB
    private PersistenceFacilitiesLocal persistenceFacilitiesBean;

    @EJB
    private TableAgentManagerLocal tableAgentManagerBean;

    private static Logger logger = Logger.getLogger(LogConstants.sfLoggerName_ViewInstanceFacilities);

    private static int instanceCount = 0;

    private static long allGroupID = 0L;

    private DataSourceHelper dataSourceHelper = null;

    private int instanceNumber = 0;

    private Hashtable viewInstanceOptionsHash = null;

    private Hashtable viewTemplateHash = null;

    /**
     *  Verifies that each table has the proper ViewInstances.
     *
     *
     */
    public void createOrVerifyViewInstances(String tableName) throws FacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            throw new FacilitiesException("Invalid tableName argument [" + tableName + "]");
        }
        if (viewInstanceExists(tableName, ViewTypesEnum.AddRecord) == false) {
            logger.info("ViewType [" + ViewTypesEnum.AddRecord.getName() + "] does not exist for table [" + tableName + "]");
            createNewViewInstance(tableName, ViewTypesEnum.AddRecord);
        }
        if (viewInstanceExists(tableName, ViewTypesEnum.ModifyRecord) == false) {
            logger.info("ViewType [" + ViewTypesEnum.ModifyRecord.getName() + "] does not exist for table [" + tableName + "]");
            createNewViewInstance(tableName, ViewTypesEnum.ModifyRecord);
        }
        if (viewInstanceExists(tableName, ViewTypesEnum.DeleteRecord) == false) {
            logger.info("ViewType [" + ViewTypesEnum.DeleteRecord.getName() + "] does not exist for table [" + tableName + "]");
            createNewViewInstance(tableName, ViewTypesEnum.DeleteRecord);
        }
        if (viewInstanceExists(tableName, ViewTypesEnum.ViewRecord) == false) {
            logger.info("ViewType [" + ViewTypesEnum.ViewRecord.getName() + "] does not exist for table [" + tableName + "]");
            createNewViewInstance(tableName, ViewTypesEnum.ViewRecord);
        }
        if (viewInstanceExists(tableName, ViewTypesEnum.ViewList) == false) {
            logger.info("ViewType [" + ViewTypesEnum.ViewList.getName() + "] does not exist for table [" + tableName + "]");
            createNewViewInstance(tableName, ViewTypesEnum.ViewList);
        }
    }

    /**
     *  Returns true if the ViewInstance definition calls for making applicable
     *  list data available to for client-side scripting.
     *
     *
     */
    public boolean getShouldProvideClientSideListBean(String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            throw new FacilitiesException("Null or blank tableName argument.");
        }
        if (viewType == null) {
            throw new FacilitiesException("Null ViewType argument.");
        }
        String key = StringUtils.lowerCase(tableName + "." + viewType.getName() + "." + DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_PROVIDELISTBEAN);
        if (getViewInstanceOptionsHash().containsKey(key)) {
            return ((Boolean) getViewInstanceOptionsHash().get(key)).booleanValue();
        }
        boolean shouldProvideListBean = false;
        String yesOrNo = getViewInstanceValue(tableName, viewType, DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_PROVIDELISTBEAN);
        shouldProvideListBean = "yes".equalsIgnoreCase(yesOrNo);
        if (logger.isDebugEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("Should provide list data bean for client-side scripting: TableName [").append(tableName).append("]  viewType [").append(viewType.getName()).append("] is [").append(shouldProvideListBean).append("].");
            logger.debug(msg);
        }
        getViewInstanceOptionsHash().put(key, new Boolean(shouldProvideListBean));
        return shouldProvideListBean;
    }

    /**
     *  Returns true if the ViewInstance definition calls for resolving one to one
     *  relationships (from the RHS perspective), thereby making the LHS record(s)
     *  available to the User Interface.
     *
     *  This is an optimization.  If the view definition does not require it, then
     *  the costs associated with fetching LHS O2O records will be avoided.
     *
     *
     */
    public boolean getShouldResolveOneToOneRelationships(String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            throw new FacilitiesException("Null or blank tableName argument.");
        }
        if (viewType == null) {
            throw new FacilitiesException("Null ViewType argument.");
        }
        String key = StringUtils.lowerCase(tableName + "." + viewType.getName() + "." + DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_RESOLVEO2O);
        if (getViewInstanceOptionsHash().containsKey(key)) {
            return ((Boolean) getViewInstanceOptionsHash().get(key)).booleanValue();
        }
        boolean shouldResolve = false;
        String yesOrNo = getViewInstanceValue(tableName, viewType, DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_RESOLVEO2O);
        shouldResolve = "yes".equalsIgnoreCase(yesOrNo);
        if (logger.isDebugEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("Should resolve OneToOne relationships for perspective: TableName [").append(tableName).append("]  viewType [").append(viewType.getName()).append("] is [").append(shouldResolve).append("].");
            logger.debug(msg);
        }
        getViewInstanceOptionsHash().put(key, new Boolean(shouldResolve));
        return shouldResolve;
    }

    /**
     *  Returns the name of the Template that is associated with a given SnapIn.
     *  Returns NULL if no template is associated with the named snapIn.
     *
     *  The snapInJNDIName argument is NOT case sensitive.
     *
     *
     */
    public String getSnapInTemplateName(String snapInJNDIName) throws FacilitiesException {
        if (StringUtils.isBlank(snapInJNDIName)) {
            throw new FacilitiesException("Invalid snapInJNDIName argument [" + snapInJNDIName + "]");
        }
        String key = snapInJNDIName.toLowerCase() + ".snapIn";
        String templateName = null;
        if (getViewTemplateHash().containsKey(key)) {
            templateName = (String) getViewTemplateHash().get(key);
            if (StringUtils.isBlank(templateName)) {
                return null;
            }
            return templateName;
        }
        StringBuffer query = new StringBuffer();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            query.append("SELECT ").append(DatabaseConstants.TableFieldName_JV_SNAPIN_TEMPLATENAME).append(" FROM ").append(DatabaseConstants.TableName_JV_SNAPIN).append(" WHERE LOWER(").append(DatabaseConstants.TableFieldName_JV_SNAPIN_NAME).append(") = '").append(snapInJNDIName.toLowerCase()).append("'");
            rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                templateName = rs.getString(DatabaseConstants.TableFieldName_JV_SNAPIN_TEMPLATENAME);
                break;
            }
        } catch (java.sql.SQLException e) {
            DataSourceHelper.logSQLException(e, logger, query);
        } finally {
            DataSourceHelper.releaseResources(conn, stmt, rs);
        }
        if (StringUtils.isBlank(templateName)) {
            templateName = null;
        }
        if (templateName == null) {
            StringBuffer msg = new StringBuffer();
            msg.append("SnapInJNDIName [").append(snapInJNDIName).append("] is not defined in the [").append(DatabaseConstants.TableName_JV_SNAPIN).append("] table.");
            logger.warn(msg.toString());
        } else {
            StringBuffer msg = new StringBuffer();
            msg.append("SnapInJNDIName [").append(snapInJNDIName).append("] is associated with UI Template [").append(templateName).append("].");
            logger.info(msg.toString());
        }
        getViewTemplateHash().put(key, StringUtils.defaultString(templateName));
        return templateName;
    }

    /**
     *  Returns the name of the template that should be used for the group
     *  and viewType context.  Note that this method returns an empty
     *  string ("") if the template is not specified (not null).
     *
     *
     */
    public String getTemplateName(ACLGroup group, String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        if (group == null) {
            String err = "Null ACLGroup argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        if (StringUtils.isBlank(tableName)) {
            String err = "Null or blank TableName argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        if (viewType == null) {
            String err = "Null viewType argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        String key = group.getName().toUpperCase() + "." + tableName.toUpperCase() + "." + viewType.getName();
        if (getViewTemplateHash().containsKey(key)) {
            return (String) getViewTemplateHash().get(key);
        }
        long viewTypeID = viewType.getID();
        long tableID = 0L;
        try {
            tableID = persistenceFacilitiesBean.getTableID(tableName);
        } catch (Exception e) {
            String err = "Exception fetching TableID for table [" + tableName + "].";
            logger.error(err, e);
            throw new FacilitiesException(err, e);
        }
        try {
            if (allGroupID == 0L) {
                allGroupID = aclServiceBean.getGroupID(ACLGroupEnum.All_Groups.getName());
            }
        } catch (Exception e) {
            String err = "Exception fetching GroupID for group [" + group + "] -- all groups.";
            logger.error(err, e);
            throw new FacilitiesException(err, e);
        }
        long groupID = 0L;
        try {
            groupID = aclServiceBean.getGroupID(group.getName());
        } catch (Exception e) {
            String err = "Exception fetching GroupID for group [" + group + "].";
            logger.error(err, e);
            throw new FacilitiesException(err, e);
        }
        String templateName = null;
        StringBuffer query = new StringBuffer();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            query.append("SELECT vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_NAME).append(", vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_GROUPID).append(" FROM ").append(DatabaseConstants.TableName_JV_VIEWTEMPLATES).append(" vt, ").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append(" vi WHERE vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_GROUPID);
            if (groupID == allGroupID) {
                query.append(" = ").append(groupID);
            } else {
                query.append(" IN (").append(groupID).append(", ").append(allGroupID).append(")");
            }
            query.append(" AND vi.").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_VIEWTYPE).append(" = ").append(viewTypeID).append(" AND vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_VIEWINSTANCE).append(" = vi.").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_ID).append(" AND vi.").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_TABLEID).append(" = ").append(tableID);
            rs = stmt.executeQuery(query.toString());
            int i = 0;
            long groupID1 = 0L;
            long groupID2 = 0L;
            String templateName1 = null;
            String templateName2 = null;
            while (rs.next()) {
                if (i++ > 1) {
                    String ls = SystemUtils.LINE_SEPARATOR;
                    StringBuffer msg = new StringBuffer();
                    msg.append("More than two templates are associated with context:").append(ls).append(Constants.sfIndent).append("Group [").append(group.getName()).append("] (").append(groupID).append(")").append(ls).append(Constants.sfIndent).append("Table [").append(tableName).append("] (").append(tableID).append(")").append(ls).append(Constants.sfIndent).append(" View [").append(viewType.getName()).append("] (").append(viewTypeID).append(")");
                    logger.info(msg);
                    break;
                }
                if (groupID1 == 0L) {
                    templateName1 = rs.getString(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_NAME);
                    groupID1 = rs.getLong(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_GROUPID);
                } else {
                    templateName2 = rs.getString(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_NAME);
                    groupID2 = rs.getLong(DatabaseConstants.TableFieldName_JV_VIEWTEMPLATES_GROUPID);
                }
                if (groupID2 > 0L) {
                    templateName = (groupID1 == allGroupID) ? templateName2 : templateName1;
                } else {
                    templateName = templateName1;
                }
            }
        } catch (java.sql.SQLException e) {
            DataSourceHelper.logSQLException(e, logger, query);
        } finally {
            DataSourceHelper.releaseResources(conn, stmt, rs);
        }
        getViewTemplateHash().put(key, StringUtils.defaultString(templateName));
        if (logger.isDebugEnabled()) {
            String ls = SystemUtils.LINE_SEPARATOR;
            StringBuffer msg = new StringBuffer();
            msg.append("TemplateName [").append(templateName).append("] is associated with context:").append(ls).append(Constants.sfIndent).append("Group [").append(group.getName()).append("] (").append(groupID).append(")").append(ls).append(Constants.sfIndent).append("Table [").append(tableName).append("] (").append(tableID).append(")").append(ls).append(Constants.sfIndent).append(" View [").append(viewType.getName()).append("] (").append(viewTypeID).append(")");
            logger.info(msg);
        }
        return templateName;
    }

    /**
     *
     */
    public String getViewInstanceFormValidationSnapIn(String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            String err = "Null or blank TableName argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        if (viewType == null) {
            String err = "Null ViewTypesEnum argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        String key = tableName.toUpperCase() + "." + viewType.getName() + ".valSnapIn";
        if (getViewInstanceOptionsHash().containsKey(key)) {
            return (String) getViewInstanceOptionsHash().get(key);
        }
        String snapIn = null;
        try {
            snapIn = getViewInstanceValue(tableName, viewType, DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_VALIDATIONSNAPIN);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        if (logger.isInfoEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("Form validation snapIn for tableName [").append(tableName).append("] viewType [").append(viewType.getName()).append("] is [").append(snapIn).append("].");
            logger.info(msg);
        }
        if (StringUtils.isBlank(snapIn)) {
            return null;
        }
        getViewInstanceOptionsHash().put(key, snapIn);
        return snapIn;
    }

    /**
     *
     */
    public String getViewInstanceHelpURL(String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        String helpURL = null;
        if (StringUtils.isBlank(tableName)) {
            String err = "Null or blank TableName argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        if (viewType == null) {
            String err = "Null ViewTypesEnum argument.";
            logger.error(err);
            throw new FacilitiesException(err);
        }
        String key = tableName.toUpperCase() + "." + viewType.getName() + ".helpURL";
        if (getViewInstanceOptionsHash().containsKey(key)) {
            return (String) getViewInstanceOptionsHash().get(key);
        }
        try {
            helpURL = getViewInstanceValue(tableName, viewType, DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_HELPURL);
            if (StringUtils.isBlank(helpURL)) {
                String appName = applicationFacilitiesBean.getApplicationNameFromTableName(tableName);
                if (StringUtils.isNotBlank(appName)) {
                    StringBuffer buf = new StringBuffer();
                    buf.append("help?").append(URLCreator.fURLParameter_ApplicationName).append("=").append(appName).append("&").append(URLCreator.fURLParameter_Table).append("=").append(tableName);
                    helpURL = buf.toString();
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        if (StringUtils.isBlank(helpURL)) {
            return null;
        }
        getViewInstanceOptionsHash().put(key, helpURL);
        return helpURL;
    }

    /**
     *  Removes all chached static objects being held by this object in 'this'
     *  JVM.
     *
     *
     */
    public void initCache() {
        logger.info(this.getClass().getName() + ": Purging cached items.");
        try {
            getViewTemplateHash().clear();
            getViewInstanceOptionsHash().clear();
        } catch (Exception e) {
            logger.error("Exception -- Processing will continue.", e);
        }
    }

    private void createNewViewInstance(String tableName, ViewTypesEnum viewType) {
        try {
            logger.info("Preparing to create ViewInstance [" + ViewTypesEnum.AddRecord.getName() + "] for table [" + tableName + "]");
            TableAgent ta = getTableAgent(DatabaseConstants.TableName_JV_VIEWINSTANCE);
            ObjectAttributeSet oas = (ObjectAttributeSet) ta.getTableAgentObjectAttributes().clone();
            long tableID = persistenceFacilitiesBean.getTableID(tableName);
            long viewTypeID = persistenceFacilitiesBean.getViewTypeID(viewType);
            ObjectAttribute tableOA = oas.getObjectWithName(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_TABLEID);
            if (tableOA == null) {
                StringBuffer msg = new StringBuffer();
                msg.append("Unable to fetch ObjectAttribute with name [").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_TABLEID).append("] from table [").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append("] ObjectAttributeSet.");
                logger.error(msg.toString());
            }
            tableOA.setValue(new Long(tableID));
            ObjectAttribute viewTypeOA = oas.getObjectWithName(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_VIEWTYPE);
            if (viewTypeOA == null) {
                StringBuffer msg = new StringBuffer();
                msg.append("Unable to fetch ObjectAttribute with name [").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_VIEWTYPE).append("] from table [").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append("] ObjectAttributeSet.");
                logger.error(msg.toString());
            }
            viewTypeOA.setValue(new Long(viewTypeID));
            ObjectAttribute nameOA = oas.getObjectWithName(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_NAME);
            if (nameOA == null) {
                StringBuffer msg = new StringBuffer();
                msg.append("Unable to fetch ObjectAttribute with name [").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_NAME).append("] from table [").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append("] ObjectAttributeSet.");
                logger.error(msg.toString());
            }
            nameOA.setValue(viewType.getName());
            ObjectAttribute descOA = oas.getObjectWithName(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_DESCRIPTION);
            if (descOA == null) {
                StringBuffer msg = new StringBuffer();
                msg.append("Unable to fetch ObjectAttribute with name [").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_DESCRIPTION).append("] from table [").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append("] ObjectAttributeSet.");
                logger.error(msg.toString());
            }
            descOA.setValue(viewType.getName() + " view instance definition for table " + tableName + ".");
            ta.insertDatabaseRecord(oas);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    private DataSource getDataSource() {
        return getDataSourceHelper().getDataSource();
    }

    private DataSourceHelper getDataSourceHelper() {
        if (dataSourceHelper == null) {
            dataSourceHelper = new DataSourceHelper();
        }
        return dataSourceHelper;
    }

    private TableAgent getTableAgent(String tableName) throws FacilitiesException {
        return tableAgentManagerBean.getTableAgent(tableName);
    }

    private Hashtable getViewInstanceOptionsHash() {
        if (viewInstanceOptionsHash == null) {
            viewInstanceOptionsHash = new Hashtable();
        }
        return viewInstanceOptionsHash;
    }

    /**
     *  Returns the value of the named field (fieldName) from the viewInstance associated
     *  with the argument tableName and ViewType.
     *
     *  @param fieldName must correspond to a field name that can be found in the ViewInstance table.
     */
    private String getViewInstanceValue(String tableName, ViewTypesEnum viewType, String fieldName) throws FacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            throw new FacilitiesException("Invalid tableName argument [" + tableName + "]");
        }
        if (StringUtils.isBlank(fieldName)) {
            throw new FacilitiesException("Invalid fieldName argument [" + tableName + "]");
        }
        if (viewType == null) {
            throw new FacilitiesException("Null viewType argument.");
        }
        String value = null;
        boolean valueWasFound = false;
        StringBuffer query = new StringBuffer();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            query.append("SELECT ").append("vi.").append(fieldName.toUpperCase()).append(" FROM ").append(DatabaseConstants.TableName_JV_VIEWINSTANCE).append(" vi, ").append(DatabaseConstants.TableName_JV_VIEWTYPES).append(" vt, ").append(DatabaseConstants.TableName_JV_TABLEATTRS).append(" ta ").append(" WHERE ").append("vi.").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_TABLEID).append(" = ta.").append(DatabaseConstants.TableFieldName_JV_TABLEATTRS_ID).append(" AND ").append("vi.").append(DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_VIEWTYPE).append(" = vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTYPES_ID).append(" AND UPPER(ta.").append(DatabaseConstants.TableFieldName_JV_TABLEATTRS_NAME).append(") = '").append(tableName.toUpperCase()).append("' AND UPPER(vt.").append(DatabaseConstants.TableFieldName_JV_VIEWTYPES_NAME).append(") = '").append(viewType.getName().toUpperCase()).append("'");
            rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                value = rs.getString(fieldName.toUpperCase());
                valueWasFound = true;
                break;
            }
        } catch (java.sql.SQLException e) {
            DataSourceHelper.logSQLException(e, logger, query);
        } finally {
            DataSourceHelper.releaseResources(conn, stmt, rs);
        }
        if (valueWasFound == false) {
            StringBuffer msg = new StringBuffer();
            msg.append("Field [").append(fieldName).append("] was not found in ViewInstance [").append(viewType.getName()).append("] for table [").append(tableName).append("].");
            logger.warn(msg);
        }
        return value;
    }

    private Hashtable getViewTemplateHash() {
        if (viewTemplateHash == null) {
            viewTemplateHash = new Hashtable();
        }
        return viewTemplateHash;
    }

    /**
     * Determines whether the ViewType exists for the given TableName.
     *
     * @param tableName - Name of the table that the ViewType should be associated with.
     * @param viewType - The ViewType.
     *
     * @return True / False
     *
     * @throws FacilitiesException if either argument is invalid.
     */
    private boolean viewInstanceExists(String tableName, ViewTypesEnum viewType) throws FacilitiesException {
        boolean viewInstanceFound = false;
        try {
            viewInstanceFound = StringUtils.isNotBlank(getViewInstanceValue(tableName, viewType, DatabaseConstants.TableFieldName_JV_VIEWINSTANCE_ID));
        } catch (Exception e) {
            return false;
        }
        return viewInstanceFound;
    }
}
