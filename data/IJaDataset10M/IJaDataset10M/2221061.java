package com.skruk.elvis.admin;

import com.skruk.elvis.beans.Xpp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlPullParser;

/**
 * Description of the Class
 *
 * @author     skruk
 * @created    24 styczeń 2004
 */
public class ConfigManager {

    /** Description of the Field */
    public static final String VERSION_ZERO = "0.0";

    /** Description of the Field */
    private static final ConfigManager INSTANCE = new ConfigManager();

    /** Description of the Field */
    protected static String RSM_ELVIS_NS = null;

    /** Constructor for the ConfigManager object */
    protected ConfigManager() {
    }

    /**
	 * Gets the instance attribute of the ConfigManager class
	 *
	 * @return    The instance value
	 */
    public static ConfigManager getInstance() {
        synchronized (ConfigManager.class) {
            if (RSM_ELVIS_NS == null) {
                if (com.skruk.elvis.beans.ContextKeeper.isContext()) {
                    RSM_ELVIS_NS = com.skruk.elvis.beans.ContextKeeper.getElvisNamespace();
                } else {
                    RSM_ELVIS_NS = "http://wbss.pg.gda.pl/";
                }
            }
        }
        return INSTANCE;
    }

    /**
	 * Gets the version attribute of the ConfigManagerInterface object
	 *
	 * @return                                            The version value
	 * @exception  org.xmlpull.v1.XmlPullParserException  Description of the Exception
	 * @exception  java.io.FileNotFoundException          Description of the Exception
	 * @exception  java.io.IOException                    Description of the Exception
	 */
    public String getVersion() throws org.xmlpull.v1.XmlPullParserException, java.io.FileNotFoundException, java.io.IOException {
        return VERSION_ZERO;
    }

    /**
	 * Gets the version attribute of the ConfigManager object
	 *
	 * @param  file                                       Description of the Parameter
	 * @param  name                                       Description of the Parameter
	 * @param  prefix                                     Description of the Parameter
	 * @return                                            The version value
	 * @exception  org.xmlpull.v1.XmlPullParserException  Description of the Exception
	 * @exception  java.io.FileNotFoundException          Description of the Exception
	 * @exception  java.io.IOException                    Description of the Exception
	 */
    public String getVersion(String file, String name, String prefix) throws org.xmlpull.v1.XmlPullParserException, java.io.FileNotFoundException, java.io.IOException {
        java.io.Reader reader = new java.io.FileReader(file);
        String version = "0.0";
        XmlPullParser xpp = Xpp.borrowParser();
        xpp.setInput(reader);
        int eventType = xpp.getEventType();
        String pname = prefix + ":" + name;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if ((eventType == XmlPullParser.START_TAG) && xpp.getName().equals(pname)) {
                version = xpp.getAttributeValue(null, "elvis:version");
                break;
            } else {
                eventType = xpp.next();
            }
        }
        reader.close();
        Xpp.returnParser(xpp);
        return version;
    }

    /**
	 * Description of the Method
	 *
	 * @return                            Description of the Return Value
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    public String loadFile() throws FileNotFoundException, IOException {
        return "";
    }

    /**
	 * Description of the Method
	 *
	 * @param  where                      Description of the Parameter
	 * @return                            Description of the Return Value
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    String loadFile(String where) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(where);
        Reader reader = new InputStreamReader(fis, java.nio.charset.Charset.forName("UTF-8"));
        Writer writer = new StringWriter();
        char[] buffor = new char[1024];
        int count = -1;
        while ((count = reader.read(buffor)) != -1) {
            writer.write(buffor, 0, count);
        }
        reader.close();
        return writer.toString();
    }

    /**
	 * Description of the Method
	 *
	 * @param  file                       Description of the Parameter
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    public void storeFile(String file) throws FileNotFoundException, IOException {
    }

    /**
	 * Description of the Method
	 *
	 * @param  file                       Description of the Parameter
	 * @param  where                      Description of the Parameter
	 * @exception  FileNotFoundException  Description of the Exception
	 * @exception  IOException            Description of the Exception
	 */
    void storeFile(String file, String where) throws FileNotFoundException, IOException {
        final String oldFileName = where + ".new";
        FileOutputStream fos = new FileOutputStream(oldFileName);
        Writer writer = new OutputStreamWriter(fos, java.nio.charset.Charset.forName("UTF-8"));
        Reader reader = new StringReader(file);
        File fold = new File(oldFileName);
        File fnew = new File(where);
        char[] buffor = new char[1024];
        int count = -1;
        while ((count = reader.read(buffor)) != -1) {
            writer.write(buffor, 0, count);
        }
        writer.close();
        reader.close();
        if (!fold.renameTo(fnew)) {
            System.err.println("[WARNING] File " + where + " has not been not updated");
        }
    }
}
