package traviaut.data;

import java.util.Arrays;

public class Stock {

    private interface StockOp {

        int func(int idx, int val);
    }

    private interface StockOp2 {

        int func(int idx, int val1, int val2);
    }

    public static final int length = 4;

    public static final int CROP = 3;

    private final int[] data = new int[length];

    public Stock() {
    }

    public Stock(int[] st) {
        System.arraycopy(st, 0, data, 0, Math.min(data.length, st.length));
    }

    public Stock(int init) {
        Arrays.fill(data, init);
    }

    public String toString() {
        return "[" + data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "]";
    }

    public boolean isNegativeCrop() {
        return data[CROP] < 0;
    }

    public int getCrop() {
        return data[CROP];
    }

    public void zeroCrop() {
        data[CROP] = 0;
    }

    public int[] getData() {
        return data.clone();
    }

    private Stock apply(StockOp op) {
        int[] res = data.clone();
        for (int i = 0; i < length; i++) {
            res[i] = op.func(i, data[i]);
        }
        return new Stock(res);
    }

    private Stock apply2(Stock st, StockOp2 op) {
        int[] res = data.clone();
        for (int i = 0; i < length; i++) {
            res[i] = op.func(i, data[i], st.data[i]);
        }
        return new Stock(res);
    }

    public void add(Stock s) {
        for (int i = 0; i < length; i++) data[i] += s.data[i];
    }

    public Stock addToCopy(Stock s) {
        return apply2(s, new StockOp2() {

            public int func(int idx, int val1, int val2) {
                return val1 + val2;
            }
        });
    }

    public Stock sub(Stock s) {
        return apply2(s, new StockOp2() {

            public int func(int idx, int val1, int val2) {
                return val1 - val2;
            }
        });
    }

    public Stock mul(final int m) {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return val * m;
            }
        });
    }

    public Stock div(final int d) {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return val / d;
            }
        });
    }

    public Stock min(Stock s) {
        return apply2(s, new StockOp2() {

            public int func(int idx, int val1, int val2) {
                return val1 < val2 ? val1 : val2;
            }
        });
    }

    public Stock max(final int m) {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return m < val ? m : val;
            }
        });
    }

    public Stock mask(Stock s) {
        return apply2(s, new StockOp2() {

            public int func(int idx, int val1, int val2) {
                return val2 > 0 ? val1 : 0;
            }
        });
    }

    public int val(int i) {
        return data[i];
    }

    public String getValue(int i) {
        return String.valueOf(data[i]);
    }

    public boolean isLessThan(Stock st) {
        for (int i = 0; i < length; i++) {
            if (data[i] > st.data[i]) return false;
        }
        return true;
    }

    public Stock positive() {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return val < 0 ? 0 : val;
            }
        });
    }

    private static int rUp(int val) {
        if (val == 0) return 0;
        int v = val / 100;
        v += 1;
        v *= 100;
        return v;
    }

    private static int rDown(int val) {
        return 100 * (val / 100);
    }

    public Stock roundUp() {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return rUp(val);
            }
        });
    }

    public Stock roundDown() {
        return apply(new StockOp() {

            public int func(int idx, int val) {
                return rDown(val);
            }
        });
    }

    public int total() {
        int ret = 0;
        for (int i = 0; i < length; i++) {
            ret += data[i];
        }
        return ret;
    }
}
