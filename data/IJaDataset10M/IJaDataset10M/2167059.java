package titancommon.tasks;

/**
 * @author Benedikt Köppel <bkoeppel@ee.ethz.ch>
 * @author Jonas Huber <huberjo@ee.ethz.ch>
 *
 * Merges the data coming in from a number of ports and sends the merged
 * data over the output port.
 *
 */
public class SyncMerger extends Task {

    public static final String NAME = "syncmerger";

    public static final int TASKID = 36;

    short numInputs;

    short samplingPeriod;

    short bufferSize;

    public SyncMerger() {
    }

    public SyncMerger(SyncMerger m) {
        super(m);
        numInputs = m.numInputs;
        samplingPeriod = m.samplingPeriod;
        bufferSize = m.bufferSize;
    }

    public String getName() {
        return NAME.toLowerCase();
    }

    public int getID() {
        return TASKID;
    }

    public Object clone() {
        return new SyncMerger(this);
    }

    public int getInPorts() {
        return numInputs;
    }

    public int getOutPorts() {
        return 1;
    }

    /**
     * the configuration:
     *  * first argument: number of inputs. if this is bigger than the actual inputs, the syncmerger will wait
     *      on the missing inputs and then just write zeroes there
     *  * second argument: the sampling period, in which the syncmerger should generate an output packet
     *  * third argument: the fixed output delay in milliseconds. the syncmerger generates at time X the packet for time X-outputdelay,
     *      because it has to wait for late incoming packets.
     *      TODO: the third argument should be renamed to output_delay or something. and double check, if the ESyncMerger correclty gets that parameter
     */
    public boolean setConfiguration(String[] strConfig) {
        if (strConfig == null || strConfig.length == 0) {
            System.out.println("No configuration for SyncMerger.");
            return false;
        } else if (strConfig.length > 3) {
            System.out.println("Wrong configuration for SyncMerger");
            return false;
        }
        numInputs = Short.parseShort(strConfig[0]);
        samplingPeriod = Short.parseShort(strConfig[1]);
        bufferSize = Short.parseShort(strConfig[2]);
        if (numInputs <= 0) {
            System.out.println("SyncMerger: Less than 0 inputs makes no sense.");
            return false;
        }
        if (samplingPeriod <= 0) {
            System.out.println("SyncMerger: Sampling Period < 0 makes no sense.");
            return false;
        }
        if (bufferSize <= 0) {
            System.out.println("SyncMerger: BufferSize < 0 makes no sense.");
            return false;
        }
        return true;
    }

    public short[][] getConfigBytes(int maxBytesPerMsg) {
        short[][] config = new short[1][4];
        config[0][0] = numInputs;
        config[0][1] = samplingPeriod;
        config[0][2] = (short) (bufferSize & 0xff);
        config[0][3] = (short) (bufferSize >> 8);
        return config;
    }
}
