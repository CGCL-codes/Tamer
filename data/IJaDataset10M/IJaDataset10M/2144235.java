package edu.umb.cs.antmanager.manager;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import edu.umb.cs.antmanager.common.AgentInterface;
import edu.umb.cs.antmanager.common.MachineStatus;
import edu.umb.cs.antmanager.domain.TestType;
import static edu.umb.cs.antmanager.common.Constants.*;

/**
 * For manger starting a thread to execute a specific task on one agent and call
 * back to the manager after the execution.
 * 
 * @author 2009CS682-3UMB_AntManager
 * 
 */
public class AgentConnRunnable implements Runnable {

    private final String agentName;

    private final AntManager manager;

    private final int taskType;

    private TestType test;

    /**
	 * Construct a AgentConnRunnable for test execution
	 * 
	 * @param agentName
	 *            Agent's host name
	 * @param manager
	 *            manager's server object for call back
	 * @param taskType
	 *            The type of task to run
	 * @param test
	 *            <tt>TestType</tt> test to execute
	 */
    public AgentConnRunnable(String agentName, AntManager manager, int taskType, TestType test) {
        this(agentName, manager, taskType);
        this.test = test;
    }

    /**
	 * Construct a AgentConnRunnable for other operation
	 * 
	 * @param agentName
	 *            Agent's host name
	 * @param manager
	 *            manager's server object for call back
	 * @param taskType
	 *            The type of task to run
	 */
    public AgentConnRunnable(String agentName, AntManager manager, int taskType) {
        this.agentName = agentName;
        this.manager = manager;
        this.taskType = taskType;
    }

    @Override
    public void run() {
        AgentInterface agent = getAgentConnector(agentName);
        try {
            MachineStatus machineStatus = null;
            if (agent != null) if (taskType == TASK_TYPE_EXECUTE) {
                if (!agent.executeTask(test, manager)) {
                    System.out.println(agentName + " is busy, " + test.getName() + " is not executed!");
                    manager.updateTestStatus(test, TestInfo.TestStatus.UNTESTED);
                }
            } else {
                machineStatus = agent.getStatus();
            }
            String status = null;
            if (machineStatus != null) status = machineStatus.getStatus();
            manager.updateMachineStatus(agentName, status);
        } catch (RemoteException e) {
            System.err.println("Machine " + agentName + " is offline");
            manager.exit();
        }
    }

    /**
	 * Connect to the Agent
	 * 
	 * @param agentName
	 *            Agent machine name
	 * @return AgentInterface
	 */
    private AgentInterface getAgentConnector(String agentName) {
        try {
            return RMIServerConnector.createAgentConnector(agentName);
        } catch (RemoteException e) {
            System.err.println("Machine " + agentName + " is offline");
            manager.exit();
        } catch (MalformedURLException e) {
        } catch (NotBoundException e) {
            System.err.println("Machine " + agentName + " is offline");
            manager.exit();
        }
        return null;
    }
}
