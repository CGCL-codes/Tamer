package net.sourceforge.webcompmath.applets;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.*;
import net.sourceforge.webcompmath.data.*;
import net.sourceforge.webcompmath.draw.*;
import net.sourceforge.webcompmath.awt.*;

/**
 * The MultiApplet can display the graphs of several functions, in different
 * colors. By default, there is only one function, but you can configure the
 * applet to use more than one function with applet params. The definitions of
 * these functions can, optionally, use parameters whose values are controled by
 * sliders at the bottom of the applet.
 */
public class MultiGraph extends GenericGraphApplet {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3258134652341532982L;

    private Vector sliders;

    private ExprIn[] inputs;

    private Graph1D[] graphs;

    private int functionCt;

    private Color[] graphColors = { Color.magenta, new Color(0, 180, 0), Color.red, new Color(0, 200, 200), Color.orange, Color.gray, Color.blue, Color.pink };

    private static class ColorPatch extends JPanel {

        /**
		 * Comment for <code>serialVersionUID</code>
		 */
        private static final long serialVersionUID = 3834031359407633717L;

        ColorPatch(Color c) {
            setBackground(c);
        }

        /**
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(25, 10);
        }

        /**
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    }

    private static class ExprIn extends ExpressionInput {

        /**
		 * Comment for <code>serialVersionUID</code>
		 */
        private static final long serialVersionUID = 4049641174338121778L;

        Graph1D graph;

        Function func;

        ExprIn(String definition, Parser p, Graph1D g, Variable v) {
            super(definition, p);
            graph = g;
            func = getFunction(v);
            if (definition.trim().length() > 0) graph.setFunction(func);
        }

        /**
		 * @see net.sourceforge.webcompmath.awt.ExpressionInput#checkInput()
		 */
        @Override
        public void checkInput() {
            if (!hasChanged) return;
            String text = getText().trim();
            if (text.length() == 0) {
                if (graph != null) graph.setFunction(null);
                hasChanged = false;
            } else {
                super.checkInput();
                if (graph != null) graph.setFunction(func);
            }
        }
    }

    /** Override this to add VariableSliders to parser. */
    @Override
    protected void setUpParser() {
        sliders = new Vector();
        int ct = 0;
        String param = getParameter("Parameter");
        if (param == null) {
            ct++;
            param = getParameter("Parameter" + ct);
        }
        while (true) {
            if (param == null) break;
            addParameter(param);
            ct++;
            param = getParameter("Parameter" + ct);
        }
        super.setUpParser();
    }

    @SuppressWarnings("unchecked")
    private void addParameter(String data) {
        double min = -5, max = 5, val = 0;
        data = data.trim();
        int pos = data.indexOf(';');
        if (pos < 0) pos = data.indexOf(' ');
        String name;
        if (pos < 0) {
            name = data;
        } else {
            String nums = data.substring(pos + 1);
            name = data.substring(0, pos).trim();
            StringTokenizer toks = new StringTokenizer(nums, " ,\t");
            try {
                if (toks.hasMoreElements()) min = (new Double(toks.nextToken())).doubleValue();
                if (toks.hasMoreElements()) max = (new Double(toks.nextToken())).doubleValue();
                if (toks.hasMoreElements()) val = (new Double(toks.nextToken())).doubleValue();
            } catch (NumberFormatException e) {
                min = -5;
                max = 5;
                val = 0;
            }
        }
        VariableSlider slide = new VariableSlider(name, new Constant(min), new Constant(max), parser);
        slide.setVal(val);
        sliders.addElement(slide);
    }

    @SuppressWarnings("unchecked")
    private void getColors() {
        Vector vec = new Vector();
        int ct = 0;
        Color c = getColorParam("GraphColor");
        if (c == null) {
            ct++;
            c = getColorParam("GraphColor" + ct);
        }
        while (true) {
            if (c == null) break;
            vec.addElement(c);
            ct++;
            c = getColorParam("GraphColor" + ct);
        }
        if (vec.size() > 0) {
            graphColors = new Color[vec.size()];
            for (int i = 0; i < vec.size(); i++) graphColors[i] = (Color) vec.elementAt(i);
        }
    }

    @SuppressWarnings("unchecked")
    private Vector getFunctions() {
        Vector functions = new Vector();
        int ct = 0;
        String c = getParameter("Function");
        if (c == null) {
            ct++;
            c = getParameter("Function" + ct);
        }
        while (true) {
            if (c == null) break;
            functions.addElement(c);
            ct++;
            c = getParameter("Function" + ct);
        }
        if (functions.size() == 0) functions.addElement(" abs( " + xVar.getName() + ") ^ " + xVar.getName());
        double[] d = getNumericParam("FunctionCount");
        if (d == null || d.length == 0 || d[0] <= 0.5) functionCt = functions.size(); else {
            functionCt = (int) Math.round(d[0]);
            if (functionCt < functions.size()) {
                functionCt = functions.size();
            } else {
                int extra = functionCt - functions.size();
                for (int i = 0; i < extra; i++) functions.addElement("");
            }
        }
        return functions;
    }

    private JPanel makeFunctionInput(Vector functions, int funcNum) {
        Graph1D graph = new Graph1D();
        graph.setColor(graphColors[funcNum % graphColors.length]);
        if (presentation) {
            graph.setLineWidth(3.0f);
        }
        ExprIn in = new ExprIn((String) functions.elementAt(funcNum), parser, graph, xVar);
        in.setOnUserAction(mainController);
        in.setForeground(ggFGColor);
        WcmPanel p = new WcmPanel();
        p.add(in, BorderLayout.CENTER);
        String name;
        if (functions.size() > 1) name = " " + getParameter("FunctionName", "f") + (funcNum + 1) + "(" + xVar.getName() + ") = "; else name = " " + getParameter("FunctionName", "f") + "(" + xVar.getName() + ") = ";
        JLabel jl = new JLabel(name);
        jl.setForeground(ggFGColor);
        p.add(jl, BorderLayout.WEST);
        if (graphColors.length > 1 && functions.size() > 1) p.add(new ColorPatch(graphColors[funcNum % graphColors.length]), BorderLayout.EAST);
        inputs[funcNum] = in;
        return p;
    }

    /** Overridden to create an appropriate input panel */
    @Override
    protected void setUpBottomPanel() {
        boolean funcInput = "yes".equalsIgnoreCase(getParameter("UseFunctionInput", "yes"));
        if (funcInput) {
            String cb = getParameter("UseComputeButton", "yes");
            if ("yes".equalsIgnoreCase(cb)) {
                String cname = getParameter("ComputeButtonName", "New Functions");
                computeButton = new JButton(cname);
                computeButton.setForeground(ggFGColor);
                computeButton.addActionListener(this);
            } else if ("queue".equalsIgnoreCase(cb)) {
                String cname = getParameter("ComputeButtonName", "New Functions");
                computeButton = new ComputeButton(cname);
                computeButton.setForeground(ggFGColor);
                computeButton.setBackground(UIManager.getLookAndFeelDefaults().getColor("Button.background"));
                ((ComputeButton) computeButton).setOnUserAction(mainController);
                WcmWorkerQueue.getInstance().setUseWorker(true);
            }
        }
        JPanel firstPanel = null;
        getColors();
        Vector functions = getFunctions();
        if (!funcInput && sliders.size() == 0) return;
        WcmPanel panel = new WcmPanel();
        if (!"no".equalsIgnoreCase(getParameter("TwoInputColumns", "no"))) panel.setLayout(new GridLayout(0, 2, 12, 3)); else panel.setLayout(new GridLayout(0, 1, 3, 3));
        panel.setBackground(getColorParam("PanelBackground", Color.lightGray));
        if (funcInput) {
            inputs = new ExprIn[functions.size()];
            for (int i = 0; i < functions.size(); i++) {
                JPanel p = makeFunctionInput(functions, i);
                p.setBackground(getColorParam("PanelBackground", Color.lightGray));
                if (firstPanel == null) firstPanel = p;
                panel.add(p);
            }
        } else {
            graphs = new Graph1D[functions.size()];
            for (int i = 0; i < functions.size(); i++) {
                graphs[i] = new Graph1D();
                graphs[i].setColor(graphColors[i % graphColors.length]);
                if (presentation) {
                    graphs[i].setLineWidth(3.0f);
                }
                String def = ((String) functions.elementAt(i)).trim();
                if (def.length() > 0) {
                    Function f = new SimpleFunction(parser.parse(def), xVar);
                    graphs[i].setFunction(f);
                }
            }
        }
        for (int i = 0; i < sliders.size(); i++) {
            WcmPanel p = new WcmPanel();
            p.setBackground(getColorParam("PanelBackground", Color.lightGray));
            VariableSlider slide = (VariableSlider) sliders.elementAt(i);
            p.add(slide, BorderLayout.CENTER);
            p.add(new DisplayLabel("  " + slide.getName() + " = # ", new Value[] { slide.getVariable() }), BorderLayout.EAST);
            panel.add(p);
            slide.setOnUserAction(mainController);
        }
        if (computeButton != null) {
            if (functions.size() == 1) firstPanel.add(computeButton, BorderLayout.EAST); else if (limitsPanel == null) {
                JPanel p = new JPanel();
                p.add(computeButton);
                panel.add(p);
            }
        }
        mainPanel.add(panel, BorderLayout.SOUTH);
    }

    /** add compute button if it hasn't been put somewhere else */
    @Override
    protected void setUpLimitsPanel() {
        super.setUpLimitsPanel();
        if (limitsPanel != null && computeButton != null && functionCt != 1) limitsPanel.addComponent(computeButton);
    }

    /** Overridden to add the graph to the canvas. */
    @Override
    protected void setUpCanvas() {
        super.setUpCanvas();
        if (graphs != null) {
            for (int i = 0; i < graphs.length; i++) canvas.add(graphs[i]);
        } else {
            for (int i = 0; i < inputs.length; i++) canvas.add(inputs[i].graph);
        }
    }

    /**
	 * This method is called when the user loads an example from the example
	 * menu (if there is one). It overrides an empty method in
	 * GenericGraphApplet. For the FamiliesOfGraphs applet, the example string
	 * should contain an expression that defines the function to be graphed.
	 * This must be followed by a semicolon and list of zero or more numbers.
	 * Then there is another semicolon and one or more function definitions,
	 * separated by semicolons. You can have as many function definitions as you
	 * have functions in your applet setup. (Note that having the numbers before
	 * the functions is different from the format of examples in all the other
	 * configurable applets. This is to allow more than one function.) Note that
	 * even if you leave out the numbers, you still need two semicolons. The
	 * list of numbers has the following meaning: The first four numbers give
	 * the x- and y-limits to be used for the example. If they are not present,
	 * then -5,5,-5,5 is used. The remaining numbers occur in groups of three.
	 * Each group give the maximum, minimum, and value of a parameters that was
	 * defined with the "Parameter", "Parameter1", ... applet params.
	 * 
	 * @param example
	 *            the function to graph
	 */
    @Override
    protected void doLoadExample(String example) {
        int pos = example.indexOf(";");
        double[] limits = { -5, 5, -5, 5 };
        if (pos > 0) {
            String nums = example.substring(0, pos);
            example = example.substring(pos + 1);
            StringTokenizer toks = new StringTokenizer(nums, " ,");
            if (toks.countTokens() >= 4) {
                for (int i = 0; i < 4; i++) {
                    try {
                        Double d = new Double(toks.nextToken());
                        limits[i] = d.doubleValue();
                    } catch (NumberFormatException e) {
                    }
                }
            }
            int i = 0;
            while (i < sliders.size() && toks.hasMoreElements()) {
                try {
                    double min = (new Double(toks.nextToken())).doubleValue();
                    double max = (new Double(toks.nextToken())).doubleValue();
                    double d = (new Double(toks.nextToken())).doubleValue();
                    VariableSlider slider = ((VariableSlider) sliders.elementAt(i));
                    slider.setMin(new Constant(min));
                    slider.setMax(new Constant(max));
                    slider.setVal(d);
                } catch (Exception e) {
                }
                i++;
            }
        }
        StringTokenizer toks = new StringTokenizer(example, ";");
        int funcNum = 0;
        while (funcNum < functionCt) {
            if (toks.hasMoreElements()) {
                String def = toks.nextToken();
                if (graphs != null) {
                    try {
                        graphs[funcNum].setFunction(new SimpleFunction(parser.parse(def), xVar));
                    } catch (ParseError e) {
                        graphs[funcNum].setFunction(null);
                    }
                } else inputs[funcNum].setText(def);
            } else {
                if (graphs != null) graphs[funcNum].setFunction(null); else inputs[funcNum].setText("");
            }
            funcNum++;
        }
        CoordinateRect coords = canvas.getCoordinateRect(0);
        coords.setLimits(limits);
        coords.setRestoreBuffer();
        mainController.compute();
    }
}
