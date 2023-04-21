package com.ilog.translator.java2cs.popup.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.Workbench;
import com.ilog.translator.java2cs.configuration.GlobalOptions;
import com.ilog.translator.java2cs.configuration.TranslatorProjectOptions;
import com.ilog.translator.java2cs.plugin.Messages;
import com.ilog.translator.java2cs.plugin.util.ProjectBuilder;

public class ConvertPackage extends AbstractHandler {

    private QualifiedName path = new QualifiedName("html", "path");

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
        Object firstElement = selection.getFirstElement();
        if (firstElement instanceof PackageFragment) {
            PackageFragment pf = (PackageFragment) firstElement;
            IJavaProject project = pf.getJavaProject();
            try {
                ICompilationUnit[] icus = pf.getCompilationUnits();
                translate(project, icus);
            } catch (JavaModelException e) {
            }
        } else if (firstElement instanceof ICompilationUnit) {
            ICompilationUnit icu = (ICompilationUnit) firstElement;
            IJavaProject project = icu.getJavaProject();
            translate(project, new ICompilationUnit[] { icu });
        } else if (firstElement instanceof IFile) {
            IFile icu = (IFile) firstElement;
            IJavaProject project = (IJavaProject) icu.getProject();
        } else {
        }
        return null;
    }

    public void translate(final IJavaProject javaProject, ICompilationUnit[] compilationUnits) {
        try {
            final GlobalOptions globalOptions = new GlobalOptions();
            final String translatorDirectory = TranslatorProjectOptions.searchTranslatorDir(javaProject, globalOptions, true, false, null);
            final String confFileName = TranslatorProjectOptions.getOrCreateTranslatorConfigurationFile(javaProject, globalOptions, true, translatorDirectory, false);
            final TranslatorProjectOptions options = new TranslatorProjectOptions(confFileName, globalOptions);
            options.read(javaProject, false);
            globalOptions.getLogger().logInfo("--- Translating project '" + javaProject.getProject().getName() + "' to directory '" + options.getSourcesDestDir() + "'");
            if (compilationUnits != null) {
                List<String> res = new ArrayList<String>();
                for (ICompilationUnit icu : compilationUnits) {
                    final IType primaryType = icu.findPrimaryType();
                    if (primaryType == null) return;
                    final IPackageFragment frag = primaryType.getPackageFragment();
                    String packageName = "";
                    if (frag != null) {
                        packageName = frag.getElementName() + ".";
                    }
                    final String className = packageName + primaryType.getElementName();
                    res.add(className);
                }
                options.setClassFilter(res.toArray(new String[res.size()]));
            }
            final IRunnableWithProgress runnable = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) {
                    try {
                        boolean res = ProjectBuilder.copyAndTranslate(javaProject, options, true, monitor, false);
                    } catch (Exception e) {
                        options.getGlobalOptions().getLogger().logException("Error ", e);
                    }
                }
            };
            final ProgressMonitorDialog pmd = new ProgressMonitorDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell());
            pmd.run(true, true, runnable);
        } catch (final InterruptedException e2) {
            System.err.println("Translation interrupt by user");
        } catch (final Exception e) {
            System.err.println(Messages.getString("ProjectTransferHandler.error_copying_projects") + e);
        }
    }

    protected String getPersistentProperty(IResource res, QualifiedName qn) {
        try {
            return (String) res.getPersistentProperty(qn);
        } catch (CoreException e) {
            return "";
        }
    }

    protected void setPersistentProperty(IResource res, QualifiedName qn, String value) {
        try {
            res.setPersistentProperty(qn, value);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
