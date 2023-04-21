package it.csi.otre.services.ooo;

import java.util.List;
import org.dom4j.Document;

/**
 * Interface of the Document Service.
 * 
 * @author oscar ghersi (email: oscar.ghersi@csi.it)
 */
public interface OOoDocServiceInterface {

    /**
	 * Creates a new document from a template, using some data in XML format and returning it in the format
	 * specified.
	 * 
	 * It uses the XBuild library; the document and template names could contain some path information;
	 * the XML data needs to be in the format requested by that library.
	 * It uses also the OOoService to convert the document to the format requested.
	 *  
	 * @param documentName Name of the document to be created.
	 * @param templateName Name of the template to be used.
	 * @param data Data to be used in the document creation.
	 * @param format Format of output.
	 * @return The document created.
	 * @throws Exception In case of error.
	 */
    public byte[] create(String documentName, String templateName, Document data, String format) throws Exception;

    /**
	 * Returns a list of formats allowed for document conversion.
	 * 
	 * @throws Exception In case of error.
	 */
    public List listFormats() throws Exception;

    /**
	 * Method to test the service functionalities.
	 * 
	 * @throws Exception In case of error.
	 */
    public void test() throws Exception;
}
