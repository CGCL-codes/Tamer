package de.javatt.tools;

import org.jdom.Element;
import org.jdom.filter.Filter;
import de.javatt.net.Target;

/**
 * The ExcludeTargetFilter is a XML-Element filter (using JDom) which exclides
 * elements of type "Target" (this is needed by the ScenarioProvider). All other
 * elements will be accepted.
 * 
 * @author Matthias Kempa
 *  
 */
public class ExcludeElementFilter implements Filter {

    private static final long serialVersionUID = 1;

    private String myElementName;

    public ExcludeElementFilter(String elementName) {
        myElementName = elementName;
    }

    public boolean matches(Object obj) {
        boolean returnValue = false;
        if (obj instanceof Element) {
            Element elem = (Element) obj;
            if (!elem.getName().equals(myElementName)) {
                returnValue = true;
            }
        }
        return returnValue;
    }
}
