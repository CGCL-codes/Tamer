package org.satish.benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.satish.core.LOCCount;

/**
 * @author satish
 * Created on Sep 15, 2007
 */
public class LOCBenchmark {

    private static final String path = "/home/satish/Programming/workspace-jsf/loccount/src";

    private LOCCount loccnt;

    /**
	 * 
	 */
    public LOCBenchmark() {
        super();
        loccnt = new LOCCount(path, true, false);
    }

    public void benchmarkJavaIO() throws IOException {
        int count = 10;
        List<Double> times = new ArrayList<Double>(count);
        Timer timer = new Timer();
        for (int i = 0; i < count; i++) {
            timer.reset();
            timer.start();
            loccnt.process();
            timer.end();
            times.add(timer.getTimeInMillis());
        }
        printResults(times, "benchmarkJavaIO");
    }

    private void printResults(List<Double> times, String name) {
        Collections.sort(times);
        long sum = 0;
        for (double time : times) {
            sum += time;
        }
        double avg = sum / (double) (times.size());
        System.out.println("============ " + name + " ============");
        System.out.println("Total Time : " + sum / 1000D + " secs");
        System.out.println("Max Time   : " + times.get(times.size() - 1) / 1000D + " secs");
        System.out.println("Avg Time   : " + avg / 1000D + " secs");
        System.out.println("Min Time   : " + times.get(0) / 1000D + " secs");
    }

    public static void main(String[] args) {
        LOCBenchmark benchmark = new LOCBenchmark();
        try {
            benchmark.benchmarkJavaIO();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
