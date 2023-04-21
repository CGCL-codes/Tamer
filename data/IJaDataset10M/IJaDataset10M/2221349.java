package jsesh.mdcDisplayer.swing.groupEditor;

import java.awt.geom.Point2D;

/**
 * Control for a group editor.
 * 
 * @author rosmord
 *  
 */
class GroupEditorControl implements GroupEditorListener {

    private GroupEditor editor;

    Point2D oldPoint;

    int horizontalHandle;

    int verticalHandle;

    boolean handleSelected;

    /**
     * @param editor
     * 
     */
    public GroupEditorControl(GroupEditor editor) {
        this.editor = editor;
        editor.addGroupEditorEventListener(this);
        handleSelected = false;
    }

    public void mouseClicked(GroupEditorEvent e) {
    }

    public void mouseEntered(GroupEditorEvent e) {
    }

    public void mouseExited(GroupEditorEvent e) {
    }

    public void mousePressed(GroupEditorEvent e) {
        editor.setSelected(e.getElementIndex());
        handleSelected = e.isOnHandle();
        if (e.isOnHandle()) {
            horizontalHandle = e.getHorizontalHandlePosition();
            verticalHandle = e.getVerticalHandlePosition();
        }
        if (e.getElementIndex() != -1) {
            oldPoint = new Point2D.Double(e.getPoint().getX(), e.getPoint().getY());
        }
    }

    public void mouseReleased(GroupEditorEvent e) {
        oldPoint = null;
    }

    public void mouseDragged(GroupEditorEvent e) {
        if (editor.getSelected() != -1 && oldPoint != null) {
            if (handleSelected) {
                if (editor.getMode() == GroupEditor.RESIZE) {
                    Point2D p = e.getPoint();
                    double dx = p.getX() - oldPoint.getX();
                    double dy = p.getY() - oldPoint.getY();
                    editor.resizeTo(dx, dy, horizontalHandle, verticalHandle);
                    oldPoint.setLocation(p.getX(), p.getY());
                } else if (editor.getMode() == GroupEditor.ROTATION) {
                    Point2D p = e.getPoint();
                    editor.rotate(oldPoint, p);
                    oldPoint.setLocation(p.getX(), p.getY());
                }
            } else {
                Point2D p = e.getPoint();
                double dx = p.getX() - oldPoint.getX();
                double dy = p.getY() - oldPoint.getY();
                oldPoint.setLocation(p.getX(), p.getY());
                editor.move(dx, dy);
            }
        }
    }

    public void mouseMoved(GroupEditorEvent e) {
    }
}
