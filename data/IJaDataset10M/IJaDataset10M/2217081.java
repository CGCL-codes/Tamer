package net.anydigit.jiliu.hash;

import java.util.Random;
import net.anydigit.jiliu.balance.AbstractBalance;

/**
 * @author xingfei [xingfei0831 AT gmail.com]
 * 
 */
public class BinarySearchCeilTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        long[] keys = new long[2093];
        Random r = new Random();
        int current = r.nextInt(10000);
        for (int i = 0; i < keys.length; i++) {
            keys[i] = current;
            current += r.nextInt(10000);
        }
        long k = keys[323];
        int m = AbstractBalance.binarySearchCeil(k, keys);
        System.out.println("search " + k);
        System.out.println("found index " + m);
        System.out.println("m-1:" + keys[m - 1] + ", m:" + keys[m] + ", m+1:" + keys[m + 1]);
    }
}
