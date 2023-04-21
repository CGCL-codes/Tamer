package jme3clogic.ve;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TriggersDisplay {

    private JPanel component = new JPanel(new BorderLayout());

    public TriggersDisplay() {
        component.setBorder(BorderFactory.createTitledBorder("Available Triggers"));
        final JPanel triggers = new JPanel(new FlowLayout(FlowLayout.LEADING));
        triggers.add(new TriggerLabel(Constants.NEW_EMPTY_TRIGGER));
        component.add(triggers);
        DragSource ds = DragSource.getDefaultDragSource();
        ds.createDefaultDragGestureRecognizer(triggers, DnDConstants.ACTION_LINK, new DragGestureListener() {

            public void dragGestureRecognized(DragGestureEvent e) {
                Component comp = triggers.getComponentAt(e.getDragOrigin());
                if (comp instanceof JLabel) {
                    e.startDrag(DragSource.DefaultLinkDrop, new LocalTransferable(Constants.NEW_EMPTY_TRIGGER));
                }
            }
        });
    }

    public JComponent getComponent() {
        return component;
    }
}

class TriggerLabel extends JLabel {

    TriggerLabel(String text) {
        super(text, JLabel.CENTER);
        setOpaque(true);
        setFont(getFont().deriveFont(18f).deriveFont(Font.BOLD));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
    }
}
