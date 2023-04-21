package de.dokchess.regeln;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import de.dokchess.allgemein.Farbe;
import de.dokchess.allgemein.Stellung;

public class AufSchachPruefenTest {

    private Spielregeln spielregeln;

    @Before
    public void setup() {
        spielregeln = new SpielregelnImpl();
    }

    @Test
    public void weisserBauerGibtSchach() {
        Stellung stellung1 = new Stellung("8/5k2/6P1/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung1, Farbe.SCHWARZ));
        Stellung stellung2 = new Stellung("8/5k2/4P3/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung2, Farbe.SCHWARZ));
    }

    @Test
    public void schwarzerBauerGibtSchach() {
        Stellung stellung1 = new Stellung("4k3/8/8/8/8/2p5/3K4/8 b - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung1, Farbe.WEISS));
        Stellung stellung2 = new Stellung("4k3/8/8/8/8/4p3/3K4/8 b - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung2, Farbe.WEISS));
    }

    @Test
    public void weisseDameGibtSchach() {
        Stellung stellung1 = new Stellung("8/5k2/8/8/8/8/8/2K2Q2 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung1, Farbe.SCHWARZ));
        Stellung stellung2 = new Stellung("8/5k2/8/8/8/8/Q7/2K5 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung2, Farbe.SCHWARZ));
        Stellung stellung3 = new Stellung("8/5k2/8/8/2R5/8/Q7/2K5 w - - 0 1");
        Assert.assertFalse("nicht im Schach", spielregeln.aufSchachPruefen(stellung3, Farbe.SCHWARZ));
    }

    @Test
    public void weisserLaeuferGibtSchach() {
        Stellung stellung1 = new Stellung("8/5k2/6B1/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung1, Farbe.SCHWARZ));
        Stellung stellung2 = new Stellung("8/5k2/8/8/8/8/B7/2K5 w - - 0 1");
        Assert.assertTrue("im Schach", spielregeln.aufSchachPruefen(stellung2, Farbe.SCHWARZ));
        Stellung stellung3 = new Stellung("8/5k2/8/8/2R5/8/B7/2K5 w - - 0 1");
        Assert.assertFalse("nicht im Schach", spielregeln.aufSchachPruefen(stellung3, Farbe.SCHWARZ));
    }
}
