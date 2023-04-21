package sk.tuke.ess.lib.integrator;

import java.io.Serializable;

/**
 *
 * @author Marek
 */
public interface IRkm extends Serializable {

    /**
     * @return absError_dov
     */
    public double getAbsError();

    /**
     * @param absError the absError_dov to set
     */
    public void setAbsError(double absError);

    /**
     * @return relError_dov
     */
    public double getRelError();

    /**
     * @param relError the relError_dov to set
     */
    public void setRelError(double relError);
}
