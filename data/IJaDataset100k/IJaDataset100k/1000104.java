package jpatch.boundary.tools;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.vecmath.*;
import jpatch.entity.*;
import jpatch.boundary.*;

public class TangentHandle extends Handle {

    public static final int IN = 1;

    public static final int OUT = 2;

    private int iDirection;

    private OLDControlPoint cp;

    private float fFactor = 1;

    public TangentHandle(Color3f color, OLDControlPoint cp, int direction) {
        super(null, color);
        iDirection = direction;
        this.cp = cp;
    }

    public void setControlPoint(OLDControlPoint cp) {
        this.cp = cp;
    }

    public float getMagnitude() {
        return cp.getInMagnitude();
    }

    public OLDControlPoint getCp() {
        return cp;
    }

    public Point3f getPosition(ViewDefinition viewDef) {
        switch(iDirection) {
            case IN:
                if (cp.getPrev() != null) return cp.getInTangent();
                break;
            case OUT:
                if (cp.getNext() != null) return cp.getOutTangent();
                break;
        }
        return null;
    }

    public void setFactor(MouseEvent mouseEvent) {
        ViewDefinition viewDef = MainFrame.getInstance().getJPatchScreen().getViewDefinition((Component) mouseEvent.getSource());
        Matrix4f m4View = viewDef.getScreenMatrix();
        Point3f p3 = new Point3f();
        p3.set(cp.getPosition());
        m4View.transform(p3);
        Point2D.Float p2Center = new Point2D.Float(p3.x, p3.y);
        if (iDirection == IN) {
            p3.set(cp.getInTangent());
        } else {
            p3.set(cp.getOutTangent());
        }
        m4View.transform(p3);
        Point2D.Float p2Tangent = new Point2D.Float(p3.x, p3.y);
        fFactor = cp.getInMagnitude() / (float) p2Center.distance(p2Tangent);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        ViewDefinition viewDef = MainFrame.getInstance().getJPatchScreen().getViewDefinition((Component) mouseEvent.getSource());
        Matrix4f m4View = viewDef.getScreenMatrix();
        Point2D.Float p2Mouse = new Point2D.Float((float) mouseEvent.getX(), (float) mouseEvent.getY());
        Point3f p3 = new Point3f();
        p3.set(cp.getPosition());
        m4View.transform(p3);
        Point2D.Float p2Center = new Point2D.Float(p3.x, p3.y);
        float d = (float) p2Center.distance(p2Mouse);
        cp.setMagnitude(d * fFactor);
        MainFrame.getInstance().getJPatchScreen().single_update((Component) mouseEvent.getSource());
    }
}
