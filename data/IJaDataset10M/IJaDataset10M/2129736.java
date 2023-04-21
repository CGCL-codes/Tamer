package org.eiichiro.jazzmaster;

import static org.eiichiro.jazzmaster.Version.*;

/**
 * {@code Main} is a command line interface to print the information about this 
 * Jazzmaster build.
 *
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Main {

    /**
	 * Prints out the information about this Jazzmaster build.
	 * 
	 * @param args The command line arguments.
	 */
    public static void main(String[] args) {
        System.out.println("Jazzmaster " + MAJOR + "." + MINER + "." + BUILD);
        System.out.println("Copyright (C) 2009-2010 Eiichiro Uchiumi. All Rights Reserved.");
    }
}
