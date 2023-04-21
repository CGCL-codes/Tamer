package org.jgrapht.alg;

import java.util.*;
import junit.framework.*;
import org.jgrapht.*;

/**
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
@SuppressWarnings("unchecked")
public class KShortestPathKValuesTest extends TestCase {

    /**
	 * @param n
	 * 
	 * @return n!.
	 */
    public static int factorial(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

    /**
	 * @param k
	 * @param n
	 * 
	 * @return A(n,k).
	 */
    public static int permutation(int n, int k) {
        if (k <= n) {
            return factorial(n) / factorial(n - k);
        } else {
            return 0;
        }
    }

    public void testMaxSizeValue() {
        KShortestPathCompleteGraph6 graph = new KShortestPathCompleteGraph6();
        for (int maxSize = 1; maxSize <= calculateNbElementaryPathsForCompleteGraph(6); maxSize++) {
            KShortestPaths finder = new KShortestPaths(graph, "vS", maxSize);
            assertEquals(finder.getPaths("v1").size(), maxSize);
            assertEquals(finder.getPaths("v2").size(), maxSize);
            assertEquals(finder.getPaths("v3").size(), maxSize);
            assertEquals(finder.getPaths("v4").size(), maxSize);
            assertEquals(finder.getPaths("v5").size(), maxSize);
        }
    }

    public void testNbReturnedPaths() {
        KShortestPathCompleteGraph4 kSPCompleteGraph4 = new KShortestPathCompleteGraph4();
        verifyNbPathsForAllVertices(kSPCompleteGraph4);
        KShortestPathCompleteGraph5 kSPCompleteGraph5 = new KShortestPathCompleteGraph5();
        verifyNbPathsForAllVertices(kSPCompleteGraph5);
        KShortestPathCompleteGraph6 kSPCompleteGraph6 = new KShortestPathCompleteGraph6();
        verifyNbPathsForAllVertices(kSPCompleteGraph6);
    }

    private int calculateNbElementaryPathsForCompleteGraph(int n) {
        int nbPaths = 0;
        for (int k = 1; k <= (n - 1); k++) {
            nbPaths = nbPaths + permutation(n - 2, k - 1);
        }
        return nbPaths;
    }

    private void verifyNbPathsForAllVertices(Graph graph) {
        int nbpaths = calculateNbElementaryPathsForCompleteGraph(graph.vertexSet().size());
        int maxSize = Integer.MAX_VALUE;
        for (Iterator sourceIterator = graph.vertexSet().iterator(); sourceIterator.hasNext(); ) {
            Object sourceVertex = sourceIterator.next();
            KShortestPaths finder = new KShortestPaths(graph, sourceVertex, maxSize);
            for (Iterator targetIterator = graph.vertexSet().iterator(); targetIterator.hasNext(); ) {
                Object targetVertex = targetIterator.next();
                if (targetVertex != sourceVertex) {
                    assertEquals(finder.getPaths(targetVertex).size(), nbpaths);
                }
            }
        }
    }
}
