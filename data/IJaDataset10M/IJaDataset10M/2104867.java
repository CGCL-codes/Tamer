package curso.ex8;

import java.io.Serializable;

public class MathServiceImpl implements MathService {

    public double potencia(double a, double b) {
        return Math.pow(a, b);
    }
}
