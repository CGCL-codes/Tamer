package playground.ciarif.retailers.stategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.facilities.ActivityFacility;
import org.matsim.core.controler.Controler;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.LinkImpl;
import playground.ciarif.retailers.data.LinkRetailersImpl;
import playground.ciarif.retailers.utils.Utils;

public class MaxLinkRetailerStrategy implements RetailerStrategy {

    private static final Logger log = Logger.getLogger(MaxLinkRetailerStrategy.class);

    public static final String NAME = "maxLinkRetailerStrategy";

    private Controler controler;

    private Map<Id, ActivityFacility> movedFacilities;

    public MaxLinkRetailerStrategy(Controler controler) {
        this.controler = controler;
    }

    public Map<Id, ActivityFacility> moveFacilities(Map<Id, ActivityFacility> facilities, ArrayList<LinkRetailersImpl> allowedLinks) {
        for (ActivityFacility f : facilities.values()) {
            int rd = MatsimRandom.getRandom().nextInt(allowedLinks.size());
            Link link = allowedLinks.get(rd);
            log.info("The link " + link.getId() + " is proposed as new location for the facility " + f.getId());
            controler.getLinkStats().addData(controler.getVolumes(), controler.getTravelTimeCalculator());
            double[] currentlink_volumes = controler.getLinkStats().getAvgLinkVolumes(f.getLinkId());
            double[] newlink_volumes = controler.getLinkStats().getAvgLinkVolumes(link.getId());
            double currentlink_volume = 0;
            double newlink_volume = 0;
            for (int j = 0; j < currentlink_volumes.length; j = j + 1) {
                currentlink_volume = currentlink_volume + currentlink_volumes[j];
            }
            for (int j = 0; j < newlink_volumes.length; j = j + 1) {
                newlink_volume = newlink_volume + newlink_volumes[j];
            }
            Link fLink = this.controler.getNetwork().getLinks().get(f.getLinkId());
            Collection<Person> persons_actual = Utils.getPersonQuadTree().get(fLink.getCoord().getX(), fLink.getCoord().getY(), 150);
            Collection<Person> persons_new = Utils.getPersonQuadTree().get(link.getCoord().getX(), link.getCoord().getY(), 150);
            Collection<ActivityFacility> facilities_actual = Utils.getFacilityQuadTree().get(f.getCoord().getX(), f.getCoord().getY(), 150);
            Collection<ActivityFacility> facilities_new = Utils.getFacilityQuadTree().get(link.getCoord().getX(), link.getCoord().getY(), 150);
            boolean move_facilities = false;
            for (ActivityFacility f1 : facilities_new) {
                if (facilities.values().contains(f1)) {
                    log.info("Around the proposed new link a facility of this retailer already exists ");
                    break;
                } else {
                    move_facilities = true;
                }
            }
            if (move_facilities == true) {
                if (persons_actual.size() / facilities_actual.size() < persons_new.size() / facilities_new.size()) {
                    log.info("Persons living around the actual location = " + persons_actual.size());
                    log.info("Persons living around the new location = " + persons_new.size());
                    log.info("Facilities in the actual area = " + facilities_actual.size());
                    log.info("Facilities in the new area = " + facilities_new.size());
                    log.info("Ratio Persons/Facilities in the actual area = " + persons_actual.size() / facilities_actual.size());
                    log.info("Ratio Persons/Facilities in the new area = " + persons_new.size() / facilities_new.size());
                    log.info("facility = " + f.getId());
                    log.info("currentlink = " + f.getLinkId());
                    log.info("currentlink_volume = " + currentlink_volume);
                    log.info("newlink_volume = " + newlink_volume);
                } else {
                    move_facilities = false;
                }
            }
            if (move_facilities == true) {
                if (newlink_volume >= currentlink_volume) {
                } else {
                    move_facilities = false;
                }
            }
            if (move_facilities == true) {
                Utils.moveFacility((ActivityFacilityImpl) f, link);
                this.movedFacilities.put(f.getId(), f);
            } else {
                log.info("The facility " + f.getId() + " will stay at the current link");
            }
        }
        return this.movedFacilities;
    }

    public ArrayList<LinkRetailersImpl> findAvailableLinks() {
        return null;
    }

    public Map<Id, ActivityFacility> moveFacilities(Map<Id, ActivityFacility> facilities, Map<Id, LinkRetailersImpl> links) {
        return null;
    }

    @Override
    public Map<Id, ActivityFacilityImpl> moveFacilities(Map<Id, ActivityFacilityImpl> paramMap, TreeMap<Id, LinkRetailersImpl> paramTreeMap) {
        return null;
    }
}
