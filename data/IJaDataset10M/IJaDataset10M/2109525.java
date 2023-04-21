package org.wfmc.audit;

/**
 * Section 8.4.5 of the WfMC Interface 5 standard.
 *
 * @author Antony Lodge
 */
public class WMACreateSourceWorkflowResponse extends WMASourceWorkflowRequest {

    private static final long serialVersionUID = 5479065368712288914L;

    public WMACreateSourceWorkflowResponse() {
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
     * @param targetState
     * @param targetProcessDefinitionId
     */
    public WMACreateSourceWorkflowResponse(CWADPrefix cwadPrefix, String messageId, short extensionNumber, String extensionType, String sourceConversationId, String targetConversationId, String sourceActivityDefinitionBusinessName, String targetProcessInstanceId, String targetProcessDefinitionBusinessName, String targetNodeId, String targetUserId, String targetRoleId, String targetState, String targetProcessDefinitionId) {
        super(cwadPrefix, messageId, extensionNumber, extensionType, sourceConversationId, targetConversationId, sourceActivityDefinitionBusinessName, targetProcessInstanceId, targetProcessDefinitionBusinessName, targetNodeId, targetUserId, targetRoleId, targetState, targetProcessDefinitionId);
    }

    public String toString() {
        return "WMACreateSourceWorkflowResponse@" + System.identityHashCode(this) + '[' + " cwadPrefix=" + formatCwadPrefix() + ", messageId=" + getMessageId() + ", sourceActivityDefinitionBusinessName='" + getSourceActivityDefinitionBusinessName() + '\'' + ", targetProcessDefinitionId=" + getTargetProcessDefinitionId() + ", targetProcessInstanceId=" + getTargetProcessInstanceId() + ", targetProcessDefinitionBusinessName='" + getTargetProcessDefinitionBusinessName() + '\'' + ", targetNodeId=" + getTargetNodeId() + ", targetUserId=" + getTargetUserId() + ", targetRoleId=" + getTargetRoleId() + ", targetState=" + getTargetState() + ", extensionNumber=" + getExtensionNumber() + ", extensionType='" + getExtensionType() + '\'' + ", sourceConversationId='" + getSourceConversationId() + '\'' + ", targetConversationId='" + getTargetConversationId() + '\'' + ']';
    }
}
