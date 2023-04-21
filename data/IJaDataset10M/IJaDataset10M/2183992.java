package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import ru.novosoft.uml.MElementEvent;

/**
 * ComboBox for UML modelelements. This implementation does not use 
 * reflection and seperates Model, View and Controller better then does
 * UMLComboBox. In the future UMLComboBoxModel and UMLComboBox will be
 * replaced with this implementation to improve performance.
 */
public abstract class UMLComboBox2 extends JComboBox implements UMLUserInterfaceComponent {

    protected UMLUserInterfaceContainer container = null;

    /**
     * Constructor for UMLActivatorComboBox.
     * @param arg0
     */
    public UMLComboBox2(UMLUserInterfaceContainer container, UMLComboBoxModel2 arg0) {
        super(arg0);
        setContainer(container);
        addActionListener(this);
    }

    /**
     * Returns the container.
     * @return UMLUserInterfaceContainer
     */
    public UMLUserInterfaceContainer getContainer() {
        return container;
    }

    /**
     * Sets the container.
     * @param container The container to set
     */
    public void setContainer(UMLUserInterfaceContainer container) {
        this.container = container;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        ((UMLComboBoxModel2) getModel()).targetChanged();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        ((UMLComboBoxModel2) getModel()).targetReasserted();
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).listRoleItemSet(e);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).propertySet(e);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(MElementEvent)
     */
    public void recovered(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).recovered(e);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(MElementEvent)
     */
    public void removed(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).removed(e);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).roleAdded(e);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        ((UMLComboBoxModel2) getModel()).roleRemoved(e);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        int i = getSelectedIndex();
        if (i >= 0) {
            doIt(arg0);
        }
    }

    protected abstract void doIt(ActionEvent event);
}
