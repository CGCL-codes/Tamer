package gnu.xml.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

/**
 * A template node representing the XSL <code>call-template</code>
 * instruction.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class CallTemplateNode extends TemplateNode {

    final QName name;

    final List withParams;

    CallTemplateNode(QName name, List withParams) {
        this.name = name;
        this.withParams = withParams;
    }

    TemplateNode clone(Stylesheet stylesheet) {
        int len = withParams.size();
        List withParams2 = new ArrayList(len);
        for (int i = 0; i < len; i++) withParams2.add(((WithParam) withParams.get(i)).clone(stylesheet));
        TemplateNode ret = new CallTemplateNode(name, withParams2);
        if (children != null) ret.children = children.clone(stylesheet);
        if (next != null) ret.next = next.clone(stylesheet);
        return ret;
    }

    void doApply(Stylesheet stylesheet, QName mode, Node context, int pos, int len, Node parent, Node nextSibling) throws TransformerException {
        TemplateNode t = stylesheet.getTemplate(mode, name);
        if (t != null) {
            if (withParams != null) {
                LinkedList values = new LinkedList();
                for (Iterator i = withParams.iterator(); i.hasNext(); ) {
                    WithParam p = (WithParam) i.next();
                    if (t.hasParam(p.name)) {
                        Object value = p.getValue(stylesheet, mode, context, pos, len);
                        Object[] pair = new Object[2];
                        pair[0] = p.name;
                        pair[1] = value;
                        values.add(pair);
                    }
                }
                stylesheet.bindings.push(Bindings.WITH_PARAM);
                for (Iterator i = values.iterator(); i.hasNext(); ) {
                    Object[] pair = (Object[]) i.next();
                    QName name = (QName) pair[0];
                    Object value = pair[1];
                    stylesheet.bindings.set(name, value, Bindings.WITH_PARAM);
                    if (stylesheet.debug) System.err.println("with-param: " + name + " = " + value);
                }
            }
            t.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
            if (withParams != null) {
                stylesheet.bindings.pop(Bindings.WITH_PARAM);
            }
        }
        if (next != null) next.apply(stylesheet, mode, context, pos, len, parent, nextSibling);
    }

    public boolean references(QName var) {
        if (withParams != null) {
            for (Iterator i = withParams.iterator(); i.hasNext(); ) {
                if (((WithParam) i.next()).references(var)) return true;
            }
        }
        return super.references(var);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("call-template");
        buf.append('[');
        buf.append("name=");
        buf.append(name);
        buf.append(']');
        return buf.toString();
    }
}
