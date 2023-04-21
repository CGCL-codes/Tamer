package org.brainypdm.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PerformanceDataHistoryId implements Serializable {

    private static final long serialVersionUID = -6883415378209168718L;

    /**
	 * id parent service data
	 */
    private Long idServiceData;

    /**
	 * the check date
	 */
    private Timestamp checkDate;

    public Long getIdServiceData() {
        return idServiceData;
    }

    public void setIdServiceData(Long idServiceData) {
        this.idServiceData = idServiceData;
    }

    public Timestamp getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Timestamp checkDate) {
        this.checkDate = checkDate;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((checkDate == null) ? 0 : checkDate.hashCode());
        result = prime * result + ((idServiceData == null) ? 0 : idServiceData.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final PerformanceDataHistoryId other = (PerformanceDataHistoryId) obj;
        if (checkDate == null) {
            if (other.checkDate != null) return false;
        } else if (!checkDate.equals(other.checkDate)) return false;
        if (idServiceData == null) {
            if (other.idServiceData != null) return false;
        } else if (!idServiceData.equals(other.idServiceData)) return false;
        return true;
    }

    /**
	 * toString methode: creates a String representation of the object
	 * 
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("PerformanceDataId[");
        buffer.append("serialVersionUID = ").append(serialVersionUID);
        buffer.append(", idServiceData = ").append(idServiceData);
        buffer.append(", checkDate = ").append(checkDate);
        buffer.append("]");
        return buffer.toString();
    }
}
