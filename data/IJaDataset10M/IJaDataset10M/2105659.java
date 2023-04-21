package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;

/**
 * An OpenWave specific fragment link renderer which renders "numeric
 * shortcut" style fragment links.
 * <p/>
 * NOTE: given that these fragment links are usually part of a list, it would
 * be nicer (and more space efficient) to render them as a multiple select
 * list rather than individual single select lists.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 */
class OpenWaveNumericShortcutFragmentLinkRenderer extends FragmentLinkRenderer {

    public OpenWaveNumericShortcutFragmentLinkRenderer(WMLFragmentLinkRendererContext context) {
        super(context);
    }

    public void doFragmentLink(OutputBuffer outputBuffer, FraglinkAttributes attributes) throws ProtocolException {
        DOMOutputBuffer dom = (DOMOutputBuffer) outputBuffer;
        WMLFragmentLinkRendererContext ctx = (WMLFragmentLinkRendererContext) context;
        Styles styles = attributes.getStyles();
        styles.getPropertyValues().setComputedValue(StylePropertyDetails.WHITE_SPACE, WhiteSpaceKeywords.NOWRAP);
        Element p = dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, styles);
        Element select = dom.openElement("select");
        ctx.addTitleAttribute(select, attributes);
        Element option = dom.openElement("option");
        ctx.addTitleAttribute(option, attributes);
        LinkAssetReference reference = attributes.getHref();
        if (reference != null) {
            option.setAttribute("onpick", reference.getURL());
        }
        dom.addOutputBuffer((DOMOutputBuffer) attributes.getLinkText());
        dom.closeElement(option);
        dom.closeElement(select);
        dom.closeElement(p);
    }
}
