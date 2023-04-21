package pl.edu.wat.wcy.jit.model;

import pl.edu.wat.wcy.jit.engine.random.SimGenerator;

public class Generator {

    public static int TRIANGULAR = 0;

    public static int NORMAL = 1;

    public static int UNIFORM = 2;

    public static int EXPONENTIAL = 3;

    public static int ERLANG = 4;

    public static int LAPLACE = 5;

    public static int CHISQUARE = 6;

    public static int STUDENT = 7;

    public static int WEIBULL = 8;

    public static int POISSON = 9;

    int type;

    SimGenerator generator;

    public Generator(int type) {
        this.type = type;
        generator = new SimGenerator();
    }

    public double generate(double param1, double param2) {
        switch(type) {
            case 0:
                return generator.triangular(param1);
            case 1:
                return generator.normal(param1, param2);
            case 2:
                return generator.uniform(param1, param2);
            case 3:
                return generator.exponential(param1);
            case 4:
                return generator.erlang((int) param1, param2);
            case 5:
                return generator.laplace(param1);
            case 6:
                return generator.chisquare((int) param1);
            case 7:
                return generator.student((int) param1);
            case 8:
                return generator.weibull(param1, param2);
            case 9:
                return generator.poisson(param1);
            default:
                return generator.normal(param1, param2);
        }
    }
}
