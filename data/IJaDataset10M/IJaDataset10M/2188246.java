package nanovm.avr;

public class Timer {

    public static final int STOPPED = 0;

    public static final int DIV1 = 1;

    public static final int DIV8 = 2;

    public static final int DIV64 = 3;

    public static final int DIV128 = 4;

    public static final int DIV1024 = 5;

    public static native void setSpeed(int speed);

    public static native int get();

    public static native void wait(int ticks);

    public static native void setPrescaler(int value);
}
