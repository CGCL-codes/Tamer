package playground.dgrether.koehlerstrehlersignal;

import java.util.Map;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.scenario.ScenarioImpl;
import playground.dgrether.koehlerstrehlersignal.data.DgCommodities;
import playground.dgrether.koehlerstrehlersignal.data.DgCommodity;
import playground.dgrether.koehlerstrehlersignal.data.DgNetwork;
import playground.dgrether.utils.DgZone;

/**
 * @author dgrether
 *
 */
public class DgMatsim2KoehlerStrehler2010Zones2Commodities implements DgMatsim2KoehlerStrehler2010DemandConverter {

    private Map<DgZone, Link> zones2LinkMap;

    public DgMatsim2KoehlerStrehler2010Zones2Commodities(Map<DgZone, Link> zones2LinkMap) {
        this.zones2LinkMap = zones2LinkMap;
    }

    @Override
    public DgCommodities convert(ScenarioImpl sc, DgNetwork dgNetwork) {
        DgCommodities coms = new DgCommodities();
        for (DgZone fromZone : this.zones2LinkMap.keySet()) {
            Link fromZoneLink = this.zones2LinkMap.get(fromZone);
            for (DgZone toZone : fromZone.getToRelationships().keySet()) {
                DgCommodity com = new DgCommodity(new IdImpl(fromZone.getId() + "_" + toZone.getId()));
                coms.addCommodity(com);
                Link toZoneLink = this.zones2LinkMap.get(toZone);
                com.addSourceNode(fromZoneLink.getToNode().getId(), fromZone.getToRelationships().get(toZone).doubleValue());
                com.addDrainNode(toZoneLink.getToNode().getId());
            }
        }
        return coms;
    }
}
