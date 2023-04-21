package org.aplikator.client.rpc.impl;

import org.aplikator.client.data.RecordContainer;
import org.aplikator.client.rpc.Response;

@SuppressWarnings("serial")
public class ProcessRecordsResponse implements Response {

    private RecordContainer container;

    @SuppressWarnings("unused")
    private ProcessRecordsResponse() {
    }

    public ProcessRecordsResponse(RecordContainer c) {
        container = c;
    }

    public RecordContainer getRecordContainer() {
        return container;
    }
}
