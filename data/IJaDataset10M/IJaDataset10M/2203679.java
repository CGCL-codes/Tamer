package javax.jmdns.impl.tasks.state;

import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSState;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Logger;

/** The Canceler sends two announces with TTL=0 for the specified services. */
public class Canceler extends DNSStateTask {

    static Logger logger = Logger.getLogger(Canceler.class.getName());

    public Canceler(JmDNSImpl jmDNSImpl) {
        super(jmDNSImpl, 0);
        this.setTaskState(DNSState.CANCELING_1);
        this.associate(DNSState.CANCELING_1);
    }

    @Override
    public String getName() {
        return "Canceler(" + (this.getDns() != null ? this.getDns().getName() : "") + ")";
    }

    @Override
    public String toString() {
        return super.toString() + " state: " + this.getTaskState();
    }

    @Override
    public void start(Timer timer) {
        timer.schedule(this, 0, DNSConstants.ANNOUNCE_WAIT_INTERVAL);
    }

    @Override
    public boolean cancel() {
        this.removeAssociation();
        return super.cancel();
    }

    @Override
    public String getTaskDescription() {
        return "canceling";
    }

    @Override
    protected boolean checkRunCondition() {
        return true;
    }

    @Override
    protected DNSOutgoing buildOutgoingForDNS(DNSOutgoing out) throws IOException {
        DNSOutgoing newOut = out;
        for (DNSRecord answer : this.getDns().getLocalHost().answers(DNSRecordClass.UNIQUE, this.getTTL())) {
            newOut = this.addAnswer(newOut, null, answer);
        }
        return newOut;
    }

    @Override
    protected DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl info, DNSOutgoing out) throws IOException {
        DNSOutgoing newOut = out;
        for (DNSRecord answer : info.answers(DNSRecordClass.UNIQUE, this.getTTL(), this.getDns().getLocalHost())) {
            newOut = this.addAnswer(newOut, null, answer);
        }
        return newOut;
    }

    @Override
    protected void recoverTask(Throwable e) {
        this.getDns().recover();
    }

    @Override
    protected void advanceTask() {
        this.setTaskState(this.getTaskState().advance());
        if (!this.getTaskState().isCanceling()) {
            cancel();
        }
    }
}
