package org.modelibra.modeler.model.event;

/**
 * 
 * @author Dzenan Ridjanovic
 * @version 2001-05-07
 */
public class ModelEvent {

    private String opType;

    private String propertyName;

    private Object opArgument;

    public ModelEvent(String opType, String propertyName, Object opArgument) {
        this.opType = opType;
        this.propertyName = propertyName;
        this.opArgument = opArgument;
    }

    public String getOpType() {
        return opType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOpArgument() {
        return opArgument;
    }
}
