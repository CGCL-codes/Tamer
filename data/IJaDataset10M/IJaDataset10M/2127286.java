package net.sf.eclint.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.eclint.core.EclintCoreModelUtil;
import net.sf.eclint.core.analyzer.EclintCore;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class EclintBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = EclintBuilder.class.getCanonicalName();

    class EclintDeltaVisitor implements IResourceDeltaVisitor {

        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            List<IResource> resources = new ArrayList<IResource>();
            if (CoreModel.getDefault().create(resource).getElementType() == ICElement.C_UNIT) {
                resources.add(resource);
                switch(delta.getKind()) {
                    case IResourceDelta.ADDED:
                    case IResourceDelta.CHANGED:
                        EclintCore.runJob(resource.getProject(), resources);
                        break;
                    case IResourceDelta.REMOVED:
                        break;
                }
            }
            return true;
        }
    }

    class EclintResourceVisitor implements IResourceVisitor {

        public boolean visit(IResource resource) throws CoreException {
            IProject p = resource.getProject();
            EclintCore.runJob(p, EclintCoreModelUtil.getAllTranslationUnits((IContainer) p));
            return false;
        }
    }

    @Override
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        switch(kind) {
            case FULL_BUILD:
                fullBuild();
                break;
            default:
                incrementalBuild(getDelta(getProject()));
                break;
        }
        return null;
    }

    protected void fullBuild() throws CoreException {
        getProject().accept(new EclintResourceVisitor());
    }

    protected void incrementalBuild(IResourceDelta delta) throws CoreException {
        if (delta != null) {
            delta.accept(new EclintDeltaVisitor());
        } else {
            fullBuild();
        }
    }
}
