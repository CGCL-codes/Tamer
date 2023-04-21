package com.angel.architecture.persistence.beans;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import com.angel.architecture.persistence.base.PersistentObject;

/**
 * @author William
 */
@Entity
public class ConfigurationParameter extends PersistentObject {

    private static final long serialVersionUID = 989778263580031157L;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(nullable = false)
    private ParameterArea parameterArea;

    @Column(length = 30, nullable = true)
    private String minimumValue;

    @Column(length = 30, nullable = true)
    private String maximumValue;

    @Column(length = 150, nullable = true)
    private String stringValue;

    @Column(scale = 7, nullable = true)
    private Long longValue;

    @Column(nullable = true)
    private Double doubleValue;

    @Column(nullable = true)
    private Float floatValue;

    @Column(nullable = true)
    private Character characterValue;

    @Column(nullable = true)
    private Boolean booleanValue;

    @Column(nullable = true)
    private Date dateValue;

    public ConfigurationParameter() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public String getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Character getCharacterValue() {
        return characterValue;
    }

    public void setCharacterValue(Character characterValue) {
        this.characterValue = characterValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    /**
	 * @return the parameterArea
	 */
    public ParameterArea getParameterArea() {
        return parameterArea;
    }

    /**
	 * @param parameterArea the parameterArea to set
	 */
    public void setParameterArea(ParameterArea parameterArea) {
        this.parameterArea = parameterArea;
    }

    public boolean isForParameterArea(String parameterAreaName) {
        return this.getParameterArea().isName(parameterAreaName);
    }
}
