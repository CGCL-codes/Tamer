package jmbench.tools.memory;

/**
 * Computes the memory overhead in just launching a process.
 *
 * @author Peter Abeles
 */
public class DetermineOverhead {

    MemoryConfig config;

    MemoryBenchmarkTools tool = new MemoryBenchmarkTools();

    int numTrials;

    OverheadProcess op = new OverheadProcess();

    public DetermineOverhead(MemoryConfig config, int numTrials) {
        this.numTrials = numTrials;
        this.config = config;
        tool.setVerbose(false);
        tool.sampleType = config.memorySampleType;
    }

    public long computeOverhead() {
        long total = 0;
        for (int i = 0; i < numTrials; i++) {
            System.out.print("*");
            long time = performTest();
            if (time <= 0) throw new RuntimeException("Overhead test failed!");
            total += time;
        }
        System.out.print("  ");
        return total / numTrials;
    }

    private long performTest() {
        tool.setFrozenDefaultTime(60 * 1000);
        tool.setMemory(config.memoryMinMB, config.memoryMaxMB);
        MemoryTest test = new MemoryTest();
        test.setup(null, null, op, 1, 0);
        test.setRandomSeed(config.seed);
        return tool.runTest(test);
    }
}
