package com.google.gdt.eclipse.designer.smart.actions;

import com.google.gdt.eclipse.designer.model.module.ModuleElement;
import com.google.gdt.eclipse.designer.util.DefaultModuleProvider;
import com.google.gdt.eclipse.designer.util.DefaultModuleProvider.ModuleModification;
import com.google.gdt.eclipse.designer.util.ModuleDescription;
import com.google.gdt.eclipse.designer.util.Utils;
import org.eclipse.wb.internal.core.EnvironmentUtils;
import org.eclipse.wb.internal.core.utils.IOUtils2;
import org.eclipse.wb.internal.core.utils.StringUtilities;
import org.eclipse.wb.internal.core.utils.execution.ExecutionUtils;
import org.eclipse.wb.internal.core.utils.execution.RunnableEx;
import org.eclipse.wb.internal.core.utils.jdt.core.ProjectUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.apache.commons.lang.SystemUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * Operation for configuring GWT module for using SmartGWT.
 * 
 * @author scheglov_ke
 * @coverage SmartGWT.actions
 */
public final class ConfigureSmartGwtOperation extends WorkspaceModifyOperation {

    private final ModuleDescription m_module;

    private final IJavaProject m_javaProject;

    private final String m_libraryLocation;

    /**
   * @param libraryLocation
   *          path to the folder with "gxt.jar" file.
   */
    public ConfigureSmartGwtOperation(ModuleDescription module, String libraryLocation) {
        m_module = module;
        m_libraryLocation = libraryLocation;
        m_javaProject = JavaCore.create(m_module.getProject());
    }

    @Override
    protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        ExecutionUtils.runRethrow(new RunnableEx() {

            public void run() throws Exception {
                execute0();
            }
        });
    }

    private void execute0() throws Exception {
        if (!ProjectUtils.hasType(m_javaProject, "com.smartgwt.client.widgets.BaseWidget")) {
            if (EnvironmentUtils.isTestingTime()) {
                ProjectUtils.addExternalJar(m_javaProject, m_libraryLocation + "/smartgwt.jar", null);
                ProjectUtils.addExternalJar(m_javaProject, m_libraryLocation + "/smartgwt-skins.jar", null);
            } else {
                ProjectUtils.addJar(m_javaProject, m_libraryLocation + "/smartgwt.jar", null);
                ProjectUtils.addJar(m_javaProject, m_libraryLocation + "/smartgwt-skins.jar", null);
            }
        }
        DefaultModuleProvider.modify(m_module, new ModuleModification() {

            public void modify(ModuleElement moduleElement) throws Exception {
                moduleElement.addInheritsElement("com.smartgwt.SmartGwt");
            }
        });
        {
            IFile htmlFile = Utils.getHTMLFile(m_module);
            if (htmlFile != null) {
                String content = IOUtils2.readString(htmlFile);
                int scriptIndex = content.indexOf("<script language=");
                if (scriptIndex != -1) {
                    String prefix = StringUtilities.getLinePrefix(content, scriptIndex);
                    String smartGWTScript = "<script> var isomorphicDir = \"" + m_module.getId() + "/sc/\"; </script>";
                    content = content.substring(0, scriptIndex) + smartGWTScript + SystemUtils.LINE_SEPARATOR + prefix + content.substring(scriptIndex);
                    IOUtils2.setFileContents(htmlFile, content);
                }
            }
        }
    }
}
