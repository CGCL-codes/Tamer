package org.fudaa.dodico.rubar;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import com.vividsolutions.jts.geom.Coordinate;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.ef.*;
import org.fudaa.dodico.h2d.rubar.H2dRubarArete;
import org.fudaa.dodico.h2d.rubar.H2dRubarGrid;
import org.fudaa.dodico.h2d.rubar.H2dRubarGridAreteSource;
import org.fudaa.dodico.rubar.io.RubarDATFileFormat;
import org.fudaa.dodico.rubar.io.RubarDATReader;
import org.fudaa.dodico.rubar.io.RubarDATWriter;
import org.fudaa.dodico.rubar.io.RubarMAIFileFormat;

/**
 * @author Fred Deniger
 * @version $Id: TestJDAT.java,v 1.2 2007-03-02 13:00:54 deniger Exp $
 */
public class TestJDAT extends TestIO {

    /**
   * SECT24.DAT.
   */
    public TestJDAT() {
        super("SECT24.DAT");
    }

    public void internTestMAI(final EfGridInterface _grid) {
        assertEquals(6649, _grid.getEltNb());
        EfElement el = _grid.getElement(0);
        assertEquals(0, el.getPtIndex(0));
        assertEquals(2, el.getPtIndex(1));
        assertEquals(3, el.getPtIndex(2));
        assertEquals(1, el.getPtIndex(3));
        el = _grid.getElement(_grid.getEltNb() - 1);
        assertEquals(6831, el.getPtIndex(0));
        assertEquals(6832, el.getPtIndex(1));
        assertEquals(6843, el.getPtIndex(2));
        assertEquals(6842, el.getPtIndex(3));
        assertEquals(6844, _grid.getPtsNb());
        Coordinate n = _grid.getCoor(0);
        assertDoubleEquals(847000.0, n.x);
        assertDoubleEquals(128975.1, n.y);
        n = _grid.getCoor(_grid.getPtsNb() - 1);
        assertDoubleEquals(847697.0, n.x);
        assertDoubleEquals(137286.0, n.y);
        assertEquals(EfElementType.T3_Q4, _grid.getEltType());
    }

    private H2dRubarGrid readMAI(final File _f) {
        assertNotNull(_f);
        final CtuluIOOperationSynthese s = RubarMAIFileFormat.getInstance().read(_f, null);
        s.printAnalyze();
        assertFalse(s.containsMessages());
        return (H2dRubarGrid) s.getSource();
    }

    public void testReaderMAI() {
        internTestMAI(readMAI(getFile("OYOAME.MAI")));
    }

    public void testWriterMAI() {
        final H2dRubarGrid grid = readMAI(getFile("OYOAME.MAI"));
        try {
            final File dest = File.createTempFile("toto", "mai");
            assertNotNull(dest);
            RubarMAIFileFormat.getInstance().write(dest, grid, null);
            internTestMAI(readMAI(dest));
            dest.delete();
        } catch (final IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    private void interntestResult(final CtuluIOOperationSynthese _s) {
        if (_s.containsMessages()) {
            _s.printAnalyze();
        }
        final H2dRubarGridAreteSource gridArete = (H2dRubarGridAreteSource) _s.getSource();
        assertEquals(1, gridArete.getNbDecimal());
        assertNotNull(gridArete);
        final EfGridVolumeInterface gv = gridArete.getRubarGrid();
        assertNotNull(gv);
        assertEquals(6375, gv.getEltNb());
        EfElementVolume v = gv.getEltVolume(0);
        assertEquals(4, v.getPtNb());
        assertEquals(0, v.getPtIndex(0));
        assertEquals(2, v.getPtIndex(1));
        assertEquals(3, v.getPtIndex(2));
        assertEquals(1, v.getPtIndex(3));
        assertEquals(4, v.getNbAretes());
        assertEquals(0, v.getIdxArete(0));
        assertEquals(1, v.getIdxArete(1));
        assertEquals(2, v.getIdxArete(2));
        assertEquals(3, v.getIdxArete(3));
        int[] eltVoison = gridArete.elementsVoisinsParElement()[0];
        assertNotNull(eltVoison);
        assertEquals(4, eltVoison.length);
        assertEquals(-1, eltVoison[0]);
        assertEquals(-1, eltVoison[1]);
        assertEquals(1, eltVoison[2]);
        assertEquals(23, eltVoison[3]);
        v = gv.getEltVolume(100);
        assertEquals(4, v.getPtNb());
        assertEquals(104, v.getPtIndex(0));
        assertEquals(105, v.getPtIndex(1));
        assertEquals(129, v.getPtIndex(2));
        assertEquals(128, v.getPtIndex(3));
        assertEquals(4, v.getNbAretes());
        assertEquals(182, v.getIdxArete(0));
        assertEquals(228, v.getIdxArete(1));
        assertEquals(229, v.getIdxArete(2));
        assertEquals(226, v.getIdxArete(3));
        eltVoison = gridArete.elementsVoisinsParElement()[100];
        assertNotNull(eltVoison);
        assertEquals(4, eltVoison.length);
        assertEquals(77, eltVoison[0]);
        assertEquals(101, eltVoison[1]);
        assertEquals(123, eltVoison[2]);
        assertEquals(99, eltVoison[3]);
        final H2dRubarGrid g = gridArete.getRubarGrid();
        assertEquals(12869, g.getNbAretes());
        H2dRubarArete ar = g.getRubarArete(0);
        assertEquals(0, ar.getPt1Idx());
        assertEquals(2, ar.getPt2Idx());
        assertEquals(2, ar.getType().getRubarIdx());
        eltVoison = gridArete.elementsVoisinsParArete()[0];
        assertEquals(2, eltVoison.length);
        assertEquals(-1, eltVoison[0]);
        assertEquals(0, eltVoison[1]);
        ar = g.getRubarArete(12865);
        assertEquals(null, ar.getType());
        ar = g.getRubarArete(12868);
        assertEquals(6493, ar.getPt1Idx());
        assertEquals(6494, ar.getPt2Idx());
        assertEquals(2, ar.getType().getRubarIdx());
        eltVoison = gridArete.elementsVoisinsParArete()[12868];
        assertEquals(2, eltVoison.length);
        assertEquals(6374, eltVoison[0]);
        assertEquals(-1, eltVoison[1]);
        assertEquals(6495, gv.getPtsNb());
        Coordinate n = gv.getCoor(0);
        assertDoubleEquals(811784.0, n.x);
        assertDoubleEquals(284760.0, n.y);
        assertDoubleEquals(235, n.z);
        n = gv.getCoor(6494);
        assertDoubleEquals(808623.3, n.x);
        assertDoubleEquals(286799.2, n.y);
        assertDoubleEquals(180, n.z);
    }

    /**
   * Test la lecture du fichier SECT24.DAT.
   */
    public void testReader() {
        final RubarDATReader r = new RubarDATReader(RubarDATFileFormat.getInstance());
        r.setFile(fic_);
        interntestResult(r.read());
    }

    public void testVoisinsFinder() {
        assertTrue(testVoisinsFinder(fic_));
    }

    protected boolean testVoisinsFinder(File _f) {
        final RubarDATReader r = new RubarDATReader(RubarDATFileFormat.getInstance());
        r.setFile(_f);
        final H2dRubarGridAreteSource gridArete = (H2dRubarGridAreteSource) r.read().getSource();
        boolean res = true;
        assertNotNull(gridArete);
        if (gridArete == null) return false;
        EfVoisinageFinderActivity finder = new EfVoisinageFinderActivity();
        CtuluAnalyze analyse = new CtuluAnalyze();
        finder.process(gridArete.getRubarGrid(), null, analyse);
        if (analyse.containsFatalError()) analyse.printResume();
        res = testIdx(finder.getElementVoisinsParArete(), gridArete.elementsVoisinsParArete());
        if (!res) return false;
        res = testIdx(finder.getElementVoisinsParElement(), gridArete.elementsVoisinsParElement());
        return res;
    }

    private boolean testIdx(int[][] _tab1, int[][] _tab2) {
        assertNotNull(_tab1);
        if (_tab1 == null) return false;
        assertNotNull(_tab2);
        if (_tab2 == null) return false;
        assertEquals(_tab1.length, _tab2.length);
        if (_tab1.length != _tab2.length) return false;
        for (int i = 0; i < _tab1.length; i++) {
            int[] tab1 = _tab1[i];
            assertNotNull(tab1);
            if (tab1 == null) return false;
            int[] tab2 = _tab2[i];
            assertNotNull(tab2);
            if (tab2 == null) return false;
            boolean equals = Arrays.equals(tab1, tab2);
            if (!equals) {
                System.err.println("numero " + i);
                System.err.println("indice calcul� " + CtuluLibString.arrayToString(tab1));
                System.err.println("indice lu " + CtuluLibString.arrayToString(tab2));
            }
            assertTrue(equals);
            if (!equals) return false;
        }
        return true;
    }

    public void testWriter() {
        RubarDATReader r = new RubarDATReader(RubarDATFileFormat.getInstance());
        r.setFile(fic_);
        final H2dRubarGridAreteSource gridArete = (H2dRubarGridAreteSource) r.read().getSource();
        final RubarDATWriter w = new RubarDATWriter();
        File f = null;
        try {
            f = File.createTempFile("rubar", ".dat");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assertNotNull(f);
        w.setFile(f);
        w.write(gridArete).printAnalyze();
        r = new RubarDATReader(RubarDATFileFormat.getInstance());
        r.setFile(f);
        interntestResult(r.read());
        if (f != null) {
            f.delete();
        }
    }

    public static void main(String[] _args) {
        if (CtuluLibArray.isEmpty(_args)) {
            System.err.println("pas de fichier DAT sp�cifi�");
            System.exit(1);
        }
        try {
            File f = new File(_args[0]).getCanonicalFile();
            if (f == null || !f.exists()) System.err.println(" le fichier dat n'existe pas :" + f);
            System.out.println("ok =" + new TestJDAT().testVoisinsFinder(f));
        } catch (IOException _evt) {
            _evt.printStackTrace();
        }
    }
}
