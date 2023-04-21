package org.formaria.editor.install;

import org.formaria.editor.builder.database.DatabaseFormBuilderDndHelper;
import org.formaria.editor.builder.pojo.PojoFormBuilderDndHelper;
import org.formaria.editor.langed.LanguageProjectListener;
import org.formaria.editor.visualizer.VisualiserProjectListener;
import org.formaria.editor.data.DataSourceLoader;
import org.formaria.editor.lm.ModuleRegistrationDialog;
import org.formaria.editor.lm.AriaLicenseManager;
import java.net.URL;
import org.formaria.editor.dnd.PageDesignerTransferHandler;
import org.formaria.editor.lm.LicenseManager;
import org.formaria.editor.project.ProjectListener;
import org.formaria.editor.project.EditorProject;
import org.formaria.editor.project.EditorProjectManager;
import org.formaria.editor.project.pages.components.EditorRegisteredComponentFactory;

/**
 * Check the project initialization and set default properties as needed
 * <p> Copyright (c) Formaria Ltd., 2001-2006, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.8 $</p>
 */
public class AriaProjectCheck implements ProjectListener {

    /** 
   * Creates a new instance of AriaProjectCheck 
   */
    public AriaProjectCheck() {
    }

    /**
   * Check that all the extra properties are configured
   */
    public void checkProject(EditorProject proj) {
    }

    /**
   * Notification of project initialized.
   * @param project the editor project just initialized
   */
    public void projectInitialized(EditorProject project) {
    }

    /**
   * Update to reflect the new project state
   */
    public void projectUpdated(EditorProject proj) {
    }

    /**
   * A project has just been loaded 
   * @param cp the project that has just been loaded
   */
    public void projectLoaded(EditorProject project) {
    }

    public void saveProject(EditorProject project) {
    }

    public void resetProject(String moduleName, EditorProject project) {
    }

    public static void initializeModules(ClassLoader classLoader) {
        LicenseManager licensManager = EditorProjectManager.getLicenseManager();
        if (licensManager == null) EditorProjectManager.setLicenseManager(new AriaLicenseManager());
        if (EditorProjectManager.isUserRegistered(ModuleRegistrationDialog.class.getClassLoader(), "Aria")) {
            EditorProjectManager.resetProject("Aria");
            if (EditorProjectManager.isUserRegistered("Aria SVG Widgets")) {
                URL url2 = classLoader.getResource("resources/svgComponents.xml");
                EditorRegisteredComponentFactory.addConfigFile("Svg", url2, true);
            }
            EditorProjectManager.setDefaultClassLoader(classLoader);
            EditorProjectManager.addProjectListener("AriaProProjectCheck", new AriaProjectCheck(), true);
            EditorProjectManager.addProjectListener("LanguageManager", new LanguageProjectListener(), true);
            EditorProjectManager.addProjectListener("ModelVisualiser", new VisualiserProjectListener(), true);
            EditorProjectManager.addProjectListener("DataSourceLoader", new DataSourceLoader(), true);
            PageDesignerTransferHandler.addDndHelper(new PojoFormBuilderDndHelper());
            PageDesignerTransferHandler.addDndHelper(new DatabaseFormBuilderDndHelper());
        }
    }
}
