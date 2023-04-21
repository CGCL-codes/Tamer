package org.wfmc.audit;

/**
 * Section 8.6.4 of the WfMC Interface 5 standard.
 *
 * @author Antony Lodge
 */
public class WMAGetAttributesTargetWorkflowResponse extends WMASourceWorkflow {

    private static final long serialVersionUID = -554613457844994324L;

    private String targetProcessDefinitionId;

    private String attributeName;

    private int attributeType;

    private int attributeLength;

    private String attributeValue;

    public WMAGetAttributesTargetWorkflowResponse() {
    }

    /**
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     * @param sourceActivityDefinitionBusinessName
     *
     * @param targetProcessInstanceId
     * @param targetProcessDefinitionBusinessName
     *
     * @param targetNodeId
     * @param targetUserId
     * @param targetRoleId
     */
    public WMAGetAttributesTargetWorkflowResponse(CWADPrefix cwadPrefix, String messageId, short extensionNumber, String extensionType, String sourceConversationId, String targetConversationId, String sourceActivityDefinitionBusinessName, String targetProcessInstanceId, String targetProcessDefinitionBusinessName, String targetNodeId, String targetUserId, String targetRoleId) {
        super(cwadPrefix, messageId, extensionNumber, extensionType, sourceConversationId, targetConversationId, sourceActivityDefinitionBusinessName, targetProcessInstanceId, targetProcessDefinitionBusinessName, targetNodeId, targetUserId, targetRoleId);
    }

    /**
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     * @param sourceActivityDefinitionBusinessName
     *
     * @param targetProcessInstanceId
     * @param targetProcessDefinitionBusinessName
     *
     * @param targetNodeId
     * @param targetUserId
     * @param targetRoleId
     * @param targetProcessDefinitionId
     * @param attributeName
     * @param attributeType
     * @param attributeLength
     * @param attributeValue
     */
    public WMAGetAttributesTargetWorkflowResponse(CWADPrefix cwadPrefix, String messageId, short extensionNumber, String extensionType, String sourceConversationId, String targetConversationId, String sourceActivityDefinitionBusinessName, String targetProcessInstanceId, String targetProcessDefinitionBusinessName, String targetNodeId, String targetUserId, String targetRoleId, String targetProcessDefinitionId, String attributeName, int attributeType, int attributeLength, String attributeValue) {
        super(cwadPrefix, messageId, extensionNumber, extensionType, sourceConversationId, targetConversationId, sourceActivityDefinitionBusinessName, targetProcessInstanceId, targetProcessDefinitionBusinessName, targetNodeId, targetUserId, targetRoleId);
        this.targetProcessDefinitionId = targetProcessDefinitionId;
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.attributeLength = attributeLength;
        this.attributeValue = attributeValue;
    }

    /**
     * @return as supplied to the target Workflow Engine
     */
    public String getTargetProcessDefinitionId() {
        return targetProcessDefinitionId;
    }

    /**
     * @param targetProcessDefinitionId as supplied to the target
     *                                  Workflow Engine
     */
    public void setTargetProcessDefinitionId(String targetProcessDefinitionId) {
        this.targetProcessDefinitionId = targetProcessDefinitionId;
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

    public String toString() {
        return "WMAGetAttributesTargetWorkflowResponse@" + System.identityHashCode(this) + '[' + " cwadPrefix=" + formatCwadPrefix() + ", messageId=" + getMessageId() + ", sourceActivityDefinitionBusinessName='" + getSourceActivityDefinitionBusinessName() + '\'' + ", targetProcessDefinitionId=" + targetProcessDefinitionId + ", targetProcessInstanceId=" + getTargetProcessInstanceId() + ", targetProcessDefinitionBusinessName='" + getTargetProcessDefinitionBusinessName() + '\'' + ", targetNodeId=" + getTargetNodeId() + ", targetUserId=" + getTargetUserId() + ", targetRoleId=" + getTargetRoleId() + ", attributeName='" + attributeName + '\'' + ", attributeType=" + attributeType + ", attributeLength=" + attributeLength + ", attributeValue='" + attributeValue + '\'' + ", extensionNumber=" + getExtensionNumber() + ", extensionType='" + getExtensionType() + '\'' + ", sourceConversationId='" + getSourceConversationId() + '\'' + ", targetConversationId='" + getTargetConversationId() + '\'' + ']';
    }
}
