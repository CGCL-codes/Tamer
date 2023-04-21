package de.schlund.pfixxml.util.xsltimpl;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.event.SaxonOutputKeys;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.tinytree.TinyBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import de.schlund.pfixxml.util.XmlSupport;
import de.schlund.pfixxml.util.Xslt;
import de.schlund.pfixxml.util.XsltVersion;

/**
 * @author mleidig@schlund.de
 */
public class XmlSaxon2 implements XmlSupport {

    public Document createInternalDOM(Source input) throws TransformerException {
        TinyBuilder builder = new TinyBuilder();
        Transformer t = Xslt.createIdentityTransformer(XsltVersion.XSLT2);
        t.transform(input, builder);
        NodeInfo node = builder.getCurrentRoot();
        return (Document) NodeOverNodeInfo.wrap(node);
    }

    public boolean isInternalDOM(Node node) {
        return node instanceof NodeOverNodeInfo;
    }

    public String getIndentOutputKey() {
        return SaxonOutputKeys.INDENT_SPACES;
    }
}
