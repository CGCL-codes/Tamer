package nl.utwente.ewi.stream.network.functors;

import java.util.List;
import java.util.Map;
import nl.utwente.ewi.stream.network.attributes.BufferData;
import nl.utwente.ewi.stream.network.attributes.StreamElement;
import nl.utwente.ewi.stream.network.dbcp.DBType;

public interface AbstractFunctor {

    public Map<String, DBType> getOutputSchema(Map<String, DBType> parentSchema);

    public List<StreamElement> processData(BufferData bufferData);

    public void validateFunctor(List<String> errors, Map<String, DBType> parentSchema);
}
