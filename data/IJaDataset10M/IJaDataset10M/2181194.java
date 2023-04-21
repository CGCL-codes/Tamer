package com.sun.org.apache.xerces.internal.xpointer;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/**
 * <p>
 * Used for scheme specific parsing and evaluation of an XPointer expression.
 * This interface applies to both ShortHand and SchemeBased XPointer 
 * expressions.
 * </p>
 *
 * @xerces.internal
 * 
 * @version $Id: XPointerPart.java,v 1.1.4.1 2005/09/08 05:25:45 sunithareddy Exp $
 */
public interface XPointerPart {

    public static final int EVENT_ELEMENT_START = 0;

    public static final int EVENT_ELEMENT_END = 1;

    public static final int EVENT_ELEMENT_EMPTY = 2;

    /**
     * Provides scheme specific parsing of a XPointer expression i.e. 
     * the PointerPart or ShortHandPointer.  
     * 
     * @param  xpointer A String representing the PointerPart or ShortHandPointer.
     * @throws XNIException Thrown if the PointerPart string does not conform to 
     *         the syntax defined by its scheme.
     *   
     */
    public void parseXPointer(String part) throws XNIException;

    /**
     * Evaluates an XML resource with respect to an XPointer expressions   
     * by checking if it's element and attributes parameters match the 
     * criteria specified in the xpointer expression.  
     * 
     * @param element - The name of the element.
     * @param attributes - The element attributes.
     * @param augs - Additional information that may include infoset augmentations
     * @param event - An integer indicating
     *                0 - The start of an element
     *                1 - The end of an element
     *                2 - An empty element call 
     * @throws XNIException Thrown to signal an error
     *   
     */
    public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException;

    /**
     * Returns true if the XPointer expression resolves to a resource fragment
     * specified as input else returns false.       
     * 
     * @return True if the xpointer expression matches a fragment in the resource
     *         else returns false. 
     * @throws XNIException Thrown to signal an error
     *   
     */
    public boolean isFragmentResolved() throws XNIException;

    /**
     * Returns true if the XPointer expression resolves to a non-element child
     * of the current resource fragment.       
     * 
     * @return True if the XPointer expression resolves to a non-element child
     *         of the current resource fragment. 
     * @throws XNIException Thrown to signal an error
     *   
     */
    public boolean isChildFragmentResolved() throws XNIException;

    /**
     * Returns a String containing the scheme name of the PointerPart 
     * or the name of the ShortHand Pointer.       
     * 
     * @return A String containing the scheme name of the PointerPart. 
     *   
     */
    public String getSchemeName();

    /**
     * Returns a String containing the scheme data of the PointerPart.       
     * 
     * @return A String containing the scheme data of the PointerPart. 
     *   
     */
    public String getSchemeData();

    /**
     * Sets the scheme name of the PointerPart or the ShortHand Pointer name.       
     * 
     * @param schemeName A String containing the scheme name of the PointerPart. 
     *   
     */
    public void setSchemeName(String schemeName);

    /**
     * Sets the scheme data of the PointerPart.       
     * 
     * @param schemeData A String containing the scheme data of the PointerPart. 
     *   
     */
    public void setSchemeData(String schemeData);
}
