package o350;

/**
 * 
 * 
 * @author Yegor Chemisov
 */
public class Main {

    private static final int NUMBERS_QUANTITY = 5;

    private static final int MAX_SPINS = 200;

    private static final int ROULETTE_COUNT = 37;

    static double probs[][] = new double[NUMBERS_QUANTITY][MAX_SPINS];

    static double win_probs[] = new double[MAX_SPINS];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < NUMBERS_QUANTITY; i++) {
            probs[i][0] = (i + 1.) / ROULETTE_COUNT;
            double x = (ROULETTE_COUNT - i - 1.) / ROULETTE_COUNT;
            for (int spin = 1; spin < MAX_SPINS; spin++) probs[i][spin] = probs[i][spin - 1] * x;
        }
        for (int spin = 0; spin < MAX_SPINS; spin++) {
            win_probs[spin] = 0;
            for (int n1 = 0; n1 <= spin; n1++) for (int n2 = 0; n2 <= spin - n1; n2++) for (int n3 = 0; n3 <= spin - n1 - n2; n3++) for (int n4 = 0; n4 <= spin - n1 - n2 - n3; n4++) win_probs[spin] += probs[0][n1] * probs[1][n2] * probs[2][n3] * probs[3][n4] * probs[4][spin - n1 - n2 - n3 - n4];
        }
        double x = 0;
        for (int spin = 0; spin < MAX_SPINS; spin++) {
            x += win_probs[spin];
            System.out.print(win_probs[spin]);
            System.out.print("\t");
            System.out.println(x);
        }
    }
}
