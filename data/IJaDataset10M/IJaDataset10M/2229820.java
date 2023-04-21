package org.jmlspecs.samples.dbc;

/** An abstract class that holds all of the common algorithms for
 *  complex numbers.  Note that this class knows about both of its subclasses
 *  Rectangular and Polar.
 *
 *  @author Gary T. Leavens with help from Abelson and Sussman's
 *          <cite>Structure and Interpretation of Computer Programs</cite>
 */
public abstract strictfp class ComplexOps implements Complex {

    public Complex add(Complex b) {
        return new Rectangular(this.realPart() + b.realPart(), this.imaginaryPart() + b.imaginaryPart());
    }

    public Complex sub(Complex b) {
        return new Rectangular(this.realPart() - b.realPart(), this.imaginaryPart() - b.imaginaryPart());
    }

    public Complex mul(Complex b) {
        try {
            return new Polar(this.magnitude() * b.magnitude(), this.angle() + b.angle());
        } catch (IllegalArgumentException e) {
            return new Rectangular(Double.NaN);
        }
    }

    public Complex div(Complex b) {
        try {
            return new Polar(this.magnitude() / b.magnitude(), this.angle() - b.angle());
        } catch (IllegalArgumentException e) {
            return new Rectangular(Double.NaN);
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof Complex)) {
            return false;
        }
        Complex b = (Complex) o;
        return this.realPart() == b.realPart() && this.imaginaryPart() == b.imaginaryPart();
    }

    public int hashCode() {
        return (int) (this.realPart() + this.imaginaryPart());
    }
}
