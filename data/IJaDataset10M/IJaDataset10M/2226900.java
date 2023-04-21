package net.sf.xml2cb.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("META-INF/xml2flat/i18n/messages");

    public static String errorEncodingCobolToXml(String name) {
        return MessageFormat.format(bundle.getString("1"), new Object[] { name });
    }

    public static String errorXmlWrite(String name, String value) {
        return MessageFormat.format(bundle.getString("2"), new Object[] { name, value });
    }

    public static String errorEncodingXmlToCobol(String name) {
        return MessageFormat.format(bundle.getString("3"), new Object[] { name });
    }

    public static String errorXmlRead(String name) {
        return MessageFormat.format(bundle.getString("4"), new Object[] { name });
    }
}
