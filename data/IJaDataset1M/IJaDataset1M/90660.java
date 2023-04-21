package net.sourceforge.tile3d.view.button;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JRadioButton;

public class TileRadioButton extends JRadioButton implements ItemListener {

    boolean isChosen;

    public TileRadioButton(String st, boolean flag) {
        super(st, flag);
        isChosen = flag;
        addItemListener(this);
    }

    public void itemStateChanged(ItemEvent p_arg0) {
        isChosen = !isChosen;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean p_isChosen) {
        isChosen = p_isChosen;
    }
}
