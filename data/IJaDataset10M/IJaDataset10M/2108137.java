package jpen.demo;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTextField;
import jpen.event.PenAdapter;
import jpen.Pen;
import jpen.PKind;
import jpen.PKindEvent;

class KindPanel {

    final JTextField kindTextField = new JTextField(6);

    {
        kindTextField.setEditable(false);
        kindTextField.setHorizontalAlignment(JTextField.CENTER);
    }

    final JComponent panel = Box.createVerticalBox();

    {
        panel.add(Utils.labelComponent(null, kindTextField));
    }

    KindPanel(Pen pen) {
        pen.addListener(new PenAdapter() {

            private Pen pen;

            @Override
            public void penKindEvent(PKindEvent ev) {
                pen = ev.pen;
            }

            @Override
            public void penTock(long availableMillis) {
                if (pen != null) update(pen.getKind());
                pen = null;
            }
        });
        update(pen.getKind());
    }

    void update(PKind kind) {
        kindTextField.setText(PKindTypeNumberCombo.getPKindTypeStringValue(kind.typeNumber));
    }
}
