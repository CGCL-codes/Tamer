package com.ngenes.mapMaker.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ngenes.mapMaker.io.MapMakerIO;

/**
 * Speichert das Projekt ab. das hei�t die aktuelle Map
 * wird in das bestehende Projektverzeichnis gespeichert.
 * Wenn kein Projektverzeichnis existiert wird ein Neues
 * angelegt. 
 * @author AbeinG
 *
 */
public class SaveListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MapMakerIO.getInstance().saveProcess();
    }
}
