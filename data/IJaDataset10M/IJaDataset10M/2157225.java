package com.platonov.network.nnet.learning;

import com.platonov.network.core.*;
import com.platonov.network.core.lerning.SupervisedLearning;
import com.platonov.network.core.lerning.TrainingData;
import com.platonov.network.core.lerning.TrainingSet;
import java.io.Serializable;

/**
 * User: Platonov
 * Date: 21.08.11
 */
public class LMS extends SupervisedLearning implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean batchMode = false;

    protected int trainingDataBufferSize = 2;

    public LMS() {
        super();
    }

    protected void updatePatternError(double[] patternErrorVector) {
        this.patternErrorSqrSum = 0;
        for (double error : patternErrorVector) {
            this.patternErrorSqrSum += (error * error) * 0.5;
        }
        this.totalPatternErrorSqrSum += this.patternErrorSqrSum;
    }

    @Override
    protected void updateTotalNetworkError() {
        this.totalNetworkError = totalPatternErrorSqrSum / (this.getTrainingSet().size());
    }

    @Override
    protected void updateNetworkWeights(double[] patternError) {
        int i = 0;
        for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
            neuron.setError(patternError[i]);
            this.updateNeuronWeights(neuron);
            i++;
        }
    }

    protected void updateNeuronWeights(Neuron neuron) {
        double neuronError = neuron.getError();
        for (Connection connection : neuron.getInputConnections()) {
            double input = connection.getInput();
            double deltaWeight = this.learningRate * neuronError * input;
            this.applyWeightChange(connection.getWeight(), deltaWeight);
        }
    }

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    @Override
    public void doLearningEpoch(TrainingSet trainingSet) {
        super.doLearningEpoch(trainingSet);
        if (this.batchMode == true) {
            batchModeWeightsUpdate();
        }
    }

    protected void applyWeightChange(Weight weight, double deltaWeight) {
        if (this.batchMode == false) {
            weight.inc(deltaWeight);
        } else {
            double deltaWeightSum = weight.getTrainingData().get(TrainingData.DELTA_WEIGHT_SUM) + deltaWeight;
            weight.getTrainingData().set(TrainingData.DELTA_WEIGHT_SUM, deltaWeightSum);
        }
    }

    protected void batchModeWeightsUpdate() {
        for (int i = neuralNetwork.getLayersCount() - 1; i > 0; i--) {
            Layer layer = neuralNetwork.getLayers().get(i);
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    Weight weight = connection.getWeight();
                    double deltaWeightSum = weight.getTrainingData().get(TrainingData.DELTA_WEIGHT_SUM);
                    weight.inc(deltaWeightSum);
                    weight.getTrainingData().set(TrainingData.DELTA_WEIGHT_SUM, 0);
                }
            }
        }
    }

    @Override
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        super.setNeuralNetwork(neuralNetwork);
        this.initTrainingDataBuffer();
    }

    protected void initTrainingDataBuffer() {
        for (int i = neuralNetwork.getLayersCount() - 1; i > 0; i--) {
            Layer layer = neuralNetwork.getLayers().get(i);
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    Weight weight = connection.getWeight();
                    weight.initTrainingDataBuffer(this.trainingDataBufferSize);
                }
            }
        }
    }

    public int getTrainingDataBufferSize() {
        return trainingDataBufferSize;
    }

    public final void setTrainingDataBufferSize(int trainingDataBufferSize) {
        this.trainingDataBufferSize = trainingDataBufferSize;
    }

    /**
     * @param patternError
     * @deprecated
     */
    @Override
    protected void updateTotalNetworkError(double[] patternError) {
        double sqrErrorSum = 0;
        for (double error : patternError) {
            sqrErrorSum += (error * error);
        }
        this.totalNetworkError += sqrErrorSum / (2 * patternError.length);
    }
}
