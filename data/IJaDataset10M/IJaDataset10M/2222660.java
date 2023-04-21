package baus.gui.config;

import baus.modell.Baugruppe;
import baus.modell.EnumSchreiner;
import baus.modell.Schreiner;
import baus.modell.Simulation;
import baus.modell.Werkstatt;
import baus.modell.WerkstueckMappe;

/**
 * @author das BAUS! team
 */
final class StartSitzungsRoutineSingleplayer {

    private final ProgrammFenster baus;

    /**
     * Erzeugt ein neues Objekt um mit dem ein Singleplayer-Spiel gestartet
     * werden kann.
     * @param baus Das aufrufende Programmfenster
     */
    protected StartSitzungsRoutineSingleplayer(final ProgrammFenster baus) {
        this.baus = baus;
    }

    /**
     * Erzeugt eine neue Einspieler-Simulation
     * @param werkstatt Die Werkstatt der Simulation
     * @param ausgangsMaterialien Die Ausgangsmaterialien die der Schreiner zum
     *            erreichen seines Auftrags zur Verfügung gestellt bekommt
     * @param schreiner Der Schreiner der Simulation
     * @param auftrag Das Werkstück, dass der Schreiner bauen soll
     * @return <code>true</code> wenn die Sitzung erfolgreich gestartet wurde
     */
    protected boolean startSingleplayerSitzung(final Werkstatt werkstatt, final WerkstueckMappe ausgangsMaterialien, final Schreiner schreiner, final Baugruppe auftrag) {
        werkstatt.addWerkstueckMappe(ausgangsMaterialien, werkstatt.getLagerPos(EnumSchreiner.SCHREINER1));
        final Simulation sim = new Simulation(werkstatt, schreiner, null, auftrag, null);
        baus.loadSimulation(sim);
        return true;
    }
}
