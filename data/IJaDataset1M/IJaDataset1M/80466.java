package se.entitymanager.presentation;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import se.entitymanager.logic.EntityCopyCutFailedException;
import se.entitymanager.logic.EntityInterface;
import se.entitymanager.logic.EntityPasteFailedException;
import se.entitymanager.presentation.swing.EntityTree;
import se.entitymanager.presentation.swing.SwingDualTreePanel;
import se.entitymanager.presentation.swing.SwingPresentationFacade;

/**
 * Action handling for quiting the application
 */
public class CopyToAction extends AbstractAction {

    /**
     * The presentation facade to use.<p>
     * 
     * @uml.property name="presentationFacade"
     * @uml.associationEnd 
     * @uml.property name="presentationFacade" multiplicity="(1 1)"
     */
    private SwingPresentationFacade presentationFacade;

    public CopyToAction(String text, ImageIcon icon, String desc, Integer mnemonic, SwingPresentationFacade presentationFacade) {
        super(text, icon);
        this.presentationFacade = presentationFacade;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
    }

    public void actionPerformed(ActionEvent e) {
        EntityTree leftTree = ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getLeftTree();
        EntityInterface leftEntity = leftTree.getSelectedEntity().getEntity();
        EntityTree rightTree = ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getRightTree();
        EntityInterface rightEntity = rightTree.getSelectedEntity().getEntity();
        boolean copySuccess = false;
        if (leftEntity != null) {
            try {
                this.presentationFacade.getLogicFacade().copy(leftEntity);
                copySuccess = true;
            } catch (EntityCopyCutFailedException ex) {
                this.presentationFacade.showError(ex.getReason());
            }
            if (copySuccess = true) {
                try {
                    this.presentationFacade.getLogicFacade().paste(rightEntity);
                    ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).getSelectedTree().addNode(leftEntity);
                    ((SwingDualTreePanel) this.presentationFacade.getDualTreeComponent()).reloadTrees();
                } catch (EntityPasteFailedException ex) {
                    this.presentationFacade.showError(ex.getReason());
                }
            }
        }
    }
}
