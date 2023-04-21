package org.fhw.cabaweb.junittests;

import java.util.Collection;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.fhw.cabaweb.ojb.UseCaseVoreinstellungenGruppierungsnamen;
import org.fhw.cabaweb.ojb.UseCaseVoreinstellungenUntergruppierungsnamen;
import org.fhw.cabaweb.ojb.UseCaseVoreinstellungenFeldnamen;
import org.fhw.cabaweb.ojb.UseCaseProjekte;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Gruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Feldnamen;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Untergruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.interfaces.UseCase;
import junit.framework.TestCase;

/**
 * Klasse f&uuml;r den Test der Funktionen des UseCases Voreinstellungen_Double
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version 18.05.2004
 */
public class UseCaseVoreinstellungenFeldnamenTest extends TestCase {

    private PersistenceBroker broker;

    private UseCase testCase;

    private Voreinstellungen_Feldnamen test;

    private Projekte prj;

    private UseCase prjCase;

    private Voreinstellungen_Gruppierungsnamen erggrpn;

    private UseCase erggrpnCase;

    private Voreinstellungen_Untergruppierungsnamen ergugrpn;

    private UseCase ergugrpnCase;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UseCaseVoreinstellungenFeldnamenTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            prj = new Projekte(new Integer(1), null, null, null);
            prjCase = (UseCase) new UseCaseProjekte(broker);
            prj = (Projekte) prjCase.sucheObjekt(prj);
            erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, "Testgruppierung 123");
            erggrpnCase = (UseCase) new UseCaseVoreinstellungenGruppierungsnamen(broker);
            erggrpnCase.erzeugen(erggrpn);
            erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
            erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
            ergugrpn = new Voreinstellungen_Untergruppierungsnamen(new Integer(1), erggrpn, "Testuntergruppierung 123");
            ergugrpnCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsnamen(broker);
            ergugrpnCase.erzeugen(ergugrpn);
            ergugrpn = (Voreinstellungen_Untergruppierungsnamen) ergugrpnCase.sucheObjekt(ergugrpn);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        testCase = (UseCase) new UseCaseVoreinstellungenFeldnamen(broker);
        test = new Voreinstellungen_Feldnamen();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            prj = new Projekte(new Integer(1), null, null, null);
            prjCase = (UseCase) new UseCaseProjekte(broker);
            prj = (Projekte) prjCase.sucheObjekt(prj);
            erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
            erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
            ergugrpn = new Voreinstellungen_Untergruppierungsnamen(new Integer(1), erggrpn, null);
            ergugrpnCase = (UseCase) new UseCaseVoreinstellungenUntergruppierungsnamen(broker);
            ergugrpnCase.loeschen(ergugrpn);
            ergugrpn = null;
            erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
            erggrpnCase = (UseCase) new UseCaseVoreinstellungenGruppierungsnamen(broker);
            erggrpnCase.loeschen(erggrpn);
            erggrpn = null;
            broker.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void testErzeugen() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        ergugrpn = new Voreinstellungen_Untergruppierungsnamen(new Integer(1), erggrpn, null);
        ergugrpn = (Voreinstellungen_Untergruppierungsnamen) ergugrpnCase.sucheObjekt(ergugrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Integer", "Integer1", "Testfeld1");
        testCase.erzeugen(test);
        Voreinstellungen_Feldnamen test2 = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Integer", "Integer1", "Testfeld1");
        assertEquals(test.toString(), ((Voreinstellungen_Feldnamen) testCase.sucheObjekt(test2)).toString());
        test = new Voreinstellungen_Feldnamen();
    }

    public void testEditieren() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Integer", "Integer1", "Testfeld1");
        testCase.erzeugen(test);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        testCase.editieren(test);
        Voreinstellungen_Feldnamen test2 = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, null, null, null, null);
        assertEquals(test.toString(), ((Voreinstellungen_Feldnamen) testCase.sucheObjekt(test2)).toString());
        test = new Voreinstellungen_Feldnamen();
    }

    public void testSucheObjekt() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        testCase.erzeugen(test);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, null, null, null, null);
        Voreinstellungen_Feldnamen objekt = (Voreinstellungen_Feldnamen) testCase.sucheObjekt(test);
        Voreinstellungen_Feldnamen test2 = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        assertEquals(test2.toString(), objekt.toString());
        test = new Voreinstellungen_Feldnamen();
    }

    public void testSucheObjekte() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        testCase.erzeugen(test);
        test = new Voreinstellungen_Feldnamen(null, null, null, "Voreinstellungen_Double", null, null);
        Collection objekte = testCase.sucheObjekte(test);
        java.util.Iterator iter = objekte.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        assertEquals(true, objekte.size() == 1);
        test = new Voreinstellungen_Feldnamen(null, null, null, null, "Double200", null);
        objekte = testCase.sucheObjekte(test);
        iter = objekte.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        assertEquals(true, objekte.size() == 1);
        test = new Voreinstellungen_Feldnamen(null, null, null, null, null, "TestfeldA");
        objekte = testCase.sucheObjekte(test);
        iter = objekte.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        assertEquals(true, objekte.size() == 1);
    }

    public void testSucheAlle() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        testCase.erzeugen(test);
        Collection alleObjekte = testCase.sucheAlle(Voreinstellungen_Feldnamen.class);
        java.util.Iterator iter = alleObjekte.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        assertEquals(true, alleObjekte.size() == 1);
    }

    public void testLoeschen() {
        prj = new Projekte(new Integer(1), null, null, null);
        prjCase = (UseCase) new UseCaseProjekte(broker);
        prj = (Projekte) prjCase.sucheObjekt(prj);
        erggrpn = new Voreinstellungen_Gruppierungsnamen(new Integer(1), prj, null);
        erggrpn = (Voreinstellungen_Gruppierungsnamen) erggrpnCase.sucheObjekt(erggrpn);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, new Integer(1), "Voreinstellungen_Double", "Double200", "TestfeldA");
        testCase.erzeugen(test);
        test = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, null, null, null, null);
        testCase.loeschen(test);
        Voreinstellungen_Feldnamen test2 = new Voreinstellungen_Feldnamen(new Integer(1), ergugrpn, null, null, null, null);
        assertNull(testCase.sucheObjekt(test2));
        test = new Voreinstellungen_Feldnamen();
    }
}
