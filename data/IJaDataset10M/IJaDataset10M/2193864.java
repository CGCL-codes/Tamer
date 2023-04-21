package com.jeantessier.classreader.impl;

import java.io.*;
import com.jeantessier.classreader.*;

public class Double_info extends ConstantPoolEntry implements com.jeantessier.classreader.Double_info {

    private double value;

    public Double_info(ConstantPool constantPool, DataInput in) throws IOException {
        super(constantPool);
        value = in.readDouble();
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(getValue());
    }

    public int hashCode() {
        return Double.valueOf(getValue()).hashCode();
    }

    public boolean equals(Object object) {
        boolean result = false;
        if (this == object) {
            result = true;
        } else if (object != null && this.getClass().equals(object.getClass())) {
            Double_info other = (Double_info) object;
            result = Double.compare(this.getValue(), other.getValue()) == 0;
        }
        return result;
    }

    public void accept(Visitor visitor) {
        visitor.visitDouble_info(this);
    }
}
