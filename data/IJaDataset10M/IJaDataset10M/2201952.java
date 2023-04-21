package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.SpacerPanel;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.ui.foundation.core.UMLModelElementStereotypeListModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;

/**
 * This the tab in the details pane for displaying the stereotypes applied to a
 * model element and allowing adding and removal of stereotypes to that list.<p>
 *
 * The code for the 2 lists and the buttons to move items
 * from one side to the other is based on the PerspectiveConfigurator class.
 */
public class TabStereotype extends PropPanel {

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    private static String orientation = Configuration.getString(Configuration.makeKey("layout", "tabstereotype"));

    private UMLModelElementListModel2 selectedListModel;

    private UMLModelElementListModel2 availableListModel;

    private JScrollPane selectedScroll;

    private JScrollPane availableScroll;

    private JPanel panel;

    private JButton addStButton;

    private JButton removeStButton;

    private JPanel xferButtons;

    private JList selectedList;

    private JList availableList;

    /**
     * Construct new Stereotype tab.
     */
    public TabStereotype() {
        super(Translator.localize("tab.stereotype"), (ImageIcon) null);
        setOrientation((orientation.equals("West") || orientation.equals("East")) ? Vertical.getInstance() : Horizontal.getInstance());
        setIcon(new UpArrowIcon());
        setLayout(new BorderLayout());
        remove(getTitleLabel());
        panel = makePanel();
        add(panel);
    }

    /**
     * Create a JPanel with everything on it.
     *
     * @return a newly created JPanel.
     */
    private JPanel makePanel() {
        selectedListModel = new UMLModelElementStereotypeListModel();
        selectedList = new UMLLinkedList(selectedListModel);
        selectedScroll = new JScrollPane(selectedList);
        selectedScroll.setBorder(BorderFactory.createEmptyBorder(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        selectedScroll.setColumnHeaderView(new JLabel(Translator.localize("label.applied-stereotypes")));
        availableListModel = new UMLModelStereotypeListModel();
        availableList = new UMLLinkedList(availableListModel);
        availableScroll = new JScrollPane(availableList);
        availableScroll.setBorder(BorderFactory.createEmptyBorder(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        availableScroll.setColumnHeaderView(new JLabel(Translator.localize("label.available-stereotypes")));
        addStButton = new JButton(">>");
        addStButton.setToolTipText(Translator.localize("button.add-stereo"));
        removeStButton = new JButton("<<");
        removeStButton.setToolTipText(Translator.localize("button.remove-stereo"));
        addStButton.setEnabled(false);
        removeStButton.setEnabled(false);
        addStButton.setMargin(new Insets(2, 15, 2, 15));
        removeStButton.setMargin(new Insets(2, 15, 2, 15));
        addStButton.setPreferredSize(addStButton.getMinimumSize());
        removeStButton.setPreferredSize(removeStButton.getMinimumSize());
        BoxLayout box;
        xferButtons = new JPanel();
        box = new BoxLayout(xferButtons, BoxLayout.Y_AXIS);
        xferButtons.setLayout(box);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(addStButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeStButton);
        Dimension dmax = box.maximumLayoutSize(xferButtons);
        Dimension dmin = box.minimumLayoutSize(xferButtons);
        xferButtons.setMaximumSize(new Dimension(dmin.width, dmax.height));
        addStButton.addActionListener(new AddRemoveListener());
        removeStButton.addActionListener(new AddRemoveListener());
        availableList.addListSelectionListener(new AvailableListSelectionListener());
        selectedList.addListSelectionListener(new SelectedListSelectionListener());
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.X_AXIS));
        thePanel.setBorder(BorderFactory.createEmptyBorder(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        thePanel.add(availableScroll);
        thePanel.add(xferButtons);
        thePanel.add(Box.createRigidArea(new Dimension(5, 1)));
        thePanel.add(selectedScroll);
        return thePanel;
    }

    /**
     * Checks if the tab should be enabled. Returns true if the target
     * returned by getTarget is a modelelement or if that target shows up as Fig
     * on the active diagram and has a modelelement as owner.
     *
     * @return true if this tab should be enabled, otherwise false.
     */
    public boolean shouldBeEnabled() {
        Object target = getTarget();
        return shouldBeEnabled(target);
    }

    @Override
    public boolean shouldBeEnabled(Object target) {
        if (target instanceof Fig) {
            target = ((Fig) target).getOwner();
        }
        return Model.getFacade().isAModelElement(target);
    }

    @Override
    public void setTarget(Object theTarget) {
        super.setTarget(theTarget);
        if (isVisible()) {
            Object me = getModelElement();
            if (me != null) {
                selectedListModel.setTarget(me);
                validate();
            }
        }
    }

    /**
     * Add the currently selected stereotype from the library
     * to the modelelement.
     */
    private void doAddStereotype() {
        Object stereotype = availableList.getSelectedValue();
        Object modelElement = TargetManager.getInstance().getModelTarget();
        if (modelElement == null) {
            return;
        }
        Model.getCoreHelper().addStereotype(modelElement, stereotype);
    }

    /**
     * Add the currently selected stereotype from the library
     * to the modelelement.
     */
    private void doRemoveStereotype() {
        Object stereotype = selectedList.getSelectedValue();
        Object modelElement = TargetManager.getInstance().getModelTarget();
        if (modelElement == null) {
            return;
        }
        if (Model.getFacade().getStereotypes(modelElement).contains(stereotype)) {
            Model.getCoreHelper().removeStereotype(modelElement, stereotype);
        }
    }

    /**
     * The list model for all stereotypes available in all the models - except
     * the ones already applied.
     */
    private static class UMLModelStereotypeListModel extends UMLModelElementListModel2 {

        /**
         * Constructor for UMLModelElementNamespaceListModel.
         */
        public UMLModelStereotypeListModel() {
            super("stereotype");
        }

        protected void buildModelList() {
            removeAllElements();
            if (Model.getFacade().isAModelElement(getTarget())) {
                Collection s;
                s = StereotypeUtility.getAvailableStereotypes(getTarget());
                s.removeAll(Model.getFacade().getStereotypes(getTarget()));
                addAll(s);
            }
        }

        protected boolean isValidElement(Object element) {
            return Model.getFacade().isAStereotype(element);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 7247425177890724453L;
    }

    /**
     * Handles pressing the ">>" or "<<" buttons.
     */
    private class AddRemoveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == addStButton) {
                doAddStereotype();
            } else if (src == removeStButton) {
                doRemoveStereotype();
            }
        }
    }

    /**
     * Handles selection changes in the available stereotypes list.
     */
    private class AvailableListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            Object selRule = availableList.getSelectedValue();
            addStButton.setEnabled(selRule != null);
        }
    }

    /**
     * Handles selection changes in the stereotypes list.
     */
    private class SelectedListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            Object selRule = selectedList.getSelectedValue();
            removeStButton.setEnabled(selRule != null);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4741653225927138553L;
}
