package org.tex4java;

/**
 * The main class of TeXPresenter
 *
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.3 $
 */
public class TeXPresenter {

    private static boolean latex;

    private static String fontDir = null;

    private static String libDir = null;

    private static String confDir = null;

    /**
   * The main function. Runs TeXPresenter.
   * 
   * @param args String[]
   */
    public static void main(String[] args) throws Exception {
        Manager manager;
        String filename = parseArguments(args);
        if ((libDir == null) || (confDir == null)) {
            System.out.println(usage());
            return;
        }
        try {
            manager = new Manager(fontDir, libDir, confDir, latex);
            manager.setInputFile(filename);
            manager.run();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static String parseArguments(String[] args) {
        String file = null;
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("--tex")) {
                latex = false;
            }
            if (arg.equals("--latex")) {
                latex = true;
            }
            if (arg.equals("--fonts")) {
                if (args.length > i + 1) {
                    fontDir = args[i + 1];
                } else {
                    throw new IllegalArgumentException(usage());
                }
            }
            if (arg.equals("--lib")) {
                if (args.length > i + 1) {
                    libDir = args[i + 1];
                } else {
                    throw new IllegalArgumentException(usage());
                }
            }
            if (arg.equals("--conf")) {
                if (args.length > i + 1) {
                    confDir = args[i + 1];
                } else {
                    throw new IllegalArgumentException(usage());
                }
            }
            if (arg.equals("--file")) {
                if (args.length > i + 1) {
                    file = args[i + 1];
                } else {
                    throw new IllegalArgumentException(usage());
                }
            }
        }
        return file;
    }

    /**
   * Returns a usage message.
   * 
   * @return a <code>String</code> value
   */
    private static String usage() {
        return "TeXPresenter presentation system\n" + "Usage: java org.tex4java.TeXPresenter [--file <file>] " + "[--latex] [--tex] [--fonts <dir>] --lib <dir> --conf <dir>";
    }
}
