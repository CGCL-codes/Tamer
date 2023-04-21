package org.clarent.ivyidea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import org.clarent.ivyidea.config.IvyIdeaConfigHelper;
import org.clarent.ivyidea.intellij.IntellijUtils;
import org.clarent.ivyidea.intellij.task.IvyIdeaBackgroundTask;
import org.jetbrains.annotations.NotNull;

/**
 * Action to remove all module libraries that match the name of the
 * IvyIDEA-resolved module.
 *
 * @author Guy Mahieu
 */
public class RemoveAllIvyIdeaModuleLibrariesAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        final Project project = DataKeys.PROJECT.getData(e.getDataContext());
        ProgressManager.getInstance().run(new IvyIdeaBackgroundTask(e) {

            public void run(@NotNull final ProgressIndicator indicator) {
                final Module[] facet = IntellijUtils.getAllModulesWithIvyIdeaFacet(project);
                indicator.setIndeterminate(false);
                for (final Module module : facet) {
                    indicator.setText2("Removing for module " + module.getName());
                    ApplicationManager.getApplication().invokeAndWait(new Runnable() {

                        public void run() {
                            ApplicationManager.getApplication().runWriteAction(new Runnable() {

                                public void run() {
                                    final ModifiableRootModel model = ModuleRootManager.getInstance(module).getModifiableModel();
                                    final LibraryTable moduleLibraryTable = model.getModuleLibraryTable();
                                    final Library[] libraries = moduleLibraryTable.getLibraries();
                                    boolean found = false;
                                    for (final Library library : libraries) {
                                        if (IvyIdeaConfigHelper.isCreatedLibraryName(library.getName())) {
                                            found = true;
                                            moduleLibraryTable.removeLibrary(library);
                                        }
                                    }
                                    if (found) {
                                        model.commit();
                                    } else {
                                        model.dispose();
                                    }
                                }
                            });
                        }
                    }, ModalityState.NON_MODAL);
                }
            }
        });
    }
}
