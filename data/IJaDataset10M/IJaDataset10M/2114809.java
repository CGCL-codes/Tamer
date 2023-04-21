package org.mcisb.util.task;

import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
 * @author Neil Swainston
 *
 */
public class TaskFactoryImpl extends UnicastRemoteObject implements TaskFactory {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 * @throws RemoteException
	 */
    public TaskFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public Task getTask(int task, Object parameters) {
        switch(task) {
            case TaskConstants.PRIDE_TASK:
                {
                    return null;
                }
        }
        return null;
    }

    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        final String name = "//" + InetAddress.getLocalHost().getHostAddress() + "/" + NAME;
        Naming.rebind(name, new TaskFactoryImpl());
    }
}
