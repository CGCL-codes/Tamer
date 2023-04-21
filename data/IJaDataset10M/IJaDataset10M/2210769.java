package tudresden.ocl20.pivot.language.ocl.resource.ocl.mopp;

import org.eclipse.emf.ecore.resource.Resource;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.IModelRegistry;

/**
 * Mix-in this interface into {@link Resource}s that belong to languages that
 * refer to OCL constructs.
 * 
 * @author Michael Thiele
 * 
 */
public interface IOclResource extends Resource {

    /**
	 * Set the active {@link IModel}. Use this for standalone applications as
	 * otherwise the model can be extracted from the {@link IModelRegistry}.
	 * 
	 * @param model
	 *          can be <code>null</code>
	 */
    public void setModel(IModel model);

    /**
	 * Get the active {@link IModel} for this OCL resource.
	 * 
	 * @return the active {@link IModel} for this OCL resource
	 */
    public IModel getModel();
}
