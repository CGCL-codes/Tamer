package playground.dgrether.signalsystems.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.matsim.api.core.v01.Id;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.io.UncheckedIOException;
import org.matsim.signalsystems.model.SignalGroupState;

/**
 * @author dgrether
 *
 */
public class DgGreenSplitWriter {

    private static final String SEPARATOR = "\t";

    private DgSignalGreenSplitHandler greenSplitHandler;

    public DgGreenSplitWriter(DgSignalGreenSplitHandler signalGreenSplitHandler) {
        this.greenSplitHandler = signalGreenSplitHandler;
    }

    public static String createHeader() {
        return "Signal System Id" + SEPARATOR + "Signal Group Id" + SEPARATOR + "Signal Group State" + SEPARATOR + "Time sec.";
    }

    public void writeFile(String filename) {
        try {
            BufferedWriter writer = IOUtils.getBufferedWriter(filename);
            String header = createHeader();
            writer.append(header);
            for (Id ssid : greenSplitHandler.getSystemIdAnalysisDataMap().keySet()) {
                Map<Id, DgSignalGroupAnalysisData> signalGroupMap = greenSplitHandler.getSystemIdAnalysisDataMap().get(ssid).getSystemGroupAnalysisDataMap();
                for (Entry<Id, DgSignalGroupAnalysisData> entry : signalGroupMap.entrySet()) {
                    for (Entry<SignalGroupState, Double> ee : entry.getValue().getStateTimeMap().entrySet()) {
                        StringBuilder line = new StringBuilder();
                        line.append(ssid);
                        line.append(SEPARATOR);
                        line.append(entry.getKey());
                        line.append(SEPARATOR);
                        line.append(ee.getKey());
                        line.append(SEPARATOR);
                        line.append(ee.getValue());
                        writer.append(line.toString());
                        writer.newLine();
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
