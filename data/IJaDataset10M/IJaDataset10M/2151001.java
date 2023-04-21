package fracas.math;

public class Complex {

    public double real;

    public double imag;

    public double mod;

    public double arg;

    public static final int CARTESIAN = 0;

    public static final int POLAR = 1;

    public Complex(double var1, double var2, int type) {
        if (type == this.CARTESIAN) {
            real = var1;
            imag = var2;
            getPolar();
        } else {
            mod = var1;
            arg = var2;
            getCart();
        }
    }

    void getPolar() {
        mod = Math.sqrt((real * real) + (imag * imag));
        double stArg = Math.atan(Math.abs(imag) / Math.abs(real));
        if (real == 0) {
            if (imag == 0) {
                arg = 0;
            } else if (imag < 0) {
                arg = (3 * Math.PI) / 2;
            } else if (imag > 0) {
                arg = Math.PI / 2;
            }
        }
        if (real < 0) {
            if (imag > 0) {
                arg = Math.PI - stArg;
            } else if (imag < 0) {
                arg = Math.PI + stArg;
            } else if (imag == 0) {
                arg = Math.PI;
            }
        } else if (real > 0) {
            if (imag > 0) {
                arg = stArg;
            } else if (imag < 0) {
                arg = (2 * Math.PI) - stArg;
            } else if (imag == 0) {
                arg = 0;
            }
        }
    }

    void getCart() {
        real = mod * Math.cos(arg);
        imag = mod * Math.sin(arg);
    }

    public Complex add(Complex addComp) {
        double addImag = addComp.getImag();
        double addReal = addComp.getReal();
        double newImag = imag + addImag;
        double newReal = real + addReal;
        return new Complex(newReal, newImag, 0);
    }

    public Complex subtract(Complex subComp) {
        double subImag = subComp.getImag();
        double subReal = subComp.getReal();
        double newImag = imag - subImag;
        double newReal = real - subReal;
        return new Complex(newReal, newImag, 0);
    }

    public Complex multiply(Complex multComp) {
        double multMod = multComp.getMod();
        double multArg = multComp.getArg();
        double newMod = multMod * mod;
        double newArg = multArg + arg;
        return new Complex(newMod, newArg, 1);
    }

    public Complex divide(Complex divComp) {
        double divMod = divComp.getMod();
        double divArg = divComp.getArg();
        double newMod = mod / divMod;
        double newArg = arg - divArg;
        return new Complex(newMod, newArg, 1);
    }

    public Complex pow(int power) {
        if (power == 0) {
            return new Complex(1, 0, 0);
        }
        Complex cpx = this;
        Complex cpxret = this;
        for (int i = 1; i < power; i++) {
            cpxret = cpxret.multiply(cpx);
        }
        return cpxret;
    }

    public Complex pow(double power) {
        double newMod = Math.pow(mod, power);
        double newArg = arg * power;
        return new Complex(newMod, newArg, 1);
    }

    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }

    public double getMod() {
        return mod;
    }

    public double getArg() {
        if (mod == 0) {
            return 0;
        }
        return arg;
    }

    public void setImag(double newImag) {
        imag = newImag;
    }

    public void setReal(double newReal) {
        real = newReal;
    }

    public void setMod(double newMod) {
        mod = newMod;
    }

    public void setArg(double newArg) {
        arg = newArg;
    }

    public boolean equals(Object eqCompOb) {
        if (eqCompOb instanceof Complex) {
            Complex eqComp = (Complex) eqCompOb;
            return ((eqComp.getReal() == real) && (eqComp.getImag() == imag) && (eqComp.getMod() == mod) && (eqComp.getArg() == arg));
        } else {
            return false;
        }
    }

    public String toString() {
        return "" + real + " + i" + imag;
    }
}
