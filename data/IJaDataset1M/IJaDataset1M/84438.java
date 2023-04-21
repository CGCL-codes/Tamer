package de.harizen.mapcreator.graphics.panels;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import de.harizen.mapcreator.graphics.map.Mapable;

public abstract class EditorDialog<T extends Mapable> extends JDialog {

    public EditorDialog(Frame owner) {
        super(owner);
    }

    public abstract JButton getOkButton();

    public abstract JButton getCancelButton();

    public abstract void setData(T mapable);
}
