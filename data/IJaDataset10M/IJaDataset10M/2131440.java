package br.com.jro.developer.tools.shapefile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import com.vividsolutions.jts.geom.Geometry;

public class ShapeFile {

    private ShapefileDataStore util;

    public ShapeFile(URL url) throws Exception {
        if (url != null) {
            util = new ShapefileDataStore(url);
        } else {
            throw new NullPointerException("Null URL for ShapefileDataSource");
        }
    }

    public Iterator<Feature> getFeatures() throws Exception {
        FeatureSource fs = util.getFeatureSource();
        FeatureCollection fc = fs.getFeatures();
        @SuppressWarnings("unchecked") Iterator<Feature> i = (Iterator<Feature>) fc.iterator();
        return i;
    }

    public List<String> getWktList() throws Exception {
        Iterator<Feature> i = this.getFeatures();
        List<String> wkts = new ArrayList<String>();
        while (i.hasNext()) {
            Feature f = i.next();
            String wkt = this.getWKT(f);
            wkts.add(wkt);
        }
        return wkts;
    }

    private String getWKT(Feature g) {
        if (g != null) {
            return g.toString();
        } else {
            return null;
        }
    }
}
