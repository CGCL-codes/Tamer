package org.luaj.platform;

import javax.microedition.midlet.MIDlet;
import org.luaj.lib.MathLib;
import org.luaj.vm.LDouble;
import org.luaj.vm.LNumber;

public class J2meMidp20Cldc11Platform extends J2meMidp10Cldc10Platform {

    public J2meMidp20Cldc11Platform(MIDlet midlet) {
        super(midlet);
    }

    public LNumber mathPow(LNumber base, LNumber exp) {
        return LDouble.numberOf(dpow(base.toJavaDouble(), exp.toJavaDouble()));
    }

    public LNumber mathop(int id, LNumber la, LNumber lb) {
        double a = la.toJavaDouble();
        double b = lb.toJavaDouble();
        double z = 0;
        switch(id) {
            default:
                return unsupportedMathOp();
            case MathLib.FMOD:
                z = a - (b * ((int) (a / b)));
                break;
            case MathLib.POW:
                z = dpow(a, b);
                break;
        }
        return LDouble.numberOf(z);
    }

    public LNumber mathop(int id, LNumber lx) {
        double x = lx.toJavaDouble();
        double z = 0;
        switch(id) {
            default:
                return unsupportedMathOp();
            case MathLib.ABS:
                z = Math.abs(x);
                break;
            case MathLib.COS:
                z = Math.cos(x);
                break;
            case MathLib.DEG:
                z = Math.toDegrees(x);
                break;
            case MathLib.RAD:
                z = Math.toRadians(x);
                break;
            case MathLib.SIN:
                z = Math.sin(x);
                break;
            case MathLib.SQRT:
                z = Math.sqrt(x);
                break;
            case MathLib.TAN:
                z = Math.tan(x);
                break;
        }
        return LDouble.numberOf(z);
    }

    public static double dpow(double a, double b) {
        if (b < 0) return 1 / dpow(a, -b);
        double p = 1;
        int whole = (int) b;
        for (double v = a; whole > 0; whole >>= 1, v *= v) if ((whole & 1) != 0) p *= v;
        if ((b -= whole) > 0) {
            int frac = (int) (0x10000 * b);
            for (; (frac & 0xffff) != 0; frac <<= 1) {
                a = Math.sqrt(a);
                if ((frac & 0x8000) != 0) p *= a;
            }
        }
        return p;
    }
}
