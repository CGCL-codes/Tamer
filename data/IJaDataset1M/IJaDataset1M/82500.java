package kursach2;

import kursach2.barriers.AArchiBot;
import kursach2.barriers.ABarier;

public class BuildingUtils {

    public static double getCloseBorder(ABarier hum1, Struct B) {
        TPremises prem = hum1.getMyPrem();
        double times[] = new double[4];
        if (hum1.getVX() == 0) {
            times[0] = 1000000.0;
            times[1] = 1000000.0;
        } else {
            times[0] = (hum1.getX() - (Const.R + prem.x[0])) / hum1.getVX();
            times[1] = (prem.x[0] + prem.l - Const.R - hum1.getX()) / hum1.getVX();
        }
        if (hum1.getVY() == 0) {
            times[2] = 1000000.0;
            times[3] = 1000000.0;
        } else {
            times[2] = (hum1.getY() - (Const.R + prem.x[1])) / hum1.getVY();
            times[3] = (prem.x[1] + prem.b - Const.R - hum1.getY()) / hum1.getVY();
        }
        int i;
        for (i = 0; i < 4; i++) if (times[i] < Const.EPS) times[i] = 1000000.0;
        i = 0;
        i = (times[i] > times[1]) ? 1 : i;
        i = (times[i] > times[2]) ? 2 : i;
        i = (times[i] > times[3]) ? 3 : i;
        B.set(i, times[i]);
        return times[i];
    }

    public static double d(double x, double y, TPremises prem, int i) {
        switch(i) {
            case 0:
                return x - prem.x[0];
            case 1:
                return prem.x[4] - x;
            case 2:
                return y - prem.x[1];
            case 3:
                return prem.x[3] - y;
        }
        return -1;
    }

    public static double d(AArchiBot hum, int i) {
        return d(hum.getX(), hum.getY(), hum.getMyPrem(), i);
    }

    public static void minD(double x, double y, TPremises prem, Struct B) {
        double a, r;
        int i = 0;
        r = d(x, y, prem, 0);
        if (r > (a = d(x, y, prem, 1))) {
            i = 1;
            r = a;
        }
        if (r > (a = d(x, y, prem, 2))) {
            i = 2;
            r = a;
        }
        if (r > (a = d(x, y, prem, 3))) {
            i = 3;
            r = a;
        }
        B.set(i, r);
    }

    public static void minD(ABarier hum, Struct B) {
        minD(hum.getX(), hum.getY(), hum.getMyPrem(), B);
    }

    public static double minD(ABarier hum) {
        double r;
        TPremises prem = hum.getMyPrem();
        Struct B = new Struct(0, 0);
        minD(hum, B);
        if (prem.getPassage(B.getI()) == null) r = B.getD(); else {
            int k = (B.getI() < 2) ? 1 : 0;
            double hA = hum.getXk(k) - prem.getPassage(B.getI()).x[k], hB = hum.getXk(k) - prem.getPassage(B.getI()).x[k + 2];
            if (hA * hB < 0) {
                if (Math.abs(hA) < Math.abs(hB)) {
                    r = Math.sqrt(hA * hA + B.getD() * B.getD());
                } else {
                    r = Math.sqrt(B.getD() * B.getD() + hB * hB);
                }
            } else {
                r = B.getD();
            }
        }
        return r;
    }

    public static boolean backWall(int i, int j) {
        if ((i + j == 1) || (i + j == 5) || (i == j)) return true;
        return false;
    }

    public static double getCollisionWithPrem(ABarier hum1) {
        if (hum1.getLenV() < 0) return 100000;
        int i = 0;
        TPremises prem = hum1.getMyPrem();
        double g, f = pseudoSin(hum1.getVX(), hum1.getVY(), prem.x[i * 2], prem.x[i * 2 + 1]);
        while (f * (g = pseudoSin(hum1.getVX(), hum1.getVY(), prem.x[((i + 1) * 2) % 8], prem.x[((i + 1) * 2 + 1) % 8])) > 0) {
            i++;
            f = g;
        }
        i = cornerToWall(i);
        double t, v, dx = prem.getPassage(i).x[0] - hum1.getX(), dy = prem.getPassage(i).x[1] - hum1.getY(), h1 = prem.getPassage(i).x[2] - prem.getPassage(i).x[0], h2 = prem.getPassage(i).x[3] - prem.getPassage(i).x[1];
        if (h1 == 0) {
            t = dx / hum1.getVX();
            v = (hum1.getVY() * t - dy) / h2;
        } else {
            t = dy / hum1.getVY();
            v = (hum1.getVX() * t - dx) / h1;
        }
        g = Math.sqrt(h1 * h1 + h2 * h2);
        g = Const.R / g;
        if ((v > g) && (v < 1 - g)) {
            return 100000;
        }
        return t;
    }

    /**
	 * ��� ����� �����, ���� �������� �� ����� �������� (x1,y1), (x2,y2)
	 */
    private static double pseudoSin(double x1, double y1, double x2, double y2) {
        return x1 * y2 - x2 * y1;
    }

    public static int cornerToWall(int i) {
        switch(i) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 1;
            case 3:
                return 2;
        }
        return -1;
    }

    public static int wallToCorner(int i) {
        switch(i) {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 1;
        }
        return -1;
    }

    /**
	 * �������� �� ����� � ����� ����? 
	 */
    public static boolean moveIntoTarget(AArchiBot hum) {
        TPassage P = hum.getMyPrem().getPassage(hum.getNumberTarget());
        double[] A = new double[] { P.x[0], P.x[1] }, B = new double[] { P.x[2], P.x[3] };
        return moveInto(hum.X(), new double[] { hum.getVX(), hum.getVY() }, A, B);
    }

    private static boolean moveInto(double[] x, double[] v, double[] A, double[] B) {
        double n1, n2, n12, n21;
        double xh, yh;
        double a, b;
        xh = x[0];
        yh = x[1];
        double d;
        if (Math.abs(v[0]) < 0.000001) {
            d = Math.min(Math.abs(xh - A[0]), Math.abs(xh - B[0]));
        } else if (Math.abs(v[1]) < 0.000001) {
            d = Math.min(Math.abs(yh - A[1]), Math.abs(yh - B[1]));
        } else {
            n1 = v[0];
            n2 = v[1];
            n12 = n1 / n2;
            n21 = n2 / n1;
            b = 1 / (n12 + n21) * (xh - A[0] + n21 * yh + n12 * A[1]);
            a = B[0] - n21 * (yh - B[1]);
            d = Math.min(Math.sqrt((a - B[0]) * (a - B[0]) + (b - B[1]) * (b - B[1])), Math.sqrt((a - A[0]) * (a - A[0]) + (b - A[1]) * (b - A[1])));
        }
        if (d >= Const.R) return true;
        return false;
    }

    /**
	 * ���������� �� ��������� ��������� �� ������
	 * ! ���� ���������� ������ ���������� �� ����.
	 */
    public static double[] distancesToPassages(ABarier ih) {
        double[] len = new double[4];
        for (int i = 0; i < 4; i++) {
            len[i] = d(ih.getX(), ih.getY(), ih.getMyPrem(), i);
        }
        return len;
    }
}
