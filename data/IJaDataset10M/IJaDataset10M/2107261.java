package org.xith3d.utility.hud.editor;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.jagatoo.input.devices.components.MouseButton;
import org.jagatoo.input.devices.components.MouseButtons;
import org.xith3d.ui.hud.base.Widget;
import org.xith3d.ui.hud.listeners.WidgetMouseAdapter;

/**
 * Same as the WidgetManipulator but adds a Swing dialog
 * when you do a right click on the widget
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public class WidgetManipulatorDialog extends WidgetManipulator {

    private static JFrame frame;

    private static JLabel label;

    private static JTextArea area;

    public static final JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("Widget located !");
            frame.setSize(500, 80);
            frame.setLayout(new FlowLayout());
            label = new JLabel("The widget you asked has been" + " located and sized, here are the infos !!");
            frame.add(label);
            area = new JTextArea();
            area.setEditable(false);
            frame.add(area);
        }
        return frame;
    }

    public WidgetManipulatorDialog(Widget widget) {
        super(widget);
        widget.addMouseListener(new WidgetMouseAdapter() {

            @Override
            public void onMouseButtonReleased(Widget widget, MouseButton button, float x, float y, long when, long lastWhen, boolean isTopMost, boolean hasFocus) {
                if ((widget == WidgetManipulatorDialog.this.widget) && (button == MouseButtons.RIGHT_BUTTON)) {
                    String name = widget.getClass().getSimpleName().toLowerCase();
                    getFrame();
                    area.setText(name + ".setLocation(" + widget.getLeft() + "f, " + widget.getTop() + "f);" + "\n" + name + ".resize(" + widget.getWidth() + "f, " + widget.getHeight() + "f);");
                    getFrame().setVisible(true);
                    getFrame().repaint();
                }
            }
        });
    }
}
