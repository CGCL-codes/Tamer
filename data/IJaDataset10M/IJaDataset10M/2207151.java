package com.javabi.sizeof.definition;

import java.io.PrintStream;
import java.util.Set;
import com.javabi.sizeof.ClassDefinition;
import com.javabi.sizeof.ClassDefinitionMap;

/**
 * A Float Array Definition.
 */
public final class FloatArrayDefinition extends ClassDefinition<float[]> {

    @Override
    public long sizeOf(float[] array) {
        return sizeOfIntArray(array.length);
    }

    @Override
    public long sizeOfDebug(float[] array, ClassDefinitionMap definitionMap, Set<Object> instanceSet, PrintStream stream) throws IllegalAccessException {
        long arraySize = sizeOfIntArray(array.length);
        stream.println("new float[" + array.length + "] " + arraySize + " bytes");
        return arraySize;
    }
}
