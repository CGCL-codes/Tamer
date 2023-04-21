package playground.jbischoff.BAsignalsDemand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.signalsystems.MatsimSignalSystemsReader;
import org.matsim.signalsystems.data.signalsystems.v20.SignalData;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemData;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemsData;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemsDataImpl;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemsReader20;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemsWriter20;
import org.xml.sax.SAXException;
import playground.jbischoff.lsacvs2kml.LinkConversionsData;

public class SignalSystemsLinkConverter {

    static final String INPUTFILE = "/Users/JB/Documents/Work/cottbus/signalSystemsCottbusByNodes_v2.0.xml";

    static final String OUTPUTFILE = "/Users/JB/Documents/Work/cottbus/cottbus_20_jb/signalSystemsCottbusByNodes_v2.0_conv.xml";

    public static void main(String args0[]) throws JAXBException, SAXException, ParserConfigurationException, IOException {
        SignalSystemsData ssdata = new SignalSystemsDataImpl();
        SignalSystemsData newssdata = new SignalSystemsDataImpl();
        SignalSystemsReader20 ssread = new SignalSystemsReader20(ssdata, MatsimSignalSystemsReader.SIGNALSYSTEMS20);
        ssread.readFile(INPUTFILE);
        Map<Id, LinkConversionsData> convmap = readConv("/Users/JB/Desktop/BA-Arbeit/sim/convdata.csv");
        for (SignalSystemData ssd : ssdata.getSignalSystemData().values()) {
            try {
                SignalSystemData nssd = newssdata.getFactory().createSignalSystemData(ssd.getId());
                for (SignalData sd : ssd.getSignalData().values()) {
                    SignalData nsd = newssdata.getFactory().createSignalData(sd.getId());
                    nsd.setLinkId(convmap.get(ssd.getId()).getConv(sd.getLinkId()));
                    System.out.println(nsd.getLinkId());
                    for (Id lid : sd.getLaneIds()) {
                        nsd.addLaneId(lid);
                    }
                    nssd.addSignalData(nsd);
                }
                newssdata.addSignalSystemData(nssd);
            } catch (NullPointerException e) {
                System.err.println("Can't find Conversion for " + ssd.getId() + " , will skip this one.");
            }
        }
        SignalSystemsWriter20 writer = new SignalSystemsWriter20(newssdata);
        writer.write(OUTPUTFILE);
    }

    public static Map<Id, LinkConversionsData> readConv(String filename) {
        Map<Id, LinkConversionsData> cdata = new HashMap<Id, LinkConversionsData>();
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(new File(filename));
            br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] result = line.split(";");
                Id ssid = new IdImpl(result[0]);
                LinkConversionsData lcd;
                if (cdata.get(ssid) == null) {
                    lcd = new LinkConversionsData(ssid);
                    cdata.put(ssid, lcd);
                }
                cdata.get(ssid).setConv(new IdImpl(result[1]), new IdImpl(result[2]));
            }
            System.out.println(cdata.get(new IdImpl("17")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cdata;
    }
}
