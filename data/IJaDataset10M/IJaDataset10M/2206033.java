package de.knowwe.diaflux.persistence;

import java.util.List;
import de.d3web.core.knowledge.KnowledgeBase;
import de.d3web.core.knowledge.TerminologyObject;
import de.d3web.core.knowledge.terminology.NamedObject;
import de.d3web.diaFlux.flow.ActionNode;
import de.d3web.diaFlux.flow.NOOPAction;
import de.d3web.diaFlux.flow.Node;
import de.d3web.we.object.NamedObjectReference;
import de.knowwe.core.kdom.Article;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.parsing.Sections;
import de.knowwe.core.report.Message;
import de.knowwe.diaflux.type.DecisionType;
import de.knowwe.diaflux.type.FlowchartType;
import de.knowwe.diaflux.type.NodeType;

/**
 * 
 * @author Reinhard Hatko
 * @created 28.11.2010
 */
public class DecisionNodeHandler extends AbstractNodeHandler {

    public DecisionNodeHandler() {
        super(DecisionType.getInstance());
    }

    @Override
    public boolean canCreateNode(Article article, KnowledgeBase kb, Section<NodeType> nodeSection) {
        return getNodeInfo(nodeSection) != null;
    }

    @Override
    public Node createNode(Article article, KnowledgeBase kb, Section<NodeType> nodeSection, Section<FlowchartType> flowSection, String id, List<Message> errors) {
        Section<NamedObjectReference> objectRef = Sections.findSuccessor(nodeSection, NamedObjectReference.class);
        NamedObject object = NamedObjectReference.getObject(article, objectRef);
        NOOPAction action;
        if (object != null) {
            action = new NOOPAction((TerminologyObject) object);
        } else {
            action = new NOOPAction();
        }
        return new ActionNode(id, action);
    }
}
