package org.xith3d.utility.hud.editor;

import org.openmali.types.twodee.Dim2f;
import org.openmali.vecmath2.Point2f;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.base.AbstractButton;
import org.xith3d.ui.hud.listeners.ButtonListener;
import org.xith3d.ui.hud.widgets.Button;
import org.xith3d.ui.hud.widgets.Frame;
import org.xith3d.ui.hud.widgets.Panel;
import org.xith3d.utility.hud.editor.info.HEInfo_Widget;

/**
 * A HUD editor
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public class HUDEditor implements ButtonListener {

    private Panel panel;

    /**
     * Creates a new HUD editor on this panel
     * 
     * @param panel 
     */
    public HUDEditor(Panel panel) {
        this.panel = panel;
        if (panel.getHUD() == null) {
            throw new Error("You should add your panel to a HUD before" + " initiating a HUDEditor on it !");
        }
        HUD hud = panel.getHUD();
        Dim2f frameSize = new Dim2f(hud.getResX() / 5f, hud.getResY() / 1.5f);
        Frame frame = new Frame(frameSize.getWidth(), frameSize.getHeight(), "Widgets");
        Dim2f size = new Dim2f(frameSize.getWidth() - 4f, frameSize.getHeight() / 7f);
        final float yStep = size.getHeight();
        float yPos = 0;
        ((Button) frame.getContentPane().addWidget(new Button(size.getWidth(), size.getHeight(), "Label"), 0f, yPos)).addButtonListener(this);
        ((Button) frame.getContentPane().addWidget(new Button(size.getWidth(), size.getHeight(), "DynamicLabel"), 0f, yPos += yStep)).addButtonListener(this);
        ((Button) frame.getContentPane().addWidget(new Button(size.getWidth(), size.getHeight(), "Image"), 0f, yPos += yStep)).addButtonListener(this);
        ((Button) frame.getContentPane().addWidget(new Button(size.getWidth(), size.getHeight(), "Button"), 0f, yPos += yStep)).addButtonListener(this);
        ((Button) frame.getContentPane().addWidget(new Button(size.getWidth(), size.getHeight(), "TextField"), 0f, yPos += yStep)).addButtonListener(this);
        hud.addWindow(frame, hud.getWidth() - frame.getWidth() - 10f, 10f);
        frame.setCloseButtonVisible(false);
        frame.setVisible(true);
    }

    public void onButtonClicked(AbstractButton button, Object userObject) {
        try {
            HEInfo_Widget widget = HEInfo_Widget.newWidget(((Button) button).getText(), new Point2f(100f, 100f), new Dim2f(100f, 50f));
            panel.addWidget(widget.getWidget());
            new WidgetManipulator(widget.getWidget());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
