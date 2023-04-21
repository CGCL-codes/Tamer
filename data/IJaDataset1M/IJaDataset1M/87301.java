package mrusanov.fantasyruler.map.pathfinding.heuristics;

import mrusanov.fantasyruler.map.pathfinding.AStarHeuristic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MrusanovHeuristicTest {

    private AStarHeuristic heuristic;

    @Before
    public void init() {
        heuristic = new MrusanovHeuristic();
    }

    @Test
    public void testHeuristic() {
        float result = heuristic.getCost(0, 0, 2, 3);
        Assert.assertEquals(3.8, result, 0.1);
    }
}
