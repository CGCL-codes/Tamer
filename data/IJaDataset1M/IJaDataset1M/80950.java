package playground.droeder.ResizeLinksByCount;

import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.LinkImpl;
import org.matsim.counts.Count;
import org.matsim.counts.Counts;

public class ResizeLinksByCount3 extends AbstractResizeLinksByCount {

    private static final Logger log = Logger.getLogger(ResizeLinksByCount3.class);

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
