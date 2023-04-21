package com.kopiright.tanit.report;

import java.io.Writer;
import java.io.IOException;

public class RSummary {

    public RSummary(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public RField getField() {
        return field;
    }

    public void setField(RField field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public byte getOperator() {
        return operator;
    }

    public void setOperator(byte operator) {
        this.operator = operator;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte getDatatype() {
        return datatype;
    }

    public void setDatatype(byte datatype) {
        this.datatype = datatype;
    }

    public String getFormatMask() {
        return formatMask;
    }

    public void setFormatMask(String formatMask) {
        this.formatMask = formatMask;
    }

    public int getPrintAt() {
        return printAt;
    }

    public void setPrintAt(int printAt) {
        this.printAt = printAt;
    }

    public int getResetAt() {
        return resetAt;
    }

    public void setResetAt(int resetAt) {
        this.resetAt = resetAt;
    }

    public void accept(ReportVisitor p) {
        p.visitSummary(this);
    }

    private int id;

    private String name;

    private RField field;

    private int order;

    private byte operator;

    private int width;

    private byte datatype;

    private String formatMask;

    private int printAt;

    private int resetAt;
}
