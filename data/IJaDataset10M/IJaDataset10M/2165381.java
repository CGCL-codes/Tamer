package de.fraunhofer.isst.axbench.resolver;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.applicationmodel.ApplicationModel;
import de.fraunhofer.isst.axbench.axlang.elements.resourcemodel.ResourceModel;
import de.fraunhofer.isst.axbench.axlang.utilities.InstancePath;

/**
 * @brief Methods for the resolution of references
 * 
 * The class contains a set of standard methods to find elements that are given by name (identifier)
 * or identifier path only and attach them to a referencing element.
 * 
 * The information required to determine which strategy to choose and to perform the corresponding method
 * is contained in the UnresolvedReference-parameter object of the method resolveReference().
 * 
 * 
 * @author mgrosse
 * @version 0.8.0
 * @since 0.8.0
 */
public class PPoptRRtoPathResolver extends AbstractReferenceResolver {

    public boolean isResolverFor(Object type) {
        return type == PPoptRRtoPathReference.class;
    }

    /**
	 * @brief looks for the referenced element and attaches it to the referencing element
	 * (both given via the UnresolvedReference-parameter)
	 * 
	 * @param uReference the unresolved reference to be resolved
	 * @return true if the reference has been resolved, else false
	 */
    public IAXLangElement getReferencedElement(AbstractReference uReference) {
        IAXLangElement theReferencingElement = uReference.referencingElement;
        IAXLangElement theReferencedElement = null;
        IAXLangElement theTwoStepModel = null;
        switch(uReference.generation) {
            case PARENT:
                theTwoStepModel = theReferencingElement.getParent().getReference(uReference.searchRole1).getReference(uReference.searchRole2);
                break;
            case GRANDPARENT:
                theTwoStepModel = theReferencingElement.getParent().getParent().getReference(uReference.searchRole1).getReference(uReference.searchRole2);
                break;
            default:
                break;
        }
        IAXLangElement theTwoStepTopComponent = null;
        if (theTwoStepModel instanceof ApplicationModel) {
            theTwoStepTopComponent = ((ApplicationModel) theTwoStepModel).getTopComponent();
        } else {
            if (theTwoStepModel instanceof ResourceModel) {
                theTwoStepTopComponent = ((ResourceModel) theTwoStepModel).getTopComponent();
            }
        }
        theReferencedElement = InstancePath.fromIdentifierList(theReferencingElement, theTwoStepTopComponent, uReference.idPath);
        return theReferencedElement;
    }
}
