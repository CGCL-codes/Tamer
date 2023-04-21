package org.arastreju.api.ontology.model.terms;

import org.arastreju.api.ontology.model.SemanticNode;
import org.arastreju.api.ontology.model.attachments.ResourceAttachment;
import org.arastreju.api.terminology.WordDefinition;

/**
 * Link between a WordDefinition and a SemanticNode.
 * 
 * Created: 11.07.2008 
 *
 * @author Oliver Tigges
 */
public interface TermLink extends ResourceAttachment {

    WordDefinition getWordDefinition();

    SemanticNode getSemanticNode();

    boolean isPersistent();
}
