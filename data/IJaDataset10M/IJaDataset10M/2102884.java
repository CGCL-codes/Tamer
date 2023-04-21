package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;

/**
 * Stylepanel which adds a checkbox to show or hide the stereotype.<p>
 * TODO: i18n.
 *
 * @author mvw@tigris.org
 */
public class StylePanelFigPackage extends StylePanelFigNodeModelElement {

    private JCheckBox stereoCheckBox = new JCheckBox("Stereotype");

    private JCheckBox visibilityCheckBox = new JCheckBox("Visibility");

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction;

    /**
     * The constructor.
     */
    public StylePanelFigPackage() {
        super();
        addToDisplayPane(stereoCheckBox);
        stereoCheckBox.setSelected(false);
        stereoCheckBox.addItemListener(this);
        addToDisplayPane(visibilityCheckBox);
        visibilityCheckBox.addItemListener(this);
    }

    public void refresh() {
        refreshTransaction = true;
        super.refresh();
        StereotypeContainer stc = (StereotypeContainer) getPanelTarget();
        stereoCheckBox.setSelected(stc.isStereotypeVisible());
        VisibilityContainer vc = (VisibilityContainer) getPanelTarget();
        visibilityCheckBox.setSelected(vc.isVisibilityVisible());
        refreshTransaction = false;
    }

    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();
            if (src == stereoCheckBox) {
                ((StereotypeContainer) getPanelTarget()).setStereotypeVisible(stereoCheckBox.isSelected());
            } else if (src == visibilityCheckBox) {
                ((VisibilityContainer) getPanelTarget()).setVisibilityVisible(visibilityCheckBox.isSelected());
            } else {
                super.itemStateChanged(e);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -41790550511653720L;
}
