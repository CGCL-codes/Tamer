package pubweb.service;

import padrmi.PpRemote;
import padrmi.exception.PpException;
import pubweb.IntegrityException;
import pubweb.Job;
import pubweb.JobProcess;

public interface Supernode2Worker extends Any2Any, PpRemote {

    public void jobExited(Job job) throws PpException;

    public void killProcesses(Job job) throws PpException;

    public void prepareCancelExecution(JobProcess process) throws PpException, IntegrityException;

    public void cancelExecution(JobProcess process) throws PpException, IntegrityException;

    public void superviseProcessRestart(JobProcess process, int restartPid) throws PpException, IntegrityException;

    public void deadProcessRestored(JobProcess process, int restoredPid) throws PpException, IntegrityException;

    public void migrateProcess(String dstGuid, JobProcess process) throws PpException, IntegrityException;
}
