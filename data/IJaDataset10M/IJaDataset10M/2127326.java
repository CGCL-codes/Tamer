package GSim;

/**
 * Linked list for particles and buffers. List is sorted with highest weight first.
 * 
 * @author Fredrik Wahlberg
 * 
 */
public class LinkedList {

    /** Pointer to first element. */
    public Link first;

    /** Pointer to last element. Needed for insertion as last element */
    public Link last;

    public LinkedList() {
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return (first == null);
    }

    public void insertFirst(ComparableData data) {
        Link newLink = new Link(data);
        if (isEmpty()) {
            first = newLink;
            last = newLink;
        } else {
            newLink.next = first;
            first = newLink;
        }
    }

    public void insertLast(ComparableData dd) {
        Link newLink = new Link(dd);
        if (isEmpty()) {
            first = newLink;
            last = newLink;
        } else {
            last.next = newLink;
            last = newLink;
        }
    }

    /**
	 * Get first element (the one with lowest weight)
	 * 
	 * @return ComparableData
	 */
    public ComparableData pop() {
        if (isEmpty()) {
            return null;
        } else {
            Link ret = first;
            if (first == last) {
                first = null;
                last = null;
            } else {
                first = first.next;
            }
            return ret.data;
        }
    }

    public ComparableData top() {
        if (isEmpty()) {
            return null;
        } else {
            Link ret = first;
            return ret.data;
        }
    }

    public int getLength() {
        if (isEmpty()) {
            return 0;
        } else {
            int len = 0;
            Link current = first;
            while (current != null) {
                len++;
                current = current.next;
            }
            return len;
        }
    }

    /**
	 * Sorted insert. Elements are sorted according to weights with smallest
	 * (earliest) first.
	 * 
	 * @param data ComparableData
	 */
    public void insertSorted(ComparableData data) {
        if (isEmpty()) {
            Link newLink = new Link(data);
            first = newLink;
            last = newLink;
        } else {
            if (first.data.comparable >= data.comparable) {
                insertFirst(data);
            } else if (last.data.comparable <= data.comparable) {
                insertLast(data);
            } else {
                Link current = first.next;
                Link trail = first;
                while (current != null) {
                    if (data.comparable <= current.data.comparable) {
                        break;
                    }
                    trail = current;
                    current = current.next;
                }
                Link newLink = new Link(data);
                newLink.next = current;
                trail.next = newLink;
            }
        }
    }

    public String toString() {
        String ret = "[";
        Link current = first;
        while (current != null) {
            ret += current.toString();
            current = current.next;
            if (current != null) {
                ret += "\n ";
            }
        }
        ret += "]";
        return ret;
    }
}

class Link {

    public ComparableData data;

    public Link next;

    public Link(ComparableData d) {
        data = d;
    }

    public String toString() {
        return data.toString();
    }
}
