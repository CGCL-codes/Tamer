package playground.droeder.Analysis.handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;
import org.matsim.core.api.experimental.events.handler.AgentArrivalEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentDepartureEventHandler;
import org.matsim.core.utils.io.IOUtils;

/**
 * @author droeder
 *
 */
public class PopulationAnalysisHandler implements AgentDepartureEventHandler, AgentArrivalEventHandler {

    private Map<Id, Double> observer;

    private Map<String, Double> mode2TT;

    private Map<String, Integer> mode2legCnt;

    public PopulationAnalysisHandler() {
        this.observer = new HashMap<Id, Double>();
        this.mode2TT = new HashMap<String, Double>();
        this.mode2legCnt = new HashMap<String, Integer>();
    }

    @Override
    public void reset(int iteration) {
    }

    @Override
    public void handleEvent(AgentArrivalEvent event) {
        if (this.observer.containsKey(event.getPersonId())) {
            double tt;
            int cnt;
            if (this.mode2TT.containsKey(event.getLegMode())) {
                tt = this.mode2TT.get(event.getLegMode());
                cnt = this.mode2legCnt.get(event.getLegMode());
            } else {
                tt = 0.;
                cnt = 0;
            }
            tt = tt + event.getTime() - observer.get(event.getPersonId());
            cnt++;
            this.mode2TT.put(event.getLegMode(), tt);
            this.mode2legCnt.put(event.getLegMode(), cnt);
            this.observer.remove(event.getPersonId());
        }
    }

    @Override
    public void handleEvent(AgentDepartureEvent event) {
        this.observer.put(event.getPersonId(), event.getTime());
    }

    public void dumpCsv(String outDir, Double scaleFactor) {
        BufferedWriter writer = IOUtils.getBufferedWriter(outDir + "legTT.csv");
        try {
            writer.write("mode;sumTT;sumLegCnt;avLegTT;");
            writer.newLine();
            double tt, cnt, avTT;
            for (Entry<String, Double> e : this.mode2TT.entrySet()) {
                tt = (e.getValue() / scaleFactor);
                cnt = (this.mode2legCnt.get(e.getKey()) / scaleFactor);
                avTT = (e.getValue() / this.mode2legCnt.get(e.getKey()));
                writer.write(e.getKey() + ";" + tt + ";" + cnt + ";" + avTT + ";");
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
