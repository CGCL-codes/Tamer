package de.jaret.examples.timebars.fzd.ctrl;

import java.util.Iterator;
import de.jaret.examples.timebars.fzd.model.Fahrzeug;
import de.jaret.examples.timebars.fzd.model.Umlauf;
import de.jaret.examples.timebars.fzd.model.UmlaufKette;
import de.jaret.examples.timebars.fzd.model.UmlaufKettenModel;

/**
 * @author Peter Kliem
 * @version $Id: FzdOperations.java 160 2007-01-02 22:02:40Z olk $
 */
public class FzdOperations {

    public static void assign(UmlaufKettenModel kettenModel, UmlaufKette kette, Umlauf umlauf, Fahrzeug fahrzeug) {
        UmlaufKette restKette = new UmlaufKette(kette.getBezeichnung() + "Rest");
        Iterator it = kette.getUmlaeufe().iterator();
        while (it.hasNext()) {
            Umlauf u = (Umlauf) it.next();
            if (u.equals(umlauf)) {
                restKette.addUmlauf(u);
                while (it.hasNext()) {
                    Umlauf uml = (Umlauf) it.next();
                    restKette.addUmlauf(uml);
                }
            }
        }
        it = restKette.getUmlaeufe().iterator();
        while (it.hasNext()) {
            Umlauf u = (Umlauf) it.next();
            kette.remUmlauf(u);
        }
        kettenModel.addUmlaufKette(restKette);
        fahrzeug.addUmlaufKette(restKette);
    }
}
