package es.caib.zkib.datamodel.xml.definition;

import java.util.HashMap;
import org.w3c.dom.Element;
import es.caib.zkib.datamodel.DataContext;
import es.caib.zkib.datamodel.DataNode;
import es.caib.zkib.datamodel.xml.ParseException;
import bsh.EvalError;
import bsh.Interpreter;

public class ModelDefinition implements DefinitionInterface {

    HashMap nodes = new HashMap();

    public ModelDefinition() {
        super();
    }

    public NodeDefinition getNode(String tag) {
        return (NodeDefinition) nodes.get(tag);
    }

    /**
	 * @param nodes The nodes to set.
	 */
    public void add(NodeDefinition node) {
        nodes.put(node.getName(), node);
    }

    public void test(Element element) throws ParseException {
        if (nodes.isEmpty()) throw new ParseException("No datanode especified", element);
    }
}
