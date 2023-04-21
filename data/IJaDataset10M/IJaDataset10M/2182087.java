package GUI.Toolkit;

import java.awt.*;
import java.awt.event.*;

/**
 Diese Klasse implementiert die Methode windowClosing aus WindowAdapter und
 kann genutzt werden, um ein Fenster zu schliessen. Dabei wird das Programm
 nicht beendet.
 @author Jan Dittberner <a href="mailto:jd9@irz.inf.tu-dresden.de?subject=SWT-Kino 98 class GUI.Toolkit.FensterSchliesser">jd9@irz.inf.tu-dresden.de</a>
 @see ProgrammSchliesser
 */
public class FensterSchliesser extends WindowAdapter {

    /**
     Erzeugt einen neuen Fensterschliesser.
     */
    public FensterSchliesser() {
        super();
    }

    /**
     Schlie�t das Fenster.
     @param event WindowEvent, da� an den WindowListener �bergeben wurde
     */
    public void windowClosing(WindowEvent event) {
        event.getWindow().dispose();
    }
}
