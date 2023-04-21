package com.jvantage.ce.facilities.external.ejb.instances.jvantage;

import com.jvantage.ce.common.Constants;
import com.jvantage.ce.common.DatabaseConstants;
import com.jvantage.ce.common.ObjectAttribute;
import com.jvantage.ce.common.ObjectAttributeSet;
import com.jvantage.ce.common.TagConstants;
import com.jvantage.ce.common.URLCreator;
import com.jvantage.ce.facilities.application.ejb.ApplicationFacilitiesLocal;
import com.jvantage.ce.facilities.external.SnapInException;
import com.jvantage.ce.facilities.external.SnapInResponse;
import com.jvantage.ce.facilities.icon.ejb.IconFacilitiesLocal;
import com.jvantage.ce.logging.LogConstants;
import com.jvantage.ce.logging.LogHelper;
import com.jvantage.ce.persistence.DataSourceHelper;
import com.jvantage.ce.persistence.TableAgent;
import com.jvantage.ce.persistence.ejb.TableAgentManagerLocal;
import com.jvantage.ce.presentation.InputFormFieldStateEnum;
import com.jvantage.ce.presentation.InputFormRuleOperatorEnum;
import com.jvantage.ce.presentation.PageContext;
import com.jvantage.ce.presentation.html.HTML;
import com.jvantage.ce.presentation.html.InputFormFieldBehavior;
import com.jvantage.ce.presentation.html.InputFormFieldBehaviorSet;
import com.jvantage.ce.presentation.html.JavaScript;
import com.jvantage.ce.presentation.html.StyleSheetClassNameEnum;
import com.jvantage.ce.property.PropertyEnum;
import com.jvantage.ce.property.ejb.PropertyDispenserLocal;
import com.jvantage.ce.session.SessionID;
import com.jvantage.ce.session.SessionKey;
import com.jvantage.ce.session.UserRequestMessage;
import com.jvantage.ce.session.ejb.SessionStoreLocal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Brent Clay
 */
@Stateless
public class FieldBehaviorBean implements FieldBehaviorRemote, FieldBehaviorLocal {

    @EJB
    private IconFacilitiesLocal iconFacilitiesBean;

    @EJB
    private PropertyDispenserLocal propertyDispenserBean;

    @EJB
    private SessionStoreLocal sessionStoreBean;

    @EJB
    private TableAgentManagerLocal tableAgentManagerBean;

    @EJB
    private ApplicationFacilitiesLocal applicationFacilitiesBean;

    public static final String sfModifyBehaviorIDParameter = "___fbsib1";

    public static final String sfDeleteBehaviorIDParameter = "___fbsib2";

    public static final String sfHighlightRowWithBehaviorIDParameter = "___fbsib3";

    public static final String sfDbMetaDataTableNameParameter = "___fbsib4";

    public static final String sfSecondaryParameterForFunctionCalls = "___fbsib5";

    public static final String sfShowJavaScriptParameter = "___fbsib6";

    public static final String sfMoveBehaviorIDParameter = "___fbsib7";

    public static final String sfMoveBehaviorInDirectionParameter = "___fbsib8";

    public static final String sfMoveBehaviorInDirectionUp = "up";

    public static final String sfMoveBehaviorInDirectionDown = "down";

    private static final int ORDERBY_BY_DELTA_VALUE = 10;

    private static final String FORM_PARAM_SUBMIT_BUTTON = "submit";

    private static final String FORM_PARAM_SUBMIT_BUTTON_ADD_INVERSE_RULE = "submit2";

    private static final String FORM_PARAM_SUBMIT_BUTTON_CANCEL_CHANGES = "submit3";

    private static final String HOLD_FOR_MODIFY_SESSION_KEY = "___jvHFMSK";

    private static Logger logger = Logger.getLogger(LogConstants.sfLoggerName_SnapIn);

    private DataSourceHelper dataSourceHelper = null;

    /**
     *
     */
    public SnapInResponse delete(PageContext pageContext) throws SnapInException {
        SessionID sessionID = pageContext.getSessionID();
        UserRequestMessage request = pageContext.getUserRequestMessage();
        Long trxNumber = request.getTransactionNumber();
        String behaviorIDString = request.getParameterValue(sfDeleteBehaviorIDParameter);
        logger.info(LogHelper.msg(sessionID, trxNumber, "Deleting Behavior [" + behaviorIDString + "]."));
        if (StringUtils.isNotBlank(behaviorIDString)) {
            try {
                long behaviorID = NumberUtils.toInt(behaviorIDString);
                if (behaviorID > 0L) {
                    applicationFacilitiesBean.deleteInputFormBehavior(behaviorID);
                }
            } catch (Exception e) {
                logger.error(LogHelper.msg(sessionID, trxNumber, "Exception"), e);
                throw new SnapInException("Exception", e);
            }
        }
        String tableName = request.getParameterValue(URLCreator.fURLParameter_Table);
        if (StringUtils.isNotBlank(tableName)) {
            renumberOrderBy(tableName);
        }
        return execute(pageContext);
    }

    public SnapInResponse execute(PageContext pageContext) throws SnapInException {
        if (logger.isInfoEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("Executing SnapIn Method [execute] on Class [").append(this.getClass().getName()).append("] for SessionID [").append(pageContext.getSessionID()).append("]");
            logger.info(msg);
        }
        SnapInResponse siResponse = pageContext.getSnapInResponse();
        try {
            UserRequestMessage request = pageContext.getUserRequestMessage();
            if (request == null) {
                StringBuffer msg = new StringBuffer();
                msg.append("Unable to access UserRequestMessage SnapIn Method [execute] on Class [").append(this.getClass().getName()).append("] for SessionID [").append(pageContext.getSessionID()).append("]");
                logger.error(msg);
                throw new SnapInException(msg.toString());
            }
            SessionID sessionID = pageContext.getSessionID();
            String tableName = request.getParameterValue(URLCreator.fURLParameter_Table);
            if (StringUtils.isBlank(tableName)) {
                StringBuffer msg = new StringBuffer();
                msg.append("No TableName on UserRequestMessage.");
                logger.error(LogHelper.msg(sessionID, request.getTransactionNumber(), msg.toString()));
                throw new SnapInException(msg.toString());
            }
            String ID = request.getParameterValue(URLCreator.fURLParameter_ID);
            if (StringUtils.isBlank(ID)) {
                StringBuffer msg = new StringBuffer();
                msg.append("No TableField ID on UserRequestMessage.");
                logger.error(LogHelper.msg(sessionID, request.getTransactionNumber(), msg.toString()));
                throw new SnapInException(msg.toString());
            }
            URLCreator url = new URLCreator();
            url.setParameter(URLCreator.fURLParameter_Table, DatabaseConstants.TableName_JV_TABLEATTRS);
            url.setParameter(URLCreator.fURLParameter_ID, request.getParameterValue(URLCreator.fURLParameter_ID));
            url.setParameter(URLCreator.fURLParameter_ViewType, URLCreator.fViewType_ViewRecord);
            StringBuffer content = new StringBuffer();
            content.append(HTML.text("Set Form Input Field Behaviors for Table: " + tableName, HTML.className(StyleSheetClassNameEnum.Title.getName()))).append(HTML.breakTag()).append(HTML.anchor("(return to " + tableName + " table view)", url.toString())).append(HTML.breakTag()).append(HTML.breakTag());
            TableAgent ta = getTableAgent(tableName);
            ObjectAttributeSet oas = ta.getTableAgentObjectAttributes();
            URLCreator formActionURL = getBaseURL(pageContext);
            formActionURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "save");
            final String pad = HTML.noBreakSpace(2);
            int i = 0;
            content.append(renderInputFormArea(request, tableName, formActionURL)).append(HTML.breakTag(2)).append(renderExistingBehaviors(pageContext, tableName));
            if (StringUtils.isNotBlank(request.getParameterValue(sfShowJavaScriptParameter))) {
                content.append(HTML.breakTag(2)).append(renderBehaviorSetAsJavaScript(pageContext, tableName));
            } else {
                URLCreator showJavaScriptURL = getBaseURL(pageContext);
                showJavaScriptURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "execute");
                showJavaScriptURL.setParameter(sfShowJavaScriptParameter, "true");
                content.append(HTML.breakTag(1)).append(HTML.anchor("Show JavaScript", showJavaScriptURL.toString()));
            }
            siResponse.setTagValue(TagConstants.sfBodyBeg, content.toString());
        } catch (Exception e) {
            logger.error("Exception in SnapIn", e);
            StringBuffer msg = new StringBuffer();
            msg.append("Exception in SnapIn Method [execute] on Class [").append(this.getClass().getName()).append("] for SessionID [").append(pageContext.getSessionID()).append("]");
            logger.info(msg);
            throw new SnapInException(msg.toString());
        }
        return siResponse;
    }

    /**
     *
     *
     * @param tableName
     *
     * @return
     *
     * @throws SnapInException
     */
    private TableAgent getTableAgent(String tableName) throws SnapInException {
        return tableAgentManagerBean.getTableAgent(tableName);
    }

    /**
     *
     */
    public SnapInResponse modify(PageContext pageContext) throws SnapInException {
        SessionID sessionID = pageContext.getSessionID();
        UserRequestMessage request = pageContext.getUserRequestMessage();
        Long trxNumber = request.getTransactionNumber();
        String tableName = request.getParameterValue(URLCreator.fURLParameter_Table);
        String behaviorIDString = request.getParameterValue(sfModifyBehaviorIDParameter);
        logger.info(LogHelper.msg(sessionID, trxNumber, "Modifying Behavior [" + behaviorIDString + "]."));
        if (StringUtils.isNotBlank(behaviorIDString)) {
            try {
                long behaviorID = NumberUtils.toInt(behaviorIDString);
                sessionStoreBean.putValue(sessionID, new SessionKey(HOLD_FOR_MODIFY_SESSION_KEY), new Long(behaviorID));
                InputFormFieldBehavior behavior = null;
                if (behaviorID > 0L) {
                    InputFormFieldBehaviorSet behaviorSet = applicationFacilitiesBean.getInputFormBehaviors(tableName);
                    if ((behaviorSet == null) || behaviorSet.isEmpty()) {
                        return execute(pageContext);
                    }
                    behavior = behaviorSet.getBehaviorWithID(behaviorID);
                    if (behavior == null) {
                        return execute(pageContext);
                    }
                }
                URLCreator modifyURL = getBaseURL(pageContext);
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_SOURCEFIELD.toLowerCase(), String.valueOf(behavior.getSourceFieldID()));
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TARGETFIELD.toLowerCase(), String.valueOf(behavior.getTargetFieldID()));
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_CONTROLSTATE.toLowerCase(), behavior.getFieldState().getName());
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_OPERATOR.toLowerCase(), behavior.getOperator().getName());
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE.toLowerCase(), behavior.getSourceFieldValue());
                modifyURL.setParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY.toLowerCase(), String.valueOf(behavior.getOrderBy()));
                modifyURL.setParameter(sfModifyBehaviorIDParameter, behaviorIDString);
                modifyURL.setParameter(sfHighlightRowWithBehaviorIDParameter, behaviorIDString);
                UserRequestMessage urm = new UserRequestMessage(modifyURL);
                pageContext.setUserRequestMessage(urm);
            } catch (Exception e) {
                logger.error(LogHelper.msg(sessionID, trxNumber, "Exception"), e);
                throw new SnapInException("Exception", e);
            }
        }
        return execute(pageContext);
    }

    /**
     *  Surprisingly, moving items up and down in the list is very expensive.
     *  Requires renumbering and reloading, behaviors, etc.
     */
    public SnapInResponse moveBehaviorInSequence(PageContext pageContext) throws SnapInException {
        Map map = Collections.synchronizedMap(new TreeMap());
        SessionID sessionID = pageContext.getSessionID();
        UserRequestMessage request = pageContext.getUserRequestMessage();
        Long trxNumber = request.getTransactionNumber();
        String tableName = request.getParameterValue(URLCreator.fURLParameter_Table);
        String behaviorIDString = request.getParameterValue(sfMoveBehaviorIDParameter);
        String directionString = request.getParameterValue(sfMoveBehaviorInDirectionParameter);
        logger.info(LogHelper.msg(sessionID, trxNumber, "Moving Behavior " + directionString + " in Sequence [" + behaviorIDString + "]."));
        if (StringUtils.isNotBlank(behaviorIDString) && StringUtils.isNotBlank(tableName)) {
            try {
                long behaviorID = NumberUtils.toInt(behaviorIDString);
                if (behaviorID > 0L) {
                    renumberOrderBy(tableName);
                    Thread.sleep(250);
                    moveBehaviorUpOrDownInSequence(behaviorID, sfMoveBehaviorInDirectionUp.equalsIgnoreCase(directionString));
                    Thread.sleep(250);
                    renumberOrderBy(tableName);
                    Thread.sleep(250);
                    applicationFacilitiesBean.clearInputFormBehaviorCache();
                    request.setParameterNameAndValue(sfHighlightRowWithBehaviorIDParameter, behaviorIDString);
                }
            } catch (Exception e) {
                logger.error(LogHelper.msg(sessionID, trxNumber, "Exception"), e);
                throw new SnapInException("Exception", e);
            }
        }
        return execute(pageContext);
    }

    /**
     *
     */
    public SnapInResponse save(PageContext pageContext) throws SnapInException {
        SessionID sessionID = pageContext.getSessionID();
        UserRequestMessage request = pageContext.getUserRequestMessage();
        if (StringUtils.isNotBlank(request.getParameterValue(FORM_PARAM_SUBMIT_BUTTON_CANCEL_CHANGES))) {
            removeFormDataFromRequest(request);
            return execute(pageContext);
        }
        String behaviorIDString = request.getParameterValue(sfModifyBehaviorIDParameter);
        boolean inModifyMode = StringUtils.isNotBlank(behaviorIDString);
        Long trxNumber = request.getTransactionNumber();
        if (inModifyMode) {
            logger.info(LogHelper.msg(sessionID, trxNumber, "Saving Modified Behavior"));
        } else {
            logger.info(LogHelper.msg(sessionID, trxNumber, "Saving New Behavior"));
        }
        logger.debug(LogHelper.msg(sessionID, trxNumber, "Request: " + request.toString()));
        String sourceField = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_SOURCEFIELD.toLowerCase());
        String targetField = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TARGETFIELD.toLowerCase());
        String value = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE.toLowerCase());
        String controlState = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_CONTROLSTATE.toLowerCase());
        String operator = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_OPERATOR.toLowerCase());
        String orderBy = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY.toLowerCase());
        String tableName = request.getParameterValue(URLCreator.fURLParameter_Table);
        String shouldAddInverseRule = request.getParameterValue(FORM_PARAM_SUBMIT_BUTTON_ADD_INVERSE_RULE);
        InputFormFieldBehavior behavior = new InputFormFieldBehavior();
        behavior.setOrderBy(NumberUtils.toInt(orderBy));
        behavior.setSourceFieldID(NumberUtils.toInt(sourceField));
        behavior.setOperator(InputFormRuleOperatorEnum.getEnum(operator));
        behavior.setTargetFieldID(NumberUtils.toInt(targetField));
        behavior.setSourceFieldValue(value);
        behavior.setFieldState(InputFormFieldStateEnum.getEnum(controlState));
        behavior.setTableName(tableName);
        if (inModifyMode == false) {
            behavior.setOrderBy(99999);
        }
        boolean successfulSave = true;
        long idOfSavedBehavior = 0L;
        try {
            idOfSavedBehavior = applicationFacilitiesBean.saveInputFormBehavior(behavior);
            if (StringUtils.isNotBlank(shouldAddInverseRule)) {
                InputFormRuleOperatorEnum operatorEnum = behavior.getOperator();
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.equals)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.notEquals);
                    }
                }
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.notEquals)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.equals);
                    }
                }
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.greaterThan)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.equalOrLessThan);
                    }
                }
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.lessThan)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.equalOrGreaterThan);
                    }
                }
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.equalOrGreaterThan)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.lessThan);
                    }
                }
                if (operatorEnum != null) {
                    if (operatorEnum.equals(InputFormRuleOperatorEnum.equalOrLessThan)) {
                        behavior.setOperator(InputFormRuleOperatorEnum.greaterThan);
                    }
                }
                if (InputFormFieldStateEnum.enable.equals(behavior.getFieldState())) {
                    behavior.setFieldState(InputFormFieldStateEnum.disable);
                } else if (InputFormFieldStateEnum.disable.equals(behavior.getFieldState())) {
                    behavior.setFieldState(InputFormFieldStateEnum.enable);
                } else if (InputFormFieldStateEnum.readOnly.equals(behavior.getFieldState())) {
                    behavior.setFieldState(InputFormFieldStateEnum.notReadOnly);
                } else if (InputFormFieldStateEnum.notReadOnly.equals(behavior.getFieldState())) {
                    behavior.setFieldState(InputFormFieldStateEnum.readOnly);
                }
                behavior.setOrderBy(behavior.getOrderBy() + 10);
                applicationFacilitiesBean.saveInputFormBehavior(behavior);
            }
            renumberOrderBy(tableName);
        } catch (Exception e) {
            successfulSave = false;
            logger.error(LogHelper.msg(sessionID, trxNumber, "Exception"), e);
        }
        if (successfulSave) {
            removeFormDataFromRequest(request);
            try {
                Long ID = (Long) sessionStoreBean.getValueAndRemoveFromSession(sessionID, new SessionKey(HOLD_FOR_MODIFY_SESSION_KEY));
                if (ID != null) {
                    applicationFacilitiesBean.deleteInputFormBehavior(ID.longValue());
                }
            } catch (Exception e1) {
                logger.error(LogHelper.msg(pageContext.getSessionID(), pageContext.getTransactionNumber(), "Could not update ErrorMessageList"), e1);
            }
        }
        request.setParameterNameAndValue(sfHighlightRowWithBehaviorIDParameter, String.valueOf(idOfSavedBehavior));
        logger.debug("===>ID of New or Modified Record:" + idOfSavedBehavior);
        return execute(pageContext);
    }

    private static String capitalizeString(String tag) {
        if (tag == null) {
            return "";
        }
        return tag.toUpperCase();
    }

    private URLCreator getBaseURL(PageContext pageContext) {
        if (pageContext == null) {
            return null;
        }
        UserRequestMessage request = pageContext.getUserRequestMessage();
        if (request == null) {
            return null;
        }
        URLCreator url = new URLCreator();
        url.setParameter(URLCreator.fURLParameter_ViewType, request.getParameterValue(URLCreator.fURLParameter_ViewType));
        url.setParameter(URLCreator.fURLParameter_SnapInType, request.getParameterValue(URLCreator.fURLParameter_SnapInType));
        url.setParameter(URLCreator.fURLParameter_JNDIName, request.getParameterValue(URLCreator.fURLParameter_JNDIName));
        url.setParameter(URLCreator.fURLParameter_ID, request.getParameterValue(URLCreator.fURLParameter_ID));
        url.setParameter(URLCreator.fURLParameter_Table, request.getParameterValue(URLCreator.fURLParameter_Table));
        return url;
    }

    private DataSource getDataSource() throws SnapInException {
        return getDataSourceHelper().getDataSource();
    }

    private DataSourceHelper getDataSourceHelper() throws SnapInException {
        if (dataSourceHelper == null) {
            dataSourceHelper = new DataSourceHelper();
        }
        return dataSourceHelper;
    }

    private String getPropertyValue(String propertyName) throws SnapInException {
        if (propertyName == null) {
            throw new SnapInException("Null propertyName argument.");
        }
        String value = null;
        try {
            value = propertyDispenserBean.getPropertyValue(propertyName);
        } catch (Exception e) {
            throw new SnapInException("Property [" + propertyName + "] is not defined.", e);
        }
        return value;
    }

    private void moveBehaviorUpOrDownInSequence(long behaviorID, boolean moveUp) throws SnapInException {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            sql.append("UPDATE ").append(DatabaseConstants.TableName_JV_FIELDBEHAVIOR).append(" SET ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY).append(" = ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY).append(moveUp ? " - " : " + ").append(ORDERBY_BY_DELTA_VALUE * 1.5).append(" WHERE ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID).append(" = ").append(behaviorID);
            stmt.execute(sql.toString());
        } catch (java.sql.SQLException e) {
            DataSourceHelper.logSQLException(e, logger, sql);
        } finally {
            DataSourceHelper.releaseResources(conn, stmt);
        }
    }

    private UserRequestMessage removeFormDataFromRequest(UserRequestMessage request) {
        if (request == null) {
            return null;
        }
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_SOURCEFIELD.toLowerCase());
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TARGETFIELD.toLowerCase());
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE.toLowerCase());
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_CONTROLSTATE.toLowerCase());
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_OPERATOR.toLowerCase());
        request.removeParameter(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY.toLowerCase());
        request.removeParameter(sfModifyBehaviorIDParameter);
        request.removeParameter(sfHighlightRowWithBehaviorIDParameter);
        return request;
    }

    private String renderBehaviorSetAsJavaScript(PageContext pageContext, String tableName) throws SnapInException {
        StringBuffer content = new StringBuffer();
        try {
            InputFormFieldBehaviorSet behaviorSet = applicationFacilitiesBean.getInputFormBehaviors(tableName);
            URLCreator hideJavaScriptURL = getBaseURL(pageContext);
            hideJavaScriptURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "execute");
            JavaScript js = new JavaScript();
            content.append(HTML.text("Generated JavaScript ")).append(HTML.anchor("(hide)", hideJavaScriptURL.toString())).append(HTML.breakTag()).append(HTML.textAreaWithDefaultValue("behaviorJS", 100, 30, js.generateFormBehaviorJavaScript(pageContext, behaviorSet), null));
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new SnapInException("Exception", e);
        }
        return content.toString();
    }

    private StringBuffer renderExistingBehaviors(PageContext pageContext, String tableName) throws SnapInException {
        UserRequestMessage request = pageContext.getUserRequestMessage();
        String idOfBehaviorBeingModifiedString = request.getParameterValue(sfModifyBehaviorIDParameter);
        int idOfBehaviorBeingModified = 0;
        boolean inModifyMode = StringUtils.isNotBlank(idOfBehaviorBeingModifiedString);
        if (inModifyMode) {
            idOfBehaviorBeingModified = NumberUtils.toInt(idOfBehaviorBeingModifiedString);
        }
        StringBuffer content = new StringBuffer();
        InputFormFieldBehaviorSet behaviorSet = null;
        try {
            behaviorSet = applicationFacilitiesBean.getInputFormBehaviors(tableName);
            if (logger.isDebugEnabled()) {
                StringBuffer msg = new StringBuffer();
                msg.append("Field Form Behaviors for Table [" + tableName + "].").append(SystemUtils.LINE_SEPARATOR);
                if (behaviorSet == null) {
                    msg.append(Constants.sfIndent).append("InputFormFieldBehaviorSet is NULL for table [").append(tableName).append("].");
                } else {
                    msg.append(behaviorSet.toString());
                }
                logger.debug(msg);
            }
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new SnapInException("Exception", e);
        }
        if ((behaviorSet == null) || behaviorSet.isEmpty()) {
            return content;
        }
        String deleteListHeaderIcon = null;
        String deleteListRowIcon = null;
        String modifyListHeaderIcon = null;
        String modifyListRowIcon = null;
        String upListHeaderIcon = null;
        String upListRowIcon = null;
        String downListHeaderIcon = null;
        String downListRowIcon = null;
        try {
            deleteListHeaderIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_HEADER_DELETE_ICON);
            deleteListRowIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_ROW_DELETE_ICON);
            modifyListHeaderIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_HEADER_MODIFY_ICON);
            modifyListRowIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_ROW_MODIFY_ICON);
            upListHeaderIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_HEADER_UP_ARROW_ICON);
            upListRowIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_ROW_UP_ICON);
            downListHeaderIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_HEADER_DOWN_ARROW_ICON);
            downListRowIcon = iconFacilitiesBean.getIcon(pageContext.getCurrentApplicationName(), PropertyEnum.LIST_ROW_DOWN_ICON);
        } catch (Exception e) {
        }
        final String pad = HTML.noBreakSpace(2);
        content.append(HTML.tableBeg("border=\"0\"")).append(HTML.tableRowBeg()).append(HTML.tableData(pad + "#" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow))).append(HTML.tableData(pad + HTML.image(upListHeaderIcon) + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " Title=\"Move Up\" Align=\"Center\"")).append(HTML.tableData(pad + HTML.image(downListHeaderIcon) + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " Title=\"Move Down\" Align=\"Center\"")).append(HTML.tableData(pad + HTML.image(modifyListHeaderIcon) + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " Title=\"Modify\" Align=\"Center\"")).append(HTML.tableData(pad + HTML.image(deleteListHeaderIcon) + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " Title=\"Delete\" Align=\"Center\"")).append(HTML.tableData(pad + "Source Field" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow))).append(HTML.tableData(pad + "Operator" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow))).append(HTML.tableData(pad + "Value" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow))).append(HTML.tableData(pad + "Set Field Behavior" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " align=\"center\"")).append(HTML.tableData(pad + "Target Field" + pad, HTML.className(StyleSheetClassNameEnum.TableSubHeadingRow) + " align=\"center\"")).append(HTML.tableRowEnd());
        URLCreator modifyURL = getBaseURL(pageContext);
        URLCreator deleteURL = getBaseURL(pageContext);
        URLCreator upURL = getBaseURL(pageContext);
        URLCreator downURL = getBaseURL(pageContext);
        String highlightIDString = request.getAndRemoveParameterValue(sfHighlightRowWithBehaviorIDParameter);
        int highlightID = 0;
        if (StringUtils.isNotBlank(highlightIDString)) {
            highlightID = NumberUtils.toInt(highlightIDString);
        }
        for (int i = 0; i < behaviorSet.size(); i++) {
            InputFormFieldBehavior behavior = behaviorSet.getBehaviorAtIndex(i);
            modifyURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "modify");
            modifyURL.setParameter(sfModifyBehaviorIDParameter, String.valueOf(behavior.getBehaviorID()));
            deleteURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "delete");
            deleteURL.setParameter(sfDeleteBehaviorIDParameter, String.valueOf(behavior.getBehaviorID()));
            upURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "moveBehaviorInSequence");
            upURL.setParameter(sfMoveBehaviorIDParameter, String.valueOf(behavior.getBehaviorID()));
            upURL.setParameter(sfMoveBehaviorInDirectionParameter, sfMoveBehaviorInDirectionUp);
            downURL.setParameter(URLCreator.fURLParameter_SnapInMethod, "moveBehaviorInSequence");
            downURL.setParameter(sfMoveBehaviorIDParameter, String.valueOf(behavior.getBehaviorID()));
            downURL.setParameter(sfMoveBehaviorInDirectionParameter, sfMoveBehaviorInDirectionDown);
            StyleSheetClassNameEnum rowClass = ((i % 2) > 0) ? StyleSheetClassNameEnum.TableRow_Odd : StyleSheetClassNameEnum.TableRow_Even;
            if (highlightID == behavior.getBehaviorID()) {
                rowClass = StyleSheetClassNameEnum.TableHighlightedRow;
            }
            content.append(HTML.tableRowBeg(HTML.className(rowClass))).append(HTML.tableData(pad + (i + 1) + pad));
            if (i > 0) {
                content.append(HTML.tableData(HTML.anchor(HTML.image(upListRowIcon, "TITLE=\"Move Up\""), upURL.toString()), "align=\"center\""));
            } else {
                content.append(HTML.tableData(HTML.noBreakSpace()));
            }
            if (i < (behaviorSet.size() - 1)) {
                content.append(HTML.tableData(HTML.anchor(HTML.image(downListRowIcon, "TITLE=\"Move Down\""), downURL.toString()), "align=\"center\""));
            } else {
                content.append(HTML.tableData(HTML.noBreakSpace()));
            }
            content.append(HTML.tableData(HTML.anchor(HTML.image(modifyListRowIcon, "TITLE=\"Modify\""), modifyURL.toString()), "align=\"center\"")).append(HTML.tableData(HTML.anchor(HTML.image(deleteListRowIcon, "TITLE=\"Delete\""), deleteURL.toString()), "align=\"center\"")).append(HTML.tableData(pad + behavior.getSourceFieldName() + pad)).append(HTML.tableData(pad + behavior.getOperator().getName() + pad)).append(HTML.tableData(pad + behavior.getSourceFieldValue() + pad)).append(HTML.tableData(pad + behavior.getFieldState().getName() + pad)).append(HTML.tableData(pad + behavior.getTargetFieldName() + pad)).append(HTML.tableRowEnd());
        }
        content.append(HTML.tableEnd());
        return content;
    }

    private StringBuffer renderFieldSelectForTable(String tableName, String fieldName, String selectedValue) throws SnapInException {
        StringBuffer content = new StringBuffer();
        try {
            TableAgent ta = getTableAgent(tableName);
            ObjectAttributeSet oas = ta.getTableAgentObjectAttributes();
            content.append(HTML.formInputSelect(fieldName, oas.getObjectNameListAsValueWithIDPairs(), selectedValue, null));
        } catch (Exception e) {
            throw new SnapInException("Exception", e);
        }
        return content;
    }

    private StringBuffer renderInputFormArea(UserRequestMessage request, String tableName, URLCreator formAction) throws SnapInException {
        StringBuffer content = new StringBuffer();
        try {
            TableAgent fieldBehaviorTA = getTableAgent(DatabaseConstants.TableName_JV_FIELDBEHAVIOR);
            ObjectAttributeSet oas = fieldBehaviorTA.getTableAgentObjectAttributes();
            ObjectAttribute valueOA = oas.getObjectWithName(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE);
            int valueTextInputWidth = valueOA.getFieldWidth();
            Vector actions = new Vector();
            Iterator it = InputFormFieldStateEnum.iterator();
            while (it.hasNext()) {
                org.apache.commons.lang.enums.Enum en = (org.apache.commons.lang.enums.Enum) it.next();
                actions.add(en.getName());
            }
            Vector operators = new Vector();
            it = InputFormRuleOperatorEnum.iterator();
            while (it.hasNext()) {
                org.apache.commons.lang.enums.Enum en = (org.apache.commons.lang.enums.Enum) it.next();
                if (((InputFormRuleOperatorEnum) en).isUserSelectableOperatorOnFieldBehaviorDefinitionTool()) {
                    operators.add(en.getName());
                }
            }
            String behaviorIDString = request.getParameterValue(sfModifyBehaviorIDParameter);
            boolean inModifyMode = StringUtils.isNotBlank(behaviorIDString);
            String sourceField = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_SOURCEFIELD.toLowerCase());
            String targetField = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TARGETFIELD.toLowerCase());
            String value = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE.toLowerCase());
            String controlState = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_CONTROLSTATE.toLowerCase());
            String operator = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_OPERATOR.toLowerCase());
            String orderBy = request.getParameterValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY.toLowerCase());
            if (StringUtils.isBlank(operator)) {
                operator = InputFormRuleOperatorEnum.equals.getName();
            }
            final int numberOfColumnsInInputFormTable = 9;
            StringBuffer buttonPanel = new StringBuffer();
            if (inModifyMode) {
                buttonPanel.append(HTML.formInputSubmit(FORM_PARAM_SUBMIT_BUTTON, "Save Changes")).append(HTML.noBreakSpace()).append(HTML.formInputSubmit(FORM_PARAM_SUBMIT_BUTTON_CANCEL_CHANGES, "Cancel"));
            } else {
                buttonPanel.append(HTML.formInputSubmit(FORM_PARAM_SUBMIT_BUTTON, "Add Rule")).append(HTML.noBreakSpace()).append(HTML.formInputSubmit(FORM_PARAM_SUBMIT_BUTTON_ADD_INVERSE_RULE, "Add with Inverse Rule"));
            }
            buttonPanel.append(HTML.noBreakSpace()).append(HTML.formInputReset());
            content.append(HTML.formBeg(formAction.toString())).append(HTML.tableBeg(HTML.className(StyleSheetClassNameEnum.borderedTable.getName())));
            if (inModifyMode) {
                content.append(HTML.tableRowBeg()).append(HTML.tableData(HTML.H2("Modifying Rule") + HTML.formInputHidden(sfHighlightRowWithBehaviorIDParameter, behaviorIDString) + " " + HTML.formInputHidden(sfModifyBehaviorIDParameter, behaviorIDString) + " " + HTML.formInputHidden(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY, orderBy), "ColSpan=\"" + numberOfColumnsInInputFormTable + "\"")).append(HTML.tableRowEnd());
            }
            content.append(HTML.tableRowBeg()).append(HTML.tableData(HTML.escapeWhitespace("When field "))).append(HTML.tableData(renderFieldSelectForTable(tableName, DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_SOURCEFIELD, sourceField).toString())).append(HTML.tableData(HTML.formInputRadioButton(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_OPERATOR, operators, operator, null))).append(HTML.tableData(HTML.formInputTextWithDefaultValue(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_VALUE, 60, 20, value, null))).append(HTML.tableData(HTML.noBreakSpace())).append(HTML.tableData(HTML.formInputRadioButton(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_CONTROLSTATE, actions, controlState, null))).append(HTML.tableData(HTML.noBreakSpace())).append(HTML.tableData(renderFieldSelectForTable(tableName, DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TARGETFIELD, targetField).toString())).append(HTML.tableRowEnd()).append(HTML.tableRowBeg()).append(HTML.tableData(buttonPanel.toString(), "Align=\"center\" ColSpan=\"" + numberOfColumnsInInputFormTable + "\"")).append(HTML.tableRowEnd()).append(HTML.tableEnd());
        } catch (Exception e) {
            throw new SnapInException("Exception", e);
        }
        return content;
    }

    private int renumberOrderBy(String tableName) throws SnapInException {
        long tableID = 0L;
        try {
            tableID = applicationFacilitiesBean.getTableID(tableName);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return renumberOrderBy(tableID);
    }

    /**
     *  This is basically copied behavior from the TableAgent.  It should be
     *  adapted to handle any table with a sequence (order by) column and placed
     *  into the PersistenceFacilities.  For now, I'm trying to get jVantage
     *  out the door.  This will do.
     */
    private int renumberOrderBy(long tableID) throws SnapInException {
        int count = 0;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getDataSource().getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            StringBuffer query = new StringBuffer();
            query.append("SELECT ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID).append(" FROM ").append(DatabaseConstants.TableName_JV_FIELDBEHAVIOR).append(" WHERE ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TABLEID).append(" = ").append(tableID).append(" ORDER BY ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY);
            Vector rowIDVector = new Vector();
            rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                count++;
                rowIDVector.add(rs.getLong(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID) + "");
            }
            StringBuffer updateString = new StringBuffer();
            updateString.append("UPDATE ").append(DatabaseConstants.TableName_JV_FIELDBEHAVIOR).append(" SET ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY).append(" = ? WHERE ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID).append(" = ?");
            PreparedStatement pstmt = con.prepareStatement(updateString.toString());
            int orderByValue = ORDERBY_BY_DELTA_VALUE;
            Enumeration en = rowIDVector.elements();
            while (en.hasMoreElements()) {
                pstmt.setInt(1, orderByValue);
                pstmt.setString(2, en.nextElement().toString());
                orderByValue += ORDERBY_BY_DELTA_VALUE;
                pstmt.executeUpdate();
            }
            con.setAutoCommit(true);
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (java.sql.SQLException e) {
            if (con == null) {
                logger.error("java.sql.SQLException", e);
            } else {
                try {
                    logger.error("Transaction is being rolled back.");
                    con.rollback();
                    con.setAutoCommit(true);
                } catch (java.sql.SQLException e2) {
                    logger.error("java.sql.SQLException", e2);
                }
            }
        } catch (Exception e) {
            logger.error("Error occured during RenumberOrderBy", e);
        } finally {
            getDataSourceHelper().releaseResources(con, stmt, rs);
        }
        return count;
    }
}
