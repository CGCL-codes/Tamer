package algo;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: Apr 4, 2010
 * Time: 8:56:14 AM
 */
public class Recursion {

    private int n;

    private int k;

    void generate(int n, int k) {
        this.n = n;
        this.k = k;
        innerGenerate("");
    }

    private void innerGenerate(String prefix) {
        if (n == prefix.length()) {
            System.out.println("a = " + prefix);
        } else if (prefix.length() < n) {
            for (int i = 0; i < k; i++) {
                innerGenerate(prefix + i);
            }
        }
    }

    public static void main(String[] args) {
        new Recursion().generate(2, 3);
        new Recursion().generate(4, 6);
    }
}
