package tudresden.ocl20.pivot.standalone.model;

import java.net.URL;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import tudresden.ocl20.pivot.metamodels.xsd.XSDMetamodelPlugin;
import tudresden.ocl20.pivot.metamodels.xsd.internal.model.XSDModel;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.model.base.AbstractModelProvider;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;

public class StandaloneXSDModelProvider extends AbstractModelProvider implements IModelProvider {

    private static final Logger logger = Logger.getLogger(StandaloneXSDModelProvider.class);

    private ResourceSet resourceSet = null;

    /**
	 * @see tudresden.ocl20.pivot.model.IModelProvider#getModel(java.net.URL)
	 * 
	 * @generated
	 */
    public IModel getModel(URL modelURL) throws ModelAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("getModel(modelURL=" + modelURL + ") - enter");
        }
        URI modelURI;
        IModel model = null;
        Resource resource;
        try {
            modelURI = URI.createURI(modelURL.toString());
        } catch (IllegalArgumentException e) {
            throw new ModelAccessException("Invalid URL: " + modelURL, e);
        }
        resource = getResourceSet().getResource(modelURI, false);
        if (resource == null) {
            resource = getResourceSet().createResource(modelURI);
        }
        model = new XSDModel(getResourceSet().getResource(modelURI, false), StandaloneFacade.INSTANCE.getStandaloneMetamodelRegistry().getMetamodel(XSDMetamodelPlugin.ID));
        if (logger.isDebugEnabled()) {
            logger.debug("getModel() - exit - return value=" + model);
        }
        return model;
    }

    /**
	 * Helper method that lazily creates a resource set.
	 * 
	 * @return
	 */
    protected ResourceSet getResourceSet() {
        if (resourceSet == null) {
            resourceSet = new ResourceSetImpl();
        }
        return resourceSet;
    }
}
