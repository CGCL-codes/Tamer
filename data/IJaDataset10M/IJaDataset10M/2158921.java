package roundabout.objects;

import java.awt.Point;

public class CPoint {

    private float x;

    private float y;

    public CPoint() {
        this.x = 0;
        this.y = 0;
    }

    public CPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CPoint(CPoint C) {
        this.x = C.GetX();
        this.y = C.GetY();
    }

    public float GetX() {
        return this.x;
    }

    public void SetX(float x) {
        this.x = x;
    }

    public void SetY(float y) {
        this.y = y;
    }

    public float GetY() {
        return this.y;
    }

    public void SetXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean Equals(CPoint P) {
        if (this.x == P.x && this.y == P.y) return true;
        return false;
    }

    public Point ToPoint() {
        Point P = new Point((int) this.x, (int) this.y);
        return P;
    }

    public float Distance(CPoint P) {
        return (float) Math.sqrt(((this.GetX() - P.GetX()) * (this.GetX() - P.GetX())) + ((this.GetY() - P.GetY()) * (this.GetY() - P.GetY())));
    }
}
