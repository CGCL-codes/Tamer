package scene;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GLScene3DMouseHandler extends MouseHandler {

    private GLScene3D glscene3d;

    double ancienX, ancienY;

    public GLScene3DMouseHandler(GLScene3D _objet) {
        this.setGlscene3d(_objet);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        ancienY = ancienX = 0;
        switch(e.getButton()) {
            case MouseEvent.BUTTON1:
                if (this.getGlscene3d().selectionMultiple) this.getGlscene3d().selectionMultiple = !this.getGlscene3d().selectionMultiple;
                this.getGlscene3d().px = e.getX();
                this.getGlscene3d().py = e.getY();
                break;
            case MouseEvent.BUTTON3:
                if (e.getX() <= this.getGlscene3d().px || e.getY() <= this.getGlscene3d().py) {
                    this.getGlscene3d().px = e.getX();
                    this.getGlscene3d().py = e.getY();
                }
                this.getGlscene3d().selectionMultiple = true;
                this.getGlscene3d().px = e.getX();
                this.getGlscene3d().py = e.getY();
                this.getGlscene3d().rx = this.getGlscene3d().px;
                this.getGlscene3d().ry = this.getGlscene3d().py;
                break;
            default:
                break;
        }
    }

    public void mouseReleased(MouseEvent e) {
        switch(e.getButton()) {
            case MouseEvent.BUTTON1:
                this.getGlscene3d().rx = this.getGlscene3d().px + 1;
                this.getGlscene3d().ry = this.getGlscene3d().py + 1;
                this.getGlscene3d().pickOne = true;
                break;
            case MouseEvent.BUTTON3:
                if (e.getX() <= this.getGlscene3d().px || e.getY() <= this.getGlscene3d().py) {
                    this.getGlscene3d().rx = this.getGlscene3d().px;
                    this.getGlscene3d().ry = this.getGlscene3d().py;
                } else {
                    this.getGlscene3d().rx = e.getX();
                    this.getGlscene3d().ry = e.getY();
                }
                this.getGlscene3d().pickZone = true;
                break;
            default:
                break;
        }
        ancienY = ancienX = 0;
    }

    public void mouseDragged(MouseEvent arg0) {
        this.getGlscene3d().dragMouseCoordXY = arg0.getPoint();
        System.out.println("test");
        if (ancienX == 0) ancienX = arg0.getX();
        if (ancienY == 0) ancienY = arg0.getY();
        double differenceCoordX = (arg0.getX() - ancienX);
        double differenceCoordY = (arg0.getY() - ancienY);
        ((VisualObject) (VisualObject.getConteneur_vobject().elementAt(this.getGlscene3d().getSelectedVObject() - 1))).setX(((VisualObject) (VisualObject.getConteneur_vobject().elementAt(this.getGlscene3d().getSelectedVObject() - 1))).getX() + differenceCoordX / this.getGlscene3d().getCanvas().getWidth() * (-this.getGlscene3d().xNear + this.getGlscene3d().xFar) * 1. / this.getGlscene3d().getScale());
        ((VisualObject) (VisualObject.getConteneur_vobject().elementAt(this.getGlscene3d().getSelectedVObject() - 1))).setY(((VisualObject) (VisualObject.getConteneur_vobject().elementAt(this.getGlscene3d().getSelectedVObject() - 1))).getY() - differenceCoordY / this.getGlscene3d().getCanvas().getHeight() * (-this.getGlscene3d().yNear + this.getGlscene3d().yFar) * 1. / this.getGlscene3d().getScale());
        ancienX = arg0.getX();
        ancienY = arg0.getY();
    }

    public void mouseMoved(MouseEvent arg0) {
        this.getGlscene3d().mouseCoordXY = arg0.getPoint();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if (rotation > 0) mouseWheelDown(e); else if (rotation < 0) mouseWheelUp(e);
    }

    public void mouseWheelDown(MouseEvent arg0) {
    }

    public void mouseWheelUp(MouseEvent arg0) {
    }

    private GLScene3D getGlscene3d() {
        return glscene3d;
    }

    private void setGlscene3d(GLScene3D glscene3d) {
        this.glscene3d = glscene3d;
    }
}
