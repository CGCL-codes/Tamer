package com.google.code.sagetvaddons.sjq.agent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.varia.NullAppender;
import sagex.SageAPI;
import com.google.code.sagetvaddons.sjq.listener.Listener;

public final class Agent {

    private static final File findBaseDir() {
        File plugin = new File("plugins/sjq-agent");
        if (!plugin.exists()) return new File("..");
        return plugin;
    }

    static final File BASE_DIR = findBaseDir();

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        configLog4j();
        Config cfg = Config.get();
        StringBuilder msg = new StringBuilder("SJQv4 Agent (Task Client) v" + cfg.getVersion() + "\n\nThe following scripting engines are available in this task client:\n");
        ScriptEngineManager mgr;
        if (SageAPI.isRemote()) {
            File engines = new File(BASE_DIR + "/engines");
            URLClassLoader clsLoader = null;
            if (engines.isDirectory() && engines.canRead()) {
                Collection<URL> urls = new ArrayList<URL>();
                for (Object f : FileUtils.listFiles(engines, new String[] { "jar" }, false)) urls.add(((File) f).toURI().toURL());
                if (urls.size() > 0) clsLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Agent.class.getClassLoader());
            }
            mgr = new ScriptEngineManager(clsLoader);
        } else mgr = new ScriptEngineManager();
        for (ScriptEngineFactory f : mgr.getEngineFactories()) msg.append("\t" + f.getEngineName() + "/" + f.getEngineVersion() + " " + f.getExtensions() + "\n");
        System.out.println(msg.toString());
        Listener listener = new Listener("com.google.code.sagetvaddons.sjq.agent.commands", cfg.getPort(), Agent.class.getPackage().getName() + ".listener");
        listener.init();
    }

    static void configLog4j() {
        Properties props = new Properties();
        try {
            props.load(new FileReader(new File(BASE_DIR + "/conf/sjqagent.log4j.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        props.setProperty("log4j.appender.sjqAgentApp.File", BASE_DIR + props.getProperty("log4j.appender.sjqAgentApp.File"));
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) Logger.getRootLogger().addAppender(new NullAppender());
        PropertyConfigurator.configure(props);
    }
}
