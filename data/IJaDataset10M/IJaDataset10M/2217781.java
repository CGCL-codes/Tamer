package iceboatgame;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import java.lang.Math;
import iceboatgame.IceBoat;

/**
 *
 * @author Administrat�r
 */
public class Physics {

    public static final float TWOPI = (float) Math.PI * 2;

    public static final float DTR = (float) Math.PI / 180f;

    public static final float RTD = 180f / (float) Math.PI;

    public static final double PIHALF2 = Math.PI * Math.PI / 2;

    public static final double PIHALF = Math.PI / 2;

    public static final double g = 9.82;

    public static void makeNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
        Vector3f a = Vector3f.sub(v2, v1, null);
        Vector3f b = Vector3f.sub(v3, v1, null);
        Vector3f c = Vector3f.cross(a, b, null);
        c = c.normalise(null);
        GL11.glNormal3f(c.x, c.y, c.z);
    }

    public static Vector3f rotate3fy(Vector3f v, float rot) {
        return new Vector3f(v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot), v.y, v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot));
    }

    public static Vector3f rotate3fz(Vector3f v, float rot) {
        return new Vector3f(v.x * (float) Math.cos(rot) - v.y * (float) Math.sin(rot), v.x * (float) Math.sin(rot) + v.y * (float) Math.cos(rot), v.z);
    }

    public static Vector3f rotate3fx(Vector3f v, float rot) {
        return new Vector3f(v.x, v.y * (float) Math.cos(rot) - v.z * (float) Math.sin(rot), v.y * (float) Math.sin(rot) + v.z * (float) Math.cos(rot));
    }

    public static Vector2f rotate2f(Vector2f v, float rot) {
        return new Vector2f(v.x * (float) Math.cos(rot) - v.y * (float) Math.sin(rot), v.x * (float) Math.sin(rot) + v.y * (float) Math.cos(rot));
    }

    public static double turnRate(IceBoat ib) {
        if (Math.abs(ib.steerangle) < 0.005) {
            ib.turnrate = 0;
            return 0;
        }
        double r = Math.tan(ib.steerangle) / ib.ibt.length;
        ib.turnrate = (ib.velocity.length() * r);
        return ib.turnrate * ib.turnrate * r;
    }

    public static double sign(double x) {
        return (x < 0) ? -1 : 1;
    }

    public static float angle2f(Vector2f a, Vector2f b) {
        float dls = Vector2f.dot(a, b) / (a.length() * b.length());
        if (dls < -1f) dls = -1f; else if (dls > 1.0f) dls = 1.0f;
        float ang = (float) Math.acos(dls);
        if (a.x * b.y - b.x * a.y < 0) ang = -ang;
        return ang;
    }

    public static double skateFriction(IceBoat ib) {
        if (ib.velocity.y > 0.1) return ib.ibt.friction * Math.pow(2, (-ib.velocity.length() / 5)) + ib.ibt.friction; else if (ib.velocity.y > -0.1) return ib.ibt.friction * 2 * ib.velocity.y / 0.1; else return -ib.ibt.friction * 4;
    }

    public static double boatDrag(IceBoat ib) {
        return ib.ibt.drag * ib.relspeed * ib.relspeed * Math.cos(ib.reldir);
    }

    public static Vector2f sailForce(IceBoat ib) {
        double fl, fd, cos, sin;
        double sa = Math.abs(-ib.reldir - ib.sailangle);
        if (sa < ib.ibt.maxalpha - 0.02) fl = ib.ibt.saillift * sa / (ib.ibt.maxalpha - 0.02); else if (sa < ib.ibt.maxalpha + 0.02) fl = ib.ibt.saillift; else if (sa < PIHALF) fl = ib.ibt.saillift * (PIHALF - sa) / (PIHALF - ib.ibt.maxalpha - 0.02); else fl = 0;
        fl *= ib.relspeed * ib.relspeed;
        if (sa <= PIHALF) fd = Math.min(sa * sa * ib.ibt.sailarea * 0.65 + ib.ibt.saildrag, (1 - ib.ibt.saildrag) * Math.sin(sa) + ib.ibt.saildrag); else fd = Math.sin(sa) * ib.ibt.sailarea * 0.65;
        fd *= ib.relspeed * ib.relspeed;
        cos = Math.cos(ib.reldir);
        sin = Math.sin(ib.reldir);
        if (ib.reldir > 0) return new Vector2f(-(float) (fl * cos + fd * sin), (float) (fl * sin - fd * cos)); else return new Vector2f((float) (fl * cos - fd * sin), -(float) (fl * sin + fd * cos));
    }

    public static void accTurnAndMoment(IceBoat ib) {
        float ay, ax, mx, my, mz;
        double temp = 0;
        Vector2f sf = sailForce(ib);
        double bd = boatDrag(ib);
        double skf = skateFriction(ib);
        double alfa = turnRate(ib);
        mx = (float) (sf.y * ib.ibt.cph - bd * ib.ibt.cgh + ib.ibt.cgl * g * ib.ibt.mass);
        ib.frontforce = Math.max((float) (mx / ib.ibt.length), 0f);
        ib.steerforce = (float) (alfa * ib.ibt.mass * Math.tan(ib.steerangle));
        if (ib.rise >= 0) {
            mx = Math.min(mx, 0f);
            ib.rise = 0;
        }
        my = (float) (sf.x * (ib.ibt.stepz - ib.ibt.cpl * Math.cos(ib.sailangle)) - sf.y * (ib.ibt.stepz - ib.ibt.cpl * Math.sin(ib.sailangle)));
        my += (float) (alfa * ib.ibt.mass * ib.ibt.cgl);
        if (my > 0) my = (float) Math.max(my - ib.ibt.length * ib.frontforce, 0f); else my = (float) Math.min(my + ib.ibt.length * ib.frontforce, 0f);
        if (my != 0f | Math.abs(ib.steerforce) > ib.frontforce) ib.frontslip = true; else ib.frontslip = false;
        mz = (float) (sf.x * ib.ibt.cph + ib.ibt.mass * g * Math.sin(ib.heel) * ib.ibt.cgh);
        temp = ib.ibt.width / 2f * Math.cos(ib.heel) * ib.ibt.mass * g;
        if (mz < 0) {
            mz += (float) temp;
            if (ib.heel >= 0 & mz > 0f) {
                ib.heel = 0;
                mz = 0f;
            }
        } else {
            mz -= (float) temp;
            if (ib.heel <= 0 & mz < 0f) {
                ib.heel = 0;
                mz = 0f;
            }
        }
        ib.moment.set(mx, my, mz);
        temp = alfa + sf.x / ib.ibt.mass;
        if (Math.abs(temp) < g) {
            ax = 0;
            ib.mainslip = false;
        } else {
            ax = (float) (g * Physics.sign(temp) - temp);
            ib.mainslip = true;
        }
        ay = (float) ((-skf - bd + sf.y - Math.abs(ib.steerforce)) / ib.ibt.mass);
        ib.acceleration.set(ax, ay);
        if (!ib.mainslip & ib.frontslip) {
            ib.turnrate = (ib.frontforce * Physics.sign(ib.steerangle) - my / ib.ibt.length) / ib.ibt.moil;
        } else if (ib.mainslip & !ib.frontslip) {
            ib.turnrate = (ib.frontforce * Physics.sign(ib.steerangle) + ax * ib.ibt.mass) / ib.ibt.moil;
        } else if (ib.mainslip & ib.frontslip) {
            ib.turnrate = (ib.frontforce * Physics.sign(ib.steerangle) - my / ib.ibt.length + ax * ib.ibt.mass) / ib.ibt.moil;
        }
    }
}
