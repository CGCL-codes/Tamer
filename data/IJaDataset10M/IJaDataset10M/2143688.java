package com.volantis.mcs.protocols;

import com.volantis.mcs.css.version.CSSProperty;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityConstants;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeAndOrElementStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultStyleEmulationPropertiesRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.ElementOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.OnOffKeywordElementPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.href.NoOpRuleSet;
import com.volantis.mcs.protocols.html.css.emulator.renderer.MarqueeEmulationAttributePropertyRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.MarqueeEmulationRenderer;
import com.volantis.mcs.protocols.trans.DefaultStyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.StyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.mcs.utilities.ProductVersion;
import com.volantis.mcs.utilities.VolantisVersion;
import com.volantis.mcs.dissection.Dissector;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a ProtocolConfiguration
 *
 * @mock.generate
 */
public class ProtocolConfigurationImpl implements ProtocolConfiguration {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(ProtocolConfigurationImpl.class);

    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    /**
     * Flag which indicates whether empty elements need a space before the
     * />.
     */
    protected boolean emptyElementRequiresSpace;

    /**
     * The set of elements which are always empty.
     */
    protected final Set alwaysEmptyElements;

    /**
     * The set of elements which must never be split.
     */
    protected final Set atomicElements;

    /**
     * The set of elements which are blocky. Subclasses are expected to
     * populate this data structure.
     */
    protected final Set blockyElements;

    /**
     * The set of element attributes which the protocol may generate that
     * may contain asset URLs.
     */
    protected final ElementAttributeMapper assetURLLocations;

    /**
     * The old style dissector that is used by this protocol.
     */
    private final Dissector dissector;

    /**
     * The set of style properties which have been deemed important (i.e.
     * their values should not be lost during optimization) for this protocol.
     */
    protected final Set importantProperties;

    /**
     * flag value to show if a protocol supports subscript elements
     */
    protected boolean canSupportSubScriptElement;

    /**
     * flag value to show if a protocol supports superscript elements
     */
    protected boolean canSupportSuperScriptElement;

    /**
     * This flag specifies whether this protocol supports events at all, if
     * they do then the following bit masks need setting accordingly.
     */
    protected boolean canSupportEvents;

    /**
     * Flag which specifies whether the protocol supports file upload
     */
    private boolean canSupportFileUpload = false;

    /**
     * These bit masks define the events which are supported by this
     * protocol.
     */
    protected int supportedGeneralEvents;

    /**
     * flag value to indicate a protocols support of javascript.
     */
    private boolean canSupportJavaScript;

    /**
     * Set that includes the Href ruleset used by the href transformer
     */
    protected HashMap hrefRuleSet;

    /**
     * Set that includes the highlight ruleset used by the highlight transformer
     */
    protected HashMap highlightRuleSet;

    /**
     * Set that includes the corners ruleset used by the Corners transformer
     */
    protected HashMap cornersRuleSet;

    /**
     * The object which manages the capabilities of the device
     */
    protected DeviceCapabilityManager deviceCapabilityManager;

    /**
     * The object which describes the CSS version that the protocol supports.
     */
    private CSSVersion cssVersion;

    /**
     * The style sheet extractor configuration.
     */
    private ExtractorConfiguration extractorConfiguration;

    /**
     * Flag which specifies whether the protocol supports Framework Client
     */
    private boolean supportsFrameworkClient = false;

    /**
     * Flag which specifies whether the protocol supports viewport virtual support;
     */
    private String supportsViewportVirtualSuport;

    /**
     * Flag which specifies version of Client Framework since
     * it is supported by the protocol
     */
    private String supportsFrameworkClientSince = DevicePolicyConstants.SUPPORTS_VFC_SINCE_DEFAULT;

    /**
     * Variable holds pixelsX for device, default value is -1.
     */
    private int devicePixelsX = -1;

    /**
      * The style property to rule mapping.
      */
    protected final DefaultStyleEmulationPropertiesRenderer styleEmulationPropertyRendererSelector;

    /**
     * The style emulation element configuration instance.
     */
    protected DefaultStyleEmulationElementConfiguration styleEmulationElements;

    private String cssMedia;

    /**
     * The maximum frame rate supported, expressed in Hz.
     * Value Double.POSITIVE_INFINITY means no frame rate limit.
     */
    private double maxClientFrameRate;

    protected final Set defaultElementCapabilities = new HashSet();

    /**
     * Need to maintain a list of the emulation elements that can be used by
     * this protocol. This is reasonable, because even though they are not
     * supported by the protocol by default, they will only be used if a) the
     * device explicitly says it supports them and b) explicitly says it
     * doesn't support the equivalent CSS.
     * This list is added to the list of elements that are allowable children
     * of stylistic elements (i.e. b, i) which is used to configure the
     * StyleEmulationElementConfiguration. If the emulation elements were not
     * included, then they would be removed rather than pushed down if they
     * were not valid in that position.
     * */
    private final Set fakePermittedChildren = new HashSet();

    /**
     * The default capability for the td element.
     */
    private final DeviceElementCapability DEFAULT_TD_CAPABILITY = new DeviceElementCapability("td", CapabilitySupportLevel.FULL);

    /**
     * Map of elements which cannot contain form links (i.e. links between form
     * fragments) and the child element which should be inserted in order to
     * resolve this. Used by {@link DOMProtocol#validateFormLinkParent}.
     * <p/>
     * NB: This may not be a one step process. For example, in XHTMLBasic,
     * table contain a form link so a child tr should be inserted. However a tr
     * also cannot contain a form link, so a child td should be inserted. The
     * td can contain a form link, so our work is done.
     */
    protected final Map invalidFormLinkParents = new HashMap();

    /**
     * The optional DTD.
     */
    protected DTD dtd;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param supportsDissection Specifies whether the protocol supports
     *                           dissection.
     */
    protected ProtocolConfigurationImpl(boolean supportsDissection) {
        alwaysEmptyElements = new HashSet();
        atomicElements = new HashSet();
        assetURLLocations = new ElementAttributeMapper();
        importantProperties = new HashSet();
        blockyElements = new HashSet();
        if (supportsDissection) {
            dissector = new Dissector();
        } else {
            dissector = null;
        }
        hrefRuleSet = new NoOpRuleSet();
        highlightRuleSet = new NoOpRuleSet();
        cornersRuleSet = new NoOpRuleSet();
        styleEmulationPropertyRendererSelector = new DefaultStyleEmulationPropertiesRenderer();
    }

    /**
     * Initialise. 
     *
     */
    public ProtocolConfigurationImpl() {
        this(false);
    }

    /**
     * Initialize the device (for example, by configuring the style emulation
     * renderers that are required).
     *
     * @param device    encapsulates the device information
     * @param builder   used to
     */
    public void initialize(InternalDevice device, DeviceCapabilityManagerBuilder builder) {
        defaultElementCapabilities.add(DEFAULT_TD_CAPABILITY);
        builder.addDefaultValues(defaultElementCapabilities);
        deviceCapabilityManager = builder.build();
        registerStyleEmulationPropertyRenderers(device);
        createStyleEmulationElements();
        registerMaxClientFrameRate(device);
        this.dtd = createDTD(device);
    }

    /**
     * Create the DTD for this protocol for the specified device.
     *
     * @param device The device for which the DTD is to be created.
     * @return The DTD, or null if DTD is not supported.
     */
    protected DTD createDTD(InternalDevice device) {
        DTDBuilder builder = new XMLDTDBuilder();
        builder.setMaximumLineLength(getMaximumLineLength(device));
        addContentModel(builder, device);
        return builder.buildDTD();
    }

    protected void addContentModel(DTDBuilder builder, InternalDevice device) {
        builder.setEmptyTagRequiresSpace(emptyElementRequiresSpace);
        builder.addIgnorableElement(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        builder.addIgnorableElement(DissectionConstants.KEEPTOGETHER_ELEMENT);
        builder.addIgnorableElement(null);
    }

    /**
     * Get the maximum line length for the requesting device.
     *
     * @return the maximum line length in characters for the requesting
     *         device.
     */
    protected int getMaximumLineLength(InternalDevice device) {
        String lineLength = device.getPolicyValue(DevicePolicyConstants.MAXIMUM_LINE_CHARS);
        int result = 0;
        if ((lineLength != null) && (lineLength.length() > 0)) {
            result = Integer.parseInt(lineLength);
        }
        return result;
    }

    public DeviceCapabilityManager getDeviceCapabilityManager() {
        return deviceCapabilityManager;
    }

    /**
     * Set the deviceCapabilityManager
     * @param deviceCapabilityManager
     */
    public void setDeviceCapabilityManager(DeviceCapabilityManager deviceCapabilityManager) {
        this.deviceCapabilityManager = deviceCapabilityManager;
    }

    /**
     * Get the value of the emptyElementRequiresSpace property.
     *
     * @return The value of the emptyElementRequiresSpace property.
     */
    public boolean getEmptyElementRequiresSpace() {
        return emptyElementRequiresSpace;
    }

    /**
     * This method is called with the name of an element and returns true if
     * it is always empty and false otherwise.
     *
     * @param name The name of an element.
     * @return True if the element is empty and false otherwise.
     */
    public boolean isElementAlwaysEmpty(String name) {
        return alwaysEmptyElements.contains(name);
    }

    /**
     * This method is called with the name of an element and returns true if
     * the element is atomic and should not be split and false if it can be.
     *
     * @param name The name of an element.
     * @return True if the element is atomic and false if it can be split.
     */
    public boolean isElementAtomic(String name) {
        return atomicElements.contains(name);
    }

    /**
     * Finds candidate element attribute values which could be an asset URL
     * and submits them to the given PackageResources object.
     * This utilizes the {@link #assetURLLocations} member to determine if the
     * given element has one or more candidate attributes.
     *
     * @param element          the DOM element to be checked for candidate attributes
     * @param packageResources the PackageResources instance to which candidate
     *                         asset URLs should be submitted
     */
    public void addCandidateElementAssetURLs(Element element, PackageResources packageResources) {
        if (packageResources != null) {
            Set assetURLattributes = assetURLLocations.getElementAttributes(element.getName());
            if (assetURLattributes != null) {
                Attribute elementAttribute = element.getAttributes();
                while (elementAttribute != null) {
                    if (assetURLattributes.contains(elementAttribute.getName())) {
                        packageResources.addEncodedURLCandidate(elementAttribute.getValue());
                    }
                    elementAttribute = elementAttribute.getNext();
                }
            }
        }
    }

    public ValidationHelper getValidationHelper() {
        return null;
    }

    public Dissector getDissector() {
        return dissector;
    }

    public boolean optimisationWouldLoseImportantStyles(Element element) {
        boolean wouldLoseStyles = false;
        if (importantProperties != null && element != null && element.getStyles() != null) {
            wouldLoseStyles = StylePropertyAnalyser.getInstance().hasVisuallyImportantProperty(importantProperties, element);
        }
        return wouldLoseStyles;
    }

    /**
     * Get the default style value associated with a given style property
     * on a given element
     *
     * @param elementType
     * @param property
     * @return String - default style value
     *         todo refactor to use data structure supporting all elements
     */
    public boolean isElementAttributeDefaultStyle(String elementType, StyleProperty property, StyleValue styleValue) {
        boolean defaultValue = false;
        if (elementType != null) {
            if (elementType.equals("sub")) {
                StyleValue fontSizeValue = STYLE_VALUE_FACTORY.getString(null, ".83em");
                StyleValue subValue = STYLE_VALUE_FACTORY.getString(null, "sub");
                if (property.equals(StylePropertyDetails.FONT_SIZE) && styleValue.equals(fontSizeValue)) {
                    defaultValue = true;
                } else if (property.equals(StylePropertyDetails.VERTICAL_ALIGN) && styleValue.equals(subValue)) {
                    defaultValue = true;
                }
            } else if (elementType.equals("sup")) {
                StyleValue fontSizeValue = STYLE_VALUE_FACTORY.getString(null, ".83em");
                StyleValue supValue = STYLE_VALUE_FACTORY.getString(null, "sup");
                if (property.equals(StylePropertyDetails.FONT_SIZE) && styleValue.equals(fontSizeValue)) {
                    defaultValue = true;
                } else if (property.equals(StylePropertyDetails.VERTICAL_ALIGN) && styleValue.equals(supValue)) {
                    defaultValue = true;
                }
            }
        }
        return defaultValue;
    }

    public boolean isElementBlocky(String name) {
        return blockyElements.contains(name);
    }

    /**
     * Does the protocol support events
     *
     * @return boolean
     */
    public boolean supportsEvents() {
        return canSupportEvents;
    }

    public int getSupportedGeneralEvents() {
        return supportedGeneralEvents;
    }

    /**
     * Describes if a protocol supports the use of the SubScript element
     *
     * @return true iff the device does support the SubScript element
     */
    public boolean getCanSupportSubScriptElement() {
        return canSupportSubScriptElement;
    }

    /**
     * Describes if a protocol supports the use of the SuperScript element
     *
     * @return true iff the device does support the SuperScript element
     */
    public boolean getCanSupportSuperScriptElement() {
        return canSupportSuperScriptElement;
    }

    /**
     * Set the value of canSupportJavaScript. This field determines the value
     * of the canSupportEvents.
     *
     * @param canSupportJavaScript
     */
    public void setCanSupportJavaScript(boolean canSupportJavaScript) {
        this.canSupportJavaScript = canSupportJavaScript;
        this.canSupportEvents = canSupportJavaScript;
    }

    /**
     * determines if the protocol supports javascript
     *
     * @return
     */
    public boolean supportsJavaScript() {
        return canSupportJavaScript;
    }

    /**
     * Return the ruleset used by the Href transformer to handle href
     * attributes on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHrefTransformationRules() {
        return hrefRuleSet;
    }

    public CSSVersion getCssVersion() {
        return cssVersion;
    }

    public StyleEmulationElementConfiguration getStyleEmulationElementConfiguration() {
        return styleEmulationElements;
    }

    /**
     * @see #cssVersion
     */
    public void setCssVersion(CSSVersion cssVersion) {
        this.cssVersion = cssVersion;
    }

    public ExtractorConfiguration getExtractorConfiguration() {
        return extractorConfiguration;
    }

    public void setExtractorConfiguration(ExtractorConfiguration extractorConfiguration) {
        this.extractorConfiguration = extractorConfiguration;
    }

    /**
     * Set whether or not this protocol supports client framework.
     */
    public void setFrameworkClientSupported(boolean isFrameworkClientSupported) {
        this.supportsFrameworkClient = isFrameworkClientSupported;
    }

    /**
     * Set viewport virtual support flag
     */
    public void setViewportVirtualSupport(String viewportVirtualSuport) {
        this.supportsViewportVirtualSuport = viewportVirtualSuport;
    }

    /**
     * Returns whether or not this protocol supports client framework 
     * in current MCS version and false otherwise. 
     * @return true if Framework Client is supported; false otherwise
     */
    public boolean isFrameworkClientSupported() {
        if (!supportsFrameworkClient) {
            return false;
        }
        ProductVersion current = VolantisVersion.getProductVersion();
        ProductVersion supportsSince = ProductVersion.parse(supportsFrameworkClientSince);
        return (null != supportsSince) ? current.isGreaterOrEqual(supportsSince) : false;
    }

    /**
     * Returns whether or not this protocol supports client framework.
     *
     * @return true if Framework Client is supported; false otherwise
     */
    public boolean getFrameworkClientSupported() {
        return this.supportsFrameworkClient;
    }

    /**
     * Set version of CFW since from this protocol supports client framework.
     */
    public void setFrameworkClientSupportedSince(String frameworkClientSupportedSince) {
        this.supportsFrameworkClientSince = frameworkClientSupportedSince;
    }

    /**
     * Returns version of CFW since from this protocol supports client framework.
     * 
     * @return version of CFW of none if device is not supported
     */
    public String getFrameworkClientSupportedSince() {
        return this.supportsFrameworkClientSince;
    }

    /**
     * Return the style emulation property renderer selector for this
     * protocol. It may be null if no styles need to be emulated for this
     * protocol.
     */
    public StyleEmulationPropertiesRenderer getStyleEmulationPropertyRendererSelector() {
        return styleEmulationPropertyRendererSelector;
    }

    /**
     * Initialize the rule mappings for this protocol.
     * @param device
     */
    private void registerStyleEmulationPropertyRenderers(InternalDevice device) {
        if (device == null) {
            LOGGER.warn("cannot-configure-style-emulators");
            return;
        }
        registerAttributeOnlyStyleEmulationPropertyRenderers(device);
        registerAttributeAndOrElementStyleEmulationPropertyRenderers(device);
        registerElementOnlyStyleEmulationPropertyRenderers(device);
    }

    /**
     * Registers the style property emulators that just add attributes. These
     * require access to the the protocol element which has just been rendered
     * for this XDIME/PAPI element in order to add atttributes to it. These
     * renderers should be registered first.
     *
     * @param device
     */
    protected void registerAttributeOnlyStyleEmulationPropertyRenderers(InternalDevice device) {
    }

    /**
     * Registers the style property emulators that can add elements and/or
     * attributes. These require access to the the protocol element which has
     * just been rendered for this XDIME/PAPI element only when rendering
     * attributes.ust add attributes. These renderers should be registered
     * after the attribute only emulators and before the element only ones.
     *
     * @param device
     */
    protected void registerAttributeAndOrElementStyleEmulationPropertyRenderers(InternalDevice device) {
        registerMarqueeEmulationRenderers(device);
    }

    /**
     * Registers the style property emulators that just add elements. These
     * have no dependency on the protocol element which has just been rendered
     * for this XDIME/PAPI element, as they do not need to add anything to it.
     * Thus, if, for example, a 'font' element has been added it will not
     * disturb these renderers.just add attributes. These renderers should be
     * registered last.
     *
     * @param device
     */
    protected void registerElementOnlyStyleEmulationPropertyRenderers(InternalDevice device) {
        addElementOnlyEmulationRenderer("u", new StyleProperty[] { StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE }, new OnOffKeywordElementPropertyRenderer(MCSTextUnderlineStyleKeywords.SOLID, "u", MCSTextUnderlineStyleKeywords.NONE, StyleEmulationVisitor.ANTI_UNDERLINE_ELEMENT));
        addElementOnlyEmulationRenderer("blink", new StyleProperty[] { StylePropertyDetails.MCS_TEXT_BLINK }, new OnOffKeywordElementPropertyRenderer(MCSTextBlinkKeywords.BLINK, "blink", MCSTextBlinkKeywords.NONE, StyleEmulationVisitor.ANTI_BLINK_ELEMENT));
        addElementOnlyEmulationRenderer("strike", new StyleProperty[] { StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE }, new OnOffKeywordElementPropertyRenderer(MCSTextLineThroughStyleKeywords.SOLID, "strike", MCSTextLineThroughStyleKeywords.NONE, StyleEmulationVisitor.ANTI_STRIKE_ELEMENT));
    }

    /**
     * Register the appropriate marquee emulation renderers, depending on the
     * the level of support for the marquee element specified in the device
     * repository.
     *
     * @param device
     */
    private void registerMarqueeEmulationRenderers(InternalDevice device) {
        if (device == null) {
            return;
        }
        if (canEmulateElement("marquee")) {
            DeviceElementCapability dec = deviceCapabilityManager.getDeviceElementCapability("marquee", true);
            boolean supportsDisplayKeyword;
            final CSSVersion cssVersion = getCssVersion();
            if (cssVersion == null) {
                supportsDisplayKeyword = false;
            } else {
                CSSProperty display = cssVersion.getProperty(StylePropertyDetails.DISPLAY);
                supportsDisplayKeyword = display.supportsKeyword(DisplayKeywords.MCS_MARQUEE);
            }
            boolean marqueeStyleCssSupported = supportsDisplayKeyword && cssVersion.getProperty(StylePropertyDetails.MCS_MARQUEE_STYLE) != null;
            boolean behaviourSupported = CapabilitySupportLevel.FULL.equals(dec.getSupportType(DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT));
            if (!marqueeStyleCssSupported && behaviourSupported) {
                AttributeAndOrElementStyleEmulationPropertyRenderer renderer = new AttributeAndOrElementStyleEmulationPropertyRenderer(new MarqueeEmulationRenderer(DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT, false));
                styleEmulationPropertyRendererSelector.register(StylePropertyDetails.MCS_MARQUEE_STYLE, renderer);
                fakePermittedChildren.add("marquee");
                boolean marqueeLoopCssSupported = marqueeStyleCssSupported && (cssVersion.getProperty(StylePropertyDetails.MCS_MARQUEE_REPETITION) != null);
                boolean loopSupported = CapabilitySupportLevel.FULL.equals(dec.getSupportType(DeviceCapabilityConstants.MARQUEE_LOOP_ATT));
                if (!marqueeLoopCssSupported && loopSupported) {
                    AttributeAndOrElementStyleEmulationPropertyRenderer loopRenderer = new AttributeAndOrElementStyleEmulationPropertyRenderer(new MarqueeEmulationAttributePropertyRenderer(DeviceCapabilityConstants.MARQUEE_LOOP_ATT, false));
                    styleEmulationPropertyRendererSelector.register(StylePropertyDetails.MCS_MARQUEE_REPETITION, loopRenderer);
                }
                boolean marqueeDirCssSupported = marqueeStyleCssSupported && (cssVersion.getProperty(StylePropertyDetails.MCS_MARQUEE_DIRECTION) != null);
                boolean directionSupported = CapabilitySupportLevel.FULL.equals(dec.getSupportType(DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT));
                if (!marqueeDirCssSupported && directionSupported) {
                    AttributeAndOrElementStyleEmulationPropertyRenderer dirRenderer = new AttributeAndOrElementStyleEmulationPropertyRenderer(new MarqueeEmulationAttributePropertyRenderer(DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT, false));
                    styleEmulationPropertyRendererSelector.register(StylePropertyDetails.MCS_MARQUEE_DIRECTION, dirRenderer);
                }
                if (!supportsDisplayKeyword && CapabilitySupportLevel.FULL.equals(dec.getSupportType(DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT))) {
                    AttributeAndOrElementStyleEmulationPropertyRenderer bgColorRenderer = new AttributeAndOrElementStyleEmulationPropertyRenderer(new MarqueeEmulationAttributePropertyRenderer(DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT, false));
                    styleEmulationPropertyRendererSelector.register(StylePropertyDetails.BACKGROUND_COLOR, bgColorRenderer);
                }
            }
        }
    }

    /**
     * Determine if the named element can be emulated, given the current
     * device's capabilities.
     *
     * @param elementName   of the element we wish to emulate
     * @return true if the device can emulate styles using the named element,
     * and false otherwise
     */
    private boolean canEmulateElement(String elementName) {
        boolean canEmulate = false;
        DeviceElementCapability dec = deviceCapabilityManager.getDeviceElementCapability(elementName, true);
        if (dec.getElementSupportLevel() != null && dec.getElementSupportLevel() != CapabilitySupportLevel.NONE) {
            canEmulate = true;
        }
        return canEmulate;
    }

    /**
     * Add an element only renderer if:
     * <ul>
     * <li>the css to represent the given style property is not supported by
     * the device.</li>
     * <li>the element used for emulating the supplied style property is
     * supported by the device.</li>
     * </ul>
     *
     * @param elementName   of the element that is used to emulate the given
     *                      style property
     * @param properties    properties to be emulated if the css to represent
     *                      them is not supported by the device
     * @param renderer      property renderer to wrap in an element only
     */
    private void addElementOnlyEmulationRenderer(String elementName, StyleProperty[] properties, StyleEmulationPropertyRenderer renderer) {
        if (canEmulateElement(elementName)) {
            boolean cssNotSupported = cssVersion == null;
            for (int i = 0; i < properties.length; i++) {
                StyleProperty property = properties[i];
                cssNotSupported = cssNotSupported || cssVersion.getProperty(property) == null;
                if (cssNotSupported) {
                    ElementOnlyStyleEmulationPropertyRenderer elementRenderer = new ElementOnlyStyleEmulationPropertyRenderer(renderer);
                    styleEmulationPropertyRendererSelector.register(property, elementRenderer);
                    fakePermittedChildren.add(elementName);
                }
            }
        }
    }

    /**
     * Return the ruleset used by the highlight transformer to handle mcs-effect-style:highlight
     * on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHighlightTransformationRules() {
        return highlightRuleSet;
    }

    /**
     * Return the ruleset used by the corners transformer to handle mcs-border-radius
     * on XHTML2 elements.
     *
     * @return
     */
    public HashMap getCornersTransformationRules() {
        return cornersRuleSet;
    }

    /**
     * 
     */
    public int getDevicePixelsX() {
        return this.devicePixelsX;
    }

    /**
     * 
     * @param pixelsX
     */
    public void setDevicePixelsX(int pixelsX) {
        this.devicePixelsX = pixelsX;
    }

    /**
     * Sets the status of file upload support.
     * 
     * @param canSupportFileUpload Set to true if file upload is supported.
     */
    protected void setCanSupportFileUpload(boolean canSupportFileUpload) {
        this.canSupportFileUpload = canSupportFileUpload;
    }

    public boolean isFileUploadSupported() {
        return this.canSupportFileUpload;
    }

    /**
     * Return the combination of:
     * <ul>
     * <li>the list of elements that are valid children of stylistic elements
     * (e.g. i, b) as returned by {@link #getPermittedChildren()} </li>
     * AND
     * <li>the set of "fake" permitted elements which has been constructed for
     * this device (see {@link #fakePermittedChildren}) which are not normally
     * supported by this protocol</li>
     * </ul>
     *
     * @return Set of all the elements that are valid children of stylistic
     * elements, according to this device's configuration
     */
    protected Set getAllPermittedChildren() {
        final String[] permittedChildren = getPermittedChildren();
        return mergePermittedChildren(permittedChildren);
    }

    /**
     * Convenience method which merges the array of elements that have been
     * defined as valid children of stylistic elements (see
     * {@link #getPermittedChildren()}) with the set of "fake" permitted
     * elements which has been constructed for this device (see {@link
     * #fakePermittedChildren}).
     *
     * @param permittedChildren     the array of elements that have been
     *                              defined as valid children of stylistic
     *                              elements
     * @return Set of all the elements that are valid children of stylistic
     * elements, according to this device's configuration
     */
    protected Set mergePermittedChildren(String[] permittedChildren) {
        HashSet permittedChildrenSet = null;
        if (permittedChildren != null && fakePermittedChildren != null) {
            final int realLength = permittedChildren.length;
            final int fakeLength = fakePermittedChildren.size();
            permittedChildrenSet = new HashSet(realLength + fakeLength);
            for (int i = 0; i < permittedChildren.length; i++) {
                if (!permittedChildrenSet.contains(permittedChildren[i])) {
                    permittedChildrenSet.add(permittedChildren[i]);
                }
            }
            permittedChildrenSet.addAll(fakePermittedChildren);
        }
        return permittedChildrenSet;
    }

    public void createStyleEmulationElements() {
        styleEmulationElements = new DefaultStyleEmulationElementConfiguration();
        final Set permittedChildren = getAllPermittedChildren();
        if (fakePermittedChildren.contains("blink")) {
            styleEmulationElements.associateStylisticAndAntiElements("blink", StyleEmulationVisitor.ANTI_BLINK_ELEMENT, permittedChildren);
        }
        if (fakePermittedChildren.contains("marquee")) {
            styleEmulationElements.associateStylisticAndAntiElements("marquee", null, permittedChildren);
            styleEmulationElements.addIndivisibleElementsThatPermitStyles(new String[] { "marquee" });
        }
        if (fakePermittedChildren.contains("u")) {
            styleEmulationElements.associateStylisticAndAntiElements("u", StyleEmulationVisitor.ANTI_UNDERLINE_ELEMENT, permittedChildren);
        }
        if (fakePermittedChildren.contains("strike")) {
            styleEmulationElements.associateStylisticAndAntiElements("strike", StyleEmulationVisitor.ANTI_STRIKE_ELEMENT, permittedChildren);
        }
    }

    public String[] getPermittedChildren() {
        return new String[] {};
    }

    public String getCSSMedia() {
        return cssMedia;
    }

    /**
     * Sets target media types for generated CSS.
     *
     * @param cssMedia The cssMedia to set.
     */
    public void setCSSMedia(String cssMedia) {
        this.cssMedia = cssMedia;
    }

    /**
     * Registers maximum frame rate, reading from device policies.
     *
     * @param device The device to read policy from.
     */
    private void registerMaxClientFrameRate(InternalDevice device) {
        maxClientFrameRate = Double.POSITIVE_INFINITY;
        if (device == null) {
            return;
        }
        String valueString = device.getPolicyValue(DevicePolicyConstants.CLIENT_FRAME_RATE_MAX);
        if (valueString != null) {
            try {
                maxClientFrameRate = Integer.valueOf(valueString).doubleValue();
            } catch (NumberFormatException e) {
            }
        }
    }

    public double getMaxClientFrameRate() {
        return maxClientFrameRate;
    }

    public boolean isInvalidFormLinkParent(String parentName) {
        return invalidFormLinkParents.containsKey(parentName);
    }

    public String getChildForInvalidFormLinkElement(String elementName) {
        return (String) invalidFormLinkParents.get(elementName);
    }

    public DTD getDTD() {
        return dtd;
    }

    public String getViewportVirtualSupport() {
        return this.supportsViewportVirtualSuport;
    }
}
