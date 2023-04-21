package game.models.Cascade;

import java.util.ArrayList;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class CascadeCorrelation implements ILearningAlgorithm, Serializable {

    private NeuralNetwork neuralNetwork;

    private static final int MAX_LAYERS_NUMBER = 4;

    private static final double ACCEPTABLE_ERROR = 0.001;

    private static final int CAND_NUMBER = 1;

    private static final int OUT_UPDATE_CYCLES = 100;

    private static final double MIN_ERROR_RED = 0.02;

    private static final double OUT_DECAY = 0.0001;

    private static final double OUT_MAX_ALFA = 2;

    private static final double OUT_EPSILON = 0.001;

    private static final int OUT_PATIENCE_PARAM = 20;

    private static final int CAND_UPDATE_CYCLES = 100;

    private static final double MIN_COR_GROWTH = 0.03;

    private static final int CAND_PATIENCE_PARAM = 20;

    private static final double CAND_DECAY = 0.0000;

    private static final double CAND_MAX_ALFA = 2;

    private static final double CAND_EPSILON = 0.02;

    private static final double OUT_LEARNING_RATE = 0.1;

    private static final double OUT_MOMENTUM_RATE = 0.1;

    private static final double CAND_LEARNING_RATE = 0.1;

    private static final double CAND_MOMENTUM_RATE = 0.1;

    private static final double CAND_ETA_MINUS = 0.5;

    private static final double CAND_ETA_PLUS = 1.2;

    private static final double OUT_ETA_MINUS = 0.5;

    private static final double OUT_ETA_PLUS = 1.2;

    private static final int USED_ALG = 3;

    private int maxLayersNumber;

    private double acceptableError;

    private int candNumber = 1;

    private int candUpdateCycles;

    private double minCorGrowth;

    private double candDecay;

    private double candMaxAlfa;

    private double candEpsilon;

    private int candPatienceParam;

    private int outUpdateCycles;

    private double minErrorRed;

    private double outDecay;

    private double outMaxAlfa;

    private double outEpsilon;

    private int outPatienceParam;

    private double outLearningRate;

    private double outMomentumRate;

    private double candLearningRate;

    private double candMomentumRate;

    private double candEtaMinus;

    private double candEtaPlus;

    private double outEtaMinus;

    private double outEtaPlus;

    private boolean log = true;

    private transient BufferedWriter out;

    private long starttime;

    private long endtime;

    private double currentError;

    private double lastError;

    private double lastBestScore;

    private double currentBestScore;

    private int bestCand;

    private int usedAlg;

    private int patternNumber;

    private int outputsNumber;

    private boolean paramInitialized = false;

    public CascadeCorrelation(NeuralNetwork neuralNetwork, int maxLayersNumber, double acceptableError, int candNumber, boolean log, int usedAlg) throws Exception {
        this.neuralNetwork = neuralNetwork;
        this.acceptableError = acceptableError;
        this.maxLayersNumber = maxLayersNumber;
        this.outputsNumber = this.neuralNetwork.outputLayer().size();
        this.currentError = 0;
        this.lastError = 0;
        this.log = log;
        this.usedAlg = usedAlg;
    }

    @SuppressWarnings("static-access")
    public CascadeCorrelation(NeuralNetwork neuralNetwork) throws Exception {
        this.neuralNetwork = neuralNetwork;
        this.acceptableError = this.ACCEPTABLE_ERROR;
        this.maxLayersNumber = this.MAX_LAYERS_NUMBER;
        this.candNumber = this.CAND_NUMBER;
        this.outputsNumber = this.neuralNetwork.outputLayer().size();
        this.currentError = 0;
        this.lastError = 0;
        this.usedAlg = this.USED_ALG;
        this.log = true;
    }

    public void setQuickParams(int candUpdatesCycles, double minCorGrowth, double candDecay, double candMaxAlfa, double candEpsilon, int candPatienceParam, int outUpdateCycles, double minErrorRed, double outDecay, double outMaxAlfa, double outEpsilon, int outPatienceParam) {
        this.candUpdateCycles = candUpdatesCycles;
        this.minCorGrowth = minCorGrowth;
        this.candDecay = candDecay;
        this.candMaxAlfa = candMaxAlfa;
        this.candEpsilon = candEpsilon;
        this.candPatienceParam = candPatienceParam;
        this.outUpdateCycles = outUpdateCycles;
        this.minErrorRed = minErrorRed;
        this.outDecay = outDecay;
        this.outMaxAlfa = outMaxAlfa;
        this.outEpsilon = outEpsilon;
        this.candPatienceParam = candPatienceParam;
        this.paramInitialized = true;
    }

    @SuppressWarnings("static-access")
    public void setQuickParams() {
        this.candUpdateCycles = this.CAND_UPDATE_CYCLES;
        this.minCorGrowth = this.MIN_COR_GROWTH;
        this.candDecay = this.CAND_DECAY;
        this.candMaxAlfa = this.CAND_MAX_ALFA;
        this.candEpsilon = this.CAND_EPSILON;
        this.candPatienceParam = this.CAND_PATIENCE_PARAM;
        this.outUpdateCycles = this.OUT_UPDATE_CYCLES;
        this.minErrorRed = this.MIN_ERROR_RED;
        this.outDecay = this.OUT_DECAY;
        this.outMaxAlfa = this.OUT_MAX_ALFA;
        this.outEpsilon = this.OUT_EPSILON;
        this.outPatienceParam = this.OUT_PATIENCE_PARAM;
        this.paramInitialized = true;
    }

    public void setBackParams(double outLearningRate, double outMomentumRate, double candLearningRate, double candMomentumRate) {
        this.outLearningRate = outLearningRate;
        this.outMomentumRate = outMomentumRate;
        this.candLearningRate = candLearningRate;
        this.candMomentumRate = candMomentumRate;
        this.paramInitialized = true;
    }

    @SuppressWarnings("static-access")
    public void setBackParams() {
        this.outLearningRate = this.OUT_LEARNING_RATE;
        this.outMomentumRate = this.OUT_MOMENTUM_RATE;
        this.candLearningRate = this.CAND_LEARNING_RATE;
        this.candMomentumRate = this.CAND_MOMENTUM_RATE;
        this.paramInitialized = true;
    }

    public void setRpropParams(int candUpdateCycles, double minCorGrowth, double candDecay, double candEtaMinus, double candEtaPlus, double outEtaMinus, double outEtaPlus, int candPatienceParam, int outUpdateCycles, double minErrorRed, double outDecay, int outPatienceParam) {
        this.candUpdateCycles = candUpdateCycles;
        this.minCorGrowth = minCorGrowth;
        this.candDecay = candDecay;
        this.candPatienceParam = candPatienceParam;
        this.outUpdateCycles = outUpdateCycles;
        this.minErrorRed = minErrorRed;
        this.outDecay = outDecay;
        this.outPatienceParam = outPatienceParam;
        this.candEtaMinus = candEtaMinus;
        this.candEtaPlus = candEtaPlus;
        this.outEtaMinus = outEtaMinus;
        this.outEtaPlus = outEtaPlus;
        this.paramInitialized = true;
    }

    @SuppressWarnings("static-access")
    public void setRpropParams() {
        this.candUpdateCycles = this.CAND_UPDATE_CYCLES;
        this.minCorGrowth = this.MIN_COR_GROWTH;
        this.candDecay = this.CAND_DECAY;
        this.candPatienceParam = this.CAND_PATIENCE_PARAM;
        this.outUpdateCycles = this.OUT_UPDATE_CYCLES;
        this.minErrorRed = this.MIN_ERROR_RED;
        this.outDecay = this.OUT_DECAY;
        this.outPatienceParam = this.OUT_PATIENCE_PARAM;
        this.candEtaMinus = this.CAND_ETA_MINUS;
        this.candEtaPlus = this.CAND_ETA_PLUS;
        this.outEtaMinus = this.OUT_ETA_MINUS;
        this.outEtaPlus = this.OUT_ETA_PLUS;
        this.paramInitialized = true;
    }

    private void initializeLog(String fileName) throws Exception {
        try {
            Calendar calendar = new GregorianCalendar();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            FileWriter fstream = new FileWriter("log.txt", true);
            out = new BufferedWriter(fstream);
        } catch (Exception ex) {
            throw new Exception("NeuralNetwork: initializeLog -> " + ex.getMessage());
        }
    }

    private void writeToLog(String text) throws Exception {
        try {
            out.write(text);
            out.newLine();
        } catch (Exception ex) {
            throw new Exception("Caccade Correlation: writeToLog -> " + ex.getMessage());
        }
    }

    private void writeToLog(double[][] outputs, TrainingSet trainingSet) throws Exception {
        try {
            for (int i = 0; i < outputs.length; i++) {
                out.write("Pattern " + i);
                for (int k = 0; k < outputs[i].length; k++) {
                    out.write(outputs[i][k] + " - ");
                    out.write(trainingSet.getTrainingPattern(i).getDesiredOutput(k) + "");
                    if (Math.abs(outputs[i][k] - trainingSet.getTrainingPattern(i).getDesiredOutput(k)) > 0.1) out.write("!!");
                    out.write("   ");
                }
                out.newLine();
            }
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: writeToLog -> " + ex.getMessage());
        }
    }

    private void writeOutputToLog(double[][] outputs, TrainingSet trainingSet) throws Exception {
        try {
            for (int i = 0; i < outputs.length; i++) {
                this.out.write(i + ".pattern ");
                for (int k = 0; k < outputs[i].length; k++) {
                    DecimalFormat format = new DecimalFormat("#,####0.0000");
                    this.out.write(format.format(outputs[i][k]) + " ");
                }
                this.out.write("\\");
                for (int k = 0; k < outputs[i].length; k++) {
                    this.out.write("" + trainingSet.getTrainingPattern(i).getDesiredOutput(k) + " ");
                    if (trainingSet.getTrainingPattern(i).getDesiredOutput(k) == 1) {
                        if (outputs[i][k] < 0.5) this.out.write("!!!");
                    } else {
                        if (outputs[i][k] > 0.5) this.out.write("!!!");
                    }
                }
                this.writeToLog("");
            }
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: writeOutputToLog -> " + ex.getMessage());
        }
    }

    private void startCountTime() {
        this.starttime = System.currentTimeMillis();
    }

    private void endCountTime() {
        this.endtime = System.currentTimeMillis();
    }

    private String getTimeLap() {
        return "" + (this.endtime - this.starttime);
    }

    public String getType() {
        return "Cascade Correlation";
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    private void closeLog() throws Exception {
        try {
            this.out.close();
        } catch (Exception ex) {
            throw new Exception("Cascade Correlation: closeLog -> " + ex.getMessage());
        }
    }

    public void trainNetwork(TrainingSet trainingSet) throws Exception {
        if (!this.paramInitialized) {
            this.paramInitialized = true;
            switch(this.usedAlg) {
                case 1:
                    this.setBackParams();
                    break;
                case 2:
                    this.setQuickParams();
                    break;
                case 3:
                    this.setRpropParams();
                    break;
            }
        }
        if (log) this.initializeLog("log");
        this.patternNumber = trainingSet.size();
        this.neuralNetwork.resetDeltas();
        this.neuralNetwork.resetSlopes();
        int numberIterations = 0;
        do {
            ILearningAlgorithm outAlg = null;
            ISlopeCalcFunction slopeCalc = null;
            switch(this.usedAlg) {
                case 1:
                    outAlg = new BackPropagation(this.neuralNetwork, this.outLearningRate, this.outMomentumRate);
                    slopeCalc = new SlopeCalcFunctionBackProp();
                    break;
                case 2:
                    outAlg = new QuickPropagation(this.neuralNetwork, this.outMaxAlfa, this.outDecay, this.outEpsilon, false);
                    slopeCalc = new SlopeCalcFunctionQuickProp();
                    break;
                case 3:
                    outAlg = new RProp(this.neuralNetwork, this.outEtaMinus, this.outEtaPlus);
                    slopeCalc = new SlopeCalcFunctionRprop();
                    break;
            }
            this.startCountTime();
            int numberEpoch = trainOutputs(trainingSet, outAlg, slopeCalc);
            this.endCountTime();
            if (log) {
                this.writeToLog("iterace " + ++numberIterations);
                this.writeToLog("outUpdateCycles = " + this.outUpdateCycles);
                this.writeToLog("output training(" + outAlg.getType() + "): " + numberEpoch + " kroku");
                this.writeToLog("E = " + this.neuralNetwork.calculateSquaredError(trainingSet));
                this.writeToLog("time = " + this.getTimeLap() + "ms");
            }
            if (this.neuralNetwork.calculateSquaredError(trainingSet) < this.acceptableError) break; else {
                NeuronLayer candidateLayer = this.includeCandidateNodes(this.candNumber);
                ILearningAlgorithm candAlg = null;
                switch(this.usedAlg) {
                    case 1:
                        candAlg = new BackPropagation(this.neuralNetwork, this.candLearningRate, this.candMomentumRate);
                        break;
                    case 2:
                        candAlg = new QuickPropagation(this.neuralNetwork, this.candMaxAlfa, this.candDecay, this.candEpsilon, false);
                        break;
                    case 3:
                        candAlg = new RProp(this.neuralNetwork, this.candEtaMinus, this.candEtaPlus);
                        break;
                }
                this.startCountTime();
                numberEpoch = this.trainCandidateNodes(candidateLayer, trainingSet, candAlg);
                this.endCountTime();
                Neuron bestCandidate = candidateLayer.getNeuron(this.bestCand);
                if (log) {
                    this.writeToLog("candUpdateCycles = " + this.candUpdateCycles);
                    this.writeToLog("Candidate training(" + candAlg.getType() + "): " + numberEpoch + " kroku");
                    this.writeToLog("bestScore = " + this.currentBestScore);
                    this.writeToLog("time = " + this.getTimeLap() + "ms");
                    this.writeToLog("====================================================");
                }
                candidateLayer = this.eliminateBadCandidates(candidateLayer, bestCandidate);
                this.integrateCandidate(candidateLayer);
            }
        } while (this.neuralNetwork.size() <= this.maxLayersNumber);
        if (log) this.closeLog();
    }

    private boolean moreTrainingOutput(int numberOfEpoch, TrainingSet trainingSet) throws Exception {
        if (numberOfEpoch > this.outUpdateCycles) return false;
        if (numberOfEpoch % this.outPatienceParam == 0) {
            try {
                this.currentError = this.neuralNetwork.calculateSquaredError(trainingSet);
            } catch (Exception ex) {
                throw new Exception("CascadeCorrelation: moreTrainingOutput -> " + ex.getMessage());
            }
            if (Math.abs(this.currentError - this.lastError) <= this.minErrorRed * this.lastError) return false; else {
                this.lastError = currentError;
                return true;
            }
        } else return true;
    }

    private int trainOutputs(TrainingSet trainingSet, ILearningAlgorithm learningAlgorithm, ISlopeCalcFunction slopeCalcFunction) throws Exception {
        int numberOfEpoch = 0;
        this.currentError = 0;
        this.lastError = 0;
        try {
            do {
                numberOfEpoch++;
                ArrayList<Synapse> synapsesToTrain = new ArrayList<Synapse>();
                Iterator<Neuron> neuronIterator = this.neuralNetwork.outputLayer().neuronList().iterator();
                while (neuronIterator.hasNext()) {
                    synapsesToTrain.addAll(neuronIterator.next().incomingSynapses());
                }
                SlopeCalcParams params = new SlopeCalcParams();
                params.neuralNetwork = this.neuralNetwork;
                params.synapsesToTrain = synapsesToTrain;
                params.decay = this.outDecay;
                params.mode = TrainMode.minimize;
                try {
                    learningAlgorithm.train(trainingSet, params, slopeCalcFunction);
                } catch (Exception ex) {
                    throw new Exception("CascadeCorrelation: trainOutputs -> " + ex.getMessage());
                }
            } while (moreTrainingOutput(numberOfEpoch, trainingSet));
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: trainOutputs -> " + ex.getMessage());
        }
        return numberOfEpoch;
    }

    private NeuronLayer includeCandidateNodes(int number) throws Exception {
        try {
            NeuronLayer candidateLayer = new NeuronLayer(number, LayerType.hidden, this.neuralNetwork.neuronId++, 0, new ActivationFunctionSigmoidFahlmanOffset());
            int hiddenLayerSize = this.neuralNetwork.hiddenLayers().size();
            if (hiddenLayerSize > 0) {
                NeuronLayer lastHiddenLayer = this.neuralNetwork.hiddenLayers().get(hiddenLayerSize - 1);
                this.neuralNetwork.fullyConnectLayers(lastHiddenLayer, candidateLayer, true);
            }
            NeuronLayer inputLayer = this.neuralNetwork.inputLayer();
            this.neuralNetwork.fullyConnectLayers(inputLayer, candidateLayer, true);
            this.neuralNetwork.layers().add(this.neuralNetwork.layers().size() - 1, candidateLayer);
            return candidateLayer;
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: includeCandidateNodes -> " + ex.getMessage());
        }
    }

    private int trainCandidateNodes(NeuronLayer candidateLayer, TrainingSet trainingSet, ILearningAlgorithm learningAlgorithm) throws Exception {
        this.currentBestScore = 0;
        this.lastBestScore = 0;
        int numberOfEpochs = 0;
        SlopeCalcParams partialDerivativeInfo = new SlopeCalcParams();
        partialDerivativeInfo.neuralNetwork = this.neuralNetwork;
        partialDerivativeInfo.decay = this.candDecay;
        partialDerivativeInfo.synapsesToTrain = new ArrayList<Synapse>();
        Iterator<Neuron> neuronIterator = candidateLayer.neuronList().iterator();
        while (neuronIterator.hasNext()) {
            partialDerivativeInfo.synapsesToTrain.addAll(neuronIterator.next().incomingSynapses());
        }
        partialDerivativeInfo.mode = TrainMode.minimize;
        try {
            do {
                DataForCorrelationComp data = this.getDataForCorrelation(this.candNumber, this.outputsNumber, trainingSet, candidateLayer);
                double[][] precorrelation = this.precalculateCorrelation(candidateLayer, trainingSet, data);
                double[][] correlations = this.calculateCorrelation(data, precorrelation);
                SlopeCalcFunctionCC partialDerivativeFunction = new SlopeCalcFunctionCC(candidateLayer, correlations, data);
                learningAlgorithm.train(trainingSet, partialDerivativeInfo, partialDerivativeFunction);
                numberOfEpochs++;
            } while (moreTrainingCand(numberOfEpochs));
            return numberOfEpochs;
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: trainCandidateNodes -> " + ex.getMessage());
        }
    }

    private NeuronLayer eliminateBadCandidates(NeuronLayer candidateLayer, Neuron bestCandidate) {
        ArrayList<Neuron> pomList = new ArrayList<Neuron>();
        Iterator<Neuron> neuronIterator = candidateLayer.neuronList().iterator();
        while (neuronIterator.hasNext()) {
            Neuron neuron = neuronIterator.next();
            if (neuron != bestCandidate) pomList.add(neuron);
        }
        neuronIterator = pomList.iterator();
        while (neuronIterator.hasNext()) {
            candidateLayer.removeNeuron(neuronIterator.next());
        }
        return candidateLayer;
    }

    private void integrateCandidate(NeuronLayer candidateLayer) throws Exception {
        try {
            this.neuralNetwork.fullyConnectLayers(candidateLayer, this.neuralNetwork.outputLayer(), true);
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: integrateCandidate -> " + ex.getMessage());
        }
    }

    private boolean moreTrainingCand(int numberOfEpoch) {
        if (numberOfEpoch > this.candUpdateCycles) return false;
        if (numberOfEpoch % this.candPatienceParam == 0) {
            if (Math.abs(this.currentBestScore - this.lastBestScore) < this.minCorGrowth * this.lastBestScore) return false; else {
                this.lastBestScore = this.currentBestScore;
                return true;
            }
        } else return true;
    }

    public void modifyWeights(TrainMode mode, ArrayList<Synapse> synapsesToModify) {
    }

    public void train(TrainingSet trainingSet, SlopeCalcParams partialDerivativeInfo, ISlopeCalcFunction partialDerivativeFunction) throws Exception {
    }

    public void train(TrainingSet trainingSet, ISlopeCalcFunction partialDerivativeFunction) {
    }

    public DataForCorrelationComp getDataForCorrelation(int candidatesNumber, int outputsNumber, TrainingSet trainingSet, NeuronLayer candidatesLayer) throws Exception {
        try {
            DataForCorrelationComp data = new DataForCorrelationComp(candidatesNumber, outputsNumber, this.patternNumber);
            Iterator<TrainingPattern> patternIterator = trainingSet.getTraningSet().iterator();
            int patternIndex = 0;
            while (patternIterator.hasNext()) {
                TrainingPattern trainingPattern = patternIterator.next();
                this.neuralNetwork.injectInput(trainingPattern.getInputPattern());
                this.neuralNetwork.bubbleThrough();
                int candidateIndex = 0;
                Iterator<Neuron> neuronIterator = candidatesLayer.neuronList().iterator();
                while (neuronIterator.hasNext()) {
                    Neuron candidateNeuron = neuronIterator.next();
                    Double candValue = candidateNeuron.currentOutput();
                    data.candidatesValues[candidateIndex][patternIndex] = candValue;
                    data.candidatesSumValue[candidateIndex++] += candValue;
                }
                neuronIterator = this.neuralNetwork.outputLayer().neuronList().iterator();
                int outputNeuronIndex = 0;
                Iterator<Double> doubleIterator = trainingPattern.getDesiredOutputs().getPatternList().iterator();
                while (neuronIterator.hasNext()) {
                    Neuron neuron = neuronIterator.next();
                    double residualError = (neuron.currentOutput() - doubleIterator.next()) * neuron.currentDerivative();
                    data.outputsResidualErrors[outputNeuronIndex][patternIndex] = residualError;
                    data.outputsAverageResidualError[outputNeuronIndex] += residualError;
                    data.sumErrors[outputNeuronIndex] += residualError;
                    data.sumSqError += residualError * residualError;
                    outputNeuronIndex++;
                }
                patternIndex++;
            }
            for (int i = 0; i < outputsNumber; i++) {
                data.outputsAverageResidualError[i] = data.outputsAverageResidualError[i] / this.patternNumber;
            }
            return data;
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: getDataForCorrelation -> " + ex.getMessage());
        }
    }

    public double[][] precalculateCorrelation(NeuronLayer candidateLayer, TrainingSet trainingSet, DataForCorrelationComp data) throws Exception {
        try {
            double[][] precorrelations = new double[this.candNumber][this.outputsNumber];
            for (int patternIndex = 0; patternIndex < this.patternNumber; patternIndex++) {
                for (int candIndex = 0; candIndex < this.candNumber; candIndex++) {
                    for (int outputNeuronIndex = 0; outputNeuronIndex < this.outputsNumber; outputNeuronIndex++) {
                        precorrelations[candIndex][outputNeuronIndex] += data.candidatesValues[candIndex][patternIndex] * data.outputsResidualErrors[outputNeuronIndex][patternIndex];
                    }
                }
            }
            return precorrelations;
        } catch (Exception ex) {
            throw new Exception("CascadeCorrelation: calculateCorrelation -> " + ex.getMessage());
        }
    }

    public double[][] calculateCorrelation(DataForCorrelationComp data, double[][] precorrelations) {
        double bestScore = -1;
        this.currentBestScore = 0;
        double[][] correlations = new double[this.candNumber][this.outputsNumber];
        for (int c = 0; c < this.candNumber; c++) {
            double score = 0;
            for (int o = 0; o < this.outputsNumber; o++) {
                correlations[c][o] = (precorrelations[c][o] - (data.outputsAverageResidualError[o] * data.candidatesSumValue[c])) / data.sumSqError;
                score += Math.abs(correlations[c][o]);
            }
            if (score > bestScore) {
                bestScore = score;
                this.bestCand = c;
            }
        }
        this.currentBestScore = bestScore;
        return correlations;
    }
}
