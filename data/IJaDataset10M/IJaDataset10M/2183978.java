package s04;

import java.util.BitSet;
import java.util.Random;

public class BSTTest {

    static boolean shouldLog = false;

    static void rndAddRm(Random r, BST s, BitSet bs, int i) {
        String log = "";
        if (r.nextBoolean()) {
            System.out.println("adds: " + i);
            s.add(new Integer(i));
            bs.set(i);
            log += "\n--- add " + i + "\n" + s.t.toReadableString();
        } else {
            System.out.println("remove: " + i);
            s.remove(new Integer(i));
            bs.clear(i);
            log += "\n--- remove " + i + "\n" + s.t.toReadableString();
        }
        if (shouldLog) System.out.println(log);
    }

    static boolean areSetEqual(BST s, BitSet bs) {
        int l = 0;
        for (int i = 0; i < bs.length(); i++) {
            if (bs.get(i) != s.contains(new Integer(i))) {
                System.out.println("SetOf : " + s);
                System.out.println("BitSet: " + bs);
                System.out.println("Size: " + s.size());
                System.out.println("missing element : " + i);
                return false;
            }
            if (s.contains(new Integer(i))) l++;
        }
        if (l != s.size()) {
            System.out.println("SetOf : " + s);
            System.out.println("BitSet: " + bs);
            System.out.println("Size: " + s.size());
            System.out.println("too much elements...");
            return false;
        }
        return true;
    }

    public static boolean testSet(int n) {
        BST s = new BST();
        BitSet bs = new BitSet();
        Random r = new Random();
        int a = 1;
        for (a = 0; a < n; a++) {
            rndAddRm(r, s, bs, r.nextInt(n));
            System.out.println(s);
            if (!shouldLog && a % (n / 10) == 0) System.out.print(".");
        }
        System.out.println("set size: " + s.size());
        return areSetEqual(s, bs);
    }

    public static void main(String[] args) {
        int n = 10;
        if (args.length > 2) {
            System.out.println("Usage : BSTTest [ n [log] ]");
            System.exit(-1);
        }
        if (args.length == 2) shouldLog = true;
        if (args.length > 0) n = Integer.parseInt(args[0]);
        if (testSet(n)) System.out.println("\nTest passed successfully"); else System.out.println("\nTest failed; there is a bug");
    }
}
