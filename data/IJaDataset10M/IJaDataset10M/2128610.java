package org.gbif.checklistbank.model;

import org.gbif.ecat.model.Identifiable;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Distribution implements Comparable, Identifiable<Integer> {

    private Integer id;

    private Integer usageId;

    private Integer statusId;

    private String status;

    private Integer meansId;

    private String means;

    private Integer threatId;

    private String threat;

    private Area area;

    private String countryCode;

    private String remarks;

    private Integer sourceId;

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    /**
   * @see java.lang.Comparable#compareTo(Object)
   */
    public int compareTo(Object object) {
        Distribution myClass = (Distribution) object;
        return new CompareToBuilder().append(this.statusId, myClass.statusId).append(this.area, myClass.area).append(this.meansId, myClass.meansId).append(this.countryCode, myClass.countryCode).toComparison();
    }

    /**
   * @see java.lang.Object#equals(Object)
   */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Distribution)) {
            return false;
        }
        Distribution rhs = (Distribution) object;
        return new EqualsBuilder().append(this.statusId, rhs.statusId).append(this.area, rhs.area).append(this.meansId, rhs.meansId).append(this.countryCode, rhs.countryCode).isEquals();
    }

    public Area getArea() {
        return area;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getId() {
        return id;
    }

    public String getMeans() {
        return means;
    }

    public EstablishmentMeans getMeansAsEnum() {
        return EstablishmentMeans.valueOfTermID(meansId);
    }

    public Integer getMeansId() {
        return meansId;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }

    public OccurrenceStatus getStatusAsEnum() {
        return OccurrenceStatus.valueOfTermID(statusId);
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getThreat() {
        return threat;
    }

    public Integer getThreatId() {
        return threatId;
    }

    public Integer getUsageId() {
        return usageId;
    }

    /**
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(152071395, 102782019).append(this.statusId).append(this.area).append(this.meansId).append(this.countryCode).toHashCode();
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public void setMeansId(Integer meansId) {
        this.meansId = meansId;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public void setThreatId(Integer threatId) {
        this.threatId = threatId;
    }

    public void setUsageId(Integer usageId) {
        this.usageId = usageId;
    }
}
