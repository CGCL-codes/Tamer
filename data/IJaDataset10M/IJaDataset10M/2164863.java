package net.sourceforge.olympos.oaw.workflow;

import java.util.ArrayList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.openarchitectureware.workflow.WorkflowContext;
import org.openarchitectureware.workflow.issues.Issues;
import org.openarchitectureware.workflow.lib.AbstractWorkflowComponent2;
import org.openarchitectureware.workflow.monitor.ProgressMonitor;

/**
 * Reduces the resource path of all passed models to only the file.
 * 
 * <p>
 * Example: <code>../metamodel/myProfile.profile.uml</code> becomes
 * <code>myProfile.profile.uml</code>
 * </p>
 * 
 * @author Niko <enikao@users.sourceforge.net>
 */
public class ResourcePathRemover extends AbstractWorkflowComponent2 {

    private ArrayList<String> modelSlots = new ArrayList<String>();

    public void addModelSlot(String slot) {
        this.modelSlots.add(slot);
    }

    @Override
    protected void checkConfigurationInternal(Issues issues) {
    }

    @Override
    protected void invokeInternal(WorkflowContext context, ProgressMonitor monitor, Issues issues) {
        for (String modelSlot : this.modelSlots) {
            EObject model = (EObject) context.get(modelSlot);
            Resource resource = model.eResource();
            resource.setURI(URI.createURI(resource.getURI().lastSegment()));
        }
    }
}
