package net.sourceforge.xquench;

import net.sourceforge.xquench.parser.Parser;
import net.sourceforge.xquench.parser.SimpleNode;
import java.io.*;
import java.util.Vector;

public class XQuench {

    public static void main(String args[]) throws Exception {
        Parser ql = null;
        if (args.length == 0) {
            System.out.println("--Input: STANDARD INPUT");
            ql = new Parser(System.in);
        } else {
            System.out.println("--Input: " + args[0]);
            ql = new Parser(new FileInputStream(args[0]));
        }
        try {
            ql.Start();
            System.out.println("--Good Input!");
            ql.getRootNode().dump(">");
        } catch (Exception e) {
            System.out.println("--Bad Input!");
            System.out.println(e.getMessage());
        }
    }
}
