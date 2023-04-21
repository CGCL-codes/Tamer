package com.volantis.mcs.protocols.widgets.renderers;

import java.io.IOException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.BlockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.internal.renderers.EffectBlockDefaultRenderer;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Container widget suitable for HTML protocols.
 */
public class BlockContentDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES = {};

    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES = {};

    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES = {};

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(EffectBlockDefaultRenderer.class);

    private com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes blockContentAttributes;

    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        BlockContentAttributes myAttributes = (BlockContentAttributes) attributes;
        require(WidgetScriptModules.BASE_BB_CONTENT, protocol, attributes);
        blockContentAttributes = new com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes();
        blockContentAttributes.copy(attributes);
        blockContentAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        if (myAttributes.isForResponse()) {
            if (myAttributes.getId() == null) {
                myAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
            }
        }
        getWidgetDefaultModule(protocol).getWidgetRenderer(blockContentAttributes).renderOpen(protocol, blockContentAttributes);
        openingBlockContent(protocol);
    }

    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        String parentBlockId = closingBlockContent(protocol);
        getWidgetDefaultModule(protocol).getWidgetRenderer(blockContentAttributes).renderClose(protocol, blockContentAttributes);
        StringBuffer buffer = new StringBuffer();
        if (attributes.getId() != null) {
            buffer.append("Widget.register(").append(createJavaScriptString(attributes.getId())).append(",");
            addCreatedWidgetId(attributes.getId());
        }
        StylesExtractor baseStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        StylesExtractor concealedStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        concealedStylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        buffer.append("new Widget.BlockContent(").append(createJavaScriptWidgetReference(blockContentAttributes.getId())).append(",{").append(getAppearableOptions(baseStylesExtractor)).append(",").append(getDisappearableOptions(concealedStylesExtractor));
        if (parentBlockId != null) {
            buffer.append(",parentBlock:").append(createJavaScriptWidgetReference(parentBlockId));
            addUsedWidgetId(parentBlockId);
        }
        buffer.append("})");
        addUsedWidgetId(blockContentAttributes.getId());
        if (attributes.getId() != null) {
            buffer.append(")");
        }
        BlockContentAttributes myAttributes = (BlockContentAttributes) attributes;
        if (myAttributes.isForResponse()) {
            buffer.append("; this.blockContent = Widget.getInstance('" + myAttributes.getId() + "')");
        }
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }

    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }

    protected BlockDefaultRenderer getBlockDefaultRenderer(VolantisProtocol protocol) throws ProtocolException {
        return (BlockDefaultRenderer) getWidgetDefaultModule(protocol).getWidgetRenderer(BlockAttributes.class);
    }

    private void openingBlockContent(VolantisProtocol protocol) throws ProtocolException {
        getBlockDefaultRenderer(protocol).openingBlockContent();
    }

    private String closingBlockContent(VolantisProtocol protocol) throws ProtocolException {
        return getBlockDefaultRenderer(protocol).closingBlockContent();
    }

    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
