package com.csft.client;

public class Merchant implements Comparable<Merchant> {

    public int x;

    public int y;

    public int r1;

    public int r2;

    public int r3;

    public int r4;

    public long minimumDelay;

    public String nextTime;

    public boolean enabled;

    /**
	 * Generate a merchant from its string representation
	 * 
	 * @param s
	 * @return
	 */
    public static Merchant fromString(String s) {
        String[] ss = s.split("\t");
        Merchant m = new Merchant();
        int n = 0;
        m.x = new Integer(ss[n++]);
        m.y = new Integer(ss[n++]);
        m.r1 = new Integer(ss[n++]);
        m.r2 = new Integer(ss[n++]);
        m.r3 = new Integer(ss[n++]);
        m.r4 = new Integer(ss[n++]);
        m.minimumDelay = new Long(ss[n++]);
        m.nextTime = ss[n++];
        m.enabled = new Boolean(ss[n++]);
        return m;
    }

    @Override
    public String toString() {
        return x + "\t" + y + "\t" + r1 + "\t" + r2 + "\t" + r3 + "\t" + r4 + "\t" + minimumDelay + "\t" + nextTime + "\t" + enabled;
    }

    @Override
    public int compareTo(Merchant o) {
        if (x > o.x) {
            return 1;
        } else if (x < o.x) {
            return -1;
        } else if (y > o.y) {
            return 1;
        } else if (y < o.y) {
            return -1;
        } else return 0;
    }
}
