package flattree.tree;

import flattree.TreeException;
import flattree.flat.ReadLine;
import flattree.flat.WriteLine;
import flattree.process.State;

/**
 * Base class handling common functionality of leaves.
 * 
 * @see #throwOnRead(String, ReadLine)
 * @see #read(ReadLine, State)
 * @see #write(WriteLine, State)
 */
public abstract class AbstractLeaf implements Leaf {

    private boolean throwOnRead = false;

    /**
	 * Should the inability to read from a {@link ReadLine} fail by throwing a
	 * {@link TreeException} or just return <code>false</code> instead.
	 * 
	 * @param throwOnRead
	 * @see #throwOnRead(String, ReadLine)
	 * @see Leaf#read(ReadLine, State)
	 * @see Node#read(ReadLine, State)
	 */
    public void setThrowOnRead(boolean throwOnRead) {
        this.throwOnRead = throwOnRead;
    }

    /**
	 * @see #setThrowOnRead(boolean)
	 */
    public boolean getThrowOnRead() {
        return throwOnRead;
    }

    /**
	 * Convenience mutator supporting method chaining.
	 * 
	 * @see #setThrowOnRead(boolean)
	 */
    public AbstractLeaf throwOnRead() {
        setThrowOnRead(true);
        return this;
    }

    /**
	 * Throw a {@link TreeException} because of a read failure if so configured.
	 * 
	 * @param message
	 *            message of exception
	 * @param line
	 *            the failed line
	 * @throws TreeException
	 *             if configured to throw on read
	 * @see #setThrowOnRead(boolean)
	 */
    protected void throwOnRead(String message, ReadLine line) throws TreeException {
        if (throwOnRead) {
            throw new TreeException(message, line);
        }
    }

    /**
	 * Delegates to {@link #writeImpl(WriteLine, State)} and sets the written
	 * range on the {@link State}.
	 * 
	 * @see State#setRange(Leaf, int, int)
	 */
    public final void write(WriteLine line, State state) {
        int from = line.getIndex();
        writeImpl(line, state);
        int to = line.getIndex();
        state.setRange(this, from, to);
    }

    /**
	 * Do the actual writing.
	 */
    protected abstract void writeImpl(WriteLine line, State state);

    /**
	 * Delegates to {@link #readImpl(ReadLine, State)} and sets the read range
	 * on the {@link State}.
	 * 
	 * @see State#setRange(Leaf, int, int)
	 * @see #setThrowOnRead(boolean)
	 */
    public final boolean read(ReadLine line, State state) {
        int from = line.getIndex();
        if (!readImpl(line, state)) {
            return false;
        }
        int to = line.getIndex();
        state.setRange(this, from, to);
        return true;
    }

    /**
	 * Do the actual reading.
	 */
    protected abstract boolean readImpl(ReadLine line, State state);
}
