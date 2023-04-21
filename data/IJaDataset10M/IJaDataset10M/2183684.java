package org.jactr.tools.async.message.event.state;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.model.IModel;
import org.jactr.tools.async.message.BaseMessage;

/**
 * @author developer
 */
public class RuntimeStateEvent extends BaseMessage implements IRuntimeStateEvent, Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = 5141289381492005562L;

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(RuntimeStateEvent.class);

    private State _state;

    private Collection<String> _modelNames;

    long _systemTime;

    double _simulationTime;

    private String _exception;

    public RuntimeStateEvent(State state, double simulationTime) {
        _state = state;
        _simulationTime = simulationTime;
        _systemTime = System.currentTimeMillis();
    }

    public RuntimeStateEvent(Collection<IModel> models, double simulationTime) {
        this(State.STARTED, simulationTime);
        _modelNames = new ArrayList<String>();
        for (IModel model : models) _modelNames.add(model.getName());
    }

    public RuntimeStateEvent(Exception exception, double simulationTime) {
        this(State.STOPPED, simulationTime);
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        writer.flush();
        _exception = writer.toString();
    }

    public State getState() {
        return _state;
    }

    public String getException() {
        return _exception;
    }

    public Collection<String> getModelNames() {
        return _modelNames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName()).append(":").append(_state).append("]");
        return sb.toString();
    }

    /**
   * @see org.jactr.tools.async.message.command.state.IStateCommand#getSimulationTime()
   */
    public double getSimulationTime() {
        return _simulationTime;
    }

    /**
   * @see org.jactr.tools.async.message.command.state.IStateCommand#getSystemTime()
   */
    public long getSystemTime() {
        return _systemTime;
    }
}
