package org.arastreju.api.ontology.model.sn;

import org.arastreju.api.ontology.model.AssociationResolver;
import org.arastreju.api.ontology.model.Stereotype;
import org.arastreju.api.ontology.model.internal.Vertex;

/**
 * 
 * Representaion of a {@link Stereotype}.
 * 
 * 
 * Created: 23.01.2008
 *
 * @author Oliver Tigges
 *
 */
public class SNStereotype extends SNResource {

    public SNStereotype() {
        super(Stereotype.STEREOTYPE);
    }

    public SNStereotype(Vertex vertex, AssociationResolver resolver) {
        super(vertex, resolver);
    }

    public Stereotype getRepresentedStereotype() {
        return Stereotype.valueOf(getName());
    }
}
