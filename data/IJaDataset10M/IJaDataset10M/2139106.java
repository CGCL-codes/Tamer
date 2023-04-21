package samples.tasks.text;

import taskgraph.channels.CharChannel;
import taskgraph.tasks.Graph;
import taskgraph.tasks.Task.Wait;
import taskgraph.tasks.io.ReadTextFile;
import taskgraph.tasks.io.StandardOutput;
import taskgraph.tasks.text.GrepLines;

/**
 * Greps the input file.
 *  
 * @author Armando Blancas
 */
public class Grep {

    /**
     * Creates a graph that greps the input text file.
     * <p><pre>
     * [ReadText] --ch1--> [GrepLines] --ch2--> [Stdout]
     * </pre>
	 */
    public static final void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java samples.tasks.text.Grep <pattern> <file>");
            System.exit(1);
        }
        System.out.printf("Looking for %s in %s:\n", args[0], args[1]);
        try {
            Graph graph = new Graph();
            CharChannel ch1 = new CharChannel();
            CharChannel ch2 = new CharChannel();
            graph.add(new ReadTextFile(ch1.getOutputPort(), args[1]));
            graph.add(new GrepLines(ch1.getInputPort(), ch2.getOutputPort(), args[0]));
            graph.add(new StandardOutput(ch2.getInputPort()));
            graph.execute(Wait.NO_WAIT);
        } catch (InterruptedException e) {
            System.err.printf("Interrupted:\n%s\n", e.getMessage());
        }
    }
}
