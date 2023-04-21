package random;

import mcaplantlr.runtime.ANTLRFileStream;
import ail.others.MAS;
import mcapl.MCAPLSpec;
import mcapl.MCAPLcontroller;
import gov.nasa.jpf.jvm.Verify;

/**
 * Runs a Random Agent MAS from a file.
 * 
 * @author louiseadennis
 *
 */
public class Random {

    /**
	 * The main method sets up the MAS and then runs it.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Verify.beginAtomic();
        int outputlevel = 1;
        if (args.length > 1) {
            outputlevel = Integer.parseInt(args[1]);
        }
        try {
            MAS mas = (new RandomMASBuilder(new ANTLRFileStream(args[0]))).getMAS();
            MCAPLSpec s = new MCAPLSpec();
            MCAPLcontroller mccontrol = new MCAPLcontroller(mas, s, outputlevel);
            Verify.endAtomic();
            mccontrol.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
