package org.matsim.core.facilities.algorithms;

import java.util.Iterator;
import java.util.TreeSet;
import org.matsim.api.basic.v01.Coord;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.facilities.Facilities;
import org.matsim.core.api.facilities.Facility;

public class FacilitiesScenarioCut {

    private final double minX;

    private final double maxX;

    private final double minY;

    private final double maxY;

    public FacilitiesScenarioCut(final Coord min, final Coord max) {
        super();
        this.minX = min.getX();
        this.maxX = max.getX();
        this.minY = min.getY();
        this.maxY = max.getY();
    }

    public void run(Facilities facilities) {
        System.out.println("    running " + this.getClass().getName() + " module...");
        TreeSet<Id> fid_set = new TreeSet<Id>();
        Iterator<Id> fid_it = facilities.getFacilities().keySet().iterator();
        while (fid_it.hasNext()) {
            Id fid = fid_it.next();
            Facility f = facilities.getFacilities().get(fid);
            Coord coord = f.getCoord();
            double x = coord.getX();
            double y = coord.getY();
            if (!((x < this.maxX) && (this.minX < x) && (y < this.maxY) && (this.minY < y))) {
                fid_set.add(fid);
            }
        }
        System.out.println("      Number of facilities to be cut = " + fid_set.size() + "...");
        fid_it = fid_set.iterator();
        while (fid_it.hasNext()) {
            Id fid = fid_it.next();
            facilities.getFacilities().remove(fid);
        }
        System.out.println("    done.");
    }
}
