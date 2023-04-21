package uk.ac.warwick.dcs.cokefolk.util.datastructures;

import uk.ac.warwick.dcs.cokefolk.util.text.XMLUtils;
import java.util.*;

/**
 * Contains the command history of the current session (created when the GUI is opened)
 * @author Rachel, Adrian
 * @designer Rachel, Adrian
 */
public class History<T> extends AbstractCollection<T> {

    public static final int DEFAULT_CAPACITY = 200;

    private static final String divider;

    static {
        char dashes[] = new char[76];
        Arrays.fill(dashes, '_');
        divider = new String(dashes);
    }

    private T[] history;

    private int storePointer = 0;

    private int retrievePointer = 0;

    private boolean historyFull = false;

    public History() {
        this(DEFAULT_CAPACITY);
    }

    public History(Collection<T> col) {
        int colSize = col.size();
        if (colSize > DEFAULT_CAPACITY) {
            @SuppressWarnings(value = { "unchecked" }) T[] data = (T[]) new Object[colSize];
            this.history = data;
            int i = 0;
            for (T item : col) {
                history[i] = item;
                i++;
            }
            historyFull = true;
        } else {
            @SuppressWarnings(value = { "unchecked" }) T[] data = (T[]) new Object[DEFAULT_CAPACITY];
            this.history = data;
            addAll(col);
        }
    }

    public History(final int capacity) {
        @SuppressWarnings(value = { "unchecked" }) T[] data = (T[]) new Object[capacity];
        this.history = data;
    }

    @Override
    public boolean add(T obj) {
        if (obj == null) throw new NullPointerException();
        history[storePointer] = obj;
        boolean current = (retrievePointer == storePointer);
        storePointer++;
        if (storePointer >= history.length) {
            historyFull = true;
            storePointer = storePointer - history.length;
        }
        if (current) retrievePointer = storePointer;
        return true;
    }

    @Override
    public ListIterator<T> iterator() {
        return new HistoryIterator<T>(history, storePointer, size(), historyFull);
    }

    public T gotoPoint(int i) throws IndexOutOfBoundsException {
        if (i == 0) {
            retrievePointer = storePointer;
            return null;
        }
        if (!historyFull) {
            if (i > storePointer) {
                throw new IndexOutOfBoundsException(Integer.toString(i));
            } else {
                retrievePointer = storePointer - i;
            }
        } else {
            if (i > history.length) {
                throw new IndexOutOfBoundsException(Integer.toString(i));
            } else if (i <= storePointer) {
                retrievePointer = storePointer - i;
            } else {
                retrievePointer = history.length - (i - storePointer);
            }
        }
        return history[retrievePointer];
    }

    public int currentIndex() {
        if (retrievePointer <= storePointer) {
            return storePointer - retrievePointer;
        } else {
            return history.length - retrievePointer + storePointer;
        }
    }

    @Override
    public int size() {
        if (!historyFull) {
            return storePointer;
        } else {
            return history.length;
        }
    }

    public int capacity() {
        return history.length;
    }

    @Override
    public void clear() {
        int max = size();
        for (int i = 0; i < max; i++) {
            history[i] = null;
        }
        storePointer = 0;
        retrievePointer = 0;
        historyFull = false;
    }

    public String toStringPlain() {
        String lineSep;
        try {
            lineSep = System.getProperty("line.separator");
        } catch (SecurityException e) {
            lineSep = "\n";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < storePointer; i++) {
            String plain = XMLUtils.stripHTML(history[i].toString());
            result.append(plain);
            result.append(divider);
            result.append(lineSep);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < storePointer; i++) {
            result.append(history[i]);
            result.append("<hr>\n");
        }
        return result.toString();
    }

    public boolean hasNext() {
        return (retrievePointer < storePointer - 1 || historyFull);
    }

    public boolean hasPrevious() {
        return (retrievePointer > 0 || historyFull);
    }

    public T next() {
        if (retrievePointer >= storePointer - 1 && !historyFull) {
            throw new NoSuchElementException();
        } else {
            retrievePointer = (retrievePointer + 1) % history.length;
            T result = history[retrievePointer];
            return result;
        }
    }

    public T previous() {
        if (retrievePointer == 0 && !historyFull) {
            throw new NoSuchElementException();
        } else {
            if (retrievePointer == 0) retrievePointer = history.length - 1; else retrievePointer--;
            return history[retrievePointer];
        }
    }

    public T current() {
        return history[retrievePointer];
    }

    public void jumpToEnd() {
        retrievePointer = storePointer;
    }

    public static class HistoryPair<T> extends Pair<String, T> {

        public HistoryPair(String one, T two) {
            super(one, two);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("INPUT: <br>\n");
            result.append(XMLUtils.escapeHTMLCharacters(first));
            result.append("\n<p>\n");
            result.append("RESULT: \n");
            result.append(second);
            result.append("</p>\n");
            return result.toString();
        }
    }
}

class HistoryIterator<T> implements ListIterator<T> {

    private int index;

    private int count;

    private int end;

    private int size;

    private final T[] history;

    HistoryIterator(final T[] data, final int current, final int used, boolean full) {
        this.history = data;
        if (full) index = current; else index = 0;
        if (full) end = current; else end = used;
        size = used;
        count = 0;
    }

    public void add(T value) {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return (count < size);
    }

    public boolean hasPrevious() {
        return (count > 0);
    }

    public T next() {
        if (count < size) {
            T result = history[index];
            index = (index + 1) % size;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    public int nextIndex() {
        return count;
    }

    public T previous() {
        if (count > 0) {
            if (index == 0) index = size - 1; else index--;
            return history[index];
        } else {
            throw new NoSuchElementException();
        }
    }

    public int previousIndex() {
        if (index == 0) return size - 1; else return index - 1;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void set(T value) {
        if (value != null) {
            history[index] = value;
        } else {
            throw new IllegalArgumentException("Cannot insert a null item into a history");
        }
    }

    public void jumpToEnd() {
        index = end;
        count = size;
    }
}
