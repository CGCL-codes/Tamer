package ch.sahits.codegen.input;

import ch.sahits.model.IGeneratedObject;

/**
 * Marker interface for an input parser
 * @author Andi Hotz
 * @since 0.9.0
 */
public interface IInputParser {

    /**
	 * Parse the input file into an object that represents
	 * a database table.
	 * @return IGeneratedJavaClass Bean with the data
	 */
    public IGeneratedObject parse();

    /**
	 * Get the file extension of the input file
	 * this InputParser is capable of parsing
	 * @return file extension
	 */
    public String getFileExtension();

    /**
	 * Initialize the parser with the file
	 * @param inputFile
	 * @throws Exception some exception occured during initialisation
	 */
    public void init(String inputFile) throws Exception;

    /**
	 * This method rates the implementation. Since this
	 * Interface is used for an extension point Implementations with
	 * the highest ranking are choosen.
	 * @return Ranking of the implementation
	 */
    public int getRanking();

    /**
	 * Defines how complex the generation of the model
	 * is
	 * @return Number of work steps
	 */
    public int getWorkload();

    /**
	 * Indicate if the input file is self contained or if for
	 * compleetion of the model information from the second page is
	 * needed.
	 * @return true if data from the second page is needed
	 * @since 2.1.0
	 */
    public EDBConnectionData needsDataBaseInformationForCompleetion();
}
