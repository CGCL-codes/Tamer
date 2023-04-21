package org.servingMathematics.mathematics.bridges;

import org.servingMathematics.mathematics.interfaces.numbers.MathInteger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Converts a <code>MathNode</code> of type <code>MathNode.INTEGER</code> to
 * MathML.
 * 
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May </a>
 * @version 0.1, 15 March 2005
 * 
 * TODO Fully document this class
 */
public class IntegerBridge {

    /**
	 * Returns a <code>Node</code> which is a
	 * presentation MathML representation of the specified <code>MathInteger</code>.
	 * 
	 * @param document
	 *            the document against which <code>Node</code> s should be
	 *            created.
	 * @param mathInteger
	 *            the <code>MathInteger</code> to be converted to MathML.
	 * @return a <code>Node</code> which is presentation MathML.      
	 */
    public static Node getPresentationMathMLNode(Document document, MathInteger mathInteger) {
        Element mnElement = document.createElementNS(MathNodeToMathML.MATHML_NAMESPACE_URI, "mn");
        Text mnValue = document.createTextNode("" + mathInteger.intValue());
        mnElement.appendChild(mnValue);
        return mnElement;
    }

    /**
	 * Returns a <code>Node</code> which is a
	 * content MathML representation of the the specified <code>MathInteger</code>.
	 * 
	 * @param document
	 *            the document against which <code>Node</code> s should be
	 *            created.
	 * @param mathInteger
	 *            the <code>MathInteger</code> to be converted to MathML.
	 * @return a <code>Node</code> which is content MathML.
	 */
    public static Node getContentMathMLNode(Document document, MathInteger mathInteger) {
        Element cnElement = document.createElementNS(MathNodeToMathML.MATHML_NAMESPACE_URI, "cn");
        Attr typeAttr = document.createAttributeNS(MathNodeToMathML.MATHML_NAMESPACE_URI, "type");
        typeAttr.setValue("integer");
        cnElement.setAttributeNodeNS(typeAttr);
        Text cnValue = document.createTextNode("" + mathInteger.intValue());
        cnElement.appendChild(cnValue);
        return cnElement;
    }

    /**
	 * Returns a <code>Node</code> which is an OpenMath representation of 
	 * the the specified <code>MathInteger</code>.
	 * 
	 * @param document
	 *            the document against which <code>Node</code> s should be
	 *            created.
	 * @param mathInteger
	 *            the <code>MathInteger</code> to be converted to OpenMath.
	 * @return a <code>Node</code> which is OpenMath.
	 */
    public static Node getOpenMathNode(Document document, MathInteger mathInteger) {
        Element omiElement = document.createElementNS(MathNodeToOpenMath.OPENMATH_NAMESPACE_URI, "OMI");
        Text omiValue = document.createTextNode("" + mathInteger.intValue());
        omiElement.appendChild(omiValue);
        return omiElement;
    }
}
