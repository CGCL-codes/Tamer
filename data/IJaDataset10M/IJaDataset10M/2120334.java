package net.sourceforge.cobertura.util;

import java.io.PrintStream;

public abstract class Copyright {

    public static final int NAME = 0;

    public static final int YEARS = 1;

    public static final String[][] COPYRIGHT = new String[][] { { "jcoverage ltd.", "2003" }, { "Mark Doliner <thekingant@users.sourceforge.net>", "2005" }, { "Ludovic Dewailly <ludovic.dewailly@dreameffect.org>", "2005, 2006" } };

    public static void print(PrintStream out) {
        out.println("Cobertura " + "1.2");
        for (int i = 0; i < COPYRIGHT.length; i++) {
            out.println("Copyright (C) " + COPYRIGHT[i][YEARS] + " " + COPYRIGHT[i][NAME]);
        }
        out.println("Cobertura is licensed under the GNU General Public License");
        out.println("Cobertura comes with ABSOLUTELY NO WARRANTY");
    }
}
