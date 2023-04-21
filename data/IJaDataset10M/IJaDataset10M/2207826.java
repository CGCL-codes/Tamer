package com.javabi.sizeof.definition;

import java.io.PrintStream;
import java.util.Set;
import com.javabi.sizeof.ClassDefinition;
import com.javabi.sizeof.ClassDefinitionMap;

/**
 * A Long Array Definition.
 */
public final class LongArrayDefinition extends ClassDefinition<long[]> {

    @Override
    public long sizeOf(long[] array) {
        return sizeOfLongArray(array.length);
    }

    @Override
    public long sizeOfDebug(long[] array, ClassDefinitionMap definitionMap, Set<Object> instanceSet, PrintStream stream) throws IllegalAccessException {
        long arraySize = sizeOfLongArray(array.length);
        stream.println("new long[" + array.length + "] " + arraySize + " bytes");
        return arraySize;
    }
}
