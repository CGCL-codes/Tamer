package com.sodad.weka.classifiers.lazy.kstar;

import com.sodad.weka.core.RevisionHandler;
import com.sodad.weka.core.RevisionUtils;

public class KStarWrapper implements RevisionHandler {

    /** used/reused to hold the sphere size */
    public double sphere = 0.0;

    /** used/reused to hold the actual entropy */
    public double actEntropy = 0.0;

    /** used/reused to hold the random entropy */
    public double randEntropy = 0.0;

    /** used/reused to hold the average transformation probability */
    public double avgProb = 0.0;

    /** used/reused to hold the smallest transformation probability */
    public double minProb = 0.0;

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.7 $");
    }
}
