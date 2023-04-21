package com.thoughtworks.bytecode2class.constantpool;

import com.thoughtworks.bytecode2class.ClassReader;

public class DoubleConstant extends Constant {

    private static final int length = 64;

    private static final int exponentialLength = 11;

    private static final int exponentMaxValue = (int) Math.pow(2, exponentialLength - 1);

    private static final int mantissaLength = length - 1 - exponentialLength;

    private static final int bias = (int) (Math.pow(2, exponentialLength - 1) - 1);

    private static final long mask = 1L << (length - 1);

    private double value;

    public DoubleConstant(ClassReader classReader) {
        super(classReader);
        this.value = parseValue(classReader.forwardDoubleWord());
    }

    private double parseValue(long bytes) {
        int exponential;
        double mantissa;
        int symbol = Integer.MIN_VALUE;
        StringBuilder exponentialBuilder = new StringBuilder(exponentialLength);
        StringBuilder mantissaBuilder = new StringBuilder("1");
        for (int i = 0; i < length; i++) {
            boolean maskIsZero = (mask & bytes) == 0;
            if (i == 0) {
                symbol = maskIsZero ? 1 : -1;
            }
            if (i > 0 && i <= exponentialLength) {
                exponentialBuilder.append(maskIsZero ? 0 : 1);
            }
            if (i > exponentialLength && i < length) {
                mantissaBuilder.append(maskIsZero ? 0 : 1);
            }
            bytes = bytes << 1;
        }
        exponential = Integer.parseInt(exponentialBuilder.toString(), 2) - bias;
        mantissa = Long.parseLong(mantissaBuilder.toString(), 2) / Math.pow(2, mantissaLength);
        if (exponential == exponentMaxValue) {
            if (mantissaBuilder.toString().startsWith("10")) {
                if (symbol == 1) {
                    return Double.POSITIVE_INFINITY;
                } else {
                    return Double.NEGATIVE_INFINITY;
                }
            } else if (mantissaBuilder.toString().startsWith("11")) {
                return Double.NaN;
            }
        }
        return symbol * mantissa * Math.pow(2, exponential);
    }

    public double getValue() {
        return value;
    }

    public void description() {
        System.out.printf("[%d]DoubleConstant - %s%n", getCount(), value);
    }
}
