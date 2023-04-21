package org.jactr.tools.async.iterative.message;

import java.io.Serializable;
import org.jactr.core.model.IModel;
import org.jactr.tools.async.message.BaseMessage;

/**
 * @author developer
 */
public class ExceptionMessage extends BaseMessage implements Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = -8801142959562498658L;

    private String _modelName;

    private int _iteration;

    private Throwable _thrown;

    public ExceptionMessage(int iteration, IModel model, Throwable thrown) {
        super();
        _iteration = iteration;
        if (model != null) _modelName = model.getName(); else _modelName = "";
        _thrown = thrown;
    }

    public int getIteration() {
        return _iteration;
    }

    public String getModelName() {
        return _modelName;
    }

    public Throwable getThrown() {
        return _thrown;
    }
}
