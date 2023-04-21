package org.metadiff.ext.generic;

import java.util.Map;
import org.metadiff.template.ModelMapping;

/**
 * @author Mark Kofman
 *
 */
public class SimpleModelMapping implements ModelMapping {

    private Map aToB;

    private Map bToA;

    /**
     * @return Returns the aToB.
     */
    public Map getAToB() {
        return aToB;
    }

    /**
     * @param toB The aToB to set.
     */
    public void setAToB(Map toB) {
        aToB = toB;
    }

    /**
     * @return Returns the bToA.
     */
    public Map getBToA() {
        return bToA;
    }

    /**
     * @param toA The bToA to set.
     */
    public void setBToA(Map toA) {
        bToA = toA;
    }
}
