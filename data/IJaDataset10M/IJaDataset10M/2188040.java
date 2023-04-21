package net.community.chest.ui.helpers.panel;

import net.community.chest.dom.DOMUtils;
import net.community.chest.swing.component.panel.BasePanelReflectiveProxy;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * <P>Any unknown child is added to the sections map</P>
 * 
 * @param <P> The reflected {@link HelperPanel} instance
 * @author Lyor G.
 * @since Dec 11, 2008 3:35:03 PM
 */
public class HelperPanelReflectiveProxy<P extends HelperPanel> extends BasePanelReflectiveProxy<P> {

    public HelperPanelReflectiveProxy(Class<P> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    protected HelperPanelReflectiveProxy(Class<P> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
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

    public static final HelperPanelReflectiveProxy<HelperPanel> HLPRPNL = new HelperPanelReflectiveProxy<HelperPanel>(HelperPanel.class, true) {

        @Override
        public HelperPanel fromXml(Element elem) throws Exception {
            return (null == elem) ? null : new HelperPanel(elem);
        }
    };
}
