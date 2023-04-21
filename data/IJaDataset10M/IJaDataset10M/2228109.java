package eu.annocultor.core.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import eu.annocultor.xconverter.api.Environment;

/**
 * Interface for converter kernel. Needed only for very special converters.
 * 
 * @author Borys Omelayenko
 * 
 */
public interface ConverterKernel {

    /**
	 * Converts a task to RDF.
	 * 
	 * @see Environment
	 */
    public int convert() throws Exception;

    /**
	 * Limit the number of records when conversion will stop, for debugging.
	 * 
	 * @param maximalRecordsToPass
	 */
    public abstract void setMaximalRecordsToPass(int maximalRecordsToPass);

    /**
	 * Sets a class with tests.
	 * 
	 * @param tester
	 */
    public void setTester(ConverterTester tester);

    /**
	 * Should the conversion ontology be created?
	 */
    public void setCreateOntology(boolean createOntology);

    public BufferedInputStream makeInputStream(File src) throws FileNotFoundException;
}
