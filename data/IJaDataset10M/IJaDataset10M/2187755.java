package mmorpho;

public interface Constants {

    public static final int FREE = -1;

    public static final int CIRCLE = 0;

    public static final int DIAMOND = 1;

    public static final int LINE = 2;

    public static final int VLINE = 3;

    public static final int HLINE = 4;

    public static final int CLINE = 5;

    public static final int HPOINTS = 6;

    public static final int VPOINTS = 5;

    public static final int SQARE = 7;

    public static final int RING = 8;

    public static final int[] OFFSET0 = { 0, 0 };

    public static final int[] NGRAD = { 0, 1 };

    public static final int[] SGRAD = { 0, -1 };

    public static final int[] WGRAD = { 1, 0 };

    public static final int[] EGRAD = { -1, 0 };

    public static final int[] NEGRAD = { -1, -1 };

    public static final int[] NWGRAD = { 1, -1 };

    public static final int[] SEGRAD = { -1, -1 };

    public static final int[] SWGRAD = { 1, 1 };

    public static final int MAXSIZE = 151;

    public static final int HPLUS = 1;

    public static final int HMINUS = -1;

    public static final int PERIM = -16;

    public static final int FULLAREA = -32;

    public static final int ERODE = 32, DILATE = 64;

    public static final double cor3 = 1.5 - Math.sqrt(3.0);
}
