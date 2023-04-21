package net.doepner.ws.model.de;

import net.doepner.ws.model.Categories;
import net.doepner.ws.model.Verb;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Testet die Konjugation deutscher Verben
 */
public class DeutschesVerbTest {

    @Test
    public void testKonjugation() {
        checkFormen(new DeutschesVerb("gehen", "ging", "gegangen"), new String[][] { { "gehe", "gehst", "geht" }, { "gehen", "geht", "gehen" } }, new String[][] { { "ging", "gingst", "ging" }, { "gingen", "gingt", "gingen" } });
        checkFormen(new DeutschesVerb("leben", "lebte", "gelebt"), new String[][] { { "lebe", "lebst", "lebt" }, { "leben", "lebt", "leben" } }, new String[][] { { "lebte", "lebtest", "lebte" }, { "lebten", "lebtet", "lebten" } });
        checkFormen(new DeutschesVerb("denken", "dachte", "gedacht"), new String[][] { { "denke", "denkst", "denkt" }, { "denken", "denkt", "denken" } }, new String[][] { { "dachte", "dachtest", "dachte" }, { "dachten", "dachtet", "dachten" } });
        checkFormen(new DeutschesVerb("zweifeln", "zweifelte", "gezweifelt"), new String[][] { { "zweifele", "zweifelst", "zweifelt" }, { "zweifeln", "zweifelt", "zweifeln" } }, new String[][] { { "zweifelte", "zweifeltest", "zweifelte" }, { "zweifelten", "zweifeltet", "zweifelten" } });
        checkFormen(Hilfsverb.SEIN, new String[][] { { "bin", "bist", "ist" }, { "sind", "seid", "sind" } }, new String[][] { { "war", "warst", "war" }, { "waren", "wart", "waren" } });
        checkFormen(Hilfsverb.HABEN, new String[][] { { "habe", "hast", "hat" }, { "haben", "habt", "haben" } }, new String[][] { { "hatte", "hattest", "hatte" }, { "hatten", "hattet", "hatten" } });
        checkFormen(new DeutschesVerb("warten", "wartete", "gewartet"), new String[][] { { "warte", "wartest", "wartet" }, { "warten", "wartet", "warten" } }, new String[][] { { "wartete", "wartetest", "wartete" }, { "warteten", "wartetet", "warteten" } });
        final DeutschesVerb treten = new DeutschesVerb("treten", "trat", "getreten");
        treten.setLautwechsel(new Lautwechsel("et", "itt"));
        checkFormen(treten, new String[][] { { "trete", "trittst", "tritt" }, { "treten", "tretet", "treten" } }, new String[][] { { "trat", "tratst", "trat" }, { "traten", "tratet", "traten" } });
        final DeutschesVerb laufen = new DeutschesVerb("laufen", "lief", "gelaufen");
        laufen.setLautwechsel(new Lautwechsel("a", "ä"));
        laufen.setHilfsverbSein(true);
        checkFormen(laufen, new String[][] { { "laufe", "läufst", "läuft" }, { "laufen", "lauft", "laufen" } }, new String[][] { { "lief", "liefst", "lief" }, { "liefen", "lieft", "liefen" } });
        final DeutschesVerb wissen = new DeutschesVerb("wissen", "wusste", "gewusst");
        wissen.setPraesensSingularStamm("weiß");
        checkFormen(wissen, new String[][] { { "weiß", "weißt", "weiß" }, { "wissen", "wisst", "wissen" } }, new String[][] { { "wusste", "wusstest", "wusste" }, { "wussten", "wusstet", "wussten" } });
    }

    private void checkFormen(Verb verb, String[][] praesensFormen, String[][] praeteritumFormen) {
        for (Categories.Numerus n : Categories.Numerus.values()) {
            for (Categories.Person p : Categories.Person.values()) {
                assertEquals("Präsens, " + n + ", " + p, praesensFormen[n.ordinal()][p.ordinal()], verb.getSimplePresent(n, p));
                assertEquals("Präteritum, " + n + ", " + p, praeteritumFormen[n.ordinal()][p.ordinal()], verb.getSimplePast(n, p));
            }
        }
    }
}
