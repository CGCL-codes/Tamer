package org.jaffa.presentation.portlet.widgets.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.jaffa.util.StringHelper;
import org.jaffa.util.BeanHelper;
import org.jaffa.presentation.portlet.widgets.taglib.exceptions.OuterFormTagMissingRuntimeException;
import org.jaffa.datatypes.Formatter;
import java.util.*;
import org.jaffa.metadata.FieldMetaData;
import org.jaffa.presentation.portlet.widgets.taglib.exceptions.JspWriteRuntimeException;
import org.jaffa.config.Config;
import org.jaffa.presentation.portlet.widgets.taglib.exceptions.MissingParametersRuntimeException;
import org.jaffa.rules.IPropertyRuleIntrospector;
import org.jaffa.session.ContextManagerFactory;
import org.jaffa.util.URLHelper;

/** Tag Handler for the Text widget.*/
public class TextTag extends CustomModelTag implements IWidgetTag, IFormTag {

    /** The name of the rule used for determining whether the text is wrapped by <pre> tag.
     * This is mostly used by comments property. 
     * When PropertyIntrospector.getCommentStyle() returns NOT NULL and this application rule is set to true, 
     * the text output will be wrapped by <pre> tag.
     */
    public static final String RULE_PRE_FORMATED = "jaffa.widgets.text.preformated";

    private static Logger log = Logger.getLogger(TextTag.class);

    private static final String TAG_NAME = "TextTag";

    private static String c_header = null;

    private static Properties c_domainFieldViewerComponentMappingProperties = new Properties();

    private static Properties c_keyFieldPerViewerComponentProperties = new Properties();

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type='text/javascript' src='jaffa/js/widgets/texttip.js'></script>\n");
        c_header = sb.toString();
        try {
            reloadViewerHyperlinkConfig();
        } catch (IOException e) {
            if (log.isInfoEnabled()) log.info("Exception thrown while trying to load the properties for generating hyperlinks to Viewer components from the TextTag. This feature will be disabled.", e);
        }
    }

    /** property declaration for tag attribute: type.
     */
    private String m_type;

    /** property declaration for tag attribute: domain.
     */
    private String m_domain;

    /** property declaration for tag attribute: domainField.
     */
    private String m_domainField;

    /** property declaration for tag attribute: layout.
     */
    private String m_layout;

    /** property declaration for tag attribute: maxLength.
     */
    private Integer m_maxLength;

    /** property declaration for tag attribute: popUp.
     */
    private boolean m_popUp;

    /** property declaration for tag attribute: disableHyperlink.
     */
    private boolean m_disableHyperlink;

    /** Default constructor.
     */
    public TextTag() {
        super();
        initTag();
    }

    private void initTag() {
        m_field = null;
        m_type = null;
        m_domain = null;
        m_domainField = null;
        m_layout = null;
        m_maxLength = null;
        m_popUp = false;
        m_disableHyperlink = false;
    }

    /**
     * This generates the HTML for the tag.
     */
    public void otherDoStartTagOperations() {
        try {
            super.otherDoStartTagOperations();
        } catch (JspException e) {
            log.error("TextTag.otherDoStartTagOperations(): error=" + e);
        }
        IPropertyRuleIntrospector propertyRuleIntrospector = lookupPropertyTag();
        if (getField() == null) {
            String str = "The " + TAG_NAME + " requires 'field' parameter to be supplied or it should be within a PropertyTag";
            log.error(str);
            throw new MissingParametersRuntimeException(str);
        }
        FormTag formTag = TagHelper.getFormTag(pageContext);
        if (formTag == null) {
            String str = "The " + TAG_NAME + " should be inside a FormTag";
            log.error(str);
            throw new OuterFormTagMissingRuntimeException(str);
        }
        Object value = null;
        if (propertyRuleIntrospector == null) try {
            propertyRuleIntrospector = TagHelper.getPropertyRuleIntrospector(pageContext, getField());
        } catch (JspException e) {
            log.error("TextTag.otherDoStartTagOperations() error getting property inspector=" + e);
        }
        if (propertyRuleIntrospector != null && propertyRuleIntrospector.isHidden()) {
            value = TagHelper.getTextForHiddenField(pageContext);
        } else {
            value = TagHelper.getFieldValue(pageContext, getField(), TAG_NAME);
            if (value != null) {
                String layout = propertyRuleIntrospector != null ? propertyRuleIntrospector.getLayout() : null;
                if (layout == null) {
                    if (m_layout != null) {
                        layout = m_layout;
                    } else {
                        if (m_domain != null && m_domainField != null) layout = obtainLayoutUsingFieldMetaData(m_domain, m_domainField);
                        String formClass = TagHelper.getFormBase(pageContext).getClass().getName();
                        if (layout == null && (m_domain == null || m_domainField == null || !m_domain.equals(formClass) || !m_domainField.equals(getField()))) layout = obtainLayoutUsingFieldMetaData(formClass, getField());
                    }
                }
                String formattedValue = null;
                if (layout != null) formattedValue = Formatter.format(value, layout); else formattedValue = Formatter.format(value);
                if (formattedValue != null) {
                    if (formattedValue.length() == 0) formattedValue = null; else {
                        if (getType() == null || !getType().equalsIgnoreCase("HTML")) {
                            formattedValue = StringHelper.convertToHTML(formattedValue);
                            formattedValue = StringHelper.replace(formattedValue, "\r\n", "<BR>");
                            formattedValue = StringHelper.replace(formattedValue, "\n\r", "<BR>");
                            formattedValue = StringHelper.replace(formattedValue, "\r", "<BR>");
                            formattedValue = StringHelper.replace(formattedValue, "\n", "<BR>");
                        }
                        if (getMaxLength() != null) formattedValue = processMaxLength(formattedValue);
                        formattedValue = generateHyperlink(formattedValue, propertyRuleIntrospector);
                    }
                }
                value = formattedValue;
            }
        }
        if (value != null) {
            if (isPreFormated(propertyRuleIntrospector)) {
                value = "<pre>" + value + "</pre>";
            }
            JspWriter out = pageContext.getOut();
            try {
                out.print(value);
            } catch (IOException e) {
                String str = "Exception in writing the " + TAG_NAME;
                log.error(str, e);
                throw new JspWriteRuntimeException(str, e);
            }
        }
    }

    public boolean isPreFormated(IPropertyRuleIntrospector propertyRuleIntrospector) {
        if (propertyRuleIntrospector == null) return false;
        String appRule = (String) ContextManagerFactory.instance().getProperty(RULE_PRE_FORMATED);
        boolean usePre = "true".equalsIgnoreCase(appRule) || "t".equalsIgnoreCase(appRule);
        if (usePre && propertyRuleIntrospector.getCommentStyle() != null) return true;
        return false;
    }

    /** Getter for the attribute field.
     * @return Value of attribute field.
     */
    public String getField() {
        return m_field;
    }

    /** Setter for the attribute field.
     * @param value New value of attribute field.
     */
    public void setField(String value) {
        m_field = value;
    }

    /** Getter for the attribute type.
     * @return Value of attribute type.
     */
    public String getType() {
        return m_type;
    }

    /** Setter for the attribute type.
     * @param value New value of attribute type.
     */
    public void setType(String value) {
        m_type = value;
    }

    /** Getter for the attribute domain.
     * @return Value of attribute domain.
     */
    public String getDomain() {
        return m_domain;
    }

    /** Setter for the attribute domain.
     * @param value New value of attribute domain.
     */
    public void setDomain(String value) {
        m_domain = value;
    }

    /** Getter for the attribute domainField.
     * @return Value of attribute domainField.
     */
    public String getDomainField() {
        return m_domainField;
    }

    /** Setter for the attribute domainField.
     * @param value New value of attribute domainField.
     */
    public void setDomainField(String value) {
        m_domainField = value;
    }

    /** Getter for the attribute layout.
     * @return Value of attribute layout.
     */
    public String getLayout() {
        return m_layout;
    }

    /** Setter for the attribute layout.
     * @param value New value of attribute layout.
     */
    public void setLayout(String value) {
        m_layout = value;
    }

    /** Getter for the attribute maxLength.
     * @return Value of attribute maxLength.
     */
    public Integer getMaxLength() {
        return m_maxLength;
    }

    /** Setter for the attribute maxLength.
     * @param value New value of attribute maxLength.
     */
    public void setMaxLength(Integer value) {
        m_maxLength = value;
    }

    /** Getter for the attribute popUp.
     * @return Value of attribute popUp.
     */
    public boolean getPopUp() {
        return m_popUp;
    }

    /** Setter for the attribute popUp.
     * @param value New value of attribute popUp.
     */
    public void setPopUp(boolean value) {
        m_popUp = value;
    }

    /** Getter for the attribute disableHyperlink.
     * @return Value of attribute disableHyperlink.
     */
    public boolean getDisableHyperlink() {
        return m_disableHyperlink;
    }

    /** Setter for the attribute disableHyperlink.
     * @param value New value of attribute disableHyperlink.
     */
    public void setDisableHyperlink(boolean value) {
        m_disableHyperlink = value;
    }

    /** Returns the string containing the javascript info for the HTML header.
     * @return the string containing the javascript info for the HTML header.
     */
    public static String getHeader() {
        return c_header;
    }

    private String processMaxLength(String value) {
        String valueShown = null;
        if (getMaxLength().intValue() < value.length()) {
            valueShown = value.substring(0, getMaxLength().intValue());
            if (!getPopUp()) {
                valueShown = valueShown + "...";
            } else {
                valueShown = valueShown + "<a href=\"javascript:void(0)\" onClick=\"doTextTip(this , '" + StringHelper.replace(value, "'", "\\'") + "')\">...</a>";
            }
        } else {
            valueShown = value;
        }
        return (valueShown);
    }

    private String generateHyperlink(String input, IPropertyRuleIntrospector propertyRuleIntrospector) {
        String output = input;
        if (!getDisableHyperlink()) {
            Properties hyperlinkInfo = propertyRuleIntrospector != null ? propertyRuleIntrospector.getHyperlinkInfo() : null;
            if (hyperlinkInfo != null) {
                output = TagHelper.generateHyperlink(pageContext, input, hyperlinkInfo.getProperty("component"), hyperlinkInfo.getProperty("inputs"), hyperlinkInfo.getProperty("values"));
            } else if (getDomain() != null && getDomainField() != null) {
                String componentName = c_domainFieldViewerComponentMappingProperties.getProperty(getDomain() + '.' + getDomainField());
                String keyField = componentName != null ? c_keyFieldPerViewerComponentProperties.getProperty(componentName) : null;
                if (componentName != null && keyField != null) output = TagHelper.generateHyperlink(pageContext, input, componentName, keyField, getField());
            }
        }
        return output;
    }

    /** This will reload the properties files containing the mappings between Viewer components and domain fields
     * and the mappings between Viewer components and its key fields
     * @throws IOException if any error occurs in loading the file.
     */
    public static void reloadViewerHyperlinkConfig() throws IOException {
        InputStream domainFieldViewerComponentMappingStream = null;
        InputStream keyFieldPerViewerComponentStream = null;
        try {
            String prop1 = (String) Config.getProperty(Config.PROP_DOMAIN_FIELD_VIEWER_COMPONENT_MAPPING_FILE, null);
            String prop2 = (String) Config.getProperty(Config.PROP_KEY_FIELD_PER_VIEWER_COMPONENT_FILE, null);
            if (prop1 != null && prop1.trim().length() > 0 && prop2 != null && prop2.trim().length() > 0) {
                synchronized (c_domainFieldViewerComponentMappingProperties) {
                    c_domainFieldViewerComponentMappingProperties.clear();
                    domainFieldViewerComponentMappingStream = URLHelper.getInputStream(prop1);
                    if (domainFieldViewerComponentMappingStream != null) {
                        c_domainFieldViewerComponentMappingProperties.load(domainFieldViewerComponentMappingStream);
                        if (log.isDebugEnabled()) log.debug("Loaded the properties from file: " + domainFieldViewerComponentMappingStream);
                    } else {
                        if (log.isInfoEnabled()) log.info("File '" + prop1 + "' to load the properties for generating hyperlinks to Viewer components from the TextTag not found. This feature will be disabled.");
                    }
                }
                synchronized (c_keyFieldPerViewerComponentProperties) {
                    c_keyFieldPerViewerComponentProperties.clear();
                    keyFieldPerViewerComponentStream = URLHelper.getInputStream(prop2);
                    if (keyFieldPerViewerComponentStream != null) {
                        c_keyFieldPerViewerComponentProperties.load(keyFieldPerViewerComponentStream);
                        if (log.isDebugEnabled()) log.debug("Loaded the properties from file: " + keyFieldPerViewerComponentStream);
                    } else {
                        c_domainFieldViewerComponentMappingProperties.clear();
                        if (log.isInfoEnabled()) log.info("File '" + prop2 + "' to load the properties for generating hyperlinks to Viewer components from the TextTag not found. This feature will be disabled.");
                    }
                }
            }
        } finally {
            try {
                if (domainFieldViewerComponentMappingStream != null) domainFieldViewerComponentMappingStream.close();
            } catch (IOException e) {
            }
            try {
                if (keyFieldPerViewerComponentStream != null) keyFieldPerViewerComponentStream.close();
            } catch (IOException e) {
            }
        }
    }

    /** Checks for the nearest outer PropertyTag.
     * Will set the 'field', 'domain' and 'domainField' on this tag, if not specifed, with the values from the outer PropertyTag.
     * @return the IPropertyRuleIntrospector from the outer PropertyTag.
     */
    private IPropertyRuleIntrospector lookupPropertyTag() {
        PropertyTag propertyTag = (PropertyTag) findCustomTagAncestorWithClass(this, PropertyTag.class);
        if (propertyTag != null) {
            if (getField() == null) setField(propertyTag.getField());
            if (getDomain() == null && getDomainField() == null) {
                setDomain(propertyTag.getPropertyClass());
                setDomainField(propertyTag.getPropertyName());
            }
            return propertyTag.getPropertyRuleIntrospector();
        } else return null;
    }

    /** Determine the FieldMetaData for the input class+field and then return its layout.
     */
    private String obtainLayoutUsingFieldMetaData(String className, String fieldName) {
        String layout = null;
        try {
            FieldMetaData meta = TagHelper.getFieldMetaData(className, fieldName);
            if (meta != null) {
                layout = (String) BeanHelper.getField(meta, "layout");
                if (log.isDebugEnabled()) log.debug("Layout for the MetaField " + className + '.' + fieldName + ": " + layout);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) log.debug("Error in determining the FieldMetaData for " + className + '.' + fieldName, e);
        }
        return layout;
    }

    /** Invoked in all cases after doEndTag() for any class implementing Tag, IterationTag or BodyTag. This method is invoked even if an exception has occurred in the BODY of the tag, or in any of the following methods: Tag.doStartTag(), Tag.doEndTag(), IterationTag.doAfterBody() and BodyTag.doInitBody().
     * This method is not invoked if the Throwable occurs during one of the setter methods.
     * This will reset the internal fields, so that the Tag can be re-used.
     */
    public void doFinally() {
        super.doFinally();
        initTag();
    }

    public String getFooterHtml() {
        return c_header;
    }
}
