package org.granite.builder.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 * @author Franck WOLFF
 */
public class XStreamUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final String ENCODING_ATTR = "encoding=\"";

    public static <T> T load(File file, Class<T> clazz) throws IOException {
        String charset = null;
        String xmlDeclaration = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            xmlDeclaration = reader.readLine();
        } finally {
            if (reader != null) reader.close();
        }
        if (xmlDeclaration != null && xmlDeclaration.startsWith("<?xml") && xmlDeclaration.endsWith("?>")) {
            int iEncoding = xmlDeclaration.indexOf(ENCODING_ATTR);
            if (iEncoding != -1) {
                int iEndEncoding = xmlDeclaration.indexOf('"', iEncoding + ENCODING_ATTR.length());
                if (iEndEncoding != -1) charset = xmlDeclaration.substring(iEncoding + ENCODING_ATTR.length(), iEndEncoding);
            }
        }
        return load(file, clazz, charset);
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(File file, Class<T> clazz, String charset) throws IOException {
        Reader reader = null;
        try {
            if (charset != null) reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)); else reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            XStream xs = new XStream(new Sun14ReflectionProvider());
            xs.processAnnotations(clazz);
            return (T) xs.fromXML(reader);
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static void save(File file, Object o) throws IOException {
        save(file, o, null);
    }

    public static void save(File file, Object o, String charset) throws IOException {
        if (charset == null) charset = DEFAULT_CHARSET;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            writer.write("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
            writer.newLine();
            XStream xs = new XStream(new Sun14ReflectionProvider());
            xs.processAnnotations(o.getClass());
            xs.toXML(o, writer);
        } finally {
            if (writer != null) writer.close();
        }
    }

    public static String toString(Object o) {
        StringWriter writer = new StringWriter();
        XStream xs = new XStream(new Sun14ReflectionProvider());
        xs.processAnnotations(o.getClass());
        xs.toXML(o, writer);
        return writer.toString();
    }
}
