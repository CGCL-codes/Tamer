package dot.junit.opcodes.neg_long.d;

public class T_neg_long_2 {

    public boolean run(long d) {
        return -d == (~d + 1);
    }
}
