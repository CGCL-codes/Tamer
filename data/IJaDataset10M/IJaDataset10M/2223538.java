package com.boxysystems.libraryfinder.view.intellij.actions;

import com.boxysystems.libraryfinder.view.intellij.PathUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.ui.Messages;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Siddique Hameed
 * Date: Oct 2, 2006
 * Time: 2:29:35 PM
 */
public class AddToModuleClasspathAction extends AbstractAction {

    private Project project;

    Module module = null;

    private List<String> libraryPaths;

    public AddToModuleClasspathAction(Project project, Module module, List<String> libraryPaths) {
        super("Add To '" + module.getName() + "' Classpath");
        this.project = project;
        this.module = module;
        this.libraryPaths = libraryPaths;
    }

    public void actionPerformed(ActionEvent e) {
        if (module != null && libraryPaths != null) {
            addLibraryToClasspath(module);
        }
    }

    private boolean isLibraryAlreadyInClasspath(LibraryTable libraryTable, String libURL) {
        Library[] existingLibraries = libraryTable.getLibraries();
        for (Library existingLibrary : existingLibraries) {
            String[] urls = existingLibrary.getRootProvider().getUrls(OrderRootType.CLASSES);
            for (String existingUrl : urls) {
                if (existingUrl.equalsIgnoreCase(libURL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addLibraryToClasspath(final Module module) {
        Application application = ApplicationManager.getApplication();
        application.runWriteAction(new Runnable() {

            public void run() {
                try {
                    final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
                    final ModifiableRootModel rootModel = rootManager.getModifiableModel();
                    for (String libraryPath : libraryPaths) {
                        String escapedLibraryURL = PathUtil.escapeLibraryURL(libraryPath);
                        final LibraryTable libraryTable = rootModel.getModuleLibraryTable();
                        addLibrary(libraryTable, libraryPath, escapedLibraryURL);
                    }
                    if (rootModel.isWritable()) {
                        rootModel.commit();
                    } else {
                        Messages.showErrorDialog(project, "The project configuration doesn't allow adding libraries to the classpath!", "Error");
                    }
                } catch (Throwable e) {
                    Messages.showErrorDialog(project, "Error while adding library to classpath: " + e, "Error");
                }
            }
        });
    }

    private void addLibrary(LibraryTable libraryTable, String libraryPath, String escapedLibraryURL) {
        if (!isLibraryAlreadyInClasspath(libraryTable, escapedLibraryURL)) {
            Library library = libraryTable.createLibrary();
            Library.ModifiableModel modifiableModel = library.getModifiableModel();
            modifiableModel.addRoot(escapedLibraryURL, OrderRootType.CLASSES);
            modifiableModel.commit();
        } else {
            Messages.showWarningDialog(project, "Library '" + libraryPath + "' already exists in classpath!", "Warning");
        }
    }
}
