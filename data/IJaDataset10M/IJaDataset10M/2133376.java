package test;

import java.nio.*;

/**
 * @author sascha
 *
 */
public class BufferTest {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 10000);
        FloatBuffer fb = buffer.asFloatBuffer();
        float[] fa = new float[fb.capacity()];
        float[] fa2 = new float[fb.capacity()];
        long t;
        copy(fa, fb);
        put(fb);
        t = System.currentTimeMillis();
        copy(fa, fb);
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        put(fb);
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void put(FloatBuffer fb) {
        for (int i = 0; i < 1000; i++) {
            fb.rewind();
            for (int j = 0; j < 10000; j++) {
                fb.put(j);
            }
        }
    }

    private static void copy(float[] fa, FloatBuffer fb) {
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 10000; j++) {
                fa[j] = j;
            }
            fb.rewind();
            fb.put(fa);
        }
    }
}
