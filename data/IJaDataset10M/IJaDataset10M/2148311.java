package com.ek.mitapp.repository;

import java.util.List;
import java.io.IOException;

/**
 * A interface for defining a reader class.
 * <br>
 * Id: $Id: IReader.java 2640 2006-09-07 21:53:22Z dhirwinjr $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public interface IReader<T> {

    /**
	 * Read and parse a file.
	 * 
	 * @param filename
	 * @throws IOException
	 * @return Return a list of objects created from the parsing process
	 */
    public List<T> read(String filename) throws IOException;

    /**
	 * Read and parse a default file.
	 * 
	 * @return
	 * @throws IOException
	 */
    public List<T> read() throws IOException;
}
