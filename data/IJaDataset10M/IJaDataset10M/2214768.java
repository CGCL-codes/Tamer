package gnu.xml.transform;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.text.Collator;
import org.w3c.dom.Node;
import gnu.xml.xpath.Expr;

/**
 * Comparator for sorting lists of nodes according to a list of sort keys.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class XSLComparator implements Comparator {

    final List sortKeys;

    XSLComparator(List sortKeys) {
        this.sortKeys = sortKeys;
    }

    public int compare(Object o1, Object o2) {
        if (o1 instanceof Node && o2 instanceof Node) {
            Node n1 = (Node) o1;
            Node n2 = (Node) o2;
            for (Iterator i = sortKeys.iterator(); i.hasNext(); ) {
                SortKey sortKey = (SortKey) i.next();
                String k1 = sortKey.key(n1);
                String k2 = sortKey.key(n2);
                if ("text".equals(sortKey.dataType)) {
                    Locale locale = (sortKey.lang == null) ? Locale.getDefault() : new Locale(sortKey.lang);
                    Collator collator = Collator.getInstance(locale);
                    int d = collator.compare(k1, k2);
                    if (d != 0) {
                        switch(sortKey.caseOrder) {
                            case SortKey.UPPER_FIRST:
                                break;
                            case SortKey.LOWER_FIRST:
                                break;
                        }
                        if (sortKey.descending) {
                            d = -d;
                        }
                        return d;
                    }
                } else if ("number".equals(sortKey.dataType)) {
                    double kn1 = Expr._number(n1, k1);
                    double kn2 = Expr._number(n2, k2);
                    int d;
                    if (Double.isNaN(kn1) || Double.isInfinite(kn2)) {
                        d = -1;
                    } else if (Double.isNaN(kn2) || Double.isInfinite(kn1)) {
                        d = 1;
                    } else {
                        d = (kn1 > kn2) ? 1 : (kn1 < kn2) ? -1 : 0;
                    }
                    return (sortKey.descending) ? -d : d;
                }
            }
        }
        return 0;
    }
}
