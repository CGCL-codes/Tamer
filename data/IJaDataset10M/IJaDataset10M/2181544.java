package com.jcompressor.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Scott Carnett
 */
public class ListOfFiles implements Enumeration<InputStream> {

    private List<InputStream> streams;

    private int current = 0;

    /**
	 * The default constructor
	 * @param streams the input streams
	 */
    public ListOfFiles(final List<InputStream> streams) {
        this.streams = streams;
    }

    /**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
    @Override
    public boolean hasMoreElements() {
        return ((this.current < this.streams.size()) ? true : false);
    }

    /**
	 * @see java.util.Enumeration#nextElement()
	 */
    @Override
    public InputStream nextElement() {
        InputStream stream = null;
        if (!hasMoreElements()) {
            throw new NoSuchElementException("No more streams");
        } else {
            final InputStream nextElement = this.streams.get(this.current);
            this.current++;
            stream = nextElement;
        }
        return stream;
    }

    /**
	 * Gets the stream count
	 * @return int with the stream count
	 */
    public int size() {
        return this.streams.size();
    }
}
