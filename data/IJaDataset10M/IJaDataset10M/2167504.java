package pubweb.supernode.sched.dhht;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import pubweb.IntegrityException;
import pubweb.InternalException;
import pubweb.Job;
import pubweb.JobProcess;
import pubweb.NotEnoughWorkersException;
import pubweb.supernode.sched.DynamicParameterContainer;
import pubweb.supernode.sched.RequirementsContainer;
import pubweb.supernode.sched.Scheduler;
import pubweb.supernode.sched.SchedulerListener;
import pubweb.supernode.sched.StaticParameterContainer;

public class DHHTScheduler extends Scheduler {

    public static final boolean debug = false;

    private Hashtable<Job, JobProcess[]> jobs = new Hashtable<Job, JobProcess[]>();

    private LowerEnvelope lowerEnvelope;

    private MigrationThread migrationThread;

    private boolean instantMode = false;

    public DHHTScheduler(SchedulerListener listener) {
        super(listener);
        lowerEnvelope = new LowerEnvelope();
        this.initThread();
    }

    public DHHTScheduler(SchedulerListener listener, boolean instantMode) {
        super(listener);
        lowerEnvelope = new LowerEnvelope();
        if (!instantMode) this.initThread(); else this.migrationThread = new MigrationThread();
        this.instantMode = instantMode;
    }

    public void setListener(SchedulerListener listener) {
        this.listener = listener;
    }

    public void initThread() {
        migrationThread = new MigrationThread();
        migrationThread.start();
    }

    private void checkProcessesForMigration(Peer peer) {
        Iterator<Entry<String, Process>> it = peer.getProcesses().entrySet().iterator();
        while (it.hasNext()) {
            Process proc = it.next().getValue();
            Peer target = lowerEnvelope.getPeerForHash(proc.getHashPosition());
            if (!target.equals(peer)) {
                it.remove();
                target.getProcesses().put(proc.getJobProcess().getJob().getId() + "-" + proc.getJobProcess().getPid(), proc);
                proc.setTarget(target.getGuid());
            }
        }
    }

    public void newJob(Job job, RequirementsContainer requirements) throws InternalException, NotEnoughWorkersException {
        if (lowerEnvelope.getPeers().size() == 0) throw new NotEnoughWorkersException();
        JobProcess[] assignments = new JobProcess[job.getNumberOfProcessors()];
        for (int i = 0; i < job.getNumberOfProcessors(); i++) {
            Process currentProcess = new Process(new JobProcess(job, i, ""));
            Peer lowestPeer = lowerEnvelope.getPeerForHash(currentProcess.getHashPosition());
            if (lowestPeer == null) throw new NotEnoughWorkersException();
            currentProcess.getJobProcess().setWorker(lowestPeer.getGuid());
            assignments[i] = currentProcess.getJobProcess();
            lowestPeer.getProcesses().put(job.getId() + "-" + currentProcess.getJobProcess().getPid(), currentProcess);
        }
        try {
            listener.startProcesses(assignments);
            jobs.put(job, assignments);
        } catch (Exception e) {
            throw new InternalException("could not start processes", e);
        }
    }

    @Override
    public void workerJoined(String guid, StaticParameterContainer staticParams, DynamicParameterContainer dynamicParams) throws InternalException {
        staticParams.cpuPower /= 40;
        lowerEnvelope.addPeer(new Peer(guid, 0, staticParams.cpuPower));
        workerLoadChanged(guid, dynamicParams);
    }

    @Override
    public void workerLoadChanged(String guid, DynamicParameterContainer dynamicParams) throws InternalException {
        synchronized (migrationThread) {
            Peer peer = lowerEnvelope.getPeers().get(guid);
            double oldWeight = peer.getWeight();
            lowerEnvelope.updatePeer(peer, dynamicParams.availCpuPower * peer.getStaticcpuPower());
            if (peer.getWeight() <= oldWeight) {
                checkProcessesForMigration(peer);
            } else {
                for (Peer p : lowerEnvelope.getPeers().values()) {
                    if (!p.equals(peer)) checkProcessesForMigration(p);
                }
            }
            if (this.instantMode) this.executeMigrations(); else migrationThread.notify();
        }
    }

    public void bulkWorkerLoadChanged(String guid, DynamicParameterContainer dynamicParams) throws InternalException {
        synchronized (migrationThread) {
            Peer peer = lowerEnvelope.getPeers().get(guid);
            lowerEnvelope.updatePeer(peer, dynamicParams.availCpuPower * peer.getStaticcpuPower());
        }
    }

    public void bulkWorkerLoadUpdatesComplete() throws InternalException {
        synchronized (migrationThread) {
            for (Peer p : lowerEnvelope.getPeers().values()) {
                checkProcessesForMigration(p);
            }
            if (this.instantMode) this.executeMigrations(); else migrationThread.notify();
        }
    }

    @Override
    public void processMigrated(JobProcess process, String srcGuid) throws IntegrityException, InternalException {
        synchronized (migrationThread) {
            Process proc = lowerEnvelope.getPeers().get(process.getWorker()).getProcesses().get(process.getJob().getId() + "-" + process.getPid());
            if (proc == null) {
                for (Peer peer : lowerEnvelope.getPeers().values()) {
                    proc = peer.getProcesses().get(process.getJob().getId() + "-" + process.getPid());
                    if (proc != null) {
                        break;
                    }
                }
            }
            if (proc == null) {
                throw new IntegrityException("DHHT.processMigrated: no such process: " + process);
            }
            proc.getJobProcess().setWorker(process.getWorker());
            proc.setMigrating(false);
            if (!this.instantMode) migrationThread.notify();
        }
    }

    public JobProcess[] locateJob(Job job) throws IntegrityException {
        JobProcess[] procs = jobs.get(job);
        if (procs == null) {
            throw new IntegrityException("no such job");
        }
        return procs;
    }

    public JobProcess locateJobProcess(Job job, int pid) throws IntegrityException {
        JobProcess[] procs = jobs.get(job);
        if (procs == null) {
            throw new IntegrityException("no such job");
        }
        if (pid < 0 || pid > procs.length || procs[pid] == null) {
            throw new IntegrityException("no such process");
        }
        return procs[pid];
    }

    @Override
    public void jobDied(Job job) throws IntegrityException, InternalException {
        JobProcess[] procs = jobs.get(job);
        if (procs == null) {
            throw new IntegrityException("no such job");
        }
        for (int i = 0; i < procs.length; i++) {
            if (procs[i] != null) {
                Process proc = lowerEnvelope.getPeers().get(procs[i].getWorker()).getProcesses().remove(job.getId() + "-" + procs[i].getPid());
                HashObject.processHasher.returnValue(proc.getHashPosition());
            }
        }
        jobs.remove(job);
    }

    @Override
    public void processFinished(JobProcess process) throws IntegrityException, InternalException {
        Process proc = lowerEnvelope.getPeers().get(process.getWorker()).getProcesses().remove(process.getJob().getId() + "-" + process.getPid());
        if (proc == null) {
            for (Peer peer : lowerEnvelope.getPeers().values()) {
                proc = peer.getProcesses().remove(process.getJob().getId() + "-" + process.getPid());
                if (proc != null) {
                    break;
                }
            }
        }
        if (proc == null) {
            throw new IntegrityException("DHHT.processFinished: no such process: " + process);
        }
        HashObject.processHasher.returnValue(proc.getHashPosition());
        boolean allNull = true;
        JobProcess[] procs = jobs.get(process.getJob());
        for (int i = 0; i < procs.length; i++) {
            if (procs[i] != null) {
                if (procs[i].getPid() == process.getPid()) procs[i] = null; else allNull = false;
            }
        }
        if (allNull) jobs.remove(process.getJob());
    }

    @Override
    public void workerLeft(String guid) throws InternalException {
        synchronized (migrationThread) {
            Peer peer = lowerEnvelope.getPeers().get(guid);
            lowerEnvelope.updatePeer(peer, 0);
            for (Process proc : peer.getProcesses().values()) {
                Peer target = lowerEnvelope.getPeerForHash(proc.getHashPosition());
                target.getProcesses().put(proc.getJobProcess().getJob().getId() + "-" + proc.getJobProcess().getPid(), proc);
                proc.getJobProcess().setWorker(target.getGuid());
            }
            lowerEnvelope.removePeer(peer);
            HashObject.peerHasher.returnValue(peer.getHashPosition());
        }
    }

    public LowerEnvelope getLowerEnvelope() {
        return lowerEnvelope;
    }

    public Hashtable<Job, JobProcess[]> getJobs() {
        return jobs;
    }

    public Object getScheduleSnapshot() {
        List<Object> l = new ArrayList<Object>();
        l.add(lowerEnvelope);
        l.add(jobs);
        return l;
    }

    public DHHTScheduler(LowerEnvelope lowerEnvelope, Hashtable<Job, JobProcess[]> jobs, SchedulerListener listener) {
        super(listener);
        this.lowerEnvelope = lowerEnvelope;
        this.jobs = jobs;
    }

    private void executeMigrations() {
        for (Peer peer : lowerEnvelope.getPeers().values()) {
            for (Process p : peer.getProcesses().values()) {
                if (!p.isMigrating() && p.getTarget() != null && !p.getJobProcess().getWorker().equals(p.getTarget())) {
                    try {
                        p.setMigrating(true);
                        JobProcess j = p.getJobProcess();
                        listener.migrateProcess(j.getWorker(), p.getTarget(), new JobProcess(j.getJob(), j.getPid(), j.getWorker()));
                    } catch (Exception e) {
                        System.err.println("DHHT: migrate process command failed:");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class MigrationThread extends Thread implements Serializable {

        public void run() {
            while (true) {
                try {
                    synchronized (this) {
                        this.wait();
                        executeMigrations();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        private static final long serialVersionUID = 1L;
    }
}
