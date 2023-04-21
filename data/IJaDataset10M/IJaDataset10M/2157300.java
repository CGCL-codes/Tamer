package pubweb.worker.re;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import padrmi.exception.PpException;
import pubweb.AbortedException;
import pubweb.IntegrityException;
import pubweb.JobProcess;
import pubweb.NotConnectedException;
import pubweb.ProcessServerException;

public interface ReServices {

    public void jobAborted(AbortedException cause);

    public void jobDiedOnError(Throwable cause);

    public void processFinished();

    public void processMigrated();

    public String isMigrationExpected();

    public String initRemoteNodeForProcess(String guid, JobProcess process, URL codebase) throws PpException, IntegrityException, MalformedURLException, NotConnectedException, ProcessServerException;

    public void migrationFailed(JobProcess process);

    public void superstepCompleted(int superstep, boolean last);

    public String getBackupDir();

    public void signBackupCopy(String name) throws PpException, IOException;

    public void fetchBackupCopy(String name, URL srcUrlPrefix) throws PpException, ClassNotFoundException, IOException;

    public void deleteOldBackups(String prefix, int upToSuperstep) throws PpException;
}
