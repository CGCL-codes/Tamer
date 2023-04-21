package org.nodevision.portal.bp.axis;

public interface NVBPSkeleton extends java.rmi.Remote {

    public java.lang.Boolean start(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.lang.Integer getPriority(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean end(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.util.Calendar getCreate(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.lang.Long findLatestProcessDefinition(java.lang.String definition) throws java.rmi.RemoteException;

    public java.lang.Long getNewProcessInstance(java.lang.String definitionName) throws java.rmi.RemoteException;

    public java.lang.Long getRootToken(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean deleteProcessInstance(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.Object[] findProcessInstances(java.lang.Long processDefinitionId) throws java.rmi.RemoteException;

    public java.lang.String getActualNodeName(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.Object[] getLeavingTransitions(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean hasEnded(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.String getDefaultLeavingTransition(java.lang.Long processInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean signal(java.lang.Long processInstanceId, java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Boolean signalTransition(java.lang.Long processInstanceId, java.lang.String transition, java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Boolean setVariable(java.lang.Long processInstanceId, java.lang.String name, java.lang.Object value) throws java.rmi.RemoteException;

    public java.lang.Object getVariable(java.lang.Long processInstanceId, java.lang.String name) throws java.rmi.RemoteException;

    public java.lang.Object[] findPooledTaskInstancesMultiple(java.lang.String[] actorIds) throws java.rmi.RemoteException;

    public java.lang.Object[] findPooledTaskInstances(java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Object[] findTaskInstances(java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Object[] findTaskInstancesMultiple(java.lang.String[] actorIds) throws java.rmi.RemoteException;

    public java.lang.Boolean addCommentToTask(java.lang.String comment, java.lang.String actorId, java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.util.HashMap getCommentFomTask(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.util.Calendar getDueDate(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.util.Calendar getEnd(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean cancel(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;

    public java.lang.Boolean endTask(java.lang.Long taskInstanceId, java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Boolean endTaskTransition(java.lang.Long taskInstanceId, java.lang.String transitionName, java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Boolean setActorId(java.lang.Long taskInstanceId, java.lang.String actorId) throws java.rmi.RemoteException;

    public java.lang.Object[] getAvailableTransitions(java.lang.Long taskInstanceId) throws java.rmi.RemoteException;
}
