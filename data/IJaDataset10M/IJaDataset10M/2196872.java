package flanagan.control;

import flanagan.complex.Complex;
import flanagan.complex.ComplexPoly;

public class LowPassPassive extends BlackBox {

    private double resistance = 0.0D;

    private double capacitance = 0.0D;

    private double timeConstant = 0.0D;

    private boolean setR = false;

    private boolean setC = false;

    public LowPassPassive() {
        super("Passive Low Pass Filter");
        super.sPoles = Complex.oneDarray(1);
        super.setSnumer(new ComplexPoly(1.0D));
        super.setSdenom(new ComplexPoly(1.0D, 1.0D));
        super.setZtransformMethod(1);
        super.addDeadTimeExtras();
        this.timeConstant = 1.0D;
    }

    public void setResistance(double res) {
        this.resistance = res;
        this.timeConstant = res * this.capacitance;
        this.calcPolesZerosS();
        super.sDenom = ComplexPoly.rootsToPoly(this.sPoles);
        super.addDeadTimeExtras();
        this.setR = true;
    }

    public void setCapacitance(double cap) {
        this.capacitance = cap;
        this.timeConstant = cap * this.resistance;
        this.calcPolesZerosS();
        super.sDenom = ComplexPoly.rootsToPoly(this.sPoles);
        super.addDeadTimeExtras();
        this.setC = true;
    }

    public void setTimeConstant(double tau) {
        this.timeConstant = tau;
        this.calcPolesZerosS();
        super.sDenom = ComplexPoly.rootsToPoly(this.sPoles);
        super.addDeadTimeExtras();
    }

    public double getResistance() {
        if (this.setR) {
            return this.resistance;
        } else {
            System.out.println("Class; LowPassPassive, method: getResistance");
            System.out.println("No resistance has been entered; zero returned");
            return 0.0D;
        }
    }

    public double getCapacitance() {
        if (this.setC) {
            return this.capacitance;
        } else {
            System.out.println("Class; LowPassPassive, method: getCapacitance");
            System.out.println("No capacitance has been entered; zero returned");
            return 0.0D;
        }
    }

    public double getTimeConstant() {
        return this.timeConstant;
    }

    protected void calcPolesZerosS() {
        super.sPoles[0].setReal(-this.timeConstant);
    }

    public LowPassPassive copy() {
        if (this == null) {
            return null;
        } else {
            LowPassPassive bb = new LowPassPassive();
            bb.resistance = this.resistance;
            bb.capacitance = this.capacitance;
            bb.timeConstant = this.timeConstant;
            bb.setR = this.setR;
            bb.setC = this.setC;
            bb.sampLen = this.sampLen;
            bb.inputT = this.inputT.clone();
            bb.outputT = this.outputT.clone();
            bb.time = this.time.clone();
            bb.forgetFactor = this.forgetFactor;
            bb.deltaT = this.deltaT;
            bb.sampFreq = this.sampFreq;
            bb.inputS = this.inputS.copy();
            bb.outputS = this.outputS.copy();
            bb.sValue = this.sValue.copy();
            bb.zValue = this.zValue.copy();
            bb.sNumer = this.sNumer.copy();
            bb.sDenom = this.sDenom.copy();
            bb.zNumer = this.zNumer.copy();
            bb.zDenom = this.zDenom.copy();
            bb.sPoles = Complex.copy(this.sPoles);
            bb.sZeros = Complex.copy(this.sZeros);
            bb.zPoles = Complex.copy(this.zPoles);
            bb.zZeros = Complex.copy(this.zZeros);
            bb.sNumerDeg = this.sNumerDeg;
            bb.sDenomDeg = this.sDenomDeg;
            bb.zNumerDeg = this.zNumerDeg;
            bb.zDenomDeg = this.zDenomDeg;
            bb.deadTime = this.deadTime;
            bb.orderPade = this.orderPade;
            bb.sNumerPade = this.sNumerPade.copy();
            bb.sDenomPade = this.sDenomPade.copy();
            bb.sPolesPade = Complex.copy(this.sPolesPade);
            bb.sZerosPade = Complex.copy(this.sZerosPade);
            bb.sNumerDegPade = this.sNumerDegPade;
            bb.sDenomDegPade = this.sDenomDegPade;
            bb.maptozero = this.maptozero;
            bb.padeAdded = this.padeAdded;
            bb.integrationSum = this.integrationSum;
            bb.integMethod = this.integMethod;
            bb.ztransMethod = this.ztransMethod;
            bb.name = this.name;
            bb.fixedName = this.fixedName;
            bb.nPlotPoints = this.nPlotPoints;
            return bb;
        }
    }

    public Object clone() {
        Object ret = null;
        if (this != null) {
            LowPassPassive bb = new LowPassPassive();
            bb.resistance = this.resistance;
            bb.capacitance = this.capacitance;
            bb.timeConstant = this.timeConstant;
            bb.setR = this.setR;
            bb.setC = this.setC;
            bb.sampLen = this.sampLen;
            bb.inputT = this.inputT.clone();
            bb.outputT = this.outputT.clone();
            bb.time = this.time.clone();
            bb.forgetFactor = this.forgetFactor;
            bb.deltaT = this.deltaT;
            bb.sampFreq = this.sampFreq;
            bb.inputS = this.inputS.copy();
            bb.outputS = this.outputS.copy();
            bb.sValue = this.sValue.copy();
            bb.zValue = this.zValue.copy();
            bb.sNumer = this.sNumer.copy();
            bb.sDenom = this.sDenom.copy();
            bb.zNumer = this.zNumer.copy();
            bb.zDenom = this.zDenom.copy();
            bb.sPoles = Complex.copy(this.sPoles);
            bb.sZeros = Complex.copy(this.sZeros);
            bb.zPoles = Complex.copy(this.zPoles);
            bb.zZeros = Complex.copy(this.zZeros);
            bb.sNumerDeg = this.sNumerDeg;
            bb.sDenomDeg = this.sDenomDeg;
            bb.zNumerDeg = this.zNumerDeg;
            bb.zDenomDeg = this.zDenomDeg;
            bb.deadTime = this.deadTime;
            bb.orderPade = this.orderPade;
            bb.sNumerPade = this.sNumerPade.copy();
            bb.sDenomPade = this.sDenomPade.copy();
            bb.sPolesPade = Complex.copy(this.sPolesPade);
            bb.sZerosPade = Complex.copy(this.sZerosPade);
            bb.sNumerDegPade = this.sNumerDegPade;
            bb.sDenomDegPade = this.sDenomDegPade;
            bb.maptozero = this.maptozero;
            bb.padeAdded = this.padeAdded;
            bb.integrationSum = this.integrationSum;
            bb.integMethod = this.integMethod;
            bb.ztransMethod = this.ztransMethod;
            bb.name = this.name;
            bb.fixedName = this.fixedName;
            bb.nPlotPoints = this.nPlotPoints;
            ret = (Object) bb;
        }
        return ret;
    }
}
