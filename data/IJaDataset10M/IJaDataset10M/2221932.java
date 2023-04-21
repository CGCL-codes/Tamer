package euler;

import com.mycila.Decomposition;
import com.mycila.math.prime.Sieve;
import static java.lang.System.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=47
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem047 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final List<Decomposition> results = new ArrayList<Decomposition>(4);
        final int[] numbers = new int[4];
        Sieve sieve = Sieve.to(100000);
        int max = sieve.last();
        for (int n = 2 * 3 * 5 * 7, consecutive = 0; ; n++) {
            if (n > max) {
                sieve = sieve.grow(100000);
                max = sieve.last();
            }
            Decomposition decomposition = Decomposition.of(n);
            if (decomposition.factorCount() == 4) {
                results.add(consecutive, decomposition);
                numbers[consecutive++] = n;
            } else consecutive = 0;
            if (consecutive == 4) {
                for (int i = 0; i < numbers.length; i++) System.out.println(numbers[i] + ": " + results.get(i));
                out.println(numbers[0] + " in " + (currentTimeMillis() - time) + "ms");
                assertEquals(134043, numbers[0]);
                break;
            }
        }
    }
}
