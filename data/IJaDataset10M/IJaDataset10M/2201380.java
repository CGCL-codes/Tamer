package org.jtestcase.core.digester;

import java.io.FileNotFoundException;
import java.util.List;
import org.jdom.Document;
import org.jdom.JDOMException;

/**
 * Interface of the XQueryParser used to retrieve the XML data for JTestCaseWizard.
 * There are several possible implementations for this interface.
 * 
 * @author <a href="mailto:faustothegrey@users.sourceforge.net">Fausto Lelli</a>
 * 
 * $Id: XQueryParser.java,v 1.5 2006/01/14 12:46:40 faustothegrey Exp $
 */
public interface XQueryParser {

    /**
     * Gets a list of elements from the XML file with the use of a XPath expression
     * @param fileName name of the XML file
     * @param xpathexpr the XPath expression
     * @return list of elements
     * @throws XQueryException in case of any errors during the processing
     * @throws JDOMException 
     */
    public List getElements(Document doc, String xpathexpr) throws FileNotFoundException, XQueryException, JDOMException;
}
