package arm.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Combinatorics {

    private static final Logger log = Logger.getLogger(Combinatorics.class);

    public static List enumerateCombinations(int numberOfBins, int numberOfBalls, int[] maxArray) {
        int total = 0;
        for (int i = 0; i < maxArray.length; i++) {
            total += maxArray[i];
        }
        if (numberOfBalls > total) {
            throw new RuntimeException("No mappings possible " + "(request to break more bonds than available).");
        }
        return knuth(numberOfBins - 1, numberOfBalls, maxArray);
    }

    public static List enumerateCombinations(int numberOfBins, int numberOfBalls, int maxSize) {
        int total = 0;
        int[] maxArray = new int[numberOfBins];
        for (int i = 0; i < maxArray.length; i++) {
            maxArray[i] = maxSize;
            total += maxSize;
        }
        if (numberOfBalls > total) {
            throw new RuntimeException("No mappings possible " + "(request to break more bonds than available).");
        }
        return knuth(numberOfBins - 1, numberOfBalls, maxArray);
    }

    public static List knuth(int s, int t, int[] m) {
        boolean allowNegatives = false;
        ArrayList al = new ArrayList();
        boolean doQ2 = true;
        boolean skipRest = false;
        int[] q = new int[s + 1];
        int x = t;
        int j = 0;
        do {
            if (doQ2) {
                j = 0;
                while (x > m[j]) {
                    q[j] = m[j];
                    x -= m[j];
                    j++;
                }
                q[j] = x;
            }
            boolean allPositive = true;
            StringBuffer sb = new StringBuffer();
            int[] vector = new int[q.length];
            for (int i = q.length - 1; i >= 0; i--) {
                if (q[i] < 0) {
                    allPositive = false;
                }
                vector[i] = q[i];
                sb.append(q[i]);
                if (i > 0) {
                    sb.append(" + ");
                }
            }
            if (allowNegatives || allPositive) {
                log.debug(sb.toString());
                al.add(vector);
            }
            skipRest = false;
            if (j == 0) {
                x = q[0] - 1;
                j = 1;
            } else if (q[0] == 0) {
                x = q[j] - 1;
                q[j] = 0;
                j++;
            } else {
                while (q[j] == m[j]) {
                    j++;
                    if (j > s) {
                        return al;
                    }
                }
                q[j]++;
                j--;
                q[j]--;
                if (q[0] == 0) {
                    j = 1;
                }
                skipRest = true;
                doQ2 = false;
            }
            if (!skipRest) {
                do {
                    if (j > s) {
                        return al;
                    } else if (q[j] == m[j]) {
                        x += m[j];
                        q[j] = 0;
                        j++;
                    } else {
                        break;
                    }
                } while (true);
                q[j]++;
                if (x == 0) {
                    q[0] = 0;
                    doQ2 = false;
                } else {
                    doQ2 = true;
                }
            }
        } while (true);
    }
}
