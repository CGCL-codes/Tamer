package es.caib.bpm.beans.remote;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBObject;
import org.dom4j.DocumentException;
import es.caib.bpm.exception.BPMException;
import es.caib.bpm.vo.ProcessDefinition;
import es.caib.bpm.vo.ProcessInstance;
import es.caib.bpm.vo.TaskDefinition;
import es.caib.bpm.vo.TaskInstance;

public interface BPMEngine extends EJBObject {

    /** Gestión de procesos */
    public List findMyProcesses() throws RemoteException;

    public ProcessDefinition getProcessDefinition(ProcessInstance process) throws RemoteException;

    public ProcessInstance cancel(ProcessInstance process) throws RemoteException;

    public List findProcessInstances(List definitions, String processId, String estado, String actor, Date startDate, boolean finalizada) throws BPMException, RemoteException;

    public List findProcessInstances(ProcessDefinition def) throws BPMException, RemoteException;

    public ProcessDefinition getDefinition(ProcessInstance process) throws RemoteException;

    /** Gestión de tareas **/
    public List findMyTasks() throws BPMException, RemoteException;

    public List findGroupTasks() throws BPMException, RemoteException;

    public TaskInstance startTask(TaskInstance task) throws BPMException, RemoteException;

    public TaskInstance addComment(TaskInstance task, String comment) throws RemoteException;

    public TaskInstance executeTask(TaskInstance task, String transitionName) throws BPMException, RemoteException;

    public TaskInstance reserveTask(TaskInstance task) throws BPMException, RemoteException;

    public TaskInstance delegateTaskToUser(TaskInstance task, String username) throws RemoteException;

    public void update(TaskInstance task) throws BPMException, RemoteException;

    public void updateSwimlane(es.caib.bpm.vo.TaskInstance task, String swimlane, String actorIds[]) throws BPMException, RemoteException;

    public ProcessInstance getProcessInstance(TaskInstance task) throws RemoteException;

    public TaskInstance cancel(TaskInstance task) throws RemoteException;

    public List findTasks(List def, TaskDefinition task, String actor, Date processStartDate, Date taskCreationDate, boolean finalizada) throws BPMException, RemoteException;

    public List findTasks(List def, String process, TaskDefinition task, String actor, Date processStartDate, Date taskCreationDate, boolean finalizada) throws BPMException, RemoteException;

    public String getUI(TaskInstance task) throws RemoteException;

    public TaskDefinition getDefinition(TaskInstance task) throws RemoteException;

    public List getPendingTasks(ProcessInstance process) throws BPMException, RemoteException;

    /** Gestión de definiciones ***/
    public List findProcessDefinitions(String name, boolean onlyLastVersions) throws RemoteException;

    public ProcessInstance newProcess(ProcessDefinition def) throws BPMException, RemoteException;

    public byte[] getProcessDefinitionImage(ProcessDefinition def) throws RemoteException;

    public int[] getCoordinates(TaskInstance task) throws DocumentException, RemoteException;

    public List findInitiatorProcessDefinitions() throws BPMException, RemoteException;

    public List findObserverProcessDefinitions() throws BPMException, RemoteException;

    public List findSupervisorProcessDefinitions() throws BPMException, RemoteException;

    public Map getUIClassesForTask(ProcessDefinition def) throws SQLException, IOException, RemoteException;

    public List findTaskDefinitions(ProcessDefinition def) throws BPMException, RemoteException;

    /** Despliegue de versiones **/
    public void openDeployParDefinitionTransfer() throws BPMException, RemoteException;

    public void nextDeployParDefinitionPackage(byte[] filePackage, int length) throws BPMException, RemoteException;

    public void endDeployParDefinitionTransfer() throws BPMException, RemoteException;

    public String[] getDeployMessages() throws RemoteException;

    public TaskInstance getTask(long id) throws BPMException, RemoteException;
}
