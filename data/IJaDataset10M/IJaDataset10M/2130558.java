package de.jakop.rugby;

import java.awt.Color;
import java.awt.Font;
import org.apache.commons.configuration.Configuration;
import de.jakop.rugby.anzeige.PanelSettings;
import de.jakop.rugby.protokoll.gui.AnzeigeElementProtokoll;
import de.jakop.rugby.spiel.gui.TorPanel;
import de.jakop.rugby.strafzeit.gui.StrafzeitPnl;
import de.jakop.rugby.types.ISpiel;
import de.jakop.rugby.types.ITeam;
import de.jakop.rugby.zeit.gui.ZeitPnl;
import de.jakop.rugby.zustand.gui.ZustandPnl;

/**
 * Erzeugt die konfigurierbaren Objekte anhand der Konfiguration
 * 
 * @author jakop
 */
public class ObjektFactory2 {

    private Configuration konf;

    /**
	 * @param konf
	 */
    public ObjektFactory2(Configuration konf) {
        this.konf = konf;
    }

    /**
	 * @return ein {@link ZustandPnl}
	 */
    public ZustandPnl createAnzeigeElementZustand() {
        Color fg = new Color(Integer.parseInt(this.konf.getString("display.state.foreground"), 16));
        Color bg = new Color(Integer.parseInt(this.konf.getString("display.state.background"), 16));
        Font f = new Font(this.konf.getString("display.state.font.type"), Font.PLAIN, this.konf.getInt("display.state.font.gr��e"));
        ZustandPnl anz = new ZustandPnl(bg, fg, f);
        return anz;
    }

    /**
	 * @return ein {@link ZeitPnl}
	 */
    public ZeitPnl createAnzeigeElementSpielzeit() {
        Color fg = new Color(Integer.parseInt(this.konf.getString("display.time.foreground"), 16));
        Color bg = new Color(Integer.parseInt(this.konf.getString("display.time.background"), 16));
        Font f = new Font(this.konf.getString("display.time.font.type"), Font.PLAIN, this.konf.getInt("display.time.font.gr��e"));
        PanelSettings s = new PanelSettings(fg, bg, f);
        ZeitPnl anz = new ZeitPnl(s);
        return anz;
    }

    /**
	 * @param h 
	 * @param t 
	 * @param blau 
	 * @return ein {@link TorPanel}
	 */
    public TorPanel createAnzeigeElementTor(ISpiel h, ITeam t, boolean blau) {
        Color fg = new Color(Integer.parseInt(this.konf.getString("display.goal.foreground." + (blau ? "blue" : "white")), 16));
        Color bg = new Color(Integer.parseInt(this.konf.getString("display.goal.background." + (blau ? "blue" : "white")), 16));
        Font f = new Font(this.konf.getString("display.goal.font.type"), Font.PLAIN, this.konf.getInt("display.goal.font.gr��e"));
        PanelSettings s = new PanelSettings(fg, bg, f);
        TorPanel anz = new TorPanel(h, t, s);
        return anz;
    }

    /**
	 * @param h 
	 * @param t 
	 * @return ein {@link StrafzeitPnl}
	 */
    public StrafzeitPnl createAnzeigeElementStrafzeit(ISpiel h, ITeam t) {
        StrafzeitPnl anz = new StrafzeitPnl(h, t);
        return anz;
    }

    /**
	 * @param h
	 * @return ein Anzeigepanel f�r Protokolleintr�ge
	 */
    public AnzeigeElementProtokoll createAnzeigeElementProtokoll(ISpiel h) {
        AnzeigeElementProtokoll anz = new AnzeigeElementProtokoll();
        anz.setHupe(h);
        return anz;
    }
}
