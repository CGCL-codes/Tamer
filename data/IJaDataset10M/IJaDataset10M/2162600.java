package org.o14x.utilz.xor;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.o14x.utilz.exception.TechException;
import org.xml.sax.SAXException;

/**
 * Class that parses XML and renders a corresponding tree of objects.
 * 
 * @author Olivier Dangr�aux
 */
public class XMLObjectReader {

    private XorSaxHandler xorSaxHandler;

    private static final String ERR_MSG_1 = "Error parsing XML object";

    private static final String ERR_MSG_2 = "IO error when parsing XML object";

    private static final String ERR_MSG_3 = "Error when configuring XML object parser";

    /**
	 * Instantiate a new XMLObjectReader.
	 * 
	 * @param rootClassName The name of the class of the root object to produce.
	 */
    public XMLObjectReader(String rootClassName) {
        xorSaxHandler = new XorSaxHandler(rootClassName);
    }

    /**
	 * Parse the given InputStream and try to produce an tree of objects.
	 * 
	 * @param inputStream The InputStream to parse.
	 * 
	 * @return The root object of produced tree of objects.
	 * 
	 * @throws TechException In case of technical error.
	 */
    public Object parse(InputStream inputStream) throws TechException {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(inputStream, xorSaxHandler);
        } catch (SAXException e) {
            throw new TechException(ERR_MSG_1, e);
        } catch (IOException e) {
            throw new TechException(ERR_MSG_2, e);
        } catch (ParserConfigurationException e) {
            throw new TechException(ERR_MSG_3, e);
        }
        return xorSaxHandler.getResult();
    }

    /**
	 * Register the implementing class corresponding to a given interface (or abstract class).
	 * 
	 * @param interfaceFullName			The full name of an interface or abstract class.
	 * @param implementingClassFullName	The full name of the class implementing the interface or abstract class.
	 */
    public void registerImplementation(String interfaceFullName, String implementingClassFullName) {
        xorSaxHandler.registerImplementation(interfaceFullName, implementingClassFullName);
    }
}
