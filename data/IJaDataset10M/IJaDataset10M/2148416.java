package net.mogray.infinitypfm.core.conf;

import java.io.File;
import org.eclipse.swt.SWT;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MM {

    public static final String APPTITLE = "Infinity PFM";

    public static final String APPVERSION = "0.3.5";

    public static final String APPLINK = "http://infinitypfm.sourceforge.net";

    public static final String APPLICENCE = "GNU General Public License";

    public static final String APPCOPYRIGHT = "(c) 2005-2010 by Wayne Gray";

    public static final String APPPATH = System.getProperty("INFINITYPFM_HOME") + File.separator;

    public static final int ROW_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;

    public static final String IMG_ADD = "calc_32x32.png";

    public static final String IMG_ARROW_DOWN = "arrowdown.gif";

    public static final String IMG_ARROW_UP = "arrowup.gif";

    public static final String IMG_CLEAR = "eraser.png";

    public static final String IMG_CLOCK = "clock_32x32.png";

    public static final String IMG_CLOSE = "fileclose.png";

    public static final String IMG_CONFIGTOPIC = "tools.gif";

    public static final String IMG_CONNECTED = "conn.gif";

    public static final String IMG_EDITSERVER = "configure.png";

    public static final String IMG_HELP = "hwinfo.png";

    public static final String IMG_HELPBACK = "arrowleft.gif";

    public static final String IMG_HELPFORWARD = "arrowright.gif";

    public static final String IMG_JMSITEM = "conn.gif";

    public static final String IMG_LOGASSOC = "log.gif";

    public static final String IMG_LOGTREE = "document.png";

    public static final String IMG_NEWSERVER = "credit_32x32.png";

    public static final String IMG_NEWSERVERSMALL = "blockdevice.png";

    public static final String IMG_NOTCONNECTED = "noconn.gif";

    public static final String IMG_OPTIONS = "tools.gif";

    public static final String IMG_PLUGINCLOSE = "tree_mode.gif";

    public static final String IMG_QUE = "circle.gif";

    public static final String IMG_QUEZEN_BANNER = "infinitylogo1.jpg";

    public static final String IMG_QUEZEN_BANNER_SMALL = "infinitylogo1_small.jpg";

    public static final String IMG_QUEZEN_ICON = "eye.gif";

    public static final String IMG_REFRESH = "reload.png";

    public static final String IMG_REMOVEQUEUE = "remove.png";

    public static final String IMG_SAVESERVER = "3floppy_mount.png";

    public static final String IMG_SELECTALL = "ok.png";

    public static final String IMG_TOPIC = "circle.gif";

    public static final String IMG_TESTMSG = "mail_forward.png";

    public static final String IMG_TREELEAF = "tree_leaf_32x32.png";

    public static final String IMG_DOLLAR = "dollar2_32x32.png";

    public static final String ACT_TYPE_EXPENSE = "Expense";

    public static final String ACT_TYPE_LIABILITY = "Liability";

    public static final int YES = 0;

    public static final int NO = 1;

    public static final int OK = 2;

    public static final int CANCEL = 3;

    public static final int THIS_MONTH = 4;

    public static final int LAST_MONTH = 5;

    public static final int RETURNTYPE_SUCCESS = 0;

    public static final int RETURNTYPE_FAIL = -1;

    public static final int DIALOG_QUESTION = 0;

    public static final int DIALOG_INFO = 1;

    public static final int NODETYPE_LOG = 0;

    public static final int NODETYPE_LOG_ROOT = 1;

    public static final int NODETYPE_BANK_ACCOUNT = 2;

    public static final int NODETYPE_EXPENSE_ACCOUNT = 3;

    public static final int NODETYPE_INCOME_ACCOUNT = 4;

    public static final int NODETYPE_ROOT = 5;

    public static final int MENU_DEFAULT = -1;

    public static final int MENU_VIEW_CONSOLE = 10;

    public static final int MENU_VIEW_TRANS_ENTRY = 11;

    public static final int MENU_TREE_REFRESH = 20;

    public static final int MENU_TREE_ACT_REMOVE = 21;

    public static final int MENU_TREE_CLOSEVIEW = 22;

    public static final int MENU_TREE_ACT_ADD = 23;

    public static final int MENU_TREE_LOAD_REGISTER = 24;

    public static final int MENU_TREE_ADD_ACT_BUDGET = 25;

    public static final int MENU_TREE_ADD_ACT_FROM_TEMP = 26;

    public static final int MENU_TREE_REM_ACT_BUDGET = 27;

    public static final int MENU_FILE_EXIT = 30;

    public static final int MENU_FILE_SAVE = 31;

    public static final int MENU_FILE_IMPORT_OFX = 32;

    public static final int MENU_FILE_IMPORT_QFX = 33;

    public static final int MENU_FILE_IMPORT_QIF = 34;

    public static final int MENU_FILE_ADD_ACCOUNT = 35;

    public static final int MENU_FILE_ADD_BUDGET = 36;

    public static final int MENU_REPORT_EXECUTE = 40;

    public static final int MENU_REPORT_SAVE = 41;

    public static final int MENU_QUEUE_REMOVE = 42;

    public static final int MENU_QUEUE_SELECTALL = 43;

    public static final int MENU_QUEUE_LOADTESTER = 44;

    public static final int MENU_CONSOLE_CLEAR = 50;

    public static final int MENU_CONSOLE_CLOSE = 51;

    public static final int MENU_TOPIC_ADD = 60;

    public static final int MENU_TOPIC_SELECTALL = 61;

    public static final int MENU_TOPIC_CONFIG = 62;

    public static final int MENU_SERVER_NEW = 70;

    public static final int MENU_SERVER_EDIT = 71;

    public static final int MENU_SERVER_REMOVE = 72;

    public static final int MENU_OPTIONS_CONFIG = 80;

    public static final int MENU_HELP_CONTENTS = 90;

    public static final int MENU_HELP_ABOUT = 91;

    public static final int MENU_REPORTS_MONTHLY_BALANCE = 100;

    public static final int MENU_REPORTS_PRIOR_MONTHLY_BALANCE = 101;

    public static final int MENU_REPORTS_ACCOUNT_HISTORY = 102;

    public static final int MENU_BUDGET_SAVE = 120;

    public static final int TAB_SERVER_AUTH = 16;

    public static final int TAB_SERVER_LOGS = 17;

    public static final int TAB_SERVER_TOPICS = 18;

    public static final int VIEW_SCRATCH = 401;

    public static final int VIEW_DEFAULT = 402;

    public static final int VIEW_REGISTER = 403;

    public static final int VIEW_LOG = 404;

    public static final int VIEW_BUDGET = 405;

    public static final int VIEW_REPORT = 406;

    public static final int VIEW_RECURRENCE = 407;

    public static final int QS_REFRESH_QUEUE = 0;

    public static final int QS_REFRESH_TOPIC = 1;

    public static final int QS_REFRESH_ALL = 2;

    public static final String RECUR_WEEKLY = "147";

    public static final String RECUR_BIWEEKLY = "149";

    public static final String RECUR_MONTHLY = "146";

    public static final String RECUR_YEARLY = "148";

    public static String DATPATH = System.getProperty("user.dir") + File.separator + "MyMoney.dat";

    ;

    public static String HELPPATH = "file://///" + MM.APPPATH + "docs" + File.separator + "index.html";

    public static String REPORTFOLDERURL = "file://///" + MM.APPPATH + "reports" + File.separator;

    public static String REPORTFOLDER = MM.APPPATH + "reports" + File.separator;

    public static SqlMapClient sqlMap = null;

    public static LangLoader PHRASES = null;
}
