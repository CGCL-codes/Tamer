package net.sourceforge.geeboss.view.menu;

import net.sourceforge.geeboss.controller.EventManager;
import net.sourceforge.geeboss.view.MainView;
import net.sourceforge.geeboss.view.tree.PatchNode;
import net.sourceforge.geeboss.view.tree.PatchTree;
import net.sourceforge.geeboss.view.tree.PatchTreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

/**
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class PatchTreePopup {

    /** The application display */
    private Display mDisplay;

    /** The application event manager */
    private EventManager mEventManager;

    /** The patch tree associated to this menu */
    private PatchTree mPatchTree;

    /** The geeboss menu */
    private GeebossMenu mGeebossMenu;

    /** The menu widget associated to this popup */
    private Menu mTreePopUpMenu;

    /** The main view */
    private MainView mMainView;

    /** The application's shell */
    private Shell mShell;

    /**
     * Instantiate a new PatchTreePopup
     * @param mainView the main view
     * @param patchTree the patch tree
     * @param eventManager the event manager
     */
    public PatchTreePopup(MainView mainView, PatchTree patchTree, EventManager eventManager) {
        mMainView = mainView;
        mDisplay = mainView.getDisplay();
        mShell = mainView.getShell();
        mPatchTree = patchTree;
        mGeebossMenu = mainView.getGeebossMenu();
        mEventManager = eventManager;
        initComponents();
    }

    /**
     * Returns the Popup menu that is associated to the patch tree
     * @return the Popup menu that is associated to the patch tree
     */
    public Menu getTreePopUpMenu() {
        return mTreePopUpMenu;
    }

    /**
     * Set the menuStructure for a given node
     * @param node the currently selected node
     */
    public void setMenu(PatchTreeNode node) {
        resetPopup();
        switch(node.getType()) {
            case PATCH:
                new PatchPopup(this, mMainView, (PatchNode) node);
                break;
        }
        mPatchTree.getTree().setMenu(mTreePopUpMenu);
    }

    /**
     * Set the menuStructure for an empty tree
     */
    public void setEmptyMenu() {
        resetPopup();
        new EmptyTreePopup(this, mMainView);
        mPatchTree.getTree().setMenu(mTreePopUpMenu);
    }

    /** Init the components */
    private void initComponents() {
        mTreePopUpMenu = new Menu(mShell, SWT.POP_UP);
    }

    /** Reset the menu */
    private void resetPopup() {
        mTreePopUpMenu.dispose();
        initComponents();
        mGeebossMenu.disposeNewMenu();
    }
}
