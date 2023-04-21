package gov.nist.mel.simsync.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Client;
import org.restlet.data.Protocol;

/**
 *
 * @author Guillaume Radde <guillaume.radde@nist.gov>
 */
public abstract class SimulationMockBase extends Thread {

    protected final int timeStepSize;

    protected final int processingTime;

    protected final String groupName;

    protected final String memberName;

    protected final int totalsimulationTime;

    protected int localTime = 0;

    protected String baseUrl;

    protected final Client client = new Client(Protocol.HTTP);

    public SimulationMockBase(ThreadGroup threadGroup, int timeStepSize, int totalSimulationTime, int processingTime, String baseUrl, String groupName, String memberName) throws Exception {
        super(threadGroup, memberName);
        this.timeStepSize = timeStepSize;
        this.processingTime = processingTime;
        this.groupName = groupName;
        this.memberName = memberName;
        this.totalsimulationTime = totalSimulationTime;
        this.baseUrl = baseUrl;
        joinGroup();
        Logger.getLogger(SimulationMockBase.class.getName()).log(Level.INFO, memberName + " joined the group.");
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (localTime <= totalsimulationTime) {
                    if (processingTime != 0) {
                        wait(processingTime);
                    }
                    setLocalTime(localTime + timeStepSize);
                    localTime += timeStepSize;
                    Logger.getLogger(SimulationMockBase.class.getName()).log(Level.INFO, memberName + " moved on to time " + localTime);
                }
            }
            leaveGroup();
        } catch (Exception ex) {
            Logger.getLogger(SimulationMockBase.class.getName()).log(Level.INFO, null, ex);
        }
        Logger.getLogger(SimulationMockBase.class.getName()).log(Level.INFO, memberName + " exited.");
    }

    protected abstract void joinGroup() throws Exception;

    protected abstract void setLocalTime(int newLocalTime) throws Exception;

    protected abstract void leaveGroup() throws Exception;
}
