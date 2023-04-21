package com.symbolicplotter.interpreter.types;

import com.symbolicplotter.interpreter.exceptions.EvaluationException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Interface for numbers, covering ints, rationals, reals and complex.
 *
 * Numbers are invariant types.
 */
public interface INumber extends IVariant {

    /**
     * Get the integer value of this number.
     *
     * If the type is not Int, throws an EvaluationException.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public BigInteger getInt() throws EvaluationException;

    /**
     * Get the numerator of this (rational) number.
     *
     * If the type is not Int or Rational, throws an EvaluationException.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public BigInteger getNumerator() throws EvaluationException;

    /**
     * Get the denominator of this (rational) number.
     *
     * If the type is not Int or Rational, throws an EvaluationException. For
     * Int, the denominator is 1.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public BigInteger getDenominator() throws EvaluationException;

    /**
     * Get the float value of this (real) number.
     *
     * If the type is Complex, throws an EvaluationException.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public BigDecimal getFloat() throws EvaluationException;

    /**
     * Get the real component of this number.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public INumber getReal() throws EvaluationException;

    /**
     * Get the number as a rational.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public VRational getRational() throws EvaluationException;

    /**
     * Get the imaginary component of this number.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public INumber getImaginary() throws EvaluationException;

    /**
     * Get the float value of this number as a double.
     *
     * @return
     * @throws com.symbolicplotter.interpreter.exceptions.EvaluationException
     */
    public double getDouble() throws EvaluationException;
}
