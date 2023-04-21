package de.hu_berlin.sam.mmunit.suggestion.execution;

import java.util.ArrayList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This Class encapsulates a QVT Runner specialized for generating Suggestions.
 * 
 * @author Tim Hartmann
 * 
 */
public class QVTSuggestionRunner extends AbstractQVTRunner {

    private static QVTSuggestionRunner INSTACE = new QVTSuggestionRunner();

    private QVTSuggestionRunner() {
    }

    /**
	 * If no instance exists yet, one is created.
	 * 
	 * @return the single instance of this QVT runner.
	 */
    public static QVTSuggestionRunner getInstace() {
        return INSTACE;
    }

    /**
	 * @return the list of model Resources, derived from the list of models
	 */
    @Override
    public ArrayList<ArrayList<Resource>> getModelResourceLists() {
        setModelRessources();
        return modelResourceLists;
    }

    /**
	 * Uses the list of models and creates a resource for each, that has none
	 */
    private void setModelRessources() {
        Resource resource;
        ArrayList<Resource> modelResourceList;
        for (ArrayList<EObject> modelList : getModelLists()) {
            modelResourceList = new ArrayList<Resource>();
            for (EObject eObject : modelList) {
                if (eObject.eResource() == null) {
                    resource = ModelResourceManager.createXMIResource("/" + eObject.eClass().getName());
                    resource.getContents().add(eObject);
                } else {
                    resource = eObject.eResource();
                }
                modelResourceList.add(resource);
            }
            modelResourceLists.add(new ArrayList<Resource>(modelResourceList));
        }
    }

    /**
	 * Specialized step() method with parameters for suggestion generation.
	 */
    @Override
    public void transform() {
        transform("generateSuggestions", "target");
    }
}
