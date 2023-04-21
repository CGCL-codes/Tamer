package org.dict.kernel;

import java.io.*;

public abstract class KeyList implements IList {

    private int[] fIndexes;

    private String fEncoding = "utf-8";

    /**
 * KeyList constructor comment.
 */
    protected KeyList() {
        super();
    }

    public IKey createKey(byte[] b, int offset, int length) {
        String s;
        try {
            s = new String(b, offset, length, getEncoding()).trim();
        } catch (Exception e) {
            s = new String(b, offset, length).trim();
        }
        int first = s.indexOf(IKey.TAB);
        int last = s.indexOf(IKey.TAB, first + 1);
        if (first < 0 || last < 0) {
            return new Key(s, "A", "A");
        }
        String k = s.substring(0, first);
        String off = s.substring(first + 1, last);
        String len = s.substring(last + 1);
        return new Key(k, off, len);
    }

    public abstract Object get(int index);

    public static byte[] getData(String fileName) throws IOException {
        return DictEngine.getData(fileName);
    }

    /**
 * Insert the method's description here.
 * Creation date: (03.09.01 22:33:10)
 * @return java.lang.String
 */
    public java.lang.String getEncoding() {
        return fEncoding;
    }

    /**
 * Insert the method's description here.
 * Creation date: (22.06.2001 10:34:16)
 * @return int[]
 */
    public int[] getIndexes() {
        return fIndexes;
    }

    public static int[] getLineMarkers(byte[] b) {
        java.util.Vector ls = new java.util.Vector(50000);
        ls.addElement(new Integer(0));
        for (int i = 0; i < b.length - 2; i++) {
            if (b[i] == '\n') {
                ls.addElement(new Integer(i + 1));
            }
        }
        int[] arr = new int[ls.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ((Integer) ls.elementAt(i)).intValue();
        }
        ls.removeAllElements();
        ls = null;
        return arr;
    }

    /**
 * Insert the method's description here.
 * Creation date: (03.09.01 22:33:10)
 * @param newEncoding java.lang.String
 */
    public void setEncoding(java.lang.String newEncoding) {
        fEncoding = newEncoding;
    }

    /**
 * Insert the method's description here.
 * Creation date: (22.06.2001 10:34:16)
 * @param newIndexes int[]
 */
    public void setIndexes(int[] newIndexes) {
        fIndexes = newIndexes;
    }

    public void shutDown() {
    }

    /**
	 * Returns the number of elements in this collection.  If this collection
	 * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @return the number of elements in this collection
	 */
    public int size() {
        return getIndexes().length;
    }

    public void startUp() {
    }
}
