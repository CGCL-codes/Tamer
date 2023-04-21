package se.uu.it.cats.brick.filter;

import java.util.Random;

/**
 * Fixed point math
 * 
 * @version $Rev$
 */
public final class Fixed {

    public static final int FIXED_POINT = 20;

    public static final int ONE = 1 << FIXED_POINT;

    public static final int HALF = ONE >> 1;

    public static final int PI = (int) (Math.PI * ONE);

    private static final int TRIG_SHIFT = 30;

    public static final int QUARTER_CIRCLE = 512;

    public static final int CIRCLE_MASK = QUARTER_CIRCLE * 4 - 1;

    /** Pseudo degrees on a circle */
    public static final int DEGREES = 4 * QUARTER_CIRCLE;

    public static final int RADIANS_TO_DEGREES = (int) ((float) (QUARTER_CIRCLE * 2 * ONE) / Math.PI);

    private static final int SIN_PRECALC = 3294193;

    private static final int COS_PRECALC = 2147473542;

    private static final int[] SIN_TABLE = new int[QUARTER_CIRCLE + 1];

    private static Random rng = new Random(45765848);

    static {
        SIN_TABLE[0] = 0;
        SIN_TABLE[1] = SIN_PRECALC;
        for (int n = 2; n < QUARTER_CIRCLE + 1; n++) {
            SIN_TABLE[n] = (int) (((long) SIN_TABLE[n - 1] * COS_PRECALC) >> TRIG_SHIFT) - SIN_TABLE[n - 2];
        }
        for (int n = 0; n < QUARTER_CIRCLE + 1; n++) {
            SIN_TABLE[n] = SIN_TABLE[n] + (1 << (TRIG_SHIFT - FIXED_POINT - 1)) >> TRIG_SHIFT - FIXED_POINT;
        }
    }

    private static final int STRING_DECIMAL_PLACES = 4;

    private static final int STRING_DECIMAL_PLACES_ROUND;

    static {
        int i = 10;
        for (int n = 1; n < STRING_DECIMAL_PLACES; n++) {
            i *= i;
        }
        STRING_DECIMAL_PLACES_ROUND = ONE / (2 * i);
    }

    private Fixed() {
    }

    public static int intToFixed(int n) {
        return n << FIXED_POINT;
    }

    public static int floatToFixed(float x) {
        return (int) (x * ONE);
    }

    public static int floatToFixed(double x) {
        return (int) (x * ONE);
    }

    public static float fixedToFloat(int x) {
        return (float) ((float) x) / ONE;
    }

    public static String toString(int n) {
        StringBuffer sb = new StringBuffer(16);
        sb.append((n += STRING_DECIMAL_PLACES_ROUND) >> FIXED_POINT);
        sb.append('.');
        n &= ONE - 1;
        for (int i = 0; i < STRING_DECIMAL_PLACES; i++) {
            n *= 10;
            sb.append((n / ONE) % 10);
        }
        return sb.toString();
    }

    public static int mul(int a, int b) {
        return (int) ((long) a * (long) b >> FIXED_POINT);
    }

    public static int div(int a, int b) {
        return (int) (((long) a << FIXED_POINT * 2) / (long) b >> FIXED_POINT);
    }

    public static int sin(int n) {
        n &= CIRCLE_MASK;
        if (n < QUARTER_CIRCLE * 2) {
            if (n < QUARTER_CIRCLE) {
                return SIN_TABLE[n];
            } else {
                return SIN_TABLE[QUARTER_CIRCLE * 2 - n];
            }
        } else {
            if (n < QUARTER_CIRCLE * 3) {
                return -SIN_TABLE[n - QUARTER_CIRCLE * 2];
            } else {
                return -SIN_TABLE[QUARTER_CIRCLE * 4 - n];
            }
        }
    }

    public static int cos(int n) {
        n &= CIRCLE_MASK;
        if (n < QUARTER_CIRCLE * 2) {
            if (n < QUARTER_CIRCLE) {
                return SIN_TABLE[QUARTER_CIRCLE - n];
            } else {
                return -SIN_TABLE[n - QUARTER_CIRCLE];
            }
        } else {
            if (n < QUARTER_CIRCLE * 3) {
                return -SIN_TABLE[QUARTER_CIRCLE * 3 - n];
            } else {
                return SIN_TABLE[n - QUARTER_CIRCLE * 3];
            }
        }
    }

    public static int hyp(int x1, int y1, int x2, int y2) {
        if ((x2 -= x1) < 0) {
            x2 = -x2;
        }
        if ((y2 -= y1) < 0) {
            y2 = -y2;
        }
        return x2 + y2 - (((x2 > y2) ? y2 : x2) >> 1);
    }

    /**
	 * Fixed point square root
	 * 
	 * @param n
	 *            Fixed point number to find square root of.
	 * @return The square root as fixed point number
	 */
    public static int sqrt(int n) {
        if (n <= 0) {
            return 0;
        }
        long sum = 0;
        int bit = 0x40000000;
        while (bit >= 0x10) {
            long tmp = sum | bit;
            if (n >= tmp) {
                n -= tmp;
                sum = tmp + bit;
            }
            bit >>= 1;
            n <<= 1;
        }
        return (int) (sum >> 16 - (FIXED_POINT / 2));
    }

    public static int norm(int a, int b) {
        return sqrt(mul(a, a) + mul(b, b));
    }

    public static int abs(int n) {
        return (n < 0) ? -n : n;
    }

    public static int sgn(int n) {
        return (n < 0) ? -1 : 1;
    }

    public static int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static int wrap(int n, int limit) {
        return ((n %= limit) < 0) ? limit + n : n;
    }

    public static int round(int n) {
        return n + HALF >> FIXED_POINT;
    }

    public static int floor(int n) {
        return n >> FIXED_POINT;
    }

    public static int ceil(int n) {
        return n + (ONE - 1) >> FIXED_POINT;
    }

    /**
	 * Gives a random fixed point integer between 0 and Fixed.ONE
	 * 
	 * @return Fixed random number
	 */
    public static int rand() {
        return rng.nextInt() >>> (32 - FIXED_POINT);
    }

    public static int rand(int n) {
        return (rand() * n);
    }

    public static int randn() {
        return floatToFixed(rng.nextGaussian());
    }
}
