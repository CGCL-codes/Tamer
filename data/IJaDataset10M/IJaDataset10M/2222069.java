package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import java.util.Arrays;
import java.util.List;

public class ColumnODOMSelectionFilter extends ODOMSelectionFilter {

    /**
     * Create an ODOMElement that is a placeholder for no matches for this
     * filter. This is necessary because the obvious choice (null value) is
     * ignored in {@link com.volantis.mcs.eclipse.common.odom.DefaultODOMElementSelectionProvider#update}.
     */
    private static final ODOMElement NON_COLUMN = new ODOMElement(ODOMElement.NULL_ELEMENT_NAME);

    /**
     * Store the list of possible parent elements.
     */
    private static List parentMatchList = Arrays.asList(new Object[] { LayoutSchemaType.GRID_FORMAT_ROWS_ELEMENT.getName(), LayoutSchemaType.SEGMENT_GRID_FORMAT_ROW_ELEMENT.getName() });

    /**
     * Store an element filter that is used to obtain the element index
     * predicate.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * Store the standard namespace prefix.
     */
    private static final String PREFIX = MCSNamespace.LPDM.getPrefix() + ":";

    /**
     * Store the xpath part 1 (up to and including the first '[').
     */
    private static final String XPATH_PART_1 = "../../" + PREFIX + LayoutSchemaType.GRID_FORMAT_COLUMNS_ELEMENT.getName() + "/" + PREFIX + LayoutSchemaType.GRID_FORMAT_COLUMN_ELEMENT.getName() + "[";

    /**
     * Store the xpath part 2 (from the first ']' to the next '[').
     */
    private static final String XPATH_PART_2 = "]|../../" + PREFIX + LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT.getName() + "/" + PREFIX + LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMN_ELEMENT.getName() + "[";

    /**
     * Store the xpath part 3 (just the last ']').
     */
    private static final String XPATH_PART_3 = "]";

    /**
     * Store the namespaces used to construct the XPath.
     */
    private static final Namespace[] NAMESPACES = new Namespace[] { MCSNamespace.LPDM };

    /**
     * Default constructor that initializes the selection filter with no
     * resolver or array filter names.
     */
    public ColumnODOMSelectionFilter() {
        super(null, null);
    }

    public ODOMElement resolve(ODOMElement element) throws XPathException {
        ODOMElement resolved = NON_COLUMN;
        final Element parent = element.getParent();
        if (parent != null && parentMatchList.contains(parent.getName())) {
            int index = getIndexPosition(element);
            if (index != -1) {
                resolved = resolvePathToElement(element, index);
            }
        }
        return resolved;
    }

    /**
     * Helper method to resolve the path to an ODOM element. We need to locate
     * the column element that this element obtains its attributes from. We do
     * this by selecting the node that matches the XPath:
     * <pre>
     *  '../../segmentGridFormatColumns/segmentGridFormatColumn[predicate] |
     *   ../../gridFormatColumns/gridFormatColumn[predicate]'
     * </pre>
     *
     * where the predicate is the index of the current element relative to its
     * siblings.
     *
     * @param element the odom element to use to resolve to.
     * @param index   the column index for the current element.
     * @return the resolved element, or NON_COLUMN if this cannot be resovled.
     * @throws com.volantis.mcs.xml.xpath.XPathException if the resolve process resolves to a non
     *                        ODOMElement or more than one element.
     */
    private ODOMElement resolvePathToElement(ODOMElement element, int index) throws XPathException {
        ODOMElement resolved = NON_COLUMN;
        StringBuffer xpath = new StringBuffer(XPATH_PART_1);
        xpath.append(index).append(XPATH_PART_2);
        xpath.append(index).append(XPATH_PART_3);
        XPath xpathResolver = new XPath(xpath.toString(), NAMESPACES);
        List resolvedElements = xpathResolver.selectNodes(element);
        if (resolvedElements != null && resolvedElements.size() > 0) {
            if (resolvedElements.size() == 1) {
                Object node = resolvedElements.get(0);
                if (node instanceof ODOMElement) {
                    resolved = (ODOMElement) node;
                } else {
                    throw new XPathException("Unexpected node type (" + node + ")");
                }
            } else {
                throw new XPathException("Element (" + element + ") was resolved to more " + "than one element.");
            }
        }
        return resolved;
    }

    /**
     * Determine the index position of this current element relative to its
     * siblings. Note that if the element has two adjacent element siblings,
     * then this method should return 2 as the index position (the element
     * must be the second sibling).
     *
     * @param element the element to determine its index position relative to
     *                its siblings.
     * @return the index position or -1 if one cannot be determined.
     */
    private int getIndexPosition(ODOMElement element) {
        int result = -1;
        Element parent = element.getParent();
        if (parent != null) {
            result = parent.getContent(ELEMENT_FILTER).indexOf(element) + 1;
        }
        return result;
    }
}
