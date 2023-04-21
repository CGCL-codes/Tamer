package org.fhw.cabaweb.junittests;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import org.fhw.cabaweb.data.DataInterfaceBerechnungsauftraege;
import org.fhw.cabaweb.ojb.dataobjects.Berechnungsauftraege;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import junit.framework.TestCase;

/**
 * Klasse f&uuml;r den Test der Datenzugriffe auf Berechnungsauftraege
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 06.06.2004
 */
public class DataInterfaceBerechnungsauftraegeTest extends TestCase {

    private DataInterfaceBerechnungsauftraege test;

    private Date datum;

    private Time uhrzeit;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DataInterfaceBerechnungsauftraegeTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        test = new DataInterfaceBerechnungsauftraege();
        datum = new Date(System.currentTimeMillis());
        uhrzeit = new Time(System.currentTimeMillis());
        Berechnungsauftraege retval = null;
        retval = (Berechnungsauftraege) test.sucheProjektnummer(new Integer(1));
        if (retval != null) test.loeschen(retval);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testErzeugen() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Collection objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        assertEquals(true, objekte.size() == 1);
        test.loeschen(retval);
    }

    public final void testEditieren() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Collection objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(1), new Short((short) 1));
        test.editieren(testberechnungsauftrag);
        retval = (Berechnungsauftraege) test.sucheProjektnummer(retval.getProjekte().getProjektnummer());
        assertEquals(retval.toString(), testberechnungsauftrag.toString());
        test.loeschen(retval);
    }

    public final void testLoeschen() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Collection objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        test.loeschen(retval);
        objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        assertEquals(true, objekte.size() == 0);
    }

    public final void testSucheAlle() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Collection objekte = test.sucheAlle();
        assertNotSame(new Integer(0), new Integer(objekte.size()));
        objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        test.loeschen(retval);
    }

    public final void testSucheProjektnummer() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Berechnungsauftraege objekt = (Berechnungsauftraege) test.sucheProjektnummer(new Integer(1));
        assertEquals(testberechnungsauftrag.toString(), objekt.toString());
        test.loeschen(objekt);
    }

    public final void testSucheGruppenname() {
        Berechnungsauftraege testberechnungsauftrag = new Berechnungsauftraege(new Projekte(new Integer(1), null, null, null), datum, uhrzeit, new Integer(0), new Short((short) 0));
        Berechnungsauftraege retval = null;
        test.erzeugen(testberechnungsauftrag);
        Collection objekte = test.sucheDatumUhrzeit(datum, uhrzeit);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            retval = (Berechnungsauftraege) iter.next();
        }
        assertEquals(new Integer(1), new Integer(objekte.size()));
        test.loeschen(retval);
    }
}
