package Popup;

import Composite.*;
import java.awt.event.*;
import java.io.IOException;

class ControleurLocaliserSurLeDisque implements ActionListener {

    Element el;

    public ControleurLocaliserSurLeDisque(Element el) {
        this.el = el;
    }

    public void actionPerformed(ActionEvent e) {
        String commande = "";
        if (el instanceof Dossier) {
            commande = "explorer ";
        } else {
            commande = "rundll32.exe C:\\WINDOWS\\System32\\shimgvw.dll,ImageView_Fullscreen ";
        }
        try {
            Runtime.getRuntime().exec(commande + el.getFichierCourant().getAbsolutePath());
        } catch (IOException ex) {
        }
    }
}
