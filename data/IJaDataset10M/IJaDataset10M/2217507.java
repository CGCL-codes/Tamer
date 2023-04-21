package com.ohua.etl.operators;

import com.ohua.checkpoint.framework.operatorcheckpoints.AbstractCheckPoint;
import com.ohua.checkpoint.framework.operatorcheckpoints.EmptyCheckpoint;
import com.ohua.engine.data.model.daapi.InputPortControl;
import com.ohua.engine.data.model.daapi.OutputPortControl;
import com.ohua.engine.flowgraph.elements.operator.UserOperator;

public abstract class AbstractAggregateFunctionOperator extends UserOperator {

    public static class AggregateProperties {

        public String path = null;
    }

    protected InputPortControl _inControl = null;

    protected OutputPortControl _outControl = null;

    protected Object _state = null;

    public AggregateProperties _properties = null;

    private boolean _sentResult = false;

    @Override
    public void prepare() {
        _inControl = getDataLayer().getInputPortController("input");
        _outControl = getDataLayer().getOutputPortController("output");
    }

    @Override
    public void runProcessRoutine() {
        while (_inControl.next()) {
            Object current = _inControl.getData(_properties.path).get(0);
            if (_state == null) {
                _state = current;
            } else {
                int compare = getDataLayer().getDataUtils().compare(current, _state);
                if (replaceState(compare)) {
                    _state = current;
                }
            }
        }
        if (_inControl.hasSeenLastPacket() && !_sentResult) {
            sendResult();
            _sentResult = true;
        }
    }

    protected abstract boolean replaceState(int compareResult);

    private void sendResult() {
        _outControl.newPacket();
        _outControl.setData("agg-result", _state);
        _outControl.send();
    }

    @Override
    public void cleanup() {
    }

    public AbstractCheckPoint getState() {
        EmptyCheckpoint cp = new EmptyCheckpoint();
        cp.attachAdditionalInformation("agg-state", _state);
        return cp;
    }

    public void setState(AbstractCheckPoint checkpoint) {
        _state = checkpoint.retrieveAdditionalInformation("agg-state");
    }
}
