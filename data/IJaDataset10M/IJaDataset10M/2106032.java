package net.sourceforge.acelogger.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import net.sourceforge.acelogger.LogEvent;
import net.sourceforge.acelogger.level.LogLevel;
import net.sourceforge.acelogger.location.LogEventLocation;
import net.sourceforge.acelogger.location.resolver.InternalCodeFrameResolver;
import com.thoughtworks.xstream.XStream;

/**
 * TODO: Create Doc.
 * 
 * @author Zardi (https://sourceforge.net/users/daniel_zardi)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DataProviderHelper {

    private static final String BASE_NAME = "src" + File.separator + "test" + File.separator + "resources";

    private static final File BASE_FILE = new File(BASE_NAME);

    private DataProviderHelper() {
    }

    private static void doAlias(XStream stream) {
        stream.alias("level", LogLevel.class);
        stream.alias("string", String.class);
        stream.alias("call", LogEvent.class);
        stream.alias("location", LogEventLocation.class);
        stream.alias("throwable", Throwable.class);
    }

    private static String getFileName(String identifier) {
        String packageName = "";
        String className = "";
        String methodName = "";
        String fileName = "";
        Integer packageEndIndex = identifier.lastIndexOf('.');
        if (packageEndIndex > 0) {
            packageName = identifier.substring(0, packageEndIndex);
        } else {
            packageEndIndex = 0;
        }
        Integer classEndIndex = identifier.lastIndexOf('#');
        if (classEndIndex > 0) {
            className = identifier.substring(packageEndIndex + 1, classEndIndex);
            methodName = identifier.substring(classEndIndex + 1);
            StringBuilder location = new StringBuilder();
            location.append(packageName);
            location.append(File.separator);
            location.append(className);
            location.append(File.separator);
            location.append(methodName);
            location.append(".xml");
            fileName = location.toString();
        }
        return fileName;
    }

    public static void saveDataFile(String identifier, Object[][] data) {
        String fileName = getFileName(identifier);
        if (fileName.length() > 0) {
            try {
                File dataFile = new File(BASE_FILE, fileName);
                if (!dataFile.exists()) {
                    if (!dataFile.getParentFile().exists()) {
                        dataFile.getParentFile().mkdirs();
                    }
                    dataFile.createNewFile();
                }
                XStream serializer = new XStream();
                doAlias(serializer);
                FileWriter writer = new FileWriter(dataFile);
                serializer.toXML(data, writer);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object[][] loadData() {
        InternalCodeFrameResolver resolver = new InternalCodeFrameResolver(1);
        StringBuilder identifier = new StringBuilder();
        StackTraceElement frame = resolver.getCodeFrame();
        identifier.append(frame.getClassName());
        identifier.append('#');
        identifier.append(frame.getMethodName());
        Object[][] data = new Object[][] {};
        String fileName = getFileName(identifier.toString());
        if (fileName.length() > 0) {
            try {
                XStream reader = new XStream();
                doAlias(reader);
                File dataFile = new File(BASE_FILE, fileName);
                FileInputStream stream = new FileInputStream(dataFile);
                data = (Object[][]) reader.fromXML(stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
