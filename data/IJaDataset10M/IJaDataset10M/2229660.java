package org.ozoneDB.xml;

import org.w3c.dom.*;

/**
 * Defines an element factory for constructing new elements. An application
 * document may elect to use this factory to create user elements derived
 * from the class {@link XMLElement}. This is an alternative to the simple
 * tag name to class mapping that is supported by {@link XMLDocument}.
 * <P>
 * The {@link #createElement} will be called to create any element and may
 * behave in one of three manners:
 * <UL>
 * <LI>Create and return a new element from a class that extends {@link
 *  XMLElement}
 * <LI>Return null and an element will be created from {@link XMLElement}
 * <LI>Throw an exception to indicate that elements of this type are not
 *  supported in this document (this behavior is highly discouraged)
 * </UL>
 *
 *
 * @version $Revision: 1.2 $ $Date: 2005/08/04 20:51:15 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a> 
 * @see XMLElement
 * @deprecated Alternative API will be introduced in OpenXML 1.1
 */
public interface XMLElementFactory {

    /**
     * Called to create an element with the specified tag name. Returned element
     * is of class derived from {@link XMLElement}. If null is returned, an
     * element of type {@link XMLElement} will be created.
     * <P>
     * When creating a new element, the parameters <TT>owner</TT> and
     * <TT>tagName</TT> must be passed as is to the {@link XMLElement}
     * constructor.
     *
     * @param owner The owner document
     * @param tagName The element tag name
     * @return New element or null
     */
    public XMLElement createElement(XMLDocument owner, String tagName);
}
