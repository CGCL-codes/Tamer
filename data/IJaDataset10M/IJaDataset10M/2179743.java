package gnu.xml.xpath;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;

/**
 * The <code>ceiling</code> function returns the smallest (closest to
 * negative infinity) number that is not less than the argument and that
 * is an integer.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class CeilingFunction extends Expr {

    final Expr arg;

    CeilingFunction(List args) {
        this((Expr) args.get(0));
    }

    CeilingFunction(Expr arg) {
        this.arg = arg;
    }

    public Object evaluate(Node context, int pos, int len) {
        Object val = arg.evaluate(context, pos, len);
        double n = _number(context, val);
        return new Double(Math.ceil(n));
    }

    public Expr clone(Object context) {
        return new CeilingFunction(arg.clone(context));
    }

    public boolean references(QName var) {
        return arg.references(var);
    }

    public String toString() {
        return "ceiling(" + arg + ")";
    }
}
