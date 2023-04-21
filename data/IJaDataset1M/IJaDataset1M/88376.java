package painter.app.ui.genview;

import genetic.component.expression.Expression;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import painter.app.ui.genview.valueview.ColorView;
import painter.app.ui.genview.valueview.Curve_colView;
import painter.app.ui.genview.valueview.Curve_dView;
import painter.app.ui.genview.valueview.DefaultView;

public class VisualNode extends JPanel {

    protected static Color nameTextColor = new Color(80, 80, 80);

    protected static Color titleBarBGColor = new Color(120, 120, 140);

    protected static Color titleBarRootBGColor = new Color(100, 100, 140);

    protected static Color titleBarTextColor = new Color(230, 230, 250);

    protected static Color mainBlockColor = new Color(235, 235, 235);

    protected static Font titleFont = new Font("Arial", Font.PLAIN, 12);

    protected static Font nameFont = new Font("Arial", Font.PLAIN, 10);

    protected static Font parameterNameFont = new Font("Arial", Font.BOLD, 11);

    protected static Color parameterNameColor = new Color(0, 0, 0);

    protected static Font parameterTypeFont = new Font("Arial", Font.PLAIN, 11);

    protected static Color parameterTypeColor = new Color(0, 0, 30);

    protected static Font parameterValueFont = new Font("Arial", Font.PLAIN, 11);

    protected static Color parameterValueColor = new Color(0, 0, 0);

    protected static final int titleBarHeight = 25;

    private Expression base;

    public int depth;

    private CirclePanel outputCircle;

    private CirclePanel inputCircles[];

    private JPanel titleBarRef;

    public void setIsRoot(boolean isRoot) {
        if (isRoot) {
            titleBarRef.setBackground(titleBarRootBGColor);
        } else {
            titleBarRef.setBackground(titleBarBGColor);
        }
    }

    public Point getOutputPoint() {
        return new Point(outputCircle.getLocationOnScreen().x + CirclePanel.circleWidth / 2, outputCircle.getLocationOnScreen().y + CirclePanel.circleWidth / 2);
    }

    public Point getInputPoint(int i) {
        return new Point(inputCircles[i].getLocationOnScreen().x + CirclePanel.circleWidth / 2, inputCircles[i].getLocationOnScreen().y + CirclePanel.circleWidth / 2);
    }

    public Expression getBase() {
        return base;
    }

    public VisualNode(Expression base) {
        this.base = base;
        setLayout(new BorderLayout());
        JPanel panel1, panel2, panel3, panel4;
        panel1 = createLeftPanel();
        panel2 = createMainPanel();
        panel3 = createRightPanel();
        panel4 = createContentPanel();
        add(panel1, BorderLayout.WEST);
        add(panel2, BorderLayout.CENTER);
        if (panel3 != null) {
            add(panel3, BorderLayout.EAST);
        }
        add(panel4, BorderLayout.SOUTH);
        setSize(getPreferredSize());
        setOpaque(false);
    }

    public void toFront() {
        if (getParent() instanceof JLayeredPane) {
            ((JLayeredPane) getParent()).moveToFront(this);
        }
    }

    protected class CircleTextPanel extends JPanel {

        CircleTextPanel(CirclePanel circle, String text) {
            JLabel outputLabel = new JLabel(text);
            outputLabel.setForeground(nameTextColor);
            outputLabel.setFont(nameFont);
            JPanel circleContainer = new JPanel();
            circleContainer.setLayout(new BoxLayout(circleContainer, BoxLayout.X_AXIS));
            circleContainer.add(Box.createHorizontalGlue());
            circleContainer.add(circle);
            circleContainer.add(Box.createHorizontalGlue());
            circleContainer.setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(outputLabel);
            add(circleContainer);
            setOpaque(false);
        }
    }

    protected JPanel createMainPanel() {
        final JPanel titleBar = createTitleBar();
        titleBarRef = titleBar;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(titleBar);
        panel.setBackground(mainBlockColor);
        int numberRows = base.getNumberParameters();
        if (numberRows > 0) {
            JPanel parameterTable = new ParameterPanel(base);
            panel.add(parameterTable);
        }
        return panel;
    }

    protected JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        String outputName = base.getOutputType().getName();
        outputName = outputName.substring(outputName.lastIndexOf(".") + 1);
        CirclePanel circlePanel = new CirclePanel(this, true, 0);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(new CircleTextPanel(circlePanel, outputName));
        panel.add(Box.createVerticalGlue());
        panel.setBorder(new EmptyBorder(3, 3, 3, 3));
        outputCircle = circlePanel;
        return panel;
    }

    protected JPanel createRightPanel() {
        if (base.getNumberInputs() == 0) {
            return null;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.setBorder(new EmptyBorder(3, 3, 3, 3));
        inputCircles = new CirclePanel[base.getNumberInputs()];
        for (int i = 0; i < base.getNumberInputs(); i++) {
            CirclePanel circlePanel = new CirclePanel(this, false, i);
            String inputName = base.getInputType(i).getName();
            inputName = inputName.substring(inputName.lastIndexOf(".") + 1);
            inputName = base.getInputName(i) + ": " + inputName;
            panel.add(new CircleTextPanel(circlePanel, inputName));
            panel.add(Box.createVerticalGlue());
            inputCircles[i] = circlePanel;
        }
        panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, (int) (panel.getPreferredSize().height * 1.5)));
        panel.setOpaque(false);
        return panel;
    }

    protected JPanel createTitleBar() {
        String titleName = base.getName();
        titleName = titleName.substring(titleName.lastIndexOf(".") + 1);
        final JLabel titleLabel = new JLabel(titleName);
        titleLabel.setForeground(titleBarTextColor);
        titleLabel.setFont(titleFont);
        JPanel titlePanel = new JPanel();
        Dimension dim = new Dimension(titleLabel.getPreferredSize().width + 25, titleLabel.getPreferredSize().height + 5);
        titlePanel.setPreferredSize(dim);
        titlePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, dim.height));
        titlePanel.setMinimumSize(dim);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setOpaque(true);
        titlePanel.setBackground(titleBarBGColor);
        titlePanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    protected JPanel createContentPanel() {
        Object value = base.getCacheValue();
        JPanel panel;
        if (value == null) {
            panel = new JPanel();
            panel.add(new JLabel("NULL"));
        } else if (value instanceof utils.linear.Color) {
            panel = new ColorView((utils.linear.Color) value);
        } else if (value instanceof utils.curves.CurveUtil.Curve_d) {
            panel = new Curve_dView((utils.curves.CurveUtil.Curve_d) value);
        } else if (value instanceof utils.curves.CurveUtil.Curve_col) {
            panel = new Curve_colView((utils.curves.CurveUtil.Curve_col) value);
        } else {
            panel = new DefaultView(value);
        }
        return panel;
    }
}
