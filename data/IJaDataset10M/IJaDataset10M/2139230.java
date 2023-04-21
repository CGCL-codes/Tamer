package net.sf.stbot.model;

import javax.xml.bind.annotation.XmlAttribute;

public class UpgradeResources {

    @XmlAttribute
    private int lumberToLevel = 10;

    @XmlAttribute
    private int clayToLevel = 10;

    @XmlAttribute
    private int ironToLevel = 10;

    @XmlAttribute
    private int cropToLevel = 10;

    public int getMaxLevelFor(Resource resource) {
        switch(resource) {
            case LUMBER:
                return lumberToLevel;
            case CLAY:
                return clayToLevel;
            case IRON:
                return ironToLevel;
            default:
                return cropToLevel;
        }
    }
}
