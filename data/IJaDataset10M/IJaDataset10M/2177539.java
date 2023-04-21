package viewGUI;

import javax.swing.*;
import java.util.*;

/**
 * GPanel ist die Superklasse zu den Geschosspanels
 *      KGPanel
 *      EGPanel
 *      OGPanel
 *
 * GPanel hat keine eigene GUI-Aufgabe, sondern dient als Klasse fuer die
 * gemeinsamen Egenschaften und Methoden der Subklassen. Die Subklassen
 * werden GUI-maessig aufbereitet (Hintergrund, Buttons,...)
 *
 * @author DI Martin Kampenhuber
 * @date August 2006
 */
public class GPanel extends JPanel {

    private static final long serialVersionUID = 2330866515696928328L;

    Schaltzentrale schaltzentrale;

    ImageIcon hintergrund;

    HashSet<HS485SchalterButton> alleLichtschalter;

    HashSet<HS485RolloButton> alleRolloschalter;

    HashSet<HS485Slider> alleDimmer;

    HashSet<HS485OfenButton> alleOfenschalter;

    /** Creates a new instance of GPanel */
    public GPanel() {
        alleLichtschalter = new HashSet<HS485SchalterButton>();
        alleRolloschalter = new HashSet<HS485RolloButton>();
        alleDimmer = new HashSet<HS485Slider>();
        alleOfenschalter = new HashSet<HS485OfenButton>();
    }

    /**
     * Die GUI-Repraesentationen der Schalter, Dimmer,... muessen eine Moeglich-
     * keit haben die Schaltzentrale erfahren zu koennen.
     */
    public Schaltzentrale getSchaltzentrale() {
        return schaltzentrale;
    }

    /**
     * die Methode wird von der Schaltzentrale aufgerufen. Diese muss
     * alle Lichtschalterbuttons des jeweiligen Geschosses kennen.
     */
    public HashSet<HS485SchalterButton> getSchalterGUIs() {
        return alleLichtschalter;
    }

    /**
     * die Methode wird von der Schaltzentrale aufgerufen. Diese muss
     * alle Rollobuttons des jeweiligen Geschosses kennen.
     */
    public HashSet<HS485RolloButton> getRolloGUIs() {
        return alleRolloschalter;
    }

    /**
     * die Methode wird von der Schaltzentrale aufgerufen. Diese muss
     * alle Sliders des jeweiligen Geschosses kennen.
     */
    public HashSet<HS485Slider> getSliderGUIs() {
        return alleDimmer;
    }

    /**
     * die Methode wird von der Schaltzentrale aufgerufen. Diese muss
     * alle Ofenbuttons des jeweiligen Geschosses kennen.
     */
    public HashSet<HS485OfenButton> getOfenGUIs() {
        return alleOfenschalter;
    }
}
