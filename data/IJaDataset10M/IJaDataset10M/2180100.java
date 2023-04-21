package com.ibm.tuningfork.core.streamgui;

import com.ibm.tuningfork.infra.stream.ConcatenationEventStream;
import com.ibm.tuningfork.infra.stream.core.EventStream;
import com.ibm.tuningfork.infra.stream.core.Stream;

public class ConcatenationEventStreamGUI extends EventStreamGUI {

    public Class<?> streamClass() {
        return ConcatenationEventStream.class;
    }

    public Class<?>[] consumes() {
        return new Class[] { EventStream.class };
    }

    public String getOperatorName() {
        return "||";
    }

    public ConcatGroup createWidgetGroupHandler(IStreamGUIHost host) {
        return new ConcatGroup(host);
    }

    private class ConcatGroup extends StreamPairGroup {

        public ConcatGroup(IStreamGUIHost host) {
            super(host, "Concatenation of Event Streams", ConcatenationEventStream.class, EventStream.class);
        }

        protected Stream createStream(Stream thatStream) {
            EventStream primary = (EventStream) host.getBaseStream();
            EventStream secondary = (EventStream) thatStream;
            return new ConcatenationEventStream(primary, secondary);
        }
    }
}
