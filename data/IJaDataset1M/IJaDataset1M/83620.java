package net.sf.sidaof.tester.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Jeff Jensen
 */
public class GetSetTestClass {

    private boolean theBooleanPrimitive;

    private char theCharPrimitive;

    private byte theBytePrimitive;

    private short theShortPrimitive;

    private int theIntPrimitive;

    private long theLongPrimitive;

    private float theFloatPrimitive;

    private double theDoublePrimitive;

    private Boolean theBoolean;

    private Character theCharacter;

    private Byte theByte;

    private Short theShort;

    private Integer theInteger;

    private Long theLong;

    private Float theFloat;

    private Double theDouble;

    private GetSetTestEnum theEnum;

    private String theString;

    private Date theDate;

    private java.sql.Date theSqlDate;

    private Collection<String> theCollection;

    private List<String> theList;

    private Set<String> theSet;

    private Map<String, Object> theMap;

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean isTheBooleanPrimitive() {
        return theBooleanPrimitive;
    }

    public void setTheBooleanPrimitive(boolean theBooleanPrimitive) {
        this.theBooleanPrimitive = theBooleanPrimitive;
    }

    public char getTheCharPrimitive() {
        return theCharPrimitive;
    }

    public void setTheCharPrimitive(char theCharPrimitive) {
        this.theCharPrimitive = theCharPrimitive;
    }

    public byte getTheBytePrimitive() {
        return theBytePrimitive;
    }

    public void setTheBytePrimitive(byte theBytePrimitive) {
        this.theBytePrimitive = theBytePrimitive;
    }

    public short getTheShortPrimitive() {
        return theShortPrimitive;
    }

    public void setTheShortPrimitive(short theShortPrimitive) {
        this.theShortPrimitive = theShortPrimitive;
    }

    public int getTheIntPrimitive() {
        return theIntPrimitive;
    }

    public void setTheIntPrimitive(int theIntPrimitive) {
        this.theIntPrimitive = theIntPrimitive;
    }

    public long getTheLongPrimitive() {
        return theLongPrimitive;
    }

    public void setTheLongPrimitive(long theLongPrimitive) {
        this.theLongPrimitive = theLongPrimitive;
    }

    public float getTheFloatPrimitive() {
        return theFloatPrimitive;
    }

    public void setTheFloatPrimitive(float theFloatPrimitive) {
        this.theFloatPrimitive = theFloatPrimitive;
    }

    public double getTheDoublePrimitive() {
        return theDoublePrimitive;
    }

    public void setTheDoublePrimitive(double theDoublePrimitive) {
        this.theDoublePrimitive = theDoublePrimitive;
    }

    public Boolean getTheBoolean() {
        return theBoolean;
    }

    public void setTheBoolean(Boolean theBoolean) {
        this.theBoolean = theBoolean;
    }

    public Character getTheCharacter() {
        return theCharacter;
    }

    public void setTheCharacter(Character theCharacter) {
        this.theCharacter = theCharacter;
    }

    public Byte getTheByte() {
        return theByte;
    }

    public void setTheByte(Byte theByte) {
        this.theByte = theByte;
    }

    public Short getTheShort() {
        return theShort;
    }

    public void setTheShort(Short theShort) {
        this.theShort = theShort;
    }

    public Integer getTheInteger() {
        return theInteger;
    }

    public void setTheInteger(Integer theInteger) {
        this.theInteger = theInteger;
    }

    public Long getTheLong() {
        return theLong;
    }

    public void setTheLong(Long theLong) {
        this.theLong = theLong;
    }

    public Float getTheFloat() {
        return theFloat;
    }

    public void setTheFloat(Float theFloat) {
        this.theFloat = theFloat;
    }

    public Double getTheDouble() {
        return theDouble;
    }

    public void setTheDouble(Double theDouble) {
        this.theDouble = theDouble;
    }

    public GetSetTestEnum getTheEnum() {
        return theEnum;
    }

    public void setTheEnum(GetSetTestEnum theEnum) {
        this.theEnum = theEnum;
    }

    public String getTheString() {
        return theString;
    }

    public void setTheString(String theString) {
        this.theString = theString;
    }

    public Date getTheDate() {
        return theDate;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }

    public java.sql.Date getTheSqlDate() {
        return theSqlDate;
    }

    public void setTheSqlDate(java.sql.Date theSqlDate) {
        this.theSqlDate = theSqlDate;
    }

    public List<String> getTheList() {
        return theList;
    }

    public void setTheList(List<String> theList) {
        this.theList = theList;
    }

    public Collection<String> getTheCollection() {
        return theCollection;
    }

    public void setTheCollection(Collection<String> theCollection) {
        this.theCollection = theCollection;
    }

    public Set<String> getTheSet() {
        return theSet;
    }

    public void setTheSet(Set<String> theSet) {
        this.theSet = theSet;
    }

    public Map<String, Object> getTheMap() {
        return theMap;
    }

    public void setTheMap(Map<String, Object> theMap) {
        this.theMap = theMap;
    }
}
