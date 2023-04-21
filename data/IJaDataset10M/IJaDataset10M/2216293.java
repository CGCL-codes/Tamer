package net.lunglet.svm;

import net.lunglet.array4j.matrix.FloatVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Svm {

    private final Logger logger = LoggerFactory.getLogger(Svm.class);

    static class decision_function {

        double[] alpha;

        double rho;
    }

    private static double sigmoid_predict(final double decision_value, final double A, final double B) {
        double fApB = decision_value * A + B;
        if (fApB >= 0) {
            return Math.exp(-fApB) / (1.0 + Math.exp(-fApB));
        } else {
            return 1.0 / (1 + Math.exp(fApB));
        }
    }

    private static void solveNuSvc(final SvmProblem prob, final SvmParameter param, final double[] alpha, final Solver.SolutionInfo si) {
        int i;
        int l = prob.l;
        double nu = param.nu;
        byte[] y = new byte[l];
        for (i = 0; i < l; i++) {
            if (prob.y[i] > 0) {
                y[i] = +1;
            } else {
                y[i] = -1;
            }
        }
        double sum_pos = nu * l / 2;
        double sum_neg = nu * l / 2;
        for (i = 0; i < l; i++) {
            if (y[i] == +1) {
                alpha[i] = Math.min(1.0, sum_pos);
                sum_pos -= alpha[i];
            } else {
                alpha[i] = Math.min(1.0, sum_neg);
                sum_neg -= alpha[i];
            }
        }
        double[] zeros = new double[l];
        for (i = 0; i < l; i++) {
            zeros[i] = 0;
        }
        NuSolver s = new NuSolver();
        s.Solve(l, new SVCKernel(prob, param, y), zeros, y, alpha, 1.0, 1.0, param.eps, si, param.shrinking);
        double r = si.r;
        for (i = 0; i < l; i++) {
            alpha[i] *= y[i] / r;
        }
        si.rho /= r;
        si.obj /= (r * r);
        si.upper_bound_p = 1 / r;
        si.upper_bound_n = 1 / r;
    }

    private static void solveOneClass(final SvmProblem prob, final SvmParameter param, final double[] alpha, final Solver.SolutionInfo si) {
        int l = prob.l;
        double[] zeros = new double[l];
        byte[] ones = new byte[l];
        int i;
        int n = (int) (param.nu * prob.l);
        for (i = 0; i < n; i++) {
            alpha[i] = 1;
        }
        if (n < prob.l) {
            alpha[n] = param.nu * prob.l - n;
        }
        for (i = n + 1; i < l; i++) {
            alpha[i] = 0;
        }
        for (i = 0; i < l; i++) {
            zeros[i] = 0;
            ones[i] = 1;
        }
        Solver s = new Solver();
        s.Solve(l, new OneClassKernel(prob, param), zeros, ones, alpha, 1.0, 1.0, param.eps, si, param.shrinking);
    }

    public static String svm_check_parameter(final SvmProblem prob, final SvmParameter param) {
        int svmType = param.svm_type;
        if (svmType != SvmParameter.C_SVC && svmType != SvmParameter.NU_SVC && svmType != SvmParameter.ONE_CLASS && svmType != SvmParameter.EPSILON_SVR && svmType != SvmParameter.NU_SVR) {
            return "unknown svm type";
        }
        int kernelType = param.kernel_type;
        if (kernelType != SvmParameter.LINEAR && kernelType != SvmParameter.POLY && kernelType != SvmParameter.RBF && kernelType != SvmParameter.SIGMOID && kernelType != SvmParameter.PRECOMPUTED) {
            return "unknown kernel type";
        }
        if (param.degree < 0) {
            return "degree of polynomial kernel < 0";
        }
        if (param.cache_size <= 0) {
            return "cache_size <= 0";
        }
        if (param.eps <= 0) {
            return "eps <= 0";
        }
        if (svmType == SvmParameter.C_SVC || svmType == SvmParameter.EPSILON_SVR || svmType == SvmParameter.NU_SVR) {
            if (param.C <= 0) {
                return "C <= 0";
            }
        }
        if (svmType == SvmParameter.NU_SVC || svmType == SvmParameter.ONE_CLASS || svmType == SvmParameter.NU_SVR) {
            if (param.nu <= 0 || param.nu > 1) {
                return "nu <= 0 or nu > 1";
            }
        }
        if (svmType == SvmParameter.EPSILON_SVR) {
            if (param.p < 0) {
                return "p < 0";
            }
        }
        if (param.shrinking != 0 && param.shrinking != 1) {
            return "shrinking != 0 and shrinking != 1";
        }
        if (param.probability != 0 && param.probability != 1) {
            return "probability != 0 and probability != 1";
        }
        if (param.probability == 1 && svmType == SvmParameter.ONE_CLASS) {
            return "one-class SVM probability output not supported yet";
        }
        if (svmType == SvmParameter.NU_SVC) {
            int l = prob.l;
            int maxNrClass = 16;
            int nrClass = 0;
            int[] label = new int[maxNrClass];
            int[] count = new int[maxNrClass];
            int i;
            for (i = 0; i < l; i++) {
                int thisLabel = (int) prob.y[i];
                int j;
                for (j = 0; j < nrClass; j++) {
                    if (thisLabel == label[j]) {
                        ++count[j];
                        break;
                    }
                }
                if (j == nrClass) {
                    if (nrClass == maxNrClass) {
                        maxNrClass *= 2;
                        int[] newData = new int[maxNrClass];
                        System.arraycopy(label, 0, newData, 0, label.length);
                        label = newData;
                        newData = new int[maxNrClass];
                        System.arraycopy(count, 0, newData, 0, count.length);
                        count = newData;
                    }
                    label[nrClass] = thisLabel;
                    count[nrClass] = 1;
                    ++nrClass;
                }
            }
            for (i = 0; i < nrClass; i++) {
                int n1 = count[i];
                for (int j = i + 1; j < nrClass; j++) {
                    int n2 = count[j];
                    if (param.nu * (n1 + n2) / 2 > Math.min(n1, n2)) {
                        return "specified nu is infeasible";
                    }
                }
            }
        }
        return null;
    }

    public static int svm_check_probability_model(final SvmModel model) {
        if (((model.param.svm_type == SvmParameter.C_SVC || model.param.svm_type == SvmParameter.NU_SVC) && model.probA != null && model.probB != null) || ((model.param.svm_type == SvmParameter.EPSILON_SVR || model.param.svm_type == SvmParameter.NU_SVR) && model.probA != null)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void svm_get_labels(final SvmModel model, final int[] label) {
        if (model.label != null) {
            for (int i = 0; i < model.nr_class; i++) {
                label[i] = model.label[i];
            }
        }
    }

    ;

    public static int svm_get_nr_class(final SvmModel model) {
        return model.nr_class;
    }

    public static int svm_get_svm_type(final SvmModel model) {
        return model.param.svm_type;
    }

    private static void svm_group_classes(final SvmProblem prob, final int[] nr_class_ret, final int[][] label_ret, final int[][] start_ret, final int[][] count_ret, final int[] perm) {
        int l = prob.l;
        int max_nr_class = 16;
        int nr_class = 0;
        int[] label = new int[max_nr_class];
        int[] count = new int[max_nr_class];
        int[] data_label = new int[l];
        int i;
        for (i = 0; i < l; i++) {
            int this_label = (int) (prob.y[i]);
            int j;
            for (j = 0; j < nr_class; j++) {
                if (this_label == label[j]) {
                    ++count[j];
                    break;
                }
            }
            data_label[i] = j;
            if (j == nr_class) {
                if (nr_class == max_nr_class) {
                    max_nr_class *= 2;
                    int[] new_data = new int[max_nr_class];
                    System.arraycopy(label, 0, new_data, 0, label.length);
                    label = new_data;
                    new_data = new int[max_nr_class];
                    System.arraycopy(count, 0, new_data, 0, count.length);
                    count = new_data;
                }
                label[nr_class] = this_label;
                count[nr_class] = 1;
                ++nr_class;
            }
        }
        int[] start = new int[nr_class];
        start[0] = 0;
        for (i = 1; i < nr_class; i++) {
            start[i] = start[i - 1] + count[i - 1];
        }
        for (i = 0; i < l; i++) {
            perm[start[data_label[i]]] = i;
            ++start[data_label[i]];
        }
        start[0] = 0;
        for (i = 1; i < nr_class; i++) {
            start[i] = start[i - 1] + count[i - 1];
        }
        nr_class_ret[0] = nr_class;
        label_ret[0] = label;
        start_ret[0] = start;
        count_ret[0] = count;
    }

    public static double svm_predict(final SvmModel model, final FloatVector x) {
        if (model.param.svm_type == SvmParameter.ONE_CLASS || model.param.svm_type == SvmParameter.EPSILON_SVR || model.param.svm_type == SvmParameter.NU_SVR) {
            double[] res = new double[1];
            svm_predict_values(model, x, res);
            if (model.param.svm_type == SvmParameter.ONE_CLASS) {
                return (res[0] > 0) ? 1 : -1;
            } else {
                return res[0];
            }
        } else {
            int i;
            int nr_class = model.nr_class;
            double[] dec_values = new double[nr_class * (nr_class - 1) / 2];
            svm_predict_values(model, x, dec_values);
            int[] vote = new int[nr_class];
            for (i = 0; i < nr_class; i++) {
                vote[i] = 0;
            }
            int pos = 0;
            for (i = 0; i < nr_class; i++) {
                for (int j = i + 1; j < nr_class; j++) {
                    if (dec_values[pos++] > 0) {
                        ++vote[i];
                    } else {
                        ++vote[j];
                    }
                }
            }
            int vote_max_idx = 0;
            for (i = 1; i < nr_class; i++) {
                if (vote[i] > vote[vote_max_idx]) {
                    vote_max_idx = i;
                }
            }
            return model.label[vote_max_idx];
        }
    }

    public static void svm_predict_values(final SvmModel model, final FloatVector x, final double[] dec_values) {
        if (model.param.svm_type == SvmParameter.ONE_CLASS || model.param.svm_type == SvmParameter.EPSILON_SVR || model.param.svm_type == SvmParameter.NU_SVR) {
            double[] sv_coef = model.sv_coef[0];
            double sum = 0;
            for (int i = 0; i < model.l; i++) {
                sum += sv_coef[i] * Kernel.k_function(x, model.SV[i], model.param);
            }
            sum -= model.rho[0];
            dec_values[0] = sum;
        } else {
            int i;
            int nr_class = model.nr_class;
            int l = model.l;
            double[] kvalue = new double[l];
            for (i = 0; i < l; i++) {
                kvalue[i] = Kernel.k_function(x, model.SV[i], model.param);
            }
            int[] start = new int[nr_class];
            start[0] = 0;
            for (i = 1; i < nr_class; i++) {
                start[i] = start[i - 1] + model.nSV[i - 1];
            }
            int p = 0;
            for (i = 0; i < nr_class; i++) {
                for (int j = i + 1; j < nr_class; j++) {
                    double sum = 0;
                    int si = start[i];
                    int sj = start[j];
                    int ci = model.nSV[i];
                    int cj = model.nSV[j];
                    int k;
                    double[] coef1 = model.sv_coef[j - 1];
                    double[] coef2 = model.sv_coef[i];
                    for (k = 0; k < ci; k++) {
                        sum += coef1[si + k] * kvalue[si + k];
                    }
                    for (k = 0; k < cj; k++) {
                        sum += coef2[sj + k] * kvalue[sj + k];
                    }
                    sum -= model.rho[p];
                    dec_values[p] = sum;
                    p++;
                }
            }
        }
    }

    private void multiclass_probability(final int k, final double[][] r, final double[] p) {
        int t, j;
        int iter = 0, max_iter = Math.max(100, k);
        double[][] Q = new double[k][k];
        double[] Qp = new double[k];
        double pQp, eps = 0.005 / k;
        for (t = 0; t < k; t++) {
            p[t] = 1.0 / k;
            Q[t][t] = 0;
            for (j = 0; j < t; j++) {
                Q[t][t] += r[j][t] * r[j][t];
                Q[t][j] = Q[j][t];
            }
            for (j = t + 1; j < k; j++) {
                Q[t][t] += r[j][t] * r[j][t];
                Q[t][j] = -r[j][t] * r[t][j];
            }
        }
        for (iter = 0; iter < max_iter; iter++) {
            pQp = 0;
            for (t = 0; t < k; t++) {
                Qp[t] = 0;
                for (j = 0; j < k; j++) {
                    Qp[t] += Q[t][j] * p[j];
                }
                pQp += p[t] * Qp[t];
            }
            double max_error = 0;
            for (t = 0; t < k; t++) {
                double error = Math.abs(Qp[t] - pQp);
                if (error > max_error) {
                    max_error = error;
                }
            }
            if (max_error < eps) {
                break;
            }
            for (t = 0; t < k; t++) {
                double diff = (-Qp[t] + pQp) / Q[t][t];
                p[t] += diff;
                pQp = (pQp + diff * (diff * Q[t][t] + 2 * Qp[t])) / (1 + diff) / (1 + diff);
                for (j = 0; j < k; j++) {
                    Qp[j] = (Qp[j] + diff * Q[t][j]) / (1 + diff);
                    p[j] /= (1 + diff);
                }
            }
        }
        if (iter >= max_iter) {
            logger.error("Exceeds max_iter in multiclass_prob");
        }
    }

    private void sigmoid_train(final int l, final double[] dec_values, final double[] labels, final double[] probAB) {
        double A, B;
        double prior1 = 0, prior0 = 0;
        int i;
        for (i = 0; i < l; i++) {
            if (labels[i] > 0) {
                prior1 += 1;
            } else {
                prior0 += 1;
            }
        }
        int max_iter = 100;
        double min_step = 1e-10;
        double sigma = 1e-12;
        double eps = 1e-5;
        double hiTarget = (prior1 + 1.0) / (prior1 + 2.0);
        double loTarget = 1 / (prior0 + 2.0);
        double[] t = new double[l];
        double fApB, p, q, h11, h22, h21, g1, g2, det, dA, dB, gd, stepsize;
        double newA, newB, newf, d1, d2;
        int iter;
        A = 0.0;
        B = Math.log((prior0 + 1.0) / (prior1 + 1.0));
        double fval = 0.0;
        for (i = 0; i < l; i++) {
            if (labels[i] > 0) {
                t[i] = hiTarget;
            } else {
                t[i] = loTarget;
            }
            fApB = dec_values[i] * A + B;
            if (fApB >= 0) {
                fval += t[i] * fApB + Math.log(1 + Math.exp(-fApB));
            } else {
                fval += (t[i] - 1) * fApB + Math.log(1 + Math.exp(fApB));
            }
        }
        for (iter = 0; iter < max_iter; iter++) {
            h11 = sigma;
            h22 = sigma;
            h21 = 0.0;
            g1 = 0.0;
            g2 = 0.0;
            for (i = 0; i < l; i++) {
                fApB = dec_values[i] * A + B;
                if (fApB >= 0) {
                    p = Math.exp(-fApB) / (1.0 + Math.exp(-fApB));
                    q = 1.0 / (1.0 + Math.exp(-fApB));
                } else {
                    p = 1.0 / (1.0 + Math.exp(fApB));
                    q = Math.exp(fApB) / (1.0 + Math.exp(fApB));
                }
                d2 = p * q;
                h11 += dec_values[i] * dec_values[i] * d2;
                h22 += d2;
                h21 += dec_values[i] * d2;
                d1 = t[i] - p;
                g1 += dec_values[i] * d1;
                g2 += d1;
            }
            if (Math.abs(g1) < eps && Math.abs(g2) < eps) {
                break;
            }
            det = h11 * h22 - h21 * h21;
            dA = -(h22 * g1 - h21 * g2) / det;
            dB = -(-h21 * g1 + h11 * g2) / det;
            gd = g1 * dA + g2 * dB;
            stepsize = 1;
            while (stepsize >= min_step) {
                newA = A + stepsize * dA;
                newB = B + stepsize * dB;
                newf = 0.0;
                for (i = 0; i < l; i++) {
                    fApB = dec_values[i] * newA + newB;
                    if (fApB >= 0) {
                        newf += t[i] * fApB + Math.log(1 + Math.exp(-fApB));
                    } else {
                        newf += (t[i] - 1) * fApB + Math.log(1 + Math.exp(fApB));
                    }
                }
                if (newf < fval + 0.0001 * stepsize * gd) {
                    A = newA;
                    B = newB;
                    fval = newf;
                    break;
                } else {
                    stepsize = stepsize / 2.0;
                }
            }
            if (stepsize < min_step) {
                logger.error("Line search fails in two-class probability estimates");
                break;
            }
        }
        if (iter >= max_iter) {
            logger.error("Reaching maximal iterations in two-class probability estimates");
        }
        probAB[0] = A;
        probAB[1] = B;
    }

    private void solve_c_svc(final SvmProblem prob, final SvmParameter param, final double[] alpha, final Solver.SolutionInfo si, final double Cp, final double Cn) {
        int l = prob.l;
        double[] minusOnes = new double[l];
        byte[] y = new byte[l];
        int i;
        for (i = 0; i < l; i++) {
            alpha[i] = 0;
            minusOnes[i] = -1;
            if (prob.y[i] > 0) {
                y[i] = +1;
            } else {
                y[i] = -1;
            }
        }
        Solver s = new Solver();
        s.Solve(l, new SVCKernel(prob, param, y), minusOnes, y, alpha, Cp, Cn, param.eps, si, param.shrinking);
        double sumAlpha = 0;
        for (i = 0; i < l; i++) {
            sumAlpha += alpha[i];
        }
        if (Cp == Cn) {
            logger.info("nu = " + sumAlpha / (Cp * prob.l));
        }
        for (i = 0; i < l; i++) {
            alpha[i] *= y[i];
        }
    }

    private void solve_epsilon_svr(final SvmProblem prob, final SvmParameter param, final double[] alpha, final Solver.SolutionInfo si) {
        int l = prob.l;
        double[] alpha2 = new double[2 * l];
        double[] linear_term = new double[2 * l];
        byte[] y = new byte[2 * l];
        int i;
        for (i = 0; i < l; i++) {
            alpha2[i] = 0;
            linear_term[i] = param.p - prob.y[i];
            y[i] = 1;
            alpha2[i + l] = 0;
            linear_term[i + l] = param.p + prob.y[i];
            y[i + l] = -1;
        }
        Solver s = new Solver();
        s.Solve(2 * l, new SVRKernel(prob, param), linear_term, y, alpha2, param.C, param.C, param.eps, si, param.shrinking);
        double sumAlpha = 0;
        for (i = 0; i < l; i++) {
            alpha[i] = alpha2[i] - alpha2[i + l];
            sumAlpha += Math.abs(alpha[i]);
        }
        logger.info("nu = " + sumAlpha / (param.C * l));
    }

    private void solve_nu_svr(final SvmProblem prob, final SvmParameter param, final double[] alpha, final Solver.SolutionInfo si) {
        int l = prob.l;
        double C = param.C;
        double[] alpha2 = new double[2 * l];
        double[] linear_term = new double[2 * l];
        byte[] y = new byte[2 * l];
        int i;
        double sum = C * param.nu * l / 2;
        for (i = 0; i < l; i++) {
            alpha2[i] = alpha2[i + l] = Math.min(sum, C);
            sum -= alpha2[i];
            linear_term[i] = -prob.y[i];
            y[i] = 1;
            linear_term[i + l] = prob.y[i];
            y[i + l] = -1;
        }
        NuSolver s = new NuSolver();
        s.Solve(2 * l, new SVRKernel(prob, param), linear_term, y, alpha2, C, C, param.eps, si, param.shrinking);
        logger.info("epsilon = " + (-si.r));
        for (i = 0; i < l; i++) {
            alpha[i] = alpha2[i] - alpha2[i + l];
        }
    }

    private void svm_binary_svc_probability(final SvmProblem prob, final SvmParameter param, final double Cp, final double Cn, final double[] probAB) {
        int i;
        int nr_fold = 5;
        int[] perm = new int[prob.l];
        double[] dec_values = new double[prob.l];
        for (i = 0; i < prob.l; i++) {
            perm[i] = i;
        }
        for (i = 0; i < prob.l; i++) {
            int j = i + (int) (Math.random() * (prob.l - i));
            do {
                int _ = perm[i];
                perm[i] = perm[j];
                perm[j] = _;
            } while (false);
        }
        for (i = 0; i < nr_fold; i++) {
            int begin = i * prob.l / nr_fold;
            int end = (i + 1) * prob.l / nr_fold;
            int j, k;
            SvmProblem subprob = new SvmProblem();
            subprob.l = prob.l - (end - begin);
            subprob.x = new SvmNode[subprob.l];
            subprob.y = new double[subprob.l];
            k = 0;
            for (j = 0; j < begin; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            for (j = end; j < prob.l; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            int p_count = 0, n_count = 0;
            for (j = 0; j < k; j++) {
                if (subprob.y[j] > 0) {
                    p_count++;
                } else {
                    n_count++;
                }
            }
            if (p_count == 0 && n_count == 0) {
                for (j = begin; j < end; j++) {
                    dec_values[perm[j]] = 0;
                }
            } else if (p_count > 0 && n_count == 0) {
                for (j = begin; j < end; j++) {
                    dec_values[perm[j]] = 1;
                }
            } else if (p_count == 0 && n_count > 0) {
                for (j = begin; j < end; j++) {
                    dec_values[perm[j]] = -1;
                }
            } else {
                SvmParameter subparam = (SvmParameter) param.clone();
                subparam.probability = 0;
                subparam.C = 1.0;
                subparam.nr_weight = 2;
                subparam.weight_label = new int[2];
                subparam.weight = new double[2];
                subparam.weight_label[0] = +1;
                subparam.weight_label[1] = -1;
                subparam.weight[0] = Cp;
                subparam.weight[1] = Cn;
                SvmModel submodel = svm_train(subprob, subparam);
                for (j = begin; j < end; j++) {
                    double[] dec_value = new double[1];
                    svm_predict_values(submodel, prob.x[perm[j]].getValue(), dec_value);
                    dec_values[perm[j]] = dec_value[0];
                    dec_values[perm[j]] *= submodel.label[0];
                }
            }
        }
        sigmoid_train(prob.l, dec_values, prob.y, probAB);
    }

    public void svm_cross_validation(final SvmProblem prob, final SvmParameter param, final int nr_fold, final double[] target) {
        int i;
        int[] foldStart = new int[nr_fold + 1];
        int l = prob.l;
        int[] perm = new int[l];
        if ((param.svm_type == SvmParameter.C_SVC || param.svm_type == SvmParameter.NU_SVC) && nr_fold < l) {
            int[] tmp_nr_class = new int[1];
            int[][] tmp_label = new int[1][];
            int[][] tmp_start = new int[1][];
            int[][] tmp_count = new int[1][];
            svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start, tmp_count, perm);
            int nrClass = tmp_nr_class[0];
            int[] start = tmp_start[0];
            int[] count = tmp_count[0];
            int[] foldCount = new int[nr_fold];
            int c;
            int[] index = new int[l];
            for (i = 0; i < l; i++) {
                index[i] = perm[i];
            }
            for (c = 0; c < nrClass; c++) {
                for (i = 0; i < count[c]; i++) {
                    int j = i + (int) (Math.random() * (count[c] - i));
                    do {
                        int other = index[start[c] + j];
                        index[start[c] + j] = index[start[c] + i];
                        index[start[c] + i] = other;
                    } while (false);
                }
            }
            for (i = 0; i < nr_fold; i++) {
                foldCount[i] = 0;
                for (c = 0; c < nrClass; c++) {
                    foldCount[i] += (i + 1) * count[c] / nr_fold - i * count[c] / nr_fold;
                }
            }
            foldStart[0] = 0;
            for (i = 1; i <= nr_fold; i++) {
                foldStart[i] = foldStart[i - 1] + foldCount[i - 1];
            }
            for (c = 0; c < nrClass; c++) {
                for (i = 0; i < nr_fold; i++) {
                    int begin = start[c] + i * count[c] / nr_fold;
                    int end = start[c] + (i + 1) * count[c] / nr_fold;
                    for (int j = begin; j < end; j++) {
                        perm[foldStart[i]] = index[j];
                        foldStart[i]++;
                    }
                }
            }
            foldStart[0] = 0;
            for (i = 1; i <= nr_fold; i++) {
                foldStart[i] = foldStart[i - 1] + foldCount[i - 1];
            }
        } else {
            for (i = 0; i < l; i++) {
                perm[i] = i;
            }
            for (i = 0; i < l; i++) {
                int j = i + (int) (Math.random() * (l - i));
                do {
                    int other = perm[i];
                    perm[i] = perm[j];
                    perm[j] = other;
                } while (false);
            }
            for (i = 0; i <= nr_fold; i++) {
                foldStart[i] = i * l / nr_fold;
            }
        }
        for (i = 0; i < nr_fold; i++) {
            int begin = foldStart[i];
            int end = foldStart[i + 1];
            int j, k;
            SvmProblem subprob = new SvmProblem();
            subprob.l = l - (end - begin);
            subprob.x = new SvmNode[subprob.l];
            subprob.y = new double[subprob.l];
            k = 0;
            for (j = 0; j < begin; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            for (j = end; j < l; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            SvmModel submodel = svm_train(subprob, param);
            if (param.probability == 1 && (param.svm_type == SvmParameter.C_SVC || param.svm_type == SvmParameter.NU_SVC)) {
                double[] probEstimates = new double[svm_get_nr_class(submodel)];
                for (j = begin; j < end; j++) {
                    target[perm[j]] = svm_predict_probability(submodel, prob.x[perm[j]].getValue(), probEstimates);
                }
            } else {
                for (j = begin; j < end; j++) {
                    target[perm[j]] = svm_predict(submodel, prob.x[perm[j]].getValue());
                }
            }
        }
    }

    public double svm_get_svr_probability(final SvmModel model) {
        if ((model.param.svm_type == SvmParameter.EPSILON_SVR || model.param.svm_type == SvmParameter.NU_SVR) && model.probA != null) {
            return model.probA[0];
        } else {
            logger.error("Model doesn't contain information for SVR probability inference");
            return 0;
        }
    }

    public double svm_predict_probability(final SvmModel model, final FloatVector x, final double[] prob_estimates) {
        if ((model.param.svm_type == SvmParameter.C_SVC || model.param.svm_type == SvmParameter.NU_SVC) && model.probA != null && model.probB != null) {
            int i;
            int nr_class = model.nr_class;
            double[] dec_values = new double[nr_class * (nr_class - 1) / 2];
            svm_predict_values(model, x, dec_values);
            double min_prob = 1e-7;
            double[][] pairwise_prob = new double[nr_class][nr_class];
            int k = 0;
            for (i = 0; i < nr_class; i++) {
                for (int j = i + 1; j < nr_class; j++) {
                    pairwise_prob[i][j] = Math.min(Math.max(sigmoid_predict(dec_values[k], model.probA[k], model.probB[k]), min_prob), 1 - min_prob);
                    pairwise_prob[j][i] = 1 - pairwise_prob[i][j];
                    k++;
                }
            }
            multiclass_probability(nr_class, pairwise_prob, prob_estimates);
            int prob_max_idx = 0;
            for (i = 1; i < nr_class; i++) {
                if (prob_estimates[i] > prob_estimates[prob_max_idx]) {
                    prob_max_idx = i;
                }
            }
            return model.label[prob_max_idx];
        } else {
            return svm_predict(model, x);
        }
    }

    private double svm_svr_probability(final SvmProblem prob, final SvmParameter param) {
        int i;
        int nr_fold = 5;
        double[] ymv = new double[prob.l];
        double mae = 0;
        SvmParameter newparam = (SvmParameter) param.clone();
        newparam.probability = 0;
        svm_cross_validation(prob, newparam, nr_fold, ymv);
        for (i = 0; i < prob.l; i++) {
            ymv[i] = prob.y[i] - ymv[i];
            mae += Math.abs(ymv[i]);
        }
        mae /= prob.l;
        double std = Math.sqrt(2 * mae * mae);
        int count = 0;
        mae = 0;
        for (i = 0; i < prob.l; i++) {
            if (Math.abs(ymv[i]) > 5 * std) {
                count = count + 1;
            } else {
                mae += Math.abs(ymv[i]);
            }
        }
        mae /= (prob.l - count);
        logger.info("Prob. model for test data: target value = predicted value + z");
        logger.info("z: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=" + mae);
        return mae;
    }

    public SvmModel svm_train(final SvmProblem prob, final SvmParameter param) {
        SvmModel model = new SvmModel();
        model.param = param;
        if (param.svm_type == SvmParameter.ONE_CLASS || param.svm_type == SvmParameter.EPSILON_SVR || param.svm_type == SvmParameter.NU_SVR) {
            model.nr_class = 2;
            model.label = null;
            model.nSV = null;
            model.probA = null;
            model.probB = null;
            model.sv_coef = new double[1][];
            if (param.probability == 1 && (param.svm_type == SvmParameter.EPSILON_SVR || param.svm_type == SvmParameter.NU_SVR)) {
                model.probA = new double[1];
                model.probA[0] = svm_svr_probability(prob, param);
            }
            decision_function f = svm_train_one(prob, param, 0, 0);
            model.rho = new double[1];
            model.rho[0] = f.rho;
            int nSV = 0;
            int i;
            for (i = 0; i < prob.l; i++) {
                if (Math.abs(f.alpha[i]) > 0) {
                    ++nSV;
                }
            }
            model.l = nSV;
            model.SV = new SvmNode[nSV];
            model.sv_coef[0] = new double[nSV];
            int j = 0;
            for (i = 0; i < prob.l; i++) {
                if (Math.abs(f.alpha[i]) > 0) {
                    model.SV[j] = prob.x[i];
                    model.sv_coef[0][j] = f.alpha[i];
                    ++j;
                }
            }
        } else {
            int l = prob.l;
            int[] tmp_nr_class = new int[1];
            int[][] tmp_label = new int[1][];
            int[][] tmp_start = new int[1][];
            int[][] tmp_count = new int[1][];
            int[] perm = new int[l];
            svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start, tmp_count, perm);
            int nr_class = tmp_nr_class[0];
            int[] label = tmp_label[0];
            int[] start = tmp_start[0];
            int[] count = tmp_count[0];
            SvmNode[] x = new SvmNode[l];
            int i;
            for (i = 0; i < l; i++) {
                x[i] = prob.x[perm[i]];
            }
            double[] weighted_C = new double[nr_class];
            for (i = 0; i < nr_class; i++) {
                weighted_C[i] = param.C;
            }
            for (i = 0; i < param.nr_weight; i++) {
                int j;
                for (j = 0; j < nr_class; j++) {
                    if (param.weight_label[i] == label[j]) {
                        break;
                    }
                }
                if (j == nr_class) {
                    logger.error("warning: class label " + param.weight_label[i] + " specified in weight is not found");
                } else {
                    weighted_C[j] *= param.weight[i];
                }
            }
            boolean[] nonzero = new boolean[l];
            for (i = 0; i < l; i++) {
                nonzero[i] = false;
            }
            decision_function[] f = new decision_function[nr_class * (nr_class - 1) / 2];
            double[] probA = null, probB = null;
            if (param.probability == 1) {
                probA = new double[nr_class * (nr_class - 1) / 2];
                probB = new double[nr_class * (nr_class - 1) / 2];
            }
            int p = 0;
            for (i = 0; i < nr_class; i++) {
                for (int j = i + 1; j < nr_class; j++) {
                    SvmProblem subProb = new SvmProblem();
                    int si = start[i], sj = start[j];
                    int ci = count[i], cj = count[j];
                    subProb.l = ci + cj;
                    subProb.x = new SvmNode[subProb.l];
                    subProb.y = new double[subProb.l];
                    subProb.kernel = prob.kernel;
                    int k;
                    for (k = 0; k < ci; k++) {
                        subProb.x[k] = x[si + k];
                        subProb.y[k] = +1;
                    }
                    for (k = 0; k < cj; k++) {
                        subProb.x[ci + k] = x[sj + k];
                        subProb.y[ci + k] = -1;
                    }
                    if (param.probability == 1) {
                        double[] probAB = new double[2];
                        svm_binary_svc_probability(subProb, param, weighted_C[i], weighted_C[j], probAB);
                        probA[p] = probAB[0];
                        probB[p] = probAB[1];
                    }
                    f[p] = svm_train_one(subProb, param, weighted_C[i], weighted_C[j]);
                    for (k = 0; k < ci; k++) {
                        if (!nonzero[si + k] && Math.abs(f[p].alpha[k]) > 0) {
                            nonzero[si + k] = true;
                        }
                    }
                    for (k = 0; k < cj; k++) {
                        if (!nonzero[sj + k] && Math.abs(f[p].alpha[ci + k]) > 0) {
                            nonzero[sj + k] = true;
                        }
                    }
                    ++p;
                }
            }
            model.nr_class = nr_class;
            model.label = new int[nr_class];
            for (i = 0; i < nr_class; i++) {
                model.label[i] = label[i];
            }
            model.rho = new double[nr_class * (nr_class - 1) / 2];
            for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) {
                model.rho[i] = f[i].rho;
            }
            if (param.probability == 1) {
                model.probA = new double[nr_class * (nr_class - 1) / 2];
                model.probB = new double[nr_class * (nr_class - 1) / 2];
                for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) {
                    model.probA[i] = probA[i];
                    model.probB[i] = probB[i];
                }
            } else {
                model.probA = null;
                model.probB = null;
            }
            int nnz = 0;
            int[] nzCount = new int[nr_class];
            model.nSV = new int[nr_class];
            for (i = 0; i < nr_class; i++) {
                int nSV = 0;
                for (int j = 0; j < count[i]; j++) {
                    if (nonzero[start[i] + j]) {
                        ++nSV;
                        ++nnz;
                    }
                }
                model.nSV[i] = nSV;
                nzCount[i] = nSV;
            }
            logger.info("Total nSV = " + nnz);
            model.l = nnz;
            model.SV = new SvmNode[nnz];
            p = 0;
            for (i = 0; i < l; i++) {
                if (nonzero[i]) {
                    model.SV[p] = x[i];
                    p++;
                }
            }
            int[] nzStart = new int[nr_class];
            nzStart[0] = 0;
            for (i = 1; i < nr_class; i++) {
                nzStart[i] = nzStart[i - 1] + nzCount[i - 1];
            }
            model.sv_coef = new double[nr_class - 1][];
            for (i = 0; i < nr_class - 1; i++) {
                model.sv_coef[i] = new double[nnz];
            }
            p = 0;
            for (i = 0; i < nr_class; i++) {
                for (int j = i + 1; j < nr_class; j++) {
                    int si = start[i];
                    int sj = start[j];
                    int ci = count[i];
                    int cj = count[j];
                    int q = nzStart[i];
                    int k;
                    for (k = 0; k < ci; k++) {
                        if (nonzero[si + k]) {
                            model.sv_coef[j - 1][q++] = f[p].alpha[k];
                        }
                    }
                    q = nzStart[j];
                    for (k = 0; k < cj; k++) {
                        if (nonzero[sj + k]) {
                            model.sv_coef[i][q++] = f[p].alpha[ci + k];
                        }
                    }
                    ++p;
                }
            }
        }
        return model;
    }

    decision_function svm_train_one(final SvmProblem prob, final SvmParameter param, final double Cp, final double Cn) {
        double[] alpha = new double[prob.l];
        Solver.SolutionInfo si = new Solver.SolutionInfo();
        switch(param.svm_type) {
            case SvmParameter.C_SVC:
                solve_c_svc(prob, param, alpha, si, Cp, Cn);
                break;
            case SvmParameter.NU_SVC:
                solveNuSvc(prob, param, alpha, si);
                break;
            case SvmParameter.ONE_CLASS:
                solveOneClass(prob, param, alpha, si);
                break;
            case SvmParameter.EPSILON_SVR:
                solve_epsilon_svr(prob, param, alpha, si);
                break;
            case SvmParameter.NU_SVR:
                solve_nu_svr(prob, param, alpha, si);
                break;
            default:
                throw new AssertionError();
        }
        logger.info("obj = " + si.obj + ", rho = " + si.rho);
        int nSV = 0;
        int nBSV = 0;
        for (int i = 0; i < prob.l; i++) {
            if (Math.abs(alpha[i]) > 0) {
                ++nSV;
                if (prob.y[i] > 0) {
                    if (Math.abs(alpha[i]) >= si.upper_bound_p) {
                        ++nBSV;
                    }
                } else {
                    if (Math.abs(alpha[i]) >= si.upper_bound_n) {
                        ++nBSV;
                    }
                }
            }
        }
        logger.info("nSV = " + nSV + ", nBSV = " + nBSV);
        decision_function f = new decision_function();
        f.alpha = alpha;
        f.rho = si.rho;
        return f;
    }
}
