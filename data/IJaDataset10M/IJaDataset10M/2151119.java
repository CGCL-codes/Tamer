package org.jactr.tools.async.message.event.data;

import java.io.Serializable;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.runtime.controller.debug.BreakpointType;
import org.jactr.tools.async.message.ast.BaseASTMessage;
import org.jactr.tools.async.message.event.IEvent;

/**
 * @author developer
 */
public class BreakpointReachedEvent extends BaseASTMessage implements IEvent, Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = 5706443530405417056L;

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(BreakpointReachedEvent.class);

    BreakpointType _type;

    String _modelName;

    double _simulationTime;

    public BreakpointReachedEvent(String modelName, BreakpointType type, double simulationTime, CommonTree details) {
        super(details);
        _simulationTime = simulationTime;
        _modelName = modelName;
        _type = type;
    }

    public double getSimulationTime() {
        return _simulationTime;
    }

    public String getModelName() {
        return _modelName;
    }

    public BreakpointType getBreakpointType() {
        return _type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName()).append(":");
        sb.append(_type).append(",").append(_modelName).append(",").append(getAST()).append("]");
        return sb.toString();
    }
}
