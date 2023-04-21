package joelib.io.types.cml;

import java.io.*;
import java.net.URL;
import org.apache.log4j.*;
import org.xml.sax.*;

/**
 * This class resolves DOCTYPE declaration for Chemical Markup Language (CML)
 * files and uses a local version for validation. More information about
 * CML can be found at http://www.xml-cml.org/.
 *
 * @author egonw
 * @author c.steinbeck@uni-koeln.de
 * @author gezelter@maul.chem.nd.edu
 * @author     wegnerj
 * @license LGPL
 * @cvsversion    $Revision: 1.6 $, $Date: 2004/04/13 14:09:24 $
 * @cite rr99b
 * @cite mr01
 * @cite gmrw01
 * @cite wil01
 * @cite mr03
 * @cite mrww04
 **/
public class CMLResolver implements EntityResolver {

    private static Category logger = Category.getInstance("joelib.io.types.cml.CMLResolver");

    private String ddtResourceDir = "joelib/io/types/cml/data/";

    public CMLResolver() {
    }

    public CMLResolver(String _ddtResourceDir) {
        if (_ddtResourceDir != null) {
            ddtResourceDir = _ddtResourceDir;
        }
    }

    /**
     * Not implemented: always returns null.
     **/
    public InputSource getExternalSubset(String name, String baseURI) {
        return null;
    }

    /**
     * Not implemented, but uses resolveEntity(String publicId, String systemId)
     * instead.
     **/
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) {
        return resolveEntity(publicId, systemId);
    }

    /**
     * Resolves SYSTEM and PUBLIC identifiers for CML DTDs.
     *
     * @param publicId the PUBLIC identifier of the DTD (unused)
     * @param systemId the SYSTEM identifier of the DTD
     * @return the CML DTD as an InputSource or null if id's unresolvable
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        if (logger.isDebugEnabled()) {
            logger.debug("CMLResolver: resolving " + publicId + ", " + systemId);
        }
        systemId = systemId.toLowerCase();
        if ((systemId.indexOf("cml-1999-05-15.dtd") != -1) || (systemId.indexOf("cml.dtd") != -1) || (systemId.indexOf("cml1_0.dtd") != -1)) {
            return getCMLType("cml1_0.dtd");
        } else if ((systemId.indexOf("cml-2001-04-06.dtd") != -1) || (systemId.indexOf("cml1_0_1.dtd") != -1)) {
            return getCMLType("cml1_0_1.dtd");
        } else {
            logger.warn("Could not resolve " + systemId);
            return null;
        }
    }

    /**
     * Returns an InputSource of the appropriate CML DTD. It accepts
     * two CML DTD names: cml1_0.dtd and cml1_0_1.dtd. Returns null
     * for any other name.
     *
     * @param type the name of the CML DTD version
     * @return the InputSource to the CML DTD
     */
    private InputSource getCMLType(String type) {
        try {
            URL url = ClassLoader.getSystemResource(ddtResourceDir + type);
            if (url == null) {
                logger.error("Error while trying to read CML DTD (" + type + ") from " + ddtResourceDir);
                return null;
            }
            return new InputSource(new BufferedReader(new InputStreamReader(url.openStream())));
        } catch (Exception e) {
            logger.error("Error while trying to read CML DTD (" + type + ") from " + ddtResourceDir + ":" + e.toString());
            return null;
        }
    }
}
