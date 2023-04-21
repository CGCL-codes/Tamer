package tjger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import tjger.game.completed.GameConfig;
import tjger.gui.internal.ZoomMenu;
import hgb.gui.HGBaseStatusBar;

/**
 * The tjger status bar. 
 * It has a field for displaying the current zoom. 
 * This field will be definded with <cod>setZoomPanel(int)</code>.
 * 
 * @author hagru
 */
public class MainStatusBar extends HGBaseStatusBar {

    private static final long serialVersionUID = 854371073L;

    private final MainFrame mainFrame;

    private int zoomIndex;

    public MainStatusBar(int[] panelWidth, MainFrame frame) {
        super(panelWidth);
        this.mainFrame = frame;
        this.zoomIndex = -1;
    }

    /**
     * @return The main frame object.
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * If there is a zoom panel, the zoom value displayed there is 
     * set to the current one.
     */
    public void actualizeZoomValue() {
        if (zoomIndex != -1) {
            MainMenu menu = mainFrame.getMainMenu();
            int zoom = (menu != null) ? menu.getZoom() : GameConfig.getInstance().getActiveZoom();
            setText(zoomIndex, zoom + "%");
        }
    }

    /**
     * Makes the panel with the given index to a zoom panel.
     * Set only one zoom panel for a status bar.
     * 
     * @param index Index of the panel, that shall be the zoom panel.
     */
    public void setZoomPanel(int index) {
        JLabel lbZoom = getLabel(index);
        if (lbZoom != null) {
            zoomIndex = index;
            lbZoom.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent ev) {
                    if (ev.getButton() == MouseEvent.BUTTON1 && ev.getClickCount() == 2) {
                        mainFrame.getMainMenu().onAction(ZoomMenu.ZOOMMENU_PREFIX + ZoomMenu.ZOOMMENU_VARIABLE, null);
                    } else if (ev.getButton() == MouseEvent.BUTTON3) {
                        JMenu zoomMenu = mainFrame.getMainMenu().getZoomMenu();
                        JPopupMenu popup = mainFrame.getMainMenu().getZoomPopupMenu();
                        if (zoomMenu != null && popup != null) {
                            int xPos = ev.getX() - zoomMenu.getWidth();
                            int yPos = ev.getY() - zoomMenu.getHeight() * zoomMenu.getMenuComponentCount() - 10;
                            popup.show(ev.getComponent(), xPos, yPos);
                        }
                    }
                }

                public void mouseEntered(MouseEvent ev) {
                }

                public void mouseExited(MouseEvent ev) {
                }

                public void mousePressed(MouseEvent ev) {
                }

                public void mouseReleased(MouseEvent ev) {
                }
            });
            actualizeZoomValue();
        }
    }

    /**
     * Makes the panel with the given index to a zoom panel.
     * Set only one zoom panel for a status bar.
     * 
     * @param index Index of the panel, that shall be the zoom panel.
     * @param zoomPanel A self written zoom panel (label) with an own functionality (menu).
     */
    public void setZoomPanel(int index, JLabel zoomPanel) {
        JPanel pn = getPanel(index);
        if (pn != null) {
            zoomIndex = index;
            pn.remove(getLabel(index));
            pn.add(zoomPanel);
            actualizeZoomValue();
        }
    }
}
