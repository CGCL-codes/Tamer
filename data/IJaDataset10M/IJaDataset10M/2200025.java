package gnu.xml.transform;

import java.util.Iterator;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * A template node representing the XSL <code>copy</code> instruction.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class CopyNode extends TemplateNode {

    final String uas;

    CopyNode(String uas) {
        this.uas = uas;
    }

    TemplateNode clone(Stylesheet stylesheet) {
        TemplateNode ret = new CopyNode(uas);
        if (children != null) ret.children = children.clone(stylesheet);
        if (next != null) ret.next = next.clone(stylesheet);
        return ret;
    }

    void doApply(Stylesheet stylesheet, QName mode, Node context, int pos, int len, Node parent, Node nextSibling) throws TransformerException {
        Node copy = parent;
        switch(context.getNodeType()) {
            case Node.TEXT_NODE:
            case Node.ATTRIBUTE_NODE:
            case Node.ELEMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.COMMENT_NODE:
                Document doc = (parent instanceof Document) ? (Document) parent : parent.getOwnerDocument();
                copy = context.cloneNode(false);
                copy = doc.adoptNode(copy);
                if (copy.getNodeType() == Node.ATTRIBUTE_NODE) {
                    if (parent.getFirstChild() != null) {
                    } else {
                        NamedNodeMap attrs = parent.getAttributes();
                        if (attrs != null) attrs.setNamedItemNS(copy);
                    }
                } else {
                    if (nextSibling != null) parent.insertBefore(copy, nextSibling); else parent.appendChild(copy);
                }
        }
        if (uas != null) {
            StringTokenizer st = new StringTokenizer(uas, " ");
            while (st.hasMoreTokens()) addAttributeSet(stylesheet, mode, context, pos, len, copy, null, st.nextToken());
        }
        if (children != null) children.apply(stylesheet, mode, context, pos, len, copy, null);
        if (next != null) next.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
    }

    void addAttributeSet(Stylesheet stylesheet, QName mode, Node context, int pos, int len, Node parent, Node nextSibling, String attributeSet) throws TransformerException {
        for (Iterator i = stylesheet.attributeSets.iterator(); i.hasNext(); ) {
            AttributeSet as = (AttributeSet) i.next();
            if (!as.name.equals(attributeSet)) continue;
            if (as.uas != null) {
                StringTokenizer st = new StringTokenizer(as.uas, " ");
                while (st.hasMoreTokens()) addAttributeSet(stylesheet, mode, context, pos, len, parent, nextSibling, st.nextToken());
            }
            if (as.children != null) as.children.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("copy");
        if (uas != null) {
            buf.append('[');
            buf.append("uas=");
            buf.append(uas);
            buf.append(']');
        }
        return buf.toString();
    }
}
