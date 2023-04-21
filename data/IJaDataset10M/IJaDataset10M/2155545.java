package gnu.xml.xpath;

import javax.xml.namespace.QName;
import org.w3c.dom.Node;

/**
 * Logical and.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class AndExpr extends Expr {

    final Expr lhs;

    final Expr rhs;

    public AndExpr(Expr lhs, Expr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Object evaluate(Node context, int pos, int len) {
        Object left = lhs.evaluate(context, pos, len);
        if (!_boolean(context, left)) {
            return Boolean.FALSE;
        }
        Object right = rhs.evaluate(context, pos, len);
        return _boolean(context, right) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Expr clone(Object context) {
        return new AndExpr(lhs.clone(context), rhs.clone(context));
    }

    public boolean references(QName var) {
        return (lhs.references(var) || rhs.references(var));
    }

    public String toString() {
        return lhs + " and " + rhs;
    }
}
