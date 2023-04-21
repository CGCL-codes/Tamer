package net.sf.parser4j.parser.entity.parsestack;

import java.util.Arrays;
import java.util.Iterator;
import net.sf.parser4j.parser.service.ParserException;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ParseStackSet extends AbstratParseStackOwner implements Iterable<ParseStack>, Iterator<ParseStack> {

    private static final int INITIAL_SIZE = 16;

    private int size = 0;

    private int[] hashCodeTable = new int[INITIAL_SIZE];

    private ParseStack[] parseStackTable = new ParseStack[INITIAL_SIZE];

    private int ambiguityCount;

    private IParseStackSetListener listener;

    public ParseStackSet(final int ownerIdentifier) {
        super(ownerIdentifier);
    }

    public void setListener(final IParseStackSetListener listener) {
        this.listener = listener;
    }

    public ParseStack get(final ParseStack parseStack) {
        final int hashCode = parseStack.hashCode();
        ParseStack found = null;
        for (int index = 0; found == null && index < size; index++) {
            if (hashCodeTable[index] == hashCode) {
                final ParseStack possible = parseStackTable[index];
                if (parseStack.equals(possible)) {
                    found = possible;
                }
            }
        }
        return found;
    }

    public boolean add(final ParseStack parseStack) {
        final boolean added;
        if (exists(parseStack)) {
            added = false;
        } else {
            internalAdd(parseStack);
            added = true;
        }
        return added;
    }

    /**
	 * 
	 * @param parseStack
	 * @return true if added, else already exist
	 * @throws ParserException
	 */
    public boolean addUnexisting(final ParseStack parseStack) throws ParserException {
        boolean found = false;
        int index = 0;
        while (!found && index < size) {
            found = hashCodeTable[index] == parseStack.hashCode() && parseStackTable[index].equals(parseStack);
            index++;
        }
        if (!found) {
            internalAdd(parseStack);
        }
        return true ^ found;
    }

    private boolean exists(final ParseStack parseStack) {
        final int identifier = parseStack.getIdentifier();
        int index = 0;
        boolean found = false;
        while (!found && index < size) {
            found = parseStackTable[index].getIdentifier() == identifier;
            index++;
        }
        return found;
    }

    private void internalAdd(final ParseStack parseStack) {
        if (parseStackTable.length <= size) {
            parseStackTable = Arrays.copyOf(parseStackTable, parseStackTable.length << 1);
            hashCodeTable = Arrays.copyOf(hashCodeTable, hashCodeTable.length << 1);
        }
        parseStackTable[size] = parseStack;
        hashCodeTable[size] = parseStack.hashCode();
        size++;
        if (parseStack.hasAlternativeForAmbiguity()) {
            ambiguityCount++;
        }
        added(parseStack);
        if (listener != null) {
            listener.changed();
        }
    }

    public void clear() {
        for (int index = 0; index < size; index++) {
            removed(parseStackTable[index]);
            parseStackTable[index] = null;
        }
        size = 0;
        ambiguityCount = 0;
        if (listener != null) {
            listener.changed();
        }
    }

    /**
	 * 
	 * @param stackIdentifier
	 * @return true if empty
	 * @throws ParserException
	 */
    public boolean remove(final int stackIdentifier) throws ParserException {
        int index = 0;
        boolean found = false;
        while (!found && index < size) {
            found = parseStackTable[index].getIdentifier() == stackIdentifier;
            index++;
        }
        if (found) {
            final ParseStack parseStack = parseStackTable[index - 1];
            if (parseStack.hasAlternativeForAmbiguity()) {
                ambiguityCount--;
            }
            removed(parseStack);
            final int length = size - index;
            System.arraycopy(parseStackTable, index, parseStackTable, index - 1, length);
            System.arraycopy(hashCodeTable, index, hashCodeTable, index - 1, length);
            size--;
            parseStackTable[size] = null;
            if (listener != null) {
                listener.changed();
            }
        }
        return size == 0;
    }

    public ParseStack get(final int stackIdentifier) {
        int index = 0;
        while (index < size && parseStackTable[index].getIdentifier() != stackIdentifier) {
            index++;
        }
        final ParseStack found;
        if (index == size) {
            found = null;
        } else {
            found = parseStackTable[index];
        }
        return found;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int index;

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public ParseStack next() {
        return parseStackTable[index++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<ParseStack> iterator() {
        index = 0;
        return this;
    }

    public void toArray(final ParseStack[] parseStacks) {
        System.arraycopy(parseStackTable, 0, parseStacks, 0, size);
    }

    public void ambiguityAdded() {
        ambiguityCount++;
    }

    public boolean hasAlternativeForAmbiguity() {
        return ambiguityCount > 0;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int index = 0; index < size; index++) {
            final ParseStack parseStack = parseStackTable[index];
            builder.append(index);
            builder.append(":\n");
            builder.append(parseStack.toString());
            builder.append('\n');
        }
        return builder.toString();
    }
}
