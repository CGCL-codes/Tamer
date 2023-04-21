package org.gocha.gef.gui.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.gocha.gef.Dot;
import org.gocha.gef.GefUtil;
import org.gocha.gef.Rectangle;
import org.gocha.gef.glyph.RectangleGlyph;
import org.gocha.gef.gui.DefaultMessages;
import org.gocha.gef.gui.ToolImgAnnotaion;

/**
 * Инструмент создания прямоугольников
 * @author gocha
 */
@ToolImgAnnotaion(img = "/org/gocha/gef/gui/tool/rectangle.gif")
public class CreateRectangleTool extends BasicTool implements MouseListener, MouseMotionListener {

    @Override
    public String getToolName() {
        return DefaultMessages.get().createRectangleTool();
    }

    private RectangleGlyph rectGlyph = null;

    public CreateRectangleTool() {
        rectGlyph = new RectangleGlyph();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!this.isEnable()) return;
        if (!this.isReady()) return;
        if (e.getButton() != MouseEvent.BUTTON1) return;
        Dot dStart = new Dot(e.getPoint());
        dStart = dStart.toLocal(getWorkLevel());
        Rectangle rect = new Rectangle(dStart, dStart);
        rectGlyph.setRectangle(rect);
        rectGlyph.setSelected(true);
        getWorkLevel().getChildren().add(rectGlyph);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!this.isEnable()) return;
        if (!this.isReady()) return;
        Dot dEnd = new Dot(e.getPoint());
        dEnd = dEnd.toLocal(getWorkLevel());
        Rectangle rect = new Rectangle(rectGlyph.getRectangle().getLeftTopDot(), dEnd);
        rectGlyph.setRectangle(rect);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!this.isEnable()) return;
        if (!this.isReady()) return;
        if (rectGlyph != null) {
            GefUtil.normalizeCenter(rectGlyph, rectGlyph, rectGlyph);
        }
        rectGlyph = new RectangleGlyph();
    }

    @Override
    protected void start() {
        if (!isReady()) return;
    }

    @Override
    protected void stop() {
        if (!isReady()) return;
        getWorkLevel().getChildren().remove(rectGlyph);
    }

    private boolean isReady() {
        return getWorkLevel() != null;
    }
}
