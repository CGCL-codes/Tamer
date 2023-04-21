package org.fhw.cabaweb.junittests;

import java.util.Collection;
import org.fhw.cabaweb.data.DataInterfaceErgebnissdatenDouble;
import org.fhw.cabaweb.data.DataInterfaceProjektgruppen;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Double;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Projektgruppen;
import junit.framework.TestCase;

/**
 * Klasse f&uuml;r den Test der Datenzugriffe auf Ergebnissdaten_Double
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 24.05.2004
 */
public class DataInterfaceErgebnissdatenDoubleTest extends TestCase {

    private DataInterfaceErgebnissdatenDouble test;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DataInterfaceErgebnissdatenDoubleTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        test = new DataInterfaceErgebnissdatenDouble();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testErzeugen() {
        Ergebnissdaten_Double retval = null;
        DataInterfaceProjektgruppen dip = new DataInterfaceProjektgruppen();
        Projektgruppen testprojektgruppe = new Projektgruppen(new Integer(1), new Projekte(new Integer(1), null, null, null), "Testgruppe", null, new Boolean(true));
        dip.erzeugen(testprojektgruppe);
        testprojektgruppe = (Projektgruppen) dip.sucheGruppennummer(new Integer(1));
        Ergebnissdaten_Double testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        test.erzeugen(testergebnissdaten);
        Collection objekte = test.sucheKombination(new Integer(1), new Integer(1), new Integer(-1));
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Ergebnissdaten_Double) iter.next();
        }
        assertEquals(true, objekte.size() == 1);
        test.loeschen(retval);
        dip.loeschen(testprojektgruppe);
    }

    public final void testEditieren() {
        Ergebnissdaten_Double retval = null;
        DataInterfaceProjektgruppen dip = new DataInterfaceProjektgruppen();
        Projektgruppen testprojektgruppe = new Projektgruppen(new Integer(1), new Projekte(new Integer(1), null, null, null), "Testgruppe", null, new Boolean(true));
        dip.erzeugen(testprojektgruppe);
        testprojektgruppe = (Projektgruppen) dip.sucheGruppennummer(new Integer(1));
        Ergebnissdaten_Double testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        test.erzeugen(testergebnissdaten);
        testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        testergebnissdaten.setDouble1(new Double(1.11));
        test.editieren(testergebnissdaten);
        Collection objekte = test.sucheKombination(new Integer(1), new Integer(1), null);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Ergebnissdaten_Double) iter.next();
        }
        assertEquals(retval.getDouble1(), testergebnissdaten.getDouble1());
        test.loeschen(retval);
        dip.loeschen(testprojektgruppe);
    }

    public final void testLoeschen() {
        Ergebnissdaten_Double retval = null;
        DataInterfaceProjektgruppen dip = new DataInterfaceProjektgruppen();
        Projektgruppen testprojektgruppe = new Projektgruppen(new Integer(1), new Projekte(new Integer(1), null, null, null), "Testgruppe", null, new Boolean(true));
        dip.erzeugen(testprojektgruppe);
        testprojektgruppe = (Projektgruppen) dip.sucheGruppennummer(new Integer(1));
        Ergebnissdaten_Double testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        test.erzeugen(testergebnissdaten);
        testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, null);
        test.loeschen(testergebnissdaten);
        Collection objekte = test.sucheKombination(new Integer(1), new Integer(1), null);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Ergebnissdaten_Double) iter.next();
        }
        assertEquals(true, objekte.size() == 0);
        dip.loeschen(testprojektgruppe);
    }

    public final void testSucheAlle() {
        Ergebnissdaten_Double retval = null;
        DataInterfaceProjektgruppen dip = new DataInterfaceProjektgruppen();
        Projektgruppen testprojektgruppe = new Projektgruppen(new Integer(1), new Projekte(new Integer(1), null, null, null), "Testgruppe", null, new Boolean(true));
        dip.erzeugen(testprojektgruppe);
        testprojektgruppe = (Projektgruppen) dip.sucheGruppennummer(new Integer(1));
        Ergebnissdaten_Double testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        test.erzeugen(testergebnissdaten);
        Collection objekte = test.sucheAlle();
        assertNotSame(new Integer(0), new Integer(objekte.size()));
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Ergebnissdaten_Double) iter.next();
        }
        test.loeschen(retval);
        dip.loeschen(testprojektgruppe);
    }

    public final void testSucheKombination() {
        Ergebnissdaten_Double retval = null;
        DataInterfaceProjektgruppen dip = new DataInterfaceProjektgruppen();
        Projektgruppen testprojektgruppe = new Projektgruppen(new Integer(1), new Projekte(new Integer(1), null, null, null), "Testgruppe", null, new Boolean(true));
        dip.erzeugen(testprojektgruppe);
        testprojektgruppe = (Projektgruppen) dip.sucheGruppennummer(new Integer(1));
        Ergebnissdaten_Double testergebnissdaten = new Ergebnissdaten_Double(testprojektgruppe, new Integer(-1));
        test.erzeugen(testergebnissdaten);
        Collection objekte = test.sucheKombination(new Integer(1), null, null);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Ergebnissdaten_Double) iter.next();
        }
        assertEquals(retval.getQuartal(), new Integer(-1));
        test.loeschen(retval);
        dip.loeschen(testprojektgruppe);
    }
}
