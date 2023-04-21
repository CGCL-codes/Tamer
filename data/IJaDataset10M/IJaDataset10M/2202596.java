package org.expasy.jpl.insilico.ms.peak;

/**
 * Enable to convert the object that implement this interface to {@code JPLIPeak}.
 */
public interface JPLPeakConvertible {

    public JPLIMSPeak toPeak();
}
