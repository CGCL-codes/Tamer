package org.sgutil.storage;

/**
 * DATE			PROGRAMMER		NOTES
 * ----------------------------------------------------------------------------
 * 2010-10-22	WM				Created
 * 
 */
public class Hash {

    public static int size = 32;

    byte[] bytes = new byte[size];

    public Hash(byte[] initBytes) throws Exception {
        if (size != initBytes.length) {
            throw new Exception();
        } else {
            System.arraycopy(initBytes, 0, bytes, 0, size);
        }
    }

    public byte[] getBytes() {
        return bytes;
    }
}
