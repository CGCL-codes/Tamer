package net.sf.opendf.cal.i2;

import java.util.ArrayList;
import java.util.List;

public class BasicOperandStack implements OperandStack {

    public void push(Object v) {
        assert tos < stackSize;
        tos += 1;
        if (tos == stackSize) {
            values.add(v);
            stackSize += 1;
        } else {
            values.set(tos, v);
        }
        assert tos < stackSize;
    }

    public Object pop() {
        assert tos >= 0;
        Object v = values.get(tos);
        zero(tos);
        tos -= 1;
        return v;
    }

    public void pop(int n) {
        assert tos >= n - 1;
        tos -= n;
        for (int i = 1; i <= n; i++) {
            zero(tos + i);
        }
    }

    public void replaceWithResult(int n, Object v) {
        if (n == 0) {
            push(v);
        } else {
            tos -= (n - 1);
            values.set(tos, v);
            assert tos >= 0;
            for (int i = 1; i < n; i++) {
                zero(tos + i);
            }
        }
    }

    public Object getValue(int n) {
        return values.get(tos - n);
    }

    public int size() {
        return tos + 1;
    }

    public BasicOperandStack() {
        values = new ArrayList<Object>();
        tos = -1;
        stackSize = 0;
    }

    private List<Object> values;

    private int tos;

    private int stackSize;

    private final void zero(int n) {
        values.set(n, null);
    }
}
