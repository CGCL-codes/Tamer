package fi.hip.gb.client;

import java.net.URL;
import java.rmi.RemoteException;
import fi.hip.gb.core.Config;
import fi.hip.gb.core.JobSecurity;
import fi.hip.gb.core.Logger;
import fi.hip.gb.core.WorkDescription;
import fi.hip.gb.server.DefaultService;
import fi.hip.gb.server.Service;
import fi.hip.gb.server.rpc.AxisService;
import fi.hip.gb.utils.FileUtils;

/**
 * Client for executing general operations on GBAgent server.
 * <p>
 * List of available sessions can be retrieved using 
 * {@link fi.hip.gb.client.Client#getJobsFromServer()} method.
 * All jobs can be removed with  
 * {@link fi.hip.gb.client.Client#removeAllJobs()} method.

 * 
 * @author Juho Karppinen
 * @version $Id: Client.java 102 2004-11-12 14:31:37Z jkarppin $
 */
public class Client {

    /** URL of the service */
    private URL serviceURL;

    /** the directory where received Axis attachments are stored */
    private String workingDir;

    /**
     * Create new client for communication with the server.
     * 
     * @param serviceURL
     *            service URL of the server
     * @param workingDirectory directory where received attachments are
     * downloaded, 
     */
    public Client(URL serviceURL, String workingDirectory) {
        this.serviceURL = serviceURL;
        this.workingDir = workingDirectory;
    }

    /**
     * Opens connection to the service.
     * 
     * @return service object to communication to the server
     * @throws RemoteException
     *             if something went wrong
     */
    protected Service openConnection() throws RemoteException {
        if (serviceURL.getProtocol().equals("file")) {
            return new DefaultService();
        }
        JobSecurity sec = new JobSecurity();
        if (serviceURL.getProtocol().equals("https")) {
            sec = Config.getSecurity();
        }
        return new AxisService(serviceURL, sec, this.workingDir);
    }

    /**
     * Removes all jobs owned by the user from the server 
     */
    public void removeAllJobs() {
        try {
            Service service = openConnection();
            service.abort(new Long(-1), Service.ABORT_AND_REMOVE);
        } catch (RemoteException e) {
            Main.errorMessage(e.getMessage());
            Logger.error(e.getMessage(), e);
        }
    }

    /**
     * Retrieves available jobs from the server. All attachment files of agents are 
     * automatically downloaded to the correct working directories. 
     * 
     * @return array of WorkDescription classes
     */
    public WorkDescription[] getJobsFromServer() {
        try {
            Service service = openConnection();
            Long[] sessions = service.getJoblist(Boolean.TRUE, Boolean.TRUE, "*");
            WorkDescription[] descriptions = new WorkDescription[sessions.length];
            for (int i = 0; i < sessions.length; i++) {
                FileUtils.createDir(Config.getWorkingDir(sessions[i]));
                if (service instanceof AxisService) {
                    AxisService axisService = (AxisService) service;
                    axisService.setAttachmentDirectory(Config.getWorkingDir(sessions[i]));
                }
                descriptions[i] = service.getDescription(sessions[i]);
                if (descriptions[i] == null) throw new RemoteException("Description was empty for id " + sessions[i]);
            }
            return descriptions;
        } catch (RemoteException e) {
            Main.errorMessage(e.getMessage());
            Logger.error(e.getMessage(), e);
            return null;
        }
    }
}
