package jcfs.benchmarks;

import jcfs.core.fs.RFile;
import jcfs.core.fs.RFileOutputStream;
import jcfs.core.fs.WriteMode;

/**
 * bench
 * @author enrico
 */
public class ReplicatedWriteBench extends BenchCommons implements Bench {

    private int size;

    private int minpeers;

    private BenchResultWriter summary;

    public ReplicatedWriteBench(BenchResultWriter summary, int size, int minpeers) {
        this.summary = summary;
        this.size = size;
        this.minpeers = minpeers;
    }

    public BenchResult run() {
        try {
            byte[] raw = genArray(size);
            long _start = System.currentTimeMillis();
            for (int round = 0; round < ROUNDS; round++) {
                RFile file = new RFile("testReplicatedWriteBench" + round + "_" + testId + ".txt");
                RFileOutputStream out = new RFileOutputStream(file, WriteMode.BESTEFFORT, false, minpeers);
                out.write(raw);
                out.close();
            }
            long _stop = System.currentTimeMillis();
            return new BenchResult(this.getClass().getSimpleName(), ROUNDS, _stop - _start, true, " minpeers=" + minpeers);
        } catch (Throwable t) {
            return new BenchResult(this.getClass().getSimpleName(), ROUNDS, 0, false, "");
        }
    }
}
