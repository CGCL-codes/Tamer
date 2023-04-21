package org.obe.client.api.model;

import org.wfmc.wapi.WAPI;
import org.wfmc.wapi.WMWorkItem;
import java.util.Date;

/**
 * An extended work item interface that exposes all OBE work item system
 * attributes directly.  OBE returns instances of this interface from the
 * {@link WAPI#getWorkItem} and {@link WAPI#listWorkItems} methods.
 *
 * @author Adrian Price
 */
public interface OBEWorkItem extends WMWorkItem {

    /**
     * @see WorkItemAttributes#COMPLETED_DATE
     */
    Date getCompletedDate();

    /**
     * @see WorkItemAttributes#DUE_DATE
     */
    Date getDueDate();

    /**
     * @see WorkItemAttributes#PERFORMER
     */
    String getPerformer();

    /**
     * @see WorkItemAttributes#PROCESS_DEFINITION_ID
     */
    String getProcessDefinitionId();

    /**
     * @see WorkItemAttributes#STARTED_DATE
     */
    Date getStartedDate();

    /**
     * @see WorkItemAttributes#TARGET_DATE
     */
    Date getTargetDate();

    /**
     * @see WorkItemAttributes#TEMPORAL_STATUS
     */
    TemporalStatus getTemporalStatus();

    /**
     * @see WorkItemAttributes#TOOL_INDEX
     */
    int getToolIndex();
}
