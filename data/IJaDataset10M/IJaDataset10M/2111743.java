package locusts.server.physics;

import locusts.common.entities.Entity;

/**
 *
 * @author Hamish Morgan
 */
public class TranslateJoint implements Joint {

    private static final double MIN_DISTANCE = 1f;

    protected final Entity A;

    protected final Entity B;

    protected double Ax;

    protected double Ay;

    protected double Bx;

    protected double By;

    public TranslateJoint(Entity a, Entity b, double ax, double ay, double bx, double by) {
        this.A = a;
        this.B = b;
        this.Ax = ax - a.getX();
        this.Ay = ay - a.getY();
        this.Bx = bx - b.getX();
        this.By = by - b.getY();
    }

    public TranslateJoint(Entity a, Entity b, double x, double y) {
        this(a, b, x, y, x, y);
    }

    public TranslateJoint(Entity a, Entity b) {
        this(a, b, a.getX(), a.getY(), b.getY(), b.getY());
    }

    protected TranslateJoint(TranslateJoint other) {
        this.A = other.A;
        this.B = other.B;
        this.Ax = other.Ax;
        this.Ay = other.Ay;
        this.Bx = other.Bx;
        this.By = other.By;
    }

    public Entity getEntityA() {
        return A;
    }

    public Entity getEntityB() {
        return B;
    }

    public void update(double period) {
        final double Ox = A.getX() + Ax;
        final double Oy = A.getY() + Ay;
        final double Tx = B.getX() + Bx;
        final double Ty = B.getY() + By;
        final double dx = Tx - Ox;
        final double dy = Ty - Oy;
        if (dx * dx + dy * dy <= MIN_DISTANCE * MIN_DISTANCE) return;
        A.setX(A.getX() - Ox + Tx);
        A.setY(A.getY() - Oy + Ty);
        Ax = Tx - A.getX();
        Ay = Ty - A.getY();
        A.setModified();
    }

    @Override
    public TranslateJoint clone() {
        return new TranslateJoint(this);
    }
}
