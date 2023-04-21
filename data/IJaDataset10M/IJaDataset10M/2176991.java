package es.optsicom.lib.tablecreator.pr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import es.optsicom.lib.experiment.Event;
import es.optsicom.lib.experiment.Execution;
import es.optsicom.lib.util.SummarizeMode;

public class SummarizeExecRP extends SummarizeRawProcessor {

    public SummarizeMode execSummarizeMode;

    public String eventName;

    public SummarizeExecRP(String eventName, SummarizeMode execSummarizeMode) {
        this.execSummarizeMode = execSummarizeMode;
        this.eventName = eventName;
    }

    @Override
    public double[] cookEvents(Execution exec) {
        List<Number> values = new ArrayList<Number>();
        for (Event event : exec.getEvents()) {
            if (event.getName().equals(eventName)) {
                values.add((Number) event.getValue());
            }
        }
        return new double[] { summarizeMode.summarizeValues(values) };
    }

    @Override
    public List<String> getCookedEventsNames() {
        return Arrays.asList(execSummarizeMode + " of " + eventName);
    }
}
