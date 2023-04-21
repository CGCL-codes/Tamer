package de.jakop.rugby;

import javax.swing.JFrame;
import org.apache.commons.configuration.Configuration;
import de.jakop.rugby.protokoll.gui.AnzeigeElementProtokoll;
import de.jakop.rugby.spiel.gui.SpielAnzeigePanel;
import de.jakop.rugby.spiel.gui.TorPanel;
import de.jakop.rugby.strafzeit.gui.StrafzeitPnl;
import de.jakop.rugby.types.IAnzeigeFactory;
import de.jakop.rugby.types.IBoFactory;
import de.jakop.rugby.types.ISpiel;
import de.jakop.rugby.types.ITeam;
import de.jakop.rugby.types.IUhrFactory;
import de.jakop.rugby.zeit.Timer;
import de.jakop.rugby.zeit.gui.ZeitPnl;
import de.jakop.rugby.zustand.gui.ZustandPnl;

/**
 * Erzeugt die konfigurierbaren Objekte anhand der Konfiguration
 * 
 * @author jakop
 */
public class ObjektFactory implements IUhrFactory, IAnzeigeFactory {

    private Configuration konf;

    private IBoFactory factory;

    private de.jakop.rugby.ObjektFactory2 anzeigeFactory;

    /**
	 * @param konf
	 */
    public ObjektFactory(Configuration konf) {
        this.konf = konf;
        this.anzeigeFactory = new de.jakop.rugby.ObjektFactory2(this.konf);
    }

    /**
	 * @return Timer mit der Spielzeitdauer
	 */
    public Timer createSpielzeitUhr() {
        Timer uhr = this.factory.createITimerInstance(1000);
        uhr.setRestzeit(this.konf.getInt("game.halftime.duration"));
        return uhr;
    }

    /**
	 * @return Timer mit der Strafwurfdauer
	 */
    public Timer createStrafwurfUhr() {
        Timer uhr = this.factory.createITimerInstance(1000);
        uhr.setRestzeit(this.konf.getInt("game.penaltythrow.duration"));
        return uhr;
    }

    /**

	 * @return Timer mit der Auszeitdauer
	 */
    public Timer createAuszeitUhr() {
        Timer uhr = this.factory.createITimerInstance(1000);
        uhr.setRestzeit(this.konf.getInt("game.timeout.duration"));
        return uhr;
    }

    /**
	 * @return Timer mit der Strafzeitdauer
	 */
    public Timer createStrafzeitUhr() {
        Timer uhr = this.factory.createITimerInstance(1000);
        uhr.setRestzeit(this.konf.getInt("game.timepenalty.duration"));
        return uhr;
    }

    /**
	 * @return Timer mit der Halbzeitdauer
	 */
    public Timer createHalbzeitUhr() {
        Timer uhr = this.factory.createITimerInstance(1000);
        uhr.setRestzeit(this.konf.getInt("game.halftimebreak.duration"));
        return uhr;
    }

    /**
	 * @return Anzeige f�r die Spielzeit
	 * @see de.jakop.rugby.ObjektFactory2#createAnzeigeElementSpielzeit()
	 */
    public ZeitPnl createAnzeigeElementSpielzeit() {
        return this.anzeigeFactory.createAnzeigeElementSpielzeit();
    }

    /**
	 * @param h
	 * @param t
	 * @return Anzeige f�r die Strafzeit
	 */
    public StrafzeitPnl createAnzeigeElementStrafzeit(ISpiel h, ITeam t) {
        return this.anzeigeFactory.createAnzeigeElementStrafzeit(h, t);
    }

    /**
	 * @param h
	 * @param t
	 * @param blau 
	 * @return Anzeige f�r die Tore eines Teams
	 */
    public TorPanel createAnzeigeElementTor(ISpiel h, ITeam t, boolean blau) {
        return this.anzeigeFactory.createAnzeigeElementTor(h, t, blau);
    }

    /**
	 * @return Anzeige f�r den Spielzustand
	 * @see de.jakop.rugby.ObjektFactory2#createAnzeigeElementZustand()
	 */
    public ZustandPnl createAnzeigeElementZustand() {
        return this.anzeigeFactory.createAnzeigeElementZustand();
    }

    /**
	 * @param hupe
	 * @return Eine Anzeige ohne Kontrollm�glichkeit f�r die Hupe
	 */
    public JFrame createAnzeigeSpielfeld(ISpiel hupe) {
        SpielAnzeigePanel d = new SpielAnzeigePanel(hupe);
        JFrame f = new JFrame();
        f.add(d);
        f.pack();
        return f;
    }

    /**
	 * @param hupe
	 * @return Eine Anzeige f�r das Spielverlaufsprotokoll, teilweise editierbar
	 */
    public AnzeigeElementProtokoll createAnzeigeElementProtokoll(ISpiel hupe) {
        AnzeigeElementProtokoll d = this.anzeigeFactory.createAnzeigeElementProtokoll(hupe);
        return d;
    }
}
