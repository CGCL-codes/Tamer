package us.jonesrychtar.gispatialnet.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JToolBar;
import us.jonesrychtar.gispatialnet.gui.GSNPanel.GSNPanel;

/**
 * @author sam
 *
 */
public class GSNToolBar extends JToolBar implements MouseListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3751834491825837993L;

    JButton exitBtn, openBtn, saveBtn;

    GSNPanel thePanel;

    GSNStatusBarInterface theStatus;

    public GSNToolBar() {
        exitBtn = GUIutil.addToolBarButton(this, "Exit", "Exit program", "Stop", "exit");
        saveBtn = GUIutil.addToolBarButton(this, "Save", "Save data", "Save", "save");
        openBtn = GUIutil.addToolBarButton(this, "Open", "Open a file", "Open", "open");
    }

    /**Since this toolbar works with a GSNPanel, we need a reference to it. 
	 * The GSNPanel is the Panel that handles the ActionEvents for 
	 * the buttons. 
	 * @param gsp the GSNPanel which we wish to control 
	 */
    public void setActionPanel(GSNPanel gsp) {
        this.thePanel = gsp;
        registerPanel();
    }

    private void registerPanel() {
        exitBtn.addActionListener(thePanel);
        exitBtn.addMouseListener(this);
        openBtn.addActionListener(thePanel);
        saveBtn.addActionListener(thePanel);
    }

    public void setStatusBar(GSNStatusBarInterface theStatus) {
        this.theStatus = theStatus;
    }

    public void mouseEntered(MouseEvent e) {
        JButton theItem = (JButton) e.getSource();
        theStatus.setStatus(theItem.getToolTipText());
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        theStatus.setStatus(" ");
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
