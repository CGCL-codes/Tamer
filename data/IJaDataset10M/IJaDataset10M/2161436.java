package com.sebulli.fakturama;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import com.sebulli.fakturama.logger.Logger;
import com.sebulli.fakturama.office.DocumentFilename;

/**
 * Translate strings using gettext
 * @see http://www.gnu.org/software/gettext/
 * 
 * @author Gerd Bartelt
 */
public class Translate {

    private static Properties messages = null;

    private enum states {

        IDLE, MSGCTXT, MSGID, MSGSTR
    }

    /**
	 * Replace a string by the translated string.
	 * If no translation is available, return the original one.
	 * 
	 * @param s
	 * 			String to translate
	 * @return
	 * 			The translated String
	 */
    public static String _(String s) {
        String sout;
        if (messages == null) {
            messages = new Properties();
            loadPoFile();
        }
        if (!messages.containsKey(s)) return s; else {
            sout = messages.getProperty(s);
            if (sout.isEmpty()) return s; else return sout;
        }
    }

    /**
	 * Replace a string by the translated string.
	 * If no translation is available, return the original one.
	 * 
	 * @param s
	 * 			String to translate
	 * @param translate
	 * 			TRUE, if the string should be translated
	 * @return
	 * 			The translated String
	 */
    public static String _(String s, boolean translate) {
        if (translate) return _(s); else return s;
    }

    /**
	 * Replace a string in a context by the translated string.
	 * If no translation is available, return the original one.
	 * 
	 * @param s
	 * 			String to translate
	 * @param context
	 * 			Context of the string
	 * @return
	 * 			The translated String
	 */
    public static String _(String s, String context) {
        String sWithContext = context + "|" + s;
        String sout;
        if (messages == null) {
            messages = new Properties();
            loadPoFile();
        }
        if (!messages.containsKey(sWithContext)) return s; else {
            sout = messages.getProperty(sWithContext);
            if (sout.isEmpty()) return s; else return sout;
        }
    }

    /**
	 * Replace a string by the translated string.
	 * If no translation is available, return the original one.
	 * 
	 * @param s
	 * 			String to translate
	 * @param context
	 * 			Context of the string
	 * @param translate
	 * 			TRUE, if the string should be translated
	 * @return
	 * 			The translated String
	 */
    public static String _(String s, String context, boolean translate) {
        if (translate) return _(s, context); else return s;
    }

    /**
	 * Load a PO file from the resource and fill the properties
	 *
	 * @return
	 * 			url of the resource to load
	 */
    public static void loadPoFile(URL url) {
        states state = states.IDLE;
        String msgCtxt = "";
        String msgId = "";
        String msgStr = "";
        try {
            if (url == null) return;
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.startsWith("msgctxt")) {
                    if (state != states.MSGCTXT) msgCtxt = "";
                    state = states.MSGCTXT;
                    strLine = strLine.substring(7).trim();
                }
                if (strLine.startsWith("msgid")) {
                    if (state != states.MSGID) msgId = "";
                    state = states.MSGID;
                    strLine = strLine.substring(5).trim();
                }
                if (strLine.startsWith("msgstr")) {
                    if (state != states.MSGSTR) msgStr = "";
                    state = states.MSGSTR;
                    strLine = strLine.substring(6).trim();
                }
                if (!strLine.startsWith("\"")) {
                    state = states.IDLE;
                    msgCtxt = "";
                    msgId = "";
                    msgStr = "";
                } else {
                    if (state == states.MSGCTXT) {
                        msgCtxt += format(strLine);
                    }
                    if (state == states.MSGID) {
                        if (msgId.isEmpty()) {
                            if (!msgCtxt.isEmpty()) {
                                msgId = msgCtxt + "|";
                                msgCtxt = "";
                            }
                        }
                        msgId += format(strLine);
                    }
                    if (state == states.MSGSTR) {
                        msgCtxt = "";
                        msgStr += format(strLine);
                        if (!msgId.isEmpty()) messages.setProperty(msgId, msgStr);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            Logger.logError(e, "Error loading message.po.");
        }
    }

    /**
	 * Load a PO file from the resource and fill the properties
	 */
    public static void loadPoFile() {
        URL url;
        boolean loadedLocalFile = false;
        String workspace = Activator.getDefault().getPreferenceStore().getString("GENERAL_WORKSPACE");
        String langFilePath = workspace + "/Language/";
        File dir = new File(langFilePath);
        String[] children = dir.list();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                DocumentFilename langFileName = new DocumentFilename(langFilePath, children[i]);
                if (langFileName.getExtension().equalsIgnoreCase(".po")) {
                    File f = new File(langFileName.getPathAndFilename());
                    if (f.exists()) {
                        try {
                            loadPoFile(f.toURI().toURL());
                            return;
                        } catch (MalformedURLException e) {
                        }
                    }
                }
            }
        }
        String localCode = System.getProperty("osgi.nl");
        url = Activator.getDefault().getBundle().getResource("po/messages_" + localCode.split("_")[0] + ".po");
        if (url != null) {
            loadPoFile(url);
            loadedLocalFile = true;
        }
        url = Activator.getDefault().getBundle().getResource("po/messages_" + localCode + ".po");
        if (url != null) {
            loadPoFile(url);
            loadedLocalFile = true;
        }
        if (!loadedLocalFile) {
            url = Activator.getDefault().getBundle().getResource("po/messages.po");
            loadPoFile(url);
        }
    }

    /**
	 * Remove the trailing and leading quotes and unescape the string.
	 * 
	 * @param sin
	 * 			The input string
	 * @return
	 * 			The formated string
	 */
    static String format(String sin) {
        sin = sin.trim();
        if (sin.startsWith("\"")) sin = sin.substring(1);
        if (sin.endsWith("\"")) sin = sin.substring(0, sin.length() - 1);
        String sout = "";
        boolean escape = false;
        for (int i = 0; i < sin.length(); i++) {
            char c = sin.charAt(i);
            if (c == '\\' && !escape) escape = true; else {
                if (escape) {
                    if (c == '\'') sout += '\'';
                    if (c == '\"') sout += '\"';
                    if (c == '\\') sout += '\\';
                    if (c == 'r') sout += '\r';
                    if (c == 'n') sout += '\n';
                    if (c == 'f') sout += '\f';
                    if (c == 't') sout += '\t';
                    if (c == 'b') sout += '\b';
                    escape = false;
                } else {
                    sout += c;
                }
            }
        }
        return sout;
    }
}
