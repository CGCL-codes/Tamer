package gnu.trove.benchmark.colt;

import cern.colt.map.OpenIntObjectHashMap;
import gnu.trove.benchmark.Benchmark;
import gnu.trove.benchmark.BenchmarkRunner;

/**
 *
 */
public class ColtHashMapPut implements Benchmark {

    private OpenIntObjectHashMap map;

    public void setUp() {
        if (map != null) return;
        map = new OpenIntObjectHashMap();
        map.ensureCapacity(BenchmarkRunner.INTEGERS.length);
    }

    public void tearDown() {
        map.clear();
    }

    public String getName() {
        return "Colt HashMap Put";
    }

    public void run() {
        for (Integer i : BenchmarkRunner.INTEGERS) {
            map.put(i.intValue(), i);
        }
    }
}
