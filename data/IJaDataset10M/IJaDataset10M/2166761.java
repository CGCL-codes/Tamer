package dataStructures;

/**
 * @author Robert
 *
 */
public class LinkedStack implements StackInterface {

    private ListInterface stack;

    public LinkedStack() {
        stack = new LList();
    }

    public void clear() {
        stack.clear();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public Object peek() {
        return stack.getEntry(1);
    }

    public Object pop() {
        Object topEntry = stack.getEntry(1);
        stack.remove(1);
        return topEntry;
    }

    public void push(Object newEntry) {
        stack.add(1, newEntry);
    }
}
