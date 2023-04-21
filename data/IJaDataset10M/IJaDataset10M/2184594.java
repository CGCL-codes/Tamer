package uk.ac.bath.machine.neural3;

import uk.ac.bath.util.MyRandom;

/**
 *
 * @author pjl
 */
class SimpFloatList implements MyFloatList {

    float f[];

    public SimpFloatList(int n, float min, float max, float inc) {
        f = new float[n];
        for (int i = 0; i < n; i++) {
            f[i] = (float) (min + (max - min) * MyRandom.nextDouble());
            f[i] = (int) (f[i] / inc) * inc;
        }
    }

    public float floatAt(int i) {
        return f[i];
    }
}
