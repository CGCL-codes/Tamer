package de.fraunhofer.isst.axbench.resolver;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;

public class PplusCResolver extends AbstractReferenceResolver {

    public boolean isResolverFor(Object type) {
        return type == PplusCReference.class;
    }

    public IAXLangElement getReferencedElement(AbstractReference uReference) throws NullPointerException {
        IAXLangElement theReferencedElement = null;
        IAXLangElement theReferencingElement = uReference.referencingElement;
        String theReferencedElementID = uReference.nodeToken.tokenImage;
        switch(uReference.generation) {
            case SAME:
                theReferencedElement = theReferencingElement.getParent().getChild(theReferencedElementID);
                break;
            case PARENT:
                theReferencedElement = theReferencingElement.getParent().getParent().getChild(theReferencedElementID);
                break;
            case GRANDPARENT:
                theReferencedElement = theReferencingElement.getParent().getParent().getParent().getChild(theReferencedElementID);
                break;
        }
        return theReferencedElement;
    }
}
