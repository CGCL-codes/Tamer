package Watermill.geo;

import Watermill.common.AbstractInfo;
import Watermill.interfaces.Info;
import Watermill.interfaces.Manager;
import Watermill.kernel.WatermillException;
import fr.tadorne.watergoat.Filter;
import fr.tadorne.watergoat.filters.DouglasPeucker;
import fr.tadorne.watergoat.interfaces.FeatureIterator;

/**
 * @author Julien Lafaye
 *
 */
public class DouglasPeuckerCommand extends AbstractGeoCommand {

    public double distance = 0.5d;

    public DouglasPeuckerCommand(double distance, String geomTable, String geomKey, String geomColumn, String docName) {
        super(geomTable, geomKey, geomColumn, docName);
        this.distance = distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public String toString() {
        return "Douglas-Peucker filtering with precision " + distance;
    }

    @Override
    Filter createFilter() {
        final DouglasPeucker filter = new DouglasPeucker();
        filter.setThreshold(distance);
        return filter;
    }
}
