package org.danann.cernunnos.xml;

import java.util.Iterator;
import java.util.List;
import org.dom4j.Node;
import org.danann.cernunnos.AbstractContainerTask;
import org.danann.cernunnos.AttributePhrase;
import org.danann.cernunnos.Attributes;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.LiteralPhrase;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;

/**
 * <code>Task</code> implementation that iterates over a node set, performing
 * embedded tasks once for each element in the set.  Unless a 
 * <code>source</code> document is specified in the task definition XML, this 
 * implementation will query the chained-in <code>Node</code> to populate the 
 * list.
 */
public final class NodeIteratorTask extends AbstractContainerTask {

    private Phrase attribute_name;

    private Phrase xpath;

    private Phrase source;

    public static final Reagent ATTRIBUTE_NAME = new SimpleReagent("ATTRIBUTE_NAME", "@attribute-name", ReagentType.PHRASE, String.class, "Optional name under which the new connection will be registered as a request attribute.  If omitted, the name " + "'Attributes.NODE' will be used.", new LiteralPhrase(Attributes.NODE));

    public static final Reagent XPATH = new SimpleReagent("XPATH", "@xpath", ReagentType.PHRASE, String.class, "The XPATH expression this task will iterate over.");

    public static final Reagent SOURCE = new SimpleReagent("SOURCE", "@source", ReagentType.PHRASE, Node.class, "Optional source node to evaluate XPATH against.  If not specified, the value of " + "the request attribute 'Attributes.NODE' will be used.", new AttributePhrase(Attributes.NODE));

    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { ATTRIBUTE_NAME, XPATH, SOURCE, AbstractContainerTask.SUBTASKS };
        final Formula rslt = new SimpleFormula(NodeIteratorTask.class, reagents);
        return rslt;
    }

    public void init(EntityConfig config) {
        super.init(config);
        this.attribute_name = (Phrase) config.getValue(ATTRIBUTE_NAME);
        this.xpath = (Phrase) config.getValue(XPATH);
        this.source = (Phrase) config.getValue(SOURCE);
    }

    public void perform(TaskRequest req, TaskResponse res) {
        Node srcNode = (Node) source.evaluate(req, res);
        List nodes = srcNode.selectNodes((String) xpath.evaluate(req, res));
        String name = (String) attribute_name.evaluate(req, res);
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            Node n = (Node) it.next();
            res.setAttribute(name, n);
            super.performSubtasks(req, res);
        }
    }
}
