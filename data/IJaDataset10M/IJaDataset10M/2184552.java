package org.ourgrid.common.spec.grammar.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.ourgrid.common.spec.grammar.Grammar;

/**
 * This entity is responsable to know how to read a gammar description file.
 * Created on 22/05/2004
 */
public interface GrammarReader {

    /**
	 * Read a gammar description file from a determined format and returns a
	 * Grammar object after the process
	 * 
	 * @param fileName The file where the grammar is described.
	 * @param grammar The object grammar that will be filled with the
	 *        informations from the file.
	 * @return A Grammar object
	 */
    public Grammar read(File fileName, Grammar grammar) throws MalformedGrammarException, IOException;

    /**
	 * Read a gammar description file from a determined format and returns a
	 * Grammar object after the process
	 * 
	 * @param stream The stream that contains the description of the grammar
	 * @param grammar The object grammar that will be filled with the
	 *        informations from the file.
	 * @return A Grammar object
	 */
    public Grammar read(InputStream stream, Grammar grammar) throws MalformedGrammarException, IOException;
}
