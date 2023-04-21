package com.jvantage.ce.common;

import org.apache.commons.lang.*;

public class Constants {

    public static final String sfJVantageDeveloperApplicationName = "jVantage";

    public static final String sfJVantageServletAlias = "controller".toLowerCase();

    public static final String sfUploadFileViewerServletAlias = "ufv";

    public static final String sfJVantageLicenseFileName = "jvantage.lic";

    public static final String sfSystemConfigPropertiesFileName = "jvantage.cfg";

    public static final String sfSystemConfigProperties_DefaultDatasource = "default.datasource";

    public static final String sfConfigurationSubDirectory_Logs = "logs";

    public static final String sfConfigurationSubDirectory_Email = "email-templates";

    public static final String sfConfigurationSubDirectory_XML = "xml";

    public static final String sfConfigurationSubDirectory_JavaBean = "generated-beans";

    public static final String sfConfigurationSubDirectory_VelocityMergeTemplate = "merge-templates";

    public static final String sfConfigurationSubDirectory_Upload = "upload";

    public static final String sfConfigurationSubDirectory_DatabaseExport = "db-export";

    public static final String sfConfigurationSubDirectory_VelocityTemplate = "code-gen-templates";

    public static final int sfDatabaseSingletonRecordNumber = 1;

    public static final String sfDefaultSitePage = sfJVantageServletAlias + "?" + URLCreator.fURLParameter_Table + "=" + DatabaseConstants.TableName_JV_ROOTCONTEXT + "&" + URLCreator.fURLParameter_JoinTable + "=" + DatabaseConstants.TableName_JV_APPLICATIONS + "&" + URLCreator.fURLParameter_List + "=" + DatabaseConstants.TableName_JV_APPLICATIONS + "Default";

    public static final String sfDefaultDataSourceJNDIName = "jVantageDefaultDS";

    public static final String sfDefaultDataSource = "jdbc/jVantageDS";

    public static final String sfDefaultUIThemeName = "corporate";

    public static final String sfUnrecoverableErrorHtmlPage = "unrecoverableerror.html";

    public static final int sfDefaultScrollListPageSize = 7;

    public static final String sfDefaultFormValidationSnapInMethod = "validate";

    public static final String sfGuestLoginName = "guest";

    public static final String sfGuestGroupName = "Guest";

    public static final String sfGuestFullName = "Guest";

    public static final String sfAdmin_RootAdminLoginName = "admin";

    public static final String sfAdminGroupName = "Admin";

    public static final String sfIndent = "    ";

    public static final String sfIndent1 = sfIndent;

    public static final String sfIndent2 = sfIndent1 + sfIndent1;

    public static final String sfIndent3 = sfIndent2 + sfIndent1;

    public static final String sfIndent4 = sfIndent2 + sfIndent2;

    public static final String sfIndent5 = sfIndent4 + sfIndent1;

    public static final String sfIndent6 = sfIndent4 + sfIndent2;

    public static final String sfIndent7 = sfIndent4 + sfIndent3;

    public static final String sfIndent8 = sfIndent4 + sfIndent4;

    public static final String sfDefaultPrimaryKeyName = "ID";

    public static final String sfDefaultApplicationHomeURL = "${DefaultHomeURL}";

    public static final String sfPerformanceMeasuresLogIndicator = "(T) ";

    public static final String sfUnregisteredGuestFullName = "Unregistered Guest";

    public static final long sfMaxCLOBSize = 4000;

    public static final String sfSystemVariablePrefix = "${";

    public static final String sfSystemVariableSuffix = "}";

    public static final String sfSystemVariable_ApplicationName = "application-name";

    public static final String sfSystemVariable_Date = "date";

    public static final String sfSystemVariable_DateTime = "datetime";

    public static final String sfSystemVariable_ID = sfDefaultPrimaryKeyName;

    public static final String sfSystemVariable_TableName = "tableName";

    public static final String sfSystemVariable_UserID = "user-id";

    public static final String sfSystemVariable_UserName = "user-name";

    public static final String sfSystemVariable_UserGroupName = "user-group-name";

    public static final String sfSystemVariable_WebContextRoot = "web-context-root";

    public static final String sfSystemVariable_WebHostName = "web-host-name";

    public static final String sfSystemVariable_jVantageHomeURL = "jvantage-home-url";

    public static final String sfSystemVariable_FieldInputValue = "field-input-value";

    public static final String sfSystemVariable_DatabaseReservedWords = "database-reserved-words";

    public static final String sfSystemVariable_TableColumnNames = "table-column-names";

    public static final String sfSystemVariable_TableNamesInCurrentApplication = "table-names-in-current-application";

    public static final String sfSystemVariable_AllExistingTableNames = "all-existing-table-names";

    public static final String sfSystemVariable_LocalElementName_Singular = "local-element-name-singular";

    public static final String sfSystemVariable_LocalElementName_Plural = "local-element-name-plural";

    public static final String sfSystemVariable_RemoteElementName_Singular = "remote-element-name-singular";

    public static final String sfSystemVariable_RemoteElementName_Plural = "remote-element-name-plural";

    public static final String[] sfSystemVariables = { sfSystemVariable_ApplicationName, sfSystemVariable_Date, sfSystemVariable_DateTime, sfSystemVariable_TableName, sfSystemVariable_UserID, sfSystemVariable_UserName, sfSystemVariable_UserGroupName, sfSystemVariable_FieldInputValue, sfSystemVariable_DatabaseReservedWords, sfSystemVariable_TableColumnNames, sfSystemVariable_TableNamesInCurrentApplication, sfSystemVariable_AllExistingTableNames, sfSystemVariable_LocalElementName_Singular, sfSystemVariable_LocalElementName_Plural, sfSystemVariable_RemoteElementName_Singular, sfSystemVariable_RemoteElementName_Plural };

    public static final String sfApplicationVariablePrefix = "${";

    public static final String sfApplicationVariablePrefix_RegEx = "\\$\\{";

    public static final String sfApplicationVariableSuffix = "}";

    public static final String sfApplicationVariableSuffix_RegEx = "\\}";

    public static final String LS = System.getProperty("line.separator");

    public static final String sfTrue = "true";

    public static final String sfFalse = "false";

    public static final String sfEmptyString = "";

    public static final String sfYes = "Yes";

    public static final String sfNo = "No";

    public static final String sfNotApplicable = "NA";

    public static final String sfSortAscending = "Asc";

    public static final String sfSortDescending = "Desc";

    public static final int sfCurrencyNumberScale = 2;

    public static final String sfTokenDelimiter = "::";

    public static final String sfDefaultDateFormat = "MMMM d, yyyy";

    public static final String sfDefaultDateAndTimeFormat = "MM/d/yyyy hh:mm a z";

    public static final long sfOneMinuteInMilliseconds = 60 * 1000;

    public static final long sfOneHourInMilliseconds = sfOneMinuteInMilliseconds * 60;

    public static final long sfOneDayInMilliseconds = sfOneHourInMilliseconds * 24;

    public static final String sfScrollListNavigation_FirstPage = "firstPage";

    public static final String sfScrollListNavigation_PreviousPage_Jump = "previousPageJump";

    public static final String sfScrollListNavigation_PreviousPage = "previousPage";

    public static final String sfScrollListNavigation_MiddlePage = "middlePage";

    public static final String sfScrollListNavigation_NextPage = "nextPage";

    public static final String sfScrollListNavigation_NextPage_Jump = "nextPageJump";

    public static final String sfScrollListNavigation_LastPage = "lastPage";

    public static final String sfNamingContext_jVantage = "jVantage";

    public static final String sfNamingContext_TableAgents = "TableAgents";

    public static final String sfNamingContext_Sessions = "Sessions";

    public static final String sfSessionKey_Prefix = "jvSK_";

    public static final String sfSessionKey_OASValidation = sfSessionKey_Prefix + "ValidateOAS";

    public static final String sfSessionKey_RegularExpressionTester = sfSessionKey_Prefix + "RegEx";

    public static final String sfSessionKey_TransactionNumber = sfSessionKey_Prefix + "TransactionNumber";

    public static final String sfSessionKey_PanelDisplayer = sfSessionKey_Prefix + "PanelDisplayer";

    public static final String sfSessionKey_IsLoggedIn = sfSessionKey_Prefix + "IsLoggedIn";

    public static final String sfSessionKey_LoggedInPrincipalLoginName = sfSessionKey_Prefix + "LoggedInPrincipalLoginName";

    public static final String sfSessionKey_LoggedInPrincipalFullName = sfSessionKey_Prefix + "LoggedInPrincipalFullName";

    public static final String sfSessionKey_LoggedInPrincipalGroupName = sfSessionKey_Prefix + "LoggedInPrincipalGroupName";

    public static final String sfSessionKey_CurrentApplicationName = sfSessionKey_Prefix + "CurrentApp";

    public static final String sfSessionKey_PreviousUserRequest = sfSessionKey_Prefix + "PreviousUserRequest";

    public static final String sfSessionKey_UserResponse = sfSessionKey_Prefix + "UserResponse";

    public static final String sfSessionKey_LoginUsurpedRequest = sfSessionKey_Prefix + "LoginUsurpedRequest";

    public static final String sfSessionKey_LoginErrorMessage = sfSessionKey_Prefix + "LoginErrorMessage";

    public static final String sfSessionKey_RedirectRequest = sfSessionKey_Prefix + "RedirectRequest";

    public static final String sfSessionKey_ObjectAttributeSet = sfSessionKey_Prefix + "CurrentOAS";

    public static final String sfSessionKey_ManyToManyScrollListID = sfSessionKey_Prefix + "M2MScrollListID";

    public static final String sfSessionKey_URLHistory = sfSessionKey_Prefix + "URLHistory";

    public static final String sfSessionKey_ScrollableListManagerBuffer = sfSessionKey_Prefix + "SLMB";

    public static final String sfSessionKey_UserRequestMessageQueue = sfSessionKey_Prefix + "URMQ";

    public static final String sfSessionKey_DevelopmentMode = sfSessionKey_Prefix + "IsDevMode";

    public static final String sfSessionKey_SearchString = sfSessionKey_Prefix + "SearchString";

    public static final String sfSessionKey_DevelopmentModeReturnURL = sfSessionKey_Prefix + "DevModeURL";

    public static final String sfSystemSessionKey_ServletRequestLocalPort = sfSessionKey_Prefix + "SRLP";

    public static final String sfSystemSessionKey_ServletRequestLocalHost = sfSessionKey_Prefix + "SRLH";

    public static final String sfSystemSessionKey_SelectionListBuilder = sfSessionKey_Prefix + "SelListBuilder";

    public static final String sfSystemSessionKey_FormValidationTagValues = sfSessionKey_Prefix + "FormTags";

    public static final String[] sfJVantageSystemApplications = { "jVantage", "PropertyService", "ACL" };

    public static final String sfFieldType_AutoAddRecordUserSign = "autoAddRecordUserSign";

    public static final String sfFieldType_AutoAddRecordTimeStamp = "autoAddRecordTimeStamp";

    public static final String sfFieldType_AutoModifyRecordUserSign = "autoModifyRecordUserSign";

    public static final String sfFieldType_AutoModifyRecordTimeStamp = "autoModifyRecordTimeStamp";

    public static final String sfFieldType_AutoRecordOwner = "autoRecordOwner";

    public static final String sfPageContext_BookmarkDelimiter = "-";

    public static final String sfPageContext_BookmarkType_Input = "I";

    public static final String sfPageContext_BookmarkType_DisplayLabel = "L";

    public static final String sfPageContext_BookmarkType_Value = "V";

    public static final String sfPageContext_BookmarkOmitStyleSuffix = "-os";

    public static final String sfTrigger_AddMenuOption = "AddMenuOption";

    public static final String sfTrigger_AllowAnonymoususer = "AllowAnonymoususer";

    public static final String sfTrigger_AppendURLParameter = "AppendURLParameter";

    public static final String sfTrigger_InvokeSnapIn = "InvokeSnapIn";

    public static final String sfTrigger_InvokeSnapIn_GenUI_PassPageContext = "InvokeSnapIn_GenUI_PassPageContext";

    public static final String sfTrigger_InvokeSnapIn_GenUI_PassSessionID = "InvokeSnapIn_GenUI_PassSessionID";

    public static final String sfTrigger_InvokeSnapIn_GenUI_PassUserLoginID = "InvokeSnapIn_GenUI_PassUserLoginID";

    public static final String sfTrigger_InvokeSnapIn_GenUI_PassUserLoginIDAndSessionID = "InvokeSnapIn_GenUI_PassUserLoginIDAndSessionID";

    public static final String sfTrigger_InvokeSnapIn_PassPageContext = "InvokeSnapIn_GenUI_PassPageContext";

    public static final String sfTrigger_InvokeSnapIn_PassSessionID = "InvokeSnapIn_GenUI_PassSessionID";

    public static final String sfTrigger_InvokeSnapIn_PassUserLoginID = "InvokeSnapIn_GenUI_PassUserLoginID";

    public static final String sfTrigger_InvokeSnapIn_PassUserLoginIDAndSessionID = "InvokeSnapIn_GenUI_PassUserLoginIDAndSessionID";

    public static final String sfTrigger_GoToURL = "GoToURL";

    public static final String sfTrigger_GoToURL_AfterAddRecordSubmit = "GoToURL_AfterAddRecordSubmit";

    public static final String sfTrigger_GoToURL_AfterDeleteRecordSubmit = "GoToURL_AfterDeleteRecordSubmit";

    public static final String sfTrigger_GoToURL_AfterModifyRecordSubmit = "GoToURL_AfterModifyRecordSubmit";

    public static final String sfTrigger_HelpURL_DocumentLevel = "HelpURL-DocumentLevel";

    public static final String sfTrigger_HelpURL_FieldLevel = "HelpURL-FieldLevel";

    public static final String sfTrigger_OmitDate = "OmitDate";

    public static final String sfTrigger_OmitKeywordSearchPanel = "OmitKeywordSearchPanel";

    public static final String sfTrigger_OmitMenu = "OmitMenu";

    public static final String sfTrigger_OmitMenuOption = "SuppressMenuOption";

    public static final String sfTrigger_OmitTitle = "OmitTitle";

    public static final String sfTrigger_OneToManyDefaultListName = "OneToManyDefaultListName";

    public static final String sfTrigger_ManyToManyDefaultListName = "ManyToManyDefaultListName";

    public static final String sfTrigger_RenameMenuOption = "RenameMenuOption";

    public static final String sfTrigger_SetListName = "SetListName";

    public static final String sfTrigger_SetTemplate = "SetTemplate";

    public static final String sfTrigger_SetTemplateOnID = "SetTemplateOnID";

    public static final String sfTrigger_SetListTable = "SetListTable";

    public static final String sfTrigger_AutoHome = "AutoHome";

    public static final String sfTrigger_SetTableName = "SetTableName";

    public static final String sfTrigger_SetTitle = "SetTitle";

    public static final String sfTrigger_SetURLParameter = "SetURLParameter";

    public static final String sfTrigger_SpawnNewWindow = "SpawnNewWindow";

    public static final String sfUIVariable_LoginAreaBegin = "login-area-begin";

    public static final String sfUIVariable_LoginAreaEnd = "login-area-end";

    public static final String sfUIVariable_UserIDInput = "user-id-input";

    public static final String sfUIVariable_PasswordInput = "password-input";

    public static final String sfUIVariable_LoginSubmitButton = "login-submit-button";

    public static final String sfResizeSuffix = "RESIZE";

    public static final String sfNoResizeImage = Constants.sfTokenDelimiter + "NO_" + sfResizeSuffix;

    public static final String sfNoListResizeImage = Constants.sfTokenDelimiter + "NO_LIST_" + sfResizeSuffix;

    public static final String sfNoViewResizeImage = Constants.sfTokenDelimiter + "NO_VIEW_" + sfResizeSuffix;

    /** Creates new Constants */
    private Constants() {
    }

    public static boolean isAutoPopulateField(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        return fieldName.equalsIgnoreCase(sfFieldType_AutoAddRecordUserSign) || fieldName.equalsIgnoreCase(sfFieldType_AutoAddRecordTimeStamp) || fieldName.equalsIgnoreCase(sfFieldType_AutoModifyRecordUserSign) || fieldName.equalsIgnoreCase(sfFieldType_AutoModifyRecordTimeStamp) || fieldName.equalsIgnoreCase(sfFieldType_AutoRecordOwner);
    }

    public static final boolean isJVantageSystemApplication(String appName) {
        if (StringUtils.isBlank(appName)) {
            return false;
        }
        for (int i = 0; i < Constants.sfJVantageSystemApplications.length; i++) {
            if (appName.equalsIgnoreCase(Constants.sfJVantageSystemApplications[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Given a variable name, such as "MyVariable" this method will return
     *  the String "${MyVariable}".  Assuming that the Prefix and Suffix
     *  are "${" and "}" respectively.  The actual Prefix and Suffix is
     *  determined using the Constants, sfSystemVariablePrefix and
     *  sfSystemVariableSuffix.
     */
    public static String systemVariableWithPrefixAndSuffix(String variable) {
        if (variable == null) {
            return null;
        }
        return sfSystemVariablePrefix + variable + sfSystemVariableSuffix;
    }
}
