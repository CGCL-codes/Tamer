package jbreport.xrl;

import org.w3c.dom.Element;

/**
 *
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
public interface XRLElement extends Element {

    /**
    * This returns the public identifier of the element, as specified by the
    * id attribute in the element definition.
    * 
    * @return the id attribute of the element, if there is one, empty string
    * otherwise
    */
    public String getElementId();
}
