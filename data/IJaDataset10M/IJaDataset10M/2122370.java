package de.dokchess.regeln.gangarten;

import static de.dokchess.allgemein.Felder.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import de.dokchess.allgemein.Farbe;
import de.dokchess.allgemein.Feld;
import de.dokchess.allgemein.Figur;
import de.dokchess.allgemein.FigurenArt;
import de.dokchess.allgemein.Stellung;
import de.dokchess.allgemein.Zug;

public class SpringerZuegeTest {

    private static final Figur SPRINGER_WEISS = new Figur(FigurenArt.SPRINGER, Farbe.WEISS);

    @Test
    public void springerRad() {
        Stellung weisserSpringerD5 = new Stellung("8/8/8/3N4/8/8/8/8 w - - 0 1");
        SpringerZuege springerZuege = new SpringerZuege();
        List<Zug> zuege = new ArrayList<Zug>();
        springerZuege.fuegeZugkandidatenHinzu(d5, weisserSpringerD5, zuege);
        Assert.assertEquals(8, zuege.size());
        for (Zug zug : zuege) {
            Assert.assertEquals(d5, zug.getVon());
        }
        Feld[] ziele = { c7, e7, b6, f6, b4, f4, c3, e3 };
        for (Feld ziel : ziele) {
            Zug zuTesten = new Zug(SPRINGER_WEISS, d5, ziel);
            Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
        }
    }

    @Test
    public void einzelnerSpringerInDerEcke() {
        {
            Stellung weisserSpringerA1 = new Stellung("8/8/8/8/8/8/8/N7 w - - 0 1");
            SpringerZuege springerZuege = new SpringerZuege();
            List<Zug> zuege = new ArrayList<Zug>();
            springerZuege.fuegeZugkandidatenHinzu(a1, weisserSpringerA1, zuege);
            Assert.assertEquals(2, zuege.size());
            for (Zug zug : zuege) {
                Assert.assertEquals(a1, zug.getVon());
            }
            Feld[] ziele = { b3, c2 };
            for (Feld ziel : ziele) {
                Zug zuTesten = new Zug(SPRINGER_WEISS, a1, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
        {
            Stellung weisserSpringerH8 = new Stellung("7N/8/8/8/8/8/8/8 w - - 0 1");
            SpringerZuege springerZuege = new SpringerZuege();
            List<Zug> zuege = new ArrayList<Zug>();
            springerZuege.fuegeZugkandidatenHinzu(h8, weisserSpringerH8, zuege);
            Assert.assertEquals(2, zuege.size());
            for (Zug zug : zuege) {
                Assert.assertEquals(h8, zug.getVon());
            }
            Feld[] ziele = { g6, f7 };
            for (Feld ziel : ziele) {
                Zug zuTesten = new Zug(SPRINGER_WEISS, h8, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void einzelnerSpringerSchlaegt() {
        Stellung weisserSpringKannZweiSchlagen = new Stellung("8/8/5B2/3N4/1n6/4r3/8/8 w - - 0 1");
        SpringerZuege springerZuege = new SpringerZuege();
        List<Zug> zuege = new ArrayList<Zug>();
        springerZuege.fuegeZugkandidatenHinzu(d5, weisserSpringKannZweiSchlagen, zuege);
        Assert.assertEquals(7, zuege.size());
        for (Zug zug : zuege) {
            Assert.assertEquals(d5, zug.getVon());
        }
        Zug schwarzenSpringerSchlagen = new Zug(SPRINGER_WEISS, d5, b4, true);
        Zug schwarzenTurmSchlagen = new Zug(SPRINGER_WEISS, d5, e3, true);
        Assert.assertTrue(schwarzenSpringerSchlagen.toString(), zuege.contains(schwarzenSpringerSchlagen));
        Assert.assertTrue(schwarzenTurmSchlagen.toString(), zuege.contains(schwarzenTurmSchlagen));
    }
}
