package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Wizard widget element.
 */
public class WizardElement extends WidgetElement implements Launchable {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(WidgetElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(WidgetElement.class);

    /**
     * The container instance used as the main container
     */
    private RegionInstance anonymousRegionInstance;

    /**
     * Creates and returns new instance of WizardElement, 
     * initalised with empty attributes.
     * @param context
     */
    public WizardElement(XDIMEContextInternal context) {
        super(WidgetElements.WIZARD, context);
        protocolAttributes = new WizardAttributes();
    }

    /**
     *  Add id and type for all dismiss elements in popup content
     */
    public void addLaunch(LaunchAttributes attrs) {
        ((WizardAttributes) protocolAttributes).addLaunch(attrs);
    }

    /**
     * Get id attriobute from wizard element
     * 
     * @return id wizard
     */
    public String getWidgetId() {
        return protocolAttributes.getId();
    }

    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        ((WizardAttributes) protocolAttributes).setCancelDialog(attributes.getValue("", "cancel-dialog"));
    }

    public XDIMEResult doElementStart(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        MarinerPageContext pageContext = getPageContext(context);
        anonymousRegionInstance = createAnonymousRegion(pageContext);
        pageContext.pushContainerInstance(anonymousRegionInstance);
        OutputBuffer outputBuffer = anonymousRegionInstance.getCurrentBuffer();
        pageContext.pushOutputBuffer(outputBuffer);
        return super.doElementStart(context, attributes);
    }

    public XDIMEResult doElementEnd(XDIMEContextInternal context) throws XDIMEException {
        MarinerPageContext pageContext = getPageContext(context);
        XDIMEResult result = super.doElementEnd(context);
        pageContext.popContainerInstance(anonymousRegionInstance);
        OutputBuffer anonymousBuffer = anonymousRegionInstance.getCurrentBuffer();
        pageContext.popOutputBuffer(anonymousBuffer);
        DeviceLayoutContext layoutToPop = pageContext.getDeviceLayoutContext();
        pageContext.popDeviceLayoutContext();
        ContainerInstance containingInstance = pageContext.getCurrentContainerInstance();
        containingInstance.getCurrentBuffer().transferContentsFrom(this.anonymousRegionInstance.getCurrentBuffer());
        return result;
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        WidgetModule widgetModule = getWidgetModule(context);
        if (null == widgetModule) {
            return XDIMEResult.SKIP_ELEMENT_BODY;
        }
        try {
            WizardRenderer wizardRenderer = widgetModule.getWizardRenderer();
            if (null == wizardRenderer) {
                return XDIMEResult.SKIP_ELEMENT_BODY;
            }
            wizardRenderer.renderOpen(getProtocol(context), (WidgetAttributes) protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format("rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }
}
