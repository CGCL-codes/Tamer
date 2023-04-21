package net.sf.lm4j.monitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.lm4j.xml.ConfigReader;
import net.sf.lm4j.xml.LogDirectoryConfig;
import org.xml.sax.SAXException;

public class MonitorManager {

    private static MonitorManager manager;

    private static Map moniterMap = null;

    private List configs = null;

    private MonitorManager() {
        if (moniterMap == null) moniterMap = new HashMap();
        try {
            configs = ConfigReader.getConfig(".");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private MonitorManager(String configFileName) {
        if (moniterMap == null) moniterMap = new HashMap();
        try {
            configs = ConfigReader.getConfig(configFileName);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static MonitorManager newInstance() {
        if (manager == null) manager = new MonitorManager();
        return manager;
    }

    public static MonitorManager newInstance(String configFileName) {
        if (manager == null) manager = new MonitorManager(configFileName);
        return manager;
    }

    public void addMonitor(File file, HttpSession session) {
        if (moniterMap.get(session.getId() + file.getAbsolutePath()) == null) {
            FileMonitor newmoniter = new FileMonitor(file, session);
            int bufferSize = 100;
            for (Iterator iterator = configs.iterator(); iterator.hasNext(); ) {
                LogDirectoryConfig config = (LogDirectoryConfig) iterator.next();
                if (file.getAbsolutePath().startsWith(new File(config.getPath()).getAbsolutePath())) {
                    bufferSize = config.getBuffersize();
                    break;
                }
            }
            newmoniter.setBufferSize(bufferSize);
            Thread monitorThread = new Thread(newmoniter);
            moniterMap.put(session.getId() + file.getAbsolutePath(), newmoniter);
            monitorThread.start();
        }
    }

    public void removeMoniter(File file, HttpSession session) {
        FileMonitor fileMonitor = (FileMonitor) moniterMap.get(session.getId() + file.getAbsolutePath());
        if (fileMonitor != null) {
            fileMonitor.setRunning(false);
            fileMonitor.beforeDestroy();
            fileMonitor = null;
            moniterMap.remove(session.getId() + file.getAbsolutePath());
        }
    }

    public FileMonitor getMoniter(File file, HttpSession session) {
        return moniterMap.get(session.getId() + file.getAbsolutePath()) != null ? (FileMonitor) moniterMap.get(session.getId() + file.getAbsolutePath()) : null;
    }
}
