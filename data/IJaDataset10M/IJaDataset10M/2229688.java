package structures.queues;

import java.util.Vector;

public class Stack implements IQueueStructure {

    private Vector list;

    /**
     * Stack constructor
     */
    public Stack() {
        this.list = new Vector();
    }

    /**
     * Returns the number of elements in the stack
     * @return Integer with the number of elements in the stack
     */
    public int size() {
        return this.list.size();
    }

    /**
     * Inserts an object in the stack
     * @param obj Object to insert in the stack
     */
    public void push(Object obj) {
        this.list.add(0, obj);
    }

    /**
     * Returns the object in the top of the stack by removing it
     * @return Object that was on top of the stack
     */
    public Object pop() {
        Object returnobj = top();
        if (returnobj != null) this.list.remove(0);
        return returnobj;
    }

    /**
     * Returns the object on the top of the stack without removing it
     * @return Object currently on top of the stack
     */
    public Object top() {
        if (this.list.size() == 0) return null;
        return this.list.get(0);
    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        stack.push(Integer.valueOf(10));
        stack.push(Integer.valueOf(12));
        stack.push(Integer.valueOf(14));
        System.out.println(stack.size());
        System.out.println(stack.top());
        System.out.println(stack.pop());
        System.out.println(stack.top());
        System.out.println(stack.pop());
        System.out.println(stack.top());
        System.out.println(stack.pop());
        System.out.println(stack.top());
        System.out.println(stack.size());
    }
}
