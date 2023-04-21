package data.generators;

import java.util.ArrayList;

/**
 * Contains methods for generation of transitions time when it works by Uniform
 * low (Ravnomernyi).
 * 
 * @author <a href="mailto:sukharevd@gmail.com">Sukharev Dmitriy</a>
 * 
 */
public class UniformGenerator implements Generator {

    private Generator lcgGen;

    private double lyambda;

    private double b;

    public UniformGenerator() {
        this.lyambda = 2.0;
        this.lcgGen = new LCGenerator();
        this.b = 0.0;
    }

    public UniformGenerator(double lyambda) {
        this.lyambda = lyambda;
        this.lcgGen = new LCGenerator();
        this.b = 0.0;
    }

    public UniformGenerator(double lyambda, LCGenerator lcgGen) {
        this.lyambda = lyambda;
        this.lcgGen = lcgGen;
        this.b = 0.0;
    }

    @Override
    public ArrayList<Double> generateList(int quantity) {
        ArrayList<Double> list = new ArrayList<Double>();
        double last;
        for (int i = 0; i < quantity; i++) {
            last = (1.0 / lyambda) * lcgGen.generateValue() * 2;
            list.add(last);
            if (last > b) {
                b = last;
            }
        }
        return list;
    }

    @Override
    public Double generateValue() {
        double res = (1.0 / lyambda) * lcgGen.generateValue() * 2;
        if (res > b) {
            b = res;
        }
        return res;
    }

    public double getB() {
        return b;
    }
}
