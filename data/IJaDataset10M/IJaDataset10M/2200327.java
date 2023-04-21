package de.kumpe.hadooptimizer;

/**
 * Marks a {@link Halter} to be stoppable. Wich means that the evolution cycle
 * can be intentionally canceled from the outside before the actual optimization
 * goal is reached.
 * 
 * @author <a href="http://kumpe.de/christian/java">Christian Kumpe</a>
 */
public interface Stoppable {

    /**
	 * Is called once to tell the {@link Halter} the stop the evoltion cycle at
	 * the end of the current iteration.
	 */
    void stop();
}
