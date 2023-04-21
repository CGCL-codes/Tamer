package de.schlund.pfixxml.loader;

import de.schlund.pfixxml.PathFactory;
import de.schlund.pfixxml.config.XMLPropertiesUtil;
import java.io.*;
import java.util.*;
import org.apache.log4j.Category;
import org.xml.sax.SAXException;

/**
 * AppLoaderConfig.java 
 * 
 * Created: 29.04.2003
 * 
 * @author mleidig
 */
public class AppLoaderConfig {

    private Category CAT = Category.getInstance(getClass().getName());

    private AppLoader loader;

    protected AppLoaderConfig(AppLoader loader) {
        this.loader = loader;
    }

    protected void init(Properties globProps) {
        try {
            Properties props = new Properties();
            String fileName = getProperty(globProps, "apploader.propertyfile");
            try {
                XMLPropertiesUtil.loadPropertiesFromXMLFile(PathFactory.getInstance().createPath(fileName).resolve(), props);
            } catch (IOException x) {
                throw new AppLoaderConfigException("AppLoader config file '" + fileName + "' can't be loaded.");
            } catch (SAXException e) {
                throw new AppLoaderConfigException("AppLoader config file '" + fileName + "' can't be parsed.");
            }
            String name = "apploader.mode";
            String val = getProperty(props, name);
            if (val.equals("on")) {
                loader.setEnabled(true);
            } else if (val.equals("off")) {
                loader.setEnabled(false);
                return;
            } else {
                throw new AppLoaderConfigException("Property '" + name + "' needs value 'on|off'.");
            }
            name = "apploader.cmdlistener.mode";
            val = getProperty(props, name);
            boolean active = false;
            if (val.equals("on")) {
                active = true;
            } else if (val.equals("off")) {
                active = false;
            } else {
                throw new AppLoaderConfigException("Property '" + name + "' needs value 'on|off'.");
            }
            name = "apploader.cmdlistener.port";
            val = getProperty(props, name);
            try {
                int port = Integer.parseInt(val);
                if (active) {
                    CommandListener cl = new CommandListener(port);
                    loader.setCommandListener(cl);
                }
            } catch (NumberFormatException x) {
                throw new AppLoaderConfigException("Property '" + name + "' has invalid value.");
            }
            name = "apploader.trigger.type";
            val = getProperty(props, name);
            if (val.equals("auto")) {
                loader.setTrigger(AppLoader.AUTO_TRIGGER);
            } else if (val.equals("manual")) {
                loader.setTrigger(AppLoader.MANUAL_TRIGGER);
            } else {
                throw new AppLoaderConfigException("Property '" + name + "' needs value 'auto|manual'.");
            }
            String ttype = val;
            if (ttype.equals("auto")) {
                name = "apploader.trigger.interval";
                val = getProperty(props, name);
                try {
                    int interval = Integer.parseInt(val) * 1000;
                    loader.setInterval(interval);
                } catch (NumberFormatException x) {
                    throw new AppLoaderConfigException("Property '" + name + "' has invalid value.");
                }
            }
            name = "apploader.policy.inconsistency.";
            Enumeration enm = props.propertyNames();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                if (key.startsWith(name)) {
                    String typeName = key.substring(name.length());
                    int type;
                    if (typeName.equals("possible")) {
                        type = AppLoader.INCONSISTENCY_POSSIBLE;
                    } else if (typeName.equals("probable")) {
                        type = AppLoader.INCONSISTENCY_PROBABLE;
                    } else {
                        throw new AppLoaderConfigException("Property '" + name + "' needs extension 'possible|probable'.");
                    }
                    val = getProperty(props, key);
                    int action;
                    if (val.equals("reload")) {
                        action = AppLoader.RELOAD_ACTION;
                    } else if (val.equals("restart")) {
                        action = AppLoader.RESTART_ACTION;
                    } else if (val.equals("ignore")) {
                        action = AppLoader.IGNORE_ACTION;
                    } else {
                        throw new AppLoaderConfigException("Property '" + key + "' needs value 'reload|restart|ignore'.");
                    }
                    loader.setPolicy(type, action);
                }
            }
            name = "apploader.repository";
            val = getProperty(props, name);
            File file = PathFactory.getInstance().createPath(val).resolve();
            if (file.exists()) {
                loader.setRepository(file);
            } else {
                throw new AppLoaderConfigException("Property '" + name + "' needs existing directory as value: " + val);
            }
            name = "apploader.package.include.";
            enm = props.propertyNames();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                if (key.startsWith(name)) {
                    val = getProperty(props, key);
                    loader.includePackage(val);
                }
            }
            name = "apploader.package.exclude.";
            enm = props.propertyNames();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                if (key.startsWith(name)) {
                    val = getProperty(props, key);
                    loader.excludePackage(val);
                }
            }
            name = "apploader.class.exclude.";
            enm = props.propertyNames();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                if (key.startsWith(name)) {
                    val = getProperty(props, key);
                    loader.excludeClass(val);
                }
            }
            String exName = "apploader.traverse.excludepackage";
            String inName = "apploader.traverse.includeclass";
            enm = props.propertyNames();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                if (key.startsWith(exName) || key.startsWith(inName)) {
                    val = props.getProperty(key);
                    if (key.startsWith(exName)) {
                        loader.excludeTraversePackage(val);
                    } else if (key.startsWith(inName)) {
                        loader.includeTraverseClass(val);
                    }
                }
            }
        } catch (AppLoaderConfigException x) {
            CAT.error("Error while reading AppLoader configuration.", x);
            loader.setEnabled(false);
        }
    }

    private static String getProperty(Properties props, String key) throws AppLoaderConfigException {
        String val;
        val = props.getProperty(key);
        if (val == null) {
            throw new AppLoaderConfigException("Property '" + key + "' needed.");
        }
        return val;
    }
}
