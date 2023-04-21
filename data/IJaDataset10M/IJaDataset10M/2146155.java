package com.volantis.mcs.xdime.widgets;

import java.io.IOException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentImageAssetReference;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.renderers.TabsRenderer;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Tab XDIME2 element
 */
public class TabElement extends WidgetElement implements Loadable {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(TabElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(TabElement.class);

    public TabElement(XDIMEContextInternal context) {
        super(WidgetElements.TAB, context);
        protocolAttributes = new TabAttributes();
    }

    public void setLoadAttributes(LoadAttributes attrs) {
        getTabAttributes().setLoadAttributes(attrs);
    }

    public TabAttributes getTabAttributes() {
        return ((TabAttributes) protocolAttributes);
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        try {
            WidgetModule widgetModule = getWidgetModule(context);
            if (null == widgetModule) {
                return doFallbackOpen(context, attributes);
            }
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer) {
                return doFallbackOpen(context, attributes);
            }
            tabsRenderer.renderTabOpen(getProtocol(context), (WidgetAttributes) protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format("rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
        WidgetModule widgetModule = getWidgetModule(context);
        if (null == widgetModule) {
            doFallbackClose(context);
            return;
        }
        try {
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer) {
                doFallbackClose(context);
                return;
            }
            tabsRenderer.renderTabClose(getProtocol(context), (WidgetAttributes) protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format("rendering-error", getTagName()), e);
        }
    }

    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) throws ProtocolException {
        VolantisProtocol protocol = getProtocol(context);
        protocol.writeOpenDiv(new DivAttributes());
        renderTabLabel(protocol);
        protocol.writeCloseDiv(new DivAttributes());
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * Render label text from CSS ::mcs-label content
     * @param protocol VolantisProtocol
     * @throws ProtocolException
     */
    private void renderTabLabel(VolantisProtocol protocol) throws ProtocolException {
        String textLabel = "";
        Styles tabStyles = protocolAttributes.getStyles();
        Styles labelStyles = (null == tabStyles ? null : tabStyles.getNestedStyles(PseudoElements.MCS_LABEL));
        StyleValue labelContent = (null == labelStyles ? null : labelStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT));
        if (null != labelContent) {
            if (labelContent instanceof StyleList) {
                StyleList contentList = (StyleList) labelContent;
                if (contentList.getList().isEmpty()) {
                    throw new ProtocolException(exceptionLocalizer.format("widget-tab-label-empty-content"));
                }
                labelContent = (StyleValue) contentList.getList().get(0);
                if (labelContent.getStyleValueType() == StyleValueType.STRING) {
                    textLabel = ((StyleString) labelContent).getString();
                } else {
                    textLabel = labelContent.getStandardCSS();
                }
            } else if (labelContent instanceof StyleComponentURI) {
                PolicyReferenceResolver referenceResolver = protocol.getMarinerPageContext().getPolicyReferenceResolver();
                AssetResolver assetResolver = protocol.getMarinerPageContext().getAssetResolver();
                RuntimePolicyReference policyReference = referenceResolver.resolvePolicyExpression(((StyleComponentURI) labelContent).getExpression());
                DefaultComponentImageAssetReference imageAsset = new DefaultComponentImageAssetReference(policyReference, assetResolver);
                TextAssetReference textAsset = imageAsset.getTextFallback();
                if (null == textAsset) {
                    throw new ProtocolException(exceptionLocalizer.format("missing-policy-failure", policyReference.getName()));
                }
                textLabel = textAsset.getText(TextEncoding.PLAIN);
                if (null == textLabel) {
                    textLabel = "";
                }
            } else if (labelContent instanceof StyleString) {
                textLabel = ((StyleString) labelContent).getString();
            }
        }
        try {
            protocol.getContentWriter().write(textLabel);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }
    }
}
