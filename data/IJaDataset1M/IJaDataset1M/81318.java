package models.trianglecases;

import models.TriangleException;
import models.Triangle.Angle;
import models.Triangle.Side;
import utils.TriangleUtils;

public class SSSCase extends TriangleCase {

    public SSSCase() {
    }

    public SSSCase(double sideA, double sideB, double sideC) {
        super();
        _triangle.set(Side.a, sideA);
        _triangle.set(Side.b, sideB);
        _triangle.set(Side.c, sideC);
    }

    public void setCharacteristic1(double value) {
        _triangle.set(Side.a, value);
    }

    public void setCharacteristic2(double value) {
        _triangle.set(Side.b, value);
    }

    public void setCharacteristic3(double value) {
        _triangle.set(Side.c, value);
    }

    public void calculateSidesAndAngles() throws TriangleException {
        double a, b, c;
        double A, B, C;
        a = _triangle.get(Side.a);
        b = _triangle.get(Side.b);
        c = _triangle.get(Side.c);
        if (!TriangleUtils.isValid(_triangle)) {
            throw new TriangleException("Triangle not valid");
        }
        A = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2 * b * c)));
        _triangle.set(Angle.A, A);
        B = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
        _triangle.set(Angle.B, B);
        C = 180 - A - B;
        _triangle.set(Angle.C, C);
    }
}
