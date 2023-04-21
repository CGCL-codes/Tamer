package Lists;

import java.io.Serializable;

public class LList implements Interfaces.ListInterface, Serializable {

    private Node firstNode;

    private long length;

    public LList() {
        removeAll();
    }

    public boolean add(Object o) {
        boolean result = false;
        Node tempNode = new Node(o);
        try {
            if (length == 0) {
                firstNode = tempNode;
                firstNode.next = null;
            } else if (length >= 1) {
                Node lastNode = getNodeAt(length - 1);
                lastNode.next = tempNode;
            }
            length++;
            result = true;
        } catch (Exception e) {
            System.err.print(e.getMessage());
            System.out.println("Add method error");
        }
        return result;
    }

    public boolean add(long index, Object o) {
        boolean isSuccessful = false;
        if ((index < length) & (index >= 0)) {
            Node newNode = new Node(o);
            if (length == 0 && index == 0) {
                newNode.next = (firstNode);
                firstNode = newNode;
            }
            if (length > 0) {
                Node before = getNodeAt(index - 1);
                Node after = getNodeAt(index);
                before.next = (newNode);
                newNode.next = (after);
            }
            length++;
            isSuccessful = true;
        }
        return isSuccessful;
    }

    private Node getNodeAt(long givenPosition) {
        Node currentNode = firstNode;
        for (long i = 0; i < givenPosition; i++) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    public boolean removeAll() {
        try {
            firstNode = null;
            length = 0;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long contains(Object anEntry) {
        long result = -1;
        Node currentNode = firstNode;
        for (long i = 0; i < length; i++) {
            if (anEntry.equals(currentNode.data)) {
                result = i;
            } else currentNode = currentNode.next;
        }
        return result;
    }

    public Object getEntry(long givenPosition) {
        Object result = null;
        if (!isEmpty() && (givenPosition >= 0) && (givenPosition < length)) {
            result = getNodeAt(givenPosition).data;
        }
        return result;
    }

    public long getLength() {
        return length;
    }

    public boolean isEmpty() {
        return (length == 0);
    }

    public Object remove(long givenPosition) {
        Object result = null;
        if (!isEmpty() && (givenPosition >= 0) && (givenPosition < length)) {
            if (givenPosition == 0) {
                result = firstNode.data;
                firstNode = firstNode.next;
            } else {
                Node nodeBefore = getNodeAt(givenPosition - 1);
                Node nodeToRemove = nodeBefore.next;
                Node nodeAfter = nodeToRemove.next;
                nodeBefore.next = nodeAfter;
                result = nodeToRemove.data;
            }
            length--;
        }
        return result;
    }

    public boolean remove(Object o) {
        boolean isSuccessful = false;
        try {
            long tmp = contains(o);
            if (tmp >= 0) {
                remove(tmp);
                isSuccessful = true;
            } else System.out.println("Object not found in list");
            return isSuccessful;
        } catch (Exception e) {
            return isSuccessful;
        }
    }

    public boolean replace(long givenPosition, Object newEntry) {
        boolean isSuccessful = true;
        if (!isEmpty() && (givenPosition >= 0) && (givenPosition < length)) {
            Node desiredNode = getNodeAt(givenPosition);
            desiredNode.data = newEntry;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    private class Node implements Serializable {

        private Object data;

        private Node next;

        private Node(Object dataPortion) {
            data = dataPortion;
            next = null;
        }

        private Node(Object dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }
    }
}
