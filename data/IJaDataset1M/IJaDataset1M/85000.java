package ch.unizh.ini.jaer.projects.opticalflow;

import java.io.*;
import java.util.Random;

/**
 * Packs data returned from optical flow sensor.
 * 
 * some changes by andstein
 * <ul>
 * <li>added a second global-x/y for comparison between software/hardware
 *     calculated values</li>
 * <li>added boolean filledIn that can be used by child classes to prevent
 *     MotionViewer from filling in motion values a 2nd time</li>
 * </ul>
 * 
 * @author tobi
 */
public abstract class MotionData implements Cloneable {

    /** Bit definitions for what this structure holds.
     User sets these bits to tell the hardware interface what data should be acquired for this buffer
     */
    public static final int GLOBAL_X = 0x01, GLOBAL_Y = 0x02, PHOTO = 0x04, UX = 0x08, UY = 0x10, BIT5 = 0x20, BIT6 = 0x40, BIT7 = 0x80;

    /** the resolution of the SiLabs_8051F320 ADC */
    public static final int ADC_RESOLUTION_BITS = 10;

    protected static int NUM_PASTMOTIONDATA = 0;

    private int sequenceNumber;

    /** The time in System.currentTimeMillis() that this data was captured */
    private long timeCapturedMs = 0;

    protected float[][][] rawDataPixel;

    protected float[] rawDataGlobal;

    protected boolean filledIn = false;

    protected float[][] ph;

    protected float[][] ux;

    protected float[][] uy;

    protected float globalX;

    protected float globalY;

    protected float minph, maxph, minux, maxux, minuy, maxuy;

    protected float globalX2;

    protected float globalY2;

    public Chip2DMotion chip;

    protected MotionData[] pastMotionData;

    /** Bits set in contents show what data has actually be acquired in this buffer.
     @see #GLOBAL_Y
     @see #GLOBAL_X etc
     */
    private int contents = 0;

    protected static Random r = new Random();

    /** Constructor to be called by non abstract subclasses */
    public MotionData(Chip2DMotion chip) {
        globalX = 0;
        globalY = 0;
        globalX2 = 0;
        globalY2 = 0;
        this.chip = chip;
        contents = chip.getCaptureMode();
        rawDataPixel = new float[this.getNumLocalChannels()][chip.getSizeX()][chip.getSizeY()];
        rawDataGlobal = new float[getNumGlobalChannels()];
        ph = new float[chip.getSizeX()][chip.getSizeY()];
        ux = new float[chip.getSizeX()][chip.getSizeY()];
        uy = new float[chip.getSizeX()][chip.getSizeY()];
    }

    protected abstract void fillPh();

    protected abstract void fillUxUy();

    protected abstract void fillMinMax();

    protected abstract void fillAdditional();

    protected abstract void updateContents();

    public final void collectMotionInfo() {
        if (filledIn == false) {
            fillPh();
            fillUxUy();
            fillMinMax();
            fillAdditional();
            updateContents();
            setFilledIn(true);
        }
    }

    /** @return total number of independent data */
    public final int getLength() {
        return 3 * (1 + chip.getSizeX() * chip.getSizeY());
    }

    /** returns the sequence number of this data
     @return the sequence number, starting at 0 with the first data captured by the reader
     */
    public final int getSequenceNumber() {
        return sequenceNumber;
    }

    /** returns the contents field, whose bits show what data is valid in this buffer */
    public final int getContents() {
        return contents;
    }

    /** gets the system time that the data was captured
     @return the time in ms as returned by System.currentTimeMillis()
     */
    public long getTimeCapturedMs() {
        return timeCapturedMs;
    }

    public final float[][][] getRawDataPixel() {
        return this.rawDataPixel;
    }

    public final float[] getRawDataGlobal() {
        return this.rawDataGlobal;
    }

    public final float getGlobalX() {
        return globalX;
    }

    public final float getGlobalY() {
        return globalY;
    }

    public final float[][] getPh() {
        return ph;
    }

    public final float[][] getUy() {
        return uy;
    }

    public final float[][] getUx() {
        return ux;
    }

    public float getMaxuy() {
        return maxuy;
    }

    public float getMinuy() {
        return minuy;
    }

    public float getMaxux() {
        return maxux;
    }

    public float getMinux() {
        return minux;
    }

    public float getMaxph() {
        return maxph;
    }

    public float getMinph() {
        return minph;
    }

    public MotionData getLastMotionData() {
        return this.pastMotionData[0];
    }

    public MotionData[] getPastMotionData() {
        return this.pastMotionData;
    }

    public int getNumGlobalChannels() {
        int numGlob = contents & GLOBAL_X + (contents & GLOBAL_Y) >> 1;
        return numGlob;
    }

    public int getNumLocalChannels() {
        int numLoc = 0;
        int f = contents & 0xFC;
        for (int i = 0; i < 8; i++) {
            numLoc += (f >> i) & 0x01;
        }
        return numLoc;
    }

    public boolean hasGlobalX() {
        return (contents & GLOBAL_X) != 0;
    }

    public boolean hasGlobalY() {
        return (contents & GLOBAL_Y) != 0;
    }

    public boolean hasPhoto() {
        return (contents & PHOTO) != 0;
    }

    public boolean hasLocalX() {
        return (contents & UX) != 0;
    }

    public boolean hasLocalY() {
        return (contents & UY) != 0;
    }

    /** set Methods*/
    public final void setRawDataGlobal(float[] data) {
        this.rawDataGlobal = data;
    }

    public final void setRawDataPixel(float[][][] data) {
        this.rawDataPixel = data;
    }

    public final void setSequenceNumber(int sequenceNumber) {
        if (sequenceNumber != this.sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            setFilledIn(false);
        }
    }

    public final void setGlobalX(float globalX) {
        this.globalX = globalX;
    }

    public final void setGlobalY(float globalY) {
        this.globalY = globalY;
    }

    public final void setPh(float[][] ph) {
        this.ph = ph;
    }

    public final void setUx(float[][] ux) {
        this.ux = ux;
    }

    public final void setUy(float[][] uy) {
        this.uy = uy;
    }

    public final void setContents(int contents) {
        this.contents = contents;
    }

    public void setMinph(float minph) {
        this.minph = minph;
    }

    public void setMaxph(float maxph) {
        this.maxph = maxph;
    }

    public void setMinux(float minux) {
        this.minux = minux;
    }

    public void setMaxus(float maxux) {
        this.maxux = maxux;
    }

    public void setMinuy(float minuy) {
        this.minuy = minuy;
    }

    public void setMaxuy(float maxuy) {
        this.maxuy = maxuy;
    }

    public void setTimeCapturedMs(long timeCapturedMs) {
        this.timeCapturedMs = timeCapturedMs;
    }

    public float getGlobalX2() {
        return globalX2;
    }

    public float getGlobalY2() {
        return globalY2;
    }

    public void setGlobalX2(float globalX2) {
        this.globalX2 = globalX2;
    }

    public void setGlobalY2(float globalY2) {
        this.globalY2 = globalY2;
    }

    public void setLastMotionData(MotionData lastData) {
        if (this.pastMotionData == null) {
            this.pastMotionData = new MotionData[NUM_PASTMOTIONDATA];
        }
        for (int i = NUM_PASTMOTIONDATA; i > 1; i--) {
            this.pastMotionData[i - 1] = this.pastMotionData[i - 2];
        }
        this.pastMotionData[0] = lastData;
    }

    public void setPastMotionData(MotionData[] pastData) {
        this.pastMotionData = pastData;
    }

    public float[][] extractRawChannel(int channelNumber) {
        int maxX = this.chip.NUM_COLUMNS;
        int maxY = this.chip.NUM_ROWS;
        float[][] channelData = new float[maxX][maxY];
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                channelData[y][x] = this.rawDataPixel[channelNumber][y][x];
            }
        }
        return channelData;
    }

    public void setFilledIn(boolean filledIn) {
        this.filledIn = filledIn;
    }

    public float[][] randomizeArray(float[][] f, float min, float max) {
        for (int i = 0; i < f.length; i++) {
            float[] g = f[i];
            for (int j = 0; j < g.length; j++) {
                g[j] = min + r.nextFloat() * (max - min);
            }
            f[i] = g;
        }
        return f;
    }

    public MotionData clone() {
        try {
            return (MotionData) super.clone();
        } catch (CloneNotSupportedException cnse) {
            return null;
        }
    }

    public String toString() {
        return "MotionData sequenceNumber=" + sequenceNumber + " timeCapturedMs=" + timeCapturedMs;
    }

    /** Implements the Externalizable writer in conjuction with the reader. 
     Each MotionData object is written as follows:
     <br>
     
        out.writeInt(contents);<br>
        out.writeInt(sequenceNumber);<br>
        out.writeLong(timeCapturedMs);<br>
        out.writeFloat(globalX);<br>
        out.writeFloat(globalY);<br>
        write2DArray(out,ph);<br>
        write2DArray(out,ux);<br>
        write2DArray(out,uy);<br>
        write2DArray(out,rawDataPixel[chan0]<br>
//        .
        .
        .
        write2DArray(out,rawDataPixel[chanN]<br>
     @param out the output
     */
    public void write(DataOutput out) throws IOException {
        contents = chip.getCaptureMode();
        out.writeInt(contents);
        out.writeInt(sequenceNumber);
        out.writeLong(timeCapturedMs);
        out.writeFloat(globalX);
        out.writeFloat(globalY);
        write2DArray(out, ph);
        this.fillUxUy();
        write2DArray(out, ux);
        write2DArray(out, uy);
        for (int i = 0; i < getNumLocalChannels(); i++) {
            write2DArray(out, rawDataPixel[i]);
        }
    }

    /** Implements the reader half of the Externalizable interface
     @param in the ObjectInput interface
     */
    public void read(DataInput in) throws IOException {
        contents = in.readInt();
        sequenceNumber = in.readInt();
        timeCapturedMs = in.readLong();
        globalX = in.readFloat();
        globalY = in.readFloat();
        read2DArray(in, ph);
        read2DArray(in, ux);
        read2DArray(in, uy);
        rawDataPixel = new float[getNumLocalChannels()][chip.NUM_ROWS][chip.NUM_COLUMNS];
        for (int i = 0; i < getNumLocalChannels(); i++) {
            read2DArray(in, rawDataPixel[i]);
        }
    }

    private void write2DArray(DataOutput out, float[][] f) throws IOException {
        for (int i = 0; i < f.length; i++) {
            float[] g = f[i];
            for (int j = 0; j < g.length; j++) {
                out.writeFloat(g[j]);
            }
        }
    }

    private void read2DArray(DataInput in, float[][] f) throws IOException {
        for (int i = 0; i < f.length; i++) {
            float[] g = f[i];
            for (int j = 0; j < g.length; j++) {
                g[j] = in.readFloat();
            }
        }
    }

    /** The serialized size in bytes of a MotionData instance */
    public int getLoggedObjectSize() {
        int size = 4 + 4 + 8 + 4 * 2 + 4 * 3 * (chip.getSizeX() * chip.getSizeY()) + 4 * getNumLocalChannels() * (chip.getSizeX() * chip.getSizeY());
        return size;
    }
}
