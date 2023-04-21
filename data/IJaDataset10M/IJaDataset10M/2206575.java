package de.proteinms.omxparser.util;

import java.io.Serializable;

/**
 * List of modificatinos that can be used in search.
 * <br><br>
 * Please see "OMSSA.mod.xsd" for further information:
 * <br><br>
 * See <a href="http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd">http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd</a>
 *
 * @author Steffen Huber
 * @author Harald Barsnes
 */
public class MSRequest_modset implements Serializable {

    /**
     * List of modificatinos that can be used in search.
     */
    public MSModSpecSet MSModSpecSet = new MSModSpecSet();

    /**
     * Sets the MSModSpecSet value.
     *
     * @param s the MSModSpecSet value
     */
    public void setMSModSpecSet(MSModSpecSet s) {
        this.MSModSpecSet = s;
    }
}
