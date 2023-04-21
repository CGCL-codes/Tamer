package se.entitymanager.presentation.swing;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import se.entitymanager.logic.EntityInterface;
import se.entitymanager.logic.LogicFacade;
import se.entitymanager.presentation.CopyAction;
import se.entitymanager.presentation.CopyToAction;
import se.entitymanager.presentation.CutAction;
import se.entitymanager.presentation.CutToAction;
import se.entitymanager.presentation.DeleteAction;
import se.entitymanager.presentation.HelpAction;
import se.entitymanager.presentation.OpenAction;
import se.entitymanager.presentation.PasteAction;
import se.entitymanager.presentation.QuitAction;
import se.entitymanager.presentation.RenameAction;
import se.entitymanager.presentation.ShowPropertiesAction;

/**
 * Tool bar for presentation with Swing framework.<p>
 * The class initializes a <code>JToolBar</code> .
 * <code>FocusListener</code> is implemented 
 * for recognizing the currently selected entity in one 
 * of the trees displayed.
 * Shares Actions with <code>SwingMenuBar</code> and
 * <code>SwingPopupMenu</code>.
 */
public class SwingToolBar extends JToolBar implements FocusListener, TreeSelectionListener {

    /**
     * The presentation facade to which this object belongs,
     * 
     * @uml.property name="presentationFacade"
     * @uml.associationEnd 
     * @uml.property name="presentationFacade" multiplicity="(1 1)"
     */
    private SwingPresentationFacade presentationFacade;

    /**
     * The logic facade to access entities.
     * 
     * @uml.property name="logicFacade"
     * @uml.associationEnd 
     * @uml.property name="logicFacade" multiplicity="(1 1)"
     */
    private LogicFacade logicFacade;

    /**
     * The entity to be edited.
     * 
     * @uml.property name="entity"
     * @uml.associationEnd 
     * @uml.property name="entity" multiplicity="(0 1)"
     */
    private EntityInterface entity;

    /**
     * The Actions
     * 
     * @uml.property name="openAction"
     * @uml.associationEnd 
     * @uml.property name="openAction" multiplicity="(1 1)"
     */
    private OpenAction openAction;

    /**
     * 
     * @uml.property name="showPropertiesAction"
     * @uml.associationEnd 
     * @uml.property name="showPropertiesAction" multiplicity="(1 1)"
     */
    private ShowPropertiesAction showPropertiesAction;

    /**
     * 
     * @uml.property name="cutAction"
     * @uml.associationEnd 
     * @uml.property name="cutAction" multiplicity="(1 1)"
     */
    private CutAction cutAction;

    /**
     * 
     * @uml.property name="copyAction"
     * @uml.associationEnd 
     * @uml.property name="copyAction" multiplicity="(1 1)"
     */
    private CopyAction copyAction;

    /**
     * 
     * @uml.property name="pasteAction"
     * @uml.associationEnd 
     * @uml.property name="pasteAction" multiplicity="(1 1)"
     */
    private PasteAction pasteAction;

    /**
     * 
     * @uml.property name="cutToAction"
     * @uml.associationEnd 
     * @uml.property name="cutToAction" multiplicity="(1 1)"
     */
    private CutToAction cutToAction;

    /**
     * 
     * @uml.property name="copyToAction"
     * @uml.associationEnd 
     * @uml.property name="copyToAction" multiplicity="(1 1)"
     */
    private CopyToAction copyToAction;

    /**
     * 
     * @uml.property name="renameAction"
     * @uml.associationEnd 
     * @uml.property name="renameAction" multiplicity="(1 1)"
     */
    private RenameAction renameAction;

    /**
     * 
     * @uml.property name="deleteAction"
     * @uml.associationEnd 
     * @uml.property name="deleteAction" multiplicity="(1 1)"
     */
    private DeleteAction deleteAction;

    /**
	 * Path to icon for application tool bar function 'open'.
	 */
    private static final String TOOLBAR_OPEN_ICON_NAME = "/icons/tb-open24.png";

    /**
	 * Path to icon for application tool bar function 'properties'.
	 */
    private static final String TOOLBAR_PROPERTIES_ICON_NAME = "/icons/tb-properties24.png";

    /**
	 * Path to icon for application tool bar function 'cut'.
	 */
    private static final String TOOLBAR_CUT_ICON_NAME = "/icons/tb-cut24.png";

    /**
	 * Path to icon for application tool bar function 'copy'.
	 */
    private static final String TOOLBAR_COPY_ICON_NAME = "/icons/tb-copy24.png";

    /**
	 * Path to icon for application tool bar function 'paste'.
	 */
    private static final String TOOLBAR_PASTE_ICON_NAME = "/icons/tb-paste24.png";

    /**
	 * Path to icon for application tool bar function 'copy to'.
	 */
    private static final String TOOLBAR_COPYTO_ICON_NAME = "/icons/tb-copyto24.png";

    /**
	 * Path to icon for application tool bar function 'cut to'.
	 */
    private static final String TOOLBAR_CUTTO_ICON_NAME = "/icons/tb-cutto24.png";

    /**
	 * Path to icon for application tool bar function 'rename'.
	 */
    private static final String TOOLBAR_RENAME_ICON_NAME = "/icons/tb-edit24.png";

    /**
	 * Path to icon for application tool bar function 'delete'.
	 */
    private static final String TOOLBAR_DELETE_ICON_NAME = "/icons/tb-delete24.png";

    /**
	 * Path to icon for application tool bar function 'help'.
	 */
    private static final String TOOLBAR_HELP_ICON_NAME = "/icons/tb-help24.png";

    /**
	 * Path to icon for application tool bar function 'close'.
	 */
    private static final String TOOLBAR_CLOSE_ICON_NAME = "/icons/tb-close24.png";

    /**
	 * Constructs a tool bar for a <code>presentationFacade</code>.<p>
	 * @param presentationFacade the presentation facade to use
	 */
    protected SwingToolBar(SwingPresentationFacade presentationFacade) {
        this.presentationFacade = presentationFacade;
        this.logicFacade = presentationFacade.getLogicFacade();
        createGUI();
        ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getLeftTree().addFocusListener(this);
        ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getRightTree().addFocusListener(this);
        ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getLeftTree().addTreeSelectionListener(this);
        ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getRightTree().addTreeSelectionListener(this);
    }

    /**
	 * Creates the GUI elements of the tool bar.<p>
	 * Initializes the buttons and actions.
	 */
    private void createGUI() {
        JButton button;
        this.setFloatable(false);
        this.setRollover(true);
        this.openAction = new OpenAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_OPEN_ICON_NAME)), "Textdatei �ffnen", new Integer(KeyEvent.VK_O), presentationFacade);
        openAction.setEnabled(false);
        button = new JButton(openAction);
        this.add(button);
        this.showPropertiesAction = new ShowPropertiesAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_PROPERTIES_ICON_NAME)), "Dateieigenschaften anzeigen", new Integer(KeyEvent.VK_E), presentationFacade);
        showPropertiesAction.setEnabled(false);
        button = new JButton(showPropertiesAction);
        this.add(button);
        this.addSeparator();
        this.cutAction = new CutAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_CUT_ICON_NAME)), "Datei oder Ordner ausschneiden", new Integer(KeyEvent.VK_A), presentationFacade);
        cutAction.setEnabled(false);
        button = new JButton(cutAction);
        this.add(button);
        this.copyAction = new CopyAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_COPY_ICON_NAME)), "Datei oder Ordner kopieren", new Integer(KeyEvent.VK_K), presentationFacade);
        copyAction.setEnabled(false);
        button = new JButton(copyAction);
        this.add(button);
        this.pasteAction = new PasteAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_PASTE_ICON_NAME)), "Dateieigenschaften anzeigen", new Integer(KeyEvent.VK_E), presentationFacade);
        pasteAction.setEnabled(false);
        button = new JButton(pasteAction);
        this.add(button);
        this.addSeparator();
        this.cutToAction = new CutToAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_CUTTO_ICON_NAME)), "Im linken Baum selektierte Datei oder Ordner zur im rechten Baum selektierte ausschneiden", new Integer(KeyEvent.VK_S), presentationFacade);
        cutToAction.setEnabled(false);
        button = new JButton(cutToAction);
        this.add(button);
        this.copyToAction = new CopyToAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_COPYTO_ICON_NAME)), "Im linken Baum selektierte Datei oder Ordner zur im rechten Baum selektierte kopieren", new Integer(KeyEvent.VK_P), presentationFacade);
        copyToAction.setEnabled(false);
        button = new JButton(copyToAction);
        this.add(button);
        this.addSeparator();
        this.renameAction = new RenameAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_RENAME_ICON_NAME)), "Datei oder Ordner umbenennen", new Integer(KeyEvent.VK_U), presentationFacade);
        renameAction.setEnabled(false);
        button = new JButton(renameAction);
        this.add(button);
        this.deleteAction = new DeleteAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_DELETE_ICON_NAME)), "Datei oder Ordner l�schen", new Integer(KeyEvent.VK_L), presentationFacade);
        deleteAction.setEnabled(false);
        button = new JButton(deleteAction);
        this.add(button);
        this.addSeparator();
        HelpAction helpAction = new HelpAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_HELP_ICON_NAME)), "Hilfeindex anzeigen", new Integer(KeyEvent.VK_H));
        button = new JButton(helpAction);
        this.add(button);
        QuitAction quitAction = new QuitAction(null, new ImageIcon(this.getClass().getResource(TOOLBAR_CLOSE_ICON_NAME)), "File Manager schlie�en", new Integer(KeyEvent.VK_Q));
        button = new JButton(quitAction);
        this.add(button);
    }

    /**
	 * Enables file specific tool bar items if an entity in one of the trees
     * of <code>SwingDualTreePanel</code> is selected 
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        this.openAction.setEnabled(true);
        this.showPropertiesAction.setEnabled(true);
        this.cutAction.setEnabled(true);
        this.copyAction.setEnabled(true);
        this.pasteAction.setEnabled(true);
        this.renameAction.setEnabled(true);
        this.deleteAction.setEnabled(true);
        if (((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getLeftTree().isSelected() && ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getRightTree().isSelected()) {
            this.cutToAction.setEnabled(true);
            this.copyToAction.setEnabled(true);
        }
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {
    }

    /**
	 * Implementation of <code>TreeSelectionListener.valueChanged(TreeSelectionEvent)</code>.<p>
	 * This method is called, when selection of a tree changes.
	 *  It makes <code>CutToAction</code> and <code>CopyToAction</code> available when nodes in both trees are selected.
	 * @param tse the selection event
	 */
    public void valueChanged(TreeSelectionEvent e) {
        if ((((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getLeftTree().getSelectionCount() != 0) && (((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getRightTree().getSelectionCount() != 0)) {
            this.cutToAction.setEnabled(true);
            this.copyToAction.setEnabled(true);
        } else {
            this.cutToAction.setEnabled(false);
            this.copyToAction.setEnabled(false);
        }
    }
}
