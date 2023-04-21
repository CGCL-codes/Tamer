package org.eiichiro.acidhouse;

import static org.eiichiro.acidhouse.Version.*;

/**
 * {@code Main} is a command line interface to print the information about this 
 * Acid House build.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Main {

    /**
	 * Prints out the information about this Acid House build.
	 * 
	 * @param args The command line arguments.
	 */
    public static void main(String[] args) {
        System.out.println("Acid House " + MAJOR + "." + MINER + "." + BUILD);
        System.out.println("Copyright (C) 2009-2011 Eiichiro Uchiumi. All Rights Reserved.");
    }
}
