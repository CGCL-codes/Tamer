package com.trilead.ssh2.compression;

/**
 * @author Kenny Root
 * 
 */
public interface ICompressor {

    int getBufferSize();

    int compress(byte[] buf, int start, int len, byte[] output);

    byte[] uncompress(byte[] buf, int start, int[] len);
}
