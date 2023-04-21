package astcentric.structure.validation;

import java.util.HashMap;
import java.util.Map;
import astcentric.structure.basic.Node;
import astcentric.structure.tool.RecursiveCompilationContext;

public class ValidatorCompilationContext extends RecursiveCompilationContext {

    private final Map<Node, Node> _bindings = new HashMap<Node, Node>();

    public Node getBoundedNode(Node keyNode) {
        return _bindings.get(keyNode);
    }

    public void bind(Node keyNode, Node boundedNode) {
        _bindings.put(keyNode, boundedNode);
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (_bindings.size() > 0) {
            result += " bindings: " + _bindings;
        }
        return result;
    }
}
