package org.zkoss.zk.ui.metainfo;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Executions;

/**
 * Represents a header element, such as &lt;link&gt; and &lt;meta&gt;.
 * They are usually represented as directives in ZUML.
 * For example, the link and meta directives represent &lt;link&gt;
 * and &lt;meta&gt; HTML tags, respectively.
 *
 * @author tomyeh
 */
public class Header {

    private final String _name;

    private final List _attrs;

    /** Constructor.
	 *
	 * <p>Note: it detects the href attribute (from the attrs argument), and
	 * encodes it with {@link Executions#encodeURL}.
	 *
	 * @param name the tag name, such as link (never null or empty).
	 * @param attrs a map of (String, String) attributes.
	 */
    public Header(String name, Map attrs) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("empty");
        _name = name;
        if (attrs == null || attrs.isEmpty()) {
            _attrs = Collections.EMPTY_LIST;
        } else {
            _attrs = new LinkedList();
            for (Iterator it = attrs.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry me = (Map.Entry) it.next();
                final Object nm = me.getKey(), val = me.getValue();
                if (!(nm instanceof String)) throw new IllegalArgumentException("String is expected, not " + nm);
                if (!(val instanceof String)) throw new IllegalArgumentException("String is expected, not " + val);
                _attrs.add(new String[] { (String) nm, (String) val });
            }
        }
    }

    /** Returns the tag name of this header element.
	 */
    public String getName() {
        return _name;
    }

    /** Returns as HTML tag(s) representing this header element.
	 *
	 * @param page the page containing this header element.
	 * It is used to evaluate EL expression, if any, contained in the value.
	 */
    public String toHTML(Page page) {
        final StringBuffer sb = new StringBuffer(128).append('<').append(_name);
        for (Iterator it = _attrs.iterator(); it.hasNext(); ) {
            final String[] p = (String[]) it.next();
            String nm = p[0];
            String val = (String) Executions.evaluate(page, p[1], String.class);
            if (val == null || val.length() == 0) {
                sb.append(' ').append(nm).append("=\"\"");
            } else {
                if ("href".equals(nm)) val = Executions.encodeURL(val);
                HTMLs.appendAttribute(sb, nm, val);
            }
        }
        return sb.append("/>").toString();
    }
}
