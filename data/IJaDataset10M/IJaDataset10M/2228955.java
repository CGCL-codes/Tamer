package com.jvito.plot.multivariat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.jvito.exception.CompilerException;
import com.jvito.parameter.ParameterTypeDynamicCategory;
import com.jvito.plot.ColorPanel;
import com.jvito.plot.MovingListener;
import com.jvito.plot.SimplePlot;
import com.jvito.plot.ZoomingListener;
import com.jvito.util.Logger;
import com.jvito.util.Utils;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeWeights;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.tools.Ontology;

/**
 * <code>PolyViz</code> is an expanded <code>RadViz</code>. One of the problems with radviz is that n-dimensional
 * points with quite different values can get mapped to the same spot on the display. If the fixed spring points for
 * each dimension are expanded as a distribution spread out along an axis, the overlap problem is greatly reduced. The
 * distzribution for each dimension is shown by lines emanating from an axis, arranged in a spread polygon
 * configuration. So you get the clustering/outlier capability of <code>RadViz</code>, as well as the ability to see
 * the distribution of the data in each dimension. The polygon is spread out to distingiush the distribution lines at
 * the adjacent ends of the dimensional anchors (axis). With higher dimensions, the polygon becomes more like a circle,
 * and the polyviz spring mappings become more similar to <code>RadViz</code>.
 * 
 * @author Daniel Hakenjos
 * @version $Id: PolyViz.java,v 1.4 2008/04/16 19:54:23 djhacker Exp $
 */
public class PolyViz extends SimplePlot {

    private JPanel mainpane, settings;

    private String[] attr_names = new String[0];

    private int number_atts = 0, number_of_samples = 0;

    private float[][] samples;

    private float[] minima, maxima;

    private float[] labelvalues, predvalues;

    private String[] classes;

    private JList attlist;

    private static final String[] ATTRIBUTE_MODE = new String[] { "original", "random", "correlation_label", "correlation_prediction", "weights", "selection" };

    private Attribute[] attributes;

    private Attribute label, prediction;

    private int[] attribute_index;

    /**
	 * Init the PolyViz.
	 */
    public PolyViz() {
        super();
    }

    /**
	 * @see com.jvito.plot.Plot#getPlotPanel()
	 */
    @Override
    public JPanel getPlotPanel() {
        if (!isCompiled()) return null;
        return mainpane;
    }

    private void initComponents() {
        initDisplay();
        mainpane = new JPanel();
        mainpane.setLayout(new BorderLayout(5, 5));
        settings = new JPanel();
        settings.setLayout(new BorderLayout(5, 5));
        mainpane.add(settings, BorderLayout.EAST);
        JPanel settings2 = new JPanel();
        settings2.setLayout(new GridLayout(2, 1, 5, 5));
        settings.add(settings2, BorderLayout.CENTER);
        ExampleSet set = source.getExampleSet();
        if (getParameterAsInt("color_attribute") != 0) {
            JPanel colorpanel = plotpanel.getColorPanel();
            Color[] colors;
            if (getParameterAsInt("color_attribute") <= set.getAttributes().size()) {
            } else if ((set.getAttributes().getLabel() != null) && (set.getAttributes().getLabel().isNominal()) && (getParameterAsInt("color_attribute") == set.getAttributes().size() + 1)) {
                colors = new Color[set.getAttributes().getLabel().getMapping().getValues().size()];
                Collection values = set.getAttributes().getLabel().getMapping().getValues();
                Object[] valuesstr = values.toArray();
                for (int i = 0; i < valuesstr.length; i++) {
                    colors[i] = source.getExampleColoring().getColorOfLabelValue(set.getAttributes().getLabel(), (String) valuesstr[i]);
                }
                colorpanel = new ColorPanel(set.getAttributes().getLabel(), (float) set.getStatistics(set.getAttributes().getLabel(), Statistics.MINIMUM), (float) set.getStatistics(set.getAttributes().getLabel(), Statistics.MAXIMUM), colors, getParameterAsColor("foreground"), getParameterAsColor("background"));
            } else if ((set.getAttributes().getPredictedLabel() != null) && (set.getAttributes().getPredictedLabel().isNominal())) {
                Collection values = set.getAttributes().getPredictedLabel().getMapping().getValues();
                Object[] valuesstr = values.toArray();
                colors = new Color[set.getAttributes().getPredictedLabel().getMapping().getValues().size() * set.getAttributes().getPredictedLabel().getMapping().getValues().size()];
                String[] valuestring = new String[valuesstr.length * valuesstr.length];
                for (int i = 0; i < valuesstr.length; i++) {
                    for (int j = 0; j < valuesstr.length; j++) {
                        valuestring[i * valuesstr.length + j] = new String((String) valuesstr[i] + " - " + (String) valuesstr[j]);
                        colors[i * valuesstr.length + j] = source.getExampleColoring().getColorOfLabelvsPred(set.getAttributes().getLabel(), set.getAttributes().getPredictedLabel(), (String) valuesstr[i], (String) valuesstr[j]);
                    }
                }
                colorpanel = new ColorPanel(set.getAttributes().getPredictedLabel(), (float) set.getStatistics(set.getAttributes().getPredictedLabel(), Statistics.MINIMUM), (float) set.getStatistics(set.getAttributes().getPredictedLabel(), Statistics.MAXIMUM), valuestring, colors, getParameterAsColor("foreground"), getParameterAsColor("background"));
            }
            if ((set.getAttributes().getPredictedLabel() != null) && (true)) colorpanel.setPreferredSize(new Dimension(125, colorpanel.getHeight()));
            settings2.add(colorpanel);
        }
        if (getParameterAsBoolean("label_axis")) {
            int s = attr_names.length;
            if (source.getExampleSet().getAttributes().getLabel() != null) {
                s++;
            }
            if (source.getExampleSet().getAttributes().getPredictedLabel() != null) {
                s++;
            }
            String[] atts = new String[s];
            for (int i = 0; i < attr_names.length; i++) {
                atts[i] = new String(Integer.toString(i + 1) + " " + attr_names[i]);
            }
            if (source.getExampleSet().getAttributes().getLabel() != null) {
                atts[attr_names.length] = new String("label: " + source.getExampleSet().getAttributes().getLabel().getName());
            }
            if (source.getExampleSet().getAttributes().getPredictedLabel() != null) {
                atts[s - 1] = new String("prediction: " + source.getExampleSet().getAttributes().getPredictedLabel().getName());
            }
            attlist = new JList(atts);
            JScrollPane attscpane = new JScrollPane(attlist);
            settings2.add(attscpane);
        }
        mainpane.add(plotpanel, BorderLayout.CENTER);
    }

    private void initDisplay() {
        ExampleSet set = source.getExampleSet();
        boolean haslabel = (source.getExampleSet().getAttributes().getLabel() != null);
        boolean haspredlabel = (source.getExampleSet().getAttributes().getPredictedLabel() != null);
        plotpanel = new PolyVizPanel(source.getExampleSet(), source.getExampleColoring(), samples, getParameterAsDouble("point_size"), getParameterAsBoolean("label_axis"), attribute_index, number_atts, getParameterAsDouble("scale"));
        plotpanel.setTitle(getParameterAsString("title"));
        plotpanel.setDrawTitle(getParameterAsBoolean("show title"));
        plotpanel.setForegroundColor(getParameterAsColor("foreground"));
        plotpanel.setBackgroundColor(getParameterAsColor("background"));
        MovingListener movinglistener = new MovingListener(plotpanel);
        ZoomingListener zoominlistener = new ZoomingListener(plotpanel);
        plotpanel.addMouseListener(movinglistener);
        plotpanel.addMouseMotionListener(movinglistener);
        plotpanel.addMouseListener(zoominlistener);
        plotpanel.addMouseMotionListener(zoominlistener);
        String color_attribute = ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).getValues()[getParameterAsInt("color_attribute")];
        if (color_attribute.equals(ATTRIBUTE_NONE)) {
        } else if ((haslabel) && (color_attribute.equals(source.getExampleSet().getAttributes().getLabel().getName()))) {
            plotpanel.colorByAttribute(source.getExampleSet().getAttributes().getLabel(), set, labelvalues);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfAttribute(source.getExampleSet().getAttributes().getLabel()));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfAttribute(source.getExampleSet().getAttributes().getLabel()));
            plotpanel.initColorTable();
        } else if ((haspredlabel) && (color_attribute.equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && (prediction.isNominal())) {
            Collection values = set.getAttributes().getPredictedLabel().getMapping().getValues();
            Object[] valuesstr = values.toArray();
            Color[] colors = new Color[set.getAttributes().getPredictedLabel().getMapping().getValues().size() * set.getAttributes().getPredictedLabel().getMapping().getValues().size()];
            for (int i = 0; i < valuesstr.length; i++) {
                for (int j = 0; j < valuesstr.length; j++) {
                    colors[i * valuesstr.length + j] = source.getExampleColoring().getColorOfLabelvsPred(set.getAttributes().getLabel(), set.getAttributes().getPredictedLabel(), (String) valuesstr[i], (String) valuesstr[j]);
                }
            }
            float[] pvalues = new float[number_of_samples];
            for (int i = 0; i < predvalues.length; i++) {
                pvalues[i] = labelvalues[i] * valuesstr.length + predvalues[i];
            }
            plotpanel.colorByAttribute(prediction, set, pvalues, colors);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(prediction));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(prediction));
        } else if ((haspredlabel) && (color_attribute.equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && (prediction.getValueType() == Ontology.NUMERICAL)) {
            plotpanel.colorByAttribute(prediction, set, predvalues);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(prediction));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(prediction));
            plotpanel.initColorTable();
        } else if (color_attribute.equals("")) {
        } else {
            plotpanel.colorByAttribute(source.getExampleSet().getAttributes().get(color_attribute), set, samples[attribute_index[getParameterAsInt("color_attribute") - 1]]);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfAttribute(source.getExampleSet().getAttributes().get(color_attribute)));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfAttribute(source.getExampleSet().getAttributes().get(color_attribute)));
            plotpanel.initColorTable();
        }
    }

    /**
	 * @see com.jvito.parameter.ParameterObject#getParameterTypes()
	 */
    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type;
        type = new ParameterTypeBoolean("local_normalization", "The data have to be local normalized, otherwise global normalized.", false);
        types.add(type);
        type = new ParameterTypeDynamicCategory("color_attribute", "This attribute is the color-dimension.", getColorAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDouble("point_size", "Size of the points.", 0, 100, 3);
        types.add(type);
        type = new ParameterTypeBoolean("draw_axis", "Draw the radial axis.", true);
        types.add(type);
        type = new ParameterTypeBoolean("label_axis", "Label the axis.", true);
        types.add(type);
        type = new ParameterTypeStringCategory("attribute_mode", "Select the mode to display the attributes.", ATTRIBUTE_MODE, ATTRIBUTE_MODE[0]);
        types.add(type);
        type = new ParameterTypeInt("number_attributes", "This number of the first correlated Attributes with the function values are displayed.", -1, Integer.MAX_VALUE, -1);
        types.add(type);
        type = new ParameterTypeFile("attribute_weights_file", "The file-location of the weights.", "wgt", true);
        types.add(type);
        type = new ParameterTypeString("attributes", "These attributes are displayed(comma-separated attribute-names).", "");
        types.add(type);
        type = new ParameterTypeDouble("scale", "Scale-Factor for the data-points (scales the distance from the center)", 0.0d, Double.MAX_VALUE, 1.0d);
        types.add(type);
        return types;
    }

    /**
	 * @see com.jvito.compile.Compileable#compile()
	 */
    @Override
    public void compile() throws CompilerException {
        if (!source.isCompiled()) return;
        this.iscompiling = true;
        ExampleSet set = this.source.getExampleSet();
        boolean haslabel = (set.getAttributes().getLabel() != null);
        boolean haspredlabel = (set.getAttributes().getPredictedLabel() != null);
        number_of_samples = set.size();
        number_atts = set.getAttributes().size();
        attr_names = new String[number_atts];
        for (int att = 0; att < number_atts; att++) {
            attr_names[att] = getAttribute(att).getName();
        }
        if (haslabel || haspredlabel) initClassNames();
        attributes = new Attribute[set.getAttributes().size()];
        label = set.getAttributes().getLabel();
        prediction = set.getAttributes().getPredictedLabel();
        for (int a = 0; a < set.getAttributes().size(); a++) {
            attributes[a] = getAttribute(a);
        }
        samples = new float[number_atts][number_of_samples];
        labelvalues = new float[number_of_samples];
        predvalues = new float[number_of_samples];
        Iterator<Example> reader = set.iterator();
        Example example;
        for (int sample = 0; sample < number_of_samples; sample++) {
            example = reader.next();
            for (int d = 0; d < number_atts; d++) {
                samples[d][sample] = (float) example.getValue(getAttribute(d));
            }
            if (haslabel) {
                labelvalues[sample] = (float) example.getLabel();
            }
            if (haspredlabel) {
                predvalues[sample] = (float) example.getPredictedLabel();
            }
        }
        minima = new float[number_atts];
        maxima = new float[number_atts];
        Attribute a;
        for (int d = 0; d < number_atts; d++) {
            a = getAttribute(d);
            if (a.isNominal()) {
                minima[d] = 0.0f;
                maxima[d] = (a.getMapping().size() - 1);
            }
            if (a.getValueType() == Ontology.NUMERICAL) {
                minima[d] = (float) set.getStatistics(a, Statistics.MINIMUM);
                maxima[d] = (float) set.getStatistics(a, Statistics.MAXIMUM);
            }
        }
        attribute_index = new int[number_atts];
        for (int i = 0; i < attribute_index.length; i++) {
            attribute_index[i] = i;
        }
        int number_attribute = getParameterAsInt("number_attributes");
        if (number_attribute == 0) {
            number_attribute = -1;
        }
        if (number_attribute == -1) {
            number_attribute = number_atts;
        }
        String mode = getParameterAsString("attribute_mode");
        if (mode.equals(ATTRIBUTE_MODE[0])) {
            number_atts = number_attribute;
        } else if (mode.equals(ATTRIBUTE_MODE[1])) {
            Vector<Integer> vector = new Vector<Integer>();
            for (int i = 0; i < set.getAttributes().size(); i++) {
                vector.add(attribute_index[i]);
                attribute_index[i] = -1;
            }
            int vindex;
            for (int i = 0; i < set.getAttributes().size(); i++) {
                vindex = (int) Math.floor(Math.random() * vector.size());
                attribute_index[i] = vector.remove(vindex);
            }
            String props = new String("\n");
            props += Utils.getString("Attribute", 25, 0) + "\n";
            props += Utils.duplicateString("-", 25) + "\n";
            for (int i = 0; (i < 100) && (i < attribute_index.length); i++) {
                props += Utils.getString(attributes[attribute_index[i]].getName(), 25, 0) + "\n";
            }
            Logger.logMessage(props, Logger.PROJECT);
            number_atts = number_attribute;
        } else if (mode.equals(ATTRIBUTE_MODE[2])) {
            if (!haslabel) {
                throw new CompilerException("The ExampleSet must have a numeric label.");
            }
            if (label.isNominal()) {
                throw new CompilerException("The ExampleSet must have a numeric label.");
            }
            double[] mean = new double[set.getAttributes().size() + 1];
            double[] st_deviation = new double[set.getAttributes().size() + 1];
            double[] covariance = new double[set.getAttributes().size()];
            for (int i = 0; i < set.getAttributes().size(); i++) {
                mean[i] = set.getStatistics(attributes[i], Statistics.AVERAGE);
            }
            mean[mean.length - 1] = set.getStatistics(label, Statistics.AVERAGE);
            for (int i = 0; i < set.getAttributes().size(); i++) {
                st_deviation[i] = Math.sqrt(set.getStatistics(attributes[i], Statistics.VARIANCE));
            }
            st_deviation[st_deviation.length - 1] = Math.sqrt(set.getStatistics(label, Statistics.VARIANCE));
            for (int i = 0; i < set.getAttributes().size(); i++) {
                covariance[i] = 0.0d;
                for (int j = 0; j < number_of_samples; j++) {
                    covariance[i] += (samples[i][j] - mean[i]) * (labelvalues[j] - mean[mean.length - 1]);
                }
                covariance[i] = covariance[i] / (number_of_samples - 1);
            }
            double[] correlation = new double[set.getAttributes().size()];
            for (int i = 0; i < correlation.length; i++) {
                correlation[i] = (st_deviation[i] * st_deviation[st_deviation.length - 1]);
                if (correlation[i] == 0.0d) {
                    correlation[i] = covariance[i];
                } else {
                    correlation[i] = covariance[i] / correlation[i];
                }
            }
            orderWeights(correlation);
            String props = new String("\n");
            props += Utils.getString("Attribute", 25, 0) + "| " + Utils.getString("Correlation with label", 25, 0) + "\n";
            props += Utils.duplicateString("-", 25) + "+ " + Utils.duplicateString("-", 25) + "\n";
            for (int i = 0; (i < 100) && (i < correlation.length); i++) {
                props += Utils.getString(attributes[attribute_index[i]].getName(), 25, 0) + "| " + Utils.getString(Double.toString(correlation[i]), 25, 1) + "\n";
            }
            Logger.logMessage(props, Logger.PROJECT);
            number_atts = number_attribute;
        } else if (mode.equals(ATTRIBUTE_MODE[3])) {
            if (!haspredlabel) {
                throw new CompilerException("The ExampleSet must have a numeric predicted label.");
            }
            if (prediction.isNominal()) {
                throw new CompilerException("The ExampleSet must have a numeric predictedlabel.");
            }
            double[] mean = new double[set.getAttributes().size() + 1];
            double[] st_deviation = new double[set.getAttributes().size() + 1];
            double[] covariance = new double[set.getAttributes().size()];
            for (int i = 0; i < set.getAttributes().size(); i++) {
                mean[i] = set.getStatistics(attributes[i], Statistics.AVERAGE);
            }
            mean[mean.length - 1] = set.getStatistics(prediction, Statistics.AVERAGE);
            for (int i = 0; i < set.getAttributes().size(); i++) {
                st_deviation[i] = Math.sqrt(set.getStatistics(attributes[i], Statistics.VARIANCE));
            }
            st_deviation[st_deviation.length - 1] = Math.sqrt(set.getStatistics(prediction, Statistics.VARIANCE));
            for (int i = 0; i < set.getAttributes().size(); i++) {
                covariance[i] = 0.0d;
                for (int j = 0; j < number_of_samples; j++) {
                    covariance[i] += (samples[i][j] - mean[i]) * (predvalues[j] - mean[mean.length - 1]);
                }
                covariance[i] = covariance[i] / (number_of_samples - 1);
            }
            double[] correlation = new double[set.getAttributes().size()];
            for (int i = 0; i < correlation.length; i++) {
                correlation[i] = (st_deviation[i] * st_deviation[st_deviation.length - 1]);
                if (correlation[i] == 0.0d) {
                    correlation[i] = covariance[i];
                } else {
                    correlation[i] = covariance[i] / correlation[i];
                }
            }
            orderWeights(correlation);
            String props = new String("\n");
            props += Utils.getString("Attribute", 25, 0) + "| " + Utils.getString("Correlation prediction", 25, 0) + "\n";
            props += Utils.duplicateString("-", 25) + "+ " + Utils.duplicateString("-", 25) + "\n";
            for (int i = 0; (i < 100) && (i < correlation.length); i++) {
                props += Utils.getString(attributes[attribute_index[i]].getName(), 25, 0) + "| " + Utils.getString(Double.toString(correlation[i]), 25, 1) + "\n";
            }
            Logger.logMessage(props, Logger.PROJECT);
            number_atts = number_attribute;
        } else if (mode.equals(ATTRIBUTE_MODE[4])) {
            String weightFileName = getParameterAsString("attribute_weights_file");
            File weightFile = new File(weightFileName);
            AttributeWeights result = null;
            try {
                result = AttributeWeights.load(weightFile);
            } catch (IOException e) {
                throw new CompilerException(e.getMessage(), e);
            }
            double[] weights = new double[set.getAttributes().size()];
            for (int i = 0; i < weights.length; i++) {
                weights[i] = result.getWeight(attributes[i].getName());
            }
            orderWeights(weights);
            String props = new String("\n");
            props += Utils.getString("Attribute", 25, 0) + "| " + Utils.getString("Weight", 25, 0) + "\n";
            props += Utils.duplicateString("-", 25) + "+ " + Utils.duplicateString("-", 25) + "\n";
            for (int i = 0; (i < 100) && (i < weights.length); i++) {
                props += Utils.getString(attributes[attribute_index[i]].getName(), 25, 0) + "| " + Utils.getString(Double.toString(weights[i]), 25, 1) + "\n";
            }
            Logger.logMessage(props, Logger.PROJECT);
            number_atts = number_attribute;
        } else if (mode.equals(ATTRIBUTE_MODE[5])) {
            StringTokenizer st = new StringTokenizer(getParameterAsString("attributes"), ",");
            String att;
            int index = 0;
            while (st.hasMoreTokens()) {
                att = st.nextToken();
                attribute_index[index] = 0;
                for (int i = 0; i < attributes.length; i++) {
                    if (attributes[i].getName().equals(att)) {
                        attribute_index[index] = i;
                        index++;
                    }
                }
            }
            number_atts = index;
            if (number_atts == 0) {
                throw new CompilerException("No attribute selected!");
            }
            for (int i = index; i < attribute_index.length; i++) attribute_index[i] = 0;
        }
        normalizeSamples(set);
        initComponents();
        this.iscompiling = false;
        this.iscompiled = true;
    }

    /**
	 * Order the subarry from index anfang to index ende with QuickSort Also the attribute_index is ordered.
	 * 
	 * @param array
	 * @param links
	 * @param rechts
	 */
    public void orderWeights(double[] array, int links, int rechts) {
        int left = links, right = rechts;
        double pivot = array[(links + rechts) / 2], temp;
        pivot = Math.abs(pivot);
        do {
            while (Math.abs(array[left]) > pivot) left++;
            while (Math.abs(array[right]) < pivot) right--;
            if (left <= right) {
                temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                temp = attribute_index[left];
                attribute_index[left] = attribute_index[right];
                attribute_index[right] = (int) temp;
                left++;
                right--;
            }
        } while (!(left > right));
        if (links < right) orderWeights(array, links, right);
        if (left < rechts) orderWeights(array, left, rechts);
    }

    /**
	 * Order weights with QuickSort. Performs an ordering to the absolute value
	 */
    public void orderWeights(double[] weights) {
        orderWeights(weights, 0, weights.length - 1);
    }

    private void normalizeSamples(ExampleSet set) {
        boolean normlocal = getParameterAsBoolean("local_normalization");
        float max = 1.0f, min = 0.0f;
        if (!normlocal) {
            max = maxima[0];
            min = minima[0];
            for (int d = 0; d < number_atts; d++) {
                max = Math.max(max, maxima[d]);
                min = Math.min(min, minima[d]);
            }
        }
        for (int d = 0; d < number_atts; d++) {
            if (normlocal) {
                max = maxima[d];
                min = minima[d];
            }
            for (int sample = 0; sample < number_of_samples; sample++) {
                samples[d][sample] = ((samples[d][sample] - min) / (max - min));
            }
        }
    }

    private void initClassNames() {
        Attribute att = null;
        if (source.getExampleSet().getAttributes().getPredictedLabel() != null) {
            att = source.getExampleSet().getAttributes().getPredictedLabel();
        }
        if (source.getExampleSet().getAttributes().getLabel() != null) {
            att = source.getExampleSet().getAttributes().getLabel();
        }
        if (att == null) {
            return;
        }
        classes = new String[att.getMapping().size()];
        Collection col = att.getMapping().getValues();
        Iterator iter = col.iterator();
        int index = 0;
        while (iter.hasNext()) {
            classes[index] = (String) iter.next();
            index++;
        }
    }

    /**
	 * @see com.jvito.plot.Plot#parentIsCompiled()
	 */
    @Override
    public void parentIsCompiled() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
    }

    /**
	 * @see com.jvito.plot.Plot#refreshParameter()
	 */
    @Override
    public void refreshParameter() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
    }

    /**
	 * Gets the color-attributes.
	 */
    public String[] getColorAttributes() {
        if (source == null) {
            return new String[] { ATTRIBUTE_NONE };
        }
        if (!source.isCompiled()) return new String[] { ATTRIBUTE_NONE };
        ExampleSet set = source.getExampleSet();
        if (set == null) return new String[] { ATTRIBUTE_NONE };
        int count = 1;
        count += set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            count++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            count++;
        }
        String[] categories = new String[count];
        categories[0] = ATTRIBUTE_NONE;
        for (int i = 1; i <= set.getAttributes().size(); i++) {
            categories[i] = getAttribute(i - 1).getName();
        }
        int index = set.getAttributes().size() + 1;
        if (set.getAttributes().getLabel() != null) {
            categories[index] = set.getAttributes().getLabel().getName();
            index++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            categories[index] = set.getAttributes().getPredictedLabel().getName();
            index++;
        }
        return categories;
    }
}
