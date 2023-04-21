package org.openaion.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.templates.siege.SiegeLocationTemplate;

/**
 * @author Sarynth
 */
@XmlRootElement(name = "siege_locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeLocationData {

    @XmlElement(name = "siege_location")
    private List<SiegeLocationTemplate> siegeLocationTemplates;

    /**
	 *  Map that contains skillId - SkillTemplate key-value pair
	 */
    private HashMap<Integer, SiegeLocation> siegeLocations = new HashMap<Integer, SiegeLocation>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        Logger.getLogger(SiegeLocationData.class).debug("After unmarshal in SiegeLocationData...");
        siegeLocations.clear();
        for (SiegeLocationTemplate template : siegeLocationTemplates) {
            Logger.getLogger(SiegeLocationData.class).debug("[" + siegeLocations.size() + "/" + siegeLocationTemplates.size() + "]Loading SiegeLocation #" + template.getId() + " with type " + template.getType());
            switch(template.getType()) {
                case FORTRESS:
                    siegeLocations.put(template.getId(), new SiegeLocation(template));
                    break;
                case ARTIFACT:
                    siegeLocations.put(template.getId(), new SiegeLocation(template));
                    break;
                case BOSSRAID_LIGHT:
                case BOSSRAID_DARK:
                    siegeLocations.put(template.getId(), new SiegeLocation(template));
                    break;
                default:
                    break;
            }
            Logger.getLogger(SiegeLocationData.class).debug("now there is " + siegeLocations.size() + " sieges locations...");
        }
    }

    public int size() {
        return siegeLocations.size();
    }

    public Map<Integer, SiegeLocation> getSiegeLocations() {
        return siegeLocations;
    }
}
