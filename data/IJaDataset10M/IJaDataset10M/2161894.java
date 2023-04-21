package java.lang;

public class VMLock {

    public static native void lock(int id);

    public static native int unlock(int id);
}
