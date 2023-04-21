package emgeno.music;

import emgeno.core.GetSampleException;
import emgeno.core.ISoundSource;

public class Silence implements ISoundSource {

    double lenght = 0;

    public double getLength() {
        return this.lenght;
    }

    public double[] getSample(double ms) throws GetSampleException {
        double[] d = { 0d, 0d };
        return d;
    }

    public Silence(double length) {
        this.lenght = length;
    }
}
