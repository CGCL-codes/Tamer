package de.miethxml.hawron.gui.context.ui;

import javax.swing.JToggleButton;
import de.miethxml.hawron.gui.context.ContextView;
import de.miethxml.hawron.gui.context.ContextViewComponent;

/**
 * @author simon
 *
 *
 *
 */
public class ButtonPanelSelectionModel extends JToggleButton.ToggleButtonModel {

    private ContextViewComponent view;

    public ButtonPanelSelectionModel(ContextViewComponent view) {
        super();
        this.view = view;
    }

    public boolean isSelected() {
        return view.isShowButtonPanels();
    }

    public void setSelected(boolean b) {
        view.showButtonPanels(b);
        super.setSelected(b);
    }
}
