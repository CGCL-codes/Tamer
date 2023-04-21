package net.sourceforge.chaperon.process.extended;

public class StackNodeSet {

    private StackNode first = null;

    private StackNode current = null;

    private StackNode last = null;

    private StackNode popnode = null;

    public int watchdog = 0;

    public String name;

    public StackNodeSet(String name) {
        this.name = name;
    }

    public void push(StackNode stackNode) {
        watchdog++;
        if (first == null) {
            first = stackNode;
            last = stackNode;
        } else {
            if (popnode == null) {
                last.next = stackNode;
                last = stackNode;
            } else {
                stackNode.next = popnode.next;
                popnode.next = stackNode;
                popnode = stackNode;
            }
        }
    }

    public StackNode pop() {
        if (current == null) current = first; else current = current.next;
        popnode = current;
        return current;
    }

    public void clear() {
        watchdog = 0;
        first = null;
        last = null;
        current = null;
        popnode = null;
    }

    public boolean isEmpty() {
        return current == null ? (first == null) : (current.next == null);
    }

    public boolean exists(State state, StackNode parent, StackNode ancestor) {
        for (StackNode node = first; node != null; node = node.next) if (node.state == state) {
            if (node.parent == parent) return true;
            StackNode follow1 = node.ancestor;
            StackNode follow2 = ancestor;
            while (follow1.state == state) follow1 = follow1.ancestor;
            while (follow2.state == state) follow2 = follow2.ancestor;
            while (follow1.state == follow2.state) {
                if (follow1 == follow2) return true;
                follow1 = follow1.ancestor;
                while (follow1.state == follow1.ancestor.state) follow1 = follow1.ancestor;
                follow2 = follow2.ancestor;
                while (follow2.state == follow2.ancestor.state) follow2 = follow2.ancestor;
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (StackNode node = first; node != null; node = node.next) buffer.append(node.dump() + "\n");
        return buffer.toString();
    }

    public String dump() {
        StringBuffer buffer = new StringBuffer();
        for (StackNode node = first; node != null; node = node.next) buffer.append(node.dump() + "\n");
        return buffer.toString();
    }
}
