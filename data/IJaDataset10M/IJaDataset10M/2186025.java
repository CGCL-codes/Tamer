package org.rubypeople.rdt.internal.debug.ui.launcher;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILauncher;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILauncherDelegate;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiPlugin;
import org.rubypeople.rdt.launching.InterpreterRunner;
import org.rubypeople.rdt.launching.InterpreterRunnerConfiguration;
import org.rubypeople.rdt.launching.InterpreterRunnerResult;

public class RubyApplicationLauncherDelegate implements ILauncherDelegate {

    public RubyApplicationLauncherDelegate() {
    }

    public String getLaunchMemento(Object element) {
        if (element instanceof IFile) {
            return ((IFile) element).getFullPath().toString();
        }
        return null;
    }

    public Object getLaunchObject(String memento) {
        return RdtDebugUiPlugin.getWorkspace().getRoot().getFile(new Path(memento));
    }

    public boolean launch(Object[] elements, String mode, ILauncher launcher) {
        IFile rubyFile = getRubyFile(elements);
        if (rubyFile == null) return false;
        final InterpreterRunner runner = getInterpreterRunner(mode);
        final InterpreterRunnerConfiguration configuration = new InterpreterRunnerConfiguration(rubyFile);
        final InterpreterRunnerResult[] runnerResult = new InterpreterRunnerResult[1];
        IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

            public void run(IProgressMonitor progressMonitor) throws InvocationTargetException {
                progressMonitor.beginTask("Launching Ruby Interpreter", 4);
                runnerResult[0] = runner.run(configuration);
                progressMonitor.done();
            }
        };
        ISourceLocator sourceLocator = new ISourceLocator() {

            public Object getSourceElement(IStackFrame stackFrame) {
                return null;
            }

            ;
        };
        try {
            new ProgressMonitorDialog(RdtDebugUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(true, false, runnableWithProgress);
        } catch (InterruptedException e) {
            return true;
        } catch (InvocationTargetException e) {
            return false;
        }
        ILaunch processLaunch = new Launch(launcher, mode, rubyFile, sourceLocator, runnerResult[0].getProcesses(), runnerResult[0].getDebugTarget());
        registerLaunch(processLaunch);
        return true;
    }

    protected void registerLaunch(final ILaunch launch) {
        getStandardDisplay().syncExec(new Runnable() {

            public void run() {
                DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
            }
        });
    }

    protected Display getStandardDisplay() {
        Display display = Display.getCurrent();
        if (display == null) display = Display.getDefault();
        return display;
    }

    protected IFile getRubyFile(Object[] selectedElements) {
        for (int i = 0; i < selectedElements.length; i++) {
            Object selected = selectedElements[i];
            if (selected instanceof IFile) {
                IFile fileSelected = (IFile) selected;
                if ("rb".equals(fileSelected.getFileExtension())) return fileSelected;
            }
        }
        return null;
    }

    protected InterpreterRunner getInterpreterRunner(String runMode) {
        return new InterpreterRunner();
    }
}
