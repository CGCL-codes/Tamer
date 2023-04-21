package com.volantis.mock.samples;

/**
 * @mock.generate
 */
public interface InterfaceWithArrayClash {

    void abc(int[] a);

    int abc(float[] a);
}
