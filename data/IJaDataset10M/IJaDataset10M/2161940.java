package titancommon.node.tasks;

import titancommon.node.TitanTask;
import titancommon.node.DataPacket;
import titancommon.tasks.Variance;

/**
 * calculates the variance of an incoming datapacket. the calculation is only done
 * with the first two bytes in the short array.
 * 
 * @author Jeremy Constantin <jeremyc@student.ethz.ch>
 */
public class EVariance extends Variance implements ExecutableTitanTask {

    private TitanTask tTask;

    public void setTitanTask(TitanTask tsk) {
        tTask = tsk;
    }

    private int winSize;

    private int winShift;

    private int resultShift = 0;

    private short dataWindow[];

    private int idx;

    public boolean setExecParameters(short[] param) {
        switch(param.length) {
            case 3:
                resultShift = param[2];
            case 2:
                winSize = param[0];
                winShift = param[1];
                if ((winShift <= winSize) && (winSize > 0) && (winShift > 0) && (resultShift >= 0)) break;
            default:
                tTask.errSource = tTask.getRunID();
                tTask.errType = 4;
                return false;
        }
        return true;
    }

    public void init() {
        idx = 0;
        dataWindow = new short[winSize];
    }

    public void inDataHandler(int port, DataPacket data) {
        if (port != 0) {
            System.err.println("incoming data on port > 0");
            return;
        }
        if (data.sdata.length % 2 != 0) {
            System.err.println("uneven number of bytes in 16bit array");
            return;
        }
        for (int i = 0; i < data.sdata.length; i += 2) {
            addValue((short) (data.sdata[i] + (data.sdata[i + 1] << 8)));
        }
    }

    private void addValue(short val) {
        dataWindow[idx++] = val;
        if (idx == winSize) {
            int var = getVariance();
            var >>= resultShift;
            var = ((var >= Short.MAX_VALUE) ? Short.MAX_VALUE : var);
            short[] data = new short[2];
            data[0] = (short) (var & 0xFF);
            data[1] = (short) ((var >> 8) & 0xFF);
            if (resultShift == 3) {
                System.out.println("Variance is " + (short) (data[0] + (data[1] << 8)));
            }
            tTask.send(0, new DataPacket(data));
            idx = winSize - winShift;
            for (int i = 0; i < idx; i++) {
                dataWindow[i] = dataWindow[i + winShift];
            }
        }
    }

    private int getVariance() {
        int mean = getSum() / winSize;
        int sum = 0;
        int val = 0;
        for (int i = 0; i < winSize; i++) {
            val = dataWindow[i] - mean;
            sum += val * val;
        }
        return sum / (winSize - 1);
    }

    private int getSum() {
        int sum = 0;
        for (int i = 0; i < winSize; i++) {
            sum += dataWindow[i];
        }
        return sum;
    }

    private int getSumSqr() {
        int sum = 0;
        for (int i = 0; i < winSize; i++) {
            sum += ((int) dataWindow[i]) * ((int) dataWindow[i]);
        }
        return sum;
    }
}
