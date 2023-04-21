package org.softmed.rest.server.defaults.generation;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import java.io.File;
import java.util.List;
import org.softmed.filehandling.FileUtil;
import org.softmed.rest.generation.HandlerSuite;
import org.softmed.rest.generation.scafold.AppConfig;
import org.softmed.rest.generation.scafold.ModConfig;
import org.softmed.rest.generation.scafold.build.ApplicationProcessor;
import org.softmed.rest.generation.suites.HandlerSuiteManager;
import org.softmed.rest.generation.xml.XMLConverterSuite;
import org.softmed.rest.generation.xml.manager.XMLSuiteManager;
import com.google.inject.Inject;

public class DefaultHandlerSuiteReader implements ApplicationProcessor {

    @Inject
    GroovyScriptEngine groovyScriptEngine;

    @Inject
    GroovyClassLoader groovyClassLoader;

    FileUtil util = new FileUtil();

    String suiteName = "name";

    String suiteVariable = "suite";

    String fileName = "handler.groovy";

    @Override
    public void process(List<AppConfig> apps) throws Throwable {
        HandlerSuiteManager.clear();
        for (AppConfig app : apps) {
            try {
                readSuite(app);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        System.out.println("HandlerSuites Read");
    }

    private void readSuite(AppConfig app) throws Throwable {
        File root = new File(app.getPath());
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) readAppSuite(file, app);
        }
        List<ModConfig> mods = app.getModules();
        for (ModConfig mod : mods) {
            readModuleSuite(app, mod);
        }
    }

    private void readModuleSuite(AppConfig app, ModConfig mod) throws Throwable {
        File root = new File(mod.getPath());
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) readModuleSuite(file, app, mod);
        }
    }

    private void readModuleSuite(File file, AppConfig app, ModConfig mod) throws Throwable {
        String script = util.readFromFile(file);
        List<Class> classes = mod.getClasses();
        String importSnippet = "import org.softmed.rest.generation.HandlerSuite;\n";
        script = importSnippet + script;
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(groovyClassLoader, binding);
        try {
            shell.evaluate(script);
        } catch (Throwable t) {
            System.err.println("Error evaluating script " + file.getName() + " from Module " + mod.getName() + " from Application " + app.getName());
            System.err.println("Groovy Script File Path : " + file.getCanonicalPath());
            t.printStackTrace();
            return;
        }
        String name = null;
        HandlerSuite suite = null;
        try {
            name = (String) binding.getVariable(suiteName);
        } catch (Throwable t) {
        }
        try {
            suite = (HandlerSuite) binding.getVariable(suiteVariable);
        } catch (Throwable t) {
        }
        if (suite != null) HandlerSuiteManager.register(name, suite);
        mod.setHandlerSuite(name);
    }

    private void readAppSuite(File file, AppConfig app) throws Throwable {
        String script = util.readFromFile(file);
        List<Class> classes = app.getClasses();
        String importSnippet = "import org.softmed.rest.generation.HandlerSuite;\n";
        script = importSnippet + script;
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(groovyClassLoader, binding);
        try {
            shell.evaluate(script);
        } catch (Throwable t) {
            System.err.println("Error evaluating script " + file.getName() + " from Application " + app.getName());
            System.err.println("Groovy Script File Path : " + file.getCanonicalPath());
            throw t;
        }
        String name = null;
        HandlerSuite suite = null;
        try {
            name = (String) binding.getVariable(suiteName);
        } catch (Throwable t) {
        }
        try {
            suite = (HandlerSuite) binding.getVariable(suiteVariable);
        } catch (Throwable t) {
        }
        if (suite != null) HandlerSuiteManager.register(name, suite);
        app.setHandlerSuite(name);
    }
}
