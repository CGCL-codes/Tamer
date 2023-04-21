package interfaces.rechercheiti;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FlecheMonter extends JButton {

    PanelPref p;

    public FlecheMonter(PanelPref _p) {
        p = _p;
        setText("/\\");
        setMargin(new Insets(0, 0, 0, 0));
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                p.signalerMonter();
            }
        });
    }
}
