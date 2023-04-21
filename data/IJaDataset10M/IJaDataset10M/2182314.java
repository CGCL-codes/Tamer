package put.inf66281.vrmi.manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;
import put.inf66281.vrmi.composition.ObjectComposition;
import put.inf66281.vrmi.server.VersionedObject;
import put.inf66281.vrmi.task.TaskDescription;
import put.inf66281.vrmi.task.TaskProcessingContext;

public interface AtomicTaskManager extends Remote {

    public TaskProcessingContext startTask(String taskID, TaskDescription taskDescription) throws RemoteException;

    public void endTask(String taskID) throws RemoteException;

    public void registerObject(VersionedObject vo, UUID objUUID, ObjectComposition objectComposition) throws RemoteException;

    public void unregisterObject(Remote obj) throws RemoteException;

    public void heartbeat(String taskID) throws RemoteException;
}
