package gameserver.model.templates.academy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = javax.xml.bind.annotation.XmlAccessType.FIELD)
@XmlType(name = "end")
public class AcademyShigoEndSpawnList {

    public AcademyShigoEndSpawnList() {
    }

    public int getMapid() {
        return mapid;
    }

    public int getNpcid() {
        return npcid;
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

    public byte getHeading() {
        return heading;
    }

    @XmlAttribute(name = "map")
    private int mapid;

    @XmlAttribute(name = "npcid")
    private int npcid;

    @XmlAttribute(name = "x")
    private float x;

    @XmlAttribute(name = "y")
    private float y;

    @XmlAttribute(name = "z")
    private float z;

    @XmlAttribute(name = "h")
    private byte heading;
}
