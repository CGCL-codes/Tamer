package astcentric.structure.validation;

import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeTool;

class HelperNodes {

    private final Node _arguments;

    public HelperNodes(Node helpers) {
        List<Node> nodes = NodeTool.getExportableChildrenOf(helpers);
        _arguments = nodes.get(0);
    }

    Node getArgumentsNode() {
        return _arguments;
    }
}
