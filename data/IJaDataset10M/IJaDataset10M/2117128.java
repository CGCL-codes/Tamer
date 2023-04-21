package org.objectstyle.wolips.core.resources.internal.build;

import java.util.Map;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.objectstyle.wolips.core.resources.builder.IBuilder;
import org.objectstyle.wolips.core.resources.builder.ICleanBuilder;

/**
 * @author ulrich
 */
public class CleanVisitor extends AbstractBuildVisitor implements IResourceVisitor {

    public CleanVisitor(BuilderWrapper[] builderWrappers, IProgressMonitor _progressMonitor, Map _buildCache) {
        super(builderWrappers, _progressMonitor, _buildCache);
    }

    public boolean visit(IResource resource) throws CoreException {
        boolean visitChildren;
        if (resource == null || isCanceled()) {
            visitChildren = false;
        } else {
            visitChildren = true;
            Map buildCache = getBuildCache();
            IProgressMonitor progressMonitor = getProgressMonitor();
            int woResourceType = getWoResourceType(resource);
            if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_CLASS) {
                this.notifyBuilderHandleClasses(resource, progressMonitor, buildCache);
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_CLASSPATH) {
                this.notifyBuilderHandleOther(resource, progressMonitor, buildCache);
                this.notifyBuilderClasspath(resource, progressMonitor, buildCache);
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_IGNORE) {
                visitChildren = false;
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_OTHER) {
                this.notifyBuilderHandleOther(resource, progressMonitor, buildCache);
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_RESOURCE) {
                this.notifyBuilderHandleResources(resource, progressMonitor, buildCache);
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_SOURCE) {
                this.notifyBuilderHandleSource(resource, progressMonitor, buildCache);
            } else if (woResourceType == AbstractBuildVisitor.WO_RESOURCE_TYPE_WEB_SERVER_RESOURCE) {
                this.notifyBuilderHandleWebServerResources(resource, progressMonitor, buildCache);
            }
        }
        return visitChildren;
    }

    private void notifyBuilderClasspath(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleClasspath(resource, _progressMonitor, _buildCache);
            }
        }
    }

    private void notifyBuilderHandleClasses(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleClasses(resource, _progressMonitor, _buildCache);
            }
        }
    }

    private void notifyBuilderHandleSource(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleSource(resource, _progressMonitor, _buildCache);
            }
        }
    }

    private void notifyBuilderHandleResources(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleWoappResources(resource, _progressMonitor, _buildCache);
            }
        }
    }

    private void notifyBuilderHandleWebServerResources(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleWebServerResources(resource, _progressMonitor, _buildCache);
            }
        }
    }

    private void notifyBuilderHandleOther(IResource resource, IProgressMonitor _progressMonitor, Map _buildCache) {
        BuilderWrapper[] builderWrappers = getBuilderWrappers();
        for (int i = 0; i < builderWrappers.length; i++) {
            IBuilder builder = builderWrappers[i].getBuilder();
            if (builder instanceof ICleanBuilder) {
                ((ICleanBuilder) builder).handleOther(resource, _progressMonitor, _buildCache);
            }
        }
    }
}
