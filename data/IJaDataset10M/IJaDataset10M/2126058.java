package edu.scripps.fl.curves;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import edu.scripps.fl.hibernate.BooleanListStringType;
import edu.scripps.fl.hibernate.DoubleListStringType;

/**
 * @author Mark Southern (southern at scripps dot edu)
 */
@Entity
@Table(name = "curves")
@TypeDefs({ @TypeDef(name = "DoubleListStringType", typeClass = DoubleListStringType.class), @TypeDef(name = "BooleanListStringType", typeClass = BooleanListStringType.class) })
public class Curve {

    private String name = "";

    private List<Double> concentrations = new ArrayList<Double>();

    private Double curveClass;

    private String curveDescription = "";

    private Double EC50;

    private Double hillSlope;

    private Double IC50;

    private Long id = null;

    private Double logEC50;

    private List<Boolean> mask = new ArrayList<Boolean>();

    private Boolean masked;

    private Double maxResponse;

    private Double pHill;

    private Double R2;

    private Double rank;

    private Double responseMax;

    private Double responseMin;

    private Double responseRange;

    private List<Double> responses = new ArrayList<Double>();

    private String signalDirection = "";

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Double SYX;

    private Double YInflection;

    private Double YZero;

    public void add(Double response, Double concentration) {
        add(response, concentration, true);
    }

    public void add(Double response, Double concentration, Boolean mask) {
        responses.add(response);
        concentrations.add(concentration);
        getMask().add(mask);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Curve)) return false;
        Curve other = (Curve) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Type(type = "DoubleListStringType")
    @Fetch(value = FetchMode.SELECT)
    @Column(name = "concentrations", length = 4000)
    public List<Double> getConcentrations() {
        return concentrations;
    }

    @Column(name = "curveClass")
    public Double getCurveClass() {
        return curveClass;
    }

    @Column(name = "curveDescription")
    public String getCurveDescription() {
        return curveDescription;
    }

    @Column(name = "ec50")
    public Double getEC50() {
        return EC50;
    }

    @Column(name = "hillSlope")
    public Double getHillSlope() {
        return hillSlope;
    }

    @Column(name = "ic50")
    public Double getIC50() {
        return IC50;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(name = "logEC50")
    public Double getLogEC50() {
        return logEC50;
    }

    @Type(type = "BooleanListStringType")
    @Fetch(value = FetchMode.SELECT)
    @Column(name = "mask", length = 4000)
    public List<Boolean> getMask() {
        return mask;
    }

    @Column(name = "masked")
    public Boolean getMasked() {
        return masked;
    }

    @Column(name = "maxResponse")
    public Double getMaxResponse() {
        return maxResponse;
    }

    @Column(name = "pHill")
    public Double getPHill() {
        return pHill;
    }

    @Column(name = "r2")
    public Double getR2() {
        return R2;
    }

    @Column(name = "rank")
    public Double getRank() {
        return rank;
    }

    @Column(name = "responseMax")
    public Double getResponseMax() {
        return responseMax;
    }

    @Column(name = "responseMin")
    public Double getResponseMin() {
        return responseMin;
    }

    @Column(name = "responseRange")
    public Double getResponseRange() {
        return responseRange;
    }

    @Type(type = "DoubleListStringType")
    @Fetch(value = FetchMode.SELECT)
    @Column(name = "responses", length = 4000)
    public List<Double> getResponses() {
        return responses;
    }

    @Column(name = "signalDirection")
    public String getSignalDirection() {
        return signalDirection;
    }

    @Column(name = "syx")
    public Double getSYX() {
        return SYX;
    }

    @Column(name = "yInflection")
    public Double getYInflection() {
        return YInflection;
    }

    @Column(name = "yZero")
    public Double getYZero() {
        return YZero;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setConcentrations(List<Double> concentrations) {
        this.concentrations = concentrations;
    }

    public void setCurveClass(Double curveClass) {
        this.curveClass = curveClass;
    }

    public void setCurveDescription(String curveDescription) {
        this.curveDescription = curveDescription;
    }

    public void setEC50(Double ec50) {
        EC50 = ec50;
    }

    public void setHillSlope(Double hillSlope) {
        this.hillSlope = hillSlope;
    }

    public void setIC50(Double ic50) {
        IC50 = ic50;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogEC50(Double logEC50) {
        this.logEC50 = logEC50;
    }

    public void setMask(List<Boolean> mask) {
        this.mask = mask;
    }

    public void setMasked(Boolean masked) {
        this.masked = masked;
    }

    public void setMaxResponse(Double maxResponse) {
        this.maxResponse = maxResponse;
    }

    public void setPHill(Double hill) {
        pHill = hill;
    }

    public void setR2(Double r2) {
        R2 = r2;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public void setResponseMax(Double responseMax) {
        this.responseMax = responseMax;
    }

    public void setResponseMin(Double responseMin) {
        this.responseMin = responseMin;
    }

    public void setResponseRange(Double responseRange) {
        this.responseRange = responseRange;
    }

    public void setResponses(List<Double> responses) {
        this.responses = responses;
    }

    public void setSignalDirection(String signalDirection) {
        this.signalDirection = signalDirection;
    }

    public void setSYX(Double syx) {
        SYX = syx;
    }

    public void setYInflection(Double inflection) {
        YInflection = inflection;
    }

    public void setYZero(Double zero) {
        YZero = zero;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
