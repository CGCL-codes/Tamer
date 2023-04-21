package com.wrupple.muba.common.shared.process.impl;

import com.wrupple.muba.common.shared.Process;
import com.wrupple.muba.common.shared.SequentialProcess;
import com.wrupple.muba.common.shared.State;

public class ProcessWrapper<I, O> extends SequentialProcess<I, O> {

    public <E, S> ProcessWrapper(State<I, E> initialConvert, Process<E, S> wrapped, State<S, O> finalConvert) {
        add(initialConvert);
        addAll(wrapped);
        add(finalConvert);
    }
}
