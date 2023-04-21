package thirdParty.jembench;

/**
 * A benchmark where the workload is automatically distributed
 * to all available cores.
 * 
 * @author Martin Schoeberl (martin@jopdesign.com)
 *
 */
public abstract class ParallelBenchmark extends Benchmark {

    ParallelExecutor pe;

    public ParallelBenchmark() {
        pe = ParallelExecutor.getExecutor();
    }

    public String toString() {
        return "Override the name";
    }

    public int measure() {
        int start, cnt, time;
        cnt = 1;
        time = 0;
        pe.start();
        while (time < MIN_EXECUTE) {
            cnt <<= 1;
            if (cnt < 0) {
                break;
            }
            start = Util.getTimeMillis();
            for (int i = 0; i < cnt; ++i) {
                pe.executeParallel(getWorker());
            }
            time = Util.getTimeMillis() - start;
        }
        pe.stop();
        setRawResult(cnt, time);
        return getResult();
    }

    protected abstract Runnable getWorker();
}
