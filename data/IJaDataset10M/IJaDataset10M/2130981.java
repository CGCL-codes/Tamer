package gameserver.dataholders;

import gameserver.model.templates.zone.ZoneTemplate;
import gameserver.world.zone.ZoneName;
import gnu.trove.THashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "flight_zones")
public class FlightZoneData implements Iterable<ZoneTemplate> {

    @XmlElement(name = "flight_zone")
    protected List<ZoneTemplate> flightZoneList;

    private THashMap<ZoneName, ZoneTemplate> zoneNameMap = new THashMap<ZoneName, ZoneTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ZoneTemplate zone : flightZoneList) {
            zoneNameMap.put(zone.getName(), zone);
        }
    }

    @Override
    public Iterator<ZoneTemplate> iterator() {
        return flightZoneList.iterator();
    }

    public int size() {
        return flightZoneList.size();
    }
}
