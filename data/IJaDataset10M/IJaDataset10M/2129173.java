package com.explosion.expfmodules.javahelp;

import java.io.File;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import com.explosion.expf.Application;
import com.explosion.expf.ExpActionListener;
import com.explosion.expf.ExpModuleManager;
import com.explosion.expf.supportmodules.editorsupport.EditorFactoryAlreadyRegisteredException;
import com.explosion.expf.supportmodules.editorsupport.EditorSupportModule;
import com.explosion.expfmodules.javahelp.project.JavaHelpEditorFactory;
import com.explosion.utilities.ByteUtils;
import com.explosion.utilities.exception.ExceptionManagerFactory;
import com.explosion.utilities.preferences.Preference;

public class JavaHelpModuleManager implements ExpModuleManager {

    private Vector preferences = new Vector();

    private Hashtable preferenceHash = new Hashtable();

    private static JavaHelpModuleManager instance = null;

    private ExpActionListener globalActionListener = null;

    public JavaHelpModuleManager() {
        instance = this;
    }

    public void initialiseGui() throws Exception {
        Application.addToGlobalCookie(JavaHelpConstants.OPEN_COMMAND, 1);
        Application.addToGlobalCookie(JavaHelpConstants.NEW_COMMAND, 1);
    }

    public void showStartUp() {
    }

    public String getPreferenceRoot() {
        return "";
    }

    public String getVersion() {
        return "Version 1.0";
    }

    public String getName() {
        return "Java Help Assistant";
    }

    public String getDescription() {
        return "A tool to assist with creating HelpSet for use in Java programs with JavaHelp.";
    }

    public void initialiseCore(Properties properties) {
        try {
            EditorSupportModule.getInstance().registerEditorFactory(new JavaHelpEditorFactory(), false);
        } catch (EditorFactoryAlreadyRegisteredException e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while initialisng core.");
        }
    }

    public Vector getPreferences() {
        return null;
    }

    public Preference getPreference(String preferenceName) {
        return (Preference) preferenceHash.get(preferenceName);
    }

    public JPanel getPreferencesEditor() {
        return null;
    }

    public static JavaHelpModuleManager instance() {
        return instance;
    }

    public ExpActionListener getGlobalListener() {
        return globalActionListener;
    }

    public static String getFileID(File baseDir, File target) throws Exception {
        Pattern pattern = Pattern.compile(baseDir.toURL().toString());
        Matcher matcher = pattern.matcher(target.toURL().toString());
        String result = matcher.replaceFirst("");
        return ByteUtils.replace('/', '.', result);
    }

    public static String getFileUrl(File baseDir, File target) throws Exception {
        Pattern pattern = Pattern.compile(baseDir.toURL().toString());
        Matcher matcher = pattern.matcher(target.toURL().toString());
        String result = matcher.replaceFirst("");
        return result;
    }
}
