package mil.army.usace.ehlschlaeger.rgik.core;

import java.util.Date;
import java.util.Random;
import mil.army.usace.ehlschlaeger.rgik.util.BubbleSort;

/**
 *  Copyright Charles R. Ehlschlaeger,
 *  work: 309-298-1841, fax: 309-298-3003,
 *	<http://faculty.wiu.edu/CR-Ehlschlaeger2/>
 *  This software is freely usable for research and educational purposes. Contact C. R. Ehlschlaeger
 *  for permission for other purposes.
 *  Use of this software requires appropriate citation in all published and unpublished documentation.
 */
public class NoiseSet extends RGIS {

    private double[] temperatures, temperatureTimes;

    private Sensor[] sensors;

    private double[][] sensorDistances;

    private int[] timeOrderSensor, timeOrderEvent;

    private double mTolerance, mDistFromSensors, mTimeDiffercial;

    private NoiseEvent[] possibleSolutions;

    private double[] aveError;

    private double[][] sensorError;

    private long solveRunTime;

    public long getSolveRunTime() {
        return solveRunTime;
    }

    public NoiseEvent[] getPossibleSolutions() {
        return possibleSolutions;
    }

    public double[] getAverageErrorOfSolutions() {
        return aveError;
    }

    public double[][] getSensorErrorOfSolutions() {
        return sensorError;
    }

    public void setMaximumDistanceFromSensors(double maximumDistance) {
        mDistFromSensors = maximumDistance;
    }

    public void setMaximumTimeDifferencial(double maximumTime) {
        mTimeDiffercial = maximumTime;
    }

    public double getTemperatureAtTime(double time) {
        double t = temperatures[0] * (temperatureTimes[1] - time) / (temperatureTimes[1] - temperatureTimes[0]) + temperatures[1] * (time - temperatureTimes[0]) / (temperatureTimes[1] - temperatureTimes[0]);
        return t;
    }

    public double getSpeedOfSoundAtTime(double time) {
        double t = getTemperatureAtTime(time);
        return (331.4 + 0.6 * t);
    }

    public NoiseSet(Sensor[] sensors, double[] temperatures, double[] temperatureTimes, double maximumToleranceInMeters) {
        super();
        assert sensors != null;
        assert temperatures != null;
        assert temperatureTimes != null;
        assert temperatureTimes.length == temperatures.length;
        mDistFromSensors = Double.POSITIVE_INFINITY;
        mTimeDiffercial = Double.POSITIVE_INFINITY;
        mTolerance = maximumToleranceInMeters;
        this.sensors = sensors;
        this.temperatures = temperatures;
        this.temperatureTimes = temperatureTimes;
    }

    public int solve() {
        Date startTime = new Date();
        int noiseCount = 0;
        for (int s = 0; s < sensors.length; s++) {
            noiseCount += sensors[s].getNumberEvents();
        }
        timeOrderSensor = new int[noiseCount];
        timeOrderEvent = new int[noiseCount];
        int[] checkList = new int[sensors.length];
        int noisesDone = 0;
        int earliestSensor = -1;
        int earliestEvent = -1;
        while (noisesDone < noiseCount) {
            earliestSensor = -1;
            earliestEvent = -1;
            double earliestTime = Double.POSITIVE_INFINITY;
            for (int s = 0; s < sensors.length; s++) {
                if (checkList[s] < sensors[s].getNumberEvents()) {
                    if (sensors[s].getEvent(checkList[s]) < earliestTime) {
                        earliestTime = sensors[s].getEvent(checkList[s]);
                        earliestSensor = s;
                        earliestEvent = checkList[s];
                    }
                }
            }
            timeOrderSensor[noisesDone] = earliestSensor;
            timeOrderEvent[noisesDone] = earliestEvent;
            checkList[earliestSensor]++;
            noisesDone++;
        }
        sensorDistances = new double[sensors.length][sensors.length];
        for (int s = 0; s < sensors.length; s++) {
            for (int t = s; t < sensors.length; t++) {
                sensorDistances[s][t] = sensors[s].getDistance(sensors[t]);
                sensorDistances[t][s] = sensorDistances[s][t];
            }
        }
        int solutionSize = 20;
        int solutions = 0;
        possibleSolutions = new NoiseEvent[20];
        aveError = new double[20];
        sensorError = new double[20][];
        int[] lowPossibleNoise = new int[sensors.length];
        int[] hihPossibleNoise = new int[sensors.length];
        for (int n = 0; n < noisesDone; n++) {
            int homeSensor = timeOrderSensor[n];
            lowPossibleNoise[homeSensor] = timeOrderEvent[n];
            hihPossibleNoise[homeSensor] = timeOrderEvent[n];
            for (int s = 0; s < sensors.length; s++) {
                if (s != homeSensor) {
                    findHighLow(homeSensor, s, timeOrderEvent[n], lowPossibleNoise, hihPossibleNoise);
                }
            }
            int sCount = 0;
            for (int s = 0; s < sensors.length; s++) {
                if (hihPossibleNoise[s] >= lowPossibleNoise[s]) {
                    sCount++;
                }
            }
            if (sCount >= 4) {
                int[] possibleNoise = new int[sensors.length];
                possibleNoise[0] = -2;
                boolean possible = nextPossibleNoise(possibleNoise, lowPossibleNoise, hihPossibleNoise);
                while (possible == true) {
                    double[] x = new double[sCount];
                    double[] y = new double[sCount];
                    double[] t = new double[sCount];
                    int i = 0;
                    for (int s = 0; s < sensors.length; s++) {
                        if (hihPossibleNoise[s] > -1) {
                            x[i] = sensors[s].getX();
                            y[i] = sensors[s].getY();
                            t[i] = sensors[s].getEvent(possibleNoise[s]);
                            i++;
                        }
                    }
                    NoiseEvent ne = new NoiseEvent(x, y, t, getTemperatureAtTime(t[0]));
                    ne.solve();
                    double[] error = new double[sCount];
                    double aveErrorInMeters = ne.distanceFitness(error);
                    double xDif = ne.getSolutionX() - ne.getSensorsCentroidX();
                    double yDif = ne.getSolutionY() - ne.getSensorsCentroidY();
                    double solutionDistanceSquared = xDif * xDif + yDif * yDif;
                    double tDif = ne.getMinimumTimeDifference() - ne.getSolutionTime();
                    if (aveErrorInMeters <= mTolerance && mTimeDiffercial >= tDif && mDistFromSensors * mDistFromSensors >= solutionDistanceSquared) {
                        if (solutions == solutionSize) {
                            solutionSize += 20;
                            NoiseEvent[] oldPossibleSolutions = possibleSolutions;
                            possibleSolutions = new NoiseEvent[solutionSize];
                            double[] oldAveError = aveError;
                            aveError = new double[solutionSize];
                            double[][] oldSensorError = sensorError;
                            sensorError = new double[solutionSize][];
                            for (int old = 0; old < oldPossibleSolutions.length; old++) {
                                possibleSolutions[old] = oldPossibleSolutions[old];
                                aveError[old] = oldAveError[old];
                                sensorError[old] = oldSensorError[old];
                            }
                        }
                        possibleSolutions[solutions] = ne;
                        aveError[solutions] = aveErrorInMeters;
                        sensorError[solutions] = error;
                        solutions++;
                    }
                    possible = nextPossibleNoise(possibleNoise, lowPossibleNoise, hihPossibleNoise);
                }
            }
        }
        if (solutions == 0) {
            possibleSolutions = null;
            aveError = null;
            sensorError = null;
        } else {
            NoiseEvent[] oldPossibleSolutions = possibleSolutions;
            possibleSolutions = new NoiseEvent[solutions];
            double[] oldAveError = aveError;
            aveError = new double[solutions];
            double[][] oldSensorError = sensorError;
            sensorError = new double[solutions][];
            for (int old = 0; old < solutions; old++) {
                possibleSolutions[old] = oldPossibleSolutions[old];
                aveError[old] = oldAveError[old];
                sensorError[old] = oldSensorError[old];
            }
        }
        Date endTime = new Date();
        solveRunTime = (endTime.getTime() - startTime.getTime()) / 1000;
        return solutions;
    }

    private boolean nextPossibleNoise(int[] possibleNoise, int[] lowPossibleNoise, int[] hihPossibleNoise) {
        boolean possible = false;
        if (possibleNoise[0] == -2) {
            for (int i = 0; i < possibleNoise.length; i++) {
                if (hihPossibleNoise[i] != -1) {
                    possibleNoise[i] = lowPossibleNoise[i];
                    possible = true;
                }
            }
        } else {
            for (int i = possibleNoise.length - 1; i >= 0; i--) {
                if (hihPossibleNoise[i] != -1) {
                    possibleNoise[i]++;
                    if (possibleNoise[i] <= hihPossibleNoise[i]) {
                        i = -1;
                        possible = true;
                    } else {
                        possibleNoise[i] = lowPossibleNoise[i];
                    }
                }
            }
        }
        return possible;
    }

    private boolean findHighLow(int homeSensor, int checkSensor, int homeSensorNoiseEvent, int[] lowPossibleNoise, int[] hihPossibleNoise) {
        lowPossibleNoise[checkSensor] = sensors[checkSensor].getNumberEvents();
        hihPossibleNoise[checkSensor] = -1;
        int e = 0;
        while (e < sensors[checkSensor].getNumberEvents() && sensors[checkSensor].getEvent(e) < sensors[homeSensor].getEvent(homeSensorNoiseEvent)) {
            e++;
        }
        if (e == sensors[checkSensor].getNumberEvents()) return false;
        boolean possible = false;
        lowPossibleNoise[checkSensor] = e;
        double homeSensorTime = sensors[homeSensor].getEvent(homeSensorNoiseEvent);
        for (; e < sensors[checkSensor].getNumberEvents(); e++) {
            double checkSensorTime = sensors[checkSensor].getEvent(e);
            double timeBetweenSensors = sensorDistances[homeSensor][checkSensor] / getSpeedOfSoundAtTime((homeSensorTime + checkSensorTime) / 2.0);
            if (homeSensorTime + timeBetweenSensors < checkSensorTime) {
                e = sensors[checkSensor].getNumberEvents() + 1;
            } else {
                hihPossibleNoise[checkSensor] = e;
                possible = true;
            }
        }
        return possible;
    }

    public static void main(String argv[]) {
        int n = 10;
        for (int sen = 6; sen <= 6; sen++) {
            double maxTolerance = 10000.0;
            double[] xs = { 100., 100., 10000., 10000., 5000., 2000. };
            double[] ys = { 100., 10000., 100., 10000., 5000., 2000. };
            Sensor[] s = new Sensor[sen];
            double[][] ts = new double[sen][n];
            double[] noiseX = new double[n];
            double[] noiseY = new double[n];
            double[] noiseTime = new double[n];
            double[] temps = { 8.0, 12.0 };
            double[] tempTime = { 0.0, 100.0 };
            Random ran = new Random(17);
            for (int j = 0; j < n; j++) {
                double nx = 10000.0 * ran.nextDouble();
                noiseX[j] = nx;
                double ny = 10000.0 * ran.nextDouble();
                noiseY[j] = ny;
                double nt = ran.nextDouble() * 30.;
                noiseTime[j] = nt;
                double temperatureCelcius = temps[0] * (tempTime[1] - nt) / (tempTime[1] - tempTime[0]) + temps[1] * (nt - tempTime[0]) / (tempTime[1] - tempTime[0]);
                double speedSound = 331.4 + 0.6 * temperatureCelcius;
                for (int i = 0; i < sen; i++) {
                    ts[i][j] = nt + Math.sqrt((xs[i] - nx) * (xs[i] - nx) + (ys[i] - ny) * (ys[i] - ny)) / speedSound;
                }
            }
            for (int i = 0; i < sen; i++) {
                s[i] = new Sensor(xs[i], ys[i], ts[i]);
            }
            NoiseSet ns = new NoiseSet(s, temps, tempTime, maxTolerance);
            BubbleSort bs = new BubbleSort(false);
            int[] order = new int[n];
            bs.sort(order, noiseTime);
            for (int j = 0; j < n; j++) {
                System.out.println("Event," + noiseX[order[j]] + "," + noiseY[order[j]] + "," + noiseTime[order[j]]);
            }
            ns.setMaximumDistanceFromSensors(10000.);
            ns.setMaximumTimeDifferencial(150.0);
            int numberSolutions = ns.solve();
            if (numberSolutions == 0) {
                System.out.println("no solutions");
            } else {
                NoiseEvent[] events = ns.getPossibleSolutions();
                double[] aveErrors = new double[events.length];
                int gSol = 0;
                double fitQuality = .10;
                for (int i = 0; i < events.length; i++) {
                    aveErrors[i] = events[i].getSolutionFitness();
                    if (aveErrors[i] < fitQuality) {
                        gSol++;
                    }
                }
                NoiseEvent[] good = new NoiseEvent[gSol];
                double[] goodError = new double[gSol];
                double[] goodTime = new double[gSol];
                int[] goodOrder = new int[gSol];
                int g = 0;
                for (int i = 0; i < events.length; i++) {
                    if (aveErrors[i] < fitQuality) {
                        good[g] = events[i];
                        goodError[g] = aveErrors[i];
                        goodTime[g] = events[i].getSolutionTime();
                        goodOrder[g] = g;
                        g++;
                    }
                }
                bs.sort(goodOrder, goodTime);
                for (int gg = 0; gg < gSol; gg++) {
                    int i = goodOrder[gg];
                    System.out.print("result," + good[i].getSolutionX() + "," + good[i].getSolutionY() + "," + good[i].getSolutionTime());
                    System.out.println("," + goodError[i]);
                }
            }
            System.out.println("Solution time: " + ns.getSolveRunTime() + " seconds for [" + sen + "] sensors");
            System.out.println("*********************************************************************");
        }
    }
}
