package org.epoline.impexp.jsf.repos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.util.*;
import org.apache.log4j.Logger;
import org.epo.utils.*;
import org.epoline.impexp.jsf.repos.dl.*;
import org.epoline.jsf.client.ServiceNotAvailableException;
import org.epoline.jsf.client.ServiceStatus;
import org.epoline.jsf.utils.Log4jManager;
import org.epoline.service.support.ErrorReportingInterface;
import org.epoline.service.support.PropertyException;
import org.epoline.service.support.ServiceSupport;

/**
 * Repository Service is a service that allows to store data persistent in the filesystem in repositories (directories)
 */
public class RepositoryService extends UnicastRemoteObject implements ReposServerInterface {

    private static Logger log;

    private ErrorReportingInterface errorReporter;

    private File root;

    private long curRequests;

    private long totalRequests;

    /**
	 * @clientRole manages
	 * @directed directed
	 * @link aggregation <{org.epoline.impexp.jsf.repos.Session}>
	 * @supplierCardinality 0..*
	 */
    private Hashtable sessions;

    /** @link aggregation <{org.epoline.impexp.jsf.repos.Repos}> */
    private Hashtable repos;

    private int totalPriorities;

    private int totalStates;

    private Pipe[][] statusPipes;

    private Pipe[] delayPipes;

    /**
	 * @clientRole starts
	 * @directed directed
	 * @link aggregation
	 * @supplierCardinality 1
	 */
    private SessionCheckerDaemon daemon;

    private FileServer fileServer;

    private int fileServerPort;

    private String fileServerName;

    private String logFile;

    private StartRepositoryService serviceStarter;

    /**
	 * RepositoryService constructor
	 * @param props The property Object that contains the specific properties for this service
	 * @param aTotalStates The total amount of states the reposaitory keeps for each entry
	 * @param service The starter Class for this service, to notify cretion of repositories
	 * @exception ServiceNotAvailableException The service can't be started
	 * @exception RemoteException Problems with RMI
	 */
    public RepositoryService(Properties props, int aTotalStates, StartRepositoryService service) throws ServiceNotAvailableException, RemoteException {
        super();
        log = Log4jManager.getLogger(RepositoryService.class);
        try {
            logFile = ServiceSupport.getProperty(props, "org.epoline.services.logfile", "LOG_FILE", null, log);
            errorReporter = ServiceSupport.initErrorLogging(props, log);
            totalPriorities = Integer.parseInt(ServiceSupport.getProperty(props, "org.epoline.services.repos.totalPriorities", "TOTAL_PRIORITIES", null, log));
        } catch (PropertyException e) {
            log.error("repositoryService Not Started: " + e.toString());
            throw new ServiceNotAvailableException(e.toString());
        }
        serviceStarter = service;
        sessions = new Hashtable();
        repos = new Hashtable();
        totalStates = aTotalStates;
        statusPipes = new Pipe[totalStates][totalPriorities];
        delayPipes = new Pipe[totalStates];
        for (int i = 0; i < totalStates; i++) {
            delayPipes[i] = new Pipe();
            for (int j = 0; j < totalPriorities; j++) {
                statusPipes[i][j] = new Pipe();
            }
        }
        curRequests = 0;
        try {
            init(props);
        } catch (Exception e) {
            log.error("repositoryService Not Started: " + e.toString());
            throw new ServiceNotAvailableException(e.toString());
        }
        log.warn("ReposService Started");
    }

    /**
	 * Commit all actions done during the specified session.  This will also end the session.
	 * @param session The session tthat must be committed
	 * @Exception NotFoundException Session not valid
	 * @Exception GenericReposException any other repos specific errors
	 * @Exception RemoteException Any RMI infrastructure errors
	*/
    public void commit(ReposSession session) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("+commit(" + session.getId() + ")");
        }
        try {
            Session s = getSession(session);
            ArrayList addedEntries = s.commit();
            Iterator it = addedEntries.iterator();
            while (it.hasNext()) {
                ReposEntry cur = (ReposEntry) it.next();
                int priority = Math.min(cur.getPriority(), totalPriorities - 1);
                int status = cur.getStatus();
                statusPipes[status][priority].add(cur);
            }
            sessions.remove(s);
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-commit(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Delete the reposEntries with the specified ids. (either all or none are deleted)
	 * @param aRepos the Repos in which the entries are located
	 * @param entryIDs the unique IDs for the entries.
	 * @exception NotFoundException Session not valid, not all entries found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public void del(String aRepos, java.lang.String[] entryIDs) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+del(" + aRepos + ")");
        try {
            Repos repos = getRepos(aRepos);
            ReposSession s = startSession(aRepos, repos.getType());
            del(s, entryIDs);
            commit(s);
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-del(" + aRepos + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Delete the reposEntries with the specified ids. (either all or none are deleted)
	 * @param session The session to which this action belongs. No null session allowed
	 * @param entryID the unique ID for the entry.
	 * @exception NotFoundException Session not valid, not all entries found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public void del(ReposSession session, java.lang.String[] entryID) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+del(" + session.getId() + ")");
        try {
            Session s = getSession(session);
            Repos repos = s.getRepos();
            for (int i = 0; i < entryID.length; i++) {
                ReposEntry entry = repos.getEntry(entryID[i]);
                repos.remove(entry, true);
                SessionAction action = new SessionAction(SessionAction.DEL, entry);
                s.addAction(action);
                if (log.isDebugEnabled()) log.debug("Entry temp removed: " + entry.getId());
            }
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-del(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * The specified entry will be ignored until further notice. This is usefull in cases where a
	 * reposEntry can't be processed at this moment, but you want to try it later. The ignore is only valid for
	 * the call to getHighestPriority(). in all other calls it will be visable.
	 * @param entry the entry that should be ignored
	 * @exception NotFoundException ReposEntry can't be found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public void delay(ReposEntry entry) throws GenericReposException, NotFoundException {
        int status = entry.getStatus();
        delayPipes[status].add(new ReposEntry(entry));
    }

    /**
	 * Get the reposEntry with the specified id.
	 * @param aRepos The Reposatory in which the entry is stored
	 * @param entryID the unique ID for the entry.
	 * @return the requested ReposEntry. 
	 * @exception NotFoundException Repos not found or entry not found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public ReposEntry get(String aRepos, String entryID) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+get(" + aRepos + ")");
        try {
            Repos repos = getRepos(aRepos);
            return prepareEntryForJourney(repos.getEntry(entryID));
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-get(" + aRepos + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Get the reposEntry with the specified id.
	 * @param session The session to which this action belongs. No null session is allowed
	 * @param entryID the unique ID for the entry.
	 * @return the requested ReposEntry. 
	 * @exception NotFoundException Repos not found or entry not found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public ReposEntry get(ReposSession session, String entryID) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+get(" + session.getId() + ")");
        try {
            Session s = getSession(session);
            Repos repos = s.getRepos();
            return prepareEntryForJourney(repos.getEntry(entryID));
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-get(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Get the reposEntry with the specified id and remove it from the Repos.
	 * @param session The session to which this action belongs. No null session is allowed
	 * @param entryID the unique ID for the entry.
	 * @return requested ReposEntry
	 * @exception NotFoundException Session not valid or entry not found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public ReposEntry getAndDel(ReposSession session, String entryID) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+getAndDel(" + session.getId() + ")");
        try {
            Session s = getSession(session);
            Repos repos = s.getRepos();
            ReposEntry localEntry = repos.getEntry(entryID);
            ReposEntry res = prepareEntryForJourney(localEntry);
            repos.remove(localEntry, true);
            SessionAction action = new SessionAction(SessionAction.DEL, localEntry);
            s.addAction(action);
            return res;
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-getAndDel(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Add the entry in the specified repository Session and return a unique ID for this entry
	 * @param aRepos the Repos to put the entry in.
	 * @param type The type of the repository
	 * @param entry The entry that must be added.
	 * @return The unique ID for the entry added to the repos
	 * @exception NotFoundException Session not valid or entry not valid
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public String put(String aRepos, ReposType type, ReposEntry entry) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+put(" + aRepos + ")");
        if (entry.getPriority() >= totalPriorities) entry.setPriority(totalPriorities - 1);
        ReposEntry localEntry = new ReposEntry(entry);
        try {
            if (!localEntry.getData().containsData()) {
                String fileName = root.getAbsolutePath() + File.separator + localEntry.getData().getFileName();
                File tmpFile = new File(fileName);
                if (!tmpFile.exists()) {
                    log.warn("data for entry not found");
                    throw new NotFoundException("data for entry not found");
                }
                localEntry.setData(new FilePlaceHolder(null, 0, fileName, tmpFile.length()));
            }
            Repos repos = getOrCreateRepos(aRepos, type);
            localEntry.setReposName(aRepos);
            localEntry.setReposId(repos.getId());
            ReposEntry newEntry = repos.put(localEntry, false);
            String id = newEntry.getId();
            if (!localEntry.getData().containsData()) {
                File tmpFile = new File(localEntry.getData().getFileName());
                tmpFile.delete();
            }
            if (repos.getType().getType() == ReposType.INPUT) {
                statusPipes[newEntry.getStatus()][entry.getPriority()].add(newEntry);
            }
            if (log.isDebugEnabled()) log.debug("Entry added: " + id);
            return id;
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-put(" + aRepos + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Add the entry in the specified repository Session and return a unique ID for this entry
	 * @param session The session to which this action belongs. No null session is allowed
	 * @param entry The entry that must be added.
	 * @param priority The priority that this entry will get in the reposatory.
	 * @return The unique ID for the entry added to the repos
	 * @exception NotFoundException Session not valid or entry not valid
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public String put(ReposSession session, ReposEntry entry) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+put(" + session.getId() + ")");
        if (entry.getPriority() >= totalPriorities) entry.setPriority(totalPriorities - 1);
        ReposEntry localEntry = new ReposEntry(entry);
        try {
            Session s = getSession(session);
            if (!localEntry.getData().containsData()) {
                String fileName = root.getAbsolutePath() + File.separator + localEntry.getData().getFileName();
                File tmpFile = new File(fileName);
                if (!tmpFile.exists()) {
                    log.warn("data for entry not found");
                    throw new NotFoundException("data for entry not found");
                }
                localEntry.setData(new FilePlaceHolder(null, 0, fileName, tmpFile.length()));
            }
            Repos repos = s.getRepos();
            localEntry.setReposName(repos.getName());
            localEntry.setReposId(repos.getId());
            ReposEntry newEntry = repos.put(localEntry, true);
            SessionAction action = new SessionAction(SessionAction.ADD, newEntry);
            s.addAction(action);
            if (!localEntry.getData().containsData()) {
                File tmpFile = new File(localEntry.getData().getFileName());
                tmpFile.delete();
            }
            if (log.isDebugEnabled()) log.debug("TempEntry added: " + newEntry.getId());
            String tmpId = newEntry.getId();
            return tmpId.substring(0, tmpId.length() - Repos.TEMP_SUFFIX.length());
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-put(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * All entries that were marked to be ignored (ignore() will become visable again for getHighestPriority().
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public synchronized void resheduleDelayed() throws RemoteException, GenericReposException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+ResheduleDelayed()");
        try {
            for (int i = 0; i < totalStates; i++) {
                while (delayPipes[i].containsData()) {
                    ReposEntry cur = (ReposEntry) delayPipes[i].get();
                    statusPipes[i][cur.getPriority()].add(cur);
                }
            }
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-resheduleDelayed(): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Update the status of the specified entry
	 * @param aRepos the Repos to update the entry in.
	 * @param entry The entry that must get the new status
	 * @param newStatus the new status value
	 * @return ReposEntry The updated reposEntry
	 * @exception NotFoundException entry not valid
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	 */
    public ReposEntry updateStatus(ReposEntry entry, int newStatus) throws NotFoundException, GenericReposException, RemoteException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+updateStatus(): " + entry.getId());
        try {
            Repos repos = getRepos(entry.getReposName());
            int oldStatus = entry.getStatus();
            int priority = entry.getPriority();
            statusPipes[oldStatus][priority].remove(entry);
            ReposEntry newEntry = repos.updateStatus(entry, newStatus);
            statusPipes[newStatus][priority].add(newEntry);
            return newEntry;
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-updateStatus(): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Rollback all actions done during the specified session. This will also end the session.
	 * @param session The session tthat must be committed
	 * @exception NotFoundException Session not valid
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public void rollback(ReposSession session) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+rollback(" + session.getId() + ")");
        try {
            Session s = getSession(session);
            s.rollback();
            sessions.remove(s);
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-rollback(" + session.getId() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Start a session for the specified reposatory for either INPUT, OUTPUT, TEMP
	 * If the specified reposatory doesn't exists, it will be created.
	 * Reposatory names must be unique. The same name may not be used for different types.
	 * @param aRepos The reposatory to work with in this session
	 * @param aType The type of the reposatory
	 * @return the ReposSession to be used in next calls within session.
	 * @exception NotFoundException repos or ReposType not found
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public ReposSession startSession(String aRepos, ReposType aType) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+startSession(" + aRepos + ")");
        try {
            Repos r = getOrCreateRepos(aRepos, aType);
            ReposSession res = createSession(r);
            return res;
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-startSession(" + aRepos + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * get a list of ids of entries in the specified repos
	 * @param aRepos the name of the reposatory to list
	 * @param type The Type of repos.
	 * @return list of id's in the requested repos.
	 * @exception NotFoundException repos not found or not a valid Type
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public String[] list(String aRepos, ReposType aType) throws GenericReposException, RemoteException, NotFoundException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+list(" + aRepos + ")");
        try {
            Repos repos = getRepos(aRepos, aType);
            Collection c = repos.getElementSet();
            String[] res = new String[c.size()];
            int index = 0;
            Iterator it = repos.getElementSet().iterator();
            while (it.hasNext()) {
                res[index++] = ((ReposEntry) it.next()).getId();
            }
            return res;
        } catch (GenericReposException e) {
            log.error(e);
            throw e;
        } catch (NotFoundException e) {
            log.error(e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-list(" + aRepos + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * get a list of repos names of the specified type
	 * @param type The Type of repos.
	 * @return list repos names
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public String[] list(ReposType aType) throws GenericReposException, RemoteException {
        incrementCurRequests();
        incrementTotalRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+list(" + aType.getType() + ")");
        try {
            ArrayList temp = new ArrayList();
            Iterator it = repos.values().iterator();
            while (it.hasNext()) {
                Repos cur = (Repos) it.next();
                if (cur.getType().getType() == aType.getType()) temp.add(cur);
            }
            String[] res = new String[temp.size()];
            int index = 0;
            it = temp.iterator();
            while (it.hasNext()) {
                res[index++] = ((Repos) it.next()).getName();
            }
            return res;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-list(" + aType.getType() + "): (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Get the reposEntry with the the highest id from any reposatory of specified status
	 * @param type The Type of repos.
	 * @return requested entry or null if none present
	 * @exception GenericReposException any other repos specific errors
	 * @exception RemoteException Any RMI infrastructure errors
	*/
    public synchronized ReposEntry getHighestPriorityLocal(int status) throws GenericReposException {
        for (int i = statusPipes[status].length - 1; i >= 0; i--) {
            if (statusPipes[status][i].containsData()) {
                return prepareEntryForLocalJourney((ReposEntry) statusPipes[status][i].get());
            }
        }
        return null;
    }

    public int getTotalEntries(int status) {
        int res = 0;
        for (int i = statusPipes[status].length - 1; i >= 0; i--) {
            res += statusPipes[status][i].size();
        }
        return res;
    }

    public int getTotalEntriesDelayed() {
        int res = 0;
        for (int i = delayPipes.length - 1; i >= 0; i--) {
            res += delayPipes[i].size();
        }
        return res;
    }

    public void stop() {
        log.warn("+Stop()");
        stopSessionChecker();
        try {
            stopConnections();
        } catch (GenericReposException e) {
            log.error("Error stopping connections: " + e.getMessage());
        }
        log.warn("-Stop()");
    }

    /**
	 * Get the current load of this service
	 * @return the current load (the higher, the more load
	 * @exception RemoteException RMI problems
	 */
    public long getCurrentLoad() throws RemoteException {
        long res = 0;
        for (int i = 0; i < totalStates; i++) {
            for (int j = 0; j < totalPriorities; j++) {
                res += statusPipes[i][j].size();
            }
        }
        for (int i = 0; i < totalStates; i++) {
            res += delayPipes[i].size();
        }
        return res;
    }

    /**
	 * Get the fully specified name of the LogFile
	 * @return the fully specified name of the logfile
	 */
    public String getLogFile() {
        return logFile;
    }

    /**
	 * get the ServiceDetails for this service. This is called from the monitoring GUI
	 * @return The service details in XML format
	 * @exception RemoteException RMI problem
	 */
    public String getServiceDetails() throws RemoteException {
        incrementCurRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+getServiceDetails()");
        try {
            StringBuffer res = new StringBuffer();
            res.append(ServiceSupport.getStartServiceDetailsAsXML());
            res.append(ServiceSupport.getEndServiceDetailsAsXML());
            return res.toString();
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-getServiceDetails() (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Get the status of the service
	 * @return OK if fully functional, otherwhise an appropriate error
	 * @exception RemoteException RMI error
	 */
    public ServiceStatus getStatus() throws RemoteException {
        incrementTotalRequests();
        incrementCurRequests();
        long t1 = System.currentTimeMillis();
        if (log.isDebugEnabled()) log.debug("+getStatus()");
        try {
            return ServiceStatus.OK;
        } finally {
            if (log.isInfoEnabled()) {
                long t2 = System.currentTimeMillis();
                log.info("-getStatus() (" + String.valueOf(t2 - t1) + " ms)");
            }
            decrementCurRequests();
        }
    }

    /**
	 * Get the Total requests for this service
	 * @return the total requests handled by this service
	 * @exception RemoteException RMI error
	 */
    public long getTotalHits() throws RemoteException {
        return totalRequests;
    }

    private void startSessionChecker(int maxIdleSeconds) {
        if (daemon == null) {
            daemon = new SessionCheckerDaemon();
            daemon.setName("Repos SessionChecker");
            daemon.setSessions(sessions, (long) maxIdleSeconds * 1000L);
            daemon.setDaemon(true);
            daemon.start();
        }
    }

    private Repos createRepos(String aRepos, ReposType aType) throws GenericReposException {
        Repos res;
        try {
            res = new Repos(aRepos, root.getAbsolutePath(), aType);
            res.getEntries();
            repos.put(aRepos, res);
            if (serviceStarter != null) serviceStarter.repositoryCreated(aRepos);
            return res;
        } catch (InvalidParameterException e) {
            log.error(e.getMessage(), e);
            throw new GenericReposException(e.getMessage());
        }
    }

    private synchronized ReposSession createSession(Repos repos) {
        while (true) {
            String id = EPODate.now();
            if (sessions.get(id) == null) {
                Session s = new Session(id);
                s.setRepos(repos);
                sessions.put(id, s);
                return new ReposSession(id);
            }
        }
    }

    private synchronized void decrementCurRequests() {
        curRequests--;
    }

    private synchronized Repos getOrCreateRepos(String aRepos, ReposType aType) throws GenericReposException {
        try {
            return getRepos(aRepos, aType);
        } catch (NotFoundException e) {
            return createRepos(aRepos, aType);
        }
    }

    private Repos getRepos(String aRepos) throws NotFoundException {
        Repos res = (Repos) repos.get(aRepos);
        if (res != null) {
            return res;
        }
        log.warn(aRepos + " not found");
        throw new NotFoundException("Repos not found: " + aRepos);
    }

    private Repos getRepos(String aRepos, ReposType aType) throws NotFoundException, GenericReposException {
        if (aType == null) {
            log.error("No type specified for repository: " + aRepos);
            throw new GenericReposException("No type specified for repository: " + aRepos);
        }
        Repos res = (Repos) repos.get(aRepos);
        if (res != null) {
            if (res.getType().getType() == aType.getType()) return res; else throw new GenericReposException("Reposatory already exist for different type: " + aRepos);
        }
        log.warn(aRepos + " not found");
        throw new NotFoundException("Repos not found: " + aRepos);
    }

    private Session getSession(ReposSession aSession) throws GenericReposException {
        String id = aSession.getId();
        Session res = (Session) sessions.get(id);
        if (res == null) {
            log.error("Session not found: " + id);
            throw new GenericReposException("Session not found: " + id);
        }
        if (res.isActive()) {
            res.setLastAccessTime(System.currentTimeMillis());
            return res;
        }
        sessions.remove(id);
        log.warn("Session has been timed out and rollbacked: " + id);
        throw new GenericReposException("Session has been timed out and rollbacked: " + id);
    }

    private synchronized void incrementCurRequests() {
        curRequests++;
    }

    private synchronized void incrementTotalRequests() {
        totalRequests++;
    }

    private void init(Properties props) throws GenericReposException, PropertyException {
        String rootDir = ServiceSupport.getProperty(props, "org.epoline.services.repos.root.dir", "ROOT_DIR", null, log);
        root = new File(rootDir);
        if (!root.isDirectory()) {
            log.error("Root directory does not exist: " + rootDir);
            throw new GenericReposException("Root directory does not exist: " + rootDir);
        }
        try {
            fileServerPort = Integer.parseInt(ServiceSupport.getProperty(props, "org.epoline.service.repos.fileserver.port", "FILE_SERVER_PORT", null, log));
            fileServerName = java.net.InetAddress.getLocalHost().getHostName();
            String dir = root.getAbsolutePath();
            fileServer = new FileServer(dir, fileServerPort);
            new Thread(fileServer).start();
        } catch (FileTransferException e) {
            log.error("FileServer creation failed:", e);
            throw new GenericReposException("FileServer creation failed: " + e.getMessage());
        } catch (java.net.UnknownHostException e) {
            log.error("FileServer creation failed:", e);
            throw new GenericReposException("FileServer creation failed: " + e.getMessage());
        }
        String[] dirs = root.list();
        for (int i = 0; i < dirs.length; i++) {
            String name = rootDir + File.separator + dirs[i];
            File cur = new File(name);
            if (cur.isDirectory()) {
                File descr = new File(name + File.separator + Repos.REP_FILE);
                if (descr.exists()) {
                    String input;
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(descr));
                        input = in.readLine();
                        in.close();
                    } catch (Exception e) {
                        log.error("Invalid Reposatory found: " + name, e);
                        throw new GenericReposException("Invalid Reposatory found: " + name);
                    }
                    java.util.StringTokenizer parser = new java.util.StringTokenizer(input, ";");
                    int repType = Integer.parseInt(parser.nextToken());
                    String repName = parser.nextToken();
                    Repos rep = new Repos(repName, rootDir, dirs[i], new ReposType(repType));
                    if (rep.getEntries().size() == 0) {
                        Utils.deleteFile(name);
                    } else {
                        repos.put(repName, rep);
                        if (serviceStarter != null) serviceStarter.repositoryCreated(repName);
                        if (repType == ReposType.INPUT) prioritise(rep);
                    }
                }
            }
        }
        String maxIdle = ServiceSupport.getProperty(props, "org.epoline.services.repos.max.idle.session", "MAX_IDLE_SESSION", null, log);
        startSessionChecker(Integer.parseInt(maxIdle));
    }

    protected ReposEntry prepareEntryForJourney(ReposEntry entry) {
        ReposEntry res = new ReposEntry(entry);
        res.setData(new FilePlaceHolder(fileServerName, fileServerPort, res.getReposId() + File.separator + res.getId(), 0));
        return res;
    }

    protected ReposEntry prepareEntryForLocalJourney(ReposEntry entry) {
        ReposEntry res = new ReposEntry(entry);
        res.setData(new FilePlaceHolder(null, 0, root.getAbsolutePath() + File.separator + res.getReposId() + File.separator + res.getId(), 0));
        return res;
    }

    private void prioritise(Repos repos) {
        Iterator it = repos.getElementSet().iterator();
        while (it.hasNext()) {
            ReposEntry cur = (ReposEntry) it.next();
            int priority = cur.getPriority();
            if (priority >= totalPriorities) priority = totalPriorities - 1;
            int status = cur.getStatus();
            statusPipes[status][priority].add(cur);
        }
    }

    private void stopConnections() throws GenericReposException {
        Enumeration en = sessions.elements();
        while (en.hasMoreElements()) {
            Session session = (Session) en.nextElement();
            try {
                session.rollback();
            } catch (Exception e) {
            }
        }
        sessions.clear();
    }

    private void stopSessionChecker() {
        if (daemon != null) {
            daemon.interrupt();
            while (daemon.isAlive()) ;
            daemon = null;
        }
    }
}
