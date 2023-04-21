package com.kopiright.tanit.report;

import java.io.Writer;
import java.io.IOException;

public class RParameter {

    public RParameter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParamOrder() {
        return paramOrder;
    }

    public void setParamOrder(int paramOrder) {
        this.paramOrder = paramOrder;
    }

    public byte getDatatype() {
        return datatype;
    }

    public void setDatatype(byte datatype) {
        this.datatype = datatype;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public byte getParamType() {
        return paramType;
    }

    public void setParamType(byte paramType) {
        this.paramType = paramType;
    }

    public String getInputMask() {
        return inputMask;
    }

    public void setInputMask(String inputMask) {
        this.inputMask = inputMask;
    }

    public String getOutputMask() {
        return outputMask;
    }

    public void setOutputMask(String outputMask) {
        this.outputMask = outputMask;
    }

    public boolean getSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void accept(ReportVisitor p) {
        p.visitParameter(this);
    }

    private int id;

    private String name;

    private int paramOrder;

    private byte datatype;

    private int width;

    private String defaultValue;

    private String label;

    private byte paramType;

    private String inputMask;

    private String outputMask;

    private boolean skip;
}
