package org.arastreju.core.modelling.statements;

import org.arastreju.api.ArastrejuGate;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.SemanticNode;
import org.arastreju.api.ontology.model.sn.SNResource;
import org.arastreju.core.modelling.impl.AbstractModellingConversation;

/**
 * 
 * ModellingConversation directly committing any changes against database
 * 
 * Created: 22.08.2008
 *
 * @author Oliver Tigges
 */
public class AttachedModellingConversation extends AbstractModellingConversation {

    public AttachedModellingConversation(ArastrejuGate gate) {
        super(gate);
    }

    public void addToDiagram(SemanticNode node) {
        model.addSemanticNode(node);
        diagram.addMember(node);
        getContentManagementService().storeDiagram(diagram);
    }

    public void removeFromDiagram(SemanticNode node) {
        model.deleteNode(node);
        diagram.removeMember(node);
        getContentManagementService().storeDiagram(diagram);
    }

    public void addNode(SemanticNode node) {
        if (!node.isPersistent()) {
            getOntologyService().storeSemanticNode(node);
        }
        model.addSemanticNode(node);
    }

    public void updateNode(SemanticNode node) {
        getOntologyService().storeSemanticNode(node);
        model.update(node);
    }

    public void addAssociation(Association assoc) {
        if (!assoc.isPersistent()) {
            getOntologyService().storeAssociation(assoc);
        }
        model.addAssociation(assoc);
    }

    public boolean deleteNode(SemanticNode node) {
        getOntologyService().deleteSemanticNode(node);
        return model.deleteNode(node);
    }

    public boolean deleteAssociation(Association association) {
        final SNResource supplier = association.getSupplier();
        supplier.removeAssociation(association);
        getOntologyService().storeSemanticNode(supplier);
        return model.deleteAssociation(association);
    }

    public void resetNode(SemanticNode node) {
        throw new UnsupportedOperationException("attached modelling conversion does not support reset!");
    }
}
