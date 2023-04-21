package sdloader.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import sdloader.util.ResourceUtil;

/**
 * ログファクトリークラス
 * 
 * @author c9katayama
 */
public class SDLoaderLogFactory {

    private static boolean useSystemLog;

    static {
        InputStream is = getLogConfig();
        if (is == null) {
            useSystemLog = true;
            System.out.println("[SDLoader] Log configuration not found.use SDLoaderLogSystemImpl.");
        } else {
            try {
                if (checkUseSystemLog(is) == true) {
                    useSystemLog = true;
                    System.out.println("[SDLoader] Log class load fail by system class loader.use SDLoaderLogSystemImpl.");
                } else {
                    is = getLogConfig();
                    LogManager.getLogManager().readConfiguration(is);
                }
            } catch (Exception ioe) {
                useSystemLog = true;
                ioe.printStackTrace();
                System.out.println("[SDLoader] Log configuration fail.use SDLoaderLogSystemImpl.");
            }
        }
    }

    private static InputStream getLogConfig() {
        String configPath = System.getProperty("java.util.logging.config.file");
        if (configPath == null || configPath.length() == 0) {
            InputStream is = ResourceUtil.getResourceAsStream("sdloader-logging.properties", SDLoaderLogFactory.class);
            if (is == null) {
                is = ResourceUtil.getResourceAsStream("sdloader.properties", SDLoaderLogFactory.class);
            }
            return is;
        }
        return null;
    }

    private static boolean checkUseSystemLog(InputStream is) throws IOException {
        Properties p = new Properties();
        p.load(is);
        String[] handlers = p.getProperty("handlers").split(",");
        for (int i = 0; i < handlers.length; i++) {
            String target = handlers[i].trim();
            try {
                ClassLoader.getSystemClassLoader().loadClass(target);
            } catch (ClassNotFoundException e) {
                if (!target.startsWith("sdloader.log")) {
                    System.err.println("Class not found. class=" + target);
                }
                return true;
            }
        }
        return false;
    }

    private SDLoaderLogFactory() {
    }

    public static SDLoaderLog getLog(Class<?> c) {
        if (useSystemLog) {
            return new SDLoaderLogSystemImpl(c);
        } else {
            return new SDLoaderLogJDKLoggerImpl(c);
        }
    }
}
