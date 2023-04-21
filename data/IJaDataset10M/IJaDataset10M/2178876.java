package gnu.xml.transform;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

/**
 * A template node representing an XSL <code>otherwise</code> instruction.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class OtherwiseNode extends TemplateNode {

    TemplateNode clone(Stylesheet stylesheet) {
        TemplateNode ret = new OtherwiseNode();
        if (children != null) ret.children = children.clone(stylesheet);
        if (next != null) ret.next = next.clone(stylesheet);
        return ret;
    }

    void doApply(Stylesheet stylesheet, QName mode, Node context, int pos, int len, Node parent, Node nextSibling) throws TransformerException {
        if (children != null) children.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
        if (next != null) next.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
    }

    public String toString() {
        return "otherwise";
    }
}
