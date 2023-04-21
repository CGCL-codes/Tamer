package com.googlecode.cceb.chemistry;

/**
 * Exception thrown when an Formula is constructed that has bad syntax. Thrown
 * when no =, => or -> sign are provided, if compounds start with coefficients,
 * and if the sides do not match in composition
 * 
 * @author ajc
 * 
 */
public class InvalidFormulaException extends Exception {

    private final String equation;

    public InvalidFormulaException(String symbolSource) {
        this.equation = symbolSource;
    }

    public String getMessage() {
        return "Formula " + equation + " is not solvable";
    }
}
