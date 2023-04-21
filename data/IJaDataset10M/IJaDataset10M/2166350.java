package gnu.xml.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import org.w3c.dom.Node;
import gnu.xml.xpath.Expr;
import gnu.xml.xpath.Function;

/**
 * The XSLT <code>generate-id()</code>function.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class GenerateIdFunction extends Expr implements XPathFunction, Function {

    List args;

    public Object evaluate(List args) throws XPathFunctionException {
        return Collections.EMPTY_SET;
    }

    public void setArguments(List args) {
        this.args = args;
    }

    public Object evaluate(Node context, int pos, int len) {
        int arity = args.size();
        List values = new ArrayList(arity);
        for (int i = 0; i < arity; i++) {
            Expr arg = (Expr) args.get(i);
            values.add(arg.evaluate(context, pos, len));
        }
        Node node;
        Collection ns = (arity == 0) ? Collections.EMPTY_SET : (Collection) values.get(0);
        if (ns.isEmpty()) {
            node = context;
        } else {
            List list = new ArrayList(ns);
            Collections.sort(list, documentOrderComparator);
            node = (Node) list.get(0);
        }
        String name = node.getNodeName();
        int index = 0, depth = 0;
        for (Node ctx = node.getPreviousSibling(); ctx != null; ctx = ctx.getPreviousSibling()) {
            index++;
        }
        for (Node ctx = node.getParentNode(); ctx != null; ctx = ctx.getParentNode()) {
            depth++;
        }
        return name + "-" + index + "-" + depth;
    }

    public Expr clone(Object context) {
        GenerateIdFunction f = new GenerateIdFunction();
        int len = args.size();
        List args2 = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            args2.add(((Expr) args.get(i)).clone(context));
        }
        f.setArguments(args2);
        return f;
    }

    public boolean references(QName var) {
        for (Iterator i = args.iterator(); i.hasNext(); ) {
            if (((Expr) i.next()).references(var)) {
                return true;
            }
        }
        return false;
    }
}
