package ru.megadevelopers.xboard.support;

import ru.megadevelopers.xboard.core.Message;
import java.util.*;

public class MessageIterator implements Iterator<Message> {

    private transient int modCount = 0;

    private List<Integer> path;

    private int depth;

    private Message current;

    private Message next;

    private boolean initialized;

    private Message root;

    private int expectedModCount = modCount;

    private boolean forward = true;

    /**
	 * Constructor.
	 *
	 * @param root iteration root
	 */
    public MessageIterator(Message root) {
        path = new Vector<Integer>();
        depth = 0;
        this.root = root;
        current = root;
        next = current;
        path.add(0);
        initialized = false;
    }

    /**
	 * If this iterator has more elements.
	 *
	 * @return true, if any elements left
	 * @see #fillNext()
	 */
    public boolean hasNext() {
        checkForComodification();
        if (initialized) {
            if (current == next) {
                fillNext();
            }
        }
        return next != null;
    }

    /**
	 * @return next element
	 * @throws java.util.NoSuchElementException
	 *          if there is no more elements
	 * @see #fillNext()
	 */
    public Message next() {
        checkForComodification();
        if (next == null) {
            throw new NoSuchElementException();
        }
        if (!initialized) {
            initialized = true;
        } else {
            if (current != next) {
                current = next;
            } else {
                fillNext();
                if (next == null) {
                    throw new NoSuchElementException();
                }
            }
        }
        return next;
    }

    /**
	 * @see #next()
	 */
    private void fillNext() {
        checkForComodification();
        int i = depth;
        Message cur = current;
        boolean found = false;
        if (cur.getChildren().size() > 0) {
            int ind = forward ? 0 : cur.getChildren().size() - 1;
            cur = cur.getChildren().get(ind);
            path.add(ind);
            i++;
            found = true;
        } else if (cur == root) {
        } else if ((forward && (cur.getParent().getChildren().size() - getCurrentIndex()) > 1) || ((!forward) && getCurrentIndex() > 0)) {
            int nextChld = getCurrentIndex() + (forward ? 1 : -1);
            cur = cur.getParent().getChildren().get(nextChld);
            path.set(depth, nextChld);
            found = true;
        } else {
            cur = cur.getParent();
            path.remove(i);
            for (i--; i > 0; i--) {
                int nextChld = path.get(i) + (forward ? 1 : -1);
                if ((forward && (cur.getParent().getChildren().size() > nextChld)) || ((!forward) && nextChld >= 0)) {
                    cur = cur.getParent().getChildren().get(nextChld);
                    path.set(i, nextChld);
                    break;
                }
                cur = cur.getParent();
                path.remove(i);
            }
            if (i != 0) found = true;
        }
        depth = i;
        if (found) next = cur; else next = null;
    }

    /**
	 * @return index on current level
	 */
    private int getCurrentIndex() {
        return path.get(depth);
    }

    private void checkForComodification() {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
