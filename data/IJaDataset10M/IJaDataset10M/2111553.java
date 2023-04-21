package org.xaware.server.engine.instruction.bizcomps.multiformat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.context.AbstractConfigTranslator;
import org.xaware.server.engine.enums.YesNoValue;
import org.xaware.server.engine.exceptions.XAwareConfigMissingException;
import org.xaware.server.engine.exceptions.XAwareConfigurationException;
import org.xaware.server.engine.instruction.bizcomps.MultiFormatBizCompInst;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareInvalidEnumerationValueException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * @author hcurtis Establish the xa:request configuration
 */
public class RequestConfigTranslator extends AbstractConfigTranslator {

    private String recordSource = null;

    private String requestType = null;

    private String format = null;

    private int recordStart = 0;

    private String recordSeperator = null;

    private String characterEncoding = null;

    private final String className = "RequestConfigTranslator";

    private final HashMap<String, MultiRecordFormat> recordDefs = new HashMap<String, MultiRecordFormat>();

    private ProcessRecordPlan processRecordPlan = null;

    private MultiFormatBizCompInst instruction = null;

    private boolean ignoreUnmatchedRecords = false;

    /**
     * Acquire the xa:request element and proceed with parsing the request and its children using subordantant
     * ConfigTranslator classes
     * 
     * @param parentElement
     * @param context
     * @param logger
     * @throws XAwareConfigurationException
     *             when it can't find an xa:request
     * @throws XAwareSubstitutionException
     *             when the substitution of the xa:request element's attributes fail
     */
    public RequestConfigTranslator(final MultiFormatBizCompInst instruction, final Element parentElement, final IBizViewContext context, final XAwareLogger logger) throws XAwareException {
        super(context, logger, "RequestConfigTranslator");
        this.instruction = instruction;
        Element configElem = lookupConfigElement(parentElement, getElementNameConstant(), getElementNamespaceConstant(), "ctor 1");
        setAndSubstituteConfigElement(configElem);
        parseTheRequestLine();
        parseRecordDefinitions();
        parseReturnRecords();
    }

    /**
     * Parse the xa:request line and establish/verify the possible attribute values related to the request
     * 
     * @throws XAwareConfigurationException
     * @throws XAwareConfigMissingException
     */
    private void parseTheRequestLine() throws XAwareConfigurationException, XAwareConfigMissingException {
        final String methodName = "parseTheRequest";
        m_logger.entering(className, methodName);
        recordSource = getMandatoryAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_RECORDSOURCE);
        parseRequestType();
        parseFormat();
        parseRecordStart();
        parseRecordSeparator();
        characterEncoding = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_CHAR_ENCODING);
        m_logger.exiting(className, methodName);
    }

    /**
     * parse the xa:record_seperator in the xa:request element
     * 
     * @throws XAwareConfigurationException
     */
    private void parseRecordSeparator() throws XAwareConfigurationException {
        final String methodName = "parseRecordSeparator";
        m_logger.entering(className, methodName);
        String errMsg;
        recordSeperator = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_RECORDSEPARATOR);
        if (recordSeperator == null) {
            recordSeperator = "none";
        }
        if ("\n".equals(recordSeperator) || "\\r\\n".equals(recordSeperator)) {
            recordSeperator = XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NEWLINE;
        }
        if ("\\t".equals(recordSeperator) || "\\\\t".equals(recordSeperator)) {
            final char cTab[] = { '\t' };
            recordSeperator = new String(cTab);
        }
        if (!(XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NORECORDSEP.equals(recordSeperator) || XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NEWLINE.equals(recordSeperator) || recordSeperator.length() == 1)) {
            errMsg = getElementNamespaceConstant().getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_RECORDSEPARATOR + "=\"" + recordSeperator + "\" is not a valid selection";
            m_logger.severe(errMsg, className, methodName);
            throw new XAwareConfigurationException(errMsg);
        }
        if (!XAwareConstants.BIZCOMPONENT_ATTR_VALUE_NORECORDSEP.equals(recordSeperator)) {
            parseIgnoreUnmatchedRecords();
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * Parse the xa:ignore_unmatched_records in the xa:request element
     * 
     * @throws XAwareConfigurationException
     */
    private void parseIgnoreUnmatchedRecords() throws XAwareConfigurationException {
        final String methodName = "parseIgnoreUnmatchedRecords";
        m_logger.entering(className, methodName);
        final String ignoreText = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_IGNORE_UNMATCHED_RECORDS);
        if (ignoreText != null) {
            try {
                final YesNoValue ignoreValue = YesNoValue.getYesNoAttributeValue(ignoreText);
                ignoreUnmatchedRecords = (ignoreValue == YesNoValue.YES);
            } catch (final XAwareInvalidEnumerationValueException e) {
                final String errMsg = "Invalid yes/no assignment for " + XAwareConstants.xaNamespace.getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_IGNORE_UNMATCHED_RECORDS + "=\"" + ignoreText + "\"";
                m_logger.severe(errMsg, className, methodName);
                throw new XAwareConfigurationException(errMsg, e);
            }
        } else {
            ignoreUnmatchedRecords = false;
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * parse the xa:start attribute in the xa:request element
     * 
     * @throws XAwareConfigurationException
     */
    private void parseRecordStart() throws XAwareConfigurationException {
        final String methodName = "parseRecordStart";
        m_logger.entering(className, methodName);
        String errMsg;
        String startText = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_RECORDSTART);
        if (startText != null) {
            try {
                final int periodPos = startText.indexOf(".");
                if (periodPos > 0) {
                    m_logger.warning("xa:" + XAwareConstants.BIZCOMPONENT_ATTR_RECORDSTART + "=\"" + startText + "\" has a decimal point, floating value will be truncated");
                    startText = startText.substring(0, periodPos - 1);
                }
                recordStart = Integer.parseInt(startText);
            } catch (final NumberFormatException e) {
                errMsg = getElementNamespaceConstant().getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_RECORDSTART + "=\"" + startText + "\" will not convert to an integer";
                m_logger.severe(errMsg, className, methodName);
                throw new XAwareConfigurationException(errMsg);
            }
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * Parse the xa:format and validate the resultant assignment
     * 
     * @throws XAwareConfigurationException
     */
    private void parseFormat() throws XAwareConfigurationException {
        final String methodName = "parseFormat";
        m_logger.entering(className, methodName);
        String errMsg;
        format = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_FORMAT);
        if (format == null) {
            format = XAwareConstants.BIZCOMPONENT_ATTR_VALUE_FIXEDLENGTH;
        }
        if (!XAwareConstants.BIZCOMPONENT_ATTR_VALUE_FIXEDLENGTH.equals(format)) {
            errMsg = getElementNamespaceConstant().getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_FORMAT + "=\"" + format + "\" is not supported by Multiformat BizComponent";
            m_logger.severe(errMsg, className, methodName);
            throw new XAwareConfigurationException(errMsg);
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * Parse the xa:request_type and validate the resultant assignment
     * 
     * @throws XAwareConfigurationException
     */
    private void parseRequestType() throws XAwareConfigurationException {
        final String methodName = "parseRequestType";
        m_logger.entering(className, methodName);
        String errMsg;
        requestType = getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_REQUEST_TYPE);
        if (requestType == null) {
            requestType = XAwareConstants.BIZCOMPONENT_ATTR_TEXTTOXML;
        }
        if (!XAwareConstants.BIZCOMPONENT_ATTR_TEXTTOXML.equals(requestType)) {
            errMsg = getElementNamespaceConstant().getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_REQUEST_TYPE + "=\"" + requestType + "\" is not supported by Multiformat BizComponent";
            m_logger.severe(errMsg, className, methodName);
            throw new XAwareConfigurationException(errMsg);
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * Parse the xa:record_definition children under the xa:request
     * 
     * @throws XAwareException
     *             when there is a validation or substitution error
     */
    private void parseRecordDefinitions() throws XAwareException {
        final String methodName = "parseRecordDefinitions";
        m_logger.entering(className, methodName);
        List recDefs = getConfigElement().getChildren(XAwareConstants.BIZCOMPONENT_ATTR_RECORDDEFINITION, XAwareConstants.xaNamespace);
        for (Iterator recDefIter = recDefs.iterator(); recDefIter.hasNext(); ) {
            Element recDefElement = (Element) recDefIter.next();
            final RecordDefinitionConfigTranslator recDefCfg = new RecordDefinitionConfigTranslator(getContext(), m_logger, recDefElement);
            final MultiRecordFormat mrFmt = new MultiRecordFormat(recDefCfg, this, instruction);
            recordDefs.put(mrFmt.getName(), mrFmt);
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * parse the xa:return configuration under the xa:request. After acquiring the constructed ProcessRecordPlan object
     * set up to pass it up
     * 
     * @throws XAwareException
     */
    private void parseReturnRecords() throws XAwareException {
        final String methodName = "parseReturnRecords";
        m_logger.entering(className, methodName);
        Element retElem = getConfigElement().getChild(XAwareConstants.BIZCOMPONENT_ELEMENT_RETURN, XAwareConstants.xaNamespace);
        if (retElem != null) {
            final ReturnConfigTranslator returnCfgTxltr = new ReturnConfigTranslator(getContext(), m_logger, this, retElem, ignoreUnmatchedRecords);
            processRecordPlan = returnCfgTxltr.getProcessRecordPlan();
        }
        m_logger.exiting(className, methodName);
    }

    /**
     * respond with the xa:request element name
     * 
     * @see org.xaware.server.engine.context.BaseConfigTranslator#getElementNameConstant()
     */
    @Override
    public String getElementNameConstant() {
        return XAwareConstants.BIZCOMPONENT_ELEMENT_REQUEST;
    }

    /**
     * @return Returns the format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return Returns the recordSeperator.
     */
    public String getRecordSeperator() {
        return recordSeperator;
    }

    /**
     * @return Returns the recordSource.
     */
    public String getRecordSource() {
        return recordSource;
    }

    /**
     * @return Returns the recordStart.
     */
    public int getRecordStart() {
        return recordStart;
    }

    /**
     * @return Returns the requestType.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @return Returns the characterEncoding.
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * @return Returns the instruction.
     */
    public MultiFormatBizCompInst getInstruction() {
        return instruction;
    }

    /**
     * @return Returns the recordDefs.
     */
    public HashMap getRecordDefs() {
        return recordDefs;
    }

    /**
     * @return Returns the processRecordPlan.
     */
    public ProcessRecordPlan getProcessRecordPlan() {
        return processRecordPlan;
    }
}
