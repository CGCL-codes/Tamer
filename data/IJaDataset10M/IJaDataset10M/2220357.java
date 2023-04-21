package tinybase.sm;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.junit.Assert;
import tinybase.basic.RedBase;

public class SM_RelcatRec {

    public String relName;

    int tupleLength;

    int attrCount;

    int indexCount;

    SM_RelcatRec(String _relName, int _tupleLength, int _attrCount, int _indexCount) {
        relName = new String(_relName);
        tupleLength = _tupleLength;
        attrCount = _attrCount;
        indexCount = _indexCount;
    }

    public SM_RelcatRec() {
        relName = null;
        tupleLength = 0;
        attrCount = 0;
        indexCount = 0;
    }

    public static void SM_SetRelcatRec(SM_RelcatRec r, String _relName, int _tupleLength, int _attrCount, int _indexCount) {
        r.relName = new String(_relName);
        r.tupleLength = _tupleLength;
        r.attrCount = _attrCount;
        r.indexCount = _indexCount;
    }

    public static int getRelNameOffset() {
        return 0;
    }

    public static int getTupleLengthOffset() {
        return RedBase.MAXNAME;
    }

    public static int getAttrCountOffset() {
        return RedBase.MAXNAME + 4;
    }

    public static int getIndexCountOffset() {
        return RedBase.MAXNAME + 4 + 4;
    }

    public static int getSM_RelcatRecSize() {
        return RedBase.MAXNAME + 4 + 4 + 4;
    }

    /**
	 * store the relName member in the container
	 * 
	 * @param container
	 *            : out, the container of result
	 * @param start
	 *            : start index in the container
	 * @return the real length of the name
	 */
    public int getRelNameToByteArray(byte[] container, int start) {
        Assert.assertTrue((container.length - start) > RedBase.MAXNAME);
        Arrays.fill(container, start, start + RedBase.MAXNAME, (byte) 0);
        int length = Math.min(relName.length(), RedBase.MAXNAME);
        System.arraycopy(relName.getBytes(Charset.forName("ASCII")), 0, container, start, length);
        return length;
    }

    public void storeAsBytes(ByteBuffer buf) {
        Assert.assertTrue(buf.capacity() >= getSM_RelcatRecSize());
        buf.clear();
        buf.put(relName.getBytes(Charset.forName("ASCII")), 0, Math.min(relName.length(), RedBase.MAXNAME));
        buf.putInt(this.getTupleLengthOffset(), tupleLength);
        buf.putInt(this.getAttrCountOffset(), attrCount);
        buf.putInt(this.getIndexCountOffset(), indexCount);
    }
}
