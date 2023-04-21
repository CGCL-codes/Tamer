package test.unit.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class MockParameterizedType implements ParameterizedType {

    private Type[] actualTypeArguments;

    private Type rawType;

    public MockParameterizedType(Type rawType, Type... actualTypeArguments) {
        super();
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments;
    }

    public Type getOwnerType() {
        return null;
    }

    public Type getRawType() {
        return this.rawType;
    }

    public String toString() {
        return "MockParameterizedType [rawType=" + rawType + ", actualTypeArguments=" + Arrays.toString(actualTypeArguments) + "]";
    }
}
