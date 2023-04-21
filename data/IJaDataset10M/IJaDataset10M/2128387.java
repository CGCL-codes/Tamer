package mipt.gui.graph.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mipt.gui.IntegerTextField;
import mipt.gui.graph.GraphUtils;
import mipt.gui.graph.plot.CurveStyle;
import mipt.gui.graph.plot.Plot;
import mipt.gui.graph.primitives.Dot;
import mipt.gui.graph.primitives.LineStyle;
import mipt.gui.graph.primitives.dots.RectDot;

/**
 * Panel for editing curves properties. For separating the curves from each
 * other. For each curve {@link CurveStylePanel} created.
 * 
 * @author Zhmurov
 * 
 */
public class CurvesEditor extends JPanel implements ActionListener, KeyListener, ListSelectionListener {

    private ResourceBundle bundle = GraphPropertiesPanel.bundle;

    private final String APPLY_TO_ALL_COMMAND = bundle.getString("Apply to all command");

    public static final CurveStyle DEFAULT_CURVE_STYLE = new CurveStyle(new RectDot(new Dimension(3, 3), true, Color.RED), new LineStyle(Color.RED, 1, LineStyle.SOLID_LINE), 10);

    private JLabel curveSelectionLabel = new JLabel(bundle.getString("Curve selection label"));

    private JList curveSelectionList = new JList();

    private JLabel pontCountLabel = new JLabel(bundle.getString("Point count label"));

    private IntegerTextField pointCountTextField = new IntegerTextField(0, Integer.MAX_VALUE, true);

    private JLabel skipCountLabel = new JLabel(bundle.getString("Skip count label"));

    private IntegerTextField skipCountTextField = new IntegerTextField(0, Integer.MAX_VALUE, true);

    private JButton applyToAllButton = new JButton(APPLY_TO_ALL_COMMAND);

    private CurveStylePanel curvePanel = new CurveStylePanel();

    private Plot plot;

    private CurveStyle curves[];

    private int currentCurve = 0;

    public CurvesEditor(Plot plot) {
        super(new GridBagLayout());
        setPlot(plot);
        curveSelectionLabel.setFont(curveSelectionLabel.getFont().deriveFont(curveSelectionLabel.getFont().getSize2D() * 1.25f));
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(2, 2, 2, 2);
        JPanel propertiesPanel = initPropertiesPanel();
        JPanel curveSelectionPanel = initCurveSelectionPanel();
        constr.gridx = 0;
        constr.gridy = 0;
        constr.weightx = 0.0;
        constr.weighty = 1.0;
        constr.gridheight = 2;
        constr.fill = GridBagConstraints.VERTICAL;
        constr.anchor = GridBagConstraints.NORTHWEST;
        add(curveSelectionPanel, constr);
        constr.gridx = 1;
        constr.gridheight = 1;
        constr.weightx = 1.0;
        constr.weighty = 0.0;
        constr.fill = GridBagConstraints.HORIZONTAL;
        add(propertiesPanel, constr);
        constr.gridy = 1;
        add(curvePanel, constr);
        applyToAllButton.addActionListener(this);
        skipCountTextField.addKeyListener(this);
        pointCountTextField.addKeyListener(this);
        curveSelectionList.addListSelectionListener(this);
    }

    private JPanel initPropertiesPanel() {
        JPanel propertiesPanel = new JPanel(new GridBagLayout());
        JLabel parametersTitleLabel = new JLabel(bundle.getString("The curve editor panel title"));
        parametersTitleLabel.setFont(parametersTitleLabel.getFont().deriveFont(parametersTitleLabel.getFont().getSize2D() * 1.25f));
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(2, 2, 2, 2);
        constr.weightx = 1.0;
        constr.weighty = 0.0;
        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 3;
        constr.fill = GridBagConstraints.NONE;
        constr.anchor = GridBagConstraints.NORTHWEST;
        propertiesPanel.add(parametersTitleLabel, constr);
        constr.gridy = 1;
        constr.fill = GridBagConstraints.BOTH;
        propertiesPanel.add(new JSeparator(), constr);
        constr.gridwidth = 1;
        constr.fill = GridBagConstraints.NONE;
        constr.gridy = 2;
        propertiesPanel.add(pontCountLabel, constr);
        constr.gridy = 3;
        propertiesPanel.add(skipCountLabel, constr);
        constr.gridx = 1;
        constr.gridy = 2;
        constr.fill = GridBagConstraints.HORIZONTAL;
        propertiesPanel.add(pointCountTextField, constr);
        constr.gridy = 3;
        propertiesPanel.add(skipCountTextField, constr);
        constr.gridx = 2;
        propertiesPanel.add(applyToAllButton, constr);
        return propertiesPanel;
    }

    private JPanel initCurveSelectionPanel() {
        JPanel curveSelectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(2, 2, 2, 2);
        constr.gridx = 0;
        constr.gridy = 0;
        constr.weightx = 1.0;
        constr.weighty = 0;
        constr.fill = GridBagConstraints.NONE;
        constr.anchor = GridBagConstraints.NORTHWEST;
        curveSelectionPanel.add(curveSelectionLabel, constr);
        constr.gridy = 1;
        constr.fill = GridBagConstraints.HORIZONTAL;
        curveSelectionPanel.add(new JSeparator(), constr);
        constr.gridy = 2;
        constr.fill = GridBagConstraints.BOTH;
        curveSelectionPanel.add(curveSelectionList, constr);
        constr.gridy = 3;
        constr.weighty = 1.0;
        curveSelectionPanel.add(new JPanel(), constr);
        return curveSelectionPanel;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
        try {
            init();
        } catch (Exception e) {
            this.setEnabled(false);
        }
        ;
    }

    public CurveStyle[] getCurves() {
        try {
            updateCurves();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return curves;
    }

    public CurveStyle getCurveStyle(int i) {
        try {
            if (i == currentCurve) {
                updateCurves();
            }
            return curves[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return DEFAULT_CURVE_STYLE;
        }
    }

    private void init() {
        int curveCount = plot.getCurvesCount();
        curves = new CurveStyle[curveCount];
        for (int i = 0; i < curveCount; i++) {
            curves[i] = plot.getCurveStyle(i);
        }
        curveSelectionList.setListData(curves);
        curveSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        curveSelectionList.setLayoutOrientation(JList.VERTICAL);
        curveSelectionList.setCellRenderer(new SimpleCellRenderer());
        skipCountTextField.setIntegerValue(curves[0].skipDotCount);
        sinchronizeTextFields(skipCountTextField, pointCountTextField);
        setCurve(0);
    }

    private void setCurve(int number) {
        curvePanel.setCurveStyle(plot.getCurveStyle(number));
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand() == APPLY_TO_ALL_COMMAND && plot != null) {
            for (int i = 0; i < plot.getCurvesCount(); i++) {
                curves[i].skipDotCount = skipCountTextField.getIntegerValue();
            }
        }
    }

    public void keyReleased(KeyEvent ke) {
        if (plot != null) {
            if (ke.getSource() == skipCountTextField) {
                sinchronizeTextFields(skipCountTextField, pointCountTextField);
            } else if (ke.getSource() == pointCountTextField) {
                sinchronizeTextFields(pointCountTextField, skipCountTextField);
            }
        }
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void setEnabled(boolean flag) {
        curvePanel.setEnabled(flag);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == curveSelectionList) {
            curves[currentCurve] = curvePanel.getCurveStyle();
            curves[currentCurve].skipDotCount = skipCountTextField.getIntegerValue();
            currentCurve = curveSelectionList.getSelectedIndex();
            try {
                skipCountTextField.setIntegerValue(curves[currentCurve].skipDotCount);
                sinchronizeTextFields(skipCountTextField, pointCountTextField);
                curvePanel.setCurveStyle(curves[currentCurve]);
            } catch (NullPointerException ex) {
            }
        }
    }

    private void updateCurves() {
        curves[currentCurve] = curvePanel.getCurveStyle();
        curves[currentCurve].skipDotCount = skipCountTextField.getIntegerValue();
    }

    private void sinchronizeTextFields(IntegerTextField source, IntegerTextField target) {
        int curveLenght = plot.getCurve(currentCurve).getRenderer().getPointCount() - 1;
        int sc = source.getIntegerValue();
        if (sc < 0) {
            sc = 0;
            source.setIntegerValue(0);
        }
        target.setIntegerValue(curveLenght / (sc + 1));
    }

    /**
	 * @param curves
	 */
    public void setCurves(CurveStyle[] curves) {
        if (currentCurve >= curves.length) return;
        this.curves = curves;
        skipCountTextField.setIntegerValue(curves[currentCurve].skipDotCount);
        sinchronizeTextFields(skipCountTextField, pointCountTextField);
        curvePanel.setCurveStyle(curves[currentCurve]);
    }

    class SimpleCellRenderer extends JLabel implements ListCellRenderer {

        public SimpleCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final CurveStyle curveStyle = (CurveStyle) value;
            setText("y" + (index + 1));
            setIcon(new Icon() {

                public static final int lenght = 40;

                public static final int heigth = 20;

                public int getIconHeight() {
                    return heigth;
                }

                public int getIconWidth() {
                    return lenght;
                }

                public void paintIcon(Component c, Graphics g, int x, int y) {
                    LineStyle lineStyle = curveStyle.lineStyle;
                    g.setColor(lineStyle.color);
                    GraphUtils.drawHorLine(g, 10, 10, 20, lineStyle);
                    Dot dot = curveStyle.dot;
                    if (dot != null) {
                        dot.paint(g, 20, 10);
                    }
                }
            });
            setBackground(isSelected ? new Color(200, 200, 200) : Color.white);
            return this;
        }
    }
}
