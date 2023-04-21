package com.jvito.plot.svm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import com.jvito.exception.CompilerException;
import com.jvito.parameter.ParameterTypeDynamicCategory;
import com.jvito.plot.ColorPanel;
import com.jvito.plot.MovingListener;
import com.jvito.plot.SimplePlot;
import com.jvito.plot.ZoomingListener;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.tools.Ontology;

/**
 * Plots an attribute against the function value (predicted label).
 * 
 * @author Daniel Hakenjos
 * @version $Id: AttributeFunctionValuePlot.java,v 1.5 2008/04/17 06:19:14 djhacker Exp $
 */
public class AttributeFunctionValuePlot extends SimplePlot {

    private int number_atts = 0;

    private int number_of_samples = 0;

    private int x_attr = 0, c_attr;

    private Attribute x_attribute, y_attribute, colorattribute;

    private double[] x_samples;

    private double[] y_samples;

    private float[] labelvalues;

    private float[] colorsamples;

    private JPanel mainpane;

    @Override
    public JPanel getPlotPanel() {
        return mainpane;
    }

    /**
	 * Init the Scatterplot_2D.
	 */
    public AttributeFunctionValuePlot() {
        super();
    }

    /**
	 * Init the Scatterplot_3D with the name.
	 */
    public AttributeFunctionValuePlot(String name) {
        setName(name);
    }

    private void initComponents() {
        initDisplay();
        mainpane = new JPanel();
        mainpane.setLayout(new BorderLayout());
        ExampleSet set;
        if (this.source.isCompiled()) set = source.getExampleSet(); else return;
        mainpane.add(plotpanel, BorderLayout.CENTER);
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
            colorpanel.setPreferredSize(new Dimension(125, colorpanel.getHeight()));
            mainpane.add(colorpanel, BorderLayout.EAST);
        }
    }

    private void initDisplay() {
        ExampleSet set = source.getExampleSet();
        boolean haslabel = (source.getExampleSet().getAttributes().getLabel() != null);
        boolean haspredlabel = (source.getExampleSet().getAttributes().getPredictedLabel() != null);
        plotpanel = new AttributeFunctionValuePanel(source.getExampleSet(), source.getExampleColoring(), set.getStatistics(x_attribute, Statistics.MINIMUM), set.getStatistics(x_attribute, Statistics.MAXIMUM), set.getStatistics(y_attribute, Statistics.MINIMUM), set.getStatistics(y_attribute, Statistics.MAXIMUM), x_attribute.getName(), y_attribute.getName(), x_samples, y_samples, getParameterAsDouble("point_size"));
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
        } else if ((haslabel) && (color_attribute.equals(set.getAttributes().getLabel().getName()))) {
            plotpanel.colorByAttribute(colorattribute, set, colorsamples);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfLabel(colorattribute));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfLabel(colorattribute));
            plotpanel.initColorTable();
        } else if ((haspredlabel) && (color_attribute.equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && (colorattribute.isNominal())) {
            Collection values = set.getAttributes().getPredictedLabel().getMapping().getValues();
            Object[] valuesstr = values.toArray();
            Color[] colors = new Color[set.getAttributes().getPredictedLabel().getMapping().getValues().size() * set.getAttributes().getPredictedLabel().getMapping().getValues().size()];
            for (int i = 0; i < valuesstr.length; i++) {
                for (int j = 0; j < valuesstr.length; j++) {
                    colors[i * valuesstr.length + j] = source.getExampleColoring().getColorOfLabelvsPred(set.getAttributes().getLabel(), set.getAttributes().getPredictedLabel(), (String) valuesstr[i], (String) valuesstr[j]);
                }
            }
            float[] pvalues = new float[number_of_samples];
            for (int i = 0; i < colorsamples.length; i++) {
                pvalues[i] = labelvalues[i] * valuesstr.length + colorsamples[i];
            }
            plotpanel.colorByAttribute(colorattribute, set, pvalues, colors);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(colorattribute));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(colorattribute));
        } else if ((haspredlabel) && (color_attribute.equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && (colorattribute.getValueType() == Ontology.NUMERICAL)) {
            plotpanel.colorByAttribute(colorattribute, set, colorsamples);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(colorattribute));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(colorattribute));
            plotpanel.initColorTable();
        } else {
            plotpanel.colorByAttribute(colorattribute, set, colorsamples);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfAttribute(colorattribute));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfAttribute(colorattribute));
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
        type = new ParameterTypeDynamicCategory("attribute", "Attribute for the x-axis.", getAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDynamicCategory("color_attribute", "This attribute is the color-dimension.", getColorAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDouble("point_size", "Size of the points.", 0, 100, 5);
        types.add(type);
        return types;
    }

    /**
	 * @see com.jvito.compile.Compileable#compile()
	 */
    @Override
    public void compile() throws CompilerException {
        this.iscompiling = true;
        if (source == null) {
            throw new CompilerException("Cannot compile ScatterPlot2D. " + "This operator must be achild of a data-operator.");
        }
        ExampleSet set = source.getExampleSet();
        if (set.getAttributes().getPredictedLabel() == null) {
            throw new CompilerException("The ExampleSet must have a numerical predicted label.");
        }
        if (set.getAttributes().getPredictedLabel().isNominal()) {
            throw new CompilerException("The ExampleSet must have a numerical predicted label.");
        }
        number_of_samples = set.size();
        number_atts = set.getAttributes().size();
        x_samples = new double[number_of_samples];
        y_samples = new double[number_of_samples];
        labelvalues = new float[number_of_samples];
        if (getParameterAsInt("color_attribute") != 0) {
            colorsamples = new float[number_of_samples];
        }
        x_attr = getParameterAsInt("attribute");
        c_attr = getParameterAsInt("color_attribute") - 1;
        if (x_attr < number_atts) {
            x_attribute = getAttribute(x_attr);
        } else if ((x_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
            x_attribute = set.getAttributes().getLabel();
        } else {
            x_attribute = set.getAttributes().getPredictedLabel();
        }
        y_attribute = set.getAttributes().getPredictedLabel();
        if (c_attr >= 0) {
            if (c_attr < number_atts) {
                colorattribute = getAttribute(c_attr);
            } else if ((c_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                colorattribute = set.getAttributes().getLabel();
            } else {
                colorattribute = set.getAttributes().getPredictedLabel();
            }
        }
        boolean haslabel = set.getAttributes().getLabel() != null;
        Iterator<Example> reader = set.iterator();
        Example example;
        for (int sample = 0; sample < number_of_samples; sample++) {
            example = reader.next();
            if (x_attr < number_atts) {
                x_samples[sample] = example.getValue(x_attribute);
            } else if ((x_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                x_samples[sample] = example.getLabel();
            } else {
                x_samples[sample] = example.getPredictedLabel();
            }
            y_samples[sample] = example.getPredictedLabel();
            if (haslabel) {
                labelvalues[sample] = (float) example.getLabel();
            }
            if (c_attr >= 0) {
                if (c_attr < number_atts) {
                    colorsamples[sample] = (float) example.getValue(colorattribute);
                } else if ((c_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                    colorsamples[sample] = (float) example.getLabel();
                } else {
                    colorsamples[sample] = (float) example.getPredictedLabel();
                }
            }
        }
        initComponents();
        this.iscompiling = false;
        this.iscompiled = true;
    }

    /**
	 * @see com.jvito.plot.Plot#parentIsCompiled()
	 */
    @Override
    public void parentIsCompiled() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("attribute")).setValues(getAttributes());
    }

    /**
	 * @see com.jvito.plot.Plot#refreshParameter()
	 */
    @Override
    public void refreshParameter() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("attribute")).setValues(getAttributes());
    }

    public String[] getAttributes() {
        if (source == null) return new String[] { "" };
        ExampleSet set = source.getExampleSet();
        if (set == null) return new String[] { "" };
        int count = 0;
        count += set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            count++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            count++;
        }
        String[] categories = new String[count];
        for (int i = 0; i < set.getAttributes().size(); i++) {
            categories[i] = getAttribute(i).getName();
        }
        int index = set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            categories[index] = set.getAttributes().getLabel().getName();
            index++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            categories[index] = set.getAttributes().getPredictedLabel().getName();
        }
        return categories;
    }

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
