package edu.pku.sei.pgie.analyzer.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author HeLi
 */
public class SourceTree {

    private static Log logger = LogFactory.getLog(SourceTree.class);

    IJavaProject javaProject;

    public SourceTree(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }

    protected IPackageFragmentRoot[] getFragmentRoots() {
        IClasspathEntry[] entries = new IClasspathEntry[0];
        try {
            entries = javaProject.getResolvedClasspath(true);
        } catch (Exception e) {
            logger.warn("retrieve classpath exception", e);
        }
        List<IPackageFragmentRoot> fragmentRoots = new ArrayList<IPackageFragmentRoot>();
        for (IClasspathEntry entry : entries) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                IPackageFragmentRoot[] roots = javaProject.findPackageFragmentRoots(entry);
                if (roots != null) {
                    for (IPackageFragmentRoot fragmentRoot : roots) {
                        fragmentRoots.add(fragmentRoot);
                    }
                }
            }
        }
        return fragmentRoots.toArray(new IPackageFragmentRoot[fragmentRoots.size()]);
    }

    public void accept(SourceTreeVisitor visitor) throws JavaModelException {
        if (javaProject == null) {
            return;
        }
        accept(javaProject, visitor);
    }

    protected void accept(IJavaElement root, SourceTreeVisitor visitor) throws JavaModelException {
        IJavaElement[] children = null;
        boolean isContinue;
        if (root instanceof IJavaProject) {
            visitor.fireJavaElement(root);
            IPackageFragmentRoot[] fragmentRoots = getFragmentRoots();
            children = new IJavaElement[fragmentRoots.length];
            for (int i = 0; i < fragmentRoots.length; i++) {
                children[i] = fragmentRoots[i];
            }
        } else if (root instanceof IPackageFragmentRoot) {
            visitor.fireJavaElement(root);
            isContinue = visitor.visit((IPackageFragmentRoot) root);
            if (!isContinue) return;
            children = ((IPackageFragmentRoot) root).getChildren();
        } else if (root instanceof IPackageFragment) {
            visitor.fireJavaElement(root);
            isContinue = visitor.visit((IPackageFragment) root);
            if (!isContinue) return;
            children = ((IPackageFragment) root).getChildren();
        } else if (root instanceof ICompilationUnit) {
            visitor.fireJavaElement(root);
            visitor.visit((ICompilationUnit) root);
        }
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                accept(children[i], visitor);
            }
        }
    }
}
