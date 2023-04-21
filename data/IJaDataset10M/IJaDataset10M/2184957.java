package de.proteinms.omxparser.util;

import java.io.Serializable;

/**
 * Type of immonium ion.
 * <br><br>
 * Please see "OMSSA.mod.xsd" for further information:
 * <br><br>
 * See <a href="http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd">http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd</a>
 *
 * @author Steffen Huber
 * @author Harald Barsnes
 */
public class MSImmonium implements Serializable {

    /**
     * Parent amino acid.
     */
    public String MSImmonium_parent;

    /**
     * Product ion code.
     */
    public String MSImmonium_product;

    /**
     * Sets the MSImmonium_product value
     * 
     * @param s the MSImmonium_product value as a String
     */
    public void setMSImmonium_product(String s) {
        this.MSImmonium_product = s;
    }

    /**
     * Sets the MSImmonium_parent value
     *
     * @param s the MSImmonium_parent value as a String
     */
    public void setMSImmonium_parent(String s) {
        this.MSImmonium_parent = s;
    }
}
