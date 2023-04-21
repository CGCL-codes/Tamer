package org.ejml.data;

import java.io.Serializable;

/**
 * <p>
 * Represents a complex number using 64bit floating point numbers.  A complex number is composed of
 * a real and imaginary components.
 * </p>
 */
public class Complex64F implements Serializable {

    public double real;

    public double imaginary;

    public Complex64F(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex64F() {
    }

    public double getReal() {
        return real;
    }

    public double getMagnitude() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double getMagnitude2() {
        return real * real + imaginary * imaginary;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public void set(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public boolean isReal() {
        return imaginary == 0.0;
    }

    public String toString() {
        if (imaginary == 0) {
            return "" + real;
        } else {
            return real + " " + imaginary + "i";
        }
    }
}
