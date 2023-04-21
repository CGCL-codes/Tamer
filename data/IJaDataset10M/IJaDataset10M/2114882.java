package gameserver.model.templates.teleport;

import gameserver.model.Race;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "teleporter_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TeleporterTemplate {

    @XmlAttribute(name = "npc_id", required = true)
    private int npcId;

    @XmlAttribute(name = "name", required = true)
    private String name = "";

    @XmlAttribute(name = "teleportId", required = true)
    private int teleportId = 0;

    @XmlAttribute(name = "type", required = true)
    private TeleportType type;

    @XmlAttribute(name = "race")
    private Race race;

    @XmlElement(name = "locations")
    private TeleLocIdData teleLocIdData;

    /**
	 * @return the npcId
	 */
    public int getNpcId() {
        return npcId;
    }

    /**
	 * @return the name of npc
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the teleportId
	 */
    public int getTeleportId() {
        return teleportId;
    }

    /**
	 * @return the type
	 */
    public TeleportType getType() {
        return type;
    }

    /**
	 * @return the race
	 */
    public Race getRace() {
        return race;
    }

    /**
	 * @return the teleLocIdData
	 */
    public TeleLocIdData getTeleLocIdData() {
        return teleLocIdData;
    }
}
