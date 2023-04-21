package com.erlang4j.internal.adapters;

import com.erlang4j.api.IGetData;
import com.erlang4j.api.process.IProcessWithState;

public class MockAdapterWithException extends MockAdapter {

    private final Exception exception;

    public MockAdapterWithException(String pattern, Exception exception) {
        super(pattern);
        this.exception = exception;
    }

    @Override
    public void process(IProcessWithState process, IGetData data) throws Exception {
        super.process(process, data);
        throw exception;
    }
}
