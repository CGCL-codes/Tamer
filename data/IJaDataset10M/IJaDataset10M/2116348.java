package pmr.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SCUtils {

    public static IJavaSearchScope buildScope(final IPackageFragmentRoot[] packageRoots) {
        IPackageFragmentRoot[] packageList = packageRoots.clone();
        for (int i = 0; i < packageRoots.length; i++) {
            if (packageRoots[i].isArchive()) {
                packageList[i] = null;
            }
        }
        return SearchEngine.createJavaSearchScope(packageList, false);
    }

    public static boolean isTestMethod(final IMethod method) {
        try {
            return method.getSource().contains("@Test");
        } catch (JavaModelException e) {
            return false;
        }
    }

    public static Shell getShell() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null) return null;
        return window.getShell();
    }

    public static IWorkbenchPage getPage() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        return window.getActivePage();
    }

    public static ICompilationUnit getJavaElement(final IFile file) {
        IJavaElement ije = JavaCore.create(file);
        return (ICompilationUnit) ije.getAncestor(5);
    }
}
