package net.sf.cybowmodeller.componenteditor.xml;

import net.sf.cybowmodeller.util.xml.DocumentBuilderInstance;
import net.sf.cybowmodeller.util.xml.MathMLConstants;
import org.w3c.dom.Document;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 44 $
 */
public final class EmptyMathMLDocument {

    private static final Document document = createDocument();

    private EmptyMathMLDocument() {
        throw new InternalError();
    }

    public static final Document getDocument() {
        return document;
    }

    private static Document createDocument() {
        final Document doc = DocumentBuilderInstance.newDocument();
        doc.appendChild(doc.createElementNS(MathMLConstants.NS_MATHML, MathMLConstants.TAG_MATH));
        return doc;
    }
}
