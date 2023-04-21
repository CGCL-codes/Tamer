package se.entitymanager.presentation.swing;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import se.entitymanager.logic.EntityChangedEvent;
import se.entitymanager.logic.EntityChangedListener;
import se.entitymanager.logic.EntityInterface;

/**
 * A panel that shows details of an entity.<p>
 * At the moment this detailed information consists only of the name of the entity.
 *  This class implements the interface <code>TreeSelectionListener</code>, so
 * its information will be updated when selection within a tree changes.
 * @see se.entitymanager.presentation.swing.EntityTree
 * 
 * @uml.stereotype name="tagged" isDefined="true" 
 */
public class EntityDetailPanel extends JPanel implements EntityChangedListener, TreeSelectionListener {

    /**
     * The label for textfield for name of entity.
     * @see #nameField
     * 
     * @uml.property name="nameLabel"
     * @uml.associationEnd 
     * @uml.property name="nameLabel" multiplicity="(1 1)"
     */
    private JLabel nameLabel;

    /**
     * The textfield to display the name of an entity.
     * 
     * @uml.property name="nameField"
     * @uml.associationEnd 
     * @uml.property name="nameField" multiplicity="(1 1)"
     */
    private JTextField nameField;

    /**
     * The entity to display the name of.
     * 
     * @uml.property name="entity"
     * @uml.associationEnd 
     * @uml.property name="entity" multiplicity="(0 1)"
     */
    private EntityInterface entity;

    /**
     * The presentation facade object, this object belongs to.
     * 
     * @uml.property name="presentationFacade"
     * @uml.associationEnd 
     * @uml.property name="presentationFacade" multiplicity="(1 1)"
     */
    private SwingPresentationFacade presentationFacade;

    /**
	 * Constructs an panel that shows detailed information about an entity.<p>
	 * The information is gathered with the logic facade accessed through <code>presentationFacade</code>.
	 * @param presentationFacade the presentation facade to use
	 */
    protected EntityDetailPanel(SwingPresentationFacade presentationFacade) {
        this.presentationFacade = presentationFacade;
        this.nameLabel = new JLabel("Name:");
        this.nameField = new JTextField(20);
        this.nameField.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.WEST, nameLabel);
        this.add(BorderLayout.CENTER, nameField);
    }

    /**
	 * Implementation of <code>TreeSelectionListener.valueChanged(TreeSelectionEvent)</code>.<p>
	 * This method is called, when selection of a tree changes. It adjusts <code>nameField</code> to new selection.
	 * @see #nameField
	 * @param tse the selection event
	 */
    public void valueChanged(TreeSelectionEvent tse) {
        this.entity = (EntityInterface) ((EntityTreeNode) tse.getPath().getLastPathComponent()).getUserObject();
        this.nameField.setText(this.presentationFacade.getLogicFacade().getEntityName(entity));
    }

    /**
	 * @see se.entitymanager.logic.EntityChangedListener#entityChanged(se.entitymanager.logic.EntityChangedEvent)
	 */
    public void entityChanged(EntityChangedEvent event) {
        if ((EntityInterface) event.getSource() == this.entity) {
            this.nameField.setText(this.presentationFacade.getLogicFacade().getEntityName(entity));
        }
    }
}
