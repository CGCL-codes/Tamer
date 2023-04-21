package fairVote.util;

import java.util.Random;

public class KeyGen extends Random {

    public static final long serialVersionUID = 0;

    public String getRandomKey(int n) {
        return getRandomKey(n, 16);
    }

    public String getRandomKey(int n, int base) {
        String s = "";
        for (int i = 0; i < n; i++) {
            int index = this.nextInt(base);
            if (index < 10) s = s + (char) (index + 48); else s = s + (char) (index + 55);
        }
        return s;
    }
}
