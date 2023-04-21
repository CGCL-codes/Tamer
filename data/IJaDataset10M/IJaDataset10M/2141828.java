package mainview;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.jdesktop.swingx.JXTable;
import model.storage.Artifact;

/**
 * Class is used to launch a JPopupMenu for Artifacts which is used in the JXTable
 * and the visualisation graph.
 * <p>
 * These are launched when the user has right clicked on a row in the JXTable or a
 * node in the visualisation graph.
 * <p>
 * The menu is kept consistent between the JXTable and Visualisation, except the
 * ability to "zoom" to a node is disabled if the user is in the JXTable and
 * the node is not in the visualisation graph.
 * 
 * @author cbride
 */
public class PopupMenuListenerTable extends MouseAdapter {

    /**
	 * The PopupMenu to launch for the jXTable
	 */
    private JPopupMenu artifactPopupMenu;

    /**
	 * Reference to the JXTable in MainviewView
	 */
    private JXTable jXTable;

    /**
	 * Stores the Artifact that was clicked in the jXTable when this Listener 
	 * is called, it is null if there was no valid Artifact.
	 */
    private Artifact artifact;

    /**
	 * Reference to MainviewView
	 */
    private MainviewView mainview;

    /**
	 *  Button group used for dye options
	 */
    private ButtonGroup buttonGroup;

    /**
	 * Menu used to store colour options
	 */
    private JMenu colourPopupItem;

    /**
	 * Constructor
	 */
    public PopupMenuListenerTable(JPopupMenu artifactPopupMenu, ButtonGroup buttonGroup, JMenu colourPopupItem, JXTable jXTable, MainviewView mainview) {
        this.artifactPopupMenu = artifactPopupMenu;
        this.buttonGroup = buttonGroup;
        this.colourPopupItem = colourPopupItem;
        this.jXTable = jXTable;
        this.mainview = mainview;
    }

    /**
	 * Event called when the mouse is released, and the popup should be shown
	 * @param e
	 */
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }

    /**
	 * Depending on the node that is selected, a right click menu is launched.
	 * @param evt
	 */
    private void showPopup(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            try {
                if (jXTable.getSelectedRow() == -1) {
                    artifact = null;
                    return;
                }
                artifact = mainview.getSelectedArtifactsInTable().get(0);
            } catch (NullPointerException e) {
                artifact = null;
                return;
            }
            if (artifact != null) {
                java.awt.Component components[] = artifactPopupMenu.getComponents();
                for (int i = 0; i < components.length; i++) {
                    JMenuItem menuItem = null;
                    try {
                        menuItem = (JMenuItem) components[i];
                        if (menuItem.getName() != null && menuItem.getName().equals("zoomPopupItem")) {
                            if (artifact.getIsSelected()) {
                                menuItem.setEnabled(true);
                            } else {
                                menuItem.setEnabled(false);
                            }
                            break;
                        }
                    } catch (ClassCastException e) {
                    }
                }
                selectDyeColourInPopup(buttonGroup, artifact);
                artifactPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
            return;
        }
    }

    /**
	* Calls the method to select the relevant menu item in the JPopup
	* to indicate which colour has been selected as the dye source. If
	* no colour is selected, then the default option is.
	* 
	* @param buttonGroup buttons containing the colour options
	* @param artifact artifact that has launched the popup menu
	*/
    public static void selectDyeColourInPopup(ButtonGroup buttonGroup, Artifact artifact) {
        Color colour = artifact.getColour();
        String colourString = null;
        if (colour == null) {
        } else if (colour == Color.RED) {
            colourString = "Red";
        } else if (colour == Color.BLUE) {
            colourString = "Blue";
        } else if (colour == Color.BLACK) {
            colourString = "Black";
        } else if (colour == Color.ORANGE) {
            colourString = "Orange";
        } else if (colour == Color.PINK) {
            colourString = "Pink";
        } else if (colour == Color.GREEN) {
            colourString = "Green";
        } else if (colour == Color.WHITE) {
            colourString = "White";
        } else if (colour == Color.YELLOW) {
            colourString = "Yellow";
        } else if (colour == Color.MAGENTA) {
            colourString = "Magenta";
        }
        selectDyePopupItem(buttonGroup, colourString);
    }

    /**
	* Select the relevant menu item in the JPopup to indicate which colour 
	* has been selected as the dye source. The JRadioBoxMenuItem corresponding
	* to the <code>colour</code> is selected.
	* 
	* @param buttonGroup buttons containing the colour options
	* @param colour name of the colour to select JRadioBoxMenuItem for 
	*/
    public static void selectDyePopupItem(ButtonGroup buttonGroup, String colour) {
        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        buttonGroup.clearSelection();
        while (buttons.hasMoreElements()) {
            JRadioButtonMenuItem button = (JRadioButtonMenuItem) buttons.nextElement();
            if (colour == null) {
                if (button.getName().equals("dyeDefaultPopupMenuItem")) {
                    buttonGroup.setSelected(button.getModel(), true);
                }
            } else if (button.getName().equals("dye" + colour + "PopupMenuItem")) {
                buttonGroup.setSelected(button.getModel(), true);
                return;
            }
        }
        return;
    }
}
