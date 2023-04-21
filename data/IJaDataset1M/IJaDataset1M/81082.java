package org.gamio.util;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 20 $ $Date: 2008-10-01 19:37:36 -0400 (Wed, 01 Oct 2008) $
 */
public final class Utility extends Util {

    private static Utility utility = new Utility();

    public static Utility getInstance() {
        return utility;
    }

    public String getFileSeparator() {
        return Helper.getFileSeparator();
    }

    public String getLineSeparator() {
        return Helper.getLineSeparator();
    }

    public char bhToChar(int b) {
        return Helper.bhToChar(b);
    }

    public char blToChar(int b) {
        return Helper.bhToChar(b);
    }

    public StringBuilder getRawStringBuilder() {
        return Helper.getRawStringBuilder();
    }

    public StringBuilder getStringBuilder() {
        return Helper.getStringBuilder();
    }

    public StringBuilder getStringBuilder(int capacity) {
        return Helper.getStringBuilder(capacity);
    }

    public String buildString(Object... args) {
        return Helper.buildString(args);
    }

    public void dumpByte(StringBuilder strBldr, int b) {
        Helper.dumpByte(strBldr, b);
    }

    public void dumpInt16(StringBuilder strBldr, int s) {
        Helper.dumpInt16(strBldr, s);
    }

    public void dumpInt32(StringBuilder strBldr, int i) {
        Helper.dumpInt32(strBldr, i);
    }

    public void dumpInt64(StringBuilder strBldr, long l) {
        Helper.dumpInt64(strBldr, l);
    }

    public void dump(StringBuilder strBldr, byte[] b) {
        Helper.dump(strBldr, b);
    }

    public void dump(StringBuilder strBldr, byte[] b, int offset, int length) {
        Helper.dump(strBldr, b, offset, length);
    }
}
