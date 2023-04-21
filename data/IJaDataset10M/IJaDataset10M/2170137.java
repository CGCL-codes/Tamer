package jweslley.ContestVolumes.VolumeCV;

import java.math.BigInteger;
import java.util.Scanner;
import jweslley.Problem;
import jweslley.Problem.Status;

/**
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/105/10579.html
 *
 * @author  Jonhnny Weslley
 * @version 1.00, 19/10/2008
 */
@Problem(Status.Accepted)
public class FibonacciNumbers {

    public static void main(String[] args) {
        StringBuilder out = new StringBuilder();
        Scanner in = new Scanner(System.in);
        int n;
        while (in.hasNext()) {
            n = in.nextInt();
            out.append(fibb(n)).append('\n');
        }
        System.out.print(out);
    }

    private static Object fibb(int n) {
        if (n <= 2) {
            return 1;
        }
        BigInteger f = new BigInteger("0");
        BigInteger g = new BigInteger("1");
        for (int i = 1; i <= n; i++) {
            f = f.add(g);
            g = f.subtract(g);
        }
        return f;
    }
}
