package nl.utwente.ewi.portunes.model.algorithm.logicalnode;

/**
* These LogicalNodes are the connection between the precondition leaves and the TopNodes.
* This ANDNode needs a certain amount of children to be satifsied before itself can be satisfied.
*/
public class ANDNode implements LogicalNode {

    public int count;

    private LogicalNode parent;

    public ANDNode(int count, LogicalNode parent) {
        this.count = count;
        this.parent = parent;
    }

    public void childResolved() {
        this.count--;
        if (this.count == 0) {
            this.parent.childResolved();
        }
    }
}
