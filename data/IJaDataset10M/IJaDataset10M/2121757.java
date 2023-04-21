package de.jakop.rugby.zustand;

import de.jakop.rugby.types.EingabeNichtZulaessigException;
import de.jakop.rugby.types.ITeam;
import de.jakop.rugby.types.IZustand;
import de.jakop.rugby.zeit.Timer;

/**
 * 
 * @author jakop
 */
public class SetupTest extends ZustandTestHelper {

    protected AbstractZustand createZustand() {
        return new Setup(this.spiel, this.uhr, this.uhrFactory);
    }

    /**
	 * @throws Exception
	 */
    public void testGetLetzterZustand() throws Exception {
        AbstractZustand alt = new Stopp(this.spiel, this.uhr, this.uhrFactory);
        this.spiel.setZustand(alt, true);
        AbstractZustand z = createZustand();
        this.spiel.setZustand(z, false);
        assertEquals(alt, ((Setup) z).getLetzterZustand());
    }

    /** */
    public void testGetAktiveSchalter() {
        IZustand z = createZustand();
        assertEquals(8, z.getAktiveSchalter().size());
    }

    /** */
    public void testGetHupe() {
        IZustand z = createZustand();
        assertEquals(this.spiel, z.getSpiel());
    }

    /** */
    public void testGetUhr() {
        IZustand z = createZustand();
        assertEquals(this.uhr, z.getUhr());
    }

    /** */
    public void testGetSpielzeit() {
        AbstractZustand z = createZustand();
        assertEquals(this.uhr.getRestzeit(), z.getSpielzeit());
    }

    /** */
    public void testSetUhr() {
        AbstractZustand z = createZustand();
        Timer uhr2 = new Timer(1000);
        z.setUhr(uhr2);
        assertEquals(uhr2, z.getUhr());
    }

    /** */
    public void testHupen1x() {
        IZustand z = createZustand();
        try {
            z.hupen1x();
        } catch (EingabeNichtZulaessigException e) {
            return;
        }
        fail("Keine Exception gefangen");
    }

    /** */
    public void testHupen2x() {
        IZustand z = createZustand();
        try {
            z.hupen2x();
        } catch (EingabeNichtZulaessigException e) {
            return;
        }
        fail("Keine Exception gefangen");
    }

    /** */
    public void testStrafzeit() {
        IZustand z = createZustand();
        try {
            z.strafzeit(this.spiel.getTeam(ITeam.TEAM_BLAU));
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }

    /** */
    public void testTor() {
        IZustand z = createZustand();
        try {
            z.tor(this.spiel.getTeam(ITeam.TEAM_BLAU));
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }

    /** */
    public void testFreiwurf() {
        IZustand z = createZustand();
        try {
            z.freiwurf(this.spiel.getTeam(ITeam.TEAM_BLAU));
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }

    /** */
    public void testStrafwurf() {
        IZustand z = createZustand();
        try {
            z.strafwurf(this.spiel.getTeam(ITeam.TEAM_BLAU));
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }

    /** */
    public void testSchiedsrichterball() {
        IZustand z = createZustand();
        try {
            z.schiedsrichterball();
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }

    /** */
    public void testAuszeit() {
        IZustand z = createZustand();
        try {
            z.auszeit(this.spiel.getTeam(ITeam.TEAM_BLAU));
        } catch (EingabeNichtZulaessigException e) {
            fail("Exception gefangen");
        }
    }
}
