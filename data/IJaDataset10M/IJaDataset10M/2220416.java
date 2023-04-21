package net.sf.fc.cfg;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.SchemaData;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.copy.CopyScript;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.settings.Settings;

/**
 * AppFactory creates singleton objects. Any object that depends on one of the objects created in AppFactory should
 * take that object as an argument in its constructor. Any object that depends on more than one object created in
 * AppFactory should take AppFactory as an argument in its constructor.
 *
 * @author David Armstrong
 *
 */
public final class AppFactory {

    private static final String fileSep = System.getProperty("file.separator");

    private final File defaultOptionsXml;

    private final File settingsXml;

    private final ScriptHelper<Settings> settingsHelper;

    private final ScriptHelper<OptionsScript> optionsHelper;

    private final ScriptHelper<CopyScript> copyScriptHelper;

    private final SettingsProxy settingsProxy;

    private final OptionsScriptProxy defaultOptionsScriptProxy;

    private final OptionsScript defaultOptionsScript;

    private final RequestFactory requestFactory;

    public AppFactory() throws JAXBException, SAXException, IOException {
        initDirectories();
        defaultOptionsXml = new File(System.getProperty("filecopier.cfg"), "defaultOptions.xml");
        settingsXml = new File(System.getProperty("filecopier.cfg"), "settings.xml");
        settingsHelper = createSettingsHelper();
        optionsHelper = createOptionsHelper();
        copyScriptHelper = createCopyScriptHelper();
        settingsProxy = createSettingsProxy(settingsHelper, settingsXml);
        defaultOptionsScript = OptionsScriptProxy.createDefaultOptionsObjectFromClasspath(optionsHelper);
        defaultOptionsScriptProxy = createDefaultOptionsScriptProxy(optionsHelper, defaultOptionsScript, defaultOptionsXml);
        requestFactory = createRequesFactory(optionsHelper, settingsHelper, defaultOptionsScript, defaultOptionsXml, settingsXml);
    }

    private String initHomeDir() throws IOException {
        System.setProperty("filecopier.home", System.getProperty("user.home") + fileSep + "filecopier");
        File homeDir = new File(System.getProperty("filecopier.home"));
        if ((!homeDir.exists()) && (!homeDir.mkdir())) {
            throw new IOException(homeDir + "directory could not be created.");
        }
        return homeDir.getPath();
    }

    private String initScriptsDir(String homeDir) throws IOException {
        System.setProperty("filecopier.scripts", homeDir + fileSep + "scripts");
        File scriptsDir = new File(System.getProperty("filecopier.scripts"));
        if ((!scriptsDir.exists()) && (!scriptsDir.mkdir())) {
            throw new IOException(scriptsDir + "directory could not be created.");
        }
        return scriptsDir.getPath();
    }

    private String initDefaultCopyScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.copy", scriptsDir + fileSep + "copy");
        File defaultCopyScriptsDir = new File(System.getProperty("filecopier.scripts.copy"));
        if ((!defaultCopyScriptsDir.exists()) && (!defaultCopyScriptsDir.mkdir())) {
            throw new IOException(defaultCopyScriptsDir + "directory could not be created.");
        }
        return defaultCopyScriptsDir.getPath();
    }

    private String initDefaultRestoreScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.restore", scriptsDir + fileSep + "restore");
        File defaultRestoreScriptsDir = new File(System.getProperty("filecopier.scripts.restore"));
        if ((!defaultRestoreScriptsDir.exists()) && (!defaultRestoreScriptsDir.mkdir())) {
            throw new IOException(defaultRestoreScriptsDir + "directory could not be created.");
        }
        return defaultRestoreScriptsDir.getPath();
    }

    private String initDefaultOptionsScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.options", scriptsDir + fileSep + "options");
        File defaultOptionsScriptsDir = new File(System.getProperty("filecopier.scripts.options"));
        if ((!defaultOptionsScriptsDir.exists()) && (!defaultOptionsScriptsDir.mkdir())) {
            throw new IOException(defaultOptionsScriptsDir + "directory could not be created.");
        }
        return defaultOptionsScriptsDir.getPath();
    }

    private String initCfgDir(String homeDir) throws IOException {
        System.setProperty("filecopier.cfg", homeDir + fileSep + "cfg");
        File cfgDir = new File(System.getProperty("filecopier.cfg"));
        if ((!cfgDir.exists()) && (!cfgDir.mkdir())) {
            throw new IOException(cfgDir + "directory could not be created.");
        }
        return cfgDir.getPath();
    }

    private String initLogDir(String homeDir) throws IOException {
        System.setProperty("filecopier.log", homeDir + fileSep + "log");
        File logDir = new File(System.getProperty("filecopier.log"));
        if ((!logDir.exists()) && (!logDir.mkdir())) {
            throw new IOException(logDir + "directory could not be created.");
        }
        return logDir.getPath();
    }

    private void initDirectories() throws IOException {
        String homeDir = initHomeDir();
        String scriptsDir = initScriptsDir(homeDir);
        initDefaultCopyScriptsDir(scriptsDir);
        initDefaultRestoreScriptsDir(scriptsDir);
        initDefaultOptionsScriptsDir(scriptsDir);
        initCfgDir(homeDir);
        initLogDir(homeDir);
    }

    private ScriptHelper<Settings> createSettingsHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);
    }

    private ScriptHelper<OptionsScript> createOptionsHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);
    }

    private ScriptHelper<CopyScript> createCopyScriptHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(CopyScript.class, SchemaData.COPY);
    }

    private SettingsProxy createSettingsProxy(ScriptHelper<Settings> settingsHelper, File settingsXml) throws JAXBException, IOException, SAXException {
        return new SettingsProxy(settingsHelper, settingsXml);
    }

    private OptionsScriptProxy createDefaultOptionsScriptProxy(ScriptHelper<OptionsScript> optionsHelper, OptionsScript defaultOptionsScript, File defaultOptionsXml) throws JAXBException, IOException, SAXException {
        return new OptionsScriptProxy(optionsHelper, defaultOptionsScript, defaultOptionsXml);
    }

    private RequestFactory createRequesFactory(ScriptHelper<OptionsScript> optionsHelper, ScriptHelper<Settings> settingsHelper, OptionsScript defaultOptionsScript, File defaultOptionsXml, File settingsXml) {
        return new RequestFactory(optionsHelper, settingsHelper, defaultOptionsScript, defaultOptionsXml, settingsXml);
    }

    public ScriptHelper<Settings> getSettingsHelper() {
        return settingsHelper;
    }

    public ScriptHelper<OptionsScript> getOptionsHelper() {
        return optionsHelper;
    }

    public ScriptHelper<CopyScript> getCopyScriptHelper() {
        return copyScriptHelper;
    }

    public SettingsProxy getSettingsProxy() {
        return settingsProxy;
    }

    public OptionsScriptProxy getDefaultOptionsScriptProxy() {
        return defaultOptionsScriptProxy;
    }

    public OptionsScript getDefaultOptionsScript() {
        return (OptionsScript) defaultOptionsScript.copyTo(null);
    }

    public File getDefaultOptionsFile() {
        return defaultOptionsXml;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }
}
