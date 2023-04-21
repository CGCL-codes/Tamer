package imageCompression;

public class DarwinSheet implements darwin.problemObject.DarwinSheet {

    @Override
    public darwin.problemObject.FitnessAnalyzer getFitnessAnalyzer() {
        return new FitnessAnalyzer();
    }

    @Override
    public darwin.problemObject.NodeFactory getNodeFactory() {
        return new NodeFactory();
    }

    @Override
    public darwin.problemObject.Output getOutput() {
        return new Output();
    }
}
