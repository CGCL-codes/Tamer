package brightwell.tools;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import brightwell.analyser.Tool;
import brightwell.gui.drawingplane.DrawingPlane;
import cholsey.Neuron;

public class FiringPattern extends Tool {

    private static String PLOT_START_INDEX = "plot start index";

    private static String PLOT_COLOR = "plot color";

    private DrawingPlane dpAverage = null;

    private DrawingPlane dpNeurons = null;

    private double lastValue = 0;

    private int plotStartIndex = 0;

    private boolean plotColor = false;

    private Color lightGray = new Color(200, 200, 200);

    public void init() {
        setToolPriority(-8);
        addInteger(PLOT_START_INDEX, 0, Integer.MAX_VALUE, 0);
        addCheckBox(PLOT_COLOR, false);
    }

    public boolean needsNet() {
        return true;
    }

    public String getToolName() {
        return "Firing Pattern";
    }

    public String getToolDescription() {
        return "Plot of Firing Pattern";
    }

    public void doAnalysis() {
        plotStartIndex = getInteger(PLOT_START_INDEX);
        plotColor = getCheckBox(PLOT_COLOR);
        dpAverage = getNewWindow("Firing Pattern", plotStartIndex, convergenceIterations, yStart, yEnd);
        dpNeurons = getNewWindow("Neuron Pattern", plotStartIndex, convergenceIterations, 0, net.neurons().size());
        int height = dataStorage.getWindowSize()[1];
        dpNeurons.setLocation(0, height);
        calculateFiringPatternter();
    }

    private void calculateFiringPatternter() {
        File file = null;
        PrintWriter out = null;
        if (dataStorage.getInialActivityMode() == 1) {
            net = dataStorage.getNet().copy();
        } else {
            net.randomInitActivity();
        }
        int outputNeuronsSize = net.getOutputNeurons().size();
        for (int step = 0; step < convergenceIterations + 1; step++) {
            net.process();
            if (step < plotStartIndex) {
                continue;
            }
            double value = 0;
            for (net.neurons().start(); net.neurons().hasMore(); net.neurons().next()) {
                value += net.neurons().neuron().getOutput();
            }
            value /= net.neurons().size();
            if (step > plotStartIndex) {
                dpAverage.drawLine(step - 1, lastValue, step, value, Color.black);
                if (step < convergenceIterations) {
                    Color color = Color.black;
                    if (plotColor) {
                        float colorGradient = (float) (1.0d - Math.abs(value));
                        colorGradient = Math.max(0.0f, Math.min(1.0f, colorGradient));
                        if (value > 0) {
                            color = new Color(1.0f, colorGradient, colorGradient);
                        } else {
                            color = new Color(colorGradient, colorGradient, 1.0f);
                        }
                    }
                    dpAverage.drawFilledCircle(step, value, 15, color);
                }
            }
            dpNeurons.drawLine(step, 0, step, net.neurons().size(), lightGray);
            for (net.neurons().start(); net.neurons().hasMore(); net.neurons().next()) {
                Neuron n = net.neurons().neuron();
                if (plotColor) {
                    float colorGradient = (float) (1.0d - Math.abs(n.getOutput()));
                    colorGradient = Math.max(0.0f, Math.min(1.0f, colorGradient));
                    Color color = null;
                    if (n.getOutput() > 0) {
                        color = new Color(1.0f, colorGradient, colorGradient);
                    } else {
                        color = new Color(colorGradient, colorGradient, 1.0f);
                    }
                    if (n.getOutput() > 0) {
                        dpNeurons.drawFilledCircle(step, n.id() + 0.5, 15, color);
                    } else {
                        dpNeurons.drawFilledCircle(step, n.id() + 0.5, 15, color);
                    }
                } else {
                    if (n.getOutput() > 0) {
                        dpNeurons.drawFilledCircle(step, n.id() + 0.5, 15, Color.black);
                    }
                }
            }
            lastValue = value;
        }
        dpAverage.drawLegend();
    }
}
