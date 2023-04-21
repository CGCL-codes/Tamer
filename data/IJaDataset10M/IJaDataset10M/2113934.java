package org.datascooter.test.hibernate;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class TestUtils {

    private static int fSAMPLE_SIZE = 100;

    private static long fSLEEP_INTERVAL = 100;

    public static int sizeOf(Object obj) throws java.io.IOException {
        ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        byteObject.close();
        return byteObject.toByteArray().length;
    }

    public static long getObjectSize(Class<?> aClass) {
        long result = 0;
        try {
            aClass.getConstructor(new Class[] {});
        } catch (NoSuchMethodException ex) {
            System.err.println(aClass + " does not have a no-argument constructor.");
            return result;
        }
        Object[] objects = new Object[fSAMPLE_SIZE];
        try {
            long startMemoryUse = getMemoryUse();
            for (int idx = 0; idx < objects.length; ++idx) {
                objects[idx] = aClass.newInstance();
            }
            long endMemoryUse = getMemoryUse();
            float approximateSize = (endMemoryUse - startMemoryUse) / fSAMPLE_SIZE;
            result = Math.round(approximateSize);
        } catch (Exception ex) {
            System.err.println("Cannot create object using " + aClass);
        }
        return result;
    }

    public static long getMemoryUse() {
        putOutTheGarbage();
        long totalMemory = Runtime.getRuntime().totalMemory();
        putOutTheGarbage();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }

    public static void putOutTheGarbage() {
        collectGarbage();
        collectGarbage();
    }

    public static void collectGarbage() {
        try {
            System.gc();
            Thread.sleep(fSLEEP_INTERVAL);
            System.runFinalization();
            Thread.sleep(fSLEEP_INTERVAL);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
