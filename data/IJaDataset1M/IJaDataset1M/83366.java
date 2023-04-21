package edu.mit.lcs.haystack.server.standard.melatonin;

import edu.mit.lcs.haystack.Constants;
import edu.mit.lcs.haystack.proxy.IServiceAccessor;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.PackageFilterRDFContainer;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.RDFNode;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import edu.mit.lcs.haystack.rdf.Utilities;
import edu.mit.lcs.haystack.server.core.service.GenericService;
import edu.mit.lcs.haystack.server.core.service.ServiceException;
import edu.mit.lcs.haystack.server.core.service.ServiceManager;
import org.apache.log4j.Category;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Dennis Quan
 */
public class MelatoninAgent extends GenericService {

    static final Category s_logger = Category.getInstance(MelatoninAgent.class);

    protected IRDFContainer m_jobStore;

    protected LinkedList m_jobs = new LinkedList();

    protected ThreadGroup m_workerThreads;

    protected int m_threadCount = 3;

    protected int m_pauseBetweenJobs = 100;

    protected boolean m_running = false;

    public static MelatoninAgent getMelatoninAgent(IRDFContainer source, IServiceAccessor sa) {
        try {
            RDFNode[] datum = source.queryExtract(new Statement[] { new Statement(Utilities.generateWildcardResource(1), Constants.s_rdf_type, MelatoninConstants.s_MelatoninAgent), new Statement(sa.getResource(), Constants.s_config_hostsService, Utilities.generateWildcardResource(1)) }, Utilities.generateWildcardResourceArray(1), Utilities.generateWildcardResourceArray(1));
            if (datum != null) {
                return (MelatoninAgent) sa.connectToService((Resource) datum[0], null);
            }
        } catch (Exception e) {
            s_logger.error("Failed to connect to Melatonin agent", e);
        }
        return null;
    }

    public void init(String basePath, ServiceManager manager, Resource res) throws ServiceException {
        super.init(basePath, manager, res);
        IRDFContainer source = manager.getRootRDFContainer();
        String x = Utilities.getLiteralProperty(res, MelatoninConstants.s_threadCount, source);
        if (x != null) {
            m_threadCount = Integer.parseInt(x);
        }
        x = Utilities.getLiteralProperty(res, MelatoninConstants.s_pauseBetweenJobs, source);
        if (x != null) {
            m_pauseBetweenJobs = Integer.parseInt(x);
        }
        m_jobStore = source;
        Resource jobStore = Utilities.getResourceProperty(res, MelatoninConstants.s_jobStore, source);
        if (jobStore != null) {
            try {
                m_jobStore = (IRDFContainer) manager.connectToService(jobStore, null);
            } catch (Exception e) {
                s_logger.error("Could not connect to job store " + jobStore + "; resorting to root store", e);
            }
        }
        File file = new File(basePath, "jobs");
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                m_jobs = (LinkedList) ois.readObject();
                ois.close();
            } catch (Exception e) {
                s_logger.error("Could not retrieve saved jobs from disk.", e);
            } finally {
                try {
                    fis.close();
                } catch (IOException e1) {
                }
            }
        }
        m_workerThreads = new ThreadGroup("Melatonin worker threads for agent " + res);
        m_workerThreads.setMaxPriority(Thread.MIN_PRIORITY);
        startThreads();
    }

    public void submitJob(Job job, IRDFContainer metadata, boolean rush) {
        Resource jobID = job.getID();
        IRDFContainer source = new PackageFilterRDFContainer(m_jobStore, jobID);
        try {
            if (metadata != null) {
                source.add(metadata);
            }
            source.add(jobID, Constants.s_rdf_type, MelatoninConstants.s_Job);
        } catch (RDFException e) {
            s_logger.error("Failed to store metadata for job " + jobID, e);
        }
        updateJobStatus(job, MelatoninConstants.s_waitingToStart);
        synchronized (m_jobs) {
            if (rush) {
                m_jobs.addFirst(job);
            } else {
                m_jobs.addLast(job);
            }
        }
    }

    public boolean cancelJob(Resource jobID) {
        synchronized (m_jobs) {
            Iterator i = m_jobs.iterator();
            while (i.hasNext()) {
                Job job = (Job) i.next();
                if (job.getID().equals(jobID)) {
                    i.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rushJob(Resource jobID) {
        synchronized (m_jobs) {
            Iterator i = m_jobs.iterator();
            while (i.hasNext()) {
                Job job = (Job) i.next();
                if (job.getID().equals(jobID)) {
                    i.remove();
                    m_jobs.addFirst(job);
                    return true;
                }
            }
        }
        return false;
    }

    public void startThreads() {
        if (m_running) {
            return;
        }
        m_running = true;
        for (int i = 0; i < m_threadCount; i++) {
            WorkerThread wt = new WorkerThread(i);
            wt.start();
        }
    }

    public void stopThreads() {
        m_running = false;
        while (m_workerThreads.activeCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    public void cleanup() throws ServiceException {
        super.cleanup();
        stopThreads();
        File file = new File(m_basePath, "jobs");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_jobs);
            oos.close();
        } catch (Exception e) {
            s_logger.error("Could not save jobs to disk.", e);
        } finally {
            try {
                fos.close();
            } catch (IOException e1) {
            }
        }
    }

    protected void updateJobStatus(Job job, Resource status) {
        Resource jobID = job.getID();
        try {
            if (status == null) {
                m_jobStore.remove(new Statement(jobID, MelatoninConstants.s_state, Utilities.generateWildcardResource(1)), Utilities.generateWildcardResourceArray(1));
            } else {
                m_jobStore.replace(jobID, MelatoninConstants.s_state, null, status);
            }
        } catch (RDFException e) {
            s_logger.error("Failed to update status for job " + jobID, e);
        }
    }

    class WorkerThread extends Thread {

        WorkerThread(int i) {
            super(m_workerThreads, Integer.toString(i + 1));
        }

        public void run() {
            while (m_running) {
                Job job = null;
                synchronized (m_jobs) {
                    if (!m_jobs.isEmpty()) {
                        job = (Job) m_jobs.removeFirst();
                    }
                }
                if (job != null) {
                    updateJobStatus(job, MelatoninConstants.s_running);
                    job.initialize(m_serviceManager.getRootRDFContainer(), m_serviceManager);
                    try {
                        job.run();
                        updateJobStatus(job, null);
                        Utilities.uninstallPackage(job.getID(), m_jobStore);
                    } catch (Exception e) {
                        s_logger.error("An error occurred running job " + job.getID(), e);
                        updateJobStatus(job, MelatoninConstants.s_failed);
                    }
                }
                try {
                    Thread.sleep(m_pauseBetweenJobs);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
