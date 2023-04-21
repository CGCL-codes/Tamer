package com.teaminabox.eclipse.wiki.editors;

import static com.teaminabox.eclipse.wiki.WikiPlugin.wikiPlugin;
import java.util.HashSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import com.teaminabox.eclipse.wiki.util.JavaUtils;

public class JavaContext implements IResourceChangeListener {

    private final WikiDocumentContext context;

    private IJavaProject javaProject;

    private HashSet<String> packages;

    public JavaContext(WikiDocumentContext context) throws CoreException {
        this.context = context;
        initialiseJavaProject();
        listenToResourceChanges();
    }

    private void listenToResourceChanges() {
        if (isInJavaProject()) {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        }
    }

    private void initialiseJavaProject() throws CoreException {
        IProject project = context.getProject();
        if (JavaUtils.isJavaProject(project)) {
            javaProject = JavaCore.create(project);
            loadPackages();
        }
    }

    public IJavaProject getJavaProject() {
        return javaProject;
    }

    private synchronized void loadPackages() throws JavaModelException {
        if (!isInJavaProject()) {
            return;
        }
        packages = new HashSet<String>();
        IPackageFragment[] packageFragments = javaProject.getPackageFragments();
        for (IPackageFragment packageFragment : packageFragments) {
            if (!packageFragment.isDefaultPackage()) {
                packages.add(packageFragment.getElementName());
            }
        }
    }

    public boolean isInJavaProject() {
        return javaProject != null && javaProject.exists();
    }

    public synchronized boolean startsWithPackageName(String text) {
        if (!isInJavaProject()) {
            return false;
        }
        if (packages.contains(text)) {
            return true;
        }
        return hasPackageWithNameStartingWith(text);
    }

    private boolean hasPackageWithNameStartingWith(String text) {
        for (String packageName : packages) {
            if (text.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void resourceChanged(IResourceChangeEvent event) {
        try {
            loadPackages();
        } catch (JavaModelException e) {
            wikiPlugin().log("JavaContext", e);
        }
    }

    public void dispose() {
        if (isInJavaProject()) {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        }
    }
}
