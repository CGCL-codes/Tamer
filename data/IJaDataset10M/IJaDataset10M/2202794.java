package OGCBenchmarks.splunc;

import java.util.*;

public class Splunc {

    private static class NodeA extends Node {
    }

    private static class NodeB extends Node {

        public int x1 = 0;
    }

    private static class NodeC extends Node {

        public int x1 = 0;

        public int x2 = 0;
    }

    private static class NodeD extends Node {

        public double x1 = 0;

        public double x2 = 0;

        public double x3 = 0;

        public double x4 = 0;

        public double x5 = 0;

        public double x6 = 0;

        public double x7 = 0;

        public double x8 = 0;
    }

    static int date = 0;

    public static void main(String[] args) {
        args = new String[4];
        args[0] = ((int) System.currentTimeMillis()) + "";
        args[1] = 170 + "";
        args[2] = 160 + "";
        args[3] = 5 + "";
        int max_size = Integer.parseInt(args[1]);
        Node tree = new NodeA();
        tree.birthday = date++;
        tree.value = max_size;
        for (int j = 2; j + 2 <= args.length; j += 2) {
            int N = Integer.parseInt(args[j]);
            int depth = Integer.parseInt(args[j + 1]);
            for (int i = 0; i < N; i++) {
                Node node;
                int value = i % N;
                if (value < 1) {
                    node = new NodeA();
                } else if (value < 2) {
                    node = new NodeB();
                } else if (value < 3) {
                    node = new NodeC();
                } else {
                    node = new NodeD();
                }
                node.birthday = date++;
                node.value = max_size;
                tree = Node.insert(tree, node);
                Node.trunc(date, tree, depth);
            }
            System.out.println(Runtime.getRuntime().gcCount());
        }
    }
}
