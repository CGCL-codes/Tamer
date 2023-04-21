package com.manning.junitbook.ch16.service;

/**
 * Calculator service interface.
 * 
 * @version $Id: CalculatorService.java 538 2009-08-17 09:08:14Z paranoid12 $
 */
public interface CalculatorService {

    public double[] parseUserInput(String str) throws NumberFormatException;

    public double add(double... numbers);

    public double multiply(double... numbers);

    public void printResult(double result);
}
