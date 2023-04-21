package org.fudaa.dodico.telemac;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.gis.GISDataModel;
import org.fudaa.ctulu.gis.GISMultiPoint;
import org.fudaa.ctulu.gis.GISPointMutable;
import org.fudaa.ctulu.gis.GISPolyligne;
import org.fudaa.ctulu.gis.GISZone;
import org.fudaa.ctulu.gis.GISZoneCollection;
import org.fudaa.ctulu.gis.GISZoneCollectionPoint;
import org.fudaa.ctulu.gis.GISZoneCollectionPolygone;
import org.fudaa.ctulu.gis.GISZoneCollectionPolyligne;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.telemac.io.SinusxFileFormat;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import org.fudaa.ctulu.gis.GISAttributeConstants;
import org.fudaa.ctulu.gis.GISDataModelFilterAdapter;
import org.fudaa.ctulu.gis.GISDataModelSelectionAdapter;

/**
 * @version $Id: TestJSinusx.java,v 1.2 2007-06-29 15:10:32 deniger Exp $
 * @author Fred Deniger
 */
public class TestJSinusx extends TestCase {

    /**
   * Le fichier de lecture.
   */
    String testLectureFic_;

    double delta_;

    /**
   * Constructeur par defaut.
   */
    public TestJSinusx() {
    }

    /**
   * @see junit.framework.TestCase#setUp()
   */
    public void setUp() {
        testLectureFic_ = "simple.sx";
        delta_ = 1E-15;
    }

    /**
   * @param _f
   * @return Les zones lues
   * @throws NumberFormatException
   */
    private GISZone lecture(final File _f) {
        assertNotNull(_f);
        assertTrue(_f.exists());
        final CtuluIOOperationSynthese op = SinusxFileFormat.getInstance().getLastVersionInstance(null).read(_f, null);
        final GISZone zone = (GISZone) op.getSource();
        final GISZoneCollectionPoint[] pts = zone.getPointCollections();
        assertEquals(2, pts.length);
        GISZoneCollectionPoint pt1 = pts[0];
        assertNotNull(pt1);
        assertEquals(pt1.getNumGeometries(), 4);
        GISPointMutable p = new GISPointMutable();
        double t;
        for (int i = 0; i < pt1.getNumGeometries(); i++) {
            pt1.get(i, p);
            t = i;
            assertEquals(p.getX(), t, delta_);
            assertEquals(p.getY(), t, delta_);
            assertEquals(p.getZ(), t, delta_);
        }
        pt1 = pts[1];
        assertEquals(pt1.getNumGeometries(), 1);
        pt1.get(0, p);
        assertEquals(p.getX(), 4, delta_);
        assertEquals(p.getY(), 4, delta_);
        assertEquals(p.getZ(), 4, delta_);
        final GISZoneCollectionPolygone[] polygones = zone.getPolygoneCollections();
        assertNotNull(polygones);
        assertEquals(1, polygones.length);
        assertEquals(polygones[0].getNumGeometries(), 1);
        final GISZoneCollectionPolyligne[] polys = zone.getPolyligneCollections();
        assertNotNull(polys);
        assertEquals(1, polys.length);
        final GISZoneCollectionPolyligne poly0 = polys[0];
        assertNotNull(poly0);
        assertEquals(2, poly0.getNumGeometries());
        GISDataModel mdcn = new GISDataModelSelectionAdapter(poly0, new Object[][] { { GISAttributeConstants.NATURE, GISAttributeConstants.ATT_NATURE_CN } });
        assertEquals(1, mdcn.getNumGeometries());
        GISDataModel mdpf = new GISDataModelSelectionAdapter(poly0, new Object[][] { { GISAttributeConstants.NATURE, GISAttributeConstants.ATT_NATURE_PF } });
        assertEquals(0, mdpf.getNumGeometries());
        GISPolyligne poly = (GISPolyligne) poly0.getGeometry(0);
        assertEquals(poly.getNumPoints(), 4);
        p = new GISPointMutable(-1d, -1d, -1d);
        for (int i = 0; i < 4; i++) {
            final CoordinateSequence s = poly.getCoordinateSequence();
            t = (double) i * 5;
            assertEquals(s.getX(i), t, delta_);
            assertEquals(s.getY(i), t, delta_);
            assertEquals(s.getOrdinate(i, 2), t, delta_);
        }
        poly = (GISPolyligne) poly0.getGeometry(1);
        assertEquals(poly.getNumPoints(), 2);
        p = new GISPointMutable(-1, -1, -1);
        for (int i = 0; i < 2; i++) {
            final CoordinateSequence s = poly.getCoordinateSequence();
            t = (double) i * 100 + 100;
            assertEquals(s.getX(i), t, delta_);
            assertEquals(s.getY(i), t, delta_);
            assertEquals(s.getOrdinate(i, 2), 100, delta_);
        }
        return zone;
    }

    public void testLectureEcriture() {
        File f = null;
        try {
            final File initFile = getFile(testLectureFic_);
            assertNotNull("Le fichier " + testLectureFic_ + " est introuvable", initFile);
            assertTrue(initFile.getAbsolutePath() + " n'existe pas !", initFile.exists());
            final GISZone zones = lecture(initFile);
            assertNotNull(zones);
            f = File.createTempFile("testSinux", ".sx");
            assertNotNull(f);
            List<GISDataModel> points = new ArrayList<GISDataModel>();
            List<GISDataModel> lignes = new ArrayList<GISDataModel>();
            List<GISDataModel> multipoints = new ArrayList<GISDataModel>();
            for (int i = 0; i < zones.getNumGeometries(); i++) {
                GISZoneCollection zone = (GISZoneCollection) zones.getGeometry(i);
                if (zone.getDataStoreClass() == LineString.class || zone.getDataStoreClass() == LinearRing.class) lignes.add((GISDataModel) zone); else if (zone.getDataStoreClass() == GISMultiPoint.class) multipoints.add((GISDataModel) zone); else points.add((GISDataModel) zone);
            }
            GISDataModel[][] gisDataModels = new GISDataModel[][] { points.toArray(new GISDataModel[0]), lignes.toArray(new GISDataModel[0]), multipoints.toArray(new GISDataModel[0]) };
            SinusxFileFormat.getInstance().getLastVersionInstance(null).write(f, gisDataModels, null);
            lecture(f);
        } catch (final Exception _e) {
            _e.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
    }

    /**
   * @param _f
   * @return File
   */
    public File getFile(final String _f) {
        return TestIO.getFile(getClass(), _f);
    }
}
