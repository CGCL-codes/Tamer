package eclant.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.resources.FileResource;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import eclant.eclipse.jdt.ClasspathVisitor;

public class FileSetClasspathVisitor implements ClasspathVisitor {

    public static enum EntryKind {

        project, con, lib, src, output
    }

    public static interface EntrySelector {

        public boolean isSelected(EntryKind kind, File library, EntryKind srcKind, IClasspathContainer container);

        public boolean isSelected(EntryKind kind, IResource resource, EntryKind srcKind, IClasspathContainer container);
    }

    private final IWorkspaceRoot root;

    private final ProjectComponent comp;

    private final List<FileResource> files = new ArrayList<FileResource>();

    private final Set<String> visited = new HashSet<String>();

    private final EntrySelector select;

    private final boolean failOnError;

    private final boolean traverse;

    private ContextNode context = null;

    public FileSetClasspathVisitor(EntrySelector select, ProjectComponent logTo, boolean failOnError, boolean traverseProjects) {
        this.root = ResourcesPlugin.getWorkspace().getRoot();
        this.comp = logTo;
        this.select = select;
        this.failOnError = failOnError;
        this.traverse = traverseProjects;
    }

    public List<FileResource> results() {
        return Collections.unmodifiableList(files);
    }

    @Override
    public void externalLibrary(IPath name, boolean encountered) {
        File libFile = name.toFile();
        if (!encountered && select.isSelected(EntryKind.lib, libFile, context.kind, context.container)) {
            if (libFile.isDirectory()) addClassFolder(libFile, libFile); else files.add(new FileResource(libFile.getParentFile(), libFile.getPath()));
        }
    }

    @Override
    public void library(IFolder classFolder, boolean encountered) {
        if (!encountered && select.isSelected(EntryKind.lib, classFolder, context.kind, context.container)) {
            addClassFolder(classFolder, classFolder);
        }
    }

    @Override
    public void library(IFile jarFile, boolean encountered) {
        if (!encountered && select.isSelected(EntryKind.lib, jarFile, context.kind, context.container)) {
            files.add(workspaceResource(jarFile.getParent(), jarFile));
        }
    }

    @Override
    public boolean enter(IClasspathContainer container) {
        context = new ContextNode(EntryKind.con, container, context);
        return true;
    }

    @Override
    public boolean enter(IJavaProject project) {
        if (!traverse && context != null) return false;
        context = new ContextNode(EntryKind.project, null, context);
        IFolder outputFolder;
        try {
            outputFolder = root.getFolder(project.getOutputLocation());
            if (visited.add(outputFolder.getFullPath().toPortableString()) && select.isSelected(EntryKind.output, outputFolder, EntryKind.project, null)) addClassFolder(outputFolder, outputFolder);
        } catch (JavaModelException e) {
            String message = project.getPath().toPortableString() + ": failed to resolve output location";
            if (failOnError) throw new BuildException(message, e, comp.getLocation()); else comp.getProject().log(message, e, Project.MSG_ERR);
        }
        return true;
    }

    @Override
    public void previouslyEncountered(IJavaProject project) {
    }

    @Override
    public void source(IJavaProject project, IFolder srcFolder, IFolder outputFolder, boolean encountered) {
        if (!encountered && outputFolder != null && visited.add(outputFolder.getFullPath().toPortableString()) && select.isSelected(EntryKind.output, outputFolder, EntryKind.src, null)) addClassFolder(outputFolder, outputFolder);
    }

    @Override
    public void exit() {
        context = context.parent;
    }

    @Override
    public void resolveFailed(IClasspathEntry entry) {
        String message = "Failed to resolve variable: " + entry.getPath().toPortableString();
        if (failOnError) throw new BuildException(message, comp.getLocation()); else comp.log(message, Project.MSG_ERR);
    }

    @Override
    public void resolved(IClasspathEntry entry, IClasspathEntry resolved) {
    }

    private void addClassFolder(File base, File folder) {
        File[] members = folder.listFiles();
        for (File member : members) {
            if (member.isDirectory()) addClassFolder(base, member); else files.add(new FileResource(base, member.getPath()));
        }
    }

    private void addClassFolder(IContainer base, IFolder folder) {
        try {
            IResource[] members = folder.members();
            for (IResource member : members) {
                if (member.getType() == IResource.FOLDER) addClassFolder(base, (IFolder) member); else files.add(workspaceResource(base, member));
            }
        } catch (CoreException e) {
            throw new BuildException(e, comp.getLocation());
        }
    }

    private FileResource workspaceResource(IContainer base, IResource resource) {
        return new FileResource(base.getLocation().toFile(), resource.getLocation().toOSString());
    }

    private static final class ContextNode {

        final EntryKind kind;

        final IClasspathContainer container;

        final ContextNode parent;

        ContextNode(EntryKind kind, IClasspathContainer container, ContextNode parent) {
            this.kind = kind;
            this.container = container;
            this.parent = parent;
        }
    }
}
