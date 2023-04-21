package zzzhc.image.recognize.nn;

import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.net.NeuralNet;

public class SigmoidNN extends NNBase {

    private Layer hiddenLayer;

    private TeachingSynapse trainer;

    protected void buildNeuralNet() {
        inputLayer = new SigmoidLayer();
        hiddenLayer = new SigmoidLayer();
        outputLayer = new SigmoidLayer();
        inputLayer.setLayerName("input");
        hiddenLayer.setLayerName("hidden");
        outputLayer.setLayerName("output");
        FullSynapse synapse_IH = new FullSynapse();
        FullSynapse synapse_HO = new FullSynapse();
        synapse_IH.setName("IH");
        synapse_HO.setName("HO");
        inputLayer.addOutputSynapse(synapse_IH);
        hiddenLayer.addInputSynapse(synapse_IH);
        hiddenLayer.addOutputSynapse(synapse_HO);
        outputLayer.addInputSynapse(synapse_HO);
        trainer = new TeachingSynapse();
        outputLayer.addOutputSynapse(trainer);
        neuralNet = new NeuralNet();
        neuralNet.addLayer(inputLayer, NeuralNet.INPUT_LAYER);
        neuralNet.addLayer(hiddenLayer, NeuralNet.HIDDEN_LAYER);
        neuralNet.addLayer(outputLayer, NeuralNet.OUTPUT_LAYER);
    }

    protected void doSetNNInput() {
        input = nnInput.createInputSynapse();
        inputLayer.removeAllInputs();
        inputLayer.addInputSynapse(input);
        if (output != null) {
            outputLayer.removeOutputSynapse(output);
        }
        output = new MemoryOutputSynapse();
        outputLayer.addOutputSynapse(output);
        inputLayer.setRows(nnInput.getFetureNum());
        hiddenLayer.setRows(nnInput.getFetureNum() + nnInput.getCodeTypeNum());
        outputLayer.setRows(nnInput.getCodeTypeNum());
        MemoryInputSynapse memInpTeach = nnInput.createDesireSynapse();
        trainer.setDesired(memInpTeach);
    }
}
