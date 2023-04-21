package org.fao.geonet.kernel.search.spatial;

import static org.fao.geonet.constants.Geonet.SearchResult.Relation.INTERSECTION;
import static org.fao.geonet.constants.Geonet.SearchResult.Relation.OUTSIDEOF;
import static org.fao.geonet.constants.Geonet.SearchResult.Relation.OVERLAPS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.junit.Test;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;

public class TestSearchOnline {

    public static final GeometryFactory fac = new GeometryFactory();

    private int index = 1;

    private final int repititions = 50;

    @Test
    public void testIntersectionSearch() throws Exception {
        ArrayList<String> results = new ArrayList<String>();
        String baseNoLucene = "http://localhost:8383/geonetwork/srv/en/main.search.embedded?any=&similarity=.8&digital=off&paper=off&dynamic=off&download=off&template=n&sortBy=relevance&hitsPerPage=10&output=full";
        String baseWithLucene = "http://localhost:8383/geonetwork/srv/en/main.search.embedded?any=soil&similarity=.8&attrset=geo&northBL=50.400000000000006&eastBL=106.19999999999999&southBL=25.200000000000003&westBL=93.60000000000002&digital=off&paper=off&dynamic=off&download=off&sortBy=relevance&hitsPerPage=10&output=full";
        String baseWithLuceneAndBBOX = "http://localhost:8383/geonetwork/srv/en/main.search.embedded?any=soil&similarity=.8&attrset=geo&northBL=50.400000000000006&eastBL=106.19999999999999&southBL=25.200000000000003&westBL=93.60000000000002&digital=off&paper=off&dynamic=off&download=off&sortBy=relevance&hitsPerPage=10&output=full";
        String baseLuceneOutSideOf = "http://localhost:8383/geonetwork/srv/en/main.search.embedded?any=&similarity=.8&attrset=geo&northBL=3.5999999999999943&eastBL=37.80000000000001&southBL=-19.799999999999997&westBL=18&digital=off&paper=off&dynamic=off&download=off&sortBy=relevance&hitsPerPage=10&output=full";
        results.add(doTiming(createRequest(baseNoLucene, 125, 130, 50, 55, INTERSECTION), "INTERSECTION query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, 0, 1, 0, 1, INTERSECTION), "INTERSECTION query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -90, -80, 0, 1, INTERSECTION), "INTERSECTION query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -180, 180, -90, 90, INTERSECTION), "INTERSECTION query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, 0, 1, 0, 1, OVERLAPS), "OVERLAPS query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, 125, 130, 50, 55, OVERLAPS), "OVERLAPS query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -90, -80, 0, 1, OVERLAPS), "OVERLAPS query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -180, 180, -90, 90, OVERLAPS), "OVERLAPS query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -90, -80, 0, 1, OUTSIDEOF), "OUTSIDEOF query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, 0, 1, 0, 1, OUTSIDEOF), "OUTSIDEOF query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, 125, 130, 50, 1, OUTSIDEOF), "OUTSIDEOF query, no lucene filtering"));
        results.add(doTiming(createRequest(baseNoLucene, -180, 180, -90, 90, OUTSIDEOF), "OUTSIDEOF query, no lucene filtering"));
        results.add(doTiming(baseWithLucene, "Basic all=soil"));
        results.add(doTiming(createRequest(baseNoLucene, -90, -80, 0, 1, OVERLAPS), "OVERLAPS query, lucene all=soil"));
        results.add(doTiming(createRequest(baseNoLucene, 125, 130, 50, 1, OVERLAPS), "OVERLAPS query, lucene all=soil"));
        results.add(doTiming(baseWithLuceneAndBBOX + "&relation=overlaps", "Lucene based all=soil with lucene base bbox filter"));
        results.add(doTiming(createRequest(baseWithLuceneAndBBOX, -90, -80, 0, 1, OVERLAPS), "OVERLAPS query, Lucene based all=soil with lucene base bbox filter"));
        results.add(doTiming(createRequest(baseWithLuceneAndBBOX, 125, 130, 50, 1, OVERLAPS), "OVERLAPS query, Lucene based all=soil with lucene base bbox filter"));
        results.add(doTiming(baseLuceneOutSideOf + "&relation=fullyOutsideOf", "Lucene out side of query"));
        results.add(doTiming(createRequest(baseLuceneOutSideOf, 18, 38, 0, 1, OVERLAPS), "OVERLAPS query, Lucene out side of query"));
        results.add(doTiming(createRequest(baseLuceneOutSideOf, 18, 38, 4, 20, OUTSIDEOF), "OUTSIDEOF query, Lucene out side of query"));
        results.add(doTiming(createRequest(baseLuceneOutSideOf, -170, -160, 4, 20, OUTSIDEOF), "OUTSIDEOF query, Lucene out side of query"));
        System.out.println();
        System.out.println(repititions + " repititions were ran for each test.");
        System.out.println("The Results show the time taken for one call on average");
        System.out.println();
        for (String string : results) {
            System.out.println(string);
        }
    }

    private String createRequest(String baseURL, int x1, int x2, int y1, int y2, String intersectionType) throws UnsupportedEncodingException {
        Polygon polygon = (Polygon) fac.toGeometry(new Envelope(x1, x2, y1, y2));
        MultiPolygon geom = fac.createMultiPolygon(new Polygon[] { polygon });
        WKTWriter writer = new WKTWriter();
        String wkt = URLEncoder.encode(writer.write(geom), "UTF-8");
        String request = baseURL + "&geometry=" + wkt + "&relation=" + intersectionType;
        return request;
    }

    private String doTiming(String request, String desc) throws MalformedURLException, IOException {
        String title = "Test " + index;
        index++;
        makeRequest(request);
        double lastPercentage = 0;
        String results = "FAILED";
        long start = System.currentTimeMillis();
        for (int i = 0; i < repititions; i++) {
            results = makeRequest(request);
            double percent = (((double) i + 1) / repititions) * 100;
            if (percent >= lastPercentage + 5) {
                lastPercentage = percent;
                System.out.println(percent + " % - " + title);
            }
        }
        double time = (((double) (System.currentTimeMillis() - start)) / 1000) / repititions;
        String result = title + ": " + time + "s with " + results + "  (" + desc + ")";
        System.out.println(result);
        return result;
    }

    private String makeRequest(String request) throws MalformedURLException, IOException {
        URL url = new URL(request);
        BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
        int i = 0;
        String matches = "FAILURE";
        for (String line = stream.readLine(); line != null; line = stream.readLine()) {
            if (i == 2) {
                matches = line.trim();
            }
            i++;
        }
        stream.close();
        return matches;
    }

    public static void main(String[] args) throws Exception {
        Thread.sleep(3000);
        new TestSearchOnline().testIntersectionSearch();
    }
}
