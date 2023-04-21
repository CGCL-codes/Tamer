package org.arastreju.api.ontology.model.sn;

import org.arastreju.api.context.Context;
import org.arastreju.api.ontology.apriori.RDF;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.SemanticNode;

/**
 * A proxy for an entity which is not yet resolved.
 * 
 * Created: 12.09.2008
 * 
 * @author Oliver Tigges
 */
public class SNProxy extends SNEntity {

    public static boolean isProxy(SemanticNode a) {
        return a instanceof SNProxy;
    }

    public SNProxy(Context context) {
        this(SNClass.ROOT_CLASS, context);
    }

    public SNProxy(SNClass classifier, Context context) {
        Association.create(context, this, classifier, RDF.TYPE);
        setName(classifier.toString());
    }

    @Override
    public String toString() {
        return "Proxy(" + getName() + ")";
    }
}
