package org.jazzteam.edu.patterns.factoryMethod;

public interface Reader {

    /**
	 * Method is intended for reading the lines
	 * from different keepers of information.
	 * @param path - full filename or path to DataBase.
	 * @return array of the strings scanned from some carrier of information.
	 */
    public String[] read(final String path);
}
