package policy.branches;

import policy.nodes.Node;

public class PortBranch extends Branch {

    protected PortBranch(String port, Node node) {
        super(port, node);
    }

    public int compareTo(Branch other) {
        PortBranch o = (PortBranch) other;
        if (o.getValue().contains("*") || value.contains("any")) return -1; else if (value.contains("*") || value.contains("any")) return 1; else return 0;
    }
}
