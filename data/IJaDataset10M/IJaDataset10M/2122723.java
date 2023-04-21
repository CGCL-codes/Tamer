package playground.droeder.osm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.network.LinkImpl;
import org.matsim.counts.Count;
import org.matsim.counts.Counts;
import org.matsim.counts.CountsReaderMatsimV1;
import org.matsim.counts.CountsWriter;
import org.matsim.counts.Volume;
import org.xml.sax.SAXException;
import playground.andreas.osmBB.osm2counts.Osm2Counts;

public class ResizeLinksByCount3 extends AbstractResizeLinksByCount {

    private static final Logger log = Logger.getLogger(ResizeLinksByCount3.class);

    public static void main(String[] args) {
        String countsFile = "d:/VSP/output/osm_bb/Di-Do_counts.xml";
        String filteredOsmFile = "d:/VSP/output/osm_bb/counts.osm";
        String networkFile = "d:/VSP/output/osm_bb/counts_network.xml";
        Osm2Counts osm2Counts = new Osm2Counts(filteredOsmFile);
        osm2Counts.prepareOsm();
        HashMap<String, String> shortNameMap = osm2Counts.getShortNameMap();
        Counts counts = new Counts();
        CountsReaderMatsimV1 countsReader = new CountsReaderMatsimV1(counts);
        try {
            countsReader.parse(countsFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResizeLinksByCount3 r = new ResizeLinksByCount3(networkFile, counts, shortNameMap, 1.0);
        r.run("d:/VSP/output/osm_bb/network_resized.xml");
    }

    /**
	 * use this contructor if the counts loc_Ids are NOT matched to the linkIds. The shortNameMap 
	 * consists of  toNodeIds mapped to counts cs_Ids!  
	 * @param networkFile
	 * @param counts
	 * @param shortNameMap
	 */
    public ResizeLinksByCount3(String networkFile, Counts counts, Map<String, String> shortNameMap, Double scaleFactor) {
        super(networkFile, counts, shortNameMap, scaleFactor);
    }

    /**
	 * use this constructor if counts loc_Ids and linkIds are matched!
	 * @param networkFile
	 * @param counts
	 */
    public ResizeLinksByCount3(String networkFile, Counts counts, Double scaleFactor) {
        super(networkFile, counts, scaleFactor);
    }

    public void run(String outFile) {
        super.run(outFile);
    }

    @Override
    protected void resize() {
        Double maxCount;
        String origId;
        Integer nrOfNewLanes;
        LinkImpl countLink;
        Double capPerLane;
        for (Count c : this.getRescaledCounts().getCounts().values()) {
            countLink = getOriginalLink(c.getLocId());
            origId = countLink.getOrigId();
            maxCount = c.getMaxVolume().getValue();
            for (Link l : this.getOrigNetwork().getLinks().values()) {
                if (((LinkImpl) l).getOrigId().equals(origId)) {
                    capPerLane = l.getCapacity() / l.getNumberOfLanes();
                    if (maxCount < l.getCapacity()) {
                        nrOfNewLanes = (int) l.getNumberOfLanes();
                    } else {
                        nrOfNewLanes = (int) (maxCount / capPerLane);
                    }
                    System.out.print(l.getId() + " " + l.getCapacity() + " " + l.getNumberOfLanes() + " " + maxCount + " " + nrOfNewLanes);
                    System.out.println();
                    this.setNewLinkData(l.getId(), maxCount, nrOfNewLanes);
                    this.addLink2shp(l.getId());
                }
            }
        }
        log.info("resizing finished!!!");
    }
}
