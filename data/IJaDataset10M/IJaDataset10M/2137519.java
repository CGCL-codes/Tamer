package org.kalypso.model.wspm.sobek.core.interfaces;

/**
 * @author thuel2
 */
public interface ISbkStructWeir extends ISbkStructure {

    public double getCrestLevel();

    public double getCrestWidth();

    public double getDischargeCoeffCE();

    public String getFlowDirection();

    public double getLateralContractionCoeffCW();

    public void setLinkToBranch(IBranch branch) throws Exception;
}
