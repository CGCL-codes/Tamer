package s02;

import java.util.Random;
import btree.BTree;
import btree.BTreeItr;

public class ExoBTree {

    public static int size(BTree t) {
        return size(t.root());
    }

    private static int size(BTreeItr ti) {
        if (ti.isBottom()) return 0;
        return 1 + size(ti.left()) + size(ti.right());
    }

    private static int computeHeight(BTreeItr treeItr) {
        if (treeItr.isBottom()) {
            return 0;
        }
        return 1 + Math.max(computeHeight(treeItr.left()), computeHeight(treeItr.right()));
    }

    public static int computeHeight(BTree t) {
        return computeHeight(t.root());
    }

    private static Random rnd = new Random();

    private static void shuffle(int[] t, Random r) {
        for (int i = 1; i < t.length; i++) {
            int j = r.nextInt(i);
            int a = t[i];
            t[i] = t[j];
            t[j] = a;
        }
    }

    public static BTree rndTree(int size) {
        int i = 0;
        BTree t = new BTree();
        BTreeItr ti;
        int[] tab = new int[size];
        for (i = 0; i < size; i++) tab[i] = i;
        shuffle(tab, rnd);
        for (i = 0; i < size; i++) {
            ti = t.root();
            while (!ti.isBottom()) {
                if (rnd.nextInt(2) == 0) ti = ti.left(); else ti = ti.right();
            }
            ti.insert(new Integer(tab[i]));
        }
        return t;
    }

    public static void main(String[] args) {
        checkAssert();
        int nbOfNodes = 10;
        if (args.length > 0) nbOfNodes = Integer.parseInt(args[0]);
        BTree t = rndTree(nbOfNodes);
    }

    public static void checkAssert() {
        int ec = 0;
        assert (ec = 1) == 1;
        if (ec != 0) return;
        System.out.println("WARNING: assertions are disabled ('java -ea...')");
    }
}
