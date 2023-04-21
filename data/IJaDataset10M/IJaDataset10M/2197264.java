package com.google.inject.tools.ideplugin.intellij;

import com.google.inject.tools.ideplugin.GuicePlugin;
import com.google.inject.tools.ideplugin.JavaProject;
import com.google.inject.tools.ideplugin.ModuleSelectionView;
import com.google.inject.tools.ideplugin.results.Results;
import com.google.inject.tools.ideplugin.results.ResultsView;
import com.google.inject.tools.suite.GuiceToolsModule;

/**
 * IntelliJ implementation of the GuicePlugin.
 * 
 * {@inheritDoc GuicePlugin}
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class IntellijGuicePlugin extends GuicePlugin {

    /**
   * Create an IntellijGuicePlugin.
   * 
   * @param module the guice module to inject from
   */
    public IntellijGuicePlugin(IntellijPluginModule module, GuiceToolsModule toolsModule) {
        super(module, toolsModule);
    }

    public IntellijGuicePlugin() {
        this(new IntellijPluginModule(), new IntellijPluginModule.IntellijGuiceToolsModule());
    }

    public static class ResultsViewImpl implements ResultsView {

        public void displayResults(Results results) {
        }
    }

    public static class ModuleSelectionViewImpl implements ModuleSelectionView {

        public void show(JavaProject project) {
        }
    }
}
