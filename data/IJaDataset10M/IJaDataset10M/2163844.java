package darwin.common.logic.nodeType;

import darwin.problemObject.NodeType;
import darwin.problemObject.Processable;

public class LessThanOrEqualTo extends NodeType {

    @Override
    public Class<?>[] getChildTypes() {
        return new Class<?>[] { Double.class, Double.class };
    }

    @Override
    public Processable<?> getProcessable() {
        return new darwin.common.logic.processable.LessThanOrEqualTo();
    }

    @Override
    public Class<?> getReturnType() {
        return Boolean.class;
    }
}
