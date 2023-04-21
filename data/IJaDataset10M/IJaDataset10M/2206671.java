package org.javatech.benchmark.log4j;

import org.javatech.benchmark.BenchmarkEngine;

public class Log4JBenchmarkEngine extends BenchmarkEngine {

    public static void main(String[] args) throws Exception {
        new Log4JBenchmarkEngine().runBenchmark();
    }

    @Override
    public String[] getConfigLocations() {
        return new String[] { "log4j-benchmarks.xml" };
    }
}
