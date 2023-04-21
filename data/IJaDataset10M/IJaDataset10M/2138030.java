package jat.gps.filters.relative;

import jat.matvec.data.*;
import jat.alg.estimators.*;
import jat.alg.integrators.*;
import jat.gps.*;
import jat.gps.filters.*;
import jat.forces.*;
import jat.timeRef.*;

/**
 * The RGPS_EOM provides the equations of motion for the 
 * RGPS-only EKF without a thruster model.
 * 
 * @author 
 * @version 1.0
 */
public class RGPS_EOM implements Derivatives {

    private IonoModel iono;

    private ReceiverFilterModel rcvr;

    private double t_mjd0 = 51969.0;

    private double issMass = 128990.0;

    private double issArea = 640.7;

    private double issCd = 2.35;

    private CIRA_ExponentialDrag iss_ced = new CIRA_ExponentialDrag(this.issCd, this.issArea, this.issMass);

    private double stsMass = 104328.0;

    private double stsArea = 454.4;

    private double stsCd = 2.0;

    private CIRA_ExponentialDrag sts_ced = new CIRA_ExponentialDrag(this.stsCd, this.stsArea, this.stsMass);

    private URE_Model ure;

    private int nsv;

    private int issIndex;

    private int numberOfStates;

    /**
	 * Constructor
	 * @param nsat number of GPS SVs
	 * @param nstates number of states
	 * @param io IonoModel
	 * @param rc ReceiverFilterModel
	 * @param ur URE_Model
	 */
    public RGPS_EOM(int nsat, int iss, int nstates, IonoModel io, ReceiverFilterModel rc, URE_Model ur) {
        this.nsv = nsat;
        this.issIndex = iss;
        this.numberOfStates = nstates;
        this.ure = ur;
        this.iono = io;
        this.rcvr = rc;
    }

    /**
	 * @see jat.alg.integrators.Derivatives#derivs(double, double[])
	 */
    public double[] derivs(double t, double[] x) {
        int n = this.numberOfStates;
        VectorN out = new VectorN(x.length);
        VectorN r = new VectorN(x[0], x[1], x[2]);
        VectorN v = new VectorN(x[3], x[4], x[5]);
        EstSTM stm = new EstSTM(x, n);
        Matrix phi = stm.phi();
        VectorN clock1 = new VectorN(2);
        clock1.set(0, x[6]);
        clock1.set(1, x[7]);
        double stsdrag = x[8];
        double del_iono = x[9];
        VectorN urevec = new VectorN(this.nsv);
        for (int i = 0; i < this.nsv; i++) {
            urevec.set(i, x[i + 10]);
        }
        int kk = this.issIndex;
        VectorN rISS = new VectorN(x[kk], x[kk + 1], x[kk + 2]);
        VectorN vISS = new VectorN(x[kk + 3], x[kk + 4], x[kk + 5]);
        VectorN clock2 = new VectorN(2);
        clock2.set(0, x[kk + 6]);
        clock2.set(1, x[kk + 7]);
        double ddrag = x[kk + 8];
        out.set(0, v);
        J2Gravity j2chaser = new J2Gravity(r);
        VectorN g = j2chaser.local_gravity();
        double Mjd = this.t_mjd0 + t / 86400.0;
        EarthRef ref = new EarthRef(Mjd);
        sts_ced.compute(ref, r, v);
        VectorN sts_drag0 = sts_ced.dragAccel();
        double dragfactor1 = 1.0 + stsdrag;
        VectorN drag = sts_drag0.times(dragfactor1);
        VectorN vdot = g.plus(drag);
        out.set(3, vdot);
        VectorN bcdot = rcvr.biasProcess(clock1);
        out.set(6, bcdot);
        double dragdot = DragProcessModel.dragProcess(stsdrag);
        out.set(8, dragdot);
        double ionodot = iono.ionoProcess(del_iono);
        out.set(9, ionodot);
        VectorN uredot = ure.ureProcess(urevec);
        out.set(10, uredot);
        out.set(kk, vISS);
        J2Gravity j2iss = new J2Gravity(rISS);
        g = j2iss.local_gravity();
        iss_ced.compute(ref, rISS, vISS);
        VectorN iss_drag0 = iss_ced.dragAccel();
        double dragfactor2 = 1.0 + ddrag;
        drag = iss_drag0.times(dragfactor2);
        vdot = g.plus(drag);
        out.set((kk + 3), vdot);
        VectorN bcdot2 = rcvr.biasProcess(clock2);
        out.set((kk + 6), bcdot2);
        dragdot = DragProcessModel.dragProcess(ddrag);
        out.set((kk + 8), dragdot);
        Matrix A = new Matrix(n, n);
        Matrix eye = new Matrix(3);
        A.setMatrix(0, 3, eye);
        Matrix G = j2chaser.gravityGradient();
        Matrix D = sts_ced.partialR().times(dragfactor1);
        Matrix GD = G.plus(D);
        A.setMatrix(3, 0, GD);
        D = sts_ced.partialV().times(dragfactor1);
        A.setMatrix(3, 3, D);
        A.set(3, 8, sts_drag0.x[0]);
        A.set(4, 8, sts_drag0.x[1]);
        A.set(5, 8, sts_drag0.x[2]);
        A.set(6, 7, 1.0);
        double tau_drag = -1.0 / DragProcessModel.correlationTime;
        A.set(8, 8, tau_drag);
        double tau_iono = -1.0 / iono.correlationTime;
        A.set(9, 9, tau_iono);
        Matrix bigeye = new Matrix(this.nsv);
        Matrix tau_ure = eye.times(-1.0 / URE_Model.correlationTime);
        A.setMatrix(10, 10, tau_ure);
        A.setMatrix(kk, (kk + 3), eye);
        G = j2iss.gravityGradient();
        D = iss_ced.partialR().times(dragfactor2);
        GD = G.plus(D);
        A.setMatrix((kk + 3), kk, GD);
        D = iss_ced.partialV().times(dragfactor2);
        A.setMatrix((kk + 3), (kk + 3), D);
        A.set((kk + 3), (kk + 8), iss_drag0.x[0]);
        A.set((kk + 4), (kk + 8), iss_drag0.x[1]);
        A.set((kk + 5), (kk + 8), iss_drag0.x[2]);
        A.set((kk + 6), (kk + 7), 1.0);
        A.set((kk + 8), (kk + 8), tau_drag);
        Matrix phidot = A.times(phi);
        int k = n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.x[k] = phidot.A[i][j];
                k = k + 1;
            }
        }
        return out.x;
    }
}
