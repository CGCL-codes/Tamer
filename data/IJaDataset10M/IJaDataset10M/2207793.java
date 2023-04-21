package euler;

/**
 * http://projecteuler.net/index.php?section=problems&id=31
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem031 {

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        int count = 2;
        for (int b = 0; b <= 1; b++) for (int c = 0; 100 * b + 50 * c <= 200; c++) for (int d = 0; 100 * b + 50 * c + 20 * d <= 200; d++) for (int e = 0; 100 * b + 50 * c + 20 * d + 10 * e <= 200; e++) for (int f = 0; 100 * b + 50 * c + 20 * d + 10 * e + 5 * f <= 200; f++) for (int g = 0; 100 * b + 50 * c + 20 * d + 10 * e + 5 * f + 2 * g <= 200; g++) count++;
        System.out.println(count + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
