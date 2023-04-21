package net.meshtest;

public class IntegerHelper {

    public static int setFlag(int dest, int flag, boolean flagValue) {
        if (flagValue) {
            return dest |= flag;
        } else {
            return dest &= (~flag);
        }
    }
}
