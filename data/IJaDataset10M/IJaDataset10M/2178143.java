package simulator.processor.instructions;

import simulator.processor.ForwardingPair;
import simulator.processor.StageIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static simulator.processor.StageIdentifier.*;

/**
 * Holds information about instruction execution. Data is used to configure various pipeline stages
 * and to compute hazard control attributes.
 * <p>
 * Values may be changed, but changing it during execution of instruction (let's say, later than IF
 * stage), may be harmful for pipeline control and is therefore not recommended.
 * </p>
 * <p>
 * By default, all stages are passed through with latency of 1.
 * </p>
 */
public class InstructionExecutionData {

    private Map<StageIdentifier, Integer> stageLatencies = new TreeMap<StageIdentifier, Integer>();

    private int latency;

    private int creationTime;

    private int completionTime = 0;

    private int stalls = 0;

    private Map<Integer, InstructionLogEntry> log = new TreeMap<Integer, InstructionLogEntry>();

    private StageIdentifier partEX = UNSPEC;

    private StageIdentifier stage;

    /**
     * Regular constructor. Initialiazes data into defaults.
     */
    public InstructionExecutionData() {
        reset();
    }

    /**
     * Copy constructor. Copies all items including changed stage latencies.
     */
    public InstructionExecutionData(InstructionExecutionData data) {
        stageLatencies.clear();
        stageLatencies.putAll(data.stageLatencies);
        computeLatency();
        log.clear();
        log.putAll(data.log);
        creationTime = data.creationTime;
        completionTime = data.completionTime;
        partEX = data.partEX;
        stalls = data.stalls;
        stage = data.stage;
    }

    /**
     * Returns latency for the given stage (using an identifier). Returns zero if data does not
     * contain latency information for the given stage. Value of zero is used by processor to find
     * out that instruction is willing to skip such stage. Latency is taken as a hint while local
     * conditions in the processor may force to stay longer in various parts of the processor.
     * However, returned value of zero is a requirement, not a hint.
     *
     * @throws IllegalArgumentException if there is no information for the given stage.
     */
    public int getStageLatency(StageIdentifier identifier) {
        if (stageLatencies.containsKey(identifier)) {
            return stageLatencies.get(identifier);
        }
        return 0;
    }

    /**
     * Sets stage latency. There are not checks no identifier expect test for null. Therefore this
     * method may be used to add new stage into data. However, reset() method returns to five
     * standard stages with default settings.
     *
     * @throws IllegalArgumentException for negative arguments.
     */
    public void setStageLatency(StageIdentifier identifier, int latency) {
        if (identifier == null) {
            throw new NullPointerException("Cannot set null stage latency.");
        }
        if (latency < 0) {
            throw new IllegalArgumentException("Latency cannot be negative: " + latency);
        }
        stageLatencies.put(identifier, latency);
        recomputeMEMLatency();
        computeLatency();
    }

    /**
     * Returns total latency of the instruction.
     */
    public int getLatency() {
        return latency;
    }

    /**
     * Returns number of stalled cycles.
     */
    public int getStalls() {
        return stalls;
    }

    /**
     * Returns total estimated latency of the instruction. Basically, this value is equal to latency
     * summed with stalls
     */
    public int getEstimatedLatency() {
        return getLatency() + getStalls();
    }

    /**
     * Returns absolute cycle time at which it is estimated the instruction will be complete
     * (finishes WB).
     */
    public int getEstimatedCompletionTime() {
        return creationTime + getEstimatedLatency() - 1;
    }

    /**
     * Used to report the instruction has been stalled. Increments stalled cycles by one.
     */
    public void inStall() {
        stalls++;
    }

    /**
     * Adds item into execution log.
     *
     * @throws NullPointerException on null argument.
     */
    public void addLogEntry(int cycle, String entry) {
        getLogEntry(cycle).setDescription(entry);
    }

    /**
     * Adds forwarding information to the entry. Can add null pairs.
     */
    public void addLogForwarding(int cycle, ForwardingPair[] pairs) {
        getLogEntry(cycle).setForwardingData(pairs);
    }

    /**
     * Returns true if log contains forwarding data for given cycle.
     */
    public boolean containsForwardingPairs(int cycle) {
        if (log.containsKey(cycle)) {
            InstructionLogEntry entry = log.get(cycle);
            return entry.getForwardingData() != null;
        }
        return false;
    }

    /**
     * Returns forwarding pairs for the given cycle.
     */
    public ForwardingPair[] getForwardingPairs(int cycle) {
        if (log.containsKey(cycle)) {
            return log.get(cycle).getForwardingData();
        }
        return null;
    }

    /**
     * Returns execution log.
     */
    public List<String> getLog() {
        List<String> stringEntries = new ArrayList<String>();
        for (InstructionLogEntry entry : log.values()) {
            stringEntries.add(entry.getDescription());
        }
        return stringEntries;
    }

    /**
     * Sets creation time.
     *
     * @throws IllegalArgumentException for negative or zero time.
     */
    public void setCreationTime(int time) {
        if (time <= 0) {
            throw new IllegalArgumentException("Illegal time value: " + time);
        }
        this.creationTime = time;
    }

    /**
     * Returns creation time.
     */
    public int getCreationTime() {
        return creationTime;
    }

    /**
     * Returns completion time.
     */
    public int getCompletionTime() {
        return completionTime;
    }

    /**
     * Sets completion time.
     *
     * @throws IllegalArgumentException for negative or zero time.
     */
    public void setCompletionTime(int time) {
        if (time <= 0) {
            throw new IllegalArgumentException("Illegal completion time value: " + time);
        }
        this.completionTime = time;
    }

    /**
     * Sets part within EX.
     *
     * @throws NullPointerException on null type.
     */
    public void setEXPart(StageIdentifier type) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot set null type.");
        }
        this.partEX = type;
    }

    /**
     * Returns part within EX.
     */
    public StageIdentifier getEXPart() {
        return partEX;
    }

    /**
     * Sets current stage.
     */
    public void setStage(StageIdentifier type) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot set null type.");
        }
        this.stage = type;
    }

    /**
     * Returns current stage.
     */
    public StageIdentifier getStage() {
        return stage;
    }

    /**
     * Resets execution data into defaults.
     */
    public void reset() {
        stageLatencies.clear();
        stageLatencies.put(STAGE_IF, 1);
        stageLatencies.put(STAGE_ID, 1);
        stageLatencies.put(STAGE_EX, 1);
        stageLatencies.put(STAGE_MEM, 1);
        stageLatencies.put(STAGE_WB, 1);
        computeLatency();
    }

    /**
     * Computes total latency of the instruction.
     */
    protected void computeLatency() {
        latency = 0;
        for (int stageLatency : stageLatencies.values()) {
            latency = latency + stageLatency;
        }
    }

    /**
     * Recalculates MEM stage latency. Check if EX == 1 and MEM == 0 which is illegal. If case, MEM
     * is set to 1.
     */
    protected void recomputeMEMLatency() {
        int exLatency = getStageLatency(STAGE_EX);
        int memLatency = getStageLatency(STAGE_MEM);
        if ((exLatency == 1) && (memLatency == 0)) {
            setStageLatency(STAGE_MEM, 1);
        }
    }

    /**
     * Returns log entry at the given cycle. If there is currently no log entry, it is created.
     *
     * @throws IllegalArgumentException for zero or negative cycle.
     */
    public InstructionLogEntry getLogEntry(int cycle) {
        if (cycle <= 0) {
            throw new IllegalArgumentException("Illegal cycle: " + cycle);
        }
        InstructionLogEntry entry;
        if (log.containsKey(cycle)) {
            entry = log.get(cycle);
        } else {
            entry = new InstructionLogEntry();
            log.put(cycle, entry);
        }
        return entry;
    }
}
