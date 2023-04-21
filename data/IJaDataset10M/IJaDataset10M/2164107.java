package com.ibm.tuningfork.infra.stream.core;

import com.ibm.tuningfork.infra.event.EventType;
import com.ibm.tuningfork.infra.feed.IDataSource;
import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;
import com.ibm.tuningfork.infra.stream.expression.StreamOperand;
import com.ibm.tuningfork.infra.units.Unit;

/**
 * A stream of functions implemented via callbacks.
 */
public class FunctionStream extends EventStream {

    public FunctionStream(String name, String operator, StreamOperand[] operands, Unit unit, EventType type) {
        super(name, operator, operands, unit, type);
    }

    public FunctionStream(String name, IDataSource[] ds, Unit unit, EventType type) {
        super(name, ds, unit, type);
        streamMode = StreamMode.MANUAL;
    }

    @Override
    public void collectSpecificReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        collectAllButLastReconstructionArguments(cb);
        cb.convert(unit);
    }

    /**
     *
     */
    public EventType getEventType() {
        EventType type = super.getEventType();
        if (type == null) {
            throw new RuntimeException("No Event Type is available for this function stream");
        }
        return type;
    }
}
