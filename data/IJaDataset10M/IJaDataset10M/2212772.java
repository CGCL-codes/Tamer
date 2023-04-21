package p400.srm401;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 06.05.2008
 * Time: 19:02:25
 */
public class DreamingAboutCarrots {

    public int carrotsBetweenCarrots(int x1, int y1, int x2, int y2) {
        int _x1 = Math.min(x1, x2);
        int _x2 = Math.max(x1, x2);
        int _y1 = Math.min(y1, y2);
        int _y2 = Math.max(y1, y2);
        x1 = _x1;
        x2 = _x2;
        y1 = _y1;
        y2 = _y2;
        final int h = y2 - y1;
        final int w = x2 - x1;
        if (h == w) {
            return w - 1;
        }
        int cnt = 0;
        if (w == 0) {
            return h - 1;
        }
        for (int i = x1 + 1; i < x2; i++) {
            int v = (h * i) / w;
            if (v * w == h * i) {
                cnt++;
            }
        }
        return cnt;
    }

    public int gcd(int a, int b) {
        System.out.println("" + a + " " + b);
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static void main(String[] args) {
        System.out.println(new DreamingAboutCarrots().gcd(36, 42));
    }
}
