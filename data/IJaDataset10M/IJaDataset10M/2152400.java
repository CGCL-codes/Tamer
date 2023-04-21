package edu.lsu.cct.piraha.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import edu.lsu.cct.piraha.DebugOutput;
import edu.lsu.cct.piraha.Grammar;
import edu.lsu.cct.piraha.Matcher;

public class Generic {

    public static void usage() {
        System.err.println("usage: Generic [--perl|--python|--xml] grammar text");
        System.exit(2);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) usage();
        Grammar g = new Grammar();
        int n = 0;
        String suffix = ".pegout";
        while (args[n].startsWith("--")) {
            if (args[n].equals("--python")) suffix = ".py"; else if (args[n].equals("--perl")) suffix = ".pm"; else if (args[n].equals("--xml")) suffix = ".xml";
            n++;
        }
        String grammarFile = args[n++];
        System.out.println("Grammar file: " + grammarFile);
        if (!grammarFile.endsWith(".peg")) usage();
        g.compileFile(new File(grammarFile));
        for (; n < args.length; n++) {
            String s = args[n];
            int nn = args[n].lastIndexOf('.');
            if (nn >= 0) s = s.substring(0, nn);
            s = s + suffix;
            System.out.println("reading file: " + args[n]);
            System.out.println("writing file: " + s);
            if (s.equals(args[n])) throw new IOException("won't over-write input file " + args[n]);
            if (s.equals(grammarFile)) throw new IOException("won't over-write grammar file " + grammarFile);
            Matcher m = g.matcher(Grammar.readContents(new File(args[n])));
            if (m.match(0)) {
                FileWriter fw = new FileWriter(s);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                DebugOutput dout = new DebugOutput(pw);
                if (suffix.equals(".py")) m.dumpMatchesPython("VAR", dout); else if (suffix.equals(".pm")) m.dumpMatchesPerl("my $VAR", dout); else if (suffix.equals(".xml")) m.dumpMatchesXML(dout); else if (suffix.equals(".pegout")) m.dumpMatches(dout);
                dout.flush();
                pw.close();
            } else {
                System.err.println("In file[" + n + "] " + args[n] + " " + m.near());
                break;
            }
        }
    }
}
