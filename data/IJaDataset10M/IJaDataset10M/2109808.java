package com.jvito.plot.multivariat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.tools.Ontology;

/**
 * @author Daniel Hakenjos
 * @version $Id: AndrewsCurvesPlot.java,v 1.4 2008/04/16 19:54:23 djhacker Exp $
 */
public class AndrewsCurvesPlot extends SimplePlot {

    private JPanel mainpane, settings;

    private String[] attr_names = new String[0];

    private int number_atts = 0, number_of_samples = 0;

    private float[][] samples;

    private float[] labelvalues, predvalues;

    private float[] min_sample, max_sample;

    private String[] classes;

    private static final String[] NORMALIZATION = new String[] { "local", "global" };

    private int allpoints;

    private float min_fourier, max_fourier;

    private float[] colorvalues;

    /**
	 * Init the AndrewsCurvesPlot.
	 */
    public AndrewsCurvesPlot() {
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
            colorpanel.setPreferredSize(new Dimension(125, colorpanel.getHeight()));
            settings2.add(colorpanel);
        }
        mainpane.add(plotpanel, BorderLayout.CENTER);
    }

    private void initDisplay() {
        ExampleSet set = source.getExampleSet();
        boolean haslabel = (source.getExampleSet().getAttributes().getLabel() != null);
        boolean haspredlabel = (source.getExampleSet().getAttributes().getPredictedLabel() != null);
        plotpanel = new ParallelCoordinatesPanel(source.getExampleSet(), source.getExampleColoring(), samples, min_fourier, max_fourier);
        ((ParallelCoordinatesPanel) plotpanel).setDrawCoordinates(false);
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
            plotpanel.colorByAttribute(set.getAttributes().getLabel(), set, labelvalues);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfAttribute(set.getAttributes().getLabel()));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfAttribute(set.getAttributes().getLabel()));
            plotpanel.initColorTable();
        } else if ((haspredlabel) && (color_attribute.equals(set.getAttributes().getPredictedLabel().getName())) && (set.getAttributes().getPredictedLabel().isNominal())) {
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
            plotpanel.colorByAttribute(set.getAttributes().getPredictedLabel(), set, pvalues, colors);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(set.getAttributes().getPredictedLabel()));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(set.getAttributes().getPredictedLabel()));
        } else if ((haspredlabel) && (color_attribute.equals(set.getAttributes().getPredictedLabel().getName())) && (set.getAttributes().getPredictedLabel().getValueType() == Ontology.NUMERICAL)) {
            plotpanel.colorByAttribute(set.getAttributes().getPredictedLabel(), set, predvalues);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfPredLabel(set.getAttributes().getPredictedLabel()));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfPredLabel(set.getAttributes().getPredictedLabel()));
            plotpanel.initColorTable();
        } else {
            plotpanel.colorByAttribute(set.getAttributes().get(color_attribute), set, colorvalues);
            plotpanel.setStartColor(source.getExampleColoring().getStartColorOfAttribute(set.getAttributes().get(color_attribute)));
            plotpanel.setEndColor(source.getExampleColoring().getEndColorOfAttribute(set.getAttributes().get(color_attribute)));
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
        type = new ParameterTypeInt("support_points", "Number of support points for the fourier transform between two parallel coordinates.", 1, 100, 5);
        types.add(type);
        type = new ParameterTypeStringCategory("normalization", "Choose the normalization for the data.", NORMALIZATION, NORMALIZATION[0]);
        types.add(type);
        type = new ParameterTypeDynamicCategory("color_attribute", "This attribute is the color-dimension.", getColorAttributes(), 0);
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
        int support_points = getParameterAsInt("support_points");
        allpoints = (number_atts - 1) * support_points + 1;
        samples = new float[allpoints][number_of_samples];
        min_sample = new float[number_atts];
        max_sample = new float[number_atts];
        labelvalues = new float[number_of_samples];
        predvalues = new float[number_of_samples];
        Attribute a;
        for (int d = 0; d < number_atts; d++) {
            a = getAttribute(d);
            if (a.getValueType() == Ontology.NUMERICAL) {
                min_sample[d] = (float) set.getStatistics(a, Statistics.MINIMUM);
                max_sample[d] = (float) set.getStatistics(a, Statistics.MAXIMUM);
            } else {
                min_sample[d] = 0.0f;
                max_sample[d] = a.getMapping().size() - 1.0f;
            }
        }
        if (getParameterAsString("normalization").equals(NORMALIZATION[1])) {
            float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
            for (int d = 0; d < number_atts; d++) {
                min = Math.min(min, min_sample[d]);
                max = Math.max(max, max_sample[d]);
            }
            for (int d = 0; d < number_atts; d++) {
                min_sample[d] = min;
                max_sample[d] = max;
            }
        }
        Example example;
        float leftbound = ((float) -Math.PI);
        float rightbound = ((float) Math.PI);
        float delta = (rightbound - leftbound) / (allpoints - 1);
        float time;
        float[] min_four, max_four;
        min_four = new float[allpoints];
        max_four = new float[allpoints];
        for (int d = 0; d < allpoints; d++) {
            min_four[d] = Float.MAX_VALUE;
            max_four[d] = Float.MIN_VALUE;
        }
        int color_att = getParameterAsInt("color_attribute") - 1;
        if ((color_att >= 0) && (color_att < number_atts)) {
            colorvalues = new float[number_of_samples];
        }
        Iterator<Example> reader = set.iterator();
        for (int sample = 0; sample < number_of_samples; sample++) {
            example = reader.next();
            time = leftbound;
            for (int d = 0; d < allpoints; d++) {
                samples[d][sample] = getFourierTransform(example, time, max_sample, min_sample);
                min_four[d] = Math.min(min_four[d], samples[d][sample]);
                max_four[d] = Math.max(max_four[d], samples[d][sample]);
                time += delta;
            }
            if ((color_att >= 0) && (color_att < number_atts)) {
                colorvalues[sample] = (float) example.getValue(getAttribute(color_att));
            }
            if (haslabel) {
                labelvalues[sample] = (float) example.getLabel();
            }
            if (haspredlabel) {
                predvalues[sample] = (float) example.getPredictedLabel();
            }
        }
        min_fourier = Float.MAX_VALUE;
        max_fourier = Float.MIN_VALUE;
        for (int d = 0; d < allpoints; d++) {
            min_fourier = Math.min(min_four[d], min_fourier);
            max_fourier = Math.max(max_four[d], max_fourier);
        }
        initComponents();
        this.iscompiling = false;
        this.iscompiled = true;
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

    /**
	 * Gets the Fourier transfrom of an example.
	 */
    public float getFourierTransform(Example example, float time, float[] maxima, float[] minima) {
        float fourier = 0.0f;
        int counter = 1;
        for (int i = 0; i < number_atts; i++) {
            if (i == 0) {
                fourier += ((((float) example.getValue(getAttribute(i))) - minima[0]) / maxima[0]) / ((float) Math.sqrt(2.0d));
                continue;
            }
            if ((i + 1) % 2 == 0) {
                fourier += ((((float) example.getValue(getAttribute(i))) - minima[i]) / (maxima[i] - minima[i])) * ((float) Math.sin((time * counter)));
            } else {
                fourier += ((((float) example.getValue(getAttribute(i))) - minima[i]) / (maxima[i] - minima[i])) * ((float) Math.cos((time * counter)));
                counter++;
            }
        }
        return fourier;
    }
}
