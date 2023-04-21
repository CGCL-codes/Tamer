package org.ws4d.java.modules;

import org.ws4d.java.xml.XMLElement;

/**
 * This class implements the interface for the RepositoryManager of the
 * Module wsdlrepository.
 */
public interface IWSDLRepositoryManager {

    /**
	 * Save the given WSDL (definitions element) to the given WSDL name in
	 * the WSDLRepository.
	 * 
	 * @param definitions The WSDL as XMLElement.
	 * @param wsdlName The WSDL name.
	 * @return <code>true</code>, if everything went fine, <code>false</code>
	 * otherwise.
	 */
    abstract boolean saveWSDL(XMLElement definitions, String wsdlName);

    /**
	 * Load a predefined WSDL from the WSDLRepository.
	 * 
	 * @param wsdlName The WSDL name.
	 * @return The WSDL as XMLElement.
	 */
    abstract XMLElement loadWSDL(String wsdlName);

    /**
	 * List the available WSDL names in the WSDLRepository.
	 * 
	 * @return The WSDL names available.
	 */
    abstract String[] listWSDLs();

    /**
	 * Rename a WSDL of the WSDLRepository.
	 * 
	 * @param wsdlNameOld The old WSDL name.
	 * @param wsdlNameNew The new WSDL name.
	 * @return <code>true</code>, if operation was successful, <code>false</code> otherwise.
	 */
    abstract boolean renameWSDL(String wsdlNameOld, String wsdlNameNew);

    /**
	 * Remove a WSDL from the WSDLRepository.
	 * 
	 * @param wsdlName The WSDL name.
	 * @return <code>true</code>, if operation was successful, <code>false</code> otherwise.
	 */
    abstract boolean removeWSDL(String wsdlName);

    /**
	 * Clear the WSDLRepository (delete all WSDLs).
	 * 
	 * @return <code>true</code>, if operation was successful, <code>false</code> otherwise.
	 */
    abstract boolean clear();
}
