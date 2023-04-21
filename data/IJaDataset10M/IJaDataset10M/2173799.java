package fisoft.shellrpc;

/**
 * Shell interface.
 *
 * @author Moez Ben MBarka
 * @version $Revision$
 */
public class DummyArrays {

    public String shell_concat(String[] str) {
        String tmp = str[0];
        for (int n = str.length, i = 1; i < n; i++) {
            tmp += str[i];
        }
        return tmp;
    }

    public int shell_sum(int[] set) {
        int sum = 0;
        for (int n = set.length, i = 0; i < n; i++) {
            sum += set[i];
        }
        return sum;
    }

    public String shell_sum_concat(String[] strs, int[] ints) {
        return shell_concat(strs) + "_" + shell_sum(ints);
    }
}
