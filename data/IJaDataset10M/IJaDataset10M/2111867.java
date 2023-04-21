package org.wfmc.audit;

/**
 * Section 8.7.4 of the WfMC Interface 5 standard.
 *
 * @author Antony Lodge
 */
public class WMASetAttributesTargetWorkflowOperation extends WMARemoteAuditBase {

    private static final long serialVersionUID = 3598051411325901641L;

    private String attributeName;

    private int attributeType;

    private int attributeLength;

    private String attributeValue;

    private int previousAttributeLength;

    private String previousAttributeValue;

    public WMASetAttributesTargetWorkflowOperation() {
    }

    /**
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     */
    public WMASetAttributesTargetWorkflowOperation(CWADPrefix cwadPrefix, String messageId, short extensionNumber, String extensionType, String sourceConversationId, String targetConversationId) {
        super(cwadPrefix, messageId, extensionNumber, extensionType, sourceConversationId, targetConversationId);
    }

    /**
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     * @param attributeName
     * @param attributeType
     * @param attributeLength
     * @param attributeValue
     * @param previousAttributeLength
     * @param previousAttributeValue
     */
    public WMASetAttributesTargetWorkflowOperation(CWADPrefix cwadPrefix, String messageId, short extensionNumber, String extensionType, String sourceConversationId, String targetConversationId, String attributeName, int attributeType, int attributeLength, String attributeValue, int previousAttributeLength, String previousAttributeValue) {
        super(cwadPrefix, messageId, extensionNumber, extensionType, sourceConversationId, targetConversationId);
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.attributeLength = attributeLength;
        this.attributeValue = attributeValue;
        this.previousAttributeLength = previousAttributeLength;
        this.previousAttributeValue = previousAttributeValue;
    }

    /**
     * @return Name of attribute requested
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * @param attributeName Name of attribute requested
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * @return Type of attribute requested
     */
    public int getAttributeType() {
        return attributeType;
    }

    /**
     * @param attributeType Type of attribute requested
     */
    public void setAttributeType(int attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * @return The attribute length.
     */
    public int getAttributeLength() {
        return attributeLength;
    }

    /**
     * @param attributeLength Length of requested attribute
     */
    public void setAttributeLength(int attributeLength) {
        this.attributeLength = attributeLength;
    }

    /**
     * @return Length of requested attribute
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * @param attributeValue
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * @return The previous attribute length.
     */
    public int getPreviousAttributeLength() {
        return previousAttributeLength;
    }

    /**
     * @param previousAttributeLength
     */
    public void setPreviousAttributeLength(int previousAttributeLength) {
        this.previousAttributeLength = previousAttributeLength;
    }

    /**
     * @return The previous attribute value.
     */
    public String getPreviousAttributeValue() {
        return previousAttributeValue;
    }

    /**
     * @param previousAttributeValue
     */
    public void setPreviousAttributeValue(String previousAttributeValue) {
        this.previousAttributeValue = previousAttributeValue;
    }

    public String toString() {
        return "WMASetAttributesTargetWorkflowOperation@" + System.identityHashCode(this) + '[' + " cwadPrefix=" + formatCwadPrefix() + ", messageId=" + getMessageId() + ", attributeName='" + attributeName + '\'' + ", attributeType=" + attributeType + ", attributeLength=" + attributeLength + ", attributeValue='" + attributeValue + '\'' + ", previousAttributeLength=" + previousAttributeLength + ", previousAttributeValue='" + previousAttributeValue + '\'' + ", extensionNumber=" + getExtensionNumber() + ", extensionType='" + getExtensionType() + '\'' + ", sourceConversationId='" + getSourceConversationId() + '\'' + ", targetConversationId='" + getTargetConversationId() + '\'' + ']';
    }
}
