package org.openaion.gameserver.dataholders;

import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.openaion.gameserver.model.templates.drops.NpcDrop;

/**
 * @author Sylar
 */
@XmlRootElement(name = "droplist")
@XmlAccessorType(XmlAccessType.FIELD)
public class DroplistData {

    @XmlElement(name = "npcdrop")
    protected List<NpcDrop> npcDrops;

    void afterUnmarshal(Unmarshaller u, Object parent) {
    }

    public List<NpcDrop> getDrops() {
        return npcDrops;
    }

    public int size() {
        return npcDrops.size();
    }
}
