package transformaciones;

import java.awt.Point;

public class ArcBall {

    private static final float Epsilon = 1.0e-5f;

    Vector3f StVec;

    Vector3f EnVec;

    float adjustWidth;

    float adjustHeight;

    public ArcBall(float NewWidth, float NewHeight) {
        StVec = new Vector3f();
        EnVec = new Vector3f();
        setBounds(NewWidth, NewHeight);
    }

    public void mapToSphere(Point point, Vector3f vector) {
        Point2f tempPoint = new Point2f(point.x, point.y);
        tempPoint.x = (tempPoint.x * this.adjustWidth) - 1.0f;
        tempPoint.y = 1.0f - (tempPoint.y * this.adjustHeight);
        float length = (tempPoint.x * tempPoint.x) + (tempPoint.y * tempPoint.y);
        if (length > 1.0f) {
            float norm = (float) (1.0 / Math.sqrt(length));
            vector.x = tempPoint.x * norm;
            vector.y = tempPoint.y * norm;
            vector.z = 0.0f;
        } else {
            vector.x = tempPoint.x;
            vector.y = tempPoint.y;
            vector.z = (float) Math.sqrt(1.0f - length);
        }
    }

    public void setBounds(float NewWidth, float NewHeight) {
        assert ((NewWidth > 1.0f) && (NewHeight > 1.0f));
        adjustWidth = 1.0f / ((NewWidth - 1.0f) * 0.5f);
        adjustHeight = 1.0f / ((NewHeight - 1.0f) * 0.5f);
    }

    public void click(Point NewPt) {
        mapToSphere(NewPt, this.StVec);
    }

    public void drag(Point NewPt, Quat4f NewRot) {
        this.mapToSphere(NewPt, EnVec);
        if (NewRot != null) {
            Vector3f Perp = new Vector3f();
            Vector3f.cross(Perp, StVec, EnVec);
            if (Perp.length() > Epsilon) {
                NewRot.x = Perp.x;
                NewRot.y = Perp.y;
                NewRot.z = Perp.z;
                NewRot.w = Vector3f.dot(StVec, EnVec);
            } else {
                NewRot.x = NewRot.y = NewRot.z = NewRot.w = 0.0f;
            }
        }
    }
}
