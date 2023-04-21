package com.arsenal.plugin;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import com.arsenal.log.Log;
import com.arsenal.observer.Observer;
import com.arsenal.util.Util;
import com.arsenal.observer.IHandler;

public class PlugInLoader {

    public static int SERVER = 0;

    public static int CLIENT = 1;

    private int loadType;

    private static PlugInLoader instance = new PlugInLoader();

    public static PlugInLoader getInstance() {
        if (instance == null) {
            instance = new PlugInLoader();
        }
        return instance;
    }

    public PlugInLoader() {
    }

    public void setLoadType(int loadType) {
        this.loadType = loadType;
    }

    public void loadAllPlugIns(int loadType) {
        Log.debug(this, "loadAllPlugIns(): start loading plugins");
        File plugInDir = new File("./plugin");
        String plugInDirName = "plugin";
        String plugInPropertyFileName = "plugin.properties";
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        File[] files = plugInDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.debug(this, "loadAllPlugIns(); plug in found: " + files[i]);
            File f = new File(files[i].getPath());
            Log.debug(this, "loadAllPlugIns(); plug in file path: " + f.getPath());
            if (f.exists() && f.isDirectory() && (f.getName().indexOf("classes") == -1)) {
                Log.debug(this, "loadAllPlugIns(); directory: " + f.getPath());
                File plugInProps = new File(f.getPath() + File.separator + plugInPropertyFileName);
                if ((plugInProps != null) && plugInProps.exists() && plugInProps.isFile()) {
                    assignPlugInProperties(plugInProps.getPath(), loadType);
                }
            }
        }
        Log.debug(this, "loadAllPlugIns(): finished loading plugins in directory:" + plugInDir.getPath());
    }

    private void assignPlugInProperties(String filename, int loadType) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        try {
            Log.info(this, "assignPlugInProperties; filename: " + filename + "|" + loadType);
            String name = null;
            String handlerClassName = null;
            Object handlerClass = null;
            Properties plugInProps = new Properties();
            FileInputStream fis = new FileInputStream(filename);
            plugInProps.load(fis);
            name = plugInProps.getProperty("name");
            if (loadType == SERVER) handlerClassName = plugInProps.getProperty("serverhandlerclassname"); else handlerClassName = plugInProps.getProperty("clienthandlerclassname");
            Log.debug(this, "assignPlugInProperties: " + name + "|" + handlerClassName);
            if (handlerClassName != null) {
                Log.debug(this, "assignPlugInProperties: attempt to load: " + handlerClassName);
                Class clazz = Class.forName(handlerClassName);
                Log.debug(this, "assignPlugInProperties: attempt to instantiate: " + handlerClassName + "|" + clazz);
                handlerClass = clazz.newInstance();
                Log.debug(this, "assignPlugInProperties: handlerClass: " + handlerClass);
            }
            Log.debug(this, "assignPlugInProperties: prepare to register new handler: " + name + "|" + handlerClass);
            if ((name != null) && (handlerClass != null)) registerObserver(name, (IHandler) handlerClass);
        } catch (Exception e) {
            Log.debug(this, "assignPlugInProperties: caught exception while trying to load plugins");
            Log.debug(this, e.getMessage(), e);
        }
    }

    private void registerObserver(String name, IHandler handler) {
        Log.info(this, "Attempt to register new Observer: " + name + "|" + handler);
        Observer.getInstance().registerListener(name, handler);
        Log.info(this, "registered new Observer: " + name + "|" + handler);
        initPlugIn(handler);
    }

    private void initPlugIn(IHandler handler) {
        handler.init();
    }
}
