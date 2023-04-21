package org.vexi.vexidev.build;

import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.vexi.vexidev.VexiProject;
import org.vexi.vexidev.VexidevPlugin;
import org.vexi.vexidev.outline.OutlineXMLParser;
import org.vexi.vexidev.outline.ParsedItem;
import org.vexi.vexidev.util.visit.FileVisitor;
import org.vexi.vexidev.util.visit.VisitUtil;

public class VexiBuilder extends IncrementalProjectBuilder {

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        System.err.println("VEXI BUILDER inited");
        super.setInitializationData(config, propertyName, data);
    }

    @Override
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            performFullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                performFullBuild(monitor);
            } else {
                int n = VisitUtil.countVexiSourceFiles(monitor, delta);
                monitor.beginTask("Checking...", n);
                ParseChecker visitor = new ParseChecker(monitor);
                delta.accept(visitor);
            }
        }
        return null;
    }

    static class ParseChecker extends FileVisitor {

        public ParseChecker(IProgressMonitor monitor) {
            super(monitor);
        }

        void deleteMarkers(IResource resource) throws CoreException {
            resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        }

        void reportError(IResource resource, int line, String msg) throws CoreException {
            IMarker m = resource.createMarker(IMarker.PROBLEM);
            m.setAttribute(IMarker.LINE_NUMBER, line);
            m.setAttribute(IMarker.MESSAGE, msg);
            m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
        }

        void reportError(IFile file, ParsedItem item) throws CoreException {
            if (item instanceof ParsedItem.Node) {
                ParsedItem.Node n = (ParsedItem.Node) item;
                for (ParsedItem c : n.getChildren()) {
                    reportError(file, c);
                }
            } else if (item instanceof ParsedItem.Error) {
                ParsedItem.Error err = (ParsedItem.Error) item;
                reportError(file, err.getLine(), err.msg);
            }
        }

        public void visitProject(VexiProject vproj) throws CoreException {
            vproj.checkPath();
        }

        public void visitUpdated(IFile file) throws CoreException {
            deleteMarkers(file);
            try {
                ParsedItem item = OutlineXMLParser.parse(file.getName(), file.getContents());
                reportError(file, item);
            } catch (Exception e) {
                throw new CoreException(new Status(IStatus.ERROR, VexidevPlugin.getPluginID(), "Problem parsing file", e));
            }
        }

        public void visitRemoved(IFile file) throws CoreException {
            deleteMarkers(file);
        }
    }

    private void performFullBuild(IProgressMonitor monitor) throws CoreException {
        IProject project = getProject();
        if (project != null) {
            ParseChecker visitor = new ParseChecker(monitor);
            VexiProject vproject = (VexiProject) project.getAdapter(VexiProject.class);
            int n = VisitUtil.countVexiSourceFiles(monitor, vproject);
            monitor.beginTask("Checking...", n);
            visitor.visitProject(vproject);
            VisitUtil.visitVexiSourceFiles(visitor, vproject);
        }
        monitor.done();
    }
}
