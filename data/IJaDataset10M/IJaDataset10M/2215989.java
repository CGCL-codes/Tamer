package com.explosion.expf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIManager;

public class ExpConstants {

    public static final String MENU_CASCADE_WINDOWS = "MENU_CASCADE_WINDOWS";

    public static final String MENU_TILE_WINDOWS_V = "MENU_TILE_WINDOWS_V";

    public static final String MENU_TILE_WINDOWS_H = "MENU_TILE_WINDOWS_H";

    public static final String MENU_WINDOW = "MENU_WINDOW";

    public static final String MENU_WINDOW_ITEM_PRESSED = "MENU_WINDOW_ITEM_PRESSED";

    public static final String FRAME_ACTIVATED = "FRAME_ACTIVATED";

    public static final String FRAME_DEACTIVATED = "FRAME_DEACTIVATED";

    public static final String FRAME_OPENED = "FRAME_OPENED";

    public static final String FRAME_CLOSED = "FRAME_CLOSED";

    public static final String MENU_FILE = "MENU_FILE";

    public static final String MENU_EDIT = "MENU_EDIT";

    public static final String MENU_HELP = "MENU_HELP";

    public static final String MENU_TOOLS = "MENU_TOOLS";

    public static final String MENU_LAF = "MENU_LAF";

    public static final String MENU_OPEN = "MENU_OPEN";

    public static final String MENU_SAVE = "MENU_SAVE";

    public static final String MENU_SAVEAS = "MENU_SAVEAS";

    public static final String MENU_SAVEALL = "MENU_SAVEALL";

    public static final String MENU_NEW = "MENU_NEW";

    public static final String MENU_CLOSEALL = "MENU_CLOSEALL";

    public static final String MENU_CLOSE = "MENU_CLOSE";

    public static final String MENU_PRINT = "MENU_PRINT";

    public static final String MENU_PRINTSETUP = "MENU_PRINTSETUP";

    public static final String MENU_COPY = "MENU_COPY";

    public static final String MENU_CUT = "MENU_CUT";

    public static final String MENU_PASTE = "MENU_PASTE";

    public static final String MENU_UNDO = "MENU_UNDO";

    public static final String MENU_REDO = "MENU_REDO";

    public static final String MENU_SELECTALL = "MENU_SELECTALL";

    public static final String MENU_CLEAR = "MENU_CLEAR";

    public static final String MENU_REFRESH = "MENU_REFRESH";

    public static final String MENU_EXIT = "MENU_EXIT";

    public static final String MENU_ABOUT = "MENU_ABOUT";

    public static final String MENU_HELP_CONTENTS = "MENU_HELP_CONTENTS";

    public static final String MENU_PROPERTIES = "MENU_PROPERTIES";

    public static final String MENU_METAL = "MENU_METAL";

    public static final String MENU_SYSTEM = "MENU_SYSTEM";

    public static final String MENU_MOTIF = "MENU_MOTIF";

    public static final String MENU_JGOOD_EXTWIN = "MENU_JGOOD_EXTWIN";

    public static final String MENU_JGOOD_PLASTIC = "MENU_JGOOD_PLASTIC";

    public static final String MENU_JGOOD_PLASTIC3D = "MENU_JGOOD_PLASTIC3D";

    public static final String MENU_JGOOD_PLASTICXP = "MENU_JGOOD_PLASTICXP";

    public static final String LAF = "lookandfeel";

    public static final String METAL = "javax.swing.plaf.metal.MetalLookAndFeel";

    public static final String MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

    public static final String JGOOD_EXTWIN = "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel";

    public static final String JGOOD_PLASTIC = "com.jgoodies.plaf.plastic.PlasticLookAndFeel";

    public static final String JGOOD_PLASTIC3D = "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel";

    public static final String JGOOD_PLASTICXP = "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel";

    public static final List looksList = new ArrayList();

    public static final Map looksMap = new HashMap();

    static {
        looksList.add(MENU_SYSTEM);
        looksList.add(MENU_METAL);
        looksList.add(MENU_MOTIF);
        looksList.add(MENU_JGOOD_EXTWIN);
        looksList.add(MENU_JGOOD_PLASTIC);
        looksList.add(MENU_JGOOD_PLASTIC3D);
        looksList.add(MENU_JGOOD_PLASTICXP);
        looksMap.put(MENU_SYSTEM, new ExpLookAndFeel(UIManager.getSystemLookAndFeelClassName(), "System", MENU_SYSTEM));
        looksMap.put(MENU_METAL, new ExpLookAndFeel(METAL, "Metal", MENU_METAL));
        looksMap.put(MENU_MOTIF, new ExpLookAndFeel(MOTIF, "Motif", MENU_MOTIF));
        looksMap.put(MENU_JGOOD_EXTWIN, new ExpLookAndFeel(JGOOD_EXTWIN, "Ext Windows", MENU_JGOOD_EXTWIN));
        looksMap.put(MENU_JGOOD_PLASTIC, new ExpLookAndFeel(JGOOD_PLASTIC, "Plastic", MENU_JGOOD_PLASTIC));
        looksMap.put(MENU_JGOOD_PLASTIC3D, new ExpLookAndFeel(JGOOD_PLASTIC3D, "Plastic 3D", MENU_JGOOD_PLASTIC3D));
        looksMap.put(MENU_JGOOD_PLASTICXP, new ExpLookAndFeel(JGOOD_PLASTICXP, "Plastic XP", MENU_JGOOD_PLASTICXP));
    }

    public static final String FRAMEWINDOW = "framewindow";

    public static final String ITEM_HISTORY = "itemhistory";

    public static final String MAXITEMSINHISTORY = "maxitemsinhistory";

    public static final String STARTMAXIMISED = "startmaximised";

    public static final String STARTCENTERED = "startcentered";

    public static final String HEIGHT = "height";

    public static final String WIDTH = "width";

    public static final String XPOS = "xpos";

    public static final String YPOS = "ypos";

    public static final String ERROR_FILE = "errorfile,";

    public static final String LOGERRORS = "writeerrorfile";

    public static final String LOG_FILE = "logfile,";

    public static final String LOG_TO_FILE = "writelogfile";

    public static final String LOG_TO_CONSOLE = "writestdout";

    public static final String ROOT_LOG_LEVEL = "rootloglevel";

    public static final String LOG_PATTERN = "logpattern";

    public static final String CUSTOM_LOG_LEVEL = "customloglevel";

    public static final String CUSTOM_LOG_VALUES = "customlogvalues";

    public static final String LOG_LEVEL_FATAL = "FATAL";

    public static final String LOG_LEVEL_ERROR = "ERROR";

    public static final String LOG_LEVEL_INFO = "INFO";

    public static final String LOG_LEVEL_WARN = "WARN";

    public static final String LOG_LEVEL_DEBUG = "DEBUG";

    public static final String TRUE = "true";

    public static final String FALSE = "false";

    public static final String INSTALL_DIR = "installdir";

    public static final String LASTDIR = "lastdir";

    public static final String TEMPDIR = "tempdir";

    public static final String FONTNAME = "fontname";

    public static final String BASE_FONT = "basefont";

    public static final String WINDOW_FONT = "windowfont";

    public static final String HELP_FILE_NAME = "helpfilename";

    public static final String HELP_STARTUP_ID = "helpstartupid";

    public static final String HELP_EMBEDDED = "helpembedded";

    public static final String HELP_HEIGHT = "helpheight";

    public static final String HELP_WIDTH = "helpwidth";

    public static final String SAVESETTINGSONEXIT = "savesettingsonexit";

    public static final String DEFAULT_WINDOW_ICON = "com/explosion/expf/resources/images/expf_icon.gif";

    public static final String DEFAULT_SPLASH_SCREEN_IMAGE = "com/explosion/expf/resources/images/expf_splash.gif";

    public static final String DEFAULT_EXCEPTION_ICON = "com/explosion/expf/resources/images/expf_exception_icon.gif";

    public static final String DEFAULT_PROPERTIES_ICON = "com/explosion/expf/resources/images/expf_properties_icon.gif";

    public static final String DEFAULT_HELP_ICON = "com/explosion/expf/resources/images/expf_help_icon.gif";

    public static final String DEFAULT_CUT_ICON = "com/explosion/expf/resources/images/edit/expf_cut_icon.gif";

    public static final String DEFAULT_COPY_ICON = "com/explosion/expf/resources/images/edit/expf_copy_icon.gif";

    public static final String DEFAULT_PASTE_ICON = "com/explosion/expf/resources/images/edit/expf_paste_icon.gif";

    public static final String DEFAULT_UNDO_ICON = "com/explosion/expf/resources/images/edit/expf_undo_icon.gif";

    public static final String DEFAULT_REDO_ICON = "com/explosion/expf/resources/images/edit/expf_redo_icon.gif";

    public static final String DEFAULT_FILEOPEN_ICON = "com/explosion/expf/resources/images/file/expf_fileopen_icon.gif";

    public static final String DEFAULT_FILESAVE_ICON = "com/explosion/expf/resources/images/file/expf_filesave_icon.gif";

    public static final String DEFAULT_NEW_ICON = "com/explosion/expf/resources/images/file/expf_filenew_icon.gif";

    public static final String DEFAULT_PRINT_ICON = "com/explosion/expf/resources/images/print/expf_print_icon.gif";

    public static final String COLORS_FOREGROUND = "col_foreground";

    public static final String COLORS_BACKGROUND = "col_background";

    public static final String COLOR_SELECTEDFORGROUND = "col_sel_foreground";

    public static final String COLOR_SELECTEDBACKGROUND = "col_sel_background";

    public static final Color BASE_COLOR = Color.white;

    public static final String BLACK = "-16777216";

    public static final String LIGHTGREY = "-12566464";

    public static final String WHITE = "-1";

    public static final String EXPF_APP_NOTATOR = "Expf-Application";

    public static final String EXPF_APPLICATION = "Application";

    public static final String EXPF_APP_NAME = "ApplicationName";

    public static final String EXPF_APP_VERSION = "ApplicationVersion";

    public static final String EXPF_APP_PREFIX = "ApplicationPrefix";

    public static final String EXPF_APP_SPLASHSCREEN_IMAGE = "SplashScreenImage";

    public static final String EXPF_APP_ABOUT_IMAGE = "AboutImage";

    public static final String EXPF_APP_FRAME_ICON_IMAGE = "FrameIconImage";

    public static final String EXPF_APP_HELP_FILE = "ApplicationHelpFile";

    public static final String EXPF_APP_HELP_STARTUPID = "ApplicationHelpStartUpID";

    public static final String EXPF_APP_MENU_MAP = "ApplicationMenuMap";

    public static final String EXPF_APP_VENDOR = "ApplicationVendor";

    public static final String EXPF_APP_VENDOR_URL = "ApplicationVendorURL";

    public static final String EXPF_APP_AUTHOR = "ApplicationAuthor";

    public static final String EXPF_APP_COPYRIGHT_YEAR = "ApplicationCopyrightYear";

    public static final String EXPF_APP_MODULES = "ApplicationModules";

    public static final String EXPF_MODULE_MANAGER = "ModuleManager";

    public static final String EXPF_MODULE_TYPE = "Type";

    public static final String EXPF_MODULE_TYPE_MODULE_DESCRIPTOR = "Module Descriptor";

    public static final String EXPF_MODULE_LOAD_ORDER = "LoadOrder";

    public static final String COMPNAME_EXPFRAME = "COMPNAME_EXPFRAME";

    public static final String COMPNAME_EXPMENUBAR = "COMPNAME_EXPMENUBAR";

    public static final String COMPNAME_EXPTOOLBAR = "COMPNAME_EXPTOOLBAR";

    public static final String COMPNAME_EXPPOPUPMENU = "COMPNAME_EXPPOPUPMENU";
}
