package pt.utl.ist.lucene.filter.distancebuilders;

import com.pjaol.search.geo.utils.DistanceHandler;
import com.pjaol.search.geo.utils.DistanceUtils;
import java.util.Map;
import java.util.BitSet;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.io.IOException;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Filter;
import org.apache.lucene.index.IndexReader;
import org.apache.solr.util.NumberUtils;

/**
 * This class does not filter any document
 * just create the distances map
 */
public class SpaceDistanceBuilderFilter extends Filter {

    private static final long serialVersionUID = 1L;

    private double lat;

    private double lng;

    private String latField;

    private String lngField;

    private Map<Integer, Double> distances = null;

    private DistanceHandler.precision precise = null;

    /**
     * Provide a distance filter based from a center point with a radius
     * in miles
     *
     * @param lat
     * @param lng
     * @param latField
     * @param lngField
     */
    public SpaceDistanceBuilderFilter(double lat, double lng, String latField, String lngField) {
        this.lat = lat;
        this.lng = lng;
        this.latField = latField;
        this.lngField = lngField;
    }

    public Map<Integer, Double> getDistances() {
        return distances;
    }

    public Double getDistance(int docid) {
        return distances.get(docid);
    }

    public BitSet bits(IndexReader reader) throws IOException {
        int maxdocs = reader.numDocs();
        BitSet bits = new BitSet(maxdocs);
        setPrecision(maxdocs);
        WeakHashMap<String, Double> cdistance = new WeakHashMap<String, Double>(maxdocs);
        String[] latIndex = FieldCache.DEFAULT.getStrings(reader, latField);
        String[] lngIndex = FieldCache.DEFAULT.getStrings(reader, lngField);
        distances = new HashMap<Integer, Double>(maxdocs);
        for (int i = 0; i < maxdocs; i++) {
            String sx = latIndex[i];
            String sy = lngIndex[i];
            if (sx != null && sy != null) {
                double x = NumberUtils.SortableStr2double(sx);
                double y = NumberUtils.SortableStr2double(sy);
                x = DistanceHandler.getPrecision(x, precise);
                y = DistanceHandler.getPrecision(y, precise);
                String ck = Double.toString(x) + "," + Double.toString(y);
                Double cachedDistance = cdistance.get(ck);
                double d;
                if (cachedDistance != null) {
                    d = cachedDistance;
                } else {
                    d = DistanceUtils.getDistanceMi(lat, lng, x, y);
                    cdistance.put(ck, d);
                }
                distances.put(i, d);
                bits.set(i);
            }
        }
        return bits;
    }

    private void setPrecision(int maxDocs) {
        precise = DistanceHandler.precision.EXACT;
        if (maxDocs > 1000) {
            precise = DistanceHandler.precision.TWENTYFEET;
        }
        if (maxDocs > 10000) {
            precise = DistanceHandler.precision.TWOHUNDREDFEET;
        }
    }

    public void setDistances(Map<Integer, Double> distances) {
        this.distances = distances;
    }
}
