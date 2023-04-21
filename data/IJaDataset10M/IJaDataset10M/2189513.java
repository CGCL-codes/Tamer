package com.javabi.sizeof.definition;

import java.io.PrintStream;
import java.util.Set;
import com.javabi.sizeof.ClassDefinition;
import com.javabi.sizeof.ClassDefinitionMap;

/**
 * A Boolean Array Definition.
 */
public final class BooleanArrayDefinition extends ClassDefinition<boolean[]> {

    @Override
    public long sizeOf(boolean[] array) {
        return sizeOfByteArray(array.length);
    }

    @Override
    public long sizeOfDebug(boolean[] array, ClassDefinitionMap definitionMap, Set<Object> instanceSet, PrintStream stream) throws IllegalAccessException {
        long arraySize = sizeOfByteArray(array.length);
        stream.println("new boolean[" + array.length + "] " + arraySize + " bytes");
        return arraySize;
    }
}
