package de.jakop.rugby.anzeige;

import junit.framework.TestCase;
import de.jakop.rugby.spiel.Spiel;
import de.jakop.rugby.spiel.gui.SpielAnzeigePanel;

/** */
public class AnzeigeSpielfeldTest extends TestCase {

    /** */
    public void testKonstruktor() {
        try {
            new SpielAnzeigePanel(null);
            fail("Keine Exception gefangen");
        } catch (NullPointerException e) {
        }
        Spiel hupe = new SpielModelMock();
        new SpielAnzeigePanel(hupe);
    }
}
