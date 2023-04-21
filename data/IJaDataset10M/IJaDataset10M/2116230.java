package p400.srm495;

import java.util.Arrays;
import utils.ExampleRunner;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 3/28/11
 * Time: 3:15 PM
 * with help
 */
public class ColorfulCards {

    public int[] theCards(int n, String cs) {
        int m = cs.length();
        int[] min = new int[m];
        int[] max = new int[m];
        boolean[] prime = initPrime(n);
        for (int i = 0, j = 1; i < m; ++i) {
            while ((cs.charAt(i) == 'B') == prime[j]) {
                j++;
            }
            min[i] = j++;
        }
        for (int i = m - 1, j = n; i >= 0; --i) {
            while ((cs.charAt(i) == 'B') == prime[j]) {
                --j;
            }
            max[i] = j--;
        }
        int[] ans = new int[m];
        for (int i = 0; i < m; ++i) {
            if (min[i] == max[i]) {
                ans[i] = min[i];
            } else {
                ans[i] = -1;
            }
        }
        return ans;
    }

    private boolean[] initPrime(int N) {
        boolean prime[] = new boolean[N + 1];
        Arrays.fill(prime, true);
        prime[1] = false;
        for (int i = 2; i <= N; i++) {
            if (!prime[i]) {
                continue;
            }
            for (int j = 2 * i; j <= N; j += i) {
                prime[j] = false;
            }
        }
        return prime;
    }

    public static void main(String[] args) {
        ExampleRunner.eq(1, new int[] { 2, 3, 5 }, new ColorfulCards().theCards(5, "RRR"));
        ExampleRunner.eq(2, new int[] { 1, 4, 6 }, new ColorfulCards().theCards(7, "BBB"));
        ExampleRunner.eq(3, new int[] { -1, 4, 5 }, new ColorfulCards().theCards(6, "RBR"));
        ExampleRunner.eq(4, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, 17, 18, 19, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 47, 53 }, new ColorfulCards().theCards(58, "RBRRBRBBRBRRBBRRBBBRRBBBRR"));
        ExampleRunner.eq(5, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(495, "RBRRBRBBRBRRBBRRBBBRRBBBRR"));
        ExampleRunner.eq(6, new int[] { 2 }, new ColorfulCards().theCards(2, "R"));
        ExampleRunner.eq(7, new int[] { -1, 4 }, new ColorfulCards().theCards(5, "RB"));
        ExampleRunner.eq(8, new int[] { 2, 3, 4 }, new ColorfulCards().theCards(4, "RRB"));
        ExampleRunner.eq(9, new int[] { 1, -1, -1, -1 }, new ColorfulCards().theCards(7, "BRRB"));
        ExampleRunner.eq(10, new int[] { 2, 3, 4, 5, 6 }, new ColorfulCards().theCards(7, "RRBRB"));
        ExampleRunner.eq(11, new int[] { 2, 3, 5, 6, 8, 9 }, new ColorfulCards().theCards(9, "RRRBBB"));
        ExampleRunner.eq(12, new int[] { -1, -1, -1, -1, -1, 11, 12 }, new ColorfulCards().theCards(12, "RBBBBRB"));
        ExampleRunner.eq(13, new int[] { 2, 3, 5, 7, 8, 9, 10, 11 }, new ColorfulCards().theCards(11, "RRRRBBBR"));
        ExampleRunner.eq(14, new int[] { -1, 4, 5, 7, 8, 9, 10, 12, 13 }, new ColorfulCards().theCards(15, "RBRRBBBBR"));
        ExampleRunner.eq(15, new int[] { 2, 3, 5, 7, 11, 13, 17, 19, -1, 23 }, new ColorfulCards().theCards(25, "RRRRRRRRBR"));
        ExampleRunner.eq(16, new int[] { 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69 }, new ColorfulCards().theCards(69, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(17, new int[] { 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69 }, new ColorfulCards().theCards(69, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(18, new int[] { 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69 }, new ColorfulCards().theCards(69, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(19, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(622, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(20, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(21, new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229 }, new ColorfulCards().theCards(232, "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"));
        ExampleRunner.eq(22, new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229 }, new ColorfulCards().theCards(231, "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"));
        ExampleRunner.eq(23, new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229 }, new ColorfulCards().theCards(229, "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"));
        ExampleRunner.eq(24, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(286, "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"));
        ExampleRunner.eq(25, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"));
        ExampleRunner.eq(26, new int[] { 1, 4, 6, 7, 8, 9, 10, 12, 13, -1, 17, 19, 23, -1, 29, 31, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, 53, 59, 61, 67, -1, -1, 71, 72, 73, -1, 79, 80, 81, 82, 83, 89, 97, 101, 102, 103, 104, 105, 106 }, new ColorfulCards().theCards(107, "BBBRBBBBRBRRRBRRBRBBRBBBBBBRRRRBBRBRBRBBBRRRRBRBBB"));
        ExampleRunner.eq(27, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, -1, 83, 89, -1, 97, -1, 101, 103, 104, 105, 106, 108 }, new ColorfulCards().theCards(109, "RBRBRRRBBBRRBRBBBRBBRBRRBRBBBRRRRBRBBRBRRBRBRRBBBB"));
        ExampleRunner.eq(28, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 53, 54, 55, 56, 57, 58, 60, 61, -1, -1, 67, -1, 71, 73, -1, -1, 79, -1, 83, 89, 90 }, new ColorfulCards().theCards(90, "BRBRRBBRRRBBBBBBRBRRBBBBBBBBBRBBBBBBRBBRBRRBBRBRRB"));
        ExampleRunner.eq(29, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 113, -1, 127, 128 }, new ColorfulCards().theCards(128, "BRBBRBRBBBRBBBBRRBBRRRRRBRBRRRRRBBBRBBRRBRRRBBRBRB"));
        ExampleRunner.eq(30, new int[] { 2, 3, 4, 6, 8, 9, 10, 12, 13, 17, 18, 19, 23, -1, 29, 30, 31, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(155, "RRBBBBBBRRBRRBRBRRBBRBRRBRRRRRRRRRBRRRRRRBRBBBRBRR"));
        ExampleRunner.eq(31, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(120, "RBRBBRBBBBRBBRBBRRRBBBBRBBRRRRBBRBRBRRBRBBBRRRBBRR"));
        ExampleRunner.eq(32, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, 41, 43, 47, -1, -1, 53, -1, 59, 61, -1, 67, 71, 73, 79, -1, -1, 83, 89, 97, -1, 101, 103, -1, 107, 109, -1, 113, -1, -1, 127, -1, 131, 137 }, new ColorfulCards().theCards(138, "BRBRBRBRRRRRBRBBRRRRBBRBRRBRRRRBBRRRBRRBRRBRBBRBRR"));
        ExampleRunner.eq(33, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, 83, 89, -1, -1, -1, 97, 98 }, new ColorfulCards().theCards(98, "RRBRBRBRBRRRRBBBBBBBBRBRBRBRBBBRRBRBBBRRBBRRRBBBRB"));
        ExampleRunner.eq(34, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, 19, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, 83, -1, -1, 89, 97, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(110, "RRBBBRBBBRRRBBBRRRRBRBBRBBRBBRBBBRRBBBRRBBRRBBRRBB"));
        ExampleRunner.eq(35, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, 17, 19, 20, 21, 22, 24, 25, 26, 27, 28, 29, 31, -1, -1, 37, -1, 41, 42, 43, -1, -1, 47, -1, -1, 53, -1, -1, -1, 59, 60, 61, -1, -1, 67, -1, -1, 71, 73, 79, 83, 89, 97 }, new ColorfulCards().theCards(98, "BBRBRRBBRRBBBBBBBBRRBBRBRBRBBRBBRBBBRBRBBRBBRRRRRR"));
        ExampleRunner.eq(36, new int[] { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(135, "BRRRBBRBBRRBRRRBRRRBRBRRBRBBBRBRBRRBBRRBBRBBRRBRBR"));
        ExampleRunner.eq(37, new int[] { 1, 2, 3, 4, 5, 7, -1, 11, 13, 17, 18, 19, -1, 23, -1, 29, 31, 37, 41, 42, 43, 47, 53, -1, 59, 60, 61, 67, 71, 72, 73, 79, 83, 89, 97, 101, 103, -1, -1, -1, -1, -1, -1, 113, -1, 127, 131, 137, 138, 139 }, new ColorfulCards().theCards(141, "BRRBRRBRRRBRBRBRRRRBRRRBRBRRRBRRRRRRRBBBBBBRBRRRBR"));
        ExampleRunner.eq(38, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, 41, 43, 47, 53, -1, -1, -1, -1, -1, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 113, -1, 127 }, new ColorfulCards().theCards(127, "RBBRBRBRRRRRBBRBRRRRBBRBBRBBBRBRBBBRRRRBBBBBRBBRBR"));
        ExampleRunner.eq(39, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, 17, 19, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 53, 59, 61, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(102, "BBBBRBBBRRBBRBBBBRBRBRBRBBBBBBBRRRBBBBBRBRBRBBRRRB"));
        ExampleRunner.eq(40, new int[] { 1, 4, 5, 6, 7, 11, 12, 13, 17, 19, 23, -1, 29, 31, -1, 37, -1, 41, 42, 43, 47, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, -1, -1, 83, 89, 97, -1, -1, 101, 102, 103, -1, 107, 108, 109, -1, -1 }, new ColorfulCards().theCards(114, "BBRBRRBRRRRBRRBRBRBRRBRBBBRBRRRBBRBBRRRBBRBRBRBRBB"));
        ExampleRunner.eq(41, new int[] { -1, 4, 5, 6, 7, 11, 13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 113, 127, 128, 129, 130, 131, -1, 137 }, new ColorfulCards().theCards(138, "RBRBRRRBBRBRRRRRRRBBRBRRRRRBRRRBRBRBRBBRBBRRBBBRBR"));
        ExampleRunner.eq(42, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(155, "RBBBBRRRBBRBRRBBRRRBRRBRBBRBBRRRRRRRRRRRRBBRRBBRBB"));
        ExampleRunner.eq(43, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, 30, 31, 37, -1, -1, -1, -1, -1, 47, -1, 53, 59, 60, 61, 67, -1, 71, 73, -1, -1, -1, 79, 83, -1, -1, 89, -1, -1, -1, -1, -1, 97, 98 }, new ColorfulCards().theCards(98, "BRBRBBBBBRRBBBBBRBRRBBBBBRBRRBRRBRRBBBRRBBRBBBBBRB"));
        ExampleRunner.eq(44, new int[] { 1, 4, 6, 7, 11, 13, 17, 19, -1, 23, 29, 31, -1, 37, -1, 41, 43, 47, -1, 53, 59, 61, 67, 71, 72, 73, -1, -1, 79, 83, -1, 89, 97, -1, 101, 103, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(158, "BBBRRRRRBRRRBRBRRRBRRRRRBRBBRRBRRBRRBBRBRRRRBRBBRR"));
        ExampleRunner.eq(45, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 127 }, new ColorfulCards().theCards(129, "RBRRBRRRRBRBBBRBBRRRBRRRBRBBRRRRRBBRBRRBRBBRBBBBBR"));
        ExampleRunner.eq(46, new int[] { -1, -1, -1, -1, -1, 11, 12, 13, -1, 17, 18, 19, 23, -1, 29, 30, 31, 37, 41, 42, 43, -1, 47, 53, -1, -1, -1, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, 83, -1, 89, -1, 97, 101, 103 }, new ColorfulCards().theCards(105, "RBRBBRBRBRBRRBRBRRRBRBRRBBBRBRBBBBRBRBBBBBRRBRBRRR"));
        ExampleRunner.eq(47, new int[] { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, 29, 30, 31, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, 53, 59, 60, 61, 67, 71, 72, 73, 79, 83, -1, 89, -1, -1, -1, 97, 101 }, new ColorfulCards().theCards(101, "BRRBRBBBBRBBBRBBRBBBRBRRBBRBBBBBBRRBRRRBRRRBRBBBRR"));
        ExampleRunner.eq(48, new int[] { 2, 3, 5, 6, 7, 11, 13, -1, -1, 17, 19, 23, -1, 29, 31, 37, 41, 43, 47, -1, -1, -1, -1, -1, -1, 59, 60, 61, -1, 67, -1, -1, -1, -1, -1, -1, 79, -1, 83, -1, -1, 89, 97, 101, 103, -1, 107, 109, -1, 113 }, new ColorfulCards().theCards(119, "RRRBRRRBBRRRBRRRRRRBBBBBBRBRBRBBBRBBRBRBBRRRRBRRBR"));
        ExampleRunner.eq(49, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 53, 59, 61, 67, 71, 73, -1, -1, -1, 79, -1, 83, -1, -1, 89, 97, -1, 101, 102, 103, 107, 109, 113, 127, 131 }, new ColorfulCards().theCards(132, "RRBRBBRRBRRRBRBBBRRRBBBBBRRRRRRBBBRBRBBRRBRBRRRRRR"));
        ExampleRunner.eq(50, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 89, 97, -1, -1, 101, 102, 104 }, new ColorfulCards().theCards(104, "BBRRRBBRRBRRRBBBRRBRBBRBBRBBBRRRBBRBBBRBBBBRRBBRBB"));
        ExampleRunner.eq(51, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, 38, 39, 40, 41, 42, 43, -1, -1, 47, 53, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(134, "BBRBRBRRRBBBRBRBBRBBBRBRBBRRBBRRBBRRBBRBRRBRRRRRRR"));
        ExampleRunner.eq(52, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, 30, 31, 37, 41, 42, 43, -1, 47, -1, -1, -1, -1, 53, 59, 60, 61, 67, 71, 72, 73, -1, 79, 80, 81, 82, 83, -1, 89, 97, 101, 103, -1, 107, 108 }, new ColorfulCards().theCards(109, "RRBBRRBBBRRBBBBRBRRRBRBRBBBBRRBRRRBRBRBBBRBRRRRBRB"));
        ExampleRunner.eq(53, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, 41, 42, 43, -1, 47, -1, -1, -1, 53, -1, 59, 61, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(108, "RBBBRBRRRBRRBBBRBBRBRBRBBBRBRRBBBBRBRBRRBRBBRRRBBB"));
        ExampleRunner.eq(54, new int[] { 1, -1, -1, -1, -1, -1, -1, 11, 13, -1, -1, 17, 19, 20, 21, 22, 23, 29, 31, -1, -1, -1, -1, 37, 41, 43, -1, 47, -1, 53, 59, 61, 67, 71, 73, -1, -1, 79, 83, -1, -1, 89, -1, 97, -1, -1, 101, 102, 104, 105 }, new ColorfulCards().theCards(105, "BRRRBBBRRBBRRBBBRRRBBBBRRRBRBRRRRRRBBRRBBRBRBBRBBB"));
        ExampleRunner.eq(55, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, 24, 25, 26, 27, 28, 29, 30, 31, 37, -1, -1, 41, 43, 47, -1, -1, -1, -1, -1, -1, 59, 61, 67, 68, 69, 70, 72, 73, 79, 83, 89, 97, 101, 102, 104, 105, 106 }, new ColorfulCards().theCards(106, "RRBBRBRRBRBBRBBBBBRBRRBBRRRBBBBBBRRRBBBBRRRRRRBBBB"));
        ExampleRunner.eq(56, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(482, "BBRBBBRRRRRBRBBRRRBRRBRRBBRRRBBRRBBBBRRBRBBRRRRRBR"));
        ExampleRunner.eq(57, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(403, "BBBBRRRBBRBRRRBBRRRBBBRRBBRBRBRBBBBBBBRRBBBRRBRRBR"));
        ExampleRunner.eq(58, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(899, "BRBRRRBBRRRRBBRRBBBRRRBRBRBRRBBRRRBBBBBBRBRBRBRBBB"));
        ExampleRunner.eq(59, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "RBBBRRRBRBRBRBBRRRBRRBBRBBBBBRBRBRRRRBRRRRBRBRRRRR"));
        ExampleRunner.eq(60, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "BBRBBBBRRBRRBRRBRBBRBBBRBBRRBBRBBRBRRRRRBBBRBRRRBR"));
        ExampleRunner.eq(61, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "BBRRBBRRBRRRRBBRBRRRBRBBBBBRBRRBBRRRBRRRBRBBBBBBBR"));
        ExampleRunner.eq(62, new int[] { 1 }, new ColorfulCards().theCards(1, "B"));
        ExampleRunner.eq(63, new int[] { 1 }, new ColorfulCards().theCards(2, "B"));
        ExampleRunner.eq(64, new int[] { 1, 2 }, new ColorfulCards().theCards(2, "BR"));
        ExampleRunner.eq(65, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(80, "RBRRBRBBRBRRBBRRBBBRRBBBRR"));
        ExampleRunner.eq(66, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(999, "RRRRRRRRRRRBBBBBRRRRRRRRRRRRRRRRBBBBBRRRRRRRRRRRRR"));
        ExampleRunner.eq(67, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(695, "RBRRBRBBRBRRBBRRBBBRRBBBRR"));
        ExampleRunner.eq(68, new int[] { 1, -1, -1, -1 }, new ColorfulCards().theCards(7, "BRRR"));
        ExampleRunner.eq(69, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "RRRRRRRRRBRRRRRRRRRBRRRRRRRRRBRRRRRRRRRBRRRRRRRRBB"));
        ExampleRunner.eq(70, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "BBBBBRRRRRRBBBBBBRRRRRBBBR"));
        ExampleRunner.eq(71, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "RRRRRBBBBBRRRRRBBBBBRRRRRBBBBBRRRRRBBBBBRRRRRBBBBB"));
        ExampleRunner.eq(72, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 107, 109, -1, -1, 113 }, new ColorfulCards().theCards(126, "RBRRBRBBRBRRBBRRBBBRRBBBRRRBRRBRBBRBRRBBRRBBRRBBR"));
        ExampleRunner.eq(73, new int[] { -1 }, new ColorfulCards().theCards(3, "R"));
        ExampleRunner.eq(74, new int[] { -1 }, new ColorfulCards().theCards(50, "R"));
        ExampleRunner.eq(75, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(50, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"));
        ExampleRunner.eq(76, new int[] { -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(10, "RRRBBB"));
        ExampleRunner.eq(77, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, new ColorfulCards().theCards(1000, "RRRRBBBRBRBRBRBRBRBRBRBBBRRRBRBRBRBRBRBBBRRBRBRBRB"));
        ExampleRunner.eq(78, new int[] { 1, 2, 3, 5, 7, 11, 13, 14, 15, 16, 17, 19, -1, 23, 24, 25, 26, 27, 28, 29, 31, -1, 37, 41, 43, 47, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, -1, 83, 89, -1, -1, 97, -1, -1 }, new ColorfulCards().theCards(100, "BRRRRRRBBBRRBRBBBBBRRBRRRRBBRBBBBRRBRBRBBRBRRBBRBB"));
    }
}
