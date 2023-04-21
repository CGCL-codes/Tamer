package visualgraph.graph.attributed;

public interface AttributeValue {

    public void copyData(AttributeValue sourceData, AttributeValue targetData);

    public void interpolateData(AttributeValue startData, AttributeValue endData, float progress, AttributeValue interpolatedData);
}
