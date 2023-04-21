package org.lirc.test;

import org.lirc.*;

/** Listens for IR signals and prints them to System.out.
	Similar in function to the <code>irw</code> program
	in the LIRC package.

	@version $Revision: 1.3 $
	@author Bjorn Bringert (bjorn@mumblebee.com)
*/
public class Irw {

    /** Prints a few lines of info.	*/
    public static void printInstructions() {
        System.out.println("The program will listen for IR signals on the lircd socket and print them.");
        System.out.println("The lircd daemon must be running for the program to work.");
    }

    /** Runs Irw, use <code>-ins</code> to display a few lines of
		info when starting the program.
	*/
    public static void main(String[] args) {
        if (args.length > 0 && args[0].indexOf("ins") > -1) {
            printInstructions();
        }
        try {
            Receiver rec = ReceiverFactory.createReceiver();
            try {
                while (true) {
                    System.out.println(rec.readCode());
                }
            } finally {
                rec.close();
            }
        } catch (LIRCException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
    }
}
