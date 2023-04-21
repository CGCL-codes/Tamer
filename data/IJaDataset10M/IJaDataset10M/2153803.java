package jsbsim.math;

import jsbsim.simgear.math.SGGeoc;
import jsbsim.simgear.math.SGGeod;
import jsbsim.simgear.math.SGGeodesy;

/** This class implements a 3 element column vector.
@author Jon S. Berndt, Tony Peden, et. al.
@version $Id: FGColumnVector3.h,v 1.9 2008/07/22 02:42:17 jberndt Exp $
 */
public class FGColumnVector3 {

    /**
     * vector components
     */
    private double data[] = { 0.0d, 0.0d, 0.0d };

    /** Initialization by given values.
    @param X value of the x-conponent.
    @param Y value of the y-conponent.
    @param Z value of the z-conponent.
    Create a vector from the doubles given in the arguments.   */
    public FGColumnVector3(double X, double Y, double Z) {
        data[0] = X;
        data[1] = Y;
        data[2] = Z;
    }

    /** Default initializer.
    Create a zero vector.   */
    public FGColumnVector3() {
    }

    /** Copy constructor.
    @param v Vector which is used for initialization.
    Create copy of the vector given in the argument.   */
    public FGColumnVector3(final FGColumnVector3 v) {
        data[0] = v.data[0];
        data[1] = v.data[1];
        data[2] = v.data[2];
    }

    /**
     * Read access the entries of the vector.
     * @param idx the component index.
     * Return the value of the matrix entry at the given index.
     * Indices are counted starting with 1.
     * Note that the index given in the argument is unchecked.   */
    public final double GetEntry(int idx) {
        return this.data[--idx];
    }

    /**
     * Write access the entries of the vector.
     * @param idx the component index.
     * Return the value of the matrix entry at the given index.
     * Indices are counted starting with 1.
     * Note that the index given in the argument is unchecked.   */
    public final void SetEntry(int idx, double value) {
        this.data[--idx] = value;
    }

    public final void MulEntry(int idx, double scalar) {
        this.data[--idx] *= scalar;
    }

    /**
     * increments given vector component with scalar value
     * @param idx id of the vector component
     * @param scalar value to increment with
     */
    public final void IncEntry(int idx, double scalar) {
        this.data[--idx] += scalar;
    }

    /** Prints the contents of the vector
    @param delimeter the item separator (tab or comma)
    @return a string with the delimeter-separated contents of the vector  */
    public String Dump(String delimeter) {
        return String.format("%10.3e%s%10.3e%s%10.3e", data[0], delimeter, data[1], delimeter, data[2]);
    }

    /**
     * bool operator==(const FGColumnVector3& b) const
     * Comparison operator.
     * @param b other vector.
     * Returns true if both vectors are exactly the same.   */
    public final boolean _oprEquals(final FGColumnVector3 b) {
        return data[0] == b.data[0] && data[1] == b.data[1] && data[2] == b.data[2];
    }

    /**
     * bool operator!=(const FGColumnVector3& b)
     * Comparison operator.
     * @param b other vector.
     * Returns false if both vectors are exactly the same.   */
    public final boolean _oprENquals(final FGColumnVector3 b) {
        return data[0] != b.data[0] || data[1] != b.data[1] || data[2] != b.data[2];
    }

    /**
     * FGColumnVector3 operator*(const double scalar) const
     * Multiplication by a scalar.
     * @param scalar scalar value to multiply the vector with.
     * @return The resulting vector from the multiplication with that scalar.
     * Multiply the vector with the scalar given in the argument.   */
    public final FGColumnVector3 _oprMulScalarOut(final double scalar) {
        return new FGColumnVector3(scalar * data[0], scalar * data[1], scalar * data[2]);
    }

    /**
     * inline FGColumnVector3 operator*(double scalar, const FGColumnVector3& A)
     * Scalar multiplication.
     * @param scalar scalar value to multiply with.
     * @param A Vector to multiply.
     * Multiply the Vector with a scalar value.*/
    public final FGColumnVector3 _oprMulScalarOutOther(double scalar, final FGColumnVector3 A) {
        return A._oprMulScalarOut(scalar);
    }

    /**
     * FGColumnVector3 operator*(const FGColumnVector3& V) const
     * Cross product multiplication.
     * @param V vector to multiply with.
     * @return The resulting vector from the cross product multiplication.
     * Compute and return the cross product of the current vector with
     * the given argument.   */
    public final FGColumnVector3 _oprMulVecOut(final FGColumnVector3 V) {
        return new FGColumnVector3(this.data[1] * V.data[2] - this.data[2] * V.data[1], this.data[2] * V.data[0] - this.data[0] * V.data[2], this.data[0] * V.data[1] - this.data[1] * V.data[0]);
    }

    /**
     * produces vector scalar product for 2 given vectors
     * @param v1
     * @param v2
     * @return Scalar dot product
     */
    public static final double dot(final FGColumnVector3 v1, final FGColumnVector3 v2) {
        return v1.data[0] * v2.data[0] + v1.data[1] * v2.data[1] + v1.data[2] * v2.data[2];
    }

    /**
     * The euclidean norm of the vector, that is what most people call length
     * @param v
     * @return
     */
    public static final double norm(final FGColumnVector3 v) {
        return Math.sqrt(dot(v, v));
    }

    /**
     * FGColumnVector3& operator*=(const double scalar)
     * Scale by a scalar.
     */
    public final void _oprMulScalarIn(final double scalar) {
        data[0] *= scalar;
        data[1] *= scalar;
        data[2] *= scalar;
    }

    /**
     * FGColumnVector3 operator/(const double scalar) const
     * Multiply by 1/scalar.
     * @param scalar scalar value to devide the vector through.
     * @return The resulting vector from the division through that scalar.
     * Multiply the vector with the 1/scalar given in the argument.   */
    public final FGColumnVector3 _oprDivOut(final double scalar) {
        return (0.0d != scalar) ? _oprMulScalarOut(1.0 / scalar) : new FGColumnVector3();
    }

    /**
     * FGColumnVector3& operator/=(const double scalar);
     * Scale by a 1/scalar
     */
    public final void _oprDivIn(final double scalar) {
        if (0.0d != scalar) {
            _oprMulScalarIn(1.0d / scalar);
        }
    }

    /**
     * FGColumnVector3 operator+(const FGColumnVector3& B) const
     * Addition operator.
     */
    public final FGColumnVector3 _oprAddOut(final FGColumnVector3 B) {
        return new FGColumnVector3(data[0] + B.data[0], data[1] + B.data[1], data[2] + B.data[2]);
    }

    /**
     * FGColumnVector3& operator+=(const FGColumnVector3 &B)
     * Add an other vector.
     */
    public final void _oprAddIn(final FGColumnVector3 B) {
        data[0] += B.data[0];
        data[1] += B.data[1];
        data[2] += B.data[2];
    }

    public final boolean isNullVector() {
        return (0.0d == (data[0] + data[1] + data[2]));
    }

    /**
     * FGColumnVector3 operator-(const FGColumnVector3& B) const
     * Subtraction operator.
     */
    public final FGColumnVector3 _oprSubtrOut(final FGColumnVector3 B) {
        return new FGColumnVector3(data[0] - B.data[0], data[1] - B.data[1], data[2] - B.data[2]);
    }

    /**
     * FGColumnVector3& operator-=(const FGColumnVector3 &B)
     * Subtract an other vector.
     */
    public final void _oprSubtrIn(final FGColumnVector3 B) {
        data[0] -= B.data[0];
        data[1] -= B.data[1];
        data[2] -= B.data[2];
    }

    public void InitMatrix() {
        data[0] = data[1] = data[2] = 0.0;
    }

    public void InitMatrix(double a) {
        data[0] = data[1] = data[2] = a;
    }

    public void InitMatrix(double a, double b, double c) {
        data[0] = a;
        data[1] = b;
        data[2] = c;
    }

    public final void InitMatrix(FGColumnVector3 value) {
        data[0] = value.data[0];
        data[1] = value.data[1];
        data[2] = value.data[2];
    }

    /** Length of the vector.
    Compute and return the euclidean norm of this vector.   */
    public final double Magnitude() {
        if (this.isNullVector()) {
            return 0.0d;
        }
        return Math.sqrt(data[0] * data[0] + data[1] * data[1] + data[2] * data[2]);
    }

    /** Length of the vector in a coordinate axis plane.
    Compute and return the euclidean norm of this vector projected into
    the coordinate axis plane idx1-idx2.   */
    public final double Magnitude(int idx1, int idx2) {
        return Math.sqrt(GetEntry(idx1) * GetEntry(idx1) + GetEntry(idx2) * GetEntry(idx2));
    }

    /** Normalize.
    Normalize the vector to have the Magnitude() == 1.0. If the vector
    is equal to zero it is left untouched.   */
    public final FGColumnVector3 Normalize() {
        double Mag = Magnitude();
        if (0.0d != Mag) {
            this._oprDivIn(Mag);
        }
        return this;
    }

    /**
     * ostream& operator<<(ostream& os, const FGColumnVector3& col);
     * Write vector to a stream.
     * @param os Stream to write to.
     * @param M Matrix to write.
     * Write the matrix to a stream.*/
    public final String _oprShl(FGColumnVector3 col) {
        return String.format("%10.3e, %10.3e, %10.3e", col.data[0], col.data[1], col.data[2]);
    }

    @Override
    public FGColumnVector3 clone() {
        return new FGColumnVector3(this);
    }

    public String getInfo(String separator) {
        return String.format("%.2f%s%.2f%s%.2f", data[0], separator, data[1], separator, data[2]);
    }

    public static final FGColumnVector3 fromGeod(final SGGeod geod) {
        FGColumnVector3 cart = new FGColumnVector3();
        SGGeodesy.SGGeodToCart(geod, cart);
        return cart;
    }

    public static FGColumnVector3 fromGeoc(final SGGeoc geoc) {
        FGColumnVector3 cart = new FGColumnVector3();
        SGGeodesy.SGGeocToCart(geoc, cart);
        return cart;
    }

    @Override
    public String toString() {
        return getInfo(", ");
    }
}
