package it.novabyte.idb;

import it.novabyte.idb.extensions.IActivation;
import java.awt.EventQueue;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import javax.swing.UIManager;

public class iDB {

    private static ServiceLoader<IActivation> activationServiceLoader;

    private static Map<String, DriverInfo> driverInfos;

    private static Configuration configuration;

    public static void main(String[] args) {
        startup();
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception xp) {
                    xp.printStackTrace();
                }
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception xp) {
                    xp.printStackTrace();
                }
            }
        });
    }

    public static void startup() {
        deleteTempFiles();
        createDefaultDriverInfos();
        activationServiceLoader = ServiceLoader.load(IActivation.class);
        for (IActivation activation : activationServiceLoader) activation.startup();
        configuration = new Configuration();
    }

    private static void createDefaultDriverInfos() {
        driverInfos = new LinkedHashMap<String, DriverInfo>();
        Enumeration<Driver> it = DriverManager.getDrivers();
        while (it.hasMoreElements()) {
            Driver driver = it.nextElement();
            getDriverInfo(driver);
        }
    }

    private static void deleteTempFiles() {
        File[] files = new File(".").listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("temp") && name.endsWith(".sqlite");
            }
        });
        for (File file : files) file.delete();
    }

    public static void shutdown() {
        for (IActivation activation : activationServiceLoader) activation.shutdown();
        deleteTempFiles();
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static DriverInfo[] getDriverInfos() {
        return driverInfos.values().toArray(new DriverInfo[0]);
    }

    public static DriverInfo getDriverInfo(Driver driver) {
        String className = driver.getClass().getName();
        DriverInfo driverInfo = driverInfos.get(className);
        if (driverInfo == null) {
            driverInfo = new DriverInfo();
            driverInfo.setUrlPrefix(className);
            driverInfo.setClassName(className);
            driverInfo.setTemplateUrl("jdbc:");
            driverInfos.put(className, driverInfo);
        }
        return driverInfo;
    }

    public static DriverInfo getDriverInfo(String url) {
        DriverInfo matchingDriverInfo = null;
        int matchingLength = 0;
        for (Entry<String, DriverInfo> e : driverInfos.entrySet()) {
            String urlPrefix = e.getKey();
            DriverInfo driverInfo = e.getValue();
            if (url.startsWith(urlPrefix)) if (urlPrefix.length() > matchingLength) {
                matchingDriverInfo = driverInfo;
                matchingLength = urlPrefix.length();
            }
        }
        return matchingDriverInfo;
    }

    public static void addDriverInfo(DriverInfo driverInfo) {
        driverInfos.put(driverInfo.getUrlPrefix(), driverInfo);
    }
}
