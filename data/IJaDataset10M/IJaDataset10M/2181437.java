package ytex.kernel;

public class ClassifierEvaluationResult {

    int targetClassId;

    int predictedClassId;

    int instanceId;

    double[] probabilities;

    public int getTargetClassId() {
        return targetClassId;
    }

    public void setTargetClassId(int targetClassIndex) {
        this.targetClassId = targetClassIndex;
    }

    public int getPredictedClassId() {
        return predictedClassId;
    }

    public void setPredictedClassId(int predictedClassIndex) {
        this.predictedClassId = predictedClassIndex;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
    }
}
