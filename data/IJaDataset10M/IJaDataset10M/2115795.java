package com.ecmdeveloper.plugin.scripting.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import com.ecmdeveloper.plugin.scripting.Activator;
import com.ecmdeveloper.plugin.scripting.util.PluginLog;
import com.ecmdeveloper.plugin.scripting.util.PluginMessage;

/**
 * @author ricardo.belfor
 *
 */
public abstract class NewScriptingProjectWizard extends Wizard implements INewWizard {

    private static final String FAILED_MESSAGE = "Creating Project failed";

    protected static final String TITLE = "New Content Engine Scripting Project";

    private NewJavaProjectWizardPageOne wizardPageOne;

    private NewJavaProjectWizardPageTwo wizardPageTwo;

    public NewScriptingProjectWizard() {
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        wizardPageOne = new NewJavaProjectWizardPageOne() {

            @Override
            public IClasspathEntry[] getDefaultClasspathEntries() {
                ArrayList<IClasspathEntry> classpathList = new ArrayList<IClasspathEntry>();
                for (IClasspathEntry classpathEntry : super.getDefaultClasspathEntries()) {
                    classpathList.add(classpathEntry);
                }
                String methodRunnerLocation;
                try {
                    methodRunnerLocation = getMethodRunnerLocation();
                    classpathList.add(JavaCore.newLibraryEntry(new Path(methodRunnerLocation), null, null));
                    classpathList.add(JavaCore.newLibraryEntry(new Path(getBaseMethodRunnerLocation()), null, null));
                    String[] libraries = getLibraries();
                    for (String library : libraries) {
                        classpathList.add(JavaCore.newLibraryEntry(new Path(library), null, null));
                    }
                } catch (Exception e) {
                    PluginLog.error(e);
                }
                return classpathList.toArray(new IClasspathEntry[classpathList.size()]);
            }

            private String getBaseMethodRunnerLocation() throws Exception {
                return Activator.getDefault().getBaseMethodRunnerLocation();
            }
        };
        addPage(wizardPageOne);
        wizardPageTwo = new NewJavaProjectWizardPageTwo(wizardPageOne);
        addPage(wizardPageTwo);
    }

    @Override
    public boolean performFinish() {
        try {
            getContainer().run(true, false, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        wizardPageTwo.performFinish(monitor);
                        IProject project = wizardPageTwo.getJavaProject().getProject();
                        addNature(project, getNatureId());
                    } catch (CoreException e) {
                        PluginMessage.openErrorFromThread(TITLE, FAILED_MESSAGE, e);
                    }
                }
            });
        } catch (InvocationTargetException e) {
            PluginMessage.openErrorFromThread(TITLE, FAILED_MESSAGE, e);
        } catch (InterruptedException e) {
            PluginMessage.openErrorFromThread(TITLE, FAILED_MESSAGE, e);
        }
        return true;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    private void addNature(IProject project, String natureId) {
        if (!project.isOpen()) {
            return;
        }
        IProjectDescription description;
        try {
            description = project.getDescription();
        } catch (CoreException e) {
            PluginLog.error(e);
            return;
        }
        List<String> newIds = new ArrayList<String>();
        newIds.addAll(Arrays.asList(description.getNatureIds()));
        int index = newIds.indexOf(natureId);
        if (index == -1) {
            newIds.add(natureId);
            description.setNatureIds(newIds.toArray(new String[newIds.size()]));
            try {
                project.setDescription(description, null);
            } catch (CoreException e) {
                PluginLog.error(e);
            }
        }
    }

    protected abstract String getNatureId();

    protected abstract String[] getLibraries() throws IOException;

    protected abstract String getMethodRunnerLocation() throws Exception;
}
