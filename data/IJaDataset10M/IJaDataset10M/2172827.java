package pinyin.utils;

import java.util.List;

/**
 * @author Michael Gilleland
 */
public class Distance {

    private int Minimum(int a, int b, int c) {
        int mi;
        mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }

    public int LD(String s, String t) {
        int d[][];
        int n;
        int m;
        int i;
        int j;
        char s_i;
        char t_j;
        int cost;
        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        return d[n][m];
    }

    public double caculateSimilarity(List<String> list) {
        double ret = 0.0;
        int count = 0;
        double sum = 0.0;
        for (int i = 0; i < list.size() - 1; i++) {
            String s1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                String s2 = list.get(j);
                sum += caculateSimilarityBetweenStrings(s1, s2);
                count++;
            }
        }
        if (count > 0) {
            ret = sum / (double) count;
        }
        return ret;
    }

    public double caculateSimilarityBetweenStrings(String s1, String s2) {
        double ret = 0.0;
        int distance = LD(s1, s2);
        int len = s1.length() > s2.length() ? s1.length() : s2.length();
        int x = len - distance;
        ret = 2 * x / (double) (s1.length() + s2.length());
        return ret;
    }
}
