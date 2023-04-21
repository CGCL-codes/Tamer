package cw.boardingschoolmanagement.gui.component;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author ManuelG
 */
public class CWPanel extends JPanel {

    public CWPanel() {
        this(new FlowLayout(FlowLayout.LEFT));
    }

    public CWPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }
}
