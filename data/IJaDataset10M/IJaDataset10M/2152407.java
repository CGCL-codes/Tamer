package vavi.sensor.mocap.macbook;

import vavi.sensor.mocap.Mocap;

/**
 * OpenNI NITE. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/08/24 nsano initial version <br>
 */
public class NiteMocap implements Mocap<float[][][]> {

    /** */
    private native int init();

    /** not thread safe */
    public int sense() {
        inject(data);
        return 0;
    }

    /** */
    private native void destroy();

    public NiteMocap() {
        int r = init();
        if (r != 0) {
            throw new IllegalStateException("error: " + r);
        }
    }

    protected void finalize() throws Throwable {
        destroy();
    }

    /** 使い回し not thread safe */
    private float[][][] data = new float[15][16][9];

    /** not thread safe */
    private native void inject(float[][][] data);

    private void whenNewUser(int id) {
        System.err.println("********************* whenNewUser " + id + " **********************");
    }

    /** not thread safe */
    public float[][][] get() {
        return data;
    }

    static {
        try {
            System.loadLibrary("NiteWrapper");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
