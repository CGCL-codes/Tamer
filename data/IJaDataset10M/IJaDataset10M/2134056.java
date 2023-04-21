package de.proteinms.omxparser.util;

import java.io.Serializable;

/**
 * Ion type at a finer level than ion series.
 * <br><br>
 * Please see "OMSSA.mod.xsd" for further information:
 * <br><br>
 * See <a href="http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd">http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd</a>
 *
 * @author Steffen Huber
 * @author Harald Barsnes
 */
public class MSIon implements Serializable {

    /**
     * Is this peak a neutral loss?
     */
    public MSIon_neutralloss MSIon_neutralloss = new MSIon_neutralloss();

    /**
     * Isotopic composition of peak.
     */
    public MSIon_isotope MSIon_isotope = new MSIon_isotope();

    /**
     * If iontype is internal, this is the internal sequence.
     */
    public String MSIon_internal;

    /**
     * If iontype is immonium, show characteristics.
     */
    public MSIon_immonium MSIon_immonium = new MSIon_immonium();

    /**
     * Sets the MSIon_immonium value
     *
     * @param s the MSIon_immonium value
     */
    public void setMSIon_immonium(MSIon_immonium s) {
        this.MSIon_immonium = s;
    }

    /**
     * Sets the MSIon_internal value
     *
     * @param s the MSIon_internal value as a String
     */
    public void setMSIon_internal(String s) {
        this.MSIon_internal = s;
    }

    /**
     * Sets the MSIon_isotope value
     *
     * @param s the MSIon_isotope value
     */
    public void setMSIon_isotope(MSIon_isotope s) {
        this.MSIon_isotope = s;
    }

    /**
     * Sets the MSIon_neutralloss value
     *
     * @param s the MSIon_neutralloss value
     */
    public void setMSIon_neutralloss(MSIon_neutralloss s) {
        this.MSIon_neutralloss = s;
    }
}
