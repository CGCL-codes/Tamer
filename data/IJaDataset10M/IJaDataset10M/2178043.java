package asdtester.asd;

import java.io.*;
import java.lang.reflect.*;

/**
   This provides a self-standing optimizer for ASD grammars,
   taking input from a grammar in its character file representation
   and writing the optimized grammar to a new character file.
   However, since the ASDEditor performs optimization when it saves
   grammars to files, separate use of this ASDOptimizer is not normally
   required.
<BR><BR>
   Command-line usage:
<BR>java -cp asddigraphs.jar asd/ASDOptimizer</b></tt>
<BR>or, if asddigraphs.jar has been put in the system classpath:
<BR><tt><b> java asd/ASDOptimizer</b></tt>
   @author James A. Mason
   @version 1.02 2001 Oct-Nov; 2002 Feb
 */
public class ASDOptimizer {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("File name of grammar to be optimized? ");
        String inFile = readLine();
        System.out.print("Output file name for the optimized grammar? ");
        String outFile = readLine();
        ASDDigraph digraph = new ASDDigraph(inFile, null);
        digraph.saveToFile(outFile);
    }

    static String readLine() {
        String inputLine = null;
        try {
            inputLine = keyboard.readLine();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
        return inputLine;
    }

    private static BufferedReader keyboard;
}
