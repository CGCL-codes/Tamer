package rails.algorithms;

import java.util.Arrays;
import org.apache.log4j.Logger;

abstract class RevenueCalculator {

    protected final int nbVertexes;

    protected final int nbTrains;

    protected final int nbEdges;

    protected final int nbBonuses;

    protected final int[][] vertexValueByTrain;

    protected final boolean[] vertexMajor;

    protected final boolean[] vertexMinor;

    protected final boolean[] vertexSink;

    protected final int[] vertexNbNeighbors;

    protected final int[] vertexNbVisitSets;

    protected final int[] vertexNbBonusSets;

    protected final int[][] vertexNeighbors;

    protected final int[][] vertexEdges;

    protected final int[][] vertexVisitSets;

    protected final int[][] vertexBonusSets;

    protected int[] startVertexes;

    protected final boolean[] edgeGreedy;

    protected final int[] edgeDistance;

    protected final int[] trainMaxMajors;

    protected final int[] trainMaxMinors;

    protected final int[] trainMaxBonuses;

    protected final boolean[] trainIgnoreMinors;

    protected final boolean[] trainIsH;

    protected final int[] trainCurrentValue;

    protected final int[] trainMajors;

    protected final int[] trainMinors;

    protected final int[] trainBonuses;

    protected final boolean[][] trainVisited;

    protected final int[][] trainStack;

    protected final int[] trainStackPos;

    protected final boolean[] trainBottomActive;

    protected final int[] trainStartEdge;

    protected final int[] trainDistance;

    protected final int[] bonusValue;

    protected final boolean[][] bonusActiveForTrain;

    protected final int[] bonusRequiresVertices;

    protected final int[][] bonusTrainVertices;

    protected int startTrainSet;

    protected int finalTrainSet;

    protected int startTrain;

    protected int finalTrain;

    protected boolean useRevenuePrediction;

    protected int currentBestValue;

    protected final int[][] currentBestRun;

    protected int[] maxCumulatedTrainRevenues;

    protected int[][] maxMajorRevenues;

    protected int[][] maxMinorRevenues;

    protected int[][] maxBonusRevenues;

    protected int countVisits;

    protected int countEdges;

    protected int nbEdgesTravelled;

    protected int nbEvaluations;

    protected int nbPredictions;

    protected RevenueAdapter revenueAdapter;

    protected boolean callDynamicModifiers;

    protected static enum Terminated {

        WithEvaluation, WithoutEvaluation, NotYet
    }

    protected static Logger log = Logger.getLogger(RevenueCalculator.class.getPackage().getName());

    public RevenueCalculator(RevenueAdapter revenueAdapter, int nbVertexes, int nbEdges, int maxNeighbors, int maxVertexSets, int maxEdgeSets, int nbTrains, int nbBonuses) {
        log.info("RC defined: nbVertexes = " + nbVertexes + ", nbEdges = " + nbEdges + ", maxNeighbors = " + maxNeighbors + ", maxVertexSets = " + maxVertexSets + ", maxEdgeSets = " + maxEdgeSets + ", nbTrains = " + nbTrains + ", nbBonuses = " + nbBonuses);
        this.revenueAdapter = revenueAdapter;
        this.nbVertexes = nbVertexes;
        this.nbEdges = nbEdges;
        this.nbTrains = nbTrains;
        this.nbBonuses = nbBonuses;
        vertexValueByTrain = new int[nbVertexes][nbTrains];
        vertexMajor = new boolean[nbVertexes];
        vertexMinor = new boolean[nbVertexes];
        vertexSink = new boolean[nbVertexes];
        vertexNbNeighbors = new int[nbVertexes];
        vertexNbVisitSets = new int[nbVertexes];
        vertexNbBonusSets = new int[nbVertexes];
        vertexNeighbors = new int[nbVertexes][maxNeighbors];
        vertexEdges = new int[nbVertexes][maxNeighbors];
        vertexVisitSets = new int[nbVertexes][maxVertexSets];
        vertexBonusSets = new int[nbVertexes][nbBonuses];
        edgeGreedy = new boolean[nbEdges];
        edgeDistance = new int[nbEdges];
        trainMaxMajors = new int[nbTrains];
        trainMaxMinors = new int[nbTrains];
        trainMaxBonuses = new int[nbTrains];
        trainIgnoreMinors = new boolean[nbTrains];
        trainIsH = new boolean[nbTrains];
        trainCurrentValue = new int[nbTrains];
        trainMajors = new int[nbTrains];
        trainMinors = new int[nbTrains];
        trainBonuses = new int[nbTrains];
        trainVisited = new boolean[nbTrains][nbVertexes];
        trainStack = new int[nbTrains][nbVertexes + 1];
        trainStackPos = new int[nbTrains];
        trainBottomActive = new boolean[nbTrains];
        trainStartEdge = new int[nbTrains];
        trainDistance = new int[nbTrains];
        maxCumulatedTrainRevenues = new int[nbTrains];
        bonusValue = new int[nbBonuses];
        bonusRequiresVertices = new int[nbBonuses];
        bonusActiveForTrain = new boolean[nbBonuses][nbTrains];
        bonusTrainVertices = new int[nbBonuses][nbTrains];
        currentBestRun = new int[nbTrains][nbVertexes + 1];
        useRevenuePrediction = false;
        callDynamicModifiers = false;
    }

    final void setVertex(int id, boolean major, boolean minor, boolean sink) {
        vertexMajor[id] = major;
        vertexMinor[id] = minor;
        vertexSink[id] = sink;
        vertexNbNeighbors[id] = 0;
        vertexNbVisitSets[id] = 0;
        vertexNbBonusSets[id] = 0;
    }

    final void setVertexValue(int vertexId, int trainId, int value) {
        vertexValueByTrain[vertexId][trainId] = value;
    }

    final void setVertexNeighbors(int id, int[] neighbors, int[] edges) {
        for (int j = 0; j < neighbors.length; j++) {
            vertexNeighbors[id][j] = neighbors[j];
            vertexEdges[id][j] = edges[j];
        }
        vertexNbNeighbors[id] = neighbors.length;
    }

    final void setStartVertexes(int[] startVertexes) {
        this.startVertexes = startVertexes;
    }

    void setEdge(int edgeId, boolean greedy, int distance) {
        edgeGreedy[edgeId] = greedy;
        edgeDistance[edgeId] = distance;
    }

    final void setTrain(int id, int majors, int minors, boolean ignoreMinors, boolean isHTrain) {
        trainMaxMajors[id] = majors;
        trainMaxMinors[id] = minors;
        trainMaxBonuses[id] = 0;
        trainIgnoreMinors[id] = ignoreMinors;
        trainIsH[id] = isHTrain;
    }

    final void setVisitSet(int[] vertices) {
        for (int j = 0; j < vertices.length; j++) {
            int vertexId = vertices[j];
            for (int k = 0; k < vertices.length; k++) {
                if (k == j) continue;
                vertexVisitSets[vertexId][vertexNbVisitSets[vertexId]++] = vertices[k];
            }
        }
    }

    final void setBonus(int id, int value, int[] vertices, boolean[] bonusForTrain) {
        log.info("RC: define bonus value = " + value + ", vertices = " + Arrays.toString(vertices) + ", bonusForTrain = " + Arrays.toString(bonusForTrain));
        bonusValue[id] = value;
        bonusRequiresVertices[id] = vertices.length;
        for (int j = 0; j < vertices.length; j++) {
            int vertexId = vertices[j];
            vertexBonusSets[vertexId][vertexNbBonusSets[vertexId]++] = id;
        }
        bonusActiveForTrain[id] = bonusForTrain;
    }

    final void setDynamicModifiers(boolean activate) {
        callDynamicModifiers = activate;
    }

    final int[][] getOptimalRun() {
        log.info("RC: currentBestRun = " + Arrays.deepToString(currentBestRun));
        return currentBestRun;
    }

    final int[][] getCurrentRun() {
        int[][] currentRun = new int[nbTrains][nbVertexes + 1];
        for (int j = startTrainSet; j <= finalTrainSet; j++) {
            for (int v = 0; v < nbVertexes + 1; v++) {
                if (v < trainStackPos[j]) {
                    currentRun[j][v] = trainStack[j][v];
                } else {
                    currentRun[j][v] = -1;
                    break;
                }
            }
        }
        return currentRun;
    }

    final int getNumberOfEvaluations() {
        return nbEvaluations;
    }

    final String getStatistics() {
        StringBuffer statistics = new StringBuffer();
        statistics.append(nbEvaluations + " evaluations");
        if (useRevenuePrediction) statistics.append(", " + nbPredictions + " predictions");
        statistics.append(" and " + nbEdgesTravelled + " edges travelled.");
        return statistics.toString();
    }

    private final void notifyRevenueAdapter(final int revenue, final boolean finalResult) {
        String modifier;
        if (finalResult) modifier = "final"; else modifier = "new best";
        log.info("Report " + modifier + " result of " + revenue + " after " + getStatistics());
        revenueAdapter.notifyRevenueListener(revenue, finalResult);
    }

    private final int[] bestRevenues(final int[] values, final int length) {
        int[] bestRevenues = new int[length + 1];
        Arrays.sort(values);
        int cumulatedRevenues = 0;
        for (int j = 1; j <= length; j++) {
            cumulatedRevenues += values[values.length - j];
            bestRevenues[j] = cumulatedRevenues;
        }
        log.debug("Best Revenues = " + Arrays.toString(bestRevenues));
        return bestRevenues;
    }

    private final void initRevenueValues(final int startTrain, final int finalTrain) {
        maxMajorRevenues = new int[nbTrains][nbVertexes];
        maxMinorRevenues = new int[nbTrains][nbVertexes];
        maxBonusRevenues = new int[nbTrains][nbVertexes + nbBonuses];
        for (int t = startTrain; t <= finalTrain; t++) {
            int[] majorValues = new int[nbVertexes];
            int[] minorValues = new int[nbVertexes];
            int[] bonusValues = new int[nbVertexes + nbBonuses];
            int major = 0, minor = 0, bonus = 0;
            for (int v = 0; v < nbVertexes; v++) {
                if (vertexValueByTrain[v][t] == 0) continue;
                if (vertexMajor[v]) {
                    majorValues[major++] = vertexValueByTrain[v][t];
                } else if (vertexMinor[v]) {
                    minorValues[minor++] = vertexValueByTrain[v][t];
                } else {
                    bonusValues[bonus++] = vertexValueByTrain[v][t];
                }
            }
            for (int b = 0; b < nbBonuses; b++) {
                if (bonusValue[b] <= 0 || !bonusActiveForTrain[b][t]) continue;
                bonusValues[bonus++] = bonusValue[b];
            }
            trainMaxBonuses[t] = bonus;
            maxMajorRevenues[t] = bestRevenues(majorValues, trainMaxMajors[t]);
            maxMinorRevenues[t] = bestRevenues(minorValues, trainMaxMinors[t]);
            maxBonusRevenues[t] = bestRevenues(bonusValues, trainMaxBonuses[t]);
            int trainRevenues = maxMajorRevenues[t][trainMaxMajors[t]] + maxMinorRevenues[t][trainMaxMinors[t]] + maxBonusRevenues[t][trainMaxBonuses[t]];
            maxCumulatedTrainRevenues[t] = trainRevenues;
        }
        log.info("maxMajorRevenues = " + Arrays.deepToString(maxMajorRevenues));
        log.info("maxMinorRevenues = " + Arrays.deepToString(maxMinorRevenues));
        log.info("maxBonusRevenues = " + Arrays.deepToString(maxBonusRevenues));
        log.info("maxCumulatedTrainRevenues = " + Arrays.toString(maxCumulatedTrainRevenues));
    }

    final void initRuns(final int startTrain, final int finalTrain) {
        log.info("RC: init runs from " + startTrain + " to " + finalTrain);
        if (startTrain > finalTrain) return;
        this.startTrainSet = startTrain;
        this.finalTrainSet = finalTrain;
        for (int i = startTrain; i < finalTrain; i++) {
            currentBestRun[i][0] = -1;
        }
        currentBestValue = 0;
    }

    final void executePredictions(final int startTrain, final int finalTrain) {
        useRevenuePrediction = true;
        if (startTrain > finalTrain) return;
        initRevenueValues(startTrain, finalTrain);
        if (startTrain == finalTrain) {
            return;
        }
        nbEvaluations = 0;
        nbPredictions = 0;
        nbEdgesTravelled = 0;
        log.info("RC: start individual prediction Runs");
        int[] maxSingleTrainRevenues = new int[nbTrains];
        for (int j = startTrain; j <= finalTrain; j++) {
            this.startTrain = j;
            this.finalTrain = j;
            currentBestValue = 0;
            runTrain(j);
            log.info("RC: Best prediction run of train number " + j + " value = " + currentBestValue + " after " + getStatistics());
            maxSingleTrainRevenues[j] = currentBestValue;
        }
        int cumulatedRevenues = 0;
        for (int j = finalTrain; j >= startTrain; j--) {
            cumulatedRevenues += maxSingleTrainRevenues[j];
            maxCumulatedTrainRevenues[j] = cumulatedRevenues;
        }
        log.info("maxCumulatedTrainRevenues = " + Arrays.toString(maxCumulatedTrainRevenues));
        if (startTrain == finalTrain - 1) return;
        log.info("RC: start combined prediction runs");
        this.finalTrain = finalTrain;
        for (int j = finalTrain - 1; j > startTrain; j--) {
            this.startTrain = j;
            runTrain(j);
            log.info("RC: Best prediction run until train nb. " + j + " value = " + currentBestValue + " after " + getStatistics());
            maxCumulatedTrainRevenues[j] = currentBestValue;
            maxCumulatedTrainRevenues[j - 1] = currentBestValue + maxSingleTrainRevenues[j - 1];
            log.info("maxCumulatedTrainRevenues = " + Arrays.toString(maxCumulatedTrainRevenues));
        }
    }

    final int calculateRevenue(final int startTrain, final int finalTrain) {
        log.info("RC: calculateRevenue trains from " + startTrain + " to " + finalTrain);
        this.startTrain = startTrain;
        this.finalTrain = finalTrain;
        runTrain(startTrain);
        notifyRevenueAdapter(currentBestValue, true);
        return currentBestValue;
    }

    protected abstract void runTrain(final int trainId);

    protected abstract void runBottom(final int trainId);

    protected final boolean encounterVertex(final int trainId, final int vertexId, final boolean arrive) {
        log.debug("RC: EncounterVertex, trainId = " + trainId + " vertexId = " + vertexId + " arrive = " + arrive);
        trainVisited[trainId][vertexId] = arrive;
        boolean stationVertex = false;
        if (arrive) {
            trainCurrentValue[trainId] += vertexValueByTrain[vertexId][trainId];
            if (vertexMajor[vertexId]) {
                trainMajors[trainId]--;
                stationVertex = true;
            } else if (vertexMinor[vertexId]) {
                trainMinors[trainId]--;
                stationVertex = !trainIgnoreMinors[trainId];
            }
            countVisits++;
        } else {
            trainCurrentValue[trainId] -= vertexValueByTrain[vertexId][trainId];
            if (vertexMajor[vertexId]) {
                trainMajors[trainId]++;
                stationVertex = true;
            } else if (vertexMinor[vertexId]) {
                trainMinors[trainId]++;
                stationVertex = !trainIgnoreMinors[trainId];
            }
            countVisits--;
        }
        for (int j = 0; j < vertexNbVisitSets[vertexId]; j++) {
            trainVisited[trainId][vertexVisitSets[vertexId][j]] = arrive;
            log.debug("RC: visited = " + arrive + " for vertex " + vertexVisitSets[vertexId][j] + " due to block rule");
        }
        for (int j = 0; j < vertexNbBonusSets[vertexId]; j++) {
            int bonusId = vertexBonusSets[vertexId][j];
            if (!bonusActiveForTrain[bonusId][trainId]) continue;
            if (arrive) {
                bonusTrainVertices[bonusId][trainId]--;
                log.debug("RC: Decreased bonus " + bonusId + " to " + bonusTrainVertices[bonusId][trainId]);
                if (bonusTrainVertices[bonusId][trainId] == 0) {
                    trainCurrentValue[trainId] += bonusValue[bonusId];
                    if (bonusValue[bonusId] > 0) trainBonuses[trainId]--;
                    log.debug("RC: Added bonus " + bonusId + " with value " + bonusValue[bonusId]);
                }
            } else {
                if (bonusTrainVertices[bonusId][trainId] == 0) {
                    trainCurrentValue[trainId] -= bonusValue[bonusId];
                    if (bonusValue[bonusId] > 0) trainBonuses[trainId]++;
                    log.debug("RC: Removed bonus " + bonusId + " with value " + bonusValue[bonusId]);
                }
                bonusTrainVertices[bonusId][trainId]++;
                log.debug("RC: Increases bonus " + bonusId + " to " + bonusTrainVertices[bonusId][trainId]);
            }
        }
        log.debug("RC: stationVertex = " + stationVertex);
        log.debug("RC: Count Visits = " + countVisits);
        return stationVertex;
    }

    protected abstract void returnEdge(final int trainId, final int edgeId);

    protected Terminated trainTerminated(final int trainId) {
        Terminated terminated = Terminated.NotYet;
        if (trainIgnoreMinors[trainId]) {
            if (trainMajors[trainId] == 0) terminated = Terminated.WithEvaluation;
        } else {
            if (trainMajors[trainId] < 0) {
                terminated = Terminated.WithoutEvaluation;
            } else if (trainMajors[trainId] + trainMinors[trainId] == 0) terminated = Terminated.WithEvaluation;
        }
        if (terminated != Terminated.NotYet) {
            log.debug("RC: Train " + trainId + " has terminated: " + "majors = " + trainMajors[trainId] + " minors = " + trainMinors[trainId]);
        }
        return terminated;
    }

    protected final void finalizeVertex(final int trainId, final int vertexId) {
        log.debug("RC: Finalize Vertex id " + vertexId + " for train " + trainId);
        if (trainId == finalTrain) {
            evaluateResults();
        } else {
            runTrain(trainId + 1);
        }
    }

    protected final void evaluateResults() {
        int totalValue = 0;
        for (int j = startTrain; j <= finalTrain; j++) {
            totalValue += trainCurrentValue[j];
        }
        if (callDynamicModifiers) totalValue += revenueAdapter.dynamicEvaluation();
        nbEvaluations++;
        log.debug("RC: current total value " + totalValue);
        if (totalValue > currentBestValue) {
            currentBestValue = totalValue;
            for (int j = startTrainSet; j <= finalTrainSet; j++) {
                for (int v = 0; v < nbVertexes + 1; v++) {
                    if (v < trainStackPos[j]) {
                        currentBestRun[j][v] = trainStack[j][v];
                    } else {
                        currentBestRun[j][v] = -1;
                        break;
                    }
                }
            }
            log.info("RC: Found better run with " + totalValue);
            notifyRevenueAdapter(currentBestValue, false);
        }
    }

    protected final boolean predictRevenues(final int trainId) {
        int totalValue = 0;
        if (trainId < finalTrain) totalValue = maxCumulatedTrainRevenues[trainId + 1];
        int trainValue = trainCurrentValue[trainId];
        if (trainIgnoreMinors[trainId]) {
            trainValue += maxMajorRevenues[trainId][trainMajors[trainId]];
        } else {
            if (trainMinors[trainId] > 0) {
                trainValue += maxMajorRevenues[trainId][trainMajors[trainId]];
                trainValue += maxMinorRevenues[trainId][trainMinors[trainId]];
            } else {
                trainValue += maxMajorRevenues[trainId][trainMajors[trainId] + trainMinors[trainId]];
            }
        }
        if (trainBonuses[trainId] != 0) {
            trainValue += maxBonusRevenues[trainId][trainBonuses[trainId]];
        }
        log.debug("RC: Current train has predicted  value of " + trainValue);
        totalValue = Math.min(totalValue + trainValue, maxCumulatedTrainRevenues[trainId]);
        for (int j = startTrain; j < trainId; j++) {
            totalValue += trainCurrentValue[j];
        }
        if (callDynamicModifiers) totalValue += revenueAdapter.dynamicPrediction();
        nbPredictions++;
        boolean terminate = (totalValue <= currentBestValue);
        if (terminate) log.debug("Run terminated due to predicted value of " + totalValue);
        return terminate;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("vertexValuesByTrain:" + Arrays.deepToString(vertexValueByTrain) + "\n");
        buffer.append("vertexMajor:" + Arrays.toString(vertexMajor) + "\n");
        buffer.append("vertexMinor:" + Arrays.toString(vertexMinor) + "\n");
        buffer.append("vertexNeighbors:" + Arrays.deepToString(vertexNeighbors) + "\n");
        buffer.append("vertexEdges:" + Arrays.deepToString(vertexEdges) + "\n");
        buffer.append("vertexVisitSets:" + Arrays.deepToString(vertexVisitSets) + "\n");
        buffer.append("vertexBonusSets:" + Arrays.deepToString(vertexBonusSets) + "\n");
        buffer.append("vertexNbVisitSets:" + Arrays.toString(vertexNbVisitSets) + "\n");
        buffer.append("vertexNbBonusSets:" + Arrays.toString(vertexNbBonusSets) + "\n");
        buffer.append("edgeGreedy:" + Arrays.toString(edgeGreedy) + "\n");
        buffer.append("edgeDistance:" + Arrays.toString(edgeDistance) + "\n");
        buffer.append("startVertexes:" + Arrays.toString(startVertexes) + "\n");
        buffer.append("trainMaxMajors:" + Arrays.toString(trainMaxMajors) + "\n");
        buffer.append("trainMaxMinors:" + Arrays.toString(trainMaxMinors) + "\n");
        buffer.append("trainIgnoreMinors:" + Arrays.toString(trainIgnoreMinors) + "\n");
        return buffer.toString();
    }
}
