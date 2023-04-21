package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MCSDOMContentHandler;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DivideHintAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuChildVisitable;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuItemGroupAttributes;
import com.volantis.mcs.protocols.MenuOrientation;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSAuralDTMFAllocationKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is the root for all classes which support the VoiceXML protocol.
 *
 * Issues.
 *
 * VoiceXML is built around a form, nothing happens unless it is in a form.
 * In order to support paragraphs and headings dummy forms are created around
 * each pane which is not in a form. When a form is completed it does not
 * automatically drop through to the next form in the page it simply exits so
 * we need to link the dummy forms and real forms in a chain. The dummy forms
 * are assigned an id which consists of a prefix and a number which is
 * incremented after every dummy form. When the dummy forms are generated they
 * add a <goto> tag which jumps to the next dummy form. Before generating the
 * standard form it first generates a dummy form which jumps straight to the
 * standard form.
 */
public abstract class VoiceXMLRoot extends DOMProtocol {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(VoiceXMLRoot.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(VoiceXMLRoot.class);

    private static final VoiceXMLGrammar nuanceGrammar = new NuanceGrammar();

    private final VoiceXMLGrammar grammar = nuanceGrammar;

    private final String dummyFormPrefix = "form-";

    private int nestingDepth;

    private int formCount;

    /**
     * Tag name used when writing out implicit content.
     */
    private static final String IMPLICIT_ELEMENT = "var";

    /**
     * Tag name used when writing out form emulation elements.
     */
    private static final String FORM_EMULATION_ELEMENT = "form";

    /**
     * This buffer is used by form fields if they need to create sub dialog
     * forms. It is appended to the end of the form postamble buffer, after
     * the form has been closed.
     */
    private DOMOutputBuffer subDialogs;

    /**
     * The main transformer for this protocol.
     */
    private final DOMTransformer transformer = new VoiceXMLTransformer();

    protected VoiceXMLRoot(ProtocolSupportFactory supportFactory, ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        styleSheetRenderer = RuntimeCSSStyleSheetRenderer.getSingleton();
    }

    public void initialise() {
        super.initialise();
    }

    public String defaultMimeType() {
        return "";
    }

    /**
     * Override this method to return a VoiceXMLTransformer.
     */
    protected DOMTransformer getDOMTransformer() {
        return transformer;
    }

    /**
     * Override skipElementBody to return the current value of skipElementBody
     * and then reset.
     * @return skipElementBody
     */
    public boolean skipElementBody() {
        boolean result = super.skipElementBody();
        setSkipElementBody(false);
        return result;
    }

    /**
     * Given some text that is well formed xml, create an element structure
     * that fully represents all the text in DOM format.
     * <p/>
     * <strong>NOTE:</strong> the text must currently be a complete DOM
     * document (i.e. it must have a single root node).
     *
     * @param s The xml text.
     * @return The Element that is a DOM representation of text.
     * @todo later enable this method to handle document fragments
     */
    protected Element createElementFromString(String s) throws ProtocolException {
        MCSDOMContentHandler domParser = new MCSDOMContentHandler();
        XMLReader saxParser = new com.volantis.xml.xerces.parsers.SAXParser();
        saxParser.setContentHandler(domParser);
        try {
            StringReader stringReader = new StringReader(s);
            InputSource source = new InputSource(stringReader);
            saxParser.parse(source);
        } catch (SAXException e) {
            throw new ProtocolException(exceptionLocalizer.format("parse-error", s), e);
        } catch (IOException e) {
            throw new ProtocolException(exceptionLocalizer.format("parse-error", s), e);
        }
        Document document = domParser.getDocument();
        return document.getRootElement();
    }

    /**
     * Add the open span markup to the specified DOMOutputBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void openSpan(DOMOutputBuffer dom, SpanAttributes attributes) throws ProtocolException {
        openPrompt(dom);
        TextAssetReference src = attributes.getSrc();
        if (src != null) {
            String value = getTextFromReference(src, TextEncoding.VOICE_XML_PROMPT);
            if (value != null) {
                Element element = createElementFromString(value);
                dom.addElement(element);
                setSkipElementBody(true);
            }
        }
    }

    /**
     * Add the close span markup to the specified DOMOutputBuffer.
     * @param dom The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void closeSpan(DOMOutputBuffer dom, SpanAttributes attributes) {
        closePrompt(dom);
    }

    private void openPrompt(DOMOutputBuffer dom) {
        if (nestingDepth == 0) {
            dom.openElement("block");
            dom.openElement("prompt");
        }
        nestingDepth += 1;
    }

    private void closePrompt(DOMOutputBuffer dom) {
        nestingDepth -= 1;
        if (nestingDepth == 0) {
            dom.closeElement("prompt");
            dom.closeElement("block");
        }
    }

    protected void openCanvas(DOMOutputBuffer dom, CanvasAttributes attributes) {
        Element element = dom.openStyledElement("vxml", attributes);
        element.setAttribute("version", "1.0");
    }

    protected void closeCanvas(DOMOutputBuffer dom, CanvasAttributes attributes) {
        dom.closeElement("vxml");
    }

    /**
     * The full number is always output (even if there is content), wrappered
     * in a sayas to ensure that it is correctly vocalized as a phone number.
     */
    protected void addPhoneNumberContents(DOMOutputBuffer dom, PhoneNumberAttributes attributes) {
        Object contents = attributes.getContent();
        String defaultContents = attributes.getDefaultContents();
        openPrompt(dom);
        if (contents instanceof DOMOutputBuffer) {
            DOMOutputBuffer contentBuffer = (DOMOutputBuffer) contents;
            if (!contentBuffer.isEmpty()) {
                dom.addOutputBuffer(contentBuffer);
            }
        } else if (contents != null) {
            dom.appendEncoded(contents.toString());
        }
        if (defaultContents != null) {
            if (defaultContents.indexOf("<") == -1) {
                Element element = dom.openStyledElement("sayas", attributes);
                element.setAttribute("class", "literal");
                dom.appendEncoded(defaultContents);
                dom.closeElement("sayas");
            } else {
                dom.appendEncoded(defaultContents);
            }
        }
        closePrompt(dom);
    }

    /**
     * Augments the superclass version to ensure that the qualified full number
     * does not include any '+' prefix (which is not allowed by this protocol)
     */
    protected String resolveQualifiedFullNumber(String fullNumber) {
        String noPrefixFullNumber = fullNumber;
        if (noPrefixFullNumber.charAt(0) == '+') {
            noPrefixFullNumber = noPrefixFullNumber.substring(1);
        }
        return super.resolveQualifiedFullNumber(noPrefixFullNumber);
    }

    protected void openColumnIteratorPane(DOMOutputBuffer dom, ColumnIteratorPaneAttributes attributes) {
        openPane(dom, attributes);
    }

    protected void closeColumnIteratorPane(DOMOutputBuffer dom, ColumnIteratorPaneAttributes attributes) {
        closePane(dom, attributes);
    }

    protected void openForm(DOMOutputBuffer dom, FormAttributes attributes) {
        dom.restoreInsertionPoint();
    }

    protected void closeForm(DOMOutputBuffer dom, FormAttributes form) {
    }

    protected void openLayout(DOMOutputBuffer dom, LayoutAttributes attributes) {
    }

    protected void closeLayout(DOMOutputBuffer dom, LayoutAttributes attributes) {
        Element element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", dummyFormPrefix + formCount);
        dom.openElement("block");
        dom.addElement("exit");
        dom.closeElement("block");
        dom.closeElement("form");
    }

    protected void openPane(DOMOutputBuffer dom, PaneAttributes attributes) {
        Pane pane = attributes.getPane();
        if (pane.getEnclosingForm() == null) {
            Element element = dom.openStyledElement("form", attributes);
            element.setAttribute("id", dummyFormPrefix + formCount);
        }
    }

    protected void closePane(DOMOutputBuffer dom, PaneAttributes attributes) {
        Pane pane = attributes.getPane();
        if (pane.getEnclosingForm() == null) {
            dom.openStyledElement("block", attributes);
            Element element = dom.openElement("goto");
            formCount += 1;
            element.setAttribute("next", "#" + dummyFormPrefix + formCount);
            dom.closeElement("goto");
            dom.closeElement("block");
            dom.closeElement("form");
        }
    }

    protected void openRowIteratorPane(DOMOutputBuffer dom, RowIteratorPaneAttributes attributes) {
        openPane(dom, attributes);
    }

    protected void closeRowIteratorPane(DOMOutputBuffer dom, RowIteratorPaneAttributes attributes) {
        closePane(dom, attributes);
    }

    protected void openHeading1(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading1(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openHeading2(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading2(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openHeading3(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading3(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openHeading4(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading4(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openHeading5(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading5(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openHeading6(DOMOutputBuffer dom, HeadingAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeHeading6(DOMOutputBuffer dom, HeadingAttributes attributes) {
        closePrompt(dom);
    }

    protected void openParagraph(DOMOutputBuffer dom, ParagraphAttributes attributes) {
        openPrompt(dom);
    }

    protected void closeParagraph(DOMOutputBuffer dom, ParagraphAttributes attributes) {
        closePrompt(dom);
    }

    public void doMenu(MenuAttributes attributes) throws ProtocolException {
        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        Pane pane = attributes.getPane();
        ContainerInstance containerInstance = (ContainerInstance) context.getFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS);
        DOMOutputBuffer dom = (DOMOutputBuffer) containerInstance.getCurrentBuffer();
        Element menu = dom.openStyledElement("menu", attributes);
        String id = attributes.getId();
        if (id != null) {
            menu.setAttribute("id", id);
        }
        StyleValue styleValue;
        styleValue = propertyValues.getComputedValue(StylePropertyDetails.MCS_AURAL_MENU_SCOPE);
        String scope = styleValue.getStandardCSS();
        menu.setAttribute("scope", scope);
        styleValue = propertyValues.getComputedValue(StylePropertyDetails.MCS_AURAL_DTMF_ALLOCATION);
        boolean manualDTMF = styleValue == MCSAuralDTMFAllocationKeywords.MANUAL;
        if (!manualDTMF) {
            menu.setAttribute("dtmf", "true");
        }
        attributes.setManualDTMF(manualDTMF);
        TextAssetReference object = attributes.getPrompt();
        if (object != null) {
            String prompt = getTextFromReference(object, TextEncoding.VOICE_XML_PROMPT);
            if (prompt != null) {
                Element element = createElementFromString(prompt);
                dom.addElement(element);
            }
        }
        boolean isIteratorPane = false;
        if (pane instanceof RowIteratorPane) {
            isIteratorPane = true;
        } else if (pane instanceof ColumnIteratorPane) {
            isIteratorPane = true;
        }
        Collection items = attributes.getItems();
        for (Iterator i = items.iterator(); i.hasNext(); ) {
            ((MenuChildVisitable) i.next()).visit(this, dom, attributes, i.hasNext(), isIteratorPane, MenuOrientation.HORIZONTAL);
            containerInstance.endCurrentBuffer();
            dom = (DOMOutputBuffer) containerInstance.getCurrentBuffer();
        }
        object = attributes.getHelp();
        if (object != null) {
            String help = getTextFromReference(object, TextEncoding.VOICE_XML_HELP);
            if (help != null) {
                dom.appendLiteral(help);
            }
        }
        object = attributes.getErrmsg();
        if (object != null) {
            String error = getTextFromReference(object, TextEncoding.VOICE_XML_ERROR);
            if (error != null) {
                dom.appendLiteral(error);
            }
        }
        dom.closeElement(menu);
    }

    public void renderMenuChild(DOMOutputBuffer dom, MenuAttributes attributes, MenuItem child, boolean notLast, boolean iteratorPane, MenuOrientation orientation) throws ProtocolException {
        doMenuItem(dom, attributes, child);
    }

    public void renderMenuChild(DOMOutputBuffer dom, MenuAttributes attributes, MenuItemGroupAttributes child, boolean notLast, boolean iteratorPane, MenuOrientation orientation) throws ProtocolException {
    }

    protected boolean doMenuItem(DOMOutputBuffer dom, MenuAttributes attributes, MenuItem item) throws ProtocolException {
        String resolvedHref = null;
        LinkAssetReference href = item.getHref();
        if (href != null) {
            resolvedHref = getRewrittenLinkFromObject(href, item.getSegment() != null);
        }
        if (resolvedHref != null) {
            Element choice = dom.openStyledElement("choice", item);
            choice.setAttribute("next", resolvedHref);
            TextAssetReference object = null;
            if (attributes.isManualDTMF()) {
                object = item.getShortcut();
                String accessKey = getPlainText(object);
                if (accessKey != null) {
                    try {
                        int dtmf = Integer.parseInt(accessKey);
                        if (dtmf < 1 || dtmf > 9) {
                            logger.warn("dtmf-range-bounds-error", new Object[] { new Integer(dtmf) });
                        } else {
                            choice.setAttribute("dtmf", String.valueOf(dtmf));
                        }
                    } catch (NumberFormatException nfe) {
                        logger.warn("dtmf-menuitem-failure", new Object[] { item, accessKey });
                    }
                }
            }
            object = item.getPrompt();
            if (object != null) {
                String prompt = getTextFromReference(object, TextEncoding.VOICE_XML_PROMPT);
                if (prompt != null) {
                    Element element = createElementFromString(prompt);
                    dom.addElement(element);
                }
            } else {
                dom.appendEncoded(item.getText());
            }
            dom.closeElement(choice);
        }
        return false;
    }

    protected void openForm(XFFormAttributes attributes) {
        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData());
        if (nestingDepth != 0) {
            throw new IllegalStateException("Nesting depth is " + nestingDepth);
        }
        nestingDepth = 1;
        Element element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", attributes.getName());
        String value;
        if ((value = getTextFromReference(attributes.getPrompt(), TextEncoding.VOICE_XML_PROMPT)) != null) {
            dom.openElement("block");
            dom.appendLiteral(value);
            dom.closeElement("block");
        }
        if ((value = getTextFromReference(attributes.getHelp(), TextEncoding.VOICE_XML_HELP)) != null) {
            dom.appendLiteral(value);
        }
        element = dom.addElement("var");
        element.setAttribute("name", URLConstants.FORM_PARAMETER);
        element.setAttribute("expr", "'" + getFormSpecifier(attributes) + "'");
        subDialogs = allocateOutputBuffer();
        dom.saveInsertionPoint();
    }

    public Element createXFormEmulationElement(String formName, EmulatedXFormDescriptor fd) {
        Element element = domFactory.createElement();
        element.setName(FORM_EMULATION_ELEMENT);
        element.setAttribute("id", formName);
        return element;
    }

    public boolean isXFormEmulationElement(Element element) {
        boolean result = false;
        if (element.getName().equals(FORM_EMULATION_ELEMENT)) {
            result = true;
        }
        return result;
    }

    public boolean isImplicitEmulationElement(Element element) {
        boolean result = false;
        if (element.getName().equals(IMPLICIT_ELEMENT)) {
            result = true;
        }
        return result;
    }

    public Element createVFormElement(String formSpecifier) {
        Element element = domFactory.createElement();
        element.setName("var");
        element.setAttribute("name", URLConstants.FORM_PARAMETER);
        element.setAttribute("expr", "'" + formSpecifier + "'");
        return element;
    }

    protected void closeForm(XFFormAttributes attributes) {
        DOMOutputBuffer dom = getContentBuffer(attributes.getFormData());
        if (nestingDepth != 1) {
            throw new IllegalStateException();
        }
        nestingDepth = 0;
        dom.closeElement("form");
        dom.addElement(subDialogs.getRoot());
        subDialogs.initialise();
        subDialogs = null;
    }

    public void doTextInput(XFTextInputAttributes attributes) {
        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        if ((entryContainerInstance = attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }
        dom = getCurrentBuffer(entryContainerInstance);
        Element element = dom.openStyledElement("field", attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", attributes.getName());
        appendVoiceXMLLiteral(dom, attributes);
        TextAssetReference reference = getTextReferenceFromStyleValue(attributes.getStyles(), StylePropertyDetails.MCS_INPUT_FORMAT);
        grammar.generateGrammarFromObject(dom, reference);
        dom.closeElement("field");
    }

    public void doBooleanInput(XFBooleanAttributes attributes) {
        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;
        if ((entryContainerInstance = attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }
        name = attributes.getName();
        dom = getCurrentBuffer(entryContainerInstance);
        Element element = dom.openStyledElement("field", attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);
        appendVoiceXMLLiteral(dom, attributes);
        element = dom.openElement("grammar");
        grammar.generateBooleanGrammar(dom, name, attributes.getFalseValues(), attributes.getTrueValues());
        dom.closeElement("grammar");
        dom.closeElement("field");
    }

    public void doSelectInput(XFSelectAttributes attributes) {
        if (attributes.isMultiple()) {
            doMultipleSelectInput(attributes);
        } else {
            doSingleSelectInput(attributes);
        }
    }

    private void doSingleSelectInput(XFSelectAttributes attributes) {
        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;
        if ((entryContainerInstance = attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }
        name = attributes.getName();
        dom = getCurrentBuffer(entryContainerInstance);
        Element element = dom.openStyledElement("field", attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);
        appendVoiceXMLLiteral(dom, attributes);
        Collection options = attributes.getOptions();
        dom.openElement("grammar");
        grammar.generateSingleSelectGrammar(dom, this, name, options);
        dom.closeElement("grammar");
        dom.closeElement("field");
    }

    private void doMultipleSelectInput(XFSelectAttributes attributes) {
        DOMOutputBuffer dom;
        ContainerInstance entryContainerInstance;
        String name;
        if ((entryContainerInstance = attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry container instance not set");
            }
            return;
        }
        name = attributes.getName();
        String containingXFFormName;
        XFFormAttributes formAttributes = attributes.getFormAttributes();
        if (formAttributes != null) {
            containingXFFormName = formAttributes.getName();
        } else {
            containingXFFormName = attributes.getContainingXFFormName();
        }
        String subId = containingXFFormName + "-" + name;
        dom = getCurrentBuffer(entryContainerInstance);
        Element element = dom.openStyledElement("subdialog", attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", name);
        element.setAttribute("src", "#" + subId);
        element = dom.openElement("filled");
        element = dom.addElement("assign");
        element.setAttribute("name", name);
        element.setAttribute("expr", name + ".result");
        dom.closeElement("filled");
        dom.closeElement("subdialog");
        dom = subDialogs;
        element = dom.openStyledElement("form", attributes);
        element.setAttribute("id", subId);
        Collection options = attributes.getOptions();
        dom.openElement("grammar");
        grammar.generateMultipleSelectGrammar(dom, name, options);
        dom.closeElement("grammar");
        dom.openElement("initial");
        appendVoiceXMLLiteral(dom, attributes);
        dom.closeElement("initial");
        int o = 0;
        handleOptionFields(options, dom, o);
        element = dom.openElement("filled");
        element.setAttribute("mode", "any");
        element = dom.openElement("script");
        dom.appendEncoded("var separator = \"\";var result = \"\";");
        element = dom.closeElement("script");
        element = dom.addElement("return");
        element.setAttribute("namelist", "result");
        dom.closeElement("filled");
        dom.closeElement("form");
    }

    private void handleOptionFields(Collection options, DOMOutputBuffer dom, int count) {
        for (Iterator i = options.iterator(); i.hasNext(); count += 1) {
            Object unknown = i.next();
            if (unknown instanceof SelectOption) {
                SelectOption option = (SelectOption) unknown;
                Element element = dom.addStyledElement("field", option);
                element.setAttribute("name", "option" + count);
                element.setAttribute("type", "boolean");
            } else {
                handleOptionFields(((SelectOptionGroup) unknown).getSelectOptionList(), dom, count);
            }
        }
    }

    /**
     * Append any voice xml literal values. This method was added a a result of
     * refactoring.
     *
     * @param dom        the DOMOutputBuffer
     * @param attributes the form field attributes to be used.
     */
    private void appendVoiceXMLLiteral(DOMOutputBuffer dom, XFFormFieldAttributes attributes) {
        String value;
        if ((value = getTextFromReference(attributes.getPrompt(), TextEncoding.VOICE_XML_PROMPT)) != null) {
            dom.appendLiteral(value);
        }
        if ((value = getTextFromReference(attributes.getHelp(), TextEncoding.VOICE_XML_HELP)) != null) {
            dom.appendLiteral(value);
        }
        if ((value = getTextFromReference(attributes.getErrmsg(), TextEncoding.VOICE_XML_ERROR)) != null) {
            dom.appendLiteral(value);
        }
    }

    /**
     * Add an implicit value to the form. Only one action is currently
     * supported and it automatically triggers when the form has been filled
     * in. The names of all the fields apart from other action fields are added
     * to the list of variable names whose values should be submitted.
     *
     * @param attributes The attributes to use when generating the mark up.
     */
    public void doActionInput(DOMOutputBuffer dom, XFActionAttributes attributes) {
        XFFormAttributes formAttributes = attributes.getFormAttributes();
        if (formAttributes != null) {
            if (formAttributes.getActionCount() != 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring action");
                }
                return;
            }
            formAttributes.setActionCount(1);
        }
        String value;
        Element element;
        element = dom.openStyledElement("filled", attributes);
        addFormFieldAttributes(element, attributes);
        String fieldName = attributes.getName();
        if (fieldName != null) {
            value = attributes.getValue();
            if (value == null) {
                value = " ";
            }
            element = dom.addElement("var");
            element.setAttribute("name", fieldName);
            element.setAttribute("expr", value);
        }
        element = dom.addElement("submit");
        if (formAttributes != null) {
            String resolvedHref = resolveFormAction(formAttributes);
            element.setAttribute("next", resolvedHref);
            element.setAttribute("method", formAttributes.getMethod());
            StringBuffer buffer = new StringBuffer(URLConstants.FORM_PARAMETER);
            for (Iterator i = formAttributes.getFields().iterator(); i.hasNext(); ) {
                XFFormFieldAttributes field = (XFFormFieldAttributes) i.next();
                if (field != attributes && field instanceof XFActionAttributes) {
                    continue;
                }
                if (!(field instanceof XFImplicitAttributes) && field.getEntryContainerInstance() == null) {
                    continue;
                }
                fieldName = field.getName();
                if (fieldName != null) {
                    buffer.append(" ").append(fieldName);
                }
            }
            element.setAttribute("namelist", buffer.toString());
        }
        dom.closeElement("filled");
    }

    public void populateEmulatedActionElement(Element element, EmulatedXFormDescriptor fd) {
        if ("submit".equals(element.getName())) {
            String resolvedHref = resolveFormAction(fd.getFormAttributes());
            element.setAttribute("next", resolvedHref);
            element.setAttribute("method", fd.getFormMethod());
            StringBuffer buffer = new StringBuffer(URLConstants.FORM_PARAMETER);
            for (Iterator i = fd.getFields().iterator(); i.hasNext(); ) {
                FieldDescriptor field = (FieldDescriptor) i.next();
                if (field.getType() instanceof ActionFieldType && element.getName().equalsIgnoreCase(field.getName())) {
                    continue;
                }
                if (field.getName() != null) {
                    buffer.append(" ").append(field.getName());
                }
            }
            element.setAttribute("namelist", buffer.toString());
        }
    }

    public void doImplicitValue(DOMOutputBuffer dom, XFImplicitAttributes attributes) {
        Element element = dom.addStyledElement(IMPLICIT_ELEMENT, attributes);
        addFormFieldAttributes(element, attributes);
        element.setAttribute("name", attributes.getName());
        element.setAttribute("expr", "'" + attributes.getValue() + "'");
    }

    protected void doDivideHint(DOMOutputBuffer dom, DivideHintAttributes attributes) {
    }
}
