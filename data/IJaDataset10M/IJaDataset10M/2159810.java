package net.jsrb.rtl;

public class string {

    /**
     * get string description from errno
     * 
     * @param errno
     *            errno
     * @return error description,if no desc,returns null
     */
    public static native String strerror(int errno);

    public static native void memcpy(long destptr, long srcptr, int len);

    public static native void memset(long ptr, int c, int len);
}
