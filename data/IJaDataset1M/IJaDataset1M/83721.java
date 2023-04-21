package br.edu.ufcg.ccc.javalog.XML;

/**
 * A persistence XML class
 * 
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 27/11/09
 */
public abstract class PersistenceXML<O extends Object> {

    /**
	 * Local tab for the date
	 */
    protected final String FILE_SEPARADOR = System.getProperties().get("file.separator").toString();

    /**
	 * Path to the user directory
	 */
    protected final String USER_PATH = System.getProperties().get("user.dir").toString() + FILE_SEPARADOR + "Data";

    /**
	 * Save the objects in xml file
	 * 
	 * @param object
	 *            Object to be saved
	 * @throws Exception
	 *             Throw exception for invalid entries
	 */
    public abstract void save(O object) throws Exception;

    /**
	 * Loads an xml file entry for an object
	 * 
	 * @param id
	 *            Identifier of the object to be sought
	 * @return The object found
	 * @throws Exception
	 *             Throw exception for invalid entries
	 */
    public abstract O load(Object id) throws Exception;
}
