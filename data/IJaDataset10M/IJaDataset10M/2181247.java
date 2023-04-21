package joshua.discriminative.semiring_parsing;

public class AtomicSemiring {

    int ATOMIC_ADD_MODE = 0;

    public static int LOG_SEMIRING = 1;

    public int ATOMIC_SEMIRING = LOG_SEMIRING;

    double ATOMIC_ZERO_IN_SEMIRING = Double.NEGATIVE_INFINITY;

    double ATOMIC_ONE_IN_SEMIRING = 0;

    public AtomicSemiring(int semiring, int add_mode) {
        ATOMIC_ADD_MODE = add_mode;
        ATOMIC_SEMIRING = semiring;
        if (ATOMIC_SEMIRING == LOG_SEMIRING) {
            if (ATOMIC_ADD_MODE == 0) {
                ATOMIC_ZERO_IN_SEMIRING = Double.NEGATIVE_INFINITY;
                ATOMIC_ONE_IN_SEMIRING = 0;
            } else if (ATOMIC_ADD_MODE == 1) {
                System.out.println("unsupported add mode");
                System.exit(0);
                ATOMIC_ZERO_IN_SEMIRING = Double.POSITIVE_INFINITY;
                ATOMIC_ONE_IN_SEMIRING = 0;
            } else if (ATOMIC_ADD_MODE == 2) {
                System.out.println("unsupported add mode");
                System.exit(0);
                ATOMIC_ZERO_IN_SEMIRING = Double.NEGATIVE_INFINITY;
                ATOMIC_ONE_IN_SEMIRING = 0;
            } else {
                System.out.println("invalid add mode");
                System.exit(0);
            }
        } else {
            System.out.println("un-supported semiring");
            System.exit(0);
        }
    }

    public double multi_in_atomic_semiring(double x, double y) {
        if (ATOMIC_SEMIRING == LOG_SEMIRING) {
            return multiInAtomicLogSemiring(x, y);
        } else {
            System.out.println("un-supported semiring");
            System.exit(0);
            return -1;
        }
    }

    public double divide_in_atomic_semiring(double x, double y) {
        if (ATOMIC_SEMIRING == LOG_SEMIRING) {
            return x - y;
        } else {
            System.out.println("un-supported semiring");
            System.exit(0);
            return -1;
        }
    }

    public double add_in_atomic_semiring(double x, double y) {
        if (ATOMIC_SEMIRING == LOG_SEMIRING) {
            return addInAtomicLogSemiring(x, y);
        } else {
            System.out.println("un-supported semiring");
            System.exit(0);
            return -1;
        }
    }

    private static double multiInAtomicLogSemiring(double x, double y) {
        return x + y;
    }

    private double addInAtomicLogSemiring(double x, double y) {
        if (ATOMIC_ADD_MODE == 0) {
            if (x == Double.NEGATIVE_INFINITY) return y;
            if (y == Double.NEGATIVE_INFINITY) return x;
            if (y <= x) return x + Log(1 + Math.exp(y - x)); else return y + Log(1 + Math.exp(x - y));
        } else if (ATOMIC_ADD_MODE == 1) {
            return (x <= y) ? x : y;
        } else if (ATOMIC_ADD_MODE == 2) {
            return (x >= y) ? x : y;
        } else {
            System.out.println("invalid add mode");
            System.exit(0);
            return 0;
        }
    }

    public static double Log(double x) {
        return Math.log(x);
    }
}
