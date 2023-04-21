package optimization;

import optimization.LinearAlgebra;
import theory.vanpelt.bifurcation.PDistEstimate;

public class QuasiNewtonSolver extends LinearAlgebra {

    /**
	 * Backtracking line search: Find value of alpha K that satisfies f(x+alphak*d) < f(x),
	 * with some conditions (Wolfe 1)
	 * 
	 * @param f function
	 * @param jacob jacobian matrix of f at x
	 * @param x initial location
	 * @param d direction of search
	 * @param sigma threshold value, e.g. 0.0001
	 * @param rho backtracking value in (0,1), e.g. 0.9
	 * @return new x
	 */
    public double[] lineSearchBacktracking(Function f, double[] jacob, double[] x, double[] d, double sigma, double rho) {
        double alphak = 1;
        double fx = f.evaluate(x);
        double[] nx = vAdd(x, vMul(alphak, d));
        double fx1 = f.evaluate(nx);
        double test = sigma * alphak * vMul(jacob, d);
        int iter = 1;
        while (fx1 > fx + sigma * alphak * vMul(jacob, d)) {
            alphak = alphak * rho;
            nx = vAdd(x, vMul(alphak, d));
            fx1 = f.evaluate(nx);
            test = sigma * alphak * vMul(jacob, d);
            iter++;
        }
        return nx;
    }

    /**
	 * 
	 * Quasi-newton search, using backtracking line search and BGFS update for Hessian. 
	 * @param f Function to be minimized
	 * @param x0 initial position
	 * @return minimal position
	 */
    public double[] quasiNewton(Function f, double[] x0) {
        double[] I = new double[] { 1, 0, 0, 1 };
        double[] H = I;
        double[] x = x0;
        System.out.println("Q-N START, X0 = " + vPrint(x0));
        int maxIter = 50;
        while (maxIter-- > 0) {
            double fx = f.evaluate(x);
            double[] grad_x = gradientForwardDifferences(f, x, new double[] { 0.0001, 0.0001 });
            double[] dir = vMul(-1, mvMul(H, grad_x));
            PDistEstimate est = new PDistEstimate(x[0], x[1] / 10);
            System.out.format("%.6f %.6f %.6f %.6f %.6f %.6f %.6f ", x[0], x[1] / 10, f.evaluate(x), est.getMeanEst(), est.getDevEst(), grad_x[0], grad_x[1]);
            double norm_grad = vNorm2(grad_x);
            if (norm_grad < 0.1) {
                System.out.println("||J|| = " + norm_grad);
                System.out.println("EXIT");
                return x;
            }
            if (fx < 0.001) {
                System.out.println("BREAK y < 0.001");
                return x;
            }
            double[] nx = lineSearchBacktracking(f, grad_x, x, dir, 0.0001, 0.9);
            if (nx[0] < 0) nx[0] = 0;
            if (nx[1] < 0) nx[1] = 0;
            double[] grad_nx = gradientForwardDifferences(f, nx, new double[] { 0.0001, 0.0001 });
            double[] s = vSub(nx, x);
            double[] y = vSub(grad_nx, grad_x);
            double rho = 1 / vMul(y, s);
            double[] A = vMul(rho, vOuterMul(s, y));
            double[] B = vMul(rho, vOuterMul(y, s));
            double[] C = vMul(rho, vOuterMul(s, s));
            double[] Hn = vAdd(mMul(mMul(vSub(I, A), H), vSub(I, B)), C);
            H = Hn;
            x = nx;
            grad_x = grad_nx;
        }
        return x;
    }
}
