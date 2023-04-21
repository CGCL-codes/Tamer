package euler;

import com.mycila.math.number.BigInt;
import static java.lang.System.*;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=55
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem055 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        final int maxNumber = 10000;
        final int maxIterations = 50;
        final Set<BigInt> lychrel = new TreeSet<BigInt>();
        final Set<BigInt> nonLychrel = new TreeSet<BigInt>();
        final Set<BigInt> stack = new TreeSet<BigInt>();
        for (int n = 0; n < maxNumber; n++) {
            BigInt test = BigInt.big(n);
            if (lychrel.contains(test)) continue;
            stack.add(test);
            BigInt reverse = test.digitsReversed();
            for (int it = 1; it < maxIterations; it++) {
                test = test.add(reverse);
                reverse = test.digitsReversed();
                stack.add(test);
                if (test.equals(reverse) || nonLychrel.contains(test)) {
                    nonLychrel.addAll(stack);
                    stack.clear();
                    break;
                } else if (lychrel.contains(test)) {
                    lychrel.addAll(stack);
                    stack.clear();
                    break;
                }
            }
            if (!stack.isEmpty()) {
                lychrel.addAll(stack);
                stack.clear();
            }
        }
        System.out.println(lychrel);
        out.println(lychrel.size() + " in " + (currentTimeMillis() - time) + "ms");
        int count = 0;
        BigInt max = BigInt.big(maxNumber);
        for (BigInt l : lychrel) if (l.compareTo(max) <= 0) count++;
        out.println(count + " under " + maxNumber);
        assertEquals(249, count);
        assertEquals(10000, maxNumber);
    }
}
