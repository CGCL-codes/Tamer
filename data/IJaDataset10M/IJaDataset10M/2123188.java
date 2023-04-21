package edu.wpi.first.wpilibj.templates;

public class VelocityControlledElevator {

    double pos;

    double vel;

    double accel;

    double poscmd;

    double posmin;

    double posmax;

    double velmin;

    double velmax;

    double accmin;

    double accmax;

    double deadb;

    double sf;

    double dt;

    int xstate;

    double s0;

    long tf;

    double t;

    VelocityControlledElevator(double t0, double p0, double v0, double pmin, double pmax, double vmin, double vmax, double amin, double amax) {
        this.deadb = 0.1;
        this.t = t0;
        this.pos = p0;
        this.vel = v0;
        this.accel = 0.0;
        this.poscmd = p0;
        this.posmin = pmin;
        this.posmax = pmax;
        this.velmin = vmin;
        this.velmax = vmax;
        this.accmin = amin;
        this.accmax = amax;
        this.sf = 1.0 / (1.0 - deadb);
        this.dt = 0.02;
        this.xstate = 4;
        this.s0 = 0.0;
        this.tf = 0;
    }

    public double control(double vnew, double tnew) {
        dt = tnew - t;
        if (vnew > velmax) {
            vnew = velmax;
        } else if (vnew < velmin) {
            vnew = velmin;
        }
        if ((vnew - vel) > dt * accmax) {
            vnew = vel + dt * accmax;
        } else if ((vnew - vel) < dt * accmin) {
            vnew = vel + dt * accmin;
        }
        double pnew = posUpdate(vnew);
        if (pnew > posmax) {
            pnew = posmax;
            vnew = (posmax - pos) / dt;
        } else if (pnew < posmin) {
            pnew = posmin;
            vnew = (posmin - pos) / dt;
        }
        double v2limmax = 2 * accmin * (pnew - posmax);
        if ((vnew * vnew > v2limmax) && (vnew > 0.0)) {
            vnew = Math.sqrt(v2limmax);
        }
        double v2limmin = 2 * accmax * (pnew - posmin);
        if ((vnew * vnew > v2limmin) && (vnew < 0.0)) {
            vnew = -Math.sqrt(v2limmin);
        }
        pnew = posUpdate(vnew);
        t = tnew;
        pos = pnew;
        vel = vnew;
        return pnew;
    }

    public double scontrol(double s, double tnew) {
        double v = scaleDeadband(s);
        return control(v, tnew);
    }

    public double posUpdate(double v) {
        return pos + 0.5 * (v + vel) * dt;
    }

    public double scaleDeadband(double s) {
        if (s > deadb) return (s - deadb) * sf * velmax; else if (s < (0 - deadb)) return (s + deadb) * sf * Math.abs(velmin); else return 0.0;
    }
}
