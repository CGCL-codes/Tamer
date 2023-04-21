package com.mattharrah.gedcom4j.model;

/**
 * A source system. Corresponds to the SOUR structure in HEADER in the GEDCOM
 * file.
 * 
 * @author frizbog1
 * 
 */
public class SourceSystem {

    /**
     * The system ID for this source system
     */
    public String systemId;

    /**
     * The version number of this source system
     */
    public String versionNum;

    /**
     * The product name for this source system
     */
    public String productName;

    /**
     * The corporation that owns this source system
     */
    public Corporation corporation;

    /**
     * Header source data for this source system.
     */
    public HeaderSourceData sourceData;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SourceSystem other = (SourceSystem) obj;
        if (corporation == null) {
            if (other.corporation != null) {
                return false;
            }
        } else if (!corporation.equals(other.corporation)) {
            return false;
        }
        if (productName == null) {
            if (other.productName != null) {
                return false;
            }
        } else if (!productName.equals(other.productName)) {
            return false;
        }
        if (sourceData == null) {
            if (other.sourceData != null) {
                return false;
            }
        } else if (!sourceData.equals(other.sourceData)) {
            return false;
        }
        if (systemId == null) {
            if (other.systemId != null) {
                return false;
            }
        } else if (!systemId.equals(other.systemId)) {
            return false;
        }
        if (versionNum == null) {
            if (other.versionNum != null) {
                return false;
            }
        } else if (!versionNum.equals(other.versionNum)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((corporation == null) ? 0 : corporation.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((sourceData == null) ? 0 : sourceData.hashCode());
        result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
        result = prime * result + ((versionNum == null) ? 0 : versionNum.hashCode());
        return result;
    }
}
