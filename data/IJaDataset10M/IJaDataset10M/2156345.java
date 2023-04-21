package brightwell.tools;

import brightwell.analyser.Tool;
import brightwell.gui.drawingplane.DrawingPlane;
import brightwell.gui.Error;
import cholsey.Neuron;
import cholsey.Synapse;
import java.awt.Color;
import java.awt.Component;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Logger;
import util.misc.IseeLogger;

public class IsoperiodicPlot extends Tool {

    private static Logger log = IseeLogger.getLogger(IsoperiodicPlot.class);

    private static final String MAX_DIFF_STRING = "max. diff";

    private static final int SYNAPSE_0 = 0;

    private static final int SYNAPSE_1 = 1;

    private static final int NEURON_0 = 2;

    private static final int NEURON_1 = 3;

    private int parameterX = 0;

    private int parameterY = 0;

    private Synapse synapse0 = null;

    private Synapse synapse1 = null;

    private Neuron neuron0 = null;

    private Neuron neuron1 = null;

    private Vector periodVector = null;

    private DrawingPlane dp = null;

    private Color[] colors = null;

    private double maxActivationDiff = 0;

    private Color[] yColor = { Color.black, Color.white };

    private String[] comboBoxEntries = { "colour", "grayscale" };

    private static final Color[] COLOR_ARRAY = { Color.white, Color.blue, Color.cyan, Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow, Color.black };

    private static final Color[] GRAYSCALE_ARRAY = { Color.white, new Color(200, 200, 200), new Color(160, 160, 160), new Color(140, 140, 140), new Color(110, 110, 110), new Color(90, 90, 90), new Color(70, 70, 70), new Color(55, 55, 55), new Color(40, 40, 40), Color.black };

    public boolean needsNet() {
        return true;
    }

    public void doAnalysis() {
        int colorMode = getComboBoxIndex("color mode");
        String biasString = getString("bias");
        String weightString = getString("weight");
        maxActivationDiff = getDouble(MAX_DIFF_STRING);
        log.debug("selectedColor = " + comboBoxEntries[colorMode]);
        log.debug("biasString = " + biasString);
        log.debug("weightString = " + weightString);
        log.debug("maxActivationDiff = " + maxActivationDiff);
        if (biasString.equals("") && weightString.equals("")) {
            Error.attractorParseError(parent);
            return;
        }
        switch(colorMode) {
            case 0:
                colors = COLOR_ARRAY;
                break;
            case 1:
                colors = GRAYSCALE_ARRAY;
                break;
        }
        String title = extractIndexInformation(biasString, weightString);
        if (title == null) {
            return;
        }
        if (synapse0 == null && synapse1 == null && neuron0 == null && neuron1 == null) {
            Error.attractorParseError((Component) parent);
            return;
        }
        dp = getNewWindow(title);
        setLabels();
        doAnalysis = true;
        calculateIsoperiodicPlot();
        dp.drawLegend();
        dp.drawPeriodLegend(periodVector, colors);
    }

    public void init() {
        setToolPriority(-7);
        addComboBox("color mode", comboBoxEntries, 0);
        addString("bias", "");
        addString("weight", "");
        addDouble(MAX_DIFF_STRING, 0, Double.MAX_VALUE, 0.0001);
    }

    public String getToolName() {
        return "Iso-periodic Plot";
    }

    public String getToolDescription() {
        return "Basins Visualisation";
    }

    private void setLabels() {
        switch(parameterX) {
            case NEURON_0:
                dp.setXLabel("Θ", "" + (neuron0.id() + 1));
                break;
            case NEURON_1:
                dp.setXLabel("Θ", "" + (neuron1.id() + 1));
                break;
            case SYNAPSE_0:
                dp.setXLabel("w", "" + (synapse0.getDestination().id() + 1) + "," + (synapse0.getSource().id() + 1));
                break;
            case SYNAPSE_1:
                dp.setXLabel("w", "" + (synapse1.getDestination().id() + 1) + "," + (synapse1.getSource().id() + 1));
                break;
        }
        switch(parameterY) {
            case NEURON_0:
                dp.setYLabel("Θ", "" + (neuron0.id() + 1));
                break;
            case NEURON_1:
                dp.setYLabel("Θ", "" + (neuron1.id() + 1));
                break;
            case SYNAPSE_0:
                dp.setYLabel("w", "" + (synapse0.getDestination().id() + 1) + "," + (synapse0.getSource().id() + 1));
                break;
            case SYNAPSE_1:
                dp.setYLabel("w", "" + (synapse1.getDestination().id() + 1) + "," + (synapse1.getSource().id() + 1));
                break;
        }
    }

    private String extractIndexInformation(String biasString, String weightString) {
        String title = new String("IsoperiodicPlot Map. ");
        if (biasString.equals("") && weightString.equals("")) {
            synapse0 = null;
            synapse1 = null;
            neuron0 = null;
            neuron1 = null;
            return null;
        }
        ;
        if (biasString.equals("")) {
            StringTokenizer st0 = new StringTokenizer(weightString, ",");
            String first = st0.nextToken().trim();
            String second = st0.nextToken().trim();
            title = title.concat("Weight " + first + ", Weight " + second + " varied");
            char axisFirst = first.charAt(first.length() - 1);
            char axisSecond = second.charAt(second.length() - 1);
            first = first.substring(0, first.length() - 1);
            second = second.substring(0, second.length() - 1);
            synapse0 = extractSynapse(first);
            synapse1 = extractSynapse(second);
            switch(axisFirst) {
                case 'x':
                    parameterX = SYNAPSE_0;
                    parameterY = SYNAPSE_1;
                    break;
                case 'y':
                    parameterX = SYNAPSE_1;
                    parameterY = SYNAPSE_0;
                    break;
            }
            return title;
        }
        if (weightString.equals("")) {
            StringTokenizer st0 = new StringTokenizer(biasString, ",");
            String first = null;
            String second = null;
            try {
                first = st0.nextToken().trim();
                second = st0.nextToken().trim();
            } catch (NoSuchElementException nse) {
                Error.attractorParseError(parent);
                return null;
            } catch (NumberFormatException nfe) {
                Error.attractorParseError(parent);
                return null;
            }
            title = title.concat("Bias " + first + ", Bias " + second + " varied");
            char axisFirst = first.charAt(first.length() - 1);
            char axisSecond = second.charAt(second.length() - 1);
            first = first.substring(0, first.length() - 1);
            second = second.substring(0, second.length() - 1);
            neuron0 = net.getNeuron(Integer.parseInt(first) - 1);
            neuron1 = net.getNeuron(Integer.parseInt(second) - 1);
            switch(axisFirst) {
                case 'x':
                    parameterX = NEURON_0;
                    parameterY = NEURON_1;
                    break;
                case 'y':
                    parameterX = NEURON_1;
                    parameterY = NEURON_0;
                    break;
            }
            return title;
        }
        title = title.concat("Bias " + biasString + ", Weight " + weightString + " varied");
        char neuronAxis = biasString.charAt(biasString.length() - 1);
        neuron0 = net.getNeuron(Integer.parseInt(biasString.substring(0, biasString.length() - 1)) - 1);
        char synapseAxis = weightString.charAt(weightString.length() - 1);
        synapse0 = extractSynapse(weightString.substring(0, weightString.length() - 1));
        switch(neuronAxis) {
            case 'x':
                parameterX = NEURON_0;
                parameterY = SYNAPSE_0;
                break;
            case 'y':
                parameterX = SYNAPSE_0;
                parameterY = NEURON_0;
                break;
        }
        return title;
    }

    private Synapse extractSynapse(String index) {
        StringTokenizer st = new StringTokenizer(index, "-");
        int destination = -1;
        int source = -1;
        try {
            destination = Integer.parseInt(st.nextToken().trim());
            source = Integer.parseInt(st.nextToken().trim());
        } catch (NoSuchElementException nse) {
            Error.attractorParseError(parent);
            return null;
        }
        Neuron destNeuron = net.getNeuron(destination - 1);
        Neuron sourceNeuron = net.getNeuron(source - 1);
        Synapse synapse = destNeuron.getSynapse(sourceNeuron);
        return synapse;
    }

    private void calculateIsoperiodicPlot() {
        double xi = 0;
        double yi = 0;
        periodVector = new Vector();
        Integer period = new Integer(0);
        if (dataStorage.getInialActivityMode() == 1) {
            net = dataStorage.getNet().copy();
        } else {
            net.randomInitActivity();
        }
        for (int x = 0; x < stepsX && doAnalysis; x++) {
            xi = xStart + x * dx;
            setParameter(parameterX, xi);
            for (int y = 0; y < stepsY && doAnalysis; y++) {
                yi = yStart + y * dy;
                setParameter(parameterY, yi);
                dp.drawProgressPointY(yi, yColor[(x + 1) % 2]);
                for (int i = 0; i < convergenceIterations; i++) {
                    net.process();
                }
                period = new Integer(getPeriod());
                dp.drawPoint(xi, yi, colors[Math.max(0, period.intValue() - 1)]);
                if (periodVector.indexOf(period) == -1) {
                    periodVector.add(period);
                }
            }
            dp.drawProgressPointX(xi, Color.blue);
        }
    }

    private int getPeriod() {
        Vector activities = new Vector();
        for (net.neurons().start(); net.neurons().hasMore(); net.neurons().next()) {
            activities.add(new Double(net.neurons().neuron().getActivation()));
        }
        for (int i = 1; i < colors.length; i++) {
            net.process();
            if (periodReached(activities)) {
                return i;
            }
        }
        return colors.length;
    }

    private boolean periodReached(Vector activities) {
        int index = 0;
        for (net.neurons().start(); net.neurons().hasMore(); net.neurons().next()) {
            double activity = ((Double) activities.elementAt(index)).doubleValue();
            if (Math.abs(activity - net.neurons().neuron().getActivation()) > maxActivationDiff) {
                return false;
            }
            index++;
        }
        return true;
    }

    private void setParameter(int parameterIndex, double value) {
        switch(parameterIndex) {
            case NEURON_0:
                neuron0.setBias(value);
                break;
            case NEURON_1:
                neuron1.setBias(value);
                break;
            case SYNAPSE_0:
                synapse0.setStrength(value);
                break;
            case SYNAPSE_1:
                synapse1.setStrength(value);
                break;
        }
    }
}
