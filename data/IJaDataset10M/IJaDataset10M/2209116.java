package net.sf.afluentes.math;

class SquareProcessor extends UnaryOperatorProcessor {

    @Override
    double processVertex(double x) {
        return Math.pow(x, 2.0);
    }
}
