package it.webscience.kpeople.client.activiti;

import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiExecuteTaskException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiGetTaskException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiGetTaskFormException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiListTaskException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiLoginException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiPerformTaskOperationException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiStartPatternException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiTaskSummaryException;
import it.webscience.kpeople.client.activiti.object.ActivitiProcessInstance;
import it.webscience.kpeople.client.activiti.object.ActivitiTask;
import it.webscience.kpeople.client.activiti.object.ActivitiTaskPerformResponse;
import it.webscience.kpeople.client.activiti.object.ActivitiUserTaskList;

public interface IActivitiClient {

    /**
	 * Metodo che consente di avviare un Processo sul workflow engine Activiti.
	 * @param pActivitiParams
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiStartPatternException
	 */
    String startActivitiProcess(final String jSonParams) throws KPeopleActivitiStartPatternException;

    /**
	 * Metodo che consente di avviare un Processo sul workflow engine Activiti.
	 * @param pActivitiParams
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiStartPatternException
	 */
    ActivitiProcessInstance startActivitiProcessObj(final String jSonParams) throws KPeopleActivitiStartPatternException;

    /**
	 * Metodo che consente di eseguire una operazione su un task. 
	 * Le operazioni che Activiti supporta sono "Claim" e "Complete".
	 * @param pTaskId
	 * @param pJSonParams
	 * @param pTaskOperation
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiPerformTaskOperationException
	 */
    String performTaskOperation(final String pTaskId, final String pJSonParams, final String pTaskOperation) throws KPeopleActivitiPerformTaskOperationException;

    /**
	 * Metodo che consente di eseguire una operazione su un task. 
	 * Le operazioni che Activiti supporta sono "Claim" e "Complete".
	 * @param pTaskId
	 * @param pJSonParams
	 * @param pTaskOperation
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiPerformTaskOperationException
	 */
    ActivitiTaskPerformResponse performTaskOperationObj(final String pTaskId, final String pJSonParams, final String pTaskOperation) throws KPeopleActivitiPerformTaskOperationException;

    /**
	 * Metodo che consente di estrarre un sommario dei task relativi 
	 * ad uno user.
	 * @param pUserId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiTaskSummaryException
	 */
    String getTaskSummary(final String pUserId) throws KPeopleActivitiTaskSummaryException;

    /**
	 * Metodo che consente di estrarre i task da eseguire e assegnati 
	 * ad un utente. 
	 * @param pUserId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    String listTasksAssignee(final String pUserId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre i task da eseguire e assegnati 
	 * ad un utente. 
	 * @param pUserId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    ActivitiUserTaskList listTasksAssigneeObj(final String pUserId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre i task potenziali che uno user 
	 * può prendere in carico.
	 * @param pUserId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    String listTasksCandidate(final String pUserId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre i task potenziali che uno user 
	 * può prendere in carico.
	 * @param pUserId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    ActivitiUserTaskList listTasksCandidateObj(final String pUserId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre i task potenziali che un gruppo di user 
	 * può prendere in carico.
	 * @param pGroupId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    String listTasksCandidateGroup(final String pGroupId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre i task potenziali che un gruppo di user 
	 * può prendere in carico.
	 * @param pGroupId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiListTaskException
	 */
    ActivitiUserTaskList listTasksCandidateGroupObj(final String pGroupId) throws KPeopleActivitiListTaskException;

    /**
	 * Metodo che consente di estrarre le informazioni relative ad un task.
	 * @param pTaskId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiGetTaskException
	 */
    String getTask(final String pTaskId) throws KPeopleActivitiGetTaskException;

    /**
	 * Metodo che consente di estrarre le informazioni relative ad un task.
	 * @param pTaskId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiGetTaskException
	 */
    ActivitiTask getTaskObj(final String pTaskId) throws KPeopleActivitiGetTaskException;

    /**
	 * Metodo che consente di estrarre il form associato al task. 
	 * @param pTaskId
	 * @return response JSON del workflow engine
	 * @throws KPeopleActivitiGetTaskFormException
	 */
    String getTaskForm(final String pTaskId) throws KPeopleActivitiGetTaskFormException;

    /**
	 * Metodo che consente di effettuare il login su Activiti.
	 * @param pUsername
	 * @param pPassword
	 * @return
	 * @throws KPeopleActivitiLoginException
	 */
    String login(final String pUsername, final String pPassword) throws KPeopleActivitiLoginException;
}
