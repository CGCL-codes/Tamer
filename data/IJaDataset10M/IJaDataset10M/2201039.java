package gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bind_points")
@XmlAccessorType(XmlAccessType.NONE)
public class BindPointTemplate {

    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "npcid")
    private int npcId;

    @XmlAttribute(name = "bindid")
    private int bindId;

    @XmlAttribute(name = "mapid")
    private int mapId = 0;

    @XmlAttribute(name = "posX")
    private float x = 0;

    @XmlAttribute(name = "posY")
    private float y = 0;

    @XmlAttribute(name = "posZ")
    private float z = 0;

    @XmlAttribute(name = "price")
    private int price = 0;

    public String getName() {
        return name;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getBindId() {
        return bindId;
    }

    public int getZoneId() {
        return mapId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public int getPrice() {
        return price;
    }
}
