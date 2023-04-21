package rescuecore.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import rescuecore.Memory;
import rescuecore.view.Layer;

/**
 * @author Jono
 *
 */
public class StringHandler implements Handler {

    private int timeStep = -1;

    private JScrollPane pane;

    private JTextArea notes;

    public JComponent getComponent() {
        if (pane == null) {
            JPanel p = new JPanel(new BorderLayout());
            Border bord = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Notes");
            p.setBorder(bord);
            notes = new JTextArea(50, 10);
            p.add(notes, BorderLayout.CENTER);
            pane = new JScrollPane(p);
            pane.setPreferredSize(new Dimension(DebugPane.HANDLER_WIDTH, 200));
        }
        return pane;
    }

    public Layer getLayer() {
        return null;
    }

    public boolean handle(Object o, int timeStep) {
        if (this.timeStep != timeStep) {
            notes.setText("");
            this.timeStep = timeStep;
        }
        if (!(o instanceof String)) return false;
        notes.append(o.toString());
        notes.append("\n");
        return true;
    }

    public void setMemory(Memory m) {
    }
}
