package org.sbfc.converter.models;

/**
 * Interface that all Models need to implement in order to reach the standardized IO
 * 
 * @author jpettit
 *
 */
public interface GeneralModel {

    /**
	 * Sets the Model from a file on the file system.
	 * 
	 * @param fileName path to the file containing the model
	 */
    public void setModelFromFile(String fileName);

    /**
	 * Sets the model from a String
	 * 
	 * @param modelString Model
	 */
    public void setModelFromString(String modelString);

    /**
	 * Writes the Model in a new file
	 * 
	 * @param fileName path at which the new file will be created
	 */
    public void modelToFile(String fileName);

    /**
	 * Returns the Model as a String
	 * 
	 * @return Model
	 */
    public String modelToString();

    /**
	 * Returns the usual model file type extension (ex: .xml for SBML, .owl for BIOPAX)
	 * 
	 * @return file type extension
	 */
    public String getFileType();
}
