package org.aiotrade.platform.core.ui.netbeans.actions;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import org.aiotrade.charting.view.ChartingControllerFactory;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author Caoyuan Deng
 */
public class SwitchCursorAcceleratedAction extends CallableSystemAction {

    private static JToggleButton toggleButton;

    /** Creates a new instance of ZoomInAction
     */
    public SwitchCursorAcceleratedAction() {
    }

    /**
     * @NOTICE
     * If override getToolbarPresenter(), you should process action in that
     * component instead of here.
     */
    public void performAction() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (toggleButton.isSelected()) {
                    toggleButton.setSelected(false);
                } else {
                    toggleButton.setSelected(true);
                }
            }
        });
    }

    public static boolean isCursorAccelerated() {
        return toggleButton.isSelected();
    }

    public String getName() {
        return "Accelerate Cursor Moving";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected String iconResource() {
        return "org/aiotrade/platform/core/ui/netbeans/resources/switchCursorAcceleratedAction.gif";
    }

    protected boolean asynchronous() {
        return false;
    }

    public static void setSelected(boolean b) {
        toggleButton.setSelected(b);
    }

    public Component getToolbarPresenter() {
        Image iconImage = Utilities.loadImage("org/aiotrade/platform/core/ui/netbeans/resources/switchCursorAcceleratedAction.gif");
        ImageIcon icon = new ImageIcon(iconImage);
        toggleButton = new JToggleButton();
        toggleButton.setIcon(icon);
        toggleButton.setToolTipText("Fast Moving");
        toggleButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                final int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    ChartingControllerFactory.setCursorAccelerated(true);
                } else {
                    ChartingControllerFactory.setCursorAccelerated(false);
                }
            }
        });
        return toggleButton;
    }
}
