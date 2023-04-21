package com.netx.generics.R1.util;

import com.netx.basic.R1.eh.Checker;
import com.netx.basic.R1.eh.IntegrityException;

public class ByteValue {

    public static enum MEASURE {

        BYTES, KILOBYTES, MEGABYTES, GIGABYTES, TERABYTES
    }

    public static double convert(double value, MEASURE initialMeasure, MEASURE finalMeasure) {
        return new ByteValue(value, initialMeasure).getAs(finalMeasure);
    }

    private final long _bytes;

    public ByteValue(double value, MEASURE measure) {
        Checker.checkMinValue((long) value, 1, "value");
        Checker.checkNull(measure, "measure");
        switch(measure) {
            case BYTES:
                _bytes = (long) value;
                break;
            case KILOBYTES:
                _bytes = (long) (value * Factors.kb);
                break;
            case MEGABYTES:
                _bytes = (long) (value * Factors.mb);
                break;
            case GIGABYTES:
                _bytes = (long) (value * Factors.gb);
                break;
            case TERABYTES:
                _bytes = (long) (value * Factors.tb);
                break;
            default:
                throw new IntegrityException(measure);
        }
    }

    public double getAs(MEASURE measure) {
        Checker.checkNull(measure, "measure");
        if (measure == MEASURE.BYTES) {
            return _bytes;
        } else if (measure == MEASURE.KILOBYTES) {
            return _bytes / Factors.kb;
        } else if (measure == MEASURE.MEGABYTES) {
            return _bytes / Factors.mb;
        } else if (measure == MEASURE.GIGABYTES) {
            return _bytes / Factors.gb;
        } else if (measure == MEASURE.TERABYTES) {
            return _bytes / Factors.tb;
        } else {
            throw new IntegrityException(measure);
        }
    }

    private static class Factors {

        public static final double tb = Math.pow(2, 40);

        public static final double gb = Math.pow(2, 30);

        public static final double mb = Math.pow(2, 20);

        public static final double kb = Math.pow(2, 10);
    }
}
