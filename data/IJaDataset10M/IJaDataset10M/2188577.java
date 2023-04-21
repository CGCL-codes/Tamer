package ossobook.client.synchronization.step;

import java.util.*;
import java.rmi.*;
import ossobook.server.ServerMethods;
import ossobook.sqlQueries.*;
import ossobook.client.LoginData;
import ossobook.exceptions.*;
import ossobook.communication.file.*;

/**
 * makes project synchronization - reintegration
 * @author j.lamprecht
 *
 */
public class LocalChangesTransmitter extends Step {

    public LocalChangesTransmitter(LocalQueryManager manager, ServerMethods stub) {
        super(manager, stub);
    }

    /**
	 * reintegrate project: contains logic of reintegration
	 * @param projectKey
	 * @param projectName
	 * @throws NoReadRightExceptionForSynchronization
	 */
    public String transmitChanges(String projectName, int[] projectKey, Vector<String[][]> scheme) throws NoWriteRightExceptionForSynchronization {
        Logging logging;
        try {
            logging = retransmitChanges(projectKey, projectName, scheme);
            logging = transmitNewChanges(projectKey, projectName, scheme, logging);
            if (logging.getRowsOfContent() <= 4) return null; else return logging.getPath();
        } catch (StatementNotExecutedException s) {
        } catch (MetaStatementNotExecutedException s) {
        }
        return null;
    }

    /**
	 * reintegrate changes that havn't been confirmed by server last synchronization
	 * @param projectKey
	 * @param projectName
	 * @param scheme - expected scheme of global database table consiting of name and type
	 * @return
	 * @throws StatementNotExecutedException
	 * @throws MetaStatementNotExecutedException
	 * @throws NoWriteRightExceptionForSynchronization
	 */
    private Logging retransmitChanges(int[] projectKey, String projectName, Vector<String[][]> scheme) throws StatementNotExecutedException, MetaStatementNotExecutedException, NoWriteRightExceptionForSynchronization {
        manager.disableAutoCommit();
        String lastSynchronization = manager.getLastSynchronizationOfProject(projectKey);
        Vector<String[][]> changes = getProjectChanges(projectKey, scheme);
        int[] messageIdentifier = getMessageIdentifier(projectKey);
        manager.enableAutoCommit();
        return logChanges(changes, lastSynchronization, projectKey, projectName, null, messageIdentifier);
    }

    /**
	 * reintegrate since last synchronization executed changes
	 * @param projectKey
	 * @param projectName
	 * @param scheme - expected scheme of global database table consiting of name and type
	 * @param logging - logging created during retransmit
	 * @return
	 * @throws StatementNotExecutedException
	 * @throws MetaStatementNotExecutedException
	 * @throws NoWriteRightExceptionForSynchronization
	 */
    private Logging transmitNewChanges(int[] projectKey, String projectName, Vector<String[][]> scheme, Logging logging) throws StatementNotExecutedException, MetaStatementNotExecutedException, NoWriteRightExceptionForSynchronization {
        manager.disableAutoCommit();
        String lastSynchronization = manager.getLastSynchronizationOfProject(projectKey);
        manager.setMessageNumber(projectKey);
        Vector<String[][]> changes = getProjectChanges(projectKey, scheme);
        int[] messageIdentifier = getMessageIdentifier(projectKey);
        manager.enableAutoCommit();
        return logChanges(changes, lastSynchronization, projectKey, projectName, logging, messageIdentifier);
    }

    /**
	 * get message identifier consisting of next message number to assign and database number
	 * @param projectKey
	 * @return
	 * @throws StatementNotExecutedException
	 */
    private int[] getMessageIdentifier(int[] projectKey) throws StatementNotExecutedException {
        int[] messageIdentifier = new int[2];
        messageIdentifier[0] = manager.getAssignedMessageNumber(projectKey);
        messageIdentifier[1] = manager.getDatabaseNumber();
        return messageIdentifier;
    }

    /**
	 * transmit changes and
	 * log occurred conflicts and save them on client
	 * @param changes
	 * @param lastSynchronization
	 * @param projectKey
	 * @param projectName
	 * @param logging
	 * @param messageIdentifier
	 * @return
	 * @throws StatementNotExecutedException
	 * @throws MetaStatementNotExecutedException
	 * @throws NoWriteRightExceptionForSynchronization
	 */
    private Logging logChanges(Vector<String[][]> changes, String lastSynchronization, int[] projectKey, String projectName, Logging logging, int[] messageIdentifier) throws StatementNotExecutedException, MetaStatementNotExecutedException, NoWriteRightExceptionForSynchronization {
        if (messageIdentifier[0] != -1) {
            Logging conflicts = changeGlobalDatabaseEntries(changes, projectKey, lastSynchronization, messageIdentifier);
            if (conflicts == null) throw new NoWriteRightExceptionForSynchronization(projectName);
            if (logging == null || logging.getRowsOfContent() <= 4) logging = new Logging(conflicts); else logging.append(conflicts);
            manager.disableAutoCommit();
            manager.setSynchronized(projectKey);
            manager.enableAutoCommit();
        } else {
            if (logging == null) {
                logging = new Logging("Konflikte-" + projectName, projectName);
                logging.log("Projekt: " + projectName + "\n");
                logging.log("-------------------------------------------------------------\n\n");
                logging.log("Es sind keine Widersprüche zum Inhalt der globalen Datenbank aufgetreten");
            }
        }
        return logging;
    }

    /**
	 * get project changes from local database
	 * @param projectKey
	 * @param scheme
	 * @return
	 * @throws StatementNotExecutedException
	 * @throws MetaStatementNotExecutedException
	 */
    private Vector<String[][]> getProjectChanges(int[] projectKey, Vector<String[][]> scheme) throws StatementNotExecutedException, MetaStatementNotExecutedException {
        Vector<String[][]> changes = manager.getChangesOfProject(projectKey, scheme);
        return changes;
    }

    /**
	 * send project changes to server
	 * @param changes
	 * @param projectKey
	 * @param lastSynchronization
	 * @param messageIdentifier
	 * @return
	 * @throws StatementNotExecutedException
	 * @throws MetaStatementNotExecutedException
	 */
    private Logging changeGlobalDatabaseEntries(Vector<String[][]> changes, int[] projectKey, String lastSynchronization, int[] messageIdentifier) throws StatementNotExecutedException, MetaStatementNotExecutedException {
        Logging conflicts = null;
        try {
            conflicts = stub.insertLocalChanges(LoginData.encryptUsername(), LoginData.encryptPassword(), projectKey, changes, lastSynchronization, messageIdentifier);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return conflicts;
    }
}
