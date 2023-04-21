package playground.gregor.evacuation.outflowanalysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.matsim.core.events.AgentArrivalEvent;
import org.matsim.core.events.Events;
import org.matsim.core.events.EventsReaderTXTv1;
import org.matsim.core.events.handler.AgentArrivalEventHandler;
import org.matsim.core.utils.io.IOUtils;

public class OutFlowRate implements AgentArrivalEventHandler {

    private final String events;

    private int time = 0;

    private int evacuated = 0;

    private ArrayList<Integer> outflow;

    private BufferedWriter out = null;

    private int offset;

    public OutFlowRate(final String input, final String output) {
        this.events = input;
        try {
            this.out = IOUtils.getBufferedWriter(output, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() {
        this.outflow = new ArrayList<Integer>();
        Events e = new Events();
        e.addHandler(this);
        new EventsReaderTXTv1(e).readFile(this.events);
        finish();
    }

    public void handleEvent(final AgentArrivalEvent event) {
        this.evacuated++;
        if (this.time == 0) {
            this.offset = (int) event.getTime();
            this.time = 1;
            writeLine(new String[] { this.time / 60 + "", this.evacuated + "" });
        }
        if ((event.getTime() - this.offset) > this.time && ((int) (event.getTime() - this.offset) % 180 == 0)) {
            this.time = (int) event.getTime() - this.offset;
            writeLine(new String[] { this.time / 60 + "", this.evacuated + "" });
        }
    }

    public void writeLine(final String[] line) {
        StringBuffer buff = new StringBuffer();
        buff.append(line[0]);
        for (int i = 1; i < line.length; i++) {
            buff.append("\t");
            buff.append(line[i]);
        }
        buff.append("\n");
        try {
            this.out.write(buff.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) {
        String baseDir = "../../outputs/output_delay/ITERS";
        String baseOutput = "../../outputs/output_delay/analysis";
        ArrayList<Integer> its = new ArrayList<Integer>();
        its.add(200);
        for (int it : its) {
            String input = baseDir + "/it." + it + "/" + it + ".events.txt.gz";
            String output = baseOutput + "/it." + it + ".outflow.txt";
            new OutFlowRate(input, output).run();
        }
    }

    public void reset(final int iteration) {
    }
}
