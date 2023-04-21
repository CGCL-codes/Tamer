package org.makagiga.editors.image.tools;

import static java.awt.event.KeyEvent.*;
import static org.makagiga.commons.UI._;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import org.makagiga.commons.swing.MCheckBox;

/**
 * @since 2.0
 */
public class RectangleTool extends AbstractRectangleTool {

    private boolean drawing;

    /**
	 * @since 3.8
	 */
    public RectangleTool() {
        super(_("Rectangle"), "rectangle");
        setKeyStroke(VK_R, SHIFT_DOWN_MASK);
        setUpdateOnMouseRelease(true);
        setVisiblePanel("brush-color", true);
        setVisiblePanel("brush-properties", true);
        setVisiblePanel("rectangle", true);
    }

    @Override
    public void draw(final Graphics2D g, final int w, final int h) {
        if (!drawing) return;
        BrushTool brush = BrushTool.getInstance();
        Rectangle bounds = getBounds();
        ToolPanel tp = getToolPanel();
        if ((tp != null) && Panel.class.cast(tp).fill.isSelected()) {
            if (brush.getType() == BrushTool.Type.CIRCLE) {
                int arc = brush.getSize();
                g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
            } else {
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        } else {
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);
        drawing = true;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (drawing) {
            super.mouseReleased(e);
            drawing = false;
        }
    }

    /**
	 * @since 4.0
	 */
    protected RectangleTool(final String name, final Icon icon, final String id) {
        super(name, icon, id);
        setUpdateOnMouseRelease(true);
    }

    @Override
    protected ToolPanel createToolPanel() {
        return new Panel(this);
    }

    private static final class Panel extends ToolPanel {

        private final MCheckBox fill;

        private Panel(final RectangleTool rt) {
            super(rt);
            fill = new MCheckBox(_("Filled"));
            setComponent(fill);
        }
    }
}
