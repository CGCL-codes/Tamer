package game.models.Cascade;

import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class SlopeCalcFunctionRprop implements ISlopeCalcFunction {

    private void calculateDelta(SlopeCalcParams params, TrainingPattern trainingPattern) throws Exception {
        try {
            params.neuralNetwork.injectInput(trainingPattern.getInputPattern());
            params.neuralNetwork.bubbleThrough();
            this.calculateDeltaForOutputs(params, trainingPattern.getDesiredOutputs());
            this.calculateDeltaForHiddens(params);
        } catch (Exception e) {
            throw new Exception("SlopeCalcFunctionBackProp: calculateDelta -> " + e.getMessage());
        }
    }

    private void calculateDeltaForOutputs(SlopeCalcParams params, Pattern desiredOutputs) throws Exception {
        NeuralNetwork neuralNetwork = params.neuralNetwork;
        Iterator<Neuron> neuronIterator = neuralNetwork.outputLayer().neuronList().iterator();
        int index = 0;
        while (neuronIterator.hasNext()) {
            neuronIterator.next().calculateDelta(desiredOutputs.get(index++));
        }
    }

    private void calculateDeltaForHiddens(SlopeCalcParams params) {
        NeuralNetwork neuralNetwork = params.neuralNetwork;
        Iterator<NeuronLayer> layerIterator = neuralNetwork.hiddenLayers().iterator();
        while (layerIterator.hasNext()) {
            Iterator<Neuron> neuronIterator = layerIterator.next().neuronList().iterator();
            while (neuronIterator.hasNext()) {
                neuronIterator.next().calculateDelta();
            }
        }
    }

    private void calculateSlope(Synapse synapse, SlopeCalcParams params, boolean accumulate) {
        double slope = synapse.destinationNeuron().currentDelta() * synapse.sourceNeuron().currentOutput();
        if (accumulate) synapse.setCurrentSlope(slope + synapse.currentSlope()); else synapse.setCurrentSlope(slope);
    }

    public void calculateSlope(SlopeCalcParams params, TrainingSet trainingSet) throws Exception {
        params.neuralNetwork.storeLastSlope();
        params.neuralNetwork.resetDeltas();
        params.neuralNetwork.resetSlopes();
        Iterator<TrainingPattern> patternIterator = trainingSet.getTraningSet().iterator();
        while (patternIterator.hasNext()) {
            TrainingPattern trainingPattern = patternIterator.next();
            this.calculateDelta(params, trainingPattern);
            boolean accumulate = true;
            Iterator<Synapse> synapseIterator = params.synapsesToTrain.iterator();
            while (synapseIterator.hasNext()) {
                calculateSlope(synapseIterator.next(), params, accumulate);
            }
        }
    }

    public void calculateSlope(SlopeCalcParams params, TrainingPattern trainingPattern) throws Exception {
        params.neuralNetwork.storeLastSlope();
        params.neuralNetwork.resetDeltas();
        params.neuralNetwork.resetSlopes();
        try {
            this.calculateDelta(params, trainingPattern);
        } catch (Exception e) {
            throw new Exception("SlopeCalcFunctionBackProp: calculateSlope -> " + e.getMessage());
        }
        boolean accumulate = false;
        Iterator<Synapse> synapseIterator = params.synapsesToTrain.iterator();
        while (synapseIterator.hasNext()) {
            this.calculateSlope(synapseIterator.next(), params, accumulate);
        }
    }
}
