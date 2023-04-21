package de.fraunhofer.isst.axbench.resolver;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.syntaxtree.NodeToken;
import de.fraunhofer.isst.axbench.axlang.utilities.MetaInformation;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;

/**
 * @brief path expression reference: Reference, Reference, zero one or two Parents, Child
 * @author mgrosse
 * @author ekleinod
 * @version 0.9.0
 * @since 0.8.0
 */
public class RRPoptPoptCReference extends AbstractReference {

    public RRPoptPoptCReference(IAXLangElement theReferencingElement, NodeToken theReferenceToken, Role theRole, Generation theGeneration, Role theSearchRole1, Role theSearchRole2, String theFileName, MetaInformation theMetaInformation) {
        super(theReferencingElement, theRole, theReferenceToken, theFileName, theMetaInformation);
        generation = theGeneration;
        searchRole1 = theSearchRole1;
        searchRole2 = theSearchRole2;
    }
}
