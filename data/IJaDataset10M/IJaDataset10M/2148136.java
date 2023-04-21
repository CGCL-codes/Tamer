package net.java.dev.joode.stepper;

import java.util.Arrays;
import java.util.Random;
import net.java.dev.joode.Body;
import net.java.dev.joode.World;
import net.java.dev.joode.joint.Joint;
import net.java.dev.joode.joint.JointFeedback;
import net.java.dev.joode.util.*;

public class QuickStepper extends AbstractStepperFunction {

    public static final Vector3 avel = new Vector3();

    public static final Vector3 lvel = new Vector3();

    private static final QuickStepper INSTANCE = new QuickStepper();

    public static final QuickStepper getInstance() {
        return INSTANCE;
    }

    /**
     * for the SOR and CG methods: uncomment the following line to use warm
     * starting. this definitely help for motor-driven joints. unfortunately it
     * appears to hurt with high-friction contacts using the SOR method. use
     * with care
     */
    public static final boolean WARM_STARTING = false;

    /**
     * for the SOR method: uncomment the following line to determine a new
     * constraint-solving order for each iteration. however, the qsort per
     * iteration is expensive, and the optimal order is somewhat problem
     * dependent.
     * 
     * @@@ try the leaf->root ordering.
     */
    public static final boolean REORDER_CONSTRAINTS = false;

    /**
     * for the SOR method: uncomment the following line to randomly reorder
     * constraint rows during the solution. depending on the situation, this can
     * help a lot or hardly at all, but it doesn't seem to hurt.
     */
    public static final boolean RANDOMLY_REORDER_CONSTRAINTS = true;

    public static final Random rnd = new Random(0);

    public final QuickStepParameters qsp = new QuickStepParameters();

    /**
     * {@inheritDoc}
     */
    @Override
    public void stepConstraints(int m, World world, Body[] body, int bcount, Joint[] joint, int jcount, Joint.Info1[] info, int[] ofs, Matrix3[] invI, float t_start, float t_end) {
        int i, j;
        float stepsize = t_end - t_start;
        float fps = 1 / stepsize;
        Matrix J = new Matrix(12, m);
        Real c = new Real(m);
        Real cfm = new Real(m);
        Arrays.fill(cfm.m, world.getGlobalCFM());
        Real lo = new Real(m);
        Arrays.fill(lo.m, Float.NEGATIVE_INFINITY);
        Real hi = new Real(m);
        Arrays.fill(hi.m, Float.POSITIVE_INFINITY);
        int[] findex = new int[m];
        Arrays.fill(findex, -1);
        Joint.Info2 Jinfo = new Joint.Info2();
        Jinfo.rowskip = 12;
        Jinfo.fps = fps;
        Jinfo.erp = world.getGlobalERP();
        Jinfo.J1l = new RealPointer(J);
        Jinfo.J1a = new RealPointer(J);
        Jinfo.J2l = new RealPointer(J);
        Jinfo.J2a = new RealPointer(J);
        Jinfo.c = new RealPointer(c);
        Jinfo.cfm = new RealPointer(cfm);
        Jinfo.lo = new RealPointer(lo);
        Jinfo.hi = new RealPointer(hi);
        Jinfo.findex = new IntPointer(findex);
        for (i = 0; i < jcount; i++) {
            int start = ofs[i] * 12;
            Jinfo.J1l.setIndex(start);
            Jinfo.J1a.setIndex(start + 3);
            Jinfo.J2l.setIndex(start + 6);
            Jinfo.J2a.setIndex(start + 9);
            Jinfo.c.setIndex(ofs[i]);
            Jinfo.cfm.setIndex(ofs[i]);
            Jinfo.lo.setIndex(ofs[i]);
            Jinfo.hi.setIndex(ofs[i]);
            Jinfo.findex.setIndex(ofs[i]);
            joint[i].getInfo2(Jinfo);
            for (j = 0; j < info[i].m; j++) {
                if (findex[ofs[i] + j] >= 0) findex[ofs[i] + j] += ofs[i];
            }
        }
        Matrix Jcopy = new Matrix(12, m);
        System.arraycopy(J.m, 0, Jcopy.m, 0, m * 12);
        int[][] jb = new int[m][2];
        for (i = 0; i < jcount; i++) {
            int b1 = (joint[i].getBody(0) != null) ? (joint[i].getBody(0).getTag()) : -1;
            int b2 = (joint[i].getBody(1) != null) ? (joint[i].getBody(1).getTag()) : -1;
            for (j = 0; j < info[i].m; j++) {
                jb[ofs[i] + j][0] = b1;
                jb[ofs[i] + j][1] = b2;
            }
        }
        float[] rhs = this.computeRightHandSide(body, bcount, fps, invI, m, J, c, jb);
        for (i = 0; i < m; i++) cfm.m[i] *= fps;
        float[] lambda = new float[m];
        if (WARM_STARTING) {
            for (i = 0; i < jcount; i++) System.arraycopy(joint[i].getLambda().m, 0, lambda, ofs[i], info[i].m);
        }
        float[][] cforce = new float[bcount][6];
        this.SOR_LCP(m, J, jb, body, invI, lambda, cforce, rhs, lo.m, hi.m, cfm.m, findex);
        if (WARM_STARTING) {
            for (i = 0; i < jcount; i++) System.arraycopy(lambda, ofs[i], joint[i].getLambda().m, 0, info[i].m);
        }
        this.addConstraintForces(body, bcount, cforce, stepsize);
        this.setJointFeedback(joint, jcount, info, ofs, Jcopy, lambda);
    }

    /**
     * compute c * h - J * (v / h + invM * fe)
     */
    private float[] computeRightHandSide(Body[] body, int bcount, float fps, Matrix3[] invI, int m, Matrix J, Real c, int[][] jb) {
        int i;
        int j;
        Vector3[][] tmp1 = new Vector3[bcount][2];
        for (i = 0; i < bcount; i++) {
            tmp1[i][0] = new Vector3();
            tmp1[i][1] = new Vector3();
            body[i].getLinearVel(lvel);
            body[i].getAngularVel(avel);
            for (j = 0; j < 3; j++) tmp1[i][0].m[j] = body[i].getFACC().m[j] * body[i].getMass().getInverseMass() + lvel.m[j] * fps;
            MathUtils.dMULTIPLY0_331(tmp1[i][1], invI[i], body[i].getTACC());
            for (j = 0; j < 3; j++) tmp1[i][1].m[j] += avel.m[j] * fps;
        }
        float[] rhs = new float[m];
        multiply_J(m, J, jb, tmp1, rhs);
        for (i = 0; i < m; i++) rhs[i] = c.m[i] * fps - rhs[i];
        return rhs;
    }

    private void addConstraintForces(Body[] body, int bcount, float[][] cforce, float stepsize) {
        int i, j;
        for (i = 0; i < bcount; i++) {
            body[i].getLinearVel(lvel);
            body[i].getAngularVel(avel);
            for (j = 0; j < 3; j++) lvel.m[j] += stepsize * cforce[i][j];
            for (j = 0; j < 3; j++) avel.m[j] += stepsize * cforce[i][j + 3];
            body[i].setLinearVel(lvel);
            body[i].setAngularVel(avel);
        }
    }

    private void setJointFeedback(Joint[] joint, int jcount, Joint.Info1[] info, int[] ofs, Matrix Jcopy, float[] lambda) {
        for (int i = 0; i < jcount; i++) {
            final JointFeedback feedback = joint[i].getFeedback();
            if (feedback != null) {
                float[] data = new float[6];
                Multiply1_12q1(data, Jcopy, lambda, info[i].m, ofs[i], 0);
                feedback.getForce1().setX(data[0]);
                feedback.getForce1().setY(data[1]);
                feedback.getForce1().setZ(data[2]);
                feedback.getTorque1().setX(data[3]);
                feedback.getTorque1().setY(data[4]);
                feedback.getTorque1().setZ(data[5]);
                if (joint[i].getBody(1) != null) {
                    Multiply1_12q1(data, Jcopy, lambda, info[i].m, ofs[i], 6);
                    feedback.getForce2().setX(data[0]);
                    feedback.getForce2().setY(data[1]);
                    feedback.getForce2().setZ(data[2]);
                    feedback.getTorque2().setX(data[3]);
                    feedback.getTorque2().setY(data[4]);
                    feedback.getTorque2().setZ(data[5]);
                }
            }
        }
    }

    /**
     * compute out = J*in.
     */
    private static void multiply_J(int m, Matrix J, int[][] jb, Vector3[][] in, float[] out) {
        int i, j;
        for (i = 0; i < m; i++) {
            float sum = 0;
            int body1 = jb[i][0];
            for (j = 0; j < 6; j++) {
                if (j < 3) sum += J.get(j, i) * in[body1][0].m[j]; else sum += J.get(j, i) * in[body1][1].m[j - 3];
            }
            if (jb[i][1] >= 0) {
                int body2 = jb[i][1];
                for (j = 0; j < 6; j++) if (j < 3) sum += J.get(j + 6, i) * in[body2][0].m[j]; else sum += J.get(j + 6, i) * in[body2][1].m[j - 3];
            }
            out[i] = sum;
        }
    }

    /**
     * multiply block of B matrix (q x 6) with 12 dReal per row with C vektor (q)
     */
    private static void Multiply1_12q1(float[] A, Matrix B, float[] C, int q, int ofs, int ofsb) {
        int i, k;
        float sum;
        for (i = 0; i < 6; i++) {
            sum = 0;
            for (k = ofs; k < ofs + q; k++) sum += B.get(i + ofsb, k) * C[k];
            A[i] = sum;
        }
    }

    /**
     * SOR-LCP method
     * 
     * note: fc is returned as inv(M)*J'*lambda, the constraint force is
     * actually J'*lambda
     * 
     * @param m
     *            number of constraints
     * @param J
     *            an m*12 matrix of constraint rows
     * @param jb
     *            an array of first and second body numbers for each constraint
     *            row
     * @param invI
     *            the global frame inverse inertia for each body (stacked 3x3
     *            matrices)
     * @param lambda
     *            return values
     * @param fc
     *            returns the constraint force
     * @param b
     * @param lo
     * @param hi
     */
    public void SOR_LCP(int m, Matrix J, int[][] jb, Body[] body, Matrix3[] invI, float[] lambda, float[][] fc, float[] b, float[] lo, float[] hi, float[] cfm, int[] findex) {
        int i, j;
        float[][] iMJ = new float[m][12];
        compute_invM_JT(m, J, iMJ, jb, body, invI);
        if (WARM_STARTING) {
            for (i = 0; i < m; i++) lambda[i] *= 0.9;
            multiply_invM_JT(m, iMJ, jb, lambda, fc);
        }
        float[] Ad = new float[m];
        for (i = 0; i < m; i++) {
            float sum = 0;
            for (j = 0; j < 6; j++) sum += iMJ[i][j] * J.get(j, i);
            if (jb[i][1] >= 0) {
                for (j = 6; j < 12; j++) sum += iMJ[i][j] * J.get(j, i);
            }
            Ad[i] = this.qsp.getW() / (sum + cfm[i]);
        }
        for (i = 0; i < m; i++) {
            for (j = 0; j < 12; j++) {
                J.set(j, i, J.get(j, i) * Ad[i]);
            }
            b[i] *= Ad[i];
        }
        for (i = 0; i < m; i++) Ad[i] *= cfm[i];
        IndexError[] order = new IndexError[m];
        for (i = 0; i < m; i++) order[i] = new IndexError();
        if (!REORDER_CONSTRAINTS) {
            j = 0;
            for (i = 0; i < m; i++) if (findex[i] < 0) {
                order[j].index = i;
                j++;
            }
            for (i = 0; i < m; i++) if (findex[i] >= 0) {
                order[j].index = i;
                j++;
            }
            assert (j == m);
        }
        float[] hicopy = new float[m];
        System.arraycopy(hi, 0, hicopy, 0, m);
        float[] last_lambda = new float[m];
        for (int iteration = 0; iteration < this.qsp.getNumIterations(); iteration++) {
            if (REORDER_CONSTRAINTS) {
                if (iteration < 2) {
                    for (i = 0; i < m; i++) {
                        order[i].error = i;
                        order[i].findex = findex[i];
                        order[i].index = i;
                    }
                } else {
                    for (i = 0; i < m; i++) {
                        float v1 = Math.abs(lambda[i]);
                        float v2 = Math.abs(last_lambda[i]);
                        float max = (v1 > v2) ? v1 : v2;
                        if (max > 0) {
                            order[i].error = Math.abs(lambda[i] - last_lambda[i]);
                        } else {
                            order[i].error = Float.POSITIVE_INFINITY;
                        }
                        order[i].findex = findex[i];
                        order[i].index = i;
                    }
                    System.arraycopy(lambda, 0, last_lambda, 0, m);
                }
                Arrays.sort(order);
            }
            if (RANDOMLY_REORDER_CONSTRAINTS) {
                if ((iteration & 7) == 0) {
                    for (i = 1; i < m; ++i) {
                        IndexError tmp = order[i];
                        int swapi = rnd.nextInt(i);
                        order[i] = order[swapi];
                        order[swapi] = tmp;
                    }
                }
            }
            for (i = 0; i < m; i++) {
                int index = order[i].index;
                if (findex[index] >= 0) {
                    hi[index] = Math.abs(hicopy[index] * lambda[findex[index]]);
                    lo[index] = -hi[index];
                }
                int b1 = jb[index][0];
                int b2 = jb[index][1];
                float delta = b[index] - lambda[index] * Ad[index];
                delta -= fc[b1][0] * J.get(0, index) + fc[b1][1] * J.get(1, index) + fc[b1][2] * J.get(2, index) + fc[b1][3] * J.get(3, index) + fc[b1][4] * J.get(4, index) + fc[b1][5] * J.get(5, index);
                if (b2 >= 0) {
                    delta -= fc[b2][0] * J.get(6, index) + fc[b2][1] * J.get(7, index) + fc[b2][2] * J.get(8, index) + fc[b2][3] * J.get(9, index) + fc[b2][4] * J.get(10, index) + fc[b2][5] * J.get(11, index);
                }
                float new_lambda = lambda[index] + delta;
                if (new_lambda < lo[index]) {
                    delta = lo[index] - lambda[index];
                    lambda[index] = lo[index];
                } else if (new_lambda > hi[index]) {
                    delta = hi[index] - lambda[index];
                    lambda[index] = hi[index];
                } else {
                    lambda[index] = new_lambda;
                }
                fc[b1][0] += delta * iMJ[index][0];
                fc[b1][1] += delta * iMJ[index][1];
                fc[b1][2] += delta * iMJ[index][2];
                fc[b1][3] += delta * iMJ[index][3];
                fc[b1][4] += delta * iMJ[index][4];
                fc[b1][5] += delta * iMJ[index][5];
                if (b2 >= 0) {
                    fc[b2][0] += delta * iMJ[index][6];
                    fc[b2][1] += delta * iMJ[index][7];
                    fc[b2][2] += delta * iMJ[index][8];
                    fc[b2][3] += delta * iMJ[index][9];
                    fc[b2][4] += delta * iMJ[index][10];
                    fc[b2][5] += delta * iMJ[index][11];
                }
            }
        }
    }

    /** compute iMJ = inv(M)*J' */
    private static void compute_invM_JT(int m, Matrix J, float[][] iMJ, int[][] jb, Body[] body, Matrix3[] invI) {
        int i, j;
        for (i = 0; i < m; i++) {
            int b = jb[i][0];
            float k = body[b].getMass().getInverseMass();
            for (j = 0; j < 3; j++) iMJ[i][j] = k * J.get(j, i);
            for (j = 0; j < 3; j++) iMJ[i][j + 3] = invI[b].m[j * 4] * J.get(3, i) + invI[b].m[j * 4 + 1] * J.get(4, i) + invI[b].m[j * 4 + 2] * J.get(5, i);
            if (jb[i][1] >= 0) {
                b = jb[i][1];
                k = body[b].getMass().getInverseMass();
                for (j = 0; j < 3; j++) iMJ[i][j + 6] = k * J.get(j + 6, i);
                for (j = 0; j < 3; j++) iMJ[i][j + 9] = invI[b].m[j * 4] * J.get(9, i) + invI[b].m[j * 4 + 1] * J.get(10, i) + invI[b].m[j * 4 + 2] * J.get(11, i);
            }
        }
    }

    /** compute out = inv(M)*J'*in. */
    private static void multiply_invM_JT(int m, float[][] iMJ, int[][] jb, float[] in, float[][] out) {
        int i, j;
        for (i = 0; i < m; i++) {
            int b1 = jb[i][0];
            for (j = 0; j < 6; j++) out[b1][j] += iMJ[i][j] * in[i];
            if (jb[i][1] >= 0) {
                int b2 = jb[i][1];
                for (j = 0; j < 6; j++) out[b2][j] += iMJ[i][j + 6] * in[i];
            }
        }
    }

    private static class IndexError implements Comparable<IndexError> {

        float error;

        int findex;

        int index;

        public int compareTo(IndexError i2) {
            if (this.findex < 0 && i2.findex >= 0) return -1;
            if (this.findex >= 0 && i2.findex < 0) return 1;
            if (this.error < i2.error) return -1;
            if (this.error > i2.error) return 1;
            return 0;
        }
    }
}
