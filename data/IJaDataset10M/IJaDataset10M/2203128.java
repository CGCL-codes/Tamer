package org.makagiga.editors.image.tools;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import org.makagiga.editors.image.ImageEditor;
import org.makagiga.editors.image.ImageEditorCore;

public abstract class AbstractTool implements Tool {

    private boolean readOnly;

    private Cursor cursor;

    private String name;

    private ToolPanel toolPanel;

    protected ImageEditor editor;

    protected ImageEditorCore core;

    public AbstractTool(final String name) {
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        this.name = name;
    }

    public Point convertPoint(final int x, final int y) {
        double scale = ImageEditorCore.getScale();
        return new Point((int) (x / scale), (int) (y / scale));
    }

    public Point convertPoint(final MouseEvent e) {
        return convertPoint(e.getX(), e.getY());
    }

    public Point convertPoint(final Point p) {
        return convertPoint(p.x, p.y);
    }

    public void draw(final Graphics2D g, final int w, final int h) {
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(final Cursor value) {
        cursor = value;
    }

    public String getName() {
        return name;
    }

    public ToolPanel getToolPanel() {
        return toolPanel;
    }

    public void setToolPanel(final ToolPanel value) {
        toolPanel = value;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(final boolean value) {
        readOnly = value;
    }

    public void setContext(final ImageEditor editor, final ImageEditorCore core) {
        this.editor = editor;
        this.core = core;
    }

    public void mouseClicked(final MouseEvent e) {
    }

    public void mouseDragged(final MouseEvent e) {
    }

    public void mouseEntered(final MouseEvent e) {
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mouseMoved(final MouseEvent e) {
    }

    public void mousePressed(final MouseEvent e) {
    }

    public void mouseReleased(final MouseEvent e) {
    }

    public void mouseWheelMoved(final MouseWheelEvent e) {
    }
}
