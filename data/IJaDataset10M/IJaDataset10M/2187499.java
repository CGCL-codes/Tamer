package it.crs4.seal.demux;

import it.crs4.seal.common.SequenceId;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sort sequence ids so that read 2 comes first (the index read),
 * then read 1 and read 3.
 */
public class TwoOneThreeSortComparator implements RawComparator<SequenceId> {

    private static final Log LOG = LogFactory.getLog(TwoOneThreeSortComparator.class);

    private static final int ByteSize = Byte.SIZE / 8;

    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        if (ByteSize != 1) throw new RuntimeException("Byte size != 1 (it's " + ByteSize + "). Make sure this code still works!");
        int sizeVint1 = WritableUtils.decodeVIntSize(b1[s1]);
        int sizeVint2 = WritableUtils.decodeVIntSize(b2[s2]);
        int locationCmp = WritableComparator.compareBytes(b1, s1 + sizeVint1, l1 - sizeVint1 - ByteSize, b2, s2 + sizeVint2, l2 - sizeVint2 - ByteSize);
        if (locationCmp == 0) {
            byte r1 = b1[s1 + l1 - 1];
            byte r2 = b2[s2 + l2 - 1];
            int retval;
            if (r1 == r2) retval = 0; else if (r1 == 2) retval = -1; else if (r2 == 2) retval = 1; else retval = (r1 < r2) ? -1 : 1;
            return retval;
        } else return locationCmp;
    }

    @Override
    public int compare(SequenceId s1, SequenceId s2) {
        int locationCmp = s1.getLocation().compareTo(s2.getLocation());
        if (locationCmp == 0) {
            int r1 = s1.getRead();
            int r2 = s2.getRead();
            if (r1 == r2) return 0; else if (r1 == 2) return -1; else if (r2 == 2) return 1; else return (r1 < r2) ? -1 : 1;
        } else return locationCmp;
    }
}
