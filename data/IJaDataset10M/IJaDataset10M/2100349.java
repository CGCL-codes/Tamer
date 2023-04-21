package pulsarhunter.datatypes;

import pulsarhunter.Data;
import pulsarhunter.DataRecordType;

/**
 *
 * @author mkeith
 */
public interface BulkWritable<E extends Data.Header> extends Data<E> {

    public void write(int startPosn, byte[] data);

    public void write(int startPosn, short[] data);

    public void write(int startPosn, int[] data);

    public void write(int startPosn, long[] data);

    public void write(int startPosn, float[] data);

    public void write(int startPosn, double[] data);

    public DataRecordType getDataRecordType();
}
