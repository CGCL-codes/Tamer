package com.setec.common.math;

import com.setec.commonutils.ReflectionUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class Vect {

    private Number[] data;

    public Vect(int size) {
        data = new Number[size];
    }

    public Number get(int index) {
        return data[index];
    }

    public void set(int index, Number value) {
        data[index] = value;
    }

    public Vect add(Vect operand) {
        for (int i = 0; i < getSize(); i++) {
            set(i, NumberUtils.add(this.get(i), operand.get(i)));
        }
        return this;
    }

    public Vect sub(Vect operand) {
        for (int i = 0; i < getSize(); i++) {
            set(i, NumberUtils.sub(this.get(i), operand.get(i)));
        }
        return this;
    }

    public Vect div(Vect operand) {
        for (int i = 0; i < getSize(); i++) {
            set(i, NumberUtils.div(this.get(i), operand.get(i)));
        }
        return this;
    }

    public Vect mul(Vect operand) {
        for (int i = 0; i < getSize(); i++) {
            set(i, NumberUtils.add(this.get(i), operand.get(i)));
        }
        return this;
    }

    public int getSize() {
        return data.length;
    }
}
