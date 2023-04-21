package jpar2.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

public class CodeValueMap<T extends Codeable<C>, C> implements Cloneable {

    private Map<C, T> codeToValue;

    private Map<T, C> valueToCode;

    public CodeValueMap(CodeValueMap<T, C> copy) {
        codeToValue = new IdentityHashMap<C, T>(copy.codeToValue);
        valueToCode = new IdentityHashMap<T, C>(copy.valueToCode);
    }

    public CodeValueMap() {
        codeToValue = new IdentityHashMap<C, T>();
        valueToCode = new IdentityHashMap<T, C>();
    }

    public CodeValueMap(int expectedMaxSize) {
        codeToValue = new IdentityHashMap<C, T>(expectedMaxSize);
        valueToCode = new IdentityHashMap<T, C>(expectedMaxSize);
    }

    public CodeValueMap(Map<C, T> map) {
        this(map.size());
        for (Map.Entry<C, T> entry : map.entrySet()) {
            encode(entry.getKey(), entry.getValue());
        }
    }

    public CodeValueMap(T[] values) {
        this(Arrays.asList(values));
    }

    public CodeValueMap(Collection<T> values) {
        this(values.size());
        for (T value : values) {
            encode(value.getCode(), value);
        }
    }

    public CodeValueMap(C[] codes, T[] values) {
        this(codes.length);
        if (codes.length != values.length) throw new IllegalArgumentException("Lengths do not match");
        for (int i = 0; i < codes.length; i++) {
            encode(codes[i], values[i]);
        }
    }

    public void encode(C code, T value) {
        if (value == null) throw new NullPointerException("Null value");
        if (containsCode(code)) throw new IllegalArgumentException("Duplicate code \"" + code + "\"");
        if (containsValue(value)) throw new IllegalArgumentException("Duplicate value \"" + value + "\"");
        codeToValue.put(code, value);
        valueToCode.put(value, code);
    }

    public boolean containsCode(C code) {
        return codeToValue.containsKey(code);
    }

    public boolean containsValue(T value) {
        return valueToCode.containsKey(value);
    }

    public C getCode(T value) {
        return valueToCode.get(value);
    }

    public T getValue(C code) {
        return codeToValue.get(code);
    }

    @Override
    public CodeValueMap<T, C> clone() {
        return new CodeValueMap<T, C>(this);
    }
}
