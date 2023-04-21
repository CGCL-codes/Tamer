package org.arastreju.core.ontology.binding;

import org.arastreju.api.common.QualifiedName;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.AssociationStereotype;

/**
 * 
 * Transient value object containing all data to create an {@link Association}
 * 
 * Created: 01.08.2008
 *
 * @author Oliver Tigges
 */
public class AssocDecl {

    private QualifiedName supplier;

    private QualifiedName client;

    private QualifiedName predicate;

    private AssociationStereotype stereotype;

    public AssocDecl(QualifiedName supplier, QualifiedName client, AssociationStereotype stereotype) {
        this(supplier, client, stereotype, null);
    }

    public AssocDecl(QualifiedName supplier, QualifiedName client, AssociationStereotype stereotype, QualifiedName predicate) {
        this.supplier = supplier;
        this.client = client;
        this.stereotype = stereotype;
        this.predicate = predicate;
    }

    public QualifiedName getSupplier() {
        return supplier;
    }

    public void setSupplier(QualifiedName supplier) {
        this.supplier = supplier;
    }

    public QualifiedName getClient() {
        return client;
    }

    public void setClient(QualifiedName client) {
        this.client = client;
    }

    /**
	 * @return the predicate
	 */
    public QualifiedName getPredicate() {
        return predicate;
    }

    /**
	 * @param predicate the predicate to set
	 */
    public void setPredicate(QualifiedName predicate) {
        this.predicate = predicate;
    }

    public boolean hasPredicate() {
        return predicate != null;
    }

    public AssociationStereotype getStereotype() {
        return stereotype;
    }

    public void setStereotype(AssociationStereotype stereotype) {
        this.stereotype = stereotype;
    }
}
