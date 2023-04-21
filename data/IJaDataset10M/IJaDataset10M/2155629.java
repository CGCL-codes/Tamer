package org.alcibiade.eternity.editor.solver.swap;

import java.util.LinkedList;
import java.util.Queue;

public class SwappingStats {

    private int windowSize;

    private Queue<Integer> queue;

    public SwappingStats(int windowSize) {
        this.windowSize = windowSize;
        queue = new LinkedList<Integer>();
    }

    public int countOccurrences(int coord) {
        int result = 0;
        for (Integer i : queue) {
            if (i == coord) {
                result += 1;
            }
        }
        return result;
    }

    public void recordSwap(SwapCoords coords) {
        queue.add(coords.getCoordA());
        queue.add(coords.getCoordB());
        while (queue.size() > windowSize) {
            queue.remove();
        }
    }

    public void reset() {
        queue.clear();
    }
}
