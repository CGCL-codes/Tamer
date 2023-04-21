package org.openofficesearch.io.openoffice;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.StringReader;

/**
 * An entity resolver for OpenOffice documents<br />
 * Created: 2005
 * @author Connor Garvey
 * @version 0.0.5
 * @since 0.0.1
 */
public class OfficeDTDEntityResolver implements EntityResolver {

    private static final String[] openOfficeDTDs = { "office.dtd", "accelerator.dtd", "dialog.dtd", "event.dtd", "image.dtd", "libraries.dtd", "library.dtd", "Manifest.dtd", "menubar.dtd", "module.dtd", "statusbar.dtd", "toolbar.dtd" };

    /**
   * Creates a new instance of this class
   */
    public OfficeDTDEntityResolver() {
    }

    /**
   * Tell our XML readers not to try to load any DTD's
   * @param publicId The public identifier of the external entity being
   *   referenced, or null if none was supplied.
   * @param systemId The system identifier of the external entity being
   *   referenced.
   * @return An InputSource object describing the new input source, or null to
   *   request that the parser open a regular URI connection to the system
   *   identifier.
   * @throws SAXException Any SAX exception, possibly wrapping another
   *   exception.
   * @throws IOException A Java-specific IO exception, possibly the result of
   *   creating a new InputStream or Reader for the InputSource.
   */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId.endsWith(".dtd")) {
            StringReader stringInput = new StringReader(" ");
            return new InputSource(stringInput);
        } else {
            return null;
        }
    }
}
