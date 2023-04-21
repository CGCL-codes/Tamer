package de.proteinms.omxparser.util;

import java.io.Serializable;

/**
 * MS outfile type.
 * <br><br>
 * Please see "OMSSA.mod.xsd" for further information:
 * <br><br>
 * See <a href="http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd">http://www.ncbi.nlm.nih.gov/data_specs/schema/OMSSA.mod.xsd</a>
 *
 * @author Steffen Huber
 * @author Harald Barsnes
 */
public class MSOutFile_outfiletype implements Serializable {

    /**
     * Serial data format.
     * <br><br>
     * Hint: To get the serial data format as text use the OmssaEnumerators class.
     */
    public int MSSerialDataFormat;

    /**
     * Should the output include the request?
     */
    public Boolean MSOutFile_includerequest;

    /**
     * Sets the MSOutFile_includerequest value.
     *
     * @param s the MSOutFile_includerequest value as a String
     */
    public void setMSOutFile_includerequest(String s) {
        this.MSOutFile_includerequest = Boolean.valueOf(s);
    }

    /**
     * Sets the MSSerialDataFormat value.
     *
     * @param s the MSSerialDataFormat value as a String
     */
    public void setMSSerialDataFormat(String s) {
        this.MSSerialDataFormat = Integer.valueOf(s);
    }
}
