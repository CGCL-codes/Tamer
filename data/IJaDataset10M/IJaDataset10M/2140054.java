package p400.srm497;

import utils.ExampleRunner;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 2/18/11
 * Time: 8:14 AM
 * with help
 */
public class PermutationSignature {

    public int[] reconstruct(String signature) {
        int[] result = new int[signature.length() + 1];
        boolean[] used = new boolean[signature.length() + 1];
        for (int i = 0; i <= signature.length(); i++) {
            int min = 0;
            int max = used.length - 1;
            while (used[min]) min++;
            for (int j = i; j < signature.length(); j++) {
                if (signature.charAt(j) != 'D') {
                    break;
                }
                min++;
                while (used[min]) min++;
            }
            for (int j = i; j < signature.length(); j++) {
                if (signature.charAt(j) != 'I') {
                    break;
                }
                max--;
                while (used[max]) max--;
            }
            for (int j = min; j <= max; j++) {
                if (used[j]) continue;
                if (i > 0 && signature.charAt(i - 1) == 'I' && j < result[i - 1]) continue;
                if (i > 0 && signature.charAt(i - 1) == 'D' && j > result[i - 1]) continue;
                result[i] = j + 1;
                used[j] = true;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ExampleRunner.eq(1, new int[] { 1, 2, 3, 4, 5, 6 }, new PermutationSignature().reconstruct("IIIII"));
        ExampleRunner.eq(2, new int[] { 2, 1, 3 }, new PermutationSignature().reconstruct("DI"));
        ExampleRunner.eq(3, new int[] { 1, 2, 3, 4, 6, 5 }, new PermutationSignature().reconstruct("IIIID"));
        ExampleRunner.eq(4, new int[] { 2, 1, 3, 5, 4, 7, 6 }, new PermutationSignature().reconstruct("DIIDID"));
        ExampleRunner.eq(5, new int[] { 2, 1 }, new PermutationSignature().reconstruct("D"));
        ExampleRunner.eq(6, new int[] { 1, 2 }, new PermutationSignature().reconstruct("I"));
        ExampleRunner.eq(7, new int[] { 3, 2, 1 }, new PermutationSignature().reconstruct("DD"));
        ExampleRunner.eq(8, new int[] { 2, 1, 3 }, new PermutationSignature().reconstruct("DI"));
        ExampleRunner.eq(9, new int[] { 1, 3, 2 }, new PermutationSignature().reconstruct("ID"));
        ExampleRunner.eq(10, new int[] { 1, 2, 3 }, new PermutationSignature().reconstruct("II"));
        ExampleRunner.eq(11, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 }, new PermutationSignature().reconstruct("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII"));
        ExampleRunner.eq(12, new int[] { 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(13, new int[] { 2, 1, 4, 3, 5, 6, 8, 7, 10, 9, 12, 11, 14, 13, 16, 15, 18, 17, 19, 21, 20, 23, 22, 25, 24, 27, 26, 33, 32, 31, 30, 29, 28 }, new PermutationSignature().reconstruct("DIDIIIDIDIDIDIDIDIIDIDIDIDIDDDDD"));
        ExampleRunner.eq(14, new int[] { 1, 2, 3, 5, 4, 7, 6, 9, 8, 11, 10, 12, 15, 14, 13, 19, 18, 17, 16, 22, 21, 20, 24, 23, 25, 26, 27, 28, 29, 30, 31 }, new PermutationSignature().reconstruct("IIIDIDIDIDIIDDIDDDIDDIDIIIIIII"));
        ExampleRunner.eq(15, new int[] { 1, 3, 2, 4, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 27, 28, 29, 30, 31, 32, 33, 35, 34, 41, 40, 39, 38, 37, 36, 42 }, new PermutationSignature().reconstruct("IDIIDIDIDIDIDIDDDDDDDDDDDIIIIIIIIDIDDDDDI"));
        ExampleRunner.eq(16, new int[] { 1, 3, 2, 4, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 27, 28, 29, 30, 31, 32, 33, 35, 34, 41, 40, 39, 38, 37, 36, 47, 46, 45, 44, 43, 42 }, new PermutationSignature().reconstruct("IDIIDIDIDIDIDIDDDDDDDDDDDIIIIIIIIDIDDDDDIDDDDD"));
        ExampleRunner.eq(17, new int[] { 1, 3, 2, 5, 4, 7, 6, 9, 8, 11, 10, 13, 12, 15, 14, 17, 16, 19, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 28, 31, 30, 33, 32, 35, 34, 37, 36, 39, 38, 41, 40, 43, 42, 45, 44, 47, 46, 49, 48, 51, 50 }, new PermutationSignature().reconstruct("IDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDID"));
        ExampleRunner.eq(18, new int[] { 2, 1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 16, 15, 18, 17, 20, 19, 22, 21, 24, 23, 26, 25, 28, 27, 30, 29, 32, 31, 34, 33, 36, 35, 38, 37, 40, 39, 42, 41, 44, 43, 46, 45, 48, 47, 50, 49, 51 }, new PermutationSignature().reconstruct("DIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDIDI"));
        ExampleRunner.eq(19, new int[] { 1, 3, 2, 5, 4, 7, 6, 9, 8, 11, 10, 13, 12, 15, 14, 17, 16, 19, 18, 21, 20, 22, 23, 25, 24, 27, 26, 29, 28, 31, 30, 33, 32, 35, 34, 37, 36, 39, 38, 41, 40, 43, 42, 45, 44, 47, 46, 49, 48, 51, 50 }, new PermutationSignature().reconstruct("IDIDIDIDIDIDIDIDIDIDIIIDIDIDIDIDIDIDIDIDIDIDIDIDID"));
        ExampleRunner.eq(20, new int[] { 1, 3, 2, 5, 4, 7, 6, 9, 8, 11, 10, 13, 12, 15, 14, 17, 16, 19, 18, 21, 20, 22, 23, 25, 24, 27, 26, 29, 28, 31, 30, 33, 32, 35, 34, 39, 38, 37, 36, 41, 40, 43, 42, 45, 44, 47, 46, 49, 48, 51, 50 }, new PermutationSignature().reconstruct("IDIDIDIDIDIDIDIDIDIDIIIDIDIDIDIDIDIDDDIDIDIDIDIDID"));
        ExampleRunner.eq(21, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 15, 18, 17, 20, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 31, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49 }, new PermutationSignature().reconstruct("IIIIIIIIIIIIIIDIDIDIIIIIIIIIIDIIIIIIIIIIIIIIIIII"));
        ExampleRunner.eq(22, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 15, 18, 17, 20, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 31, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 50, 49 }, new PermutationSignature().reconstruct("IIIIIIIIIIIIIIDIDIDIIIIIIIIIIDIIIIIIIIIIIIIIIIIID"));
        ExampleRunner.eq(23, new int[] { 2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 16, 19, 18, 21, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50 }, new PermutationSignature().reconstruct("DIIIIIIIIIIIIIIDIDIDIIIIIIIIIIDIIIIIIIIIIIIIIIIII"));
        ExampleRunner.eq(24, new int[] { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 17, 24, 23, 22, 21, 20, 19, 18, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 46, 45 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDIIDDDDDDIDDDDDDDDDDDDDDDDDDDID"));
        ExampleRunner.eq(25, new int[] { 2, 1, 3, 5, 4, 7, 6, 9, 8, 10, 12, 11, 13, 15, 14 }, new PermutationSignature().reconstruct("DIIDIDIDIIDIID"));
        ExampleRunner.eq(26, new int[] { 1, 2, 3, 4, 6, 5, 8, 7, 10, 9, 11, 13, 12, 15, 14, 16, 18, 17 }, new PermutationSignature().reconstruct("IIIIDIDIDIIDIDIID"));
        ExampleRunner.eq(27, new int[] { 2, 1, 3, 5, 4, 7, 6, 8, 10, 9, 13, 12, 11, 15, 14, 17, 16 }, new PermutationSignature().reconstruct("DIIDIDIIDIDDIDID"));
        ExampleRunner.eq(28, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 10, 9, 12, 11, 14, 13, 16, 15, 18, 17, 19, 20 }, new PermutationSignature().reconstruct("IIIIIIIIDIDIDIDIDII"));
        ExampleRunner.eq(29, new int[] { 1, 5, 4, 3, 2, 7, 6, 8 }, new PermutationSignature().reconstruct("IDDDIDI"));
        ExampleRunner.eq(30, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 10, 13, 12, 15, 14, 16, 17, 18, 20, 19, 22, 21, 24, 23, 27, 26, 25, 29, 28 }, new PermutationSignature().reconstruct("IIIIIIIIIDIDIDIIIIDIDIDIDDID"));
        ExampleRunner.eq(31, new int[] { 2, 1, 3, 4, 5, 6, 7, 8, 10, 9, 11, 12, 13, 14, 15, 16, 18, 17, 20, 19, 22, 21, 23, 24, 25, 26, 27, 28, 29, 37, 36, 35, 34, 33, 32, 31, 30 }, new PermutationSignature().reconstruct("DIIIIIIIDIIIIIIIDIDIDIIIIIIIIDDDDDDD"));
        ExampleRunner.eq(32, new int[] { 2, 1, 3, 6, 5, 4, 7, 8, 10, 9, 12, 11, 17, 16, 15, 14, 13, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 31, 30, 29, 28, 35, 34, 33, 32, 36, 37, 38, 39, 40, 41, 43, 42, 45, 44, 47, 46, 49, 48, 50 }, new PermutationSignature().reconstruct("DIIDDIIIDIDIDDDDIIIIIIIIIIIDDDIDDDIIIIIIIDIDIDIDI"));
        ExampleRunner.eq(33, new int[] { 1, 5, 4, 3, 2, 6, 10, 9, 8, 7, 14, 13, 12, 11, 18, 17, 16, 15, 23, 22, 21, 20, 19, 24, 26, 25, 28, 27, 35, 34, 33, 32, 31, 30, 29, 36, 42, 41, 40, 39, 38, 37, 44, 43, 47, 46, 45, 50, 49, 48, 51 }, new PermutationSignature().reconstruct("IDDDIIDDDIDDDIDDDIDDDDIIDIDIDDDDDDIIDDDDDIDIDDIDDI"));
        ExampleRunner.eq(34, new int[] { 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 13, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 25, 26, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37 }, new PermutationSignature().reconstruct("DDDDDDDDDDDIIDDDDDDDDDDIIIDDDDDDDDDIDDDDDDDDDDDDD"));
        ExampleRunner.eq(35, new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 19, 18, 17, 16, 15, 14, 13, 12, 11, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 34, 35, 36, 37, 38, 39, 47, 46, 45, 44, 43, 42, 41, 40, 49, 48 }, new PermutationSignature().reconstruct("DDDDDDDDDIDDDDDDDDIDDDDDDDDDDDDDIIIIIIIDDDDDDDID"));
        ExampleRunner.eq(36, new int[] { 6, 5, 4, 3, 2, 1, 8, 7, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 29, 28, 27, 26, 25, 24, 23, 22, 30, 31, 32, 39, 38, 37, 36, 35, 34, 33, 40, 49, 48, 47, 46, 45, 44, 43, 42, 41, 51, 50 }, new PermutationSignature().reconstruct("DDDDDIDIDDDDDDDDDDDDIDDDDDDDIIIIDDDDDDIIDDDDDDDDID"));
        ExampleRunner.eq(37, new int[] { 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDD"));
        ExampleRunner.eq(38, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34 }, new PermutationSignature().reconstruct("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII"));
        ExampleRunner.eq(39, new int[] { 1, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 19, 20 }, new PermutationSignature().reconstruct("IDDDDDDDDDDDDDDDDII"));
        ExampleRunner.eq(40, new int[] { 3, 2, 1, 4 }, new PermutationSignature().reconstruct("DDI"));
        ExampleRunner.eq(41, new int[] { 4, 3, 2, 1, 5, 6, 7, 11, 10, 9, 8 }, new PermutationSignature().reconstruct("DDDIIIIDDD"));
        ExampleRunner.eq(42, new int[] { 5, 4, 3, 2, 1, 6 }, new PermutationSignature().reconstruct("DDDDI"));
        ExampleRunner.eq(43, new int[] { 3, 2, 1, 4, 5, 6, 8, 7, 9, 12, 11, 10, 13, 18, 17, 16, 15, 14, 20, 19, 24, 23, 22, 21 }, new PermutationSignature().reconstruct("DDIIIIDIIDDIIDDDDIDIDDD"));
        ExampleRunner.eq(44, new int[] { 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 31, 32, 33, 34, 35, 36, 37, 38, 39, 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDIIIIIIIIIIDDDDDDDDDDD"));
        ExampleRunner.eq(45, new int[] { 2, 1, 5, 4, 3, 7, 6 }, new PermutationSignature().reconstruct("DIDDID"));
        ExampleRunner.eq(46, new int[] { 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(47, new int[] { 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(48, new int[] { 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDD"));
        ExampleRunner.eq(49, new int[] { 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(50, new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDD"));
        ExampleRunner.eq(51, new int[] { 4, 3, 2, 1, 8, 7, 6, 5 }, new PermutationSignature().reconstruct("DDDIDDD"));
        ExampleRunner.eq(52, new int[] { 6, 5, 4, 3, 2, 1, 7, 8, 9, 10, 16, 15, 14, 13, 12, 11, 17, 18, 19, 20, 26, 25, 24, 23, 22, 21, 27, 28, 29, 30, 36, 35, 34, 33, 32, 31, 37, 38, 39, 40, 46, 45, 44, 43, 42, 41, 47, 48, 49, 50, 51 }, new PermutationSignature().reconstruct("DDDDDIIIIIDDDDDIIIIIDDDDDIIIIIDDDDDIIIIIDDDDDIIIII"));
        ExampleRunner.eq(53, new int[] { 4, 3, 2, 1, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15, 23, 22, 21, 20, 19, 18, 17, 16, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36 }, new PermutationSignature().reconstruct("DDDIIIIDDDIIIIIDDDDDDDIIIIIIIIIIIIIDDDDDDDDDDDD"));
        ExampleRunner.eq(54, new int[] { 1, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 19, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20 }, new PermutationSignature().reconstruct("IDDDDDDDDDDDDDDDDIIDDDDDDDDDDDD"));
        ExampleRunner.eq(55, new int[] { 1, 7, 6, 5, 4, 3, 2, 8, 13, 12, 11, 10, 9, 14, 19, 18, 17, 16, 15, 20, 24, 23, 22, 21, 25, 32, 31, 30, 29, 28, 27, 26, 33, 41, 40, 39, 38, 37, 36, 35, 34, 42, 47, 46, 45, 44, 43, 48, 51, 50, 49 }, new PermutationSignature().reconstruct("IDDDDDIIDDDDIIDDDDIIDDDIIDDDDDDIIDDDDDDDIIDDDDIIDD"));
        ExampleRunner.eq(56, new int[] { 3, 2, 1, 5, 4, 6, 8, 7, 9, 16, 15, 14, 13, 12, 11, 10, 17, 18, 19, 20, 21, 26, 25, 24, 23, 22, 27, 28, 31, 30, 29, 33, 32, 34, 36, 35, 39, 38, 37 }, new PermutationSignature().reconstruct("DDIDIIDIIDDDDDDIIIIIIDDDDIIIDDIDIIDIDD"));
        ExampleRunner.eq(57, new int[] { 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDD"));
        ExampleRunner.eq(58, new int[] { 7, 6, 5, 4, 3, 2, 1, 8, 9, 10, 11, 12, 13, 20, 19, 18, 17, 16, 15, 14, 21, 22, 23, 24, 25, 26, 27, 28, 36, 35, 34, 33, 32, 31, 30, 29, 37, 38, 39, 40, 41, 43, 42, 45, 44, 47, 46, 51, 50, 49, 48 }, new PermutationSignature().reconstruct("DDDDDDIIIIIIIDDDDDDIIIIIIIIIDDDDDDDIIIIIIDIDIDIDDD"));
        ExampleRunner.eq(59, new int[] { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 42, 41, 40, 39, 38, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDIIIIIIIIIIIIIIIIIIIIIIDDDDDIIIIIIIII"));
        ExampleRunner.eq(60, new int[] { 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(61, new int[] { 1, 7, 6, 5, 4, 3, 2 }, new PermutationSignature().reconstruct("IDDDDD"));
        ExampleRunner.eq(62, new int[] { 3, 2, 1, 5, 4, 7, 6, 9, 8, 13, 12, 11, 10, 15, 14, 17, 16, 21, 20, 19, 18, 24, 23, 22, 26, 25, 27, 28, 30, 29, 31, 33, 32, 35, 34, 37, 36, 39, 38, 41, 40, 42, 44, 43, 45, 46, 48, 47, 49, 51, 50 }, new PermutationSignature().reconstruct("DDIDIDIDIDDDIDIDIDDDIDDIDIIIDIIDIDIDIDIDIIDIIIDIID"));
        ExampleRunner.eq(63, new int[] { 5, 4, 3, 2, 1, 6, 7, 8, 11, 10, 9, 12, 17, 16, 15, 14, 13, 19, 18, 21, 20, 27, 26, 25, 24, 23, 22 }, new PermutationSignature().reconstruct("DDDDIIIIDDIIDDDDIDIDIDDDDD"));
        ExampleRunner.eq(64, new int[] { 2, 1, 6, 5, 4, 3, 7, 9, 8, 10, 16, 15, 14, 13, 12, 11, 17, 18, 25, 24, 23, 22, 21, 20, 19, 26, 29, 28, 27, 33, 32, 31, 30, 35, 34, 39, 38, 37, 36, 41, 40, 42, 44, 43, 48, 47, 46, 45 }, new PermutationSignature().reconstruct("DIDDDIIDIIDDDDDIIIDDDDDDIIDDIDDDIDIDDDIDIIDIDDD"));
        ExampleRunner.eq(65, new int[] { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 32, 33, 34, 35, 36, 37, 38, 39, 40, 46, 45, 44, 43, 42, 41, 47, 48, 49, 50, 51 }, new PermutationSignature().reconstruct("DDDDDDDDDDIIIIIIIIIIDDDDDDDDDDIIIIIIIIIIDDDDDIIIII"));
        ExampleRunner.eq(66, new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 19, 18, 17, 16, 15, 14, 13, 12, 11, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 33, 32, 31, 30, 34, 37, 36, 35, 38, 39, 41, 40, 43, 42, 44, 46, 45, 47 }, new PermutationSignature().reconstruct("DDDDDDDDDIDDDDDDDDIDDDDDDDDDIDDDIIDDIIIDIDIIDI"));
        ExampleRunner.eq(67, new int[] { 5, 4, 3, 2, 1, 6, 7, 8, 13, 12, 11, 10, 9 }, new PermutationSignature().reconstruct("DDDDIIIIDDDD"));
        ExampleRunner.eq(68, new int[] { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDD"));
        ExampleRunner.eq(69, new int[] { 2, 1, 3, 4, 12, 11, 10, 9, 8, 7, 6, 5, 14, 13, 16, 15, 20, 19, 18, 17, 22, 21 }, new PermutationSignature().reconstruct("DIIIDDDDDDDIDIDIDDDID"));
        ExampleRunner.eq(70, new int[] { 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 51, 50, 49, 48, 47, 46, 45, 44, 43 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDIDDDDDDDDDDDDDDDDIDDDDDDDD"));
        ExampleRunner.eq(71, new int[] { 1, 2, 6, 5, 4, 3, 7 }, new PermutationSignature().reconstruct("IIDDDI"));
        ExampleRunner.eq(72, new int[] { 2, 1, 4, 3, 6, 5, 7 }, new PermutationSignature().reconstruct("DIDIDI"));
        ExampleRunner.eq(73, new int[] { 4, 3, 2, 1, 5, 6, 7, 11, 10, 9, 8, 13, 12, 15, 14, 20, 19, 18, 17, 16 }, new PermutationSignature().reconstruct("DDDIIIIDDDIDIDIDDDD"));
        ExampleRunner.eq(74, new int[] { 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 51 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDI"));
        ExampleRunner.eq(75, new int[] { 1, 2, 5, 4, 3, 6 }, new PermutationSignature().reconstruct("IIDDI"));
        ExampleRunner.eq(76, new int[] { 2, 1, 3, 7, 6, 5, 4 }, new PermutationSignature().reconstruct("DIIDDD"));
        ExampleRunner.eq(77, new int[] { 2, 1, 3, 5, 4, 8, 7, 6, 9, 11, 10, 14, 13, 12, 15, 17, 16, 20, 19, 18, 21, 23, 22, 26, 25, 24, 27, 29, 28, 32, 31, 30, 33, 35, 34, 38, 37, 36, 39, 41, 40, 44, 43, 42, 45, 47, 46 }, new PermutationSignature().reconstruct("DIIDIDDIIDIDDIIDIDDIIDIDDIIDIDDIIDIDDIIDIDDIID"));
        ExampleRunner.eq(78, new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 35, 34, 33, 32, 31, 30, 29, 28, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 }, new PermutationSignature().reconstruct("DDDDDDDDIIIIIIIIIIIIIIIIIIIDDDDDDDIIIIIIIIIIIIIIII"));
        ExampleRunner.eq(79, new int[] { 1, 4, 3, 2, 5, 9, 8, 7, 6, 10, 11, 16, 15, 14, 13, 12, 17, 18, 19, 25, 24, 23, 22, 21, 20, 26, 27, 28, 29, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 51 }, new PermutationSignature().reconstruct("IDDIIDDDIIIDDDDIIIIDDDDDIIIIIDDDDDDDDDDDDDDDDDDDDI"));
        ExampleRunner.eq(80, new int[] { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(81, new int[] { 1, 4, 3, 2 }, new PermutationSignature().reconstruct("IDD"));
        ExampleRunner.eq(82, new int[] { 4, 3, 2, 1, 5, 10, 9, 8, 7, 6, 11, 15, 14, 13, 12, 16 }, new PermutationSignature().reconstruct("DDDIIDDDDIIDDDI"));
        ExampleRunner.eq(83, new int[] { 8, 7, 6, 5, 4, 3, 2, 1, 9, 13, 12, 11, 10, 16, 15, 14, 21, 20, 19, 18, 17 }, new PermutationSignature().reconstruct("DDDDDDDIIDDDIDDIDDDD"));
        ExampleRunner.eq(84, new int[] { 4, 3, 2, 1, 5, 6, 10, 9, 8, 7, 11, 12, 16, 15, 14, 13, 17, 18, 22, 21, 20, 19, 23, 24, 28, 27, 26, 25 }, new PermutationSignature().reconstruct("DDDIIIDDDIIIDDDIIIDDDIIIDDD"));
        ExampleRunner.eq(85, new int[] { 1, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 28, 27, 29, 31, 30 }, new PermutationSignature().reconstruct("IDDDDDDDDDDDDIIIIIIIIIIIIIDIID"));
        ExampleRunner.eq(86, new int[] { 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDD"));
        ExampleRunner.eq(87, new int[] { 1, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 }, new PermutationSignature().reconstruct("IDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(88, new int[] { 1, 2, 4, 3, 6, 5, 10, 9, 8, 7, 12, 11, 14, 13, 18, 17, 16, 15, 20, 19, 22, 21, 24, 23, 26, 25, 30, 29, 28, 27, 32, 31, 33, 34, 38, 37, 36, 35, 39, 40, 41, 45, 44, 43, 42, 47, 46, 49, 48, 51, 50 }, new PermutationSignature().reconstruct("IIDIDIDDDIDIDIDDDIDIDIDIDIDDDIDIIIDDDIIIIDDDIDIDID"));
        ExampleRunner.eq(89, new int[] { 1, 4, 3, 2, 5 }, new PermutationSignature().reconstruct("IDDI"));
        ExampleRunner.eq(90, new int[] { 2, 1, 3, 5, 4, 8, 7, 6, 9, 11, 10, 14, 13, 12, 15, 17, 16, 20, 19, 18, 21, 23, 22, 27, 26, 25, 24, 28, 29, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 41, 42, 43, 44, 45, 46, 47 }, new PermutationSignature().reconstruct("DIIDIDDIIDIDDIIDIDDIIDIDDDIIIDDDDDDDDDDIIIIIII"));
        ExampleRunner.eq(91, new int[] { 1, 2, 3, 5, 4, 7, 6, 10, 9, 8, 12, 11, 14, 13, 16, 15, 17, 18, 20, 19, 22, 21, 24, 23, 25, 31, 30, 29, 28, 27, 26, 33, 32, 35, 34, 43, 42, 41, 40, 39, 38, 37, 36, 44, 45, 46, 47, 48, 49, 51, 50 }, new PermutationSignature().reconstruct("IIIDIDIDDIDIDIDIIIDIDIDIIDDDDDIDIDIDDDDDDDIIIIIIID"));
        ExampleRunner.eq(92, new int[] { 1, 4, 3, 2, 7, 6, 5, 11, 10, 9, 8, 12, 14, 13, 15, 17, 16, 18, 20, 19, 22, 21, 25, 24, 23 }, new PermutationSignature().reconstruct("IDDIDDIDDDIIDIIDIIDIDIDD"));
        ExampleRunner.eq(93, new int[] { 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 15 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDI"));
        ExampleRunner.eq(94, new int[] { 3, 2, 1, 4, 7, 6, 5, 8, 11, 10, 9, 12, 13 }, new PermutationSignature().reconstruct("DDIIDDIIDDII"));
        ExampleRunner.eq(95, new int[] { 1, 5, 4, 3, 2 }, new PermutationSignature().reconstruct("IDDD"));
        ExampleRunner.eq(96, new int[] { 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 32, 33, 34 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDIII"));
        ExampleRunner.eq(97, new int[] { 2, 1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11, 15, 14, 13, 16, 18, 17, 20, 19, 22, 21, 24, 23, 26, 25, 28, 27, 30, 29, 32, 31, 34, 33, 36, 35, 37, 40, 39, 38, 42, 41, 44, 43, 46, 45, 49, 48, 47, 50 }, new PermutationSignature().reconstruct("DIDIDIDIDIDIDDIIDIDIDIDIDIDIDIDIDIDIIDDIDIDIDIDDI"));
        ExampleRunner.eq(98, new int[] { 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));
        ExampleRunner.eq(99, new int[] { 3, 2, 1, 6, 5, 4, 7 }, new PermutationSignature().reconstruct("DDIDDI"));
        ExampleRunner.eq(100, new int[] { 4, 3, 2, 1, 6, 5, 7 }, new PermutationSignature().reconstruct("DDDIDI"));
        ExampleRunner.eq(101, new int[] { 2, 1, 4, 3, 6, 5, 7, 9, 8, 10, 12, 11, 15, 14, 13, 17, 16, 18, 19, 20, 21, 24, 23, 22, 25, 26, 27, 30, 29, 28, 32, 31, 35, 34, 33, 36, 37, 39, 38, 40, 45, 44, 43, 42, 41, 47, 46, 48, 50, 49, 51 }, new PermutationSignature().reconstruct("DIDIDIIDIIDIDDIDIIIIIDDIIIIDDIDIDDIIIDIIDDDDIDIIDI"));
        ExampleRunner.eq(102, new int[] { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new PermutationSignature().reconstruct("DDDDDDDDDD"));
        ExampleRunner.eq(103, new int[] { 2, 1, 3, 5, 4, 8, 7, 6 }, new PermutationSignature().reconstruct("DIIDIDD"));
        ExampleRunner.eq(104, new int[] { 1, 2, 6, 5, 4, 3, 7, 11, 10, 9, 8, 12, 15, 14, 13, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 26, 27, 28, 30, 29, 31, 35, 34, 33, 32, 38, 37, 36, 43, 42, 41, 40, 39, 44, 45, 46, 47 }, new PermutationSignature().reconstruct("IIDDDIIDDDIIDDIDDDDDDDDDIIIIDIIDDDIDDIDDDDIIII"));
        ExampleRunner.eq(105, new int[] { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 25, 24, 23, 22, 21, 26, 28, 27, 29, 30, 32, 31, 33, 37, 36, 35, 34, 39, 38, 40, 43, 42, 41, 45, 44, 46, 50, 49, 48, 47, 51 }, new PermutationSignature().reconstruct("DDDDDDDDDDDDDDDDDDDIDDDDIIDIIIDIIDDDIDIIDDIDIIDDDI"));
        ExampleRunner.eq(106, new int[] { 4, 3, 2, 1, 8, 7, 6, 5, 9 }, new PermutationSignature().reconstruct("DDDIDDDI"));
        ExampleRunner.eq(107, new int[] { 2, 1, 4, 3, 5, 7, 6, 10, 9, 8, 11, 14, 13, 12, 15, 16, 18, 17, 22, 21, 20, 19, 23, 24, 25, 30, 29, 28, 27, 26, 32, 31, 34, 33, 36, 35, 38, 37, 40, 39, 42, 41, 44, 43, 46, 45, 48, 47, 50, 49, 51 }, new PermutationSignature().reconstruct("DIDIIDIDDIIDDIIIDIDDDIIIIDDDDIDIDIDIDIDIDIDIDIDIDI"));
    }
}
