package dbStruct;

import edu.umd.cs.piccolo.PNode;

public class Table extends Node {

    public Table() {
        super(NodeType.TABLE);
    }

    public Table(String name) {
        super(NodeType.TABLE);
        super.setName(name);
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public PNode getPNode() {
        return super.getNode();
    }

    public void setPNode(PNode node) {
        super.setNode(node);
    }
}
