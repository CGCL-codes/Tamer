package net.simplemodel.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import net.simplemodel.core.ast.SMModuleDeclaration;
import net.simplemodel.core.generator.IGenerator;
import net.simplemodel.core.generator.IGeneratorContext;
import net.simplemodel.core.generator.IGeneratorFactory;
import net.simplemodel.core.generator.IGeneratorOptions;
import net.simplemodel.core.generator.internal.GeneratorContext;
import net.simplemodel.core.generator.internal.GeneratorFactory;
import net.simplemodel.core.internal.SMLogListener;
import net.simplemodel.core.parser.ISimplemodelParser;
import net.simplemodel.core.parser.internal.SimplemodelParserImpl;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;

public class SimplemodelCore extends Plugin {

    private static final ISimplemodelParser simpleModelParser = new SimplemodelParserImpl();

    private static final IGeneratorFactory generatorFactory = new GeneratorFactory();

    public static IGeneratorContext createGeneratorContext(IProject project, SMModuleDeclaration moduleDeclaration) {
        return new GeneratorContext(project, moduleDeclaration);
    }

    private static IEclipsePreferences getEclipsePreferences(IProject project) {
        IScopeContext context = new ProjectScope(project);
        return context.getNode(SimplemodelCore.PLUGIN_ID);
    }

    private static IEclipsePreferences getEclipsePreferencesDefault() {
        IScopeContext context = new DefaultScope();
        return context.getNode(SimplemodelCore.PLUGIN_ID);
    }

    private static Map<String, String> getOptions(IProject project) {
        Map<String, String> result = new TreeMap<String, String>();
        writeOptions(getEclipsePreferencesDefault(), result);
        IEclipsePreferences projectPreferences = getEclipsePreferences(project);
        if (projectPreferences.getBoolean(IGeneratorOptions.USE_PROJECT_SETTINGS, false)) {
            writeOptions(projectPreferences, result);
        } else {
            writeOptions(getEclipsePreferences(), result);
        }
        return result;
    }

    public static ISimplemodelParser getSimpleModelParser() {
        return simpleModelParser;
    }

    private static void writeOptions(IEclipsePreferences preferences, Map<String, String> options) {
        for (String key : IGeneratorOptions.ALL_KEYS) {
            String value = preferences.get(key, null);
            if (value != null) options.put(key, value);
        }
    }

    private SMLogListener logListener;

    public static final String PLUGIN_ID = "net.simplemodel.core";

    private static SimplemodelCore plugin;

    public static IGenerator createGenerator(IProject project) {
        return generatorFactory.createGenerator(project, getOptions(project));
    }

    public static IGenerator createGenerator(IProject project, Map<String, String> options) {
        return generatorFactory.createGenerator(project, options);
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static SimplemodelCore getDefault() {
        return plugin;
    }

    private static IEclipsePreferences getEclipsePreferences() {
        IScopeContext context = new InstanceScope();
        return context.getNode(SimplemodelCore.PLUGIN_ID);
    }

    /**
	 * The constructor
	 */
    public SimplemodelCore() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        IPath location = getStateLocation();
        File configAreaDirectory = location.toFile();
        String logFileName = new SimpleDateFormat("yyyyddmm").format(new Date()) + ".log";
        File logFile = new File(configAreaDirectory, logFileName);
        logListener = new SMLogListener(logFile);
        Platform.addLogListener(logListener);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        Platform.removeLogListener(logListener);
        logListener.stop();
        super.stop(context);
    }
}
