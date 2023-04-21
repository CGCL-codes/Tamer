package darwin.common.math.nodeType;

import darwin.problemObject.NodeType;
import darwin.problemObject.Processable;

public class Multiply extends NodeType {

    @Override
    public Class<?>[] getChildTypes() {
        return new Class<?>[] { Double.class, Double.class };
    }

    @Override
    public Processable<?> getProcessable() {
        return new darwin.common.math.processable.Multiply();
    }

    @Override
    public Class<?> getReturnType() {
        return Double.class;
    }
}
