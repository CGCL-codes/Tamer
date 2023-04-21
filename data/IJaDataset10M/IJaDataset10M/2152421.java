package org.mortbay.util;

/** Byte Array Pool
 * Simple pool for recycling byte arrays of a fixed size.
 *
 * @version $Id: ByteArrayPool.java,v 1.9 2004/05/09 20:32:49 gregwilkins Exp $
 * @author Greg Wilkins (gregw)
 */
public class ByteArrayPool {

    public static final int __POOL_SIZE = Integer.getInteger("org.mortbay.util.ByteArrayPool.pool_size", 8).intValue();

    public static final ThreadLocal __pools = new BAThreadLocal();

    public static int __slot;

    /** Get a byte array from the pool of known size.
     * @param size Size of the byte array.
     * @return Byte array of known size.
     */
    public static byte[] getByteArray(int size) {
        byte[][] pool = (byte[][]) __pools.get();
        boolean full = true;
        for (int i = pool.length; i-- > 0; ) {
            if (pool[i] != null && pool[i].length == size) {
                byte[] b = pool[i];
                pool[i] = null;
                return b;
            } else full = false;
        }
        if (full) for (int i = pool.length; i-- > 0; ) pool[i] = null;
        return new byte[size];
    }

    public static byte[] getByteArrayAtLeast(int minSize) {
        byte[][] pool = (byte[][]) __pools.get();
        for (int i = pool.length; i-- > 0; ) {
            if (pool[i] != null && pool[i].length >= minSize) {
                byte[] b = pool[i];
                pool[i] = null;
                return b;
            }
        }
        return new byte[minSize];
    }

    public static void returnByteArray(final byte[] b) {
        if (b == null) return;
        byte[][] pool = (byte[][]) __pools.get();
        for (int i = pool.length; i-- > 0; ) {
            if (pool[i] == null) {
                pool[i] = b;
                return;
            }
        }
        int s = __slot++;
        if (s < 0) s = -s;
        pool[s % pool.length] = b;
    }

    private static final class BAThreadLocal extends ThreadLocal {

        protected Object initialValue() {
            return new byte[__POOL_SIZE][];
        }
    }
}
