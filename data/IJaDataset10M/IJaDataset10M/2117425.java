package configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ConfigurationTree {

    private static ConfigurationFile root;

    private static ConfigurationTree instance;

    private static final String CONFIG_FILE = System.getProperty("user.home") + "/" + ".game/files/";

    protected ConfigurationTree() {
    }

    private ConfigurationFile processingFile(File f) {
        List<File> files = new LinkedList<File>();
        List<ConfigurationFile> modules = new LinkedList<ConfigurationFile>();
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                modules.add(processingFile(file));
            } else {
                files.add(file);
            }
        }
        return new ConfigurationFile(f.getName(), modules, files);
    }

    private void readConfig() {
        File f;
        f = new File(CONFIG_FILE);
        root = processingFile(f);
    }

    public static ConfigurationTree getInstance() {
        if (instance == null) {
            instance = new ConfigurationTree();
            instance.readConfig();
        }
        return instance;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        for (ConfigurationFile mod : root.getModules()) {
            buff.append(mod.getModule());
            buff.append("\n");
        }
        return buff.toString();
    }
}
