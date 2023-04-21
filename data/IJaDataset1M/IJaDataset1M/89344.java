package interpreter;

import java.util.HashMap;

public class Task2and3Data {

    public final long interpreterStep;

    public final long[] jA;

    public final long[] jC;

    public final long[] jR;

    public final double[] aSS;

    public final double[] busies;

    public final double[] meanArrivalTime;

    public final double[] utili;

    public final int steps;

    public final double[] serviceTime;

    public final long[] thinkTime;

    public final double[] averageActiveJobs;

    public long sumOfInterArrivalTime = 0;

    public long lastArrival = 0;

    public long arrivalCount = 0;

    public double[][] server_throughput;

    public double[][] server_mean_service_rate;

    public double[][] server_utilization;

    private HashMap<String, Integer> host2id = new HashMap<String, Integer>();

    private int cur_id = 0;

    public int host2id(String host) {
        if (!host2id.containsKey(host)) {
            host2id.put(host, cur_id);
            cur_id++;
        }
        return host2id.get(host);
    }

    public Task2and3Data(long _interpStep, int _steps) {
        interpreterStep = _interpStep;
        steps = _steps;
        jA = new long[steps];
        jC = new long[steps];
        jR = new long[steps];
        aSS = new double[steps];
        busies = new double[steps];
        meanArrivalTime = new double[steps];
        utili = new double[steps];
        serviceTime = new double[steps];
        thinkTime = new long[steps];
        averageActiveJobs = new double[steps];
        server_throughput = new double[3][steps];
        server_mean_service_rate = new double[3][steps];
        server_utilization = new double[3][steps];
    }

    public void computeMeanArrivalTime() {
        for (int i = 0; i < steps; i++) {
            meanArrivalTime[i] = 1.0 / jA[i];
        }
    }

    public void computeUtilization() {
        for (int i = 0; i < steps; i++) {
            utili[i] = busies[i] / (double) interpreterStep;
        }
    }
}
