package p500.srm512;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 7/13/11
 * Time: 7:08 PM
 */
public class MarbleDecoration {

    public int maxLength(int R, int G, int B) {
        int maxOne = calc(R, G);
        int maxTwo = calc(R, B);
        int maxThree = calc(G, B);
        return Math.max(maxOne, Math.max(maxTwo, maxThree));
    }

    private int calc(int g, int b) {
        int m0 = Math.min(g, b);
        int m1 = Math.max(g, b);
        if (m0 == m1) {
            return m0 * 2;
        }
        return m0 * 2 + 1;
    }

    public static void main(String[] args) {
        System.out.println(new MarbleDecoration().maxLength(5, 1, 2) == 5);
    }
}
