package org.apache.batik.test.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLConstants;

/**
 * Checks for regressions in rendering of a document with a given
 * alternate stylesheet.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGTextContentRenderingAccuracyTest.java 475477 2006-11-15 22:44:28Z cam $
 */
public class SVGTextContentRenderingAccuracyTest extends ParametrizedRenderingAccuracyTest {

    protected String script;

    protected String onload;

    protected String parameter;

    public void setScript(String script) {
        this.script = script;
    }

    public void setOnLoadFunction(String onload) {
        this.onload = onload;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    protected Document manipulateSVGDocument(Document doc) {
        Element root = doc.getDocumentElement();
        String function;
        if (parameter == null) {
            function = onload + "()";
        } else {
            function = onload + "(" + parameter + ")";
        }
        root.setAttributeNS(null, "onload", function);
        Element scriptElement = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SCRIPT_TAG);
        scriptElement.setAttributeNS(XMLConstants.XLINK_NAMESPACE_URI, XMLConstants.XLINK_HREF_QNAME, script);
        root.appendChild(scriptElement);
        return doc;
    }
}
