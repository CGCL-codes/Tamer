package ru.adv.bin;

import ru.adv.db.config.*;
import ru.adv.util.*;
import java.io.*;

/**
 * @version $Revision: 1.8 $
 */
public class Prepare {

    private Prepare() {
    }

    /**
	 * Используется для тестирования.
	 */
    public static void main(String[] args) {
        try {
            StringBuffer fromFile = new StringBuffer();
            StringBuffer toFile = new StringBuffer();
            parseArgs(args, fromFile, toFile);
            System.out.println("from=" + fromFile);
            System.out.println("to=" + toFile);
            File f = new File(fromFile.toString());
            String abs = f.getAbsolutePath();
            InputOutput from = InputOutput.create(abs);
            f = new File(toFile.toString());
            abs = f.getAbsolutePath();
            InputOutput to = InputOutput.create(abs);
            new ConfigParser().parse(from, to, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args, StringBuffer from, StringBuffer to) {
        if (args.length < 1) usage();
        String f = null, t = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String option = args[i];
                option = option.substring(1);
                if (i + 1 >= args.length) usage();
                String value = args[++i];
                if (option.equals("f")) {
                    f = value;
                }
                if (option.equals("t")) {
                    t = value;
                }
            } else {
                usage();
                break;
            }
        }
        if (f == null || t == null) usage();
        from.append(f);
        to.append(t);
    }

    private static void usage() {
        System.out.println("usage:\n\tjava ru.adv.bin.Prepare -f <config file> -t <config file>");
        System.exit(-1);
    }
}
