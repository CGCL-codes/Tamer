package playground.andreas.utils.ana.point2kml;

import org.matsim.api.core.v01.Coord;

public class XYPointData {

    private String headline;

    private String description;

    private Coord coord;

    public String getHeadline() {
        return this.headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coord getCoord() {
        return this.coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
