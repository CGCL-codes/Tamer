package net.sf.dvstar.transmission.spmeter;

/**
 *
 * @author dstarzhynskyi
 */
public class MemorySpeedMeter implements SpeedMetersInterface {

    private int maxValue = 0;

    private float ptsData[];

    /**
     * index of array & current len
     */
    private float freeMemory, totalMemory;

    private Runtime r = Runtime.getRuntime();

    /**
     * len of filled data
     */
    private int length = 0;

    public MemorySpeedMeter() {
    }

    @Override
    public void setMaxValue(int graphW) {
        this.maxValue = graphW;
        if (ptsData == null) {
            ptsData = new float[graphW];
            length = 0;
        } else if (ptsData.length != graphW) {
            float tmp[] = null;
            if (length < graphW) {
                tmp = new float[length];
                System.arraycopy(ptsData, 0, tmp, 0, tmp.length);
            } else {
                tmp = new float[graphW];
                System.arraycopy(ptsData, ptsData.length - tmp.length, tmp, 0, tmp.length);
                length = tmp.length - 1;
            }
            ptsData = new float[graphW];
            System.arraycopy(tmp, 0, ptsData, 0, tmp.length);
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getLength() {
        return length;
    }

    public float getCurValue(int pos) {
        if (pos <= length) {
            return ptsData[pos];
        } else {
            return 0;
        }
    }

    public void addValue() {
        freeMemory = (float) r.freeMemory();
        totalMemory = (float) r.totalMemory();
        if (length + 1 == maxValue) {
            System.arraycopy(ptsData, 1, ptsData, 0, length - 1);
            length--;
        }
        ptsData[length] = (float) ((freeMemory / totalMemory));
        if (length + 1 < maxValue) {
            length++;
        }
    }

    public String printArray() {
        String ret = "[" + maxValue + "][" + length + "]=";
        ret += "[" + (float) ptsData[0] + "]" + "[" + (float) ptsData[length - 1] + "]" + "[" + (float) ptsData[length] + "]";
        return ret;
    }

    public void clearValue() {
        ptsData = null;
    }
}
