package de.fraunhofer.isst.axbench.resolver;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;

public class PRCResolver extends AbstractReferenceResolver {

    public boolean isResolverFor(Object type) {
        return type == PRCReference.class;
    }

    public IAXLangElement getReferencedElement(AbstractReference uReference) throws NullPointerException {
        IAXLangElement theReferencingElement = uReference.referencingElement;
        String theReferencedElementID = uReference.nodeToken.tokenImage;
        return theReferencingElement.getParent().getReference(uReference.searchRole1).getChild(theReferencedElementID);
    }
}
