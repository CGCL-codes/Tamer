package org.progeeks.filament.demo;

import java.io.*;
import java.util.*;
import org.progeeks.filament.*;
import org.progeeks.filament.core.DefaultGraph;
import org.progeeks.filament.util.HashTripleStore;

/**
 *
 *  @version   $Revision: 4030 $
 *  @author    Paul Speed
 */
public class Test2 {

    protected static void addFiles(SimpleGraph g, Node parent, File dir) {
        for (File f : dir.listFiles()) {
            Node n = g.newNode();
            parent.addEdge(n, "child");
            n.put("name", f.getName());
            if (f.isDirectory()) addFiles(g, n, f); else n.put("size", f.length());
        }
    }

    public static void dump(Node node, String indent) {
        System.out.println(indent + node.entrySet());
        for (Edge e : node.edges("child")) {
            if (e.getHead().equals(node)) continue;
            dump(e.getHead(), indent + "    ");
        }
    }

    public static void main(String[] args) {
        SimpleGraph g = DefaultGraph.create(HashTripleStore.FACTORY);
        Node root = g.newNode();
        addFiles(g, root, new File("."));
        dump(root, "");
    }
}
