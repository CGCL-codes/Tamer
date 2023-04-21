package fr.vtt.gattieres.gcs.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class QuickSaveAction extends CommonAction {

    public QuickSaveAction(String name, ImageIcon icon) {
        super(name, icon);
    }

    @Override
    public void updateActionState() {
        this.setEnabled(super.getCurrentRace() != null);
    }

    @Override
    public void perform(ActionEvent e) {
    }
}
