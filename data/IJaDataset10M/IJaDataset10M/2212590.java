package twente.visico.org.GPSFilter;

import java.util.ArrayList;
import org.jfree.data.xy.XYSeries;

public interface FilterStrategy {

    XYSeries getSeries(ArrayList<Point3D> points);
}
