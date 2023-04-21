package client.game.ui.forum;

import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import client.game.ui.event.Dispatcher;

public class InternalFrameTemas extends JInternalFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4900992500811232741L;

    private final int DIMENSIONX = 450;

    private final int DIMENSIONY = 550;

    private ForumTree tree = null;

    private JScrollPane scrollTemas = null;

    private Dispatcher dispacher;

    public Dispatcher getDispacher() {
        return dispacher;
    }

    public InternalFrameTemas(ForumTree tree, Dispatcher dispacher) {
        super("Topic Forum", true, true, true, true);
        this.dispacher = dispacher;
        this.setMinimumSize(new Dimension(DIMENSIONX, DIMENSIONY));
        this.tree = tree;
        this.tree.setSelectionRow(0);
        scrollTemas = new JScrollPane();
        this.setVisible(true);
        this.setSize(DIMENSIONX, DIMENSIONY);
        scrollTemas.setViewportView(new PanelTemas(tree, this));
        this.add(scrollTemas);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
