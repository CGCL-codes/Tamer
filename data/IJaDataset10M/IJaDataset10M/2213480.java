package de.intarsys.pdf.cos;

import java.io.IOException;
import java.util.Map;
import de.intarsys.pdf.parser.COSLoadException;
import de.intarsys.tools.locator.ILocator;

/**
 * An object that is able to create a COSDocument with the help of a locator
 * object that designates the physical source.
 * 
 * <p>
 * Possible implementations include parsing the associated input stream or
 * looking up previous results in a cache.
 * </p>
 */
public interface ICOSDocumentFactory {

    /**
	 * Create a COSDocument instance representing the object structure of the
	 * PDF file designated by locator.
	 * 
	 * @param locator
	 *            The reference to the physical source of the PDF file.
	 * @param options
	 *            The options for document creation
	 * 
	 * @return A new COSDocument
	 * 
	 * @throws IOException
	 * @throws COSLoadException
	 */
    public COSDocument createDocument(ILocator locator, Map options) throws COSLoadException, IOException;
}
