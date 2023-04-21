package jeco.dmm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import jeco.kernel.operator.comparator.SolutionDominance;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.problem.Variable;
import jeco.kernel.util.Util;

/**
 * Once the results were obtained and exported to text files, we reuse
 * previous code to obtain quality measures.
 *
 * This class takes a text file where each solution is represented by the values
 * of the objectives (separated by " "), translates each solution into a Solution
 * object, and obtains the reference front.
 *
 * @author J. M. Colmenar
 */
public class GetReferenceFrontFromFile {

    private static Logger logger = Logger.getLogger(GetReferenceFrontFromFile.class.getName());

    private static Solutions<Variable> solutions;

    public GetReferenceFrontFromFile(String fileName) {
        try {
            solutions = Util.readFrontFromFile(fileName);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Input file trouble", ex);
        }
    }

    private void obtainFront() {
        SolutionDominance<Variable> comparator = new SolutionDominance<Variable>();
        solutions.keepParetoNonDominated(comparator);
    }

    /** Write front both to the terminal and to a file **/
    private void writeFront(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
            Iterator<Solution<Variable>> iter = solutions.iterator();
            while (iter.hasNext()) {
                Solution<Variable> sol = iter.next();
                for (int i = 0; i < sol.getObjectives().size(); i++) {
                    String data = Double.toString(sol.getObjectives().get(i)) + " ";
                    System.out.print(data);
                    writer.write(data);
                }
                System.out.println();
                writer.write("\n");
                writer.flush();
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GetReferenceFrontFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int index = 0; index < handlers.length; index++) {
            handlers[index].setLevel(Level.INFO);
        }
        if (args.length != 2) {
            String bench = "cfrac";
            args = new String[2];
            args[0] = "test" + File.separator + bench + "_all_solutions_plain.txt";
            args[1] = "test" + File.separator + bench + "_ref_front.txt";
            System.out.println("\nUsage:");
            System.out.println("GetReferenceFrontFromFile <all_solutions_plain_file> <output_ref_front_file>");
            return;
        }
        GetReferenceFrontFromFile refFront = new GetReferenceFrontFromFile(args[0]);
        refFront.obtainFront();
        refFront.writeFront(args[1]);
    }
}
