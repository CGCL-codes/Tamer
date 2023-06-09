package jdd.zdd;

import java.io.*;
import jdd.util.*;
import jdd.bdd.NodeTable;

/**
 * Helper class for printing Z-BDD graphs, as text/sets/DOT files.
 *
 */
public class ZDDPrinter {

    private static NodeTable nt;

    private static PrintStream ps;

    private static final int NODE_MASK = 0x7FFFFFFF, DOT_MARK = 0x80000000;

    private static boolean had_0, had_1;

    private static NodeName nn;

    private static void helpGC() {
        nt = null;
        ps = null;
        nn = null;
    }

    static void print(int dd, NodeTable nt, NodeName nn) {
        if (dd == 0) JDDConsole.out.println("0. " + nn.zero()); else if (dd == 1) JDDConsole.out.println("1. " + nn.one()); else {
            ZDDPrinter.nt = nt;
            ZDDPrinter.nn = nn;
            print_rec(dd);
            nt.unmark_tree(dd);
            helpGC();
            JDDConsole.out.println();
        }
    }

    private static void print_rec(int dd) {
        if (dd == 0 || dd == 1) return;
        if (nt.isNodeMarked(dd)) return;
        JDDConsole.out.println("" + dd + ". " + nn.variable(nt.getVar(dd)) + ": " + nt.getLow(dd) + ", " + nt.getHigh(dd));
        nt.mark_node(dd);
        print_rec(nt.getLow(dd));
        if (nt.getLow(dd) != nt.getHigh(dd)) print_rec(nt.getHigh(dd));
    }

    static void printDot(String filename, int zdd, NodeTable nt, NodeName nn) {
        try {
            ps = new PrintStream(new FileOutputStream(filename));
            ps.println("digraph G {");
            ps.println("\tcenter = true;");
            ps.println("\tnodesep = 0.05;");
            ZDDPrinter.had_0 = ZDDPrinter.had_1 = false;
            ZDDPrinter.nt = nt;
            ZDDPrinter.nn = nn;
            ps.println("\tinit__ [label=\"\", style=invis, height=0, width=0];");
            ps.println("\tinit__ -> " + zdd + ";");
            printDot_rec(zdd);
            if (had_0 && had_1) ps.println("\t{ rank = same; 0; 1;}");
            if (had_0) ps.println("\t0 [shape=box, label=\"" + nn.zeroShort() + "\", style=filled, height=0.3, width=0.3];");
            if (had_1) ps.println("\t1 [shape=box, label=\"" + nn.oneShort() + "\", style=filled, height=0.3, width=0.3];\n");
            ps.println("}\n");
            nt.unmark_tree(zdd);
            ps.close();
            Dot.showDot(filename);
            helpGC();
        } catch (IOException exx) {
            JDDConsole.out.println("ZDDPrinter.printDOT failed: " + exx);
        }
    }

    private static void printDot_rec(int zdd) {
        if (zdd == 0) {
            had_0 = true;
            return;
        }
        if (zdd == 1) {
            had_1 = true;
            return;
        }
        if (nt.isNodeMarked(zdd)) return;
        int low = nt.getLow(zdd);
        int high = nt.getHigh(zdd);
        int var = nt.getVar(zdd);
        nt.mark_node(zdd);
        ps.println("\t" + zdd + "[label=\"" + nn.variable(var) + "\"];");
        ps.println("\t" + zdd + "-> " + low + " [style=dotted];");
        ps.println("\t" + zdd + "-> " + high + " [style=filled];");
        printDot_rec(low);
        if (low != high) printDot_rec(high);
    }

    private static char[] set_chars = null;

    private static int max, count;

    static void printSet(int zdd, NodeTable nt, NodeName nn) {
        if (zdd < 2) {
            if (nn != null) JDDConsole.out.println((zdd == 0) ? nn.zero() : nn.one()); else JDDConsole.out.println((zdd == 0) ? "empty" : "base");
        } else {
            int max_ = 2 + nt.getVar(zdd);
            if (ZDDPrinter.set_chars == null || ZDDPrinter.set_chars.length < max_) ZDDPrinter.set_chars = Allocator.allocateCharArray(max_);
            ZDDPrinter.count = 0;
            ZDDPrinter.nn = nn;
            ZDDPrinter.nt = nt;
            JDDConsole.out.print("{ ");
            printSet_rec(zdd, 0, nt.getVar(zdd));
            JDDConsole.out.println(" }");
            helpGC();
        }
    }

    private static void printSet_rec(int zdd, int level, int top) {
        if (zdd == 0) return;
        if (zdd == 1 && top < 0) {
            if (count != 0) JDDConsole.out.print(", ");
            count++;
            if (nn != null) {
                int got = 0;
                for (int i = 0; i < level; i++) if (set_chars[i] == '1') {
                    JDDConsole.out.print(nn.variable(level - i - 1));
                    got++;
                }
                if (got == 0) JDDConsole.out.print(nn.one());
            } else {
                for (int i = 0; i < level; i++) JDDConsole.out.print(set_chars[i]);
            }
            return;
        }
        top--;
        if (nt.getVar(zdd) <= top) {
            set_chars[level] = '0';
            printSet_rec(zdd, level + 1, top);
            return;
        }
        set_chars[level] = '0';
        printSet_rec(nt.getLow(zdd), level + 1, top);
        set_chars[level] = '1';
        printSet_rec(nt.getHigh(zdd), level + 1, top);
    }
}
