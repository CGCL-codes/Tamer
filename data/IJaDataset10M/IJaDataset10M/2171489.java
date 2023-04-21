package org.wfmc.wapi;

/**
 * Represents an activity instance.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public interface WMActivityInstance {

    String getName();

    String getId();

    String getActivityDefinitionId();

    String getProcessInstanceId();

    WMActivityInstanceState getState();

    int getPriority();

    WMParticipant[] getParticipants();
}
