package kursach2;

import kursach2.THuman;
import kursach2.ICollision;
import kursach2.barriers.ABarier;

public class HumanCollision implements ICollision {

    private int Type;

    private boolean OneSided;

    private double d12, d21;

    private double[] ProbTime;

    private double Time, Dt;

    private int N;

    private ABarier Human1, Human2;

    private double Alfa1, Alfa2;

    private double x1, y1, x2, y2;

    private boolean Coll_Is_Compute;

    private boolean Hum1_Forward = false;

    private boolean Hum2_Forward = false;

    private boolean NearColl = false;

    private double n1, n2, ln;

    public boolean isCollision() {
        return (Time < 0.0);
    }

    public boolean isHum1_Forward() {
        return Hum1_Forward;
    }

    public boolean isHum2_Forward() {
        return Hum2_Forward;
    }

    public boolean isNearColl() {
        return NearColl;
    }

    public void flee(double dt) {
        if (!isCollision()) return;
        double dx = Human1.getX() - Human2.getX();
        double dy = Human1.getY() - Human2.getY();
        double D = Math.sqrt(dx * dx + dy * dy);
        if (Human1 instanceof IHuman && multScal(dx, dy, Human1.getVX(), Human1.getVY()) < 0 && d12 < 1.90 * (Human1.getWidth(0, 0) + Human2.getWidth(0, 0)) / 2) {
            ((IHuman) Human1).accel(-2 * dt);
        }
        if (Human2 instanceof IHuman && multScal(-dx, -dy, Human2.getVX(), Human2.getVY()) < 0 && d21 < 1.90 * (Human1.getWidth(0, 0) + Human2.getWidth(0, 0)) / 2) {
            ((IHuman) Human2).accel(-2 * dt);
        }
        dx /= D;
        dy /= D;
        double g = 0.05, dg = 0.5;
        double r1 = Human1.getWidth(Human2.getX(), Human2.getY()), r2 = Human2.getWidth(Human1.getX(), Human1.getY());
        double r = r1 + r2;
        if (!this.isHum1_Forward() && this.isHum2_Forward()) {
            Human1.setXY(Human2.getX() + r * dx * (1 + g * dg), Human2.getY() + r * dy * (1 + g * dg));
            Human2.setXY(Human2.getX() - r2 * dx * g * (1 - dg), Human2.getY() - r2 * dy * g * (1 - dg));
        } else if (this.isHum1_Forward() && !this.isHum2_Forward()) {
            Human2.setXY(Human1.getX() - r * dx * (1 + g * dg), Human1.getY() - r * dy * (1 + g * dg));
            Human1.setXY(Human1.getX() + r1 * dx * g * (1 - dg), Human1.getY() + r1 * dy * g * (1 - dg));
        } else {
            Human1.setXY(Human2.getX() + r * dx * (1 + g / 2), Human2.getY() + r * dy * (1 + g / 2));
            Human2.setXY(Human2.getX() - r2 * dx * g / 2, Human2.getY() - r2 * dy * g / 2);
        }
    }

    public HumanCollision(double time, ABarier h1, ABarier h2) {
        Time = time;
        Dt = Const.Steplength;
        N = Const.Nsight;
        Human1 = h1;
        Human2 = h2;
        Coll_Is_Compute = false;
        NearColl = false;
        analysisOfCollision();
    }

    public boolean getColl_Is_Compute() {
        return Coll_Is_Compute;
    }

    public void setProbTime(double[] t) {
        ProbTime = t;
    }

    public double[] getProbTime() {
        return ProbTime;
    }

    public double getTime() {
        return Time;
    }

    public void setDt(double t) {
        Dt = t;
    }

    public double getDt() {
        return Dt;
    }

    public boolean isOneSided() {
        return OneSided;
    }

    public void setHumans(THuman h1, THuman h2) {
        Human1 = h1;
        Human2 = h2;
    }

    public ABarier getHuman1() {
        return Human1;
    }

    public ABarier getHuman2() {
        return Human2;
    }

    public double getAlfa1() {
        return Alfa1;
    }

    public double getAlfa2() {
        return Alfa2;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getD12() {
        return d12;
    }

    public double getD21() {
        return d21;
    }

    public int getN() {
        return N;
    }

    public void analysisOfCollision() {
        Coll_Is_Compute = true;
        if ((Time < -2) || (Time > N)) {
            Coll_Is_Compute = false;
            return;
        }
        x1 = Human1.getX();
        y1 = Human1.getY();
        x2 = Human2.getX();
        y2 = Human2.getY();
        if (Time > 0.0) {
            x1 += Time * Dt * Human1.getVX();
            y1 += Time * Dt * Human1.getVY();
            x2 += Time * Dt * Human2.getVX();
            y2 += Time * Dt * Human2.getVY();
        }
        double dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        if (Time < 0.0) {
            n1 = dx;
            n2 = dy;
            ln = D;
            n1 /= ln;
            n2 /= ln;
            ln = (Human1.getWidth(0, 0) + Human2.getWidth(0, 0)) - D;
        }
        OneSided = oneSided(Human1.getVX(), Human1.getVY(), Human2.getVX(), Human2.getVY(), x1, y1, x2, y2);
        if ((Time <= N + 1)) {
            double vx = (Human1.getLenV() > 0) ? Human1.getVX() : Math.cos(Human1.getAlpha()), vy = (Human1.getLenV() > 0) ? Human1.getVY() : Math.sin(Human1.getAlpha());
            double a = multScal(dx, dy, vx, vy);
            Alfa1 = a / (Math.abs(Human1.getLenV()) * D);
            if (Alfa1 < 0) Hum1_Forward = true;
            if (Math.abs(Alfa1) > 1) Alfa1 = Math.signum(Alfa1);
            dx *= -1;
            dy *= -1;
            vx = (Human2.getLenV() > 0) ? Human2.getVX() : Math.cos(Human2.getAlpha());
            vy = (Human2.getLenV() > 0) ? Human2.getVY() : Math.sin(Human2.getAlpha());
            a = multScal(dx, dy, vx, vy);
            Alfa2 = a / (Math.abs(Human2.getLenV()) * D);
            if (Alfa2 < 0) Hum2_Forward = true;
            if (Math.abs(Alfa2) > 1) Alfa2 = Math.signum(Alfa2);
            d12 = Math.sqrt(1 - Alfa1 * Alfa1) * D;
            d21 = Math.sqrt(1 - Alfa2 * Alfa2) * D;
            if (Hum1_Forward && Hum2_Forward) {
                Type = -1;
            } else if (Hum1_Forward || Hum2_Forward) {
                Type = 1;
            } else if (!Hum1_Forward && !Hum2_Forward) {
                Type = 2;
            }
        }
    }

    private boolean oneSided(double a1, double a2, double b1, double b2, double x1, double y1, double x2, double y2) {
        double n1 = y1 - y2, n2 = x2 - x1;
        if (java.lang.Math.signum(multScal(a1, a2, n1, n2)) * java.lang.Math.signum(multScal(b1, b2, n1, n2)) > 0) return true; else return false;
    }

    private double multScal(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public int getType() {
        return Type;
    }

    public double getN1() {
        return n1;
    }

    public double getN2() {
        return n2;
    }

    public double getLn() {
        return ln;
    }

    @Override
    public double[] getAvoidVLeft() {
        return null;
    }

    @Override
    public double[] getAvoidVRight() {
        return null;
    }

    @Override
    public void set(ABarier b1, ABarier b2) {
    }
}
