package equilClient;

import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.*;
import equilSharedFramework.*;

/**
 * Used for communication with EQUIL Server.
 * 
 * @author CITS3200 2006 Group E
 * @version 1.0.0
 */
public class OutgoingCommunicator {

    private ServerInterface server;

    private IncomingCommunicator incomingCommunicator;

    /**
     * Creates a new OutgoingCommunicator object.
     * @param ic the IncomingCommunicator to be used to later send to server
     *        for use in callbacks
     */
    public OutgoingCommunicator(IncomingCommunicator ic) throws Exception {
        incomingCommunicator = ic;
        try {
            UnicastRemoteObject.exportObject(incomingCommunicator);
        } catch (RemoteException e) {
            throw new Exception("Error occured while exporting communication" + " object.\nUnable to continue.");
        }
    }

    /**
     * This method must be called before any other methods.
     * <p>
     * Finds the server by reading config file and then doing a lookup on the
     * RMI registry at supposed server address specified in config.
     * @return a Reply object containing information regarding success or
     *         failure. The message of Reply will explain what step in finding
     *         the server caused the problem.
     */
    public Reply findServer() {
        File configFile;
        configFile = new File(System.getProperty("user.home") + GUIProperties.FILE_SEPERATOR + GUIProperties.HOME_CONFIG_DIRECTORY + GUIProperties.FILE_SEPERATOR + GUIProperties.CONFIG_FILE_NAME);
        if (!configFile.exists() || !configFile.canRead()) {
            configFile = new File(GUIProperties.MAIN_CONFIG_DIRECTORY + GUIProperties.FILE_SEPERATOR + GUIProperties.CONFIG_FILE_NAME);
            if (!configFile.exists()) {
                return new Reply(false, "Couldn't find config file.");
            } else if (!configFile.canRead()) {
                return new Reply(false, "Couldn't read config file.");
            }
        }
        String firstline = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(configFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            LineNumberReader reads = new LineNumberReader(inputStreamReader);
            firstline = reads.readLine();
        } catch (IOException e) {
            return new Reply(false, "Error while reading config file.");
        }
        if (firstline == null) {
            return new Reply(false, "Config file not in expected format.");
        }
        try {
            server = (ServerInterface) Naming.lookup(firstline);
            return new Reply(true, "Successfully found server.");
        } catch (Exception e) {
            return new Reply(false, "Could not find EQUIL server.");
        }
    }

    /**
     * Calls remote method on server to log student onto EQUIL system.<p>
     * An IncomingCommunicator must be passed with it so the server can 
     * call methods on it to communicate with client in the future.<p>
     * If the server already has an IncomingCommunicator (say from a 
     * demonstrator login), it is simply ignored.
     * @return  a Reply with the session key as the message for identify 
     *          authentication if successful, otherwise Reply with explaination 
     *          of failure.
     * @param username  the username for the student.
     * @param password  the password for the student.
     */
    public Reply studentLogin(String username, String password) {
        Reply reply;
        try {
            reply = server.studentLogin(username, password);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    ;

    /**
     * Calls remote method on server to log student onto EQUIL system.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply studentLogoff(String sessionKey) {
        try {
            return server.studentLogoff(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to allow student to join the specified lab. session.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply studentJoinLab(String sessionKey, String lab, String studentUserName, String machineName) {
        Reply reply = null;
        try {
            reply = server.studentAddToLab(sessionKey, lab, studentUserName, machineName, incomingCommunicator);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to request the addition of student to
     * the queue.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply studentAddToQueue(String sessionKey) {
        try {
            return server.studentAddToQueue(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to request the student's removal from 
     * the queue.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply studentRemoveFromQueue(String sessionKey) {
        try {
            return server.studentRemoveFromQueue(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to notify it that student has been attended
     * to. If successful, this will remove the student from the queue, but the
     * information can be used by the server differently.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply studentNotifyAttendedTo(String sessionKey) {
        try {
            return server.studentNotifyAttendedTo(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to log demonstrator into the EQUIL 
     * system.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorLogin(String username, String password) {
        Reply reply;
        try {
            reply = server.demonstratorLogin(username, password);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to log demonstrator off from the EQUIL 
     * system.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorLogoff(String sessionKey) {
        try {
            return server.demonstratorLogoff(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to request the removal of a student
     * from the queue.
     * @param sessionKey the session key used to identify the demonstrator
     * @param student the string identifying the student, normally username
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorRemoveStudent(String sessionKey, String student) {
        Reply reply;
        try {
            reply = server.demonstratorRemoveStudent(sessionKey, student);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to request the start of a new lab
     * session.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorStartLab(String sessionKey, String labName, String labDescription) {
        Reply reply;
        try {
            reply = server.demonstratorStartLab(sessionKey, labName, labDescription);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to request the current lab be stopped.
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorStopLab(String sessionKey) {
        Reply reply;
        try {
            reply = server.demonstratorStopLab(sessionKey);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to request a list of the possible labs that
     * aren't running.
     * @return  a 2-dimensional array where the (i,0) entry is the unique lab ID
     *          and the (i,1) entry is the lab description. Return value is null
     *          if there is a communication problem with server.
     */
    public String[][] demonstratorGetStartableLabs(String sessionKey) {
        try {
            return server.getAllLabs(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Calls remote method on server to allow demonstrator to start managing the
     * specified lab session.
     * @param sessionKey the session key to send as identification
     * @param lab the name of the lab to join
     * @return  object with information regarding the success of the attempt.
     */
    public Reply demonstratorJoinLab(String sessionKey, String lab) {
        Reply reply = null;
        try {
            reply = server.demonstratorAddToLab(sessionKey, lab, incomingCommunicator);
        } catch (RemoteException e) {
            reply = null;
        }
        return reply;
    }

    /**
     * Calls remote method on server to request a list of the running labs.
     * @return  a 2-dimensional array where the (i,0) entry is the unique lab ID
     *          and the (i,1) entry is the lab description. Returns 
     */
    public String[][] getRunningLabs(String sessionKey) {
        try {
            return server.getRunningLabs(sessionKey);
        } catch (RemoteException e) {
            return null;
        }
    }
}
