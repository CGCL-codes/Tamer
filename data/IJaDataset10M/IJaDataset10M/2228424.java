package org.personalsmartspace.psm.rs.api.pss3p;

public interface ISharingStrategyDescription {

    public int getStrategyType();

    public boolean isContextDependent();

    public String getAttributeType();

    public String getValue();
}
