package com.genia.toolbox.form.editor.gui.view.internal.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.server.UID;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import com.genia.toolbox.basics.editor.gui.view.internal.panel.ElementViewPanel;
import com.genia.toolbox.form.editor.gui.FormEditorGUI;
import com.genia.toolbox.form.editor.gui.view.internal.FormDocumentInternalFrame;
import com.genia.toolbox.form.editor.gui.view.internal.factory.FormViewFactory;
import com.genia.toolbox.form.editor.message.Messages;
import com.genia.toolbox.form.editor.model.form.impl.ContainerFormModel;
import com.genia.toolbox.form.editor.model.form.impl.FormModel;
import com.genia.toolbox.form.editor.model.form.impl.FormType;
import com.genia.toolbox.web.portlet.description.ContainerPortletDescription.LayoutDirection;

/**
 * The basic container view panel.
 */
@SuppressWarnings("serial")
public abstract class AbstractContainerFormViewPanel extends AbstractFormViewPanel {

    /**
   * The add form menu.
   */
    private JMenuItem menuAddForm = null;

    /**
   * The view factory. Used to create the sub panels.
   */
    private FormViewFactory formViewFactory = null;

    /**
   * The sub panels list.
   */
    private List<ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType>> subViewPanels = null;

    /**
   * Constructor.
   * 
   * @param gui
   *          The gui.
   * @param internalFrame
   *          The internal frame.
   */
    public AbstractContainerFormViewPanel(FormEditorGUI gui, FormDocumentInternalFrame internalFrame) {
        super(gui, internalFrame);
        this.formViewFactory = internalFrame.getViewFactory();
        this.subViewPanels = new LinkedList<ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType>>();
    }

    /**
   * Create the specific panel.
   * 
   * @return the specific panel.
   */
    @Override
    public JPanel createSpecificPanel() {
        JPanel panel = new JPanel();
        panel.addMouseListener(new MouseAdapter() {

            /**
       * The mouse clicked event.
       * 
       * @param e
       *          the mouse event.
       */
            @Override
            public void mouseClicked(MouseEvent e) {
                deactivateParent();
                if (isCommonSelected()) {
                    deactivateAll();
                    getGui().displayCurrentDocumentSettings();
                } else {
                    activateCommon(true);
                    deactivateSpecific();
                    getGui().displayElementSettings(getElement());
                }
            }

            /**
       * The mouse pressed event.
       * 
       * @param e
       *          the mouse event.
       */
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            /**
       * The mouse released event.
       * 
       * @param e
       *          the mouse event.
       */
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
        });
        return panel;
    }

    /**
   * Select the panel of a sub-element.
   * 
   * @param index
   *          the index of the sub-element.
   */
    public void selectSubElement(Integer index) {
        this.deactivateAll();
        if (index != -1) {
            ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel = this.subViewPanels.get(index);
            panel.activateCommon(true);
            ContainerFormModel containerFormModel = (ContainerFormModel) this.getElement();
            this.getGui().displayElementSettings(containerFormModel.getSubElements().get(index));
        }
    }

    /**
   * Set the displayed element.
   * 
   * @param model
   *          the displayed element model.
   */
    @Override
    public void setElement(FormModel model) {
        super.setElement(model);
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        this.getLabelType().setText(this.getLabelType().getText() + " : " + containerElementModel.getName());
        this.getLabelId().setVisible(false);
        ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel = null;
        for (FormModel subElement : containerElementModel.getSubElements()) {
            panel = this.getViewPanel(subElement.getUniqueID());
            if (panel == null) {
                panel = this.formViewFactory.createView(subElement);
                panel.setParentPanel(this);
                this.subViewPanels.add(panel);
            } else {
                panel.setElement(subElement);
            }
        }
        this.displaySubElements();
    }

    /**
   * Update the displayed element.
   * 
   * @param updatedModel
   *          the displayed element model.
   */
    @Override
    public void updateElement(FormModel updatedModel) {
        super.updateElement(updatedModel);
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> newPanel = null;
        if (!this.getElement().getUniqueID().equals(updatedModel.getUniqueID())) {
            if (this.getElement().getUniqueID().equals(updatedModel.getParentID())) {
                int index = this.indexOf(updatedModel.getUniqueID());
                if (index == -1) {
                    containerElementModel.getSubElements().add(updatedModel);
                    newPanel = this.formViewFactory.createView(updatedModel);
                    newPanel.setParentPanel(this);
                    this.subViewPanels.add(newPanel);
                } else {
                    containerElementModel.getSubElements().remove(index);
                    containerElementModel.getSubElements().add(index, updatedModel);
                    ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel = this.subViewPanels.get(index);
                    panel.setElement(updatedModel);
                }
            } else {
                for (ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> submodel : this.subViewPanels) {
                    submodel.updateElement(updatedModel);
                }
            }
        }
        if (newPanel != null) {
            this.deactivateAll();
            newPanel.activateCommon(true);
            this.getGui().displayElementSettings(updatedModel);
        }
        this.displaySubElements();
    }

    /**
   * Remove the sub-element.
   * 
   * @param model
   *          the sub-element model.
   */
    public void removeSubElement(FormModel model) {
        ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> viewPanel = this.getViewPanel(model.getUniqueID());
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        containerElementModel.getSubElements().remove(model);
        this.subViewPanels.remove(viewPanel);
        this.displaySubElements();
    }

    /**
   * Move the sub element up.
   * 
   * @param model
   *          the sub-element model.
   */
    public void moveUpSubElement(FormModel model) {
        int index = this.indexOf(model.getUniqueID());
        if (index != 0) {
            ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
            containerElementModel.getSubElements().remove(index);
            containerElementModel.getSubElements().add(index - 1, model);
            ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> viewPanel = this.getViewPanel(model.getUniqueID());
            this.subViewPanels.remove(index);
            this.subViewPanels.add(index - 1, viewPanel);
        }
        this.displaySubElements();
    }

    /**
   * Move the sub element down.
   * 
   * @param model
   *          the sub-element model.
   */
    public void moveDownSubElement(FormModel model) {
        int index = this.indexOf(model.getUniqueID());
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        int nbSubElements = containerElementModel.getNbSubForms();
        if (index != nbSubElements - 1) {
            containerElementModel.getSubElements().remove(index);
            containerElementModel.getSubElements().add(index + 1, model);
            ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> viewPanel = this.getViewPanel(model.getUniqueID());
            this.subViewPanels.remove(index);
            this.subViewPanels.add(index + 1, viewPanel);
        }
        this.displaySubElements();
    }

    /**
   * Get the index of the sub-element.
   * 
   * @param updatedID
   *          the unique sub-element ID.
   * @return the index of the sub-element.
   */
    private int indexOf(UID updatedID) {
        int index = -1;
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        for (FormModel submodel : containerElementModel.getSubElements()) {
            if (submodel.getUniqueID().equals(updatedID)) {
                index = containerElementModel.getSubElements().indexOf(submodel);
                break;
            }
        }
        return index;
    }

    /**
   * Display the sub-elements.
   */
    protected void displaySubElements() {
        this.getSpecificPanel().removeAll();
        ContainerFormModel containerFormModel = (ContainerFormModel) this.getElement();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        this.getSpecificPanel().setLayout(layout);
        int index = 1;
        for (ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel : this.subViewPanels) {
            if (LayoutDirection.HORIZONTAL.equals(containerFormModel.getDirection())) {
                constraints.gridx = index;
                constraints.gridy = 1;
                if (index == 1) {
                    constraints.insets = new Insets(10, 10, 10, 10);
                } else {
                    constraints.insets = new Insets(10, 0, 10, 10);
                }
            } else {
                constraints.gridx = 1;
                constraints.gridy = index;
                if (index == 1) {
                    constraints.insets = new Insets(10, 10, 10, 10);
                } else {
                    constraints.insets = new Insets(0, 10, 10, 10);
                }
            }
            this.getSpecificPanel().add((JPanel) panel, constraints);
            index++;
        }
        this.getSpecificPanel().revalidate();
        this.revalidate();
        this.repaint();
    }

    /**
   * Deactivate the whole panel.
   */
    @Override
    public void deactivateAll() {
        this.deactivateSpecific();
        super.deactivateAll();
    }

    /**
   * Handle the common panel activation.
   * 
   * @param isActivated
   *          Whether the panel is activated.
   */
    @Override
    public void activateCommon(boolean isActivated) {
        super.activateCommon(isActivated);
        this.activate(isActivated, this.getSpecificPanel());
    }

    /**
   * Deactivate the specific panel.
   */
    @Override
    public void deactivateSpecific() {
        super.deactivateSpecific();
        for (ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel : this.subViewPanels) {
            panel.deactivateAll();
        }
    }

    /**
   * Get the view factory.
   * 
   * @return the view factory.
   */
    public FormViewFactory getViewFactory() {
        return this.formViewFactory;
    }

    /**
   * Display the popup.
   * 
   * @param component
   *          The component.
   * @param x
   *          The x position.
   * @param y
   *          The y position.
   */
    @Override
    public void displayPopup(Component component, int x, int y) {
        ContainerFormModel containerElementModel = (ContainerFormModel) this.getElement();
        int nbDisplayedSubElements = containerElementModel.getNbSubForms();
        if (nbDisplayedSubElements >= containerElementModel.getMaxNbSubForms()) {
            this.getMenuAdd().setEnabled(false);
        } else {
            this.getMenuAdd().setEnabled(true);
        }
        if (containerElementModel.isSubElement()) {
            this.getMenuDelete().setEnabled(true);
            this.getMenuDown().setEnabled(true);
            this.getMenuUp().setEnabled(true);
            if (LayoutDirection.HORIZONTAL.equals(containerElementModel.getParentDirection())) {
                this.getMenuDown().setEnabled(false);
                this.getMenuUp().setEnabled(false);
                this.getMenuRight().setEnabled(true);
                this.getMenuLeft().setEnabled(true);
            } else {
                this.getMenuDown().setEnabled(true);
                this.getMenuUp().setEnabled(true);
                this.getMenuRight().setEnabled(false);
                this.getMenuLeft().setEnabled(false);
            }
        } else {
            this.getMenuDelete().setEnabled(false);
            this.getMenuDown().setEnabled(false);
            this.getMenuUp().setEnabled(false);
            this.getMenuRight().setEnabled(false);
            this.getMenuLeft().setEnabled(false);
        }
        this.getPopup().show(component, x, y);
    }

    /**
   * Handle action events.
   * 
   * @param e
   *          the action event.
   */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        if (source.equals(this.getMenuAdd())) {
            this.addSubElement();
        } else if (source.equals(this.menuAddForm)) {
            this.addSubForm();
        }
    }

    /**
   * Get the view panel from the element or one of its sub-element.
   * 
   * @param elementUniqueID
   *          The element unique ID.
   * @return the view panel.
   */
    @Override
    public ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> getViewPanel(UID elementUniqueID) {
        ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel = super.getViewPanel(elementUniqueID);
        if (panel == null) {
            for (ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> subpanel : this.subViewPanels) {
                panel = subpanel.getViewPanel(elementUniqueID);
                if (panel != null) {
                    break;
                }
            }
        }
        return panel;
    }

    /**
   * Create the popup menu.
   * 
   * @return the popup menu.
   */
    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu popup = super.createPopupMenu();
        this.menuAddForm = new JMenuItem(this.getGui().getController().notifyTranslation(Messages.VIEW_PANEL_POPUP_MENU_ADD_FORM));
        this.menuAddForm.addActionListener(this);
        popup.add(this.menuAddForm, 1);
        return popup;
    }

    /**
   * Get the sub view panels.
   * 
   * @return the sub view panels.
   */
    public List<ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType>> getSubViewPanels() {
        return this.subViewPanels;
    }

    /**
   * Get the add form menu.
   * 
   * @return the add form menu.
   */
    public JMenuItem getMenuAddForm() {
        return menuAddForm;
    }

    /**
   * Whether the common panel, or one of its sub panel is selected.
   * 
   * @return whether the common label or one of its sub panel is selected.
   */
    @Override
    public boolean hasAtLeastOneCommonSelected() {
        boolean isSelected = this.isCommonSelected();
        if (!isSelected) {
            for (ElementViewPanel<FormEditorGUI, FormDocumentInternalFrame, FormModel, FormType> panel : this.getSubViewPanels()) {
                isSelected = panel.hasAtLeastOneCommonSelected();
                if (isSelected) {
                    break;
                }
            }
        }
        return isSelected;
    }
}
