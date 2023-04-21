package edu.ucla.stat.SOCR.util;

import java.awt.*;
import edu.ucla.stat.SOCR.distributions.*;

/**This class models a special graph used in the hypothesis testing experiment for the variance
in the standard normal model.*/
public class VarianceTestGraph extends RandomVariableGraph {

    private double mean, stdDev, testStdDev;

    private IntervalData data;

    /**This general constructor creates a ndw variance test graph with a specified random
	variable and a specified test standard deviation.*/
    public VarianceTestGraph(RandomVariable v, double t) {
        super(v);
        showMoments(0);
        testStdDev = t;
    }

    /**This method paints the graph.*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        int j = getSize().height - 10;
        drawBoxPlot(g, mean - testStdDev, mean - stdDev, mean, mean + stdDev, mean + testStdDev, j);
        if (data.getSize() > 0) {
            g.setColor(Color.red);
            fillBoxPlot(g, mean, data.getSD(), j);
        }
    }

    /**This method sets the test standard deviation.*/
    public void setTestStdDev(double t) {
        testStdDev = t;
    }

    /**This method resets the graph.*/
    public void reset() {
        super.reset();
        RandomVariable v = getRandomVariable();
        mean = v.getDistribution().getMean();
        stdDev = v.getDistribution().getSD();
        data = v.getIntervalData();
    }
}
