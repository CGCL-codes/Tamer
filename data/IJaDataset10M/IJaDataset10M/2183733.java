package GEMpire;

import java.awt.*;
import java.awt.event.*;

public class StringDialog extends Dialog {

    TextField tf;

    Button ok = new Button("ok");

    StringDialog(Frame f, String title, String initialValue) {
        super(f, title, true);
        setLayout(new BorderLayout());
        Panel p = new Panel(new FlowLayout());
        p.add(tf = new TextField(initialValue, 3));
        add("North", p);
        p = new Panel(new FlowLayout());
        p.add(ok);
        add("South", p);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (event.getSource() instanceof Button) {
                    setVisible(false);
                }
            }
        });
    }

    String getString() {
        return tf.getText();
    }
}
