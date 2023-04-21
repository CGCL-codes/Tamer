package uk.ac.ed.rapid.jobsubmission.jobmanager.bes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import uk.ac.ed.rapid.data.filesystem.AbstractFileSystem;
import uk.ac.ed.rapid.data.filesystem.FileSystemConnector;
import uk.ac.ed.rapid.exception.RapidException;
import uk.ac.ed.rapid.job.Job;

/**
 * 
 * @author jos
 */
public class ForkPlugin extends BESPlugin {

    ForkProperties properties;

    Map<String, ForkThread> threadMap = new HashMap<String, ForkThread>();

    public ForkPlugin(Job job, AbstractFileSystem fileSystem, ForkProperties properties) {
        super(job, fileSystem);
        this.properties = properties;
    }

    protected void updateStatus(int subJob) throws RapidException {
    }

    @Override
    public String toString() {
        return "FORK " + super.toString();
    }

    public String doSubmit(FileSystemConnector connector, int subJob) {
        ForkThread newThread = new ForkThread(this.getJobData(), this.getJob(), subJob, this.getFileSystem(), connector, this.properties);
        String uuid = UUID.randomUUID().toString();
        this.threadMap.put(uuid, newThread);
        new Thread(newThread).start();
        return uuid;
    }

    public void haltJob() throws Exception {
    }

    @Override
    protected void doCleanUp() throws RapidException {
        Set<String> uuidSet = this.threadMap.keySet();
        for (String uuid : uuidSet) this.threadMap.get(uuid).doCleanUp();
    }
}
