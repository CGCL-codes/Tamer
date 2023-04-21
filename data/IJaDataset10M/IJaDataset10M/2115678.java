package gnu.xml.xpath;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;

/**
 * The <code>name</code> function returns a string containing a QName
 * representing the expanded-name of the node in the argument node-set that
 * is first in document order. The QName must represent the expanded-name
 * with respect to the namespace declarations in effect on the node whose
 * expanded-name is being represented. Typically, this will be the QName
 * that occurred in the XML source. This need not be the case if there are
 * namespace declarations in effect on the node that associate multiple
 * prefixes with the same namespace. However, an implementation may include
 * information about the original prefix in its representation of nodes; in
 * this case, an implementation can ensure that the returned string is
 * always the same as the QName used in the XML source. If the argument
 * node-set is empty or the first node has no expanded-name, an empty string
 * is returned. If the argument it omitted, it defaults to a node-set with
 * the context node as its only member.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class NameFunction extends Expr {

    final Expr arg;

    NameFunction(List args) {
        this(args.size() > 0 ? (Expr) args.get(0) : null);
    }

    NameFunction(Expr arg) {
        this.arg = arg;
    }

    public Object evaluate(Node context, int pos, int len) {
        Object val = (arg == null) ? Collections.singleton(context) : arg.evaluate(context, pos, len);
        return _name(context, (Collection) val);
    }

    public Expr clone(Object context) {
        return new NameFunction((arg == null) ? null : arg.clone(context));
    }

    public boolean references(QName var) {
        return (arg == null) ? false : arg.references(var);
    }

    public String toString() {
        return (arg == null) ? "name()" : "name(" + arg + ")";
    }
}
