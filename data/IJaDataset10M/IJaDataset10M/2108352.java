package pspdash.data.compiler;

import java.util.LinkedList;

public class ListStack implements Stack {

    private LinkedList list = new LinkedList();

    private LinkedList descriptors = new LinkedList();

    public ListStack() {
    }

    public Object pop() {
        descriptors.removeFirst();
        return list.removeFirst();
    }

    public Object push(Object o) {
        return push(o, null);
    }

    public Object push(Object o, Object d) {
        list.addFirst(o);
        descriptors.addFirst(d);
        return o;
    }

    public void clear() {
        list.clear();
        descriptors.clear();
    }

    public boolean empty() {
        return list.isEmpty();
    }

    public Object peek() {
        return list.getFirst();
    }

    public Object peekDescriptor() {
        return descriptors.getFirst();
    }
}
