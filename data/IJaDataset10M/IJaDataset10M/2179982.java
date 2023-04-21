package apdol.string.format;

/**
 *
 * @author Hari RZ
 */
public class Rupiah {

    public Rupiah() {
    }

    public String formatRupiah(String s) {
        int j = s.length();
        int k = j / 3;
        int m = j % 3;
        String result = new String();
        String t1;
        for (int i = 0; i < k; i++) {
            t1 = "." + s.substring(j - 3, j);
            result = t1 + result;
            j = j - 3;
        }
        String t2 = new String();
        if (m != 0) {
            for (int n = 0; n < m; n++) {
                t2 = t2 + s.charAt(n);
            }
            result = t2 + result;
        } else {
            result = result.substring(1);
        }
        return result;
    }
}
