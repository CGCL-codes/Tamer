package org.javatech.benchmark.jamon;

import org.javatech.benchmark.BenchmarkEngine;

public class JamonBenchmarkEngine extends BenchmarkEngine {

    public static void main(String[] args) throws Exception {
        new JamonBenchmarkEngine().runBenchmark();
    }

    @Override
    public String[] getConfigLocations() {
        return new String[] { "jamon-benchmarks.xml" };
    }
}
