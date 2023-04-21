package org.ejen;

import java.util.Properties;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xpath.XPathAPI;
import org.apache.xalan.transformer.TransformerImpl;

/**
 * Filter node class.
 * <p>
 * A filter node tranforms the current in memory DOM tree.
 * <p>
 * <table class="usage">
 * <tr><th class="usage">Usage (ant build file)</th></tr>
 * <tr><td class="usage"><pre><code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *
 *  &lt;project name="generate" default="build"&gt;
 *
 *    &lt;taskdef name="ejen" classname="org.ejen.EjenTask"/&gt;
 *
 *    &lt;target name="build"&gt;
 *      &lt;{@link org.ejen.EjenTask ejen} ...&gt;
 *        ...
 *        <b>&lt;filter {@link org.ejen.EjenStylesheetNode#setFile(String) file}="filter.xml"
 *               [{@link #setForeach(String) foreach}="/ejen/entity-bean"]&gt;</b>
 *          ...
 *          [&lt;{@link org.ejen.EjenIncludeNode include} .../&gt;]
 *          [&lt;{@link org.ejen.EjenImportNode import} .../&gt;]
 *          [&lt;{@link org.ejen.EjenParamNode param} .../&gt;]
 *          ...
 *        <b>&lt;/filter&gt;</b>
 *        ...
 *      &lt;/ejen&gt;
 *    &lt;/target&gt;
 *
 *  &lt;/project&gt;
 * </code></pre></td></tr></table>
 * <p>
 * <b>Parent nodes</b>:
 * <ul>
 *   <li>{@link org.ejen.EjenTask ejen}
 * </ul>
 * @author F. Wolff
 * @version 1.0
 */
public class EjenFilterNode extends EjenStylesheetNode {

    protected String _foreach = null;

    /**
	 * Returns the name of this EjenFilterNode (always "filter").
	 * @return the name of this EjenFilterNode.
	 */
    public String nodeName() {
        return "filter";
    }

    /**
	 * Returns all non null attributes of this EjenFilterNode.
	 * @return non null attributes of this EjenFilterNode.
	 */
    public Properties getAttributes() {
        Properties attrs = super.getAttributes();
        if (_foreach != null) attrs.setProperty("foreach", _foreach);
        return attrs;
    }

    /**
	 * <b>[optional/AVT]</b> - sets the foreach attribute. This attribute
	 * allows iterative applications of this filter stylesheet to a sub-nodes
	 * set of the current in memory DOM tree.
	 * <p>
	 * Suppose you have the following DOM tree in memory:
	 * <table class="usage">
	 * <tr><td class="usage"><pre><code>
	 *  &lt;?xml version="1.0" encoding="iso-8859-1"?&gt;
	 *  &lt;ejen&gt;
	 *    &lt;name&gt;Name1&lt;/name&gt;
	 *    &lt;name&gt;Name2&lt;/name&gt;
	 *    &lt;name&gt;Name3&lt;/name&gt;
	 *    ...
	 *  &lt;/ejen&gt;
	 * </code></pre></td></tr></table>
	 * You want to transform it into this DOM tree:
	 * <table class="usage">
	 * <tr><td class="usage"><pre><code>
	 *  &lt;?xml version="1.0" encoding="iso-8859-1"?&gt;
	 *  &lt;ejen&gt;
	 *    &lt;name&gt;Dear Name1&lt;/name&gt;
	 *    &lt;name&gt;Dear Name2&lt;/name&gt;
	 *    &lt;name&gt;Dear Name3&lt;/name&gt;
	 *    ...
	 *  &lt;/ejen&gt;
	 * </code></pre></td></tr></table>
	 * You can use this filter stylesheet (with the foreach attribute set to "/ejen/name"):
	 * <table class="usage">
	 * <tr><td class="usage"><pre><code>
	 *  &lt;?xml version="1.0" encoding="iso-8859-1"?&gt;
	 *  &lt;xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	 *                  version="1.0"&gt;
	 *    &lt;xsl:output method="xml" encoding="iso-8859-1"/&gt;
	 *    <b>&lt;xsl:template match="name"&gt;
	 *      &lt;name&gt;Dear &lt;xsl:value-of select="."/&gt;&lt;/name&gt;
	 *    &lt;/xsl:template&gt;</b>
	 *  &lt;/xsl:stylesheet&gt;
	 * </code></pre></td></tr></table>
	 * <p>
	 * If this attribute is used, a parameter whose name is "root" and value is the root
	 * node of the current DOM tree is always and automaticaly passed to the filter
	 * stylesheet (you may have the line "<code>&lt;xsl:param name"root"/&gt;</code>" in
	 * the stylesheet).
	 * <p>
	 * @param foreach foreach String (default is null, meaning "apply this filter
	 *        stylesheet to the entire current DOM tree").
	 */
    public void setForeach(String foreach) {
        _foreach = foreach;
    }

    /**
	 * Executes this EjenFilterNode.
	 * @throws org.ejen.EjenException if something goes wrong...
	 */
    public void process() {
        super.process();
        TransformerImpl ti = null;
        DOMSource src = null;
        try {
            ti = (TransformerImpl) (getFromContext(CTX_TRANSFORMER_IMPL));
            src = (DOMSource) (getFromGlobalContext(CTX_DOM_SOURCE));
        } catch (Exception e) {
            throw new EjenException(this, null, e);
        }
        if (ti == null) throw new EjenException(this, "no '" + CTX_TRANSFORMER_IMPL + "' in context");
        if (src == null) throw new EjenException(this, "no '" + CTX_DOM_SOURCE + "' in global context");
        if (_foreach != null) {
            NodeList nl = null;
            try {
                ti.setParameter("root", src.getNode());
                nl = XPathAPI.selectNodeList(src.getNode(), evaluateAVT(ti, _foreach));
            } catch (Exception e) {
                throw new EjenException(this, "invalid 'foreach' attribute: " + _foreach, e);
            }
            try {
                Document doc = (Document) (src.getNode());
                for (int i = 0; i < nl.getLength(); i++) {
                    Node parent = nl.item(i).getParentNode();
                    if (parent == null) throw new EjenException(this, "Invalid 'foreach' attribute:" + _foreach + " (node " + i + " has no parent)");
                    DOMResult res = new DOMResult();
                    ti.transform(new DOMSource(nl.item(i)), res);
                    Node root = doc.importNode(((Document) (res.getNode())).getDocumentElement(), true);
                    parent.replaceChild(root, nl.item(i));
                }
            } catch (EjenException e) {
                throw e;
            } catch (Exception e) {
                throw new EjenException(this, null, e);
            }
        } else {
            DOMResult res = new DOMResult();
            try {
                ti.transform(src, res);
                putInGlobalContext(CTX_DOM_SOURCE, new DOMSource(res.getNode()));
            } catch (Exception e) {
                throw new EjenException(this, null, e);
            }
        }
    }
}
