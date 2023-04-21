package rossi.dfp.calc;

/**
 *
 */
public class RpnCalcKeys {

    public static final short SHIFT_RESET = -3;

    public static final short SHIFT_G = -2;

    public static final short SHIFT_F = -1;

    public static final short ZERO = 0;

    public static final short ONE = 1;

    public static final short TWO = 2;

    public static final short THREE = 3;

    public static final short FOUR = 4;

    public static final short FIVE = 5;

    public static final short SIX = 6;

    public static final short SEVEN = 7;

    public static final short EIGHT = 8;

    public static final short NINE = 9;

    public static final short DECIMAL = 10;

    public static final short CHS = 11;

    public static final short EEX = 12;

    public static final short E = 14;

    public static final short PI = 15;

    public static final short DIV = 16;

    public static final short MUL = 17;

    public static final short SUB = 18;

    public static final short ADD = 19;

    public static final short POW = 20;

    public static final short LOG = 21;

    public static final short POW10 = 22;

    public static final short LN = 23;

    public static final short POWE = 24;

    public static final short SQRT = 25;

    public static final short SQR = 26;

    public static final short SIN = 27;

    public static final short ASIN = 28;

    public static final short COS = 29;

    public static final short ACOS = 30;

    public static final short TAN = 31;

    public static final short ATAN = 32;

    public static final short RECIP = 33;

    public static final short FACTORIAL = 34;

    public static final short SWAP = 35;

    public static final short ROLL = 36;

    public static final short LASTX = 37;

    public static final short STO = 38;

    public static final short RCL = 39;

    public static final short ENTER = 40;

    public static final short CLX = 41;

    public static final short MEMORY_DIV = 45;

    public static final short MEMORY_MUL = 46;

    public static final short MEMORY_SUB = 47;

    public static final short MEMORY_ADD = 48;

    /** Hyperbolic SIN */
    public static final short SINH = 50;

    /** Inverse Hyperbolic SIN */
    public static final short ASINH = 51;

    public static final short COSH = 52;

    public static final short ACOSH = 53;

    public static final short TANH = 54;

    public static final short ATANH = 55;

    public static final short FINANCIAL_N = 60;

    public static final short FINANCIAL_I = 61;

    public static final short FINANCIAL_PV = 62;

    public static final short FINANCIAL_PMT = 63;

    public static final short FINANCIAL_FV = 64;

    public static final short FINANCIAL_N_TIMES_12 = 65;

    public static final short FINANCIAL_I_DIV_12 = 66;

    /** Clear Financial registers */
    public static final short FINANCIAL_CLR = 67;

    /** Programming stuff */
    public static final short PROGRAM_LABEL_A = 80;

    public static final short PROGRAM_LABEL_B = 81;

    public static final short PROGRAM_LABEL_C = 82;

    public static final short PROGRAM_LABEL_D = 83;

    public static final short PROGRAM_LABEL_E = 84;

    public static final short PROGRAM_LABEL_F = 85;

    public static boolean isDigit(short key) {
        return (key >= ZERO && key <= NINE);
    }

    public static boolean isEntryKey(short key) {
        return (isDigit(key) || key == DECIMAL || key == CHS || key == EEX || key == CLX);
    }

    public static boolean isShiftKey(short key) {
        return (key == SHIFT_F || key == SHIFT_G || key == SHIFT_RESET);
    }

    public static boolean isFinancialKey(short key) {
        return (key >= FINANCIAL_N && key <= FINANCIAL_CLR);
    }

    public static boolean isConstantKey(short key) {
        return (key == PI || key == E);
    }

    public static char getDigitChar(short key) {
        if (isDigit(key)) {
            return (char) ('0' + key);
        }
        return ' ';
    }
}
