package com.jvantage.ce.logging;

public class LogConstants {

    public static final String sfTestCaseLoggerFileNamePrefix = "Test_";

    public static final String sfLoggerFileNameSuffix = ".log";

    public static final String sfRootLoggerName = "com.jvantage";

    public static final String sfLoggerName_ACL = sfRootLoggerName + ".ACL";

    public static final String sfLoggerName_AddRecord = sfRootLoggerName + ".AddRecord";

    public static final String sfLoggerName_DataSource = sfRootLoggerName + ".DataSource";

    public static final String sfLoggerName_DeleteRecord = sfRootLoggerName + ".DeleteRecord";

    public static final String sfLoggerName_JSPLibrary = sfRootLoggerName + ".JSPLibrary";

    public static final String sfLoggerName_Login = sfRootLoggerName + ".Login";

    public static final String sfLoggerName_ModifyRecord = sfRootLoggerName + ".ModifyRecord";

    public static final String sfLoggerName_Persistence = sfRootLoggerName + ".Persistence";

    public static final String sfLoggerName_Presentation = sfRootLoggerName + ".Presentation";

    public static final String sfLoggerName_Property = sfRootLoggerName + ".Property";

    public static final String sfLoggerName_Search = sfRootLoggerName + ".Search";

    public static final String sfLoggerName_Servlet_Main = sfRootLoggerName + ".Servlet_Main";

    public static final String sfLoggerName_SessionStore = sfRootLoggerName + ".SessionStore";

    public static final String sfLoggerName_Setup = sfRootLoggerName + ".Setup";

    public static final String sfLoggerName_SnapIn = sfRootLoggerName + ".SnapIn";

    public static final String sfLoggerName_Velocity = sfRootLoggerName + ".Velocity";

    public static final String sfLoggerName_LogHelper = sfRootLoggerName + ".LogHelper";

    public static final String sfLoggerName_ApplicationFacilities = sfRootLoggerName + ".ApplicationFacilities";

    public static final String sfLoggerName_CalendarFacilities = sfRootLoggerName + ".CalendarFacilities";

    public static final String sfLoggerName_ListFacilities = sfRootLoggerName + ".ListFacilities";

    public static final String sfLoggerName_PersistenceFacilities = sfRootLoggerName + ".PersistenceFacilities";

    public static final String sfLoggerName_PropertyFacilities = sfRootLoggerName + ".PropertyFacilities";

    public static final String sfLoggerName_RelationshipFacilities = sfRootLoggerName + ".RelationshipFacilities";

    public static final String sfLoggerName_SessionFacilities = sfRootLoggerName + ".SessionFacilities";

    public static final String sfLoggerName_SystemFacilities = sfRootLoggerName + ".SystemFacilities";

    public static final String sfLoggerName_TriggerFacilities = sfRootLoggerName + ".TriggerFacilities";

    public static final String sfLoggerName_ViewInstanceFacilities = sfRootLoggerName + ".ViewInstanceFacilities";

    public static final String sfLoggerName_RootContextFacilities = sfRootLoggerName + ".RootContextFacilities";

    public static final String sfLoggerName_Test = sfRootLoggerName + ".Test";

    public static final String[] sfAllLoggerNames = { sfLoggerName_ACL, sfLoggerName_AddRecord, sfLoggerName_ApplicationFacilities, sfLoggerName_LogHelper, sfLoggerName_CalendarFacilities, sfLoggerName_DataSource, sfLoggerName_DeleteRecord, sfLoggerName_ListFacilities, sfLoggerName_Login, sfLoggerName_ModifyRecord, sfLoggerName_Persistence, sfLoggerName_PersistenceFacilities, sfLoggerName_Presentation, sfLoggerName_Property, sfLoggerName_PropertyFacilities, sfLoggerName_RelationshipFacilities, sfLoggerName_Search, sfLoggerName_Servlet_Main, sfLoggerName_SessionFacilities, sfLoggerName_SessionStore, sfLoggerName_Setup, sfLoggerName_SnapIn, sfLoggerName_SystemFacilities, sfLoggerName_TriggerFacilities, sfLoggerName_Velocity, sfLoggerName_RootContextFacilities, sfLoggerName_Test };

    public static final String sfDefaultPattern = "%d{yyyy/MM/d HH:mm:ss} %-5p: %m%n";

    /**
     *  The name of the logger configuration file.
     */
    public static final String sfLoggerPropertyFileName = "jVantageLog.properties";

    /** Creates a new instance of LogConstants */
    public LogConstants() {
    }
}
