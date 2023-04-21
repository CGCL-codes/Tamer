package com.ecmdeveloper.plugin.diagrams.model;

/**
 * @author Ricardo.Belfor
 *
 */
public class ClassConnector {

    private String classId;

    private String className;

    private String multiplicity;

    private String propertyId;

    private String propertyName;

    private boolean aggregate;

    public ClassConnector() {
    }

    public ClassConnector(String classId, String className, String propertyId, String propertyName, String multiplicity) {
        this.classId = classId;
        this.className = className;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.multiplicity = multiplicity;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setAggregate(boolean aggregate) {
        this.aggregate = aggregate;
    }

    public boolean isAggregate() {
        return aggregate;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(className);
        if (propertyName != null) {
            stringBuffer.append(", ");
            stringBuffer.append(propertyName);
        }
        return stringBuffer.toString();
    }
}
