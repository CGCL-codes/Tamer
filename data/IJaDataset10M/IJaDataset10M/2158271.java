package com.loribel.commons.gui;

import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.swing.tools.GB_ButtonBarTools;
import com.loribel.commons.util.CTools;

/**
 * Decorator with Buttons for ViewManager.
 *
 * @author Gregory Borelli
 */
public class GB_VMDecoratorWithButtons extends GB_VMDecorator {

    private Collection buttons;

    private Collection buttonsLeft;

    private boolean useBorder = true;

    /**
     * Constructor.
     */
    public GB_VMDecoratorWithButtons(GB_ViewManager a_mainViewManager) {
        super(a_mainViewManager);
    }

    /**
     * Constructor.
     */
    public GB_VMDecoratorWithButtons(GB_ViewManager a_mainViewManager, JButton[] a_buttons) {
        this(a_mainViewManager);
        setButtons(a_buttons);
    }

    /**
     * Build the panel with buttons.
     *
     * @return JComponent
     */
    protected JComponent buildButtonsBar() {
        JComponent retour = GB_ButtonBarTools.newButtonsBar(getButtonsLeft(), getButtons(), isUseBorder());
        return retour;
    }

    /**
     * Returns the view managed by this ViewManager.
     * Build the view based on the view of the mainViewManager.
     */
    public JComponent buildView(JComponent a_viewToDecorate) {
        GB_PanelRows retour = new GB_PanelRows();
        retour.addRowFill2(a_viewToDecorate);
        if (!isUseBorder()) {
            retour.addRow(5);
        }
        retour.addRowFill(buildButtonsBar());
        return retour;
    }

    protected Collection getButtons() {
        return buttons;
    }

    protected Collection getButtonsLeft() {
        return buttonsLeft;
    }

    protected boolean isUseBorder() {
        return useBorder;
    }

    public void setButtons(JButton[] a_buttons) {
        buttons = CTools.toList(a_buttons);
    }

    public void setButtonsLeft(JButton[] a_buttons) {
        buttonsLeft = CTools.toList(a_buttons);
    }

    public void setUseBorder(boolean a_useBorder) {
        useBorder = a_useBorder;
    }
}
