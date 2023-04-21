package tm.clc.ast;

public class StatementNodeLink {

    private StatementNode x;

    public void set(StatementNode y) {
        x = y;
    }

    public StatementNode get() {
        return x;
    }

    public void beVisited(StatementNodeVisitor visitor) {
        if (x != null && !visitor.visited(x)) {
            x.beVisited(visitor);
        }
    }
}
