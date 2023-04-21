package gui.listener;

import java.awt.event.ActionEvent;
import gui.*;

public class MnuViewStandard extends MainActionListener {

    public MnuViewStandard(Object obj) {
        component = obj;
    }

    public void actionPerformed(ActionEvent arg0) {
        ((WMain) component).setWindowPos();
    }
}
