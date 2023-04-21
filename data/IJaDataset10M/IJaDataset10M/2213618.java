package com.volantis.mcs.protocols.widgets.renderers;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ClockContextHandler;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.DigitalClockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.styling.Styles;

public class DigitalClockDefaultRenderer extends ClockDefaultRenderer {

    public static final ScriptModule WIDGET_CLOCK = createAndRegisterWidgetClock();

    private static ScriptModule createAndRegisterWidgetClock() {
        Set dependencies = new HashSet();
        dependencies.add(WIDGET_CLOCK_COMMON);
        dependencies.add(WidgetScriptModules.BASE_REFRESHABLE);
        ScriptModule module = new ScriptModule("/vfc-clock.mscr", dependencies, 3700, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES = { ActionName.FORCE_SYNC };

    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES = {};

    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES = {};

    private static char specialMarks[] = { 'Y', 'y', 'F', 'm', 'D', 'd', 'h', 'H', 'i', 's', 'A', 'S' };

    private SpanAttributes clockSpanAttributes;

    protected void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        require(WIDGET_CLOCK, protocol, attributes);
        String clockId = attributes.getId();
        if (clockId == null) {
            clockId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        clockSpanAttributes = new SpanAttributes();
        clockSpanAttributes.copy(attributes);
        if (clockSpanAttributes.getId() == null) {
            clockSpanAttributes.setId(clockId);
        }
        protocol.writeOpenSpan(clockSpanAttributes);
    }

    protected void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        DigitalClockAttributes digitalClockAttributes = (DigitalClockAttributes) attributes;
        if (digitalClockAttributes.getRefreshAttributes() != null) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }
        protocol.writeCloseSpan(clockSpanAttributes);
        List contentAttributes = digitalClockAttributes.getContentAttributes();
        ClockContextHandler clockContextHandler = new ClockContextHandler();
        clockContextHandler.mergeContentAttributes(contentAttributes);
        String[] digits = clockContextHandler.getClockContentIds("digits");
        String[] days = clockContextHandler.getClockContentIds("days");
        String[] months = clockContextHandler.getClockContentIds("months");
        String[] ampm = clockContextHandler.getClockContentIds("ampm");
        String[] separators = clockContextHandler.getClockContentIds("separators");
        Styles styles = digitalClockAttributes.getStyles();
        StylesExtractor stylesExtractor = createStylesExtractor(protocol, styles);
        String dateTimeFormat = getDateTimeFormat(stylesExtractor.getDateTimeFormat(), specialMarks);
        RefreshAttributes refreshAttributes = digitalClockAttributes.getRefreshAttributes();
        StringBuffer textRefreshAttr = new StringBuffer();
        if (refreshAttributes != null) {
            textRefreshAttr.append(", refreshURL: ").append(createJavaScriptString(refreshAttributes.getSrc())).append(", refreshInterval: " + refreshAttributes.getInterval());
        }
        StringBuffer textBuffer = new StringBuffer();
        textBuffer.append(createJavaScriptWidgetRegistrationOpening(digitalClockAttributes.getId())).append("new Widget.Clock(").append(createJavaScriptString(clockSpanAttributes.getId())).append(", {").append("format: [").append(dateTimeFormat).append("], ").append("digits: [").append(splitIds(digits)).append("], ").append("days: [").append(splitIds(days)).append("], ").append("months: [").append(splitIds(months)).append("], ").append("ampm: [").append(splitIds(ampm)).append("], ").append("separators: [").append(splitIds(separators)).append("]").append(textRefreshAttr).append("})").append(createJavaScriptWidgetRegistrationClosure());
        addCreatedWidgetId(clockSpanAttributes.getId());
        addUsedWidgetIds(digits);
        addUsedWidgetIds(days);
        addUsedWidgetIds(months);
        addUsedWidgetIds(ampm);
        addUsedWidgetIds(separators);
        writeJavaScript(textBuffer.toString());
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
}
