package com.ibm.tuningfork.infra.stream.core;

import com.ibm.tuningfork.infra.data.ITimedData;
import com.ibm.tuningfork.infra.event.EventType;
import com.ibm.tuningfork.infra.event.IEvent;
import com.ibm.tuningfork.infra.stream.IEventSummarizer;
import com.ibm.tuningfork.infra.stream.MultipleInputStreamManager;
import com.ibm.tuningfork.infra.stream.expression.StreamOperand;

public class SummarizingEventStream extends EventStream {

    protected final Stream[] inputs;

    protected final IEventSummarizer summarizer;

    public SummarizingEventStream(String name, Stream[] inputs, IEventSummarizer summarizer, EventType eventType) {
        super(name, summarizer.getName(), StreamOperand.makeOperandList(inputs), eventType);
        this.inputs = inputs;
        this.summarizer = summarizer;
    }

    public SummarizingEventStream(String name, Stream input, IEventSummarizer summarizer, EventType eventType) {
        this(name, new Stream[] { input }, summarizer, eventType);
    }

    public SummarizingEventStream(String name, Stream[] inputs, IEventSummarizer summarizer) {
        this(name, inputs, summarizer, summarizer.getSummaryEventType());
    }

    public SummarizingEventStream(String name, Stream input, IEventSummarizer summarizer) {
        this(name, new Stream[] { input }, summarizer, summarizer.getSummaryEventType());
    }

    public void derivedRun() {
        MultipleInputStreamManager manager = new MultipleInputStreamManager(this, inputs).start();
        while (true) {
            ITimedData current = manager.getNext();
            if (current == null) {
                close();
                return;
            }
            summarizer.addToSummary(current, inputs[manager.getCurrentStream()]);
            processEvent(current);
        }
    }

    protected void processEvent(ITimedData current) {
        IEvent summaryEvent = summarizer.getSummary();
        if (summaryEvent != null) {
            addEventToStream(summaryEvent);
        }
    }

    public IEventSummarizer getSummarizer() {
        return summarizer;
    }
}
