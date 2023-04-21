package com.tapina.robe.swi.clib.stdlib;

import com.tapina.robe.runtime.CodeBlock;
import com.tapina.robe.runtime.Environment;
import com.tapina.robe.swi.clib.Stub;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This function sorts an array of a2 objects, each of which is a3 bytes long, and whose first object is pointed
 * to by a1 into ascending order.
 * The function a4 which must be defined by you is called with pointers to objects. If the first is less than, equal
 * to, or greater than the second then the function should return less than, equal to, or greater than zero.
 */
public class QSort extends Stub {

    public void executeStub(final Environment environment) {
        final int R[] = environment.getCpu().R;
        final int numObjects = R[1];
        final int objectSize = R[2];
        final int baseAddress = R[0];
        final int functionAddress = R[3];
        final Integer[] objectAddresses = new Integer[numObjects];
        for (int i = 0; i < numObjects; i++) {
            objectAddresses[i] = new Integer(baseAddress + i * objectSize);
        }
        final CodeBlock function = environment.getMemoryMap().addEntryPoint(functionAddress);
        final Comparator comparator = new Comparator() {

            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                R[0] = i1.intValue();
                R[1] = i2.intValue();
                executeSubroutine(environment, function);
                return R[0];
            }
        };
        Arrays.sort(objectAddresses, comparator);
        final byte[][] output = new byte[numObjects][];
        for (int i = 0; i < numObjects; i++) {
            final int objectAddress = objectAddresses[i].intValue();
            output[i] = environment.getMemoryMap().getBytes(objectAddress, objectSize);
        }
        for (int i = 0; i < output.length; i++) {
            final int outputAddress = baseAddress + i * objectSize;
            environment.getMemoryMap().storeBytes(outputAddress, output[i], 0, output[i].length);
        }
    }
}
