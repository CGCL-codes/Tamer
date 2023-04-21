package jcontrol.conect.data;

import jcontrol.conect.data.settings.Settings;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * This class represents a ParameterType. 
 */
public class ParameterType implements Serializable, Comparable {

    /** Class ID for object serialization */
    static final long serialVersionUID = 6333031183431523604L;

    /** Variable name for parameter type id. */
    public static final String PARAMETER_TYPE_ID = "parameterTypeId";

    /** Variable name for atomic type number. */
    public static final String ATOMIC_TYPE_NUMBER = "atomicTypeNumber";

    /** Variable name for program id . */
    public static final String PROGRAM_ID = "programId";

    /** Variable name for parameter type name. */
    public static final String PARAMETER_TYPE_NAME = "parameterTypeName";

    /** Variable name for parameter minimum value. */
    public static final String PARAMETER_MINIMUM_VALUE = "parameterMinimumValue";

    /** Variable name for parameter maximum value . */
    public static final String PARAMETER_MAXIMUM_VALUE = "parameterMaximumValue";

    /** Variable name for parameter type description. */
    public static final String PARAMETER_TYPE_DESCRIPTION = "parameterTypeDescription";

    /** Variable name for parameter type low access. */
    public static final String PARAMETER_TYPE_LOW_ACCESS = "parameterTypeLowAccess";

    /** Variable name for parameter type high access. */
    public static final String PARAMETER_TYPE_HIGH_ACCESS = "parameterTypeHighAccess";

    /** Variable name for parameter type size. */
    public static final String PARAMETER_TYPE_SIZE = "parameterTypeSize";

    /** Variable name for parameter minimum double value. */
    public static final String PARAMETER_MINIMUM_DOUBLE_VALUE = "parameterMinimumDoubleValue";

    /** Variable name for parameter maximum double value . */
    public static final String PARAMETER_MAXIMUM_DOUBLE_VALUE = "parameterMaximumDoubleValue";

    /** Id of the ParameterType that belongs to this Parameter. */
    protected int parameterTypeId;

    /** Id of the atomic type. */
    protected int atomicTypeNumber;

    /** Id of the ApplicationProgram that belongs to this parameter. */
    protected int programId;

    /** AtomicType of this ParameterType. */
    protected ParameterAtomicType parameterAtomicType;

    /** ApplicationProgram that belongs to parameter. */
    protected ApplicationProgram applicationProgram;

    /** Parameter list of values. */
    protected ParameterListOfValues[] parameterListOfValues;

    /** Name of this ParameterType. */
    protected String parameterTypeName;

    /** Minimum value of parameter. */
    protected int parameterMinimumValue;

    /** Maximum value of parameter. */
    protected int parameterMaximumValue;

    /** Description of this ParameterType. */
    protected String parameterTypeDescription;

    /** Low access of this ParameterType. */
    protected int parameterTypeLowAccess;

    /** High access of this ParameterType. */
    protected int parameterTypeHighAccess;

    /** Size of this ParameterType. */
    protected int parameterTypeSize;

    /** Minimum double value of parameter. */
    protected double parameterMinimumDoubleValue;

    /** Maximum double value of parameter. */
    protected double parameterMaximumDoubleValue;

    /** 
     * Constructor with name of this ParameterType.
     *
     * @param parameterTypeName name of this ParameterType.
     *
     */
    public ParameterType(String parameterTypeName) {
        this();
        this.parameterTypeName = parameterTypeName;
    }

    /** 
     * Default constructor.
     */
    public ParameterType() {
        super();
    }

    /**
     * Get the value of parameterTypeId.
     * @return Value of parameterTypeId.
     */
    public int getParameterTypeId() {
        return parameterTypeId;
    }

    /**
     * Set the value of parameterTypeId.
     * @param v  Value to assign to parameterTypeId.
     */
    public void setParameterTypeId(int v) {
        this.parameterTypeId = v;
    }

    /**
     * Get the value of atomicTypeNumber.
     * @return Value of atomicTypeNumber.
     */
    public int getAtomicTypeNumber() {
        return atomicTypeNumber;
    }

    /**
     * Set the value of atomicTypeNumber.
     * @param v  Value to assign to atomicTypeNumber.
     */
    public void setAtomicTypeNumber(int v) {
        this.atomicTypeNumber = v;
    }

    /**
     * Get the value of programId.
     * @return Value of programId.
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * Set the value of programId.
     * @param v  Value to assign to programId.
     */
    public void setProgramId(int v) {
        this.programId = v;
    }

    /**
     * Get the value of parameterAtomicType.
     * @return Value of parameterAtomicType.
     */
    public ParameterAtomicType getParameterAtomicType() {
        return parameterAtomicType;
    }

    /**
     * Set the value of parameterAtomicType.
     * @param v  Value to assign to parameterAtomicType.
     */
    public void setParameterAtomicType(ParameterAtomicType v) {
        this.parameterAtomicType = v;
    }

    /**
     * Get the value of applicationProgram.
     * @return Value of applicationProgram.
     */
    public ApplicationProgram getApplicationProgram() {
        return applicationProgram;
    }

    /**
     * Set the value of applicationProgram.
     * @param v  Value to assign to applicationProgram.
     */
    public void setApplicationProgram(ApplicationProgram v) {
        this.applicationProgram = v;
    }

    /**
     * Get the value of parameterListOfValues.
     * @return Value of parameterListOfValues.
     */
    public ParameterListOfValues[] getParameterListOfValues() {
        return parameterListOfValues;
    }

    /**
     * Set the value of parameterListOfValues.
     * @param v  Value to assign to parameterListOfValues.
     */
    public void setParameterListOfValues(ParameterListOfValues[] v) {
        this.parameterListOfValues = v;
    }

    /**
     * Get the value of parameterTypeName.
     * @return Value of parameterTypeName.
     */
    public String getParameterTypeName() {
        return parameterTypeName;
    }

    /**
     * Set the value of parameterTypeName.
     * @param v  Value to assign to parameterTypeName.
     */
    public void setParameterTypeName(String v) {
        this.parameterTypeName = v;
    }

    /**
     * Get the value of parameterMinimumValue.
     * @return Value of parameterMinimumValue.
     */
    public int getParameterMinimumValue() {
        return parameterMinimumValue;
    }

    /**
     * Set the value of parameterMinimumValue.
     * @param v  Value to assign to parameterMinimumValue.
     */
    public void setParameterMinimumValue(int v) {
        this.parameterMinimumValue = v;
    }

    /**
     * Get the value of parameterMaximumValue.
     * @return Value of parameterMaximumValue.
     */
    public int getParameterMaximumValue() {
        return parameterMaximumValue;
    }

    /**
     * Set the value of parameterMaximumValue.
     * @param v  Value to assign to parameterMaximumValue.
     */
    public void setParameterMaximumValue(int v) {
        this.parameterMaximumValue = v;
    }

    /**
     * Get the value of parameterTypeDescription.
     * @return Value of parameterTypeDescription.
     */
    public String getParameterTypeDescription() {
        return parameterTypeDescription;
    }

    /**
     * Set the value of parameterTypeDescription.
     * @param v  Value to assign to parameterTypeDescription.
     */
    public void setParameterTypeDescription(String v) {
        this.parameterTypeDescription = v;
    }

    /**
     * Get the value of parameterTypeLowAccess.
     * @return Value of parameterTypeLowAccess.
     */
    public int getParameterTypeLowAccess() {
        return parameterTypeLowAccess;
    }

    /**
     * Set the value of parameterTypeLowAccess.
     * @param v  Value to assign to parameterTypeLowAccess.
     */
    public void setParameterTypeLowAccess(int v) {
        this.parameterTypeLowAccess = v;
    }

    /**
     * Get the value of parameterTypeHighAccess.
     * @return Value of parameterTypeHighAccess.
     */
    public int getParameterTypeHighAccess() {
        return parameterTypeHighAccess;
    }

    /**
     * Set the value of parameterTypeHighAccess.
     * @param v  Value to assign to parameterTypeHighAccess.
     */
    public void setParameterTypeHighAccess(int v) {
        this.parameterTypeHighAccess = v;
    }

    /**
     * Get the value of parameterTypeSize.
     * @return Value of parameterTypeSize.
     */
    public int getParameterTypeSize() {
        return parameterTypeSize;
    }

    /**
     * Set the value of parameterTypeSize.
     * @param v  Value to assign to parameterTypeSize.
     */
    public void setParameterTypeSize(int v) {
        this.parameterTypeSize = v;
    }

    /**
     * Get the value of parameterMinimumDoubleValue.
     * @return Value of parameterMinimumDoubleValue.
     */
    public double getParameterMinimumDoubleValue() {
        return parameterMinimumDoubleValue;
    }

    /**
     * Set the value of parameterMinimumDoubleValue.
     * @param v  Value to assign to parameterMinimumDoubleValue.
     */
    public void setParameterMinimumDoubleValue(double v) {
        this.parameterMinimumDoubleValue = v;
    }

    /**
     * Get the value of parameterMaximumDoubleValue.
     * @return Value of parameterMaximumDoubleValue.
     */
    public double getParameterMaximumDoubleValue() {
        return parameterMaximumDoubleValue;
    }

    /**
     * Set the value of parameterMaximumDoubleValue.
     * @param v  Value to assign to parameterMaximumDoubleValue.
     */
    public void setParameterMaximumDoubleValue(double v) {
        this.parameterMaximumDoubleValue = v;
    }

    /**
     * Implementation of <code>Interface Comparable</code>.
     */
    public int compareTo(Object o) {
        return parameterTypeName.compareTo(((ParameterType) o).getParameterTypeName());
    }

    /**
     * Prints all entrys of ParameterType.
     */
    public String toString() {
        String str = "";
        final String START = "<";
        final String EQUAL = "=";
        final String QUOTE = "\"";
        final String END = " />";
        final String COMMENT_START = "<!-- ";
        final String COMMENT_END = " -->";
        str += COMMENT_START + getClass().getName() + " " + parameterTypeName + COMMENT_END + System.getProperty("line.separator");
        str += START;
        str += getClass().getName() + System.getProperty("line.separator");
        str += "\t" + PARAMETER_TYPE_ID + EQUAL + QUOTE + parameterTypeId + QUOTE + System.getProperty("line.separator");
        str += "\t" + ATOMIC_TYPE_NUMBER + EQUAL + QUOTE + atomicTypeNumber + QUOTE + System.getProperty("line.separator");
        str += "\t" + PROGRAM_ID + EQUAL + QUOTE + programId + QUOTE + System.getProperty("line.separator");
        if (parameterTypeName == null) str += "\t" + PARAMETER_TYPE_NAME + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator"); else str += "\t" + PARAMETER_TYPE_NAME + EQUAL + QUOTE + parameterTypeName + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_MINIMUM_VALUE + EQUAL + QUOTE + parameterMinimumValue + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_MAXIMUM_VALUE + EQUAL + QUOTE + parameterMaximumValue + QUOTE + System.getProperty("line.separator");
        if (parameterTypeDescription != null) str += "\t" + PARAMETER_TYPE_DESCRIPTION + EQUAL + QUOTE + parameterTypeDescription + QUOTE + System.getProperty("line.separator"); else str += "\t" + PARAMETER_TYPE_DESCRIPTION + EQUAL + QUOTE + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_TYPE_LOW_ACCESS + EQUAL + QUOTE + parameterTypeLowAccess + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_TYPE_HIGH_ACCESS + EQUAL + QUOTE + parameterTypeHighAccess + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_TYPE_SIZE + EQUAL + QUOTE + parameterTypeSize + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_MINIMUM_DOUBLE_VALUE + EQUAL + QUOTE + parameterMinimumDoubleValue + QUOTE + System.getProperty("line.separator");
        str += "\t" + PARAMETER_MAXIMUM_DOUBLE_VALUE + EQUAL + QUOTE + parameterMaximumDoubleValue + QUOTE + END + System.getProperty("line.separator");
        return str;
    }
}
