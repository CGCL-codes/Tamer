package geovista.geoviz.map;

import junit.framework.TestCase;
import geovista.geoviz.Exerciser;

public class GeoMapTest extends TestCase {

    public void testGeoMap() {
        GeoMap comp = new GeoMap();
        Exerciser exer = new Exerciser();
        exer.testGUIAndEvents(comp);
    }
}
