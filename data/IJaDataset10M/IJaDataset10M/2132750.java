package net.sourceforge.pmd.ui;

/**
 * This interface groups all plugin constants
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2006/05/22 21:23:58  phherlin
 * Refactor the plug-in architecture to better support future evolutions
 *
 * Revision 1.12  2006/05/07 12:01:50  phherlin
 * Add the possibility to use the PMD violation review style
 *
 * Revision 1.11  2006/05/02 20:10:49  phherlin
 * Limit the number of reported violations per file and per rule
 *
 * Revision 1.10  2006/05/02 18:49:51  phherlin
 * Remove dead code
 *
 * Revision 1.9  2006/04/24 21:18:11  phherlin
 * Rulesets list are now managed in the core plugin
 *
 * Revision 1.8  2006/04/11 21:00:58  phherlin
 * Add new VBHTML report
 *
 * Revision 1.7  2005/10/24 22:36:42  phherlin
 * Integrating Sebastian Raffel's work
 *
 * Revision 1.6  2005/05/31 23:04:10  phherlin
 * Fix Bug 1190624: refactor CPD integration
 *
 * Revision 1.5  2005/05/31 20:33:02  phherlin
 * Continuing refactoring
 *
 * Revision 1.4  2005/04/20 23:15:32  phherlin
 * Implement reports generation RFE#1177802
 *
 * Revision 1.3  2005/01/31 23:39:37  phherlin
 * Upgrading to PMD 2.2
 *
 * Revision 1.2  2005/01/16 22:52:17  phherlin
 * Upgrade to PMD 2.1: add new packaged rulesets
 *
 * Revision 1.1  2004/06/29 22:00:30  phherlin
 * Adapting the plugin to the new OSGi standards
 *
 */
public class PMDUiConstants {

    public static final String PLUGIN_ID = "net.sourceforge.pmd.ui";

    public static final String RULESET_PREFERENCE = PLUGIN_ID + ".ruleset";

    public static final String RULESET_DEFAULT = "";

    public static final String LIST_DELIMITER = ";";

    public static final String ICON_ERROR = "icons/error.gif";

    public static final String ICON_WARN = "icons/warn.gif";

    public static final String ICON_INFO = "icons/info.gif";

    public static final String ICON_FILE = "icons/file.gif";

    public static final String ICON_PRIO1 = "icons/prio_1.gif";

    public static final String ICON_PRIO2 = "icons/prio_2.gif";

    public static final String ICON_PRIO3 = "icons/prio_3.gif";

    public static final String ICON_PRIO4 = "icons/prio_4.gif";

    public static final String ICON_PRIO5 = "icons/prio_5.gif";

    public static final String ICON_REMVIO = "icons/remvio.gif";

    public static final String ICON_LABEL_ERROR = "icons/lab_error.gif";

    public static final String ICON_LABEL_WARN = "icons/lab_warn.gif";

    public static final String ICON_LABEL_INFO = "icons/lab_info.gif";

    public static final String ICON_LABEL_ERR1 = "icons/lab_err1.gif";

    public static final String ICON_LABEL_ERR2 = "icons/lab_err2.gif";

    public static final String ICON_LABEL_ERR3 = "icons/lab_err3.gif";

    public static final String ICON_LABEL_ERR4 = "icons/lab_err4.gif";

    public static final String ICON_LABEL_ERR5 = "icons/lab_err5.gif";

    public static final String ICON_LABEL_ERR_DFA = "icons/lab_errdfa.gif";

    public static final String ICON_LABEL_ARRUP = "icons/lab_arrup.gif";

    public static final String ICON_LABEL_ARRDN = "icons/lab_arrdn.gif";

    public static final String ICON_PROJECT = "icons/obj_project.gif";

    public static final String ICON_PACKAGE = "icons/obj_package.gif";

    public static final String ICON_JAVACU = "icons/obj_javacu.gif";

    public static final String ICON_BUTTON_PRIO1 = "icons/btn_prio1.gif";

    public static final String ICON_BUTTON_PRIO2 = "icons/btn_prio2.gif";

    public static final String ICON_BUTTON_PRIO3 = "icons/btn_prio3.gif";

    public static final String ICON_BUTTON_PRIO4 = "icons/btn_prio4.gif";

    public static final String ICON_BUTTON_PRIO5 = "icons/btn_prio5.gif";

    public static final String ICON_BUTTON_PACKFILES = "icons/btn_packfiles.gif";

    public static final String ICON_BUTTON_FILES = "icons/btn_files.gif";

    public static final String ICON_BUTTON_COLLAPSE = "icons/btn_collapse.gif";

    public static final String ICON_BUTTON_REMVIO = "icons/btn_remvio.gif";

    public static final String ICON_BUTTON_QUICKFIX = "icons/btn_quickfix.gif";

    public static final String ICON_BUTTON_REVIEW = "icons/btn_review.gif";

    public static final String ID_PERSPECTIVE = PLUGIN_ID + ".views.pmdPerspective";

    public static final String ID_OUTLINE = PLUGIN_ID + ".views.violationOutline";

    public static final String ID_OVERVIEW = PLUGIN_ID + ".views.violationOverview";

    public static final String ID_DATAFLOWVIEW = PLUGIN_ID + ".views.dataflowView";

    public static final String MEMENTO_OUTLINE_FILE = "/violationOutline_memento.xml";

    public static final String MEMENTO_OVERVIEW_FILE = "/violationOverview_memento.xml";

    public static final String KEY_MARKERATT_RULENAME = "rulename";

    public static final String KEY_MARKERATT_PRIORITY = "pmd_priority";

    public static final String KEY_MARKERATT_LINE2 = "line2";

    public static final String KEY_MARKERATT_VARIABLE = "variable";

    public static final String SETTINGS_VIEW_FILE_SELECTION = "view.file_selection";

    public static final String SETTINGS_VIEW_PROJECT_SELECTION = "view.project_selection";

    public static final String SETTINGS_VIEW_ERRORHIGH_FILTER = "view.errorhigh_filter";

    public static final String SETTINGS_VIEW_ERROR_FILTER = "view.high_filter";

    public static final String SETTINGS_VIEW_WARNINGHIGH_FILTER = "view.warninghigh_filter";

    public static final String SETTINGS_VIEW_WARNING_FILTER = "view.warning_filter";

    public static final String SETTINGS_VIEW_INFORMATION_FILTER = "view.information_filter";

    public static final String REPORT_FOLDER = "reports";

    public static final String HTML_REPORT_NAME = "pmd-report.html";

    public static final String VBHTML_REPORT_NAME = "pmd-report.vb.html";

    public static final String CSV_REPORT_NAME = "pmd-report.csv";

    public static final String XML_REPORT_NAME = "pmd-report.xml";

    public static final String TXT_REPORT_NAME = "pmd-report.txt";

    public static final String SIMPLE_CPDREPORT_NAME = "cpd-report.txt";

    /**
     * This class is not meant to be instanciated
     *
     */
    private PMDUiConstants() {
        super();
    }
}
