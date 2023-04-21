package client.game.ui.forum;

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import client.game.ui.event.Dispatcher;

public class InternalFrameTree extends JInternalFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private PanelTree panelTree = null;

    private Dispatcher dispacher;

    public InternalFrameTree(Dispatcher dispacher) {
        super("Tree Forum", true, true, true, true);
        this.setDispacher(dispacher);
        this.setVisible(true);
        this.setLocation(400, 0);
        this.setSize(300, 300);
        panelTree = new PanelTree(dispacher);
        this.add(panelTree);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public ForumTree GetTree() {
        return panelTree.GetTree();
    }

    public void setDispacher(Dispatcher dispacher) {
        this.dispacher = dispacher;
    }

    public Dispatcher getDispacher() {
        return dispacher;
    }
}
