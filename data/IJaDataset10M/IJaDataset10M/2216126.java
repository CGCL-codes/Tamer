package org.eclipse.core.internal.content;

/**
 * A common abstract view for lazy character/binary input streams.
 * 
 * @since 3.1
 */
public interface ILazySource {

    /**
	 * @return a boolean indicating whether this stream is character or byte-based 
	 */
    public boolean isText();

    /**
	 * Rewinds the stream.
	 */
    public void rewind();
}
