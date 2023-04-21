package playground.droeder.bvg09.analysis.preProcess;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.gis.ShapeFileWriter;

/**
 * @author droeder
 *
 */
public class CreateSingleSectorShapes {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        final String SHAPEFILE = "D:/VSP/BVG09_Auswertung/input/Bezirke_BVG_zone.SHP";
        final String OUTDIR = "D:/VSP/BVG09_Auswertung/BerlinSHP/sectors/";
        FeatureSource features = ShapeFileReader.readDataFile(SHAPEFILE);
        HashMap<String, Collection<Feature>> newFeatures = new HashMap<String, Collection<Feature>>();
        Feature f;
        for (Iterator<Feature> it = features.getFeatures().iterator(); it.hasNext(); ) {
            f = it.next();
            if (!((Integer) f.getAttribute(4) == 4)) {
                if (!newFeatures.containsKey(f.getAttribute(1).toString().substring(0, 5))) {
                    newFeatures.put(f.getAttribute(1).toString().substring(0, 5), new LinkedList<Feature>());
                }
                newFeatures.get(f.getAttribute(1).toString().substring(0, 5)).add(f);
            }
        }
        for (Entry<String, Collection<Feature>> e : newFeatures.entrySet()) {
            ShapeFileWriter.writeGeometries(e.getValue(), OUTDIR + "Berlin_Zone_" + e.getKey() + ".shp");
        }
    }
}
