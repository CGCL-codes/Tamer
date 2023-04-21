package net.innig.macker.example.conventions;

import java.io.File;

public final class Main {

    public static void main(String[] args) {
        AlphaTree alpha = new AlphaTree(null, "");
        FactorialTree fact = new FactorialTree();
        FileTree file = new FileTree(File.listRoots()[0]);
        System.out.println("Alpha dump:");
        DepthFirstDump.dump(alpha, 4, 3, "    ");
        System.out.println();
        System.out.println("Factorial dump:");
        DepthFirstDump.dump(fact, 4, 999, "    ");
        System.out.println();
        System.out.println("File dump:");
        DepthFirstDump.dump(file, 4, 4, "    ");
        System.out.println();
        System.out.println("Alpha walk:");
        RandomWalk.walk(alpha, 30);
        System.out.println();
        System.out.println("Factorial walk:");
        RandomWalk.walk(fact, 30);
        System.out.println();
        System.out.println("File walk:");
        RandomWalk.walk(file, 30);
        System.out.println();
    }
}
