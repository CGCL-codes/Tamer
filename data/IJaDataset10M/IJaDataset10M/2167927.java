package no.ugland.utransprod.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CuttingLine extends BaseObject {

    public static final int GRADE = 5;

    public static final int TICKNESS = 6;

    public static final int DEPTH = 7;

    public static final int TOTAL_LENGTH = 8;

    public static final int LENGTH_CENTER = 9;

    public static final int AREA = 10;

    public static final int DESCRIPTION = 27;

    public static final int NUMBER_OF = 28;

    public static final int TIMBER_MARKING = 29;

    public static final int GROSS_LENGTH = 30;

    public static final int DEL_PC_BELONGS_TO = 56;

    private Integer cuttingLineId;

    private String name;

    private String cutId;

    private String cutLine;

    private Integer lineNr;

    private Cutting cutting;

    private String grade;

    private String tickness;

    private String depth;

    private String totalLength;

    private String lengthCenter;

    private String area;

    private String description;

    private String numberOf;

    private String timberMarking;

    private String grossLength;

    private String delPcBelongsTo;

    public void setCutting(Cutting aCutting) {
        cutting = aCutting;
    }

    public String getName() {
        return name;
    }

    public String getCutId() {
        return cutId;
    }

    public String getCutLine() {
        return cutLine;
    }

    public void setName(String aName) {
        name = aName;
    }

    public void setCutId(String aCutId) {
        cutId = aCutId;
    }

    public void setCutLine(String line) {
        cutLine = line;
    }

    public Integer getLineNr() {
        return lineNr;
    }

    public void setLineNr(Integer aLineNr) {
        lineNr = aLineNr;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CuttingLine)) return false;
        CuttingLine castOther = (CuttingLine) other;
        return new EqualsBuilder().append(name, castOther.name).append(cutId, castOther.cutId).append(lineNr, castOther.lineNr).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(cutId).append(lineNr).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("cutId", cutId).append("cutLine", cutLine).append("lineNr", lineNr).append("cutting", cutting).toString();
    }

    public Integer getCuttingLineId() {
        return cuttingLineId;
    }

    public void setCuttingLineId(Integer cuttingLineId) {
        this.cuttingLineId = cuttingLineId;
    }

    public Cutting getCutting() {
        return cutting;
    }

    public String getGrade() {
        return grade;
    }

    public String getTickness() {
        return tickness;
    }

    public String getDepth() {
        return depth;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public String getLengthCenter() {
        return lengthCenter;
    }

    public String getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }

    public String getNumberOf() {
        return numberOf;
    }

    public String getTimberMarking() {
        return timberMarking;
    }

    public String getGrossLength() {
        return grossLength;
    }

    public String getDelPcBelongsTo() {
        return delPcBelongsTo;
    }

    public void setGrade(String aGrade) {
        grade = aGrade;
    }

    public void setTickness(String aTickness) {
        tickness = aTickness;
    }

    public void setDepth(String aDepth) {
        depth = aDepth;
    }

    public void setTotalLength(String aTotalLength) {
        totalLength = aTotalLength;
    }

    public void setLengthCenter(String aLengthCenter) {
        lengthCenter = aLengthCenter;
    }

    public void setArea(String aArea) {
        area = aArea;
    }

    public void setDescription(String aDescription) {
        description = aDescription;
    }

    public void setNumberOf(String aNumberOf) {
        numberOf = aNumberOf;
    }

    public void setTimberMarking(String aTimberMarking) {
        timberMarking = aTimberMarking;
    }

    public void setGrossLength(String aGrossLength) {
        grossLength = aGrossLength;
    }

    public void setDelPcBelongsTo(String aDelPcBelongTo) {
        delPcBelongsTo = aDelPcBelongTo;
    }
}
