package lelouet.datacenter.simulation.vms;

import java.util.ArrayList;
import java.util.List;
import lelouet.datacenter.simulation.Event;
import lelouet.datacenter.simulation.events.VMSpecChange;
import lelouet.datacenter.simulation.vms.webserver.LongIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a VM that receives request at specific flow rate.<br />
 * Memory need is constant, CPU needs is constant when there is no request to
 * proceed and constant-infinite when a request is not yet finished.<br />
 * Internally, has a list of time for arriving requests, associated to their
 * number of instructions.
 * 
 * @author guillaume
 * 
 */
public class WebServer extends AVM {

    private static final Logger mlogger = LoggerFactory.getLogger(WebServer.class);

    final LongIterator delayIterator;

    final LongIterator instructionsIterator;

    /**
	 * the constant Million Instruction Per Second this require when no request
	 * is being proceeded
	 */
    final long lowMIPS;

    /**
	 * the constant Million Instructions Per Second this requires when handling
	 * one or more request.
	 */
    final long highMIPS;

    /** the next slot time a request will be submitted at the end */
    long nextRequestArrival;

    /** number of instructions we still need to do */
    long remainingMillionInstructions = 0;

    /**
	 * constructs a {@link WebServer} with specific job arrival rate(delay in
	 * ms) and job instructions quantity (in million instructions)
	 * 
	 * @param id
	 *            the id of the webserver
	 * @param time
	 *            the time of creation of the VM
	 * @param delayIterator
	 *            the sequence of delays between two request arrival.<br />
	 *            the delays are in ms.<br />
	 *            This should not depend on another Datacenter's element,
	 *            because the value may be stored and used much after it was
	 *            computed.
	 * @param instructionsIterator
	 *            the sequence of numbers of Million instructions the requests
	 *            need to do to complete.<br />
	 *            This should not depend on another Datacenter's element,
	 *            because the value may be stored and used much after it was
	 *            computed.
	 * @param lowMIPS
	 *            the number of max MIPS used when no request is being handled
	 * @param highMIPS
	 *            the number of max MIPs used when handling a request.
	 * @param memUsage
	 *            the size, in Mo, of memory this uses.
	 */
    public WebServer(String id, long time, LongIterator delayIterator, LongIterator instructionsIterator, long lowMIPS, long highMIPS, long memUsage) {
        super(id, time);
        this.delayIterator = delayIterator;
        this.instructionsIterator = instructionsIterator;
        this.lowMIPS = lowMIPS;
        this.highMIPS = highMIPS;
        setMemNeeds(memUsage);
        setMemUsed(memUsage);
        nextRequestArrival = time;
        nextRequest();
    }

    public WebServer(String id, long time, LongIterator delayIterator, LongIterator instructionsIterator, long minMIPS, long memUsage) {
        this(id, time, delayIterator, instructionsIterator, minMIPS, Long.MAX_VALUE, memUsage);
    }

    public WebServer(String id, long time, long delayms, long sizeMI, long memUsage) {
        this(id, time, new LongIterator.Constant(delayms), new LongIterator.Constant(sizeMI), 0, Long.MAX_VALUE, memUsage);
    }

    @Override
    public long getMaxCPU() {
        if (remainingMillionInstructions > 0) {
            return Long.MAX_VALUE;
        } else {
            return lowMIPS;
        }
    }

    /**
	 * computes, after last computed request arrival, when the next will
	 * happen<br /< this can lead to the next arrival time being before
	 * {@link #getTime()}, and this case should be considered, in case a VM is
	 * put to sleep and awakened after a long time
	 */
    protected void nextRequest() {
        long futureRequestArrival = nextRequestArrival + delayIterator.nextLength(nextRequestArrival);
        mlogger.debug("time {}, last arrival {}, next arrival {}", new Object[] { getTime(), nextRequestArrival, futureRequestArrival });
        nextRequestArrival = futureRequestArrival;
    }

    @Override
    public long nextEventTime() {
        if (remainingMillionInstructions <= 0) {
            mlogger.debug("{}, time {} : no instruction, next arrival at {}", new Object[] { getId(), getTime(), nextRequestArrival });
            return nextRequestArrival;
        }
        mlogger.debug("{}, time {} : {} million instructions, next arrival at {}", new Object[] { getId(), getTime(), remainingMillionInstructions, nextRequestArrival });
        long MIPS = getAllocatedCPU();
        long requestEnd = getTime() + (long) Math.ceil((double) remainingMillionInstructions / MIPS);
        return Math.max(0, Math.min(requestEnd, nextRequestArrival));
    }

    @Override
    public List<Event> work(long nextStop) {
        long MIPS = getAllocatedCPU();
        List<Event> ret = new ArrayList<Event>();
        while (getTime() < nextStop) {
            long nextEventTime = nextEventTime();
            if (nextStop < nextEventTime) {
                nextEventTime = nextStop;
            }
            long instructionsDone = (MIPS - lowMIPS) * (nextEventTime - getTime());
            long cpuUsage = getMaxCPU();
            if (remainingMillionInstructions > 0) {
                remainingMillionInstructions -= instructionsDone;
                if (remainingMillionInstructions <= 0) {
                    remainingMillionInstructions = 0;
                }
            }
            setTime(nextEventTime);
            if (getTime() == nextRequestArrival) {
                remainingMillionInstructions += instructionsIterator.nextLength(nextStop);
                nextRequest();
            }
            if (getMaxCPU() != cpuUsage) {
                ret.add(new VMSpecChange(this, getTime(), new PhysicalRequirementsImpl(cpuUsage, getMemNeeds(), getMemUsed()), new PhysicalRequirementsImpl(getMaxCPU(), getMemNeeds(), getMemUsed())));
            }
        }
        return ret;
    }

    /**
	 * do nothing : a webserver requires a fixed amount of memory, any more
	 * memory is discarded
	 */
    @Override
    public void allocateMemory(long mo) {
    }

    @Override
    public void reconfigure() {
    }
}
