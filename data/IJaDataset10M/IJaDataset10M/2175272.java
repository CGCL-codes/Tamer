package de.ibk.ods;

import java.util.Hashtable;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class OpenAOS {

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("OpenAOS V5.0.0 (ASAM ODS V5.0)");
        System.out.println("Copyright (c) 2005-2006 Reinhard Kessler, Ing.-Buero Kessler");
        System.out.println("This is free software; see the license/source for copying conditions.");
        System.out.println("There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A");
        System.out.println("PARTICULAR PURPOSE.");
        System.out.println("Report bugs to <reinhard@ibkessler.de>.");
        System.out.println();
        if ("IMPORT".equals(args[0].toUpperCase())) {
            AoImport aoimport = new AoImport(parseArgs(args));
            aoimport.execute(args[args.length - 1]);
        } else if ("INIT".equals(args[0].toUpperCase())) {
            AoInit aoinit = new AoInit(parseArgs(args));
            aoinit.execute(args[args.length - 1]);
        } else if ("START".equals(args[0].toUpperCase())) {
            AoServer aoServer = new AoServer(parseArgs(args));
            aoServer.start();
        }
    }

    /**
	 * 
	 * @param args
	 * @return
	 */
    private static Hashtable parseArgs(String[] args) {
        Hashtable retval = new Hashtable();
        int i = 0;
        while (i < args.length) {
            String key = args[i];
            String value = null;
            if (i < args.length - 1) {
                value = args[i + 1];
            }
            if (key.startsWith("--")) {
                key = key.substring(2);
                i++;
            } else if (key.startsWith("-")) {
                if ("-b".equals(key)) {
                    key = "basemodel";
                    i++;
                } else if ("-c".equals(key)) {
                    key = "configuration";
                    i++;
                } else if ("-f".equals(key)) {
                    key = "factory";
                    i++;
                } else if ("-h".equals(key)) {
                    key = "host";
                    i++;
                } else if ("-l".equals(key)) {
                    key = "logfile";
                    i++;
                } else if ("-p".equals(key)) {
                    key = "port";
                    i++;
                }
            } else {
                key = null;
            }
            if (key != null) {
                retval.put(key, value);
            }
            i++;
        }
        return retval;
    }
}
