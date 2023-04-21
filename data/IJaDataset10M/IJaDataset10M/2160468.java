package jcfs.benchmarks;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import jcfs.core.fs.RFile;
import jcfs.core.fs.RFileOutputStream;
import jcfs.core.fs.WriteMode;

/**
 * bench
 * @author enrico
 */
public class SimpleWriteBench extends BenchCommons implements Bench {

    private int size;

    private BenchResultWriter summary;

    private boolean transact;

    public SimpleWriteBench(BenchResultWriter summary, int size, boolean transact) {
        this.summary = summary;
        this.size = size;
        this.transact = transact;
    }

    public BenchResult run() {
        try {
            byte[] raw = genArray(size);
            long _start = System.currentTimeMillis();
            for (int round = 0; round < ROUNDS; round++) {
                WriteMode mode = WriteMode.BESTEFFORT;
                String name = "testSimpleWriteBench" + round + "_" + testId + ".txt";
                if (transact) {
                    mode = WriteMode.TRANSACTED;
                    name = "testTransactedSimpleWriteBench" + round + "_" + testId + ".txt";
                }
                RFile file = new RFile(name);
                OutputStream out = new RFileOutputStream(file, mode, false, 1);
                out = new BufferedOutputStream(out);
                out.write(raw);
                out.close();
            }
            long _stop = System.currentTimeMillis();
            return new BenchResult(this.getClass().getSimpleName(), ROUNDS, _stop - _start, true, " transacted=" + transact);
        } catch (Throwable t) {
            return new BenchResult(this.getClass().getSimpleName(), ROUNDS, 0, false, "");
        }
    }
}
