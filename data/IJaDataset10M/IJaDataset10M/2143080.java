package net.community.chest.eclipse.classpath;

import java.util.Collection;
import net.community.chest.dom.DOMUtils;
import net.community.chest.dom.ElementDataComparator;
import net.community.chest.lang.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @param <E> The XML {@link Element} generic type
 * @author Lyor G.
 * @since Nov 22, 2007 12:53:06 PM
 */
public class ClasspathEntryComparator<E extends Element> extends ElementDataComparator<E> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3814269862621694024L;

    public ClasspathEntryComparator(Class<E> nodeClass, boolean ascending, boolean caseSensitive) {
        super(nodeClass, ascending, caseSensitive);
    }

    public ClasspathEntryComparator(Class<E> nodeClass, boolean ascending) {
        this(nodeClass, ascending, true);
    }

    public int comparePathComponent(final String p1, final String p2, final String c) {
        final int cLen = (null == c) ? 0 : c.length();
        if (cLen <= 0) return 0;
        final int p1Len = (null == p1) ? 0 : p1.length(), p2Len = (null == p2) ? 0 : p2.length(), c1Pos = (p1Len < cLen) ? (-1) : p1.indexOf(c), c2Pos = (p2Len < cLen) ? (-1) : p2.indexOf(c);
        if (c1Pos < 0) return (c2Pos < 0) ? 0 : (+1); else if (c2Pos < 0) return (-1);
        return 0;
    }

    public int compareEntryPath(final E e1, final E e2) {
        final String p1 = (null == e1) ? null : e1.getAttribute(ClasspathUtils.CLASSPATHENTRY_PATH_ATTR), p2 = (null == e2) ? null : e2.getAttribute(ClasspathUtils.CLASSPATHENTRY_PATH_ATTR);
        final int p1Len = (null == p1) ? 0 : p1.length(), p2Len = (null == p2) ? 0 : p2.length();
        if (p1Len <= 0) return (p2Len <= 0) ? 0 : (+1); else if (p2Len <= 0) return (-1);
        int nRes = comparePathComponent(p1, p2, "main");
        if (nRes != 0) return nRes;
        if ((nRes = comparePathComponent(p1, p2, "test")) != 0) return nRes;
        final int l1Pos = p1.lastIndexOf('/'), l2Pos = p2.lastIndexOf('/');
        if ((l1Pos > 0) && (l1Pos < (p1Len - 1)) && (l2Pos > 0) && (l2Pos < (p2Len - 1))) {
            final String n1 = p1.substring(l1Pos + 1), n2 = p2.substring(l2Pos + 1);
            if ((nRes = StringUtil.compareDataStrings(n1, n2, false)) != 0) return nRes;
        }
        return p1.compareTo(p2);
    }

    public int compareEntryCombineAccessRules(final E e1, final E e2) {
        final String r1 = (null == e1) ? null : e1.getAttribute(ClasspathUtils.CLASSPATHENTRY_COMBINE_ACC_RULES_ATTR), r2 = (null == e2) ? null : e2.getAttribute(ClasspathUtils.CLASSPATHENTRY_COMBINE_ACC_RULES_ATTR);
        if ((null == r1) || (r1.length() <= 0)) return ((null == r2) || (r2.length() <= 0)) ? 0 : (-1); else if ((null == r2) || (r2.length() <= 0)) return (+1);
        if (isCaseSensitive()) return r1.compareTo(r2); else return r1.compareToIgnoreCase(r2);
    }

    private static final String[] _kindOrder = { ClasspathUtils.SRC_ENTRY_KIND, ClasspathUtils.OUTPUT_ENTRY_KIND, ClasspathUtils.LIB_ENTRY_KIND, ClasspathUtils.VAR_ENTRY_KIND, ClasspathUtils.CON_ENTRY_KIND };

    public int getKindPriority(final String k) {
        if ((null == k) || (k.length() <= 0)) return Short.MAX_VALUE;
        for (int kIndex = 0; kIndex < _kindOrder.length; kIndex++) {
            if (k.equalsIgnoreCase(_kindOrder[kIndex])) return kIndex;
        }
        return Short.MAX_VALUE;
    }

    public int compareEntryKind(final E e1, final E e2) {
        final String k1 = (null == e1) ? null : e1.getAttribute(ClasspathUtils.CLASSPATHENTRY_KIND_ATTR), k2 = (null == e2) ? null : e2.getAttribute(ClasspathUtils.CLASSPATHENTRY_KIND_ATTR);
        if (0 == StringUtil.compareDataStrings(k1, k2, isCaseSensitive())) {
            final int nRes = compareEntryCombineAccessRules(e1, e2);
            if (nRes != 0) return nRes;
            return compareEntryPath(e1, e2);
        }
        final int p1 = getKindPriority(k1), p2 = getKindPriority(k2);
        if (p1 == p2) return 0;
        return p1 - p2;
    }

    @Override
    public int compareValues(E e1, E e2) {
        int nRes = compareTagName(e1, e2);
        if (nRes != 0) return nRes;
        {
            final String tagName = (null == e1) ? null : e1.getTagName();
            if (0 == StringUtil.compareDataStrings(ClasspathUtils.CLASSPATHENTRY_ELEM_NAME, tagName, isCaseSensitive())) {
                if ((nRes = compareEntryKind(e1, e2)) != 0) return nRes;
            }
        }
        final Collection<? extends Element> c1 = DOMUtils.extractAllNodes(Element.class, e1, Node.ELEMENT_NODE), c2 = DOMUtils.extractAllNodes(Element.class, e2, Node.ELEMENT_NODE);
        final int c1Num = (null == c1) ? 0 : c1.size(), c2Num = (null == c2) ? 0 : c2.size();
        if (c1Num == c2Num) return compareAttributes(e1, e2);
        return (c1Num - c2Num);
    }

    public static final ClasspathEntryComparator<Element> CASE_SENSITIVE_CPENTRY = new ClasspathEntryComparator<Element>(Element.class, true, true), CASE_INSENSITIVE_CPENTRY = new ClasspathEntryComparator<Element>(Element.class, true, false);
}
