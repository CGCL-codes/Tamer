package ru.jnano.math.fuzzyset.value;

import java.util.TreeMap;
import ru.jnano.math.IFunctionOne;
import ru.jnano.math.fun.FunLinear;

public class FuzzyIntervalLR extends ValueFuzzySet {

    private double modaLow;

    private double modaHigh;

    private double alpha;

    private double betta;

    private IFunctionOne fun;

    public FuzzyIntervalLR(double modaLow, double modaHigh, double alpha, double betta, IFunctionOne fun) {
        super(modaLow - alpha, modaHigh + betta, 100);
        this.modaLow = modaLow;
        this.modaHigh = modaHigh;
        this.alpha = alpha;
        this.betta = betta;
        this.fun = fun;
    }

    @Override
    public double membershipFunction(Double x) {
        if (x < modaLow) return fun.function((modaLow - x) / alpha);
        if ((x >= modaLow) && (x <= modaHigh)) return 1;
        return fun.function((x - modaHigh) / betta);
    }

    public double getModaLow() {
        return modaLow;
    }

    public double getModaHigh() {
        return modaHigh;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBetta() {
        return betta;
    }

    public IFunctionOne getFun() {
        return fun;
    }

    public FuzzyIntervalLR summ(FuzzyIntervalLR a) {
        return new FuzzyIntervalLR(modaLow + a.modaLow, modaHigh + a.modaHigh, alpha + a.alpha, betta + a.betta, fun);
    }

    public FuzzyIntervalLR dif(FuzzyIntervalLR a) {
        return new FuzzyIntervalLR(modaLow - a.modaLow, modaHigh - a.modaHigh, alpha + a.betta, betta + a.alpha, fun);
    }

    public FuzzyIntervalLR mul(FuzzyIntervalLR a) {
        return null;
    }

    public FuzzyIntervalLR div(FuzzyIntervalLR a) {
        return null;
    }

    @Override
    public TreeMap<Double, Double> toDiscret() {
        if (fun instanceof FunLinear) {
            TreeMap<Double, Double> rez = new TreeMap<Double, Double>();
            rez.put(modaLow - alpha, 0.0);
            rez.put(modaLow, 1.0);
            rez.put(modaHigh + betta, 0.0);
            rez.put(modaHigh, 1.0);
            return rez;
        }
        return super.toDiscret();
    }

    public static FuzzyIntervalLR getTrapezium(double modaLow, double modaHigh, double alpha, double betta) {
        return new FuzzyIntervalLR(modaLow, modaHigh, alpha, betta, new FunLinear());
    }
}
