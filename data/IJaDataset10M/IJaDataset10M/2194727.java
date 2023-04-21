package plugin.searchingStrategy.distance;

public class LevenshteinDistance {

    public static int getDistance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] d = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) != str2.charAt(j - 1)) {
                    int k = Math.min(d[i][j - 1], d[i - 1][j]);
                    int l = Math.min(k, d[i - 1][j - 1]);
                    d[i][j] = l + 1;
                } else {
                    d[i][j] = d[i - 1][j - 1];
                }
            }
        }
        return d[m][n];
    }

    public static float getSimilarity(String str1, String str2) {
        float levensteinDistance = getDistance(str1, str2);
        float len;
        if (str1.length() > str2.length()) {
            len = (float) str1.length();
        } else {
            len = (float) str2.length();
        }
        if (len == 0) {
            return (float) 1;
        } else {
            return (float) 1 - (levensteinDistance / len);
        }
    }
}
