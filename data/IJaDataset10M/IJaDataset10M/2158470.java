package edu.mit.osidutil.tty;

import java.io.IOException;
import jline.ConsoleReader;

/**
 *  <p>
 *  Fun ith consoles. The JLine library is used
 *  to get user input that can mask password entry. This requires a 
 *  callout to a DLL on the windows side. The other unix-i use stty.
 *  </p><p>
 *  CVS $Id: Console.java,v 1.1 2005/08/25 15:45:55 tom Exp $
 *  </p>
 *  
 *  @author  Tom Coppeto
 *  @version $Revision: 1.1 $
 *  @see java.lang.Object
 */
public class Console extends Object {

    /**
     *  Gets input.
     *
     *  @param prompt the prompt to be displayed
     *  @return the input string, <code>null</code> if input failed
     */
    public static String getInput(final String prompt) {
        try {
            ConsoleReader reader = new ConsoleReader();
            String input = reader.readLine(prompt);
            return (input);
        } catch (IOException ie) {
            return (null);
        }
    }

    /**
     *  Gets input and masks the input at the console.
     *  Good for password entry.
     *
     *  @param prompt the prompt to be displayed
     *  @return the input string, <code>null</code> if input failed
     */
    public static String getMaskedInput(final String prompt) {
        try {
            ConsoleReader reader = new ConsoleReader();
            Character mask = new Character((char) 0);
            String input = reader.readLine(prompt, mask);
            return (input);
        } catch (IOException ie) {
            return (null);
        }
    }
}
