package com.elibera.m.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import de.enough.polish.ui.Alert;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.android.lcdui.AlertType;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.android.lcdui.Image;
import com.elibera.m.app.AppSettings;
import com.elibera.m.app.MLE;
import com.elibera.m.display.AlertFactory;
import com.elibera.m.display.HelperDisplay;
import com.elibera.m.rms.HelperRMSStoreMLibera;
import com.elibera.m.xml.HelperXMLParser;

/**
 * this class contains the constants for translating of core words, of text messages and for the system images<br>
 * all constants starting with "WORD_" are core-system words which are cached.<br>
 * all constants starting with "TEXT_" are standard text messages which are not cached.<br>
 * all constants starting with "IMAGE_" are system images which are cached.
 * @author Matthias Meisenberger
 */
public class HelperApp {

    public static String DEVICE_ENCODING;

    public static final String PATH_LANG = "/";

    public static int CONF_SERVER_URI = 72;

    public static final String PATH_IMAGE = "/";

    public static final String PATH_SYS_PAGE = "/";

    public static MLE midlet;

    /**
	 * übersetzt ein Wort
	 * filename --> Name der Datei im Verzeichnis /lang/word/
	 * welches das zu übersetzten Wort enthält
	 * bei einem fehler wird der filename zurückgegeben
	 */
    public static String translateWord(int word) {
        try {
            java.io.InputStream is = translateWordToStream(word);
            if (is == null) return "null";
            byte[] b = HelperStd.getByteFromStream(is);
            if (b.length <= 0) return "null";
            try {
                return new String(b, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String(b);
        } catch (Exception e) {
        }
        return "null";
    }

    public static InputStream translateWordToStream(int word) {
        try {
            java.io.InputStream is = midlet.getClass().getResourceAsStream(PATH_LANG + "t" + word);
            return is;
        } catch (Exception e) {
        }
        return null;
    }

    public static InputStreamReader translateWordToStreamReader(int word) {
        InputStream is = translateWordToStream(word);
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (Exception e) {
        }
        return new InputStreamReader(is);
    }

    /**
	 * übersetzt ein Kern-Wort, was sehr häufig vorkommt. 
	 * dieses wird auch gespeichert, für den schnelleren Zugriff
	 */
    public static String translateCoreWord(int word) {
        if (word >= midlet.cachedCoreWords.length) midlet.cachedCoreWords = enhanceStringArrayAndSetValue(midlet.cachedCoreWords, word + 1); else if (midlet.cachedCoreWords[word] != null) return midlet.cachedCoreWords[word];
        String t = translateWord(word);
        if (t == null) return "null";
        midlet.cachedCoreWords[word] = t;
        return t;
    }

    /**
	 * erhöht das Array auf die neue größe
	 * @param src
	 * @param newSize
	 * @param indexToSet
	 * @param toSetValue
	 * @return
	 */
    private static String[] enhanceStringArrayAndSetValue(String[] src, int newSize) {
        String[] n = new String[newSize];
        System.arraycopy(src, 0, n, 0, src.length);
        return n;
    }

    private static Image[] enhanceImageArrayAndSetValue(Image[] src, int newSize) {
        Image[] n = new Image[newSize];
        System.arraycopy(src, 0, n, 0, src.length);
        return n;
    }

    /**
	 * gibt den Dateinamen für die ID zurück
	 */
    public static String getFileName(int img) {
        if (img == IMAGE_APP_LOGO) return "logo";
        if (img == IMAGE_APP_LOGO_KLEIN) return "logo";
        if (img == IMAGE_APP_NO_IMAGE) return "noimg";
        if (img == IMAGE_APP_NO_IMAGE_BIG) return "noimg_b";
        if (img == IMAGE_APP_LINK_SERVER) return "on";
        if (img == IMAGE_APP_LINK_LOCAL) return "doc";
        if (img == IMAGE_APP_FOLDER) return "fol";
        if (img == IMAGE_APP_DELETE) return "del";
        if (img == IMAGE_APP_MAIN) return "hom";
        if (img == IMAGE_APP_NAV_INDEX || img == IMAGE_APP_LEFT) return "lft";
        if (img == IMAGE_APP_RIGHT) return "rgt";
        if (img == IMAGE_APP_DOWN) return "dwn";
        if (img == IMAGE_APP_UP) return "up";
        if (img == IMAGE_APP_SEARCH) return "sea";
        if (img == IMAGE_APP_BUTTON_INFO) return "inf";
        if (img == IMAGE_APP_HELP) return "hlp";
        if (img == IMAGE_APP_DOWNLOAD || img == IMAGE_APP_UPLOAD) return "sav";
        if (img == IMAGE_APP_SETTINGS) return "set";
        if (img == IMAGE_APP_CLOSE) return "ext";
        if (img == IMAGE_APP_CANCEL) return "ccl";
        if (img == IMAGE_APP_BACK) return "back";
        if (img == IMAGE_APP_MAIL) return "mai";
        if (img == IMAGE_APP_NEW_MSG) return "nma";
        if (img == IMAGE_APP_MULTIPLE_CHOICE_EMPTY) return "mc_empty";
        if (img == IMAGE_APP_MULTIPLE_CHOICE_FULL) return "mc_sel";
        if (img == IMAGE_APP_SINGLE_CHOICE_EMPTY) return "sc_empty";
        if (img == IMAGE_APP_SINGLE_CHOICE_FULL) return "sc_sel";
        if (img == IMAGE_APP_LOAD_IMAGE || img == IMAGE_APP_IMAGE) return "img";
        if (img == IMAGE_APP_FAVORITES) return "fav";
        if (img == IMAGE_APP_USER) return "prs";
        if (img == IMAGE_APP_DEVICE) return "dev";
        if (img == IMAGE_APP_CONNECTION) return "cn";
        if (img == IMAGE_APP_FILE_SYSTEM) return "fio";
        if (img == IMAGE_APP_SETTINGS_BIG) return "set_b";
        if (img == IMAGE_APP_DISPLAY) return "dis";
        if (img == IMAGE_APP_MULTIMEDIA) return "mul";
        if (img == IMAGE_APP_VOLUME) return "vol";
        if (img == IMAGE_APP_KEYS) return "key";
        if (img == IMAGE_APP_FONT) return "fon";
        if (img == IMAGE_APP_INFO_BIG) return "inf_b";
        if (img == IMAGE_APP_ATTENTION) return "imp";
        if (img == IMAGE_APP_STORED_OBJECT) return "mlo";
        if (img == IMAGE_APP_EMPTY_PIXEL) return "p";
        if (img == IMAGE_QUESTION_CORRECT) return "q_richtig";
        if (img == IMAGE_QUESTION_WRONG) return "q_falsch";
        if (img == IMAGE_QUESTION_RESULT) return "result";
        if (img == IMAGE_VOC_BOX) return "box";
        if (img == IMAGE_QUESTION_TIPP) return "tip";
        if (img == IMAGE_MLE_SEARCH) return "sea";
        if (img == IMAGE_REFRESH) return "ref";
        if (img == IMAGE_VIDEO) return "vid";
        if (img == IMAGE_AUDIO) return "aud";
        if (img == IMAGE_REC) return "rec";
        if (img == IMAGE_PAUSE) return "pau";
        if (img == IMAGE_OP_DONE) return "opDone";
        return null;
    }

    public static Image getSystemImageBig(int img) {
        try {
            String t = getFileName(img);
            if (t == null) t = getFileName(IMAGE_APP_NO_IMAGE);
            Image m = getJarImage(PATH_IMAGE + t + "_b.png");
            if (m != null) return m;
            if (getJarImage(PATH_IMAGE + t + ".png") == null) m = getJarImage(PATH_IMAGE + getFileName(IMAGE_APP_NO_IMAGE) + "_b.png");
            if (m != null) return m;
        } catch (Exception e) {
        }
        return getSystemImage(img);
    }

    /**
	 * übersetzt ein Kern-Wort, was sehr häufig vorkommt. 
	 * dieses wird auch gespeichert, für den schnelleren Zugriff
	 */
    public static Image getSystemImage(int img) {
        try {
            if (img >= midlet.cachedCoreImages.length) midlet.cachedCoreImages = enhanceImageArrayAndSetValue(midlet.cachedCoreImages, img + 1); else if (midlet.cachedCoreImages[img] != null) return midlet.cachedCoreImages[img];
            String t = getFileName(img);
            if (t == null) {
                t = getFileName(IMAGE_APP_NO_IMAGE);
                img = IMAGE_APP_NO_IMAGE;
            }
            Image m = getJarImage(PATH_IMAGE + t + ".png");
            if (m == null) return getJarImage(PATH_IMAGE + getFileName(IMAGE_APP_NO_IMAGE) + ".png");
            midlet.cachedCoreImages[img] = m;
            return m;
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * gibt byte[] für ein System-Image zurück
	 * wird nicht gecacht
	 * @param midlet
	 * @param img
	 * @return
	 */
    public static byte[] getSystemImageByte(int img) {
        try {
            String t = HelperApp.getFileName(img);
            return getJarFileByte(PATH_IMAGE + t + ".png");
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * gibt ein Bild aus dem JAR Archiv zurück
	 */
    public static Image getJarImage(String url) {
        try {
            InputStream is = midlet.getClass().getResourceAsStream(url);
            if (is == null) return null;
            return Image.createImage(is);
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * gibt ein Bild aus dem JAR Archiv zurück
	 */
    public static byte[] getJarFileByte(String url) {
        try {
            InputStream is = midlet.getClass().getResourceAsStream(url);
            if (is == null) return null;
            return HelperStd.readBytesFromStream(is, null);
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * gibt eine System-Seite zurück
	 */
    public static InputStreamReader getSysPage(String name) {
        return getJarPage(PATH_SYS_PAGE + name);
    }

    public static InputStreamReader getJarPage(String url) {
        try {
            InputStream is = midlet.getClass().getResourceAsStream(url);
            if (is == null) {
                if (url.indexOf(".xml") < 0) is = midlet.getClass().getResourceAsStream(url + ".xml");
                if (is == null) return null;
            }
            return new InputStreamReader(is, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getSysPageByte(String url) {
        try {
            InputStream is = midlet.getClass().getResourceAsStream(PATH_SYS_PAGE + url);
            if (is == null) {
                if (url.indexOf(".xml") < 0) is = midlet.getClass().getResourceAsStream(PATH_SYS_PAGE + url + ".xml");
                if (is == null) return null;
            }
            return HelperStd.readBytesFromStream(is, null);
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * Setzt Einen Fehler Alert
	 * Exception kann null sein
	 */
    public static void setErrorAlert(String error, Exception e, Object obj, Displayable background) {
        if (background == null) background = midlet.display.getCurrent();
        HelperApp.runJavaGarbageCollector();
        AlertFactory alerts = midlet.alerts;
        if (alerts.errorAlert == null) {
            Alert errorAlert = new Alert(translateCoreWord(WORD_ERROR));
            Image img = getSystemImage(HelperApp.IMAGE_APP_LOGO_KLEIN);
            if (img != null) errorAlert.setImage(img);
            errorAlert.setType(AlertType.ERROR);
            errorAlert.addCommand(midlet.alertHide);
            errorAlert.setCommandListener(midlet);
            errorAlert.setTimeout(Alert.FOREVER);
            alerts.errorAlert = errorAlert;
        }
        if (e != null) error = error + "; \nMsg:" + e.getMessage() + "; \n\nType:" + e.getClass();
        alerts.errorAlert.setString(translateWord(TEXT_ERROR_DISPLAY_ERROR_ALERT_STD_MSG) + error);
        midlet.isAlertActive = true;
        alerts.background = background;
        midlet.setCurrent(alerts.errorAlert);
        HelperDisplay.layoutPainted = false;
    }

    /**
	 * Setzt Einen Info-Alert
	 */
    public static void setInfoAlert(String info, String titel, Displayable background) {
        if (background == null) background = midlet.display.getCurrent();
        AlertFactory alerts = midlet.alerts;
        if (alerts.infoAlert == null) {
            Alert infoAlert = new Alert(titel);
            Image img = getSystemImage(HelperApp.IMAGE_APP_LOGO_KLEIN);
            if (img != null) infoAlert.setImage(img);
            infoAlert.setType(AlertType.INFO);
            infoAlert.setTimeout(Alert.FOREVER);
            infoAlert.addCommand(midlet.alertHide);
            infoAlert.setCommandListener(midlet);
            alerts.infoAlert = infoAlert;
        } else alerts.infoAlert.setTitle(titel);
        alerts.infoAlert.setString(info);
        midlet.isAlertActive = true;
        alerts.background = background;
        midlet.setCurrent(alerts.infoAlert);
        HelperDisplay.layoutPainted = false;
    }

    /**
	 * kombiniert HelperApp.translateWord + formateMessage
	 * @param midlet
	 * @param word
	 * @param search
	 * @param inserts
	 * @return
	 */
    public static String formateTranslatedWord(int word, char[] search, String[] inserts) {
        return HelperStd.formateMessage(translateWord(word), search, inserts);
    }

    /**
	 * kombiniert HelperApp.translateWord + formateMessage
	 * @param midlet
	 * @param word
	 * @param search
	 * @param inserts
	 * @return
	 */
    public static String formateTranslatedWord(int word, char search, String inserts) {
        return HelperStd.formateMessage(translateWord(word), search, inserts);
    }

    /**
	 * returns the stored binary or null it the id doesn't exists
	 * @param id
	 * @return
	 */
    public static byte[] getCachedAppBinary(String id, StringBuffer ct) {
        int pos = HelperStd.whereIsItInArray(midlet.appBinaryCacheID, id);
        if (pos < 0) return null;
        if (ct != null) ct.append(midlet.appBinaryCacheCT[pos]);
        System.out.println("found in cache:" + id + "," + midlet.appBinaryCache.length);
        return midlet.appBinaryCache[pos];
    }

    /**
	 * durchsucht die caches, ob die URL drinnen ist
	 * @param url
	 * @param type
	 * @return
	 */
    public static int checkForHTTPURLInCache(String url, char type) {
        return HelperStd.whereIsItInArray(midlet.appBinaryCacheID, url);
    }

    public static void cacheAppBinary(String id, byte[] data, String ct) {
        if (data == null || id == null) return;
        if (ct == null) ct = "";
        long size = id.length();
        size += data.length;
        long CACHE_SIZE = AppSettings.BINARY_CACHE_SIZE;
        System.out.println("cache binary:" + id + ";" + size + ";" + ct + "," + CACHE_SIZE);
        if (size > CACHE_SIZE * 2 / 3) return;
        midlet.appBinaryCacheSize += size;
        long maxLastTime = HelperRMSStoreMLibera.getAppData(18) * 1000;
        if (midlet.appBinaryCacheSize > CACHE_SIZE) {
            for (int i = 0; i < midlet.appBinaryCacheTime.length; i++) {
                if (midlet.appBinaryCacheTime[i] + maxLastTime < System.currentTimeMillis()) {
                    removeAppBinaryCacheIndex(i);
                    if (midlet.appBinaryCacheSize < CACHE_SIZE) break;
                }
            }
        }
        while (midlet.appBinaryCacheSize > CACHE_SIZE) {
            removeAppBinaryCacheIndex(0);
        }
        midlet.appBinaryCacheID = HelperStd.incArray(midlet.appBinaryCacheID, id);
        midlet.appBinaryCacheCT = HelperStd.incArray(midlet.appBinaryCacheCT, ct);
        midlet.appBinaryCache = HelperStd.incArray(midlet.appBinaryCache, data);
        midlet.appBinaryCacheTime = HelperStd.incArray(midlet.appBinaryCacheTime, System.currentTimeMillis());
        System.out.println("cache size:" + midlet.appBinaryCacheSize + ",of:" + CACHE_SIZE);
    }

    private static void removeAppBinaryCacheIndex(int index) {
        midlet.appBinaryCacheSize -= (midlet.appBinaryCache[index].length + midlet.appBinaryCacheID[index].length());
        midlet.appBinaryCacheID = HelperStd.removeIndexFromArray(midlet.appBinaryCacheID, index);
        midlet.appBinaryCacheCT = HelperStd.removeIndexFromArray(midlet.appBinaryCacheCT, index);
        midlet.appBinaryCacheTime = HelperStd.removeIndexFromArray(midlet.appBinaryCacheTime, index);
        midlet.appBinaryCache = HelperStd.removeIndexFromArray(midlet.appBinaryCache, index);
        System.out.println("cache removed:" + midlet.appBinaryCacheSize);
    }

    /**
	 * ruft den Garbage Collector von Java auf
	 */
    public static void runJavaGarbageCollector() {
        long start = rt.freeMemory();
        try {
            rt.gc();
        } catch (Exception e) {
        }
        System.out.println("runJavaGarbageCollector:" + (rt.freeMemory() / 1024) + "KB;won:" + ((rt.freeMemory() - start) / 1024) + "KB");
    }

    /**
	 * fragt ab, ob noch genug speicher zum verarbeiten dieser Daten vorhanden ist
	 * @param neededBytes
	 * @return
	 */
    public static boolean hasEnoughMemoryForProcessObject(long neededBytes) {
        if (neededBytes * 2 + 50 * 1024 > rt.freeMemory()) return false;
        return true;
    }

    /**
	 * löscht dieverse Caches der Anwendung
	 *
	 */
    public static void resetMLiberaCaches() {
        System.out.println("resetMLiberaCaches");
        HelperXMLParser.resetTempProcElements();
        midlet.appBinaryCacheID = new String[0];
        midlet.appBinaryCacheCT = new String[0];
        midlet.appBinaryCacheTime = new long[0];
        midlet.appBinaryCache = new byte[0][0];
        midlet.appBinaryCacheSize = 0;
        midlet.cachedCoreImages = new Image[0];
        midlet.cachedCoreWords = new String[0];
        runJavaGarbageCollector();
    }

    /**
	 * überprüft den freien Speicher und
	 * löscht ansonsten die caches
	 */
    public static void checkMemory() {
        try {
            if (rt.freeMemory() < minFreeMemoryLevel1) runJavaGarbageCollector(); else return;
            if (rt.freeMemory() < minFreeMemoryLevel2) resetMLiberaCaches();
        } catch (Exception e) {
        }
    }

    public static Runtime rt = Runtime.getRuntime();

    public static final long minFreeMemoryLevel1 = 150 * 1024;

    public static final long minFreeMemoryLevel2 = 50 * 1024;

    public static int WORD_CLOSE_NOTE = 96;

    public static int WORD_LOADING = 83;

    public static int WORD_INPUT = 33;

    public static int WORD_OVERVIEW = 91;

    public static int WORD_PAGE = 58;

    public static int WORD_OF = 120;

    public static int WORD_DELETE = 25;

    public static int WORD_LERNRAUM = 74;

    public static int WORD_LERNRAUM_DELETE = 82;

    public static int WORD_CHANGE = 56;

    public static int WORD_CALL_LINK = 99;

    public static int WORD_SAVE = 119;

    public static int WORD_BACK = 113;

    public static int WORD_QUIT = 19;

    public static int WORD_CANCEL = 66;

    public static int WORD_ACCEPT = 61;

    public static int WORD_OKAY = 36;

    public static int WORD_ERROR = 103;

    public static int WORD_INFO = 62;

    public static int WORD_TRANSFER = 53;

    public static int WORD_STATUS = 73;

    public static int WORD_SIZE = 34;

    public static int WORD_CONVERTING = 23;

    public static int WORD_EXTRACT = 11;

    public static int WORD_DOWNLOAD = 26;

    public static int WORD_TRANSFERING = 57;

    public static int WORD_STARTSEITE = 52;

    public static int WORD_SERVER = 15;

    public static int WORD_SETTINGS = 40;

    public static int WORD_HELP = 43;

    public static int WORD_MENU = 37;

    public static int WORD_CLOSE_MENU = 55;

    public static int WORD_GO_TO_INDEX = 106;

    public static int WORD_BACK_TO_LERNRAUM = 71;

    public static int WORD_SHOW_INFOS = 105;

    public static int WORD_DELETE_CONTENT = 51;

    public static int WORD_SAVE_CONTENT = 20;

    public static int WORD_MENU_SERVER = 59;

    public static int WORD_BOOKMARKS = 97;

    public static int WORD_BOOKMARKS_DELETE = 110;

    public static int WORD_IMAGES = 10;

    public static int WORD_SELECTION = 115;

    public static int WORD_VIEW = 49;

    public static int WORD_KONTAKT = 29;

    public static int WORD_NEXT_PAGE = 45;

    public static int WORD_PREVIOUS_PAGE = 6;

    public static int TEXT_ERROR_TEXTBOX_MIN_SIZE = 124;

    public static int TEXT_ERROR_NOT_PARSABLE_CONTENT = 32;

    public static int TEXT_ENTER_TITEL = 64;

    public static int TEXT_NO_BOOKMARK_AV = 104;

    public static int TEXT_ADD_BOOKMARK_SUCCESS = 47;

    public static int TEXT_ADD_BOOKMARK = 100;

    public static int TEXT_JUMP_TO_PAGE = 22;

    public static int TEXT_ENTER_PAGE_NUM = 60;

    public static int TEXT_ERROR_APP_START_LOADING = 121;

    public static int TEXT_PS_TITLE_PS_POPUP_ADDITIONAL_ACTIONS_NAV_BAR = 98;

    public static int TEXT_PS_POPUP_BUTTON_TITLE_DELETE_PAGE = 50;

    public static int TEXT_PS_POPUP_BUTTON_TITLE_STORE_PS_TO_RMS = 67;

    public static int TEXT_PS_POPUP_BUTTON_TITLE_DISPLAY_INFO_TO_THE_PS = 95;

    public static int TEXT_MSG_DELETE_PAGESUITE_SUCCESS = 116;

    public static int TEXT_MSG_SAVE_PAGESUITE_SUCCESS = 18;

    public static int TEXT_PROGRESS_BAR_MSG_DOWNLOADING_PS = 9;

    public static int TEXT_PROGRESS_BAR_MSG_STORE_CURRENT_PS_TO_RMS = 8;

    public static int TEXT_ERROR_DOWNLOAD_AND_STORE_PS = 69;

    public static int TEXT_LERNRAUM_NO_CONTENT_WAS_STORED_EMPTY_ROOM = 2;

    public static int TEXT_DISPLAY_PS_MSG_LOAD_PAGE = 81;

    public static int TEXT_ERROR_DISPLAY_PS_PS_IS_EMPTY = 76;

    public static int TEXT_PROGRESSBAR_INFO_MSG_FOR_CANCEL_ACTION = 68;

    public static int TEXT_ERROR_MSG_XML_PARSING = 93;

    public static int TEXT_ERROR_MSG_UNKOWN_ERROR = 90;

    public static int TEXT_ERROR_MSG_RMS_FULL_NOT_ENOUGH_MEMORY = 27;

    public static int TEXT_ERROR_MSG_DISPLAY_NOTE = 41;

    public static int TEXT_LOAGING_MSG_1_INIT_APP = 122;

    public static int TEXT_LOAGING_MSG_2_LOADING_MODULES = 65;

    public static int TEXT_LOAGING_MSG_3_LOADING_RMS = 109;

    public static int TEXT_LOAGING_MSG_4_PARSING_INDEX_PAGE = 1;

    public static int TEXT_LOAGING_MSG_5_LOADING_INDEX_PAGE_AND_DISPLAY = 30;

    public static int TEXT_ERROR_LOADING_UNKOWN_ERROR = 31;

    public static int TEXT_ERROR_LOADING_RMS_FULL_NOT_ENOUGH_SPACE = 117;

    public static int TEXT_MSG_SETTINGS_SET_FONT_FACE_SUCCESS = 54;

    public static int TEXT_MSG_SETTINGS_SET_FONT_SIZE_SUCCESS = 7;

    public static int TEXT_MSG_SETTINGS_SET_USER_DATA_SUCCESS = 28;

    public static int ERROR_TEXT_NOT_ENOUGH_MEMORY_FOR_DOWNLOAD = 21;

    public static int TEXT_ERROR_USER_ABORTED_CURRENT_ACTION = 94;

    public static int TEXT_SETTINGS_GENERAL_SETTINGS_SUCCESFUL_CHANGED = 24;

    public static int TEXT_ERROR_VALIDATING_APPLICATION_MANIPULATED_APP = 35;

    public static int TEXT_ERROR_DISPLAY_ERROR_ALERT_STD_MSG = 112;

    public static int TEXT_DISPLAY_STD_PAGESUITE_TITEL_FOR_NOTE = 17;

    public static int TEXT_DISPLAY_STD_PAGESUITE_TITEL = 118;

    public static int TEXT_KEY_CANVAS_TITLE = 108;

    public static int TEXT_KEY_CANVAS_PAGE_INTRO = 88;

    public static int TEXT_KEY_CANVAS_PAGE_0 = 84;

    public static int TEXT_KEY_CANVAS_PAGE_STD = 101;

    public static int TEXT_KEY_CANVAS_PAGE_1 = 87;

    public static int TEXT_KEY_CANVAS_PAGE_2 = 89;

    public static int TEXT_KEY_CANVAS_PAGE_3 = 78;

    public static int TEXT_KEY_CANVAS_PAGE_4 = 79;

    public static int TEXT_KEY_CANVAS_PAGE_7 = 80;

    public static int TEXT_MENU_FORM_STD_TITLE = 48;

    public static final int IMAGE_APP_LOGO = 0, IMAGE_APP_LOGO_KLEIN = 1, IMAGE_APP_LINK_SERVER = 2, IMAGE_APP_LINK_LOCAL = 3, IMAGE_APP_MAIN = 4, IMAGE_APP_LEFT = 5, IMAGE_APP_NEW_MSG = 6, IMAGE_APP_NAV_INDEX = 7, IMAGE_APP_BUTTON_INFO = 8, IMAGE_APP_SEARCH = 9, IMAGE_APP_HELP = 10, IMAGE_APP_DOWNLOAD = 11, IMAGE_APP_UPLOAD = 12, IMAGE_APP_SETTINGS = 13, IMAGE_APP_CLOSE = 14, IMAGE_APP_BACK = 15, IMAGE_APP_MAIL = 16, IMAGE_APP_MULTIPLE_CHOICE_EMPTY = 17, IMAGE_APP_MULTIPLE_CHOICE_FULL = 18, IMAGE_APP_SINGLE_CHOICE_EMPTY = 19, IMAGE_APP_SINGLE_CHOICE_FULL = 20, IMAGE_APP_FOLDER = 21, IMAGE_APP_DELETE = 22, IMAGE_APP_LOAD_IMAGE = 23, IMAGE_APP_CANCEL = 24, IMAGE_APP_UP = 25, IMAGE_APP_DOWN = 26, IMAGE_APP_RIGHT = 27, IMAGE_APP_FAVORITES = 28, IMAGE_APP_NO_IMAGE = 29, IMAGE_APP_NO_IMAGE_BIG = 30, IMAGE_APP_USER = 31, IMAGE_APP_DEVICE = 32, IMAGE_APP_CONNECTION = 33, IMAGE_APP_FILE_SYSTEM = 34, IMAGE_APP_SETTINGS_BIG = 35, IMAGE_APP_IMAGE = 36, IMAGE_APP_DISPLAY = 37, IMAGE_APP_MULTIMEDIA = 38, IMAGE_APP_VOLUME = 39, IMAGE_APP_KEYS = 40, IMAGE_APP_FONT = 41, IMAGE_APP_INFO_BIG = 42, IMAGE_APP_ATTENTION = 43, IMAGE_APP_STORED_OBJECT = 44, IMAGE_APP_EMPTY_PIXEL = 45;

    public static int IMAGE_VIDEO = 201;

    public static int IMAGE_AUDIO = 206;

    public static int IMAGE_REC = 202;

    public static int IMAGE_OP_DONE = 203;

    public static int IMAGE_PAUSE = 204;

    public static int WORD_AUFNAHME = 159;

    public static int WORD_AUFNEHMEN = 157;

    public static int WORD_FERTIGSTELLEN = 127;

    public static int WORD_RESTART = 145;

    public static int WORD_STOP = 151;

    public static int WORD_PAUSE = 130;

    public static int WORD_RESUME = 129;

    public static int WORD_LAUTSTAERKE = 141;

    public static int WORD_PLAY = 158;

    public static int TEXT_MEDIA_AUDIO_PLAYER = 138;

    public static int TEXT_MEDIA_PROGRESS_BAR_EVENT_PLAYER_LOAD_DATA = 140;

    public static int TEXT_MEDIA_PROGRESS_BAR_EVENT_PLAYER_LOAD_DATA_FROM_SERVER = 133;

    public static int TEXT_MEDIA_PROGRESS_BAR_EVENT_PLAYER_PROCESS_DATA = 171;

    public static int TEXT_MEDIA_ERROR_PLAYING_DATA = 144;

    public static int TEXT_MEDIA_SETTINGS_MSG_SET_VOLUME_SUCCESS = 139;

    public static int TEXT_MEDIA_RECORD_INFO_TEXT = 166;

    public static int TEXT_MEDIA_ERROR_RECORD_START_RECORDING = 143;

    public static int TEXT_MEDIA_ERROR_RECORD_FINISH_RECORDING = 162;

    public static int TEXT_MEDIA_RECORD_AUDIO_SUCCESS_POP_UP_TEXT_SHOW_RECORDING = 160;

    public static int TEXT_MEDIA_ERROR_RECORD_IMAGE_GET_VIDEO_CONTROL = 148;

    public static int TEXT_MEDIA_ERROR_RECORD_IMAGE_TAKE_SNAPESHOT = 146;

    public static int TEXT_MEDIA_RECORD_IMAGE_SUCCESS_POP_UP_TEXT_SHOW_IMAGE_RECORDING = 168;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_AUDIO_RECORDING = 155;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_IMAGE_RECORDING = 164;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_PLAYING_VIDEO = 136;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_PLAYING_AUDIO = 149;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_FORMAT = 131;

    public static int TEXT_LOADING_MEDIA_DATA = 172;

    public static int TEXT_ERROR_LOADING_MEDIA_DATA = 142;

    public static int TEXT_MEDIA_TITEL_VIDEO_PLAYER = 126;

    public static int TEXT_MEDIA_RECORD_VIDEO_SUCCESS_POP_UP_TEXT_SHOW_RECORDING = 152;

    public static int TEXT_MEDIA_ERROR_NOT_SUPPORTED_VIDEO_RECORDING = 137;

    public static int TEXT_MEDIA_ERROR_RECORD_INIT_AUDIO_CAPTURE = 170;

    public static String COLOR_VOCS_FRONT_SIDE = "bbfffc";

    public static String COLOR_VOCS_BACK_SIDE = "b1b6b6";

    public static final int IMAGE_QUESTION_TIPP = 350;

    public static final int IMAGE_QUESTION_CORRECT = 302;

    public static final int IMAGE_QUESTION_WRONG = 303;

    public static final int IMAGE_QUESTION_RESULT = 304;

    public static final int IMAGE_VOC_BOX = 305;

    public static final int IMAGE_MLE_SEARCH = 315;

    public static final int IMAGE_REFRESH = 316;

    public static int WORD_LOESUNG = 181;

    public static int WORD_PUNKTE = 193;

    public static int WORD_HINT = 195;

    public static int WORD_AUFDECKEN = 184;

    public static int WORD_VOC_NEXT_CARD = 194;

    public static int WORD_VOC_SIDE_LEFT = 190;

    public static int WORD_VOC_SIDE_RIGHT = 198;

    public static int WORD_VOC_RICHTIG = 180;

    public static int WORD_VOC_FALSCH = 177;

    public static int WORD_VOC_BOX_NAME = 187;

    public static int WORD_VOC_BOX_FERTIGE = 196;

    public static int WORD_VOC_BOX_START = 182;

    public static int WORD_VOC_CARD = 174;

    public static int TEXT_VOC_SYNCING_ERROR = 188;

    public static int TEXT_VOC_SYNCING_SUCCESS = 175;

    public static int TEXT_MLE_QUESTION_SOLVE_GRAFICAL_MARKING_QUESTION_WRONG_MARKER_COUNT = 183;

    public static int TEXT_MLE_QUESTION_SOLVE_QUESTION_WAS_ALREADY_SOLVED_MSG = 186;

    public static int TEXT_MLE_QUESTION_SOLVE_REACHED_POINTS_MSG = 191;

    public static int TEXT_MLE_PROGRESS_BAR_VOC_TRAINER_LOADING_MSG = 189;

    public static int TEXT_MLE_PROGRESS_BAR_SOLVING_QUESTION_MSG = 197;

    public static int TEXT_MLE_VOC_TRAINER_MSG_BOX_FINISHED_BOX_IS_NOW_EMPTY = 178;

    public static int TEXT_MLE_RESULT_TAG_STD_WORDS = 192;

    public static int TEXT_MLE_VOC_TRAINER_BACKSIDE_RIGHT_OR_WRONG = 179;

    public static int TEXT_QUESTION_SOLVED_ALL_CORRECT = 185;

    public static int TEXT_QUESTION_SOLVED_FALSE = 173;
}
