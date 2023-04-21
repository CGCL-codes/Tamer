package jp.co.lattice.vProcessor.node;

/**
 * @author	  created by Eishin Matsui (00/06/29-)
 */
public class x3pFieldVec3f extends x3pField {

    public double x;

    public double y;

    public double z;

    public x3pFieldVec3f() {
    }

    public x3pFieldVec3f(double px, double py, double pz) {
        x = px;
        y = py;
        z = pz;
    }
}
