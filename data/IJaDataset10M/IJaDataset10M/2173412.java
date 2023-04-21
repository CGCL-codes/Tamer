package org.leviatan.dataharbour.core.model;

import java.util.Map;

/**
 * Models a request sent to DataHarbour for persistence handling. It contains
 * all information, including the data itself, needed to harbour that data.
 *  
 * @author leviatan
 *
 */
public abstract interface HarbourRequest extends XmlConvertible {

    public String getId();

    public void setId(String id);

    public String getSenderId();

    public void setSenderId(String senderId);

    public String getProcessId();

    public void setProcessId(String processId);

    public String getActorId();

    public void setActorId(String actorId);

    public String getTaskId();

    public void setTaskId(String taskId);

    public Map<String, DataEntity> getDataEntityMap();

    public void setDataEntityMap(Map<String, DataEntity> variableMap);

    public String getProcessInstanceId();

    public void setProcessInstanceId(String processInstanceId);

    public String getTaskName();

    public void setTaskName(String taskName);

    public String getProcessName();

    public void setProcessName(String processName);

    public int getProcessVersion();

    public void setProcessVersion(int processVersion);
}
