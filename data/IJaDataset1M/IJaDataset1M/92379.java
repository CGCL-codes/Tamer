package org.columba.ristretto.io;

import java.io.IOException;

/**
 * Interface definition of Sources.
 * These are the basic data units that the
 * parsers of Ristretto work on.
 * 
 * @author tstich
 *
 */
public interface Source extends CharSequence {

    /**
	 * Creates a sub source of this source.
	 * 
	 * @param start the start position
	 * @param end the end position
	 * @return the subsource
	 */
    public Source subSource(int start, int end);

    /**
	 * Creates a sub source starting from the actual position.
	 * 
	 * @return the subsource
	 */
    public Source fromActualPosition();

    /**
	 * Get the actual read position of the source. 
	 * 
	 * @return the actual read position
	 */
    public int getPosition();

    /**
	 * Seek to the given position
	 * 
	 * @param position
	 * @throws IOException
	 */
    public void seek(int position) throws IOException;

    /**
	 * Returns the character at the given position
	 * and increases the position.
	 * 
	 * @return the character at the actual position
	 * @throws IOException
	 */
    public char next() throws IOException;

    /**
	 * Checks if the end of the Source is reached.
	 * 
	 * @return <code>true</code> if the end of the source is reached
	 */
    public boolean isEOF();

    /**
	 * Closes the source.
	 * 
	 * @throws IOException
	 */
    public void close() throws IOException;

    /**
	 * Does a deep close of all sources and subsources
	 * associated with this source.
	 * 
	 * @throws IOException
	 */
    public void deepClose() throws IOException;
}
