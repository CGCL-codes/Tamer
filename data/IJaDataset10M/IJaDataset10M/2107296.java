package es.optsicom.lib.tablecreator.pr;

import java.util.Arrays;
import java.util.List;
import es.optsicom.lib.experiment.Event;
import es.optsicom.lib.experiment.Execution;

public class CounterEventRP extends SummarizeRawProcessor {

    private String eventName;

    public CounterEventRP(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public List<String> getCookedEventsNames() {
        return Arrays.asList("count " + eventName);
    }

    @Override
    public double[] cookEvents(Execution exec) {
        int counter = 0;
        for (Event event : exec.getEvents()) {
            if (event.getName().equals(eventName)) {
                counter++;
            }
        }
        return new double[] { counter };
    }
}
