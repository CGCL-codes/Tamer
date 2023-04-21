package com.jetigy.tach.analyzation.event.tmp;

import java.util.ArrayList;
import java.util.List;
import com.jetigy.magicbus.event.bus.ChannelEventType;
import com.jetigy.magicbus.event.bus.ChanneledEvent;
import com.jetigy.tach.common.domain.DataMetric;

public class TMPStateEvent extends ChanneledEvent {

    private DataMetric metric;

    public TMPStateEvent(Object arg0, DataMetric metric, ChannelEventType arg1) {
        super(arg0, arg1);
        this.metric = metric;
    }

    public DataMetric getMetric() {
        return metric;
    }

    public static class Type extends ChannelEventType {

        public static final Type TMP_DATA_RECEIVED = new Type("TMP_DATA_RECEIVED", "dataReceived");

        public static List<ChannelEventType> getTypes() {
            List<ChannelEventType> list = new ArrayList<ChannelEventType>();
            list.add(TMP_DATA_RECEIVED);
            return list;
        }

        public Type(String arg0, String arg1) {
            super(arg0, arg1);
        }
    }
}
