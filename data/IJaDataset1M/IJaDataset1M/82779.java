package it.unisannio.rcost.callgraphanalyzer.diagram.part;

import org.eclipse.emf.ecore.EObject;

/**
 * @generated
 */
public class CallGraphNodeDescriptor {

    /**
	 * @generated
	 */
    private EObject myModelElement;

    /**
	 * @generated
	 */
    private int myVisualID;

    /**
	 * @generated
	 */
    private String myType;

    /**
	 * @generated
	 */
    public CallGraphNodeDescriptor(EObject modelElement, int visualID) {
        myModelElement = modelElement;
        myVisualID = visualID;
    }

    /**
	 * @generated
	 */
    public EObject getModelElement() {
        return myModelElement;
    }

    /**
	 * @generated
	 */
    public int getVisualID() {
        return myVisualID;
    }

    /**
	 * @generated
	 */
    public String getType() {
        if (myType == null) {
            myType = CallGraphVisualIDRegistry.getType(getVisualID());
        }
        return myType;
    }
}
