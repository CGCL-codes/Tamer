package gnu.xml.xpath;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The <code>lang</code> function returns true or false depending on whether
 * the language of the context node as specified by xml:lang attributes is
 * the same as or is a sublanguage of the language specified by the argument
 * string. The language of the context node is determined by the value of
 * the xml:lang attribute on the context node, or, if the context node has
 * no xml:lang attribute, by the value of the xml:lang attribute on the
 * nearest ancestor of the context node that has an xml:lang attribute. If
 * there is no such attribute, then lang returns false. If there is such an
 * attribute, then lang returns true if the attribute value is equal to the
 * argument ignoring case, or if there is some suffix starting with - such
 * that the attribute value is equal to the argument ignoring that suffix of
 * the attribute value and ignoring case.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class LangFunction extends Expr {

    final Expr arg;

    LangFunction(List args) {
        this((Expr) args.get(0));
    }

    LangFunction(Expr arg) {
        this.arg = arg;
    }

    public Object evaluate(Node context, int pos, int len) {
        Object val = arg.evaluate(context, pos, len);
        String lang = _string(context, val);
        String clang = getLang(context);
        while (clang == null && context != null) {
            context = context.getParentNode();
            clang = getLang(context);
        }
        boolean ret = (clang == null) ? false : clang.toLowerCase().startsWith(lang.toLowerCase());
        return ret ? Boolean.TRUE : Boolean.FALSE;
    }

    String getLang(Node node) {
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String lang = ((Element) node).getAttribute("xml:lang");
                if (lang != null) return lang;
            }
            node = node.getParentNode();
        }
        return null;
    }

    public Expr clone(Object context) {
        return new IdFunction(arg.clone(context));
    }

    public boolean references(QName var) {
        return arg.references(var);
    }

    public String toString() {
        return "lang(" + arg + ")";
    }
}
