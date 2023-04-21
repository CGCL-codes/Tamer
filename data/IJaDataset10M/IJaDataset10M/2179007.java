package com.gc.iotools.stream.store;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

public class ThresholdStorageTest {

    static int getTmpFileNum() {
        final File tmpDir1 = new File(System.getProperty("java.io.tmpdir"));
        int result = 0;
        final File[] files = tmpDir1.listFiles();
        for (final File file : files) {
            final String name = file.getName();
            if (name.matches("iotools-storage.*tmp")) {
                result++;
            }
        }
        return result;
    }

    @Test
    public void seek() throws IOException {
        final ThresholdStore tss = new ThresholdStore(20);
        final byte[] ref1 = new byte[140];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        tss.seek(20);
        final byte[] read = new byte[20];
        tss.get(read, 0, read.length);
        assertArrayEquals("lettura corretta", ArrayUtils.subarray(ref1, 20, 40), read);
        tss.seek(0);
        tss.get(read, 0, read.length);
        assertArrayEquals("lettura corretta", ArrayUtils.subarray(ref1, 0, 20), read);
        tss.cleanup();
    }

    private void seekEqualsReference(final ThresholdStore t, final byte[] reference) throws IOException {
        for (int i = 0; i < reference.length; i++) {
            final byte b = reference[i];
            final byte[] read = new byte[1];
            t.seek(i);
            t.get(read, 0, 1);
            assertEquals("position [" + i + "]", b, read[0]);
        }
    }

    @Before
    public void setUp() throws Exception {
        final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        final File[] files = tmpDir.listFiles();
        for (final File file : files) {
            final String name = file.getName();
            if (name.matches("iotools-storage.*tmp") && file.isFile()) {
                final boolean canDelete = file.delete();
                if (!canDelete) {
                    System.gc();
                    System.runFinalization();
                    Thread.sleep(2000);
                    final boolean canDelete2 = file.delete();
                    if (!canDelete2) {
                        final File dest = File.createTempFile("iotools-movedstorage", ".tmp");
                        if (!file.renameTo(dest)) {
                            throw new IOException("Can't delete or move file [" + name + "]");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testCleanup() throws IOException {
        final ThresholdStore tss = new ThresholdStore(20);
        final byte[] ref1 = new byte[140];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        tss.cleanup();
        final byte[] read = new byte[20];
        int n = tss.get(ref1, 0, read.length);
        assertEquals("read " + tss, -1, n);
        assertEquals("temporary files after cleanup", 0, getTmpFileNum());
        tss.put(ref1, 0, ref1.length);
        final byte[] read1 = new byte[280];
        int pos = 0;
        while ((n = tss.get(read1, pos, (read1.length - pos))) >= 0) {
            pos += n;
        }
        assertEquals("read", ref1.length, pos);
        assertArrayEquals("arrays equal", ref1, ArrayUtils.subarray(read1, 0, ref1.length));
        tss.cleanup();
    }

    @Test
    public void testGetOverTreshold() throws IOException {
        final ThresholdStore tss = new ThresholdStore(150);
        final byte[] ref1 = new byte[140];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        assertEquals("temporary files", 0, getTmpFileNum());
        tss.put(ref1, 0, ref1.length);
        int n;
        int pos = 0;
        final byte[] read = new byte[280];
        while ((n = tss.get(read, pos, (read.length - pos))) > 0) {
            pos += n;
        }
        assertArrayEquals("letti = scritti", ArrayUtils.addAll(ref1, ref1), read);
        assertEquals("temporary files", 1, getTmpFileNum());
        tss.cleanup();
        assertEquals("temporary files after cleanup", 0, getTmpFileNum());
    }

    @Test
    public void testGetPositionAcrossThreshold() throws IOException {
        final ThresholdStore tss = new ThresholdStore(150);
        final byte[] ref1 = new byte[130];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        tss.get(new byte[5], 0, 5);
        tss.put(ref1, 0, ref1.length);
        for (int i = 0; i < (ref1.length - 5); i++) {
            final byte b = ref1[i + 5];
            final byte[] read = new byte[1];
            tss.get(read, 0, 1);
            assertEquals("position [" + i + "]", b, read[0]);
        }
        tss.cleanup();
    }

    @Test
    public void testPutMoreThanTreshold() throws IOException {
        final ThresholdStore tss = new ThresholdStore(50);
        final byte[] ref1 = new byte[130];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        seekEqualsReference(tss, ref1);
        assertEquals("get over eof", -1, tss.get(new byte[10], 0, 10));
        seekEqualsReference(tss, ref1);
        tss.cleanup();
    }

    @Test
    public void testPutTwiceLessThanTreshold() throws IOException {
        final ThresholdStore tss = new ThresholdStore(50);
        final byte[] ref1 = new byte[25];
        final Random r = new Random();
        r.nextBytes(ref1);
        tss.put(ref1, 0, ref1.length);
        tss.put(ref1, 0, ref1.length);
        seekEqualsReference(tss, ArrayUtils.addAll(ref1, ref1));
        tss.cleanup();
    }

    @Test
    public void testRandomAccess() throws IOException {
        final byte[] ref1 = new byte[1024 * 8];
        final Random r = new Random();
        r.nextBytes(ref1);
        for (int i = 0; i < 1024; i++) {
            final ThresholdStore tss = new ThresholdStore(1024);
            for (final byte element : ref1) {
                tss.put(new byte[] { element }, 0, 1);
            }
            final int pos = r.nextInt(ref1.length - 1);
            tss.seek(pos);
            final byte[] read = new byte[128];
            final int rl = tss.get(read, 0, read.length);
            final byte[] ref = new byte[128];
            System.arraycopy(ref1, pos, ref, 0, rl);
            assertArrayEquals("array equals pos[" + pos + "]", ref, read);
            tss.cleanup();
        }
    }
}
