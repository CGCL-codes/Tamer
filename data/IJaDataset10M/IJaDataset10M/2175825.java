package com.groovytagger.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public final class StaticObj {

    public static final boolean DEBUG = true;

    public static final String APPNAME = "Groovy Tagger";

    public static final String APPVERSION = "1.0";

    public static final String APPDATE = "11.10.2009";

    public static final String CODEPAGE = "codepage";

    public static final String PREF_UNICODE_KEY = "unicode";

    public static final String PREF_UNICODE_DEFAULT = "";

    public static final String PREF_ID3V1_KEY = "id3v1";

    public static final String PREF_ID3V1_DEFAULT = "YES";

    public static final String PREF_ID3V2_KEY = "id3v2";

    public static final String PREF_ID3V2_DEFAULT = "YES";

    public static final String PREF_USEPIC_KEY = "pic";

    public static final String PREF_USEPIC_DEFAULT = "YES";

    public static final String PREF_STARTFROMLAST = "startfromlast";

    public static final String PREF_STARTDIR = "startdir";

    public static final String PREF_LASTDDIR = "path";

    public static final String PREF_LASTDDIR_PICTURE = "pathPicture";

    public static final int SLEEP = 0;

    public static final String GOOGLE_IMG_URL = "http://images.google.com/images?hl=en&um=1&ie=UTF-8&sa=N&tab=wi&q=";

    public static final String GOOGLE_LYR_URL = "http://www.google.com/search?hl=en&btnG=Google+Search&aq=f&oq=&q=";

    public static boolean PROXY_ENABLED = false;

    public static String PROXY_URL = "";

    public static String PROXY_PORT = "";

    public static String PROXY_USERNAME = "";

    public static String PROXY_PASSWORD = "";

    public static String PROXY_IGNORE_LIST = "";

    public static final String PROXY_SETTING_FILE = "proxy.ini";

    public static final String PATTERN_SETTING_FILE = "pattern.ini";

    public static final String MAGIC_SETTING_FILE = "settings.ini";

    public static final String AGREE_FILE = "agreement.agr";

    public static final String AGREED_FILE = "agreed";

    public static String AVAILABLE_LOCALE = "";

    public static String DEFAULT_LOCALE = "";

    public static Locale ACTIVE_LOCALE = Locale.getDefault();

    public static Locale[] LOCALE_LIST = null;

    public static ResourceBundle RBUNDLE = ResourceBundle.getBundle("language", ACTIVE_LOCALE);

    public static void readProxySetting() {
        try {
            File dir = new File(PROXY_SETTING_FILE);
            if (dir.exists()) {
                Encode enc = new Encode();
                FileReader input = new FileReader(PROXY_SETTING_FILE);
                BufferedReader bufRead = new BufferedReader(input);
                String line;
                line = bufRead.readLine();
                while (line != null) {
                    String[] bude = line.split("=");
                    if (bude.length > 1) {
                        if (bude[0].trim().equalsIgnoreCase("PROXY_ENABLED")) {
                            PROXY_ENABLED = Boolean.valueOf(bude[1].trim());
                        }
                        if (bude[0].trim().equalsIgnoreCase("PROXY_URL")) {
                            PROXY_URL = bude[1].trim();
                        } else if (bude[0].trim().equalsIgnoreCase("PROXY_PORT")) {
                            PROXY_PORT = bude[1].trim();
                        } else if (bude[0].trim().equalsIgnoreCase("PROXY_USERNAME")) {
                            PROXY_USERNAME = enc.decrypt(bude[1].trim());
                        } else if (bude[0].trim().equalsIgnoreCase("PROXY_PASSWORD")) {
                            PROXY_PASSWORD = enc.decrypt(bude[1].trim());
                        } else if (bude[0].trim().equalsIgnoreCase("PROXY_IGNORE_LIST")) {
                            PROXY_IGNORE_LIST = bude[1].trim();
                        }
                    }
                    line = bufRead.readLine();
                }
                bufRead.close();
                input.close();
            } else {
                PROXY_ENABLED = false;
            }
        } catch (Exception e) {
            PROXY_ENABLED = false;
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static void saveProxySetting() {
        try {
            Encode enc = new Encode();
            FileWriter output = new FileWriter((PROXY_SETTING_FILE));
            BufferedWriter bufWrite = new BufferedWriter(output);
            bufWrite.write("PROXY_ENABLED=" + PROXY_ENABLED);
            bufWrite.newLine();
            bufWrite.write("PROXY_URL=" + PROXY_URL);
            bufWrite.newLine();
            bufWrite.write("PROXY_PORT=" + PROXY_PORT);
            bufWrite.newLine();
            bufWrite.write("PROXY_USERNAME=" + enc.encrypt(PROXY_USERNAME));
            bufWrite.newLine();
            bufWrite.write("PROXY_PASSWORD=" + enc.encrypt(PROXY_PASSWORD));
            bufWrite.newLine();
            bufWrite.write("PROXY_IGNORE_LIST=" + PROXY_IGNORE_LIST);
            bufWrite.close();
            output.close();
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static void readMagicSetting() {
        try {
            File dir = new File(MAGIC_SETTING_FILE);
            if (dir.exists()) {
                Encode enc = new Encode();
                FileReader input = new FileReader(MAGIC_SETTING_FILE);
                BufferedReader bufRead = new BufferedReader(input);
                String line;
                line = bufRead.readLine();
                while (line != null) {
                    String[] bude = line.split("=");
                    if (bude.length > 1) {
                        if (bude[0].trim().equalsIgnoreCase("DEFAULT_LOCALE")) {
                            try {
                                DEFAULT_LOCALE = bude[1].trim();
                                String[] _vv = DEFAULT_LOCALE.split("_");
                                ACTIVE_LOCALE = new Locale(_vv[0], _vv[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ACTIVE_LOCALE = Locale.getDefault();
                            }
                        }
                        if (bude[0].trim().equalsIgnoreCase("AVAILABLE_LOCALE")) {
                            AVAILABLE_LOCALE = bude[1].trim();
                            String[] _sLoc = AVAILABLE_LOCALE.split(",");
                            LOCALE_LIST = new Locale[_sLoc.length];
                            for (int i = 0; i < _sLoc.length; i++) {
                                String[] _cc = _sLoc[i].split("_");
                                LOCALE_LIST[i] = new Locale(_cc[0], _cc[1]);
                            }
                        }
                    }
                    line = bufRead.readLine();
                }
                bufRead.close();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ACTIVE_LOCALE = Locale.getDefault();
            RBUNDLE = ResourceBundle.getBundle("language", ACTIVE_LOCALE);
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        RBUNDLE = ResourceBundle.getBundle("language", ACTIVE_LOCALE);
    }

    public static void saveMagicSetting() {
        try {
            FileWriter output = new FileWriter((MAGIC_SETTING_FILE));
            BufferedWriter bufWrite = new BufferedWriter(output);
            bufWrite.write("DEFAULT_LOCALE=" + DEFAULT_LOCALE);
            bufWrite.newLine();
            bufWrite.write("AVAILABLE_LOCALE=" + AVAILABLE_LOCALE);
            bufWrite.newLine();
            bufWrite.close();
            output.close();
            readMagicSetting();
            RBUNDLE = ResourceBundle.getBundle("language", ACTIVE_LOCALE);
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
