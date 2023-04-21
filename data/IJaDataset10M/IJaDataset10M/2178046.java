package Forms.StdComponents;

import java.awt.*;
import java.awt.event.*;

/** Dialog zur Ausgabe der Bilanz */
public class ShowBalanceDialog extends Dialog {

    private EnterDateDialog dia;

    /** - erzeugt neuen Dialog <br>
      - f - zugeh. Frame <br>
      - d - zugeh. Dialog zur Datumseingabe <br>
      - title - Titel <br>
      - s - alle bilanzierbaren Daten */
    public ShowBalanceDialog(Frame f, EnterDateDialog d, String title, String s) {
        super(f, title, true);
        dia = d;
        setBackground(Color.lightGray);
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(grid);
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setText(s);
        gc.gridx = 0;
        gc.gridy = 0;
        grid.setConstraints(ta, gc);
        add(ta);
        Button b = new Button("Schlie�en");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                dia.update();
            }
        });
        gc.gridx = 0;
        gc.gridy = GridBagConstraints.RELATIVE;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);
        grid.setConstraints(b, gc);
        add(b);
        pack();
        setVisible(true);
    }
}
