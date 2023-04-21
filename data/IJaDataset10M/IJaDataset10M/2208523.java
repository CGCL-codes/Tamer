package playground.johannes.socialnetworks.spatial;

import gnu.trove.TDoubleDoubleHashMap;
import gnu.trove.TDoubleIntHashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.matsim.contrib.sna.gis.CRSUtils;
import org.matsim.contrib.sna.graph.spatial.SpatialSparseGraph;
import org.matsim.contrib.sna.graph.spatial.SpatialVertex;
import org.matsim.contrib.sna.math.Distribution;
import org.matsim.core.utils.geometry.CoordUtils;
import playground.johannes.socialnetworks.graph.spatial.io.Population2SpatialGraph;

/**
 * @author illenberger
 *
 */
public class Fractals {

    public TDoubleDoubleHashMap correlationDimension(Set<? extends SpatialVertex> vertices, double descretization) {
        List<? extends SpatialVertex> points = new ArrayList<SpatialVertex>(vertices);
        int n = points.size();
        TDoubleIntHashMap counts = new TDoubleIntHashMap();
        for (int i = 0; i < n; i++) {
            for (int j = (i + 1); j < n; j++) {
                double d = descretize(CoordUtils.calcDistance(points.get(i).getCoordinate(), points.get(j).getCoordinate()), descretization);
                counts.adjustOrPutValue(d, 1, 1);
            }
        }
        TDoubleDoubleHashMap values = new TDoubleDoubleHashMap();
        double[] classes = counts.keys();
        Arrays.sort(classes);
        int n_acc = 0;
        for (double d : classes) {
            n_acc = counts.get(d);
            double nom = 2.0 * n_acc / (n * n);
            values.put(d, Math.log(nom * d * d) / Math.log(d));
        }
        return values;
    }

    private double descretize(double d, double descretization) {
        d = Math.ceil(d / descretization);
        d = Math.max(1, d);
        return d;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        SpatialSparseGraph graph = new Population2SpatialGraph(CRSUtils.getCRS(21781)).read("/Users/fearonni/vsp-work/work/socialnets/data/schweiz/random/randompop.xml");
        TDoubleDoubleHashMap d_c = new Fractals().correlationDimension(graph.getVertices(), 500.0);
        Distribution.writeHistogram(d_c, "/Users/fearonni/vsp-work/work/socialnets/data/schweiz/complete/d_c.txt");
    }
}
