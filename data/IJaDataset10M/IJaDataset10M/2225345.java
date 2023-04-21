package net.community.chest.ui.helpers.tabbed;

import org.w3c.dom.Element;
import net.community.chest.dom.DOMUtils;
import net.community.chest.dom.proxy.AbstractXmlProxyConverter;
import net.community.chest.swing.component.tabbed.JTabbedPaneReflectiveProxy;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <P> The reflected {@link HelperTabbedPane} instance
 * @author Lyor G.
 * @since Dec 23, 2008 9:12:24 AM
 */
public class HelperTabbedPaneReflectiveProxy<P extends HelperTabbedPane> extends JTabbedPaneReflectiveProxy<P> {

    public HelperTabbedPaneReflectiveProxy(Class<P> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    protected HelperTabbedPaneReflectiveProxy(Class<P> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }

    public static final String TAB_ELEM_NAME = "tab";

    public static final boolean isDefaultTabElement(final Element elem) {
        return AbstractXmlProxyConverter.isDefaultMatchingElement(elem, (null == elem) ? null : elem.getTagName(), TAB_ELEM_NAME);
    }

    public String getSectionName(P src, Element elem) {
        if ((null == src) || (null == elem)) return null;
        return elem.getAttribute(NAME_ATTR);
    }

    @Override
    public P handleUnknownXmlChild(P src, Element elem) throws Exception {
        final String n = getSectionName(src, elem);
        if ((n != null) && (n.length() > 0)) {
            final Element prev = src.addSection(n, elem);
            if (prev != null) throw new IllegalStateException("handleUnknownXmlChild(" + n + "[" + DOMUtils.toString(elem) + "] duplicate section found: " + DOMUtils.toString(prev));
            return src;
        }
        return super.handleUnknownXmlChild(src, elem);
    }

    public static final HelperTabbedPaneReflectiveProxy<HelperTabbedPane> TABBEDHLPR = new HelperTabbedPaneReflectiveProxy<HelperTabbedPane>(HelperTabbedPane.class, true);
}
