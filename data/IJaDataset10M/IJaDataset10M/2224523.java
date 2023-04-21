package fr.thomascoffin.mocaf;

import static fr.thomascoffin.mocaf.EasyGBC.*;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This example provides a full example of EasyGBC methods and thus of GridBagConstraints functionalities.
 * Running this example using the main method will display a frame containing a grid of labels, each label
 * displaying a number and the EasyGBC commands applied to layout the label, starting from the previous label.
 * Label tooltips display the full parameters of the GridBagConstraints used to display the label.
 * A left-click on a label will highlight it, and will highlight as well the previous label added to the panel.
 * This way, one can easily understand the impact of EasyGBC methods. This also provides a convenient way to understand
 * and test GridBagConstraints functionalities.
 * One can extend the class and override the fill method, containing a list of calls to the ad method,
 * to test a set of EasyGBC commands.
 * It is not recommanded to use the add() method outside of the fill() method, since the dimensions of the grid
 * are computed just after the fill() method is exited, based on the parameters that were provided in the add() method
 * calls already made at this time.
 * <p/>
 * <p/>
 * This class is released by Thomas Coffin (thomas.coffin@gmail.com) under the <a href="http://www.gnu.org/copyleft/lesser.html" target="_blank">LGPL License</a>
 * as a component of the <a href="http://code.google.com/p/mocaf" target="_blank">mocaf project</a>
 * <p/>
 * (c) Thomas Coffin 2008.
 */
public class EasyGBCFullExample {

    private static final Insets NO_INSETS = new Insets(0, 0, 0, 0);

    private static final EasyGBC STANDARD_GBC = new EasyGBC(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, NO_INSETS, 0, 0);

    private final JPanel _testPanel;

    private int _labelCounter;

    private JLabel _lastDefinedLabel;

    private JLabel _currentFrom;

    private JLabel _currentTo;

    private int _maxX;

    private int _maxY;

    /**
	 * Runs the example.
	 *
	 * @param args no argument is taken into account.
	 */
    public static void main(String[] args) {
        new EasyGBCFullExample();
    }

    /**
	 * Default constructor. Can be used to run the example instead of the main method in order to extend the example.
	 */
    public EasyGBCFullExample() {
        _testPanel = new JPanel(new GridBagLayout());
        _testPanel.setBackground(Color.BLACK);
        _labelCounter = 0;
        _lastDefinedLabel = null;
        _maxX = 0;
        _maxY = 0;
        fill();
        EasyGBC xAxisGBC = STANDARD_GBC.clone();
        EasyGBC yAxisGBC = xAxisGBC.clone();
        for (int axisPosition = 1; axisPosition <= _maxX; axisPosition++) {
            JLabel xAxisLabel = createAxisLabel(axisPosition);
            _testPanel.add(xAxisLabel, xAxisGBC.x(axisPosition));
        }
        for (int axisPosition = 1; axisPosition <= _maxY; axisPosition++) {
            JLabel yAxisLabel = createAxisLabel(axisPosition);
            _testPanel.add(yAxisLabel, yAxisGBC.y(axisPosition));
        }
        EasyGBC chessLabelGBC = STANDARD_GBC.clone();
        for (int x = 0; x <= _maxX; x++) {
            chessLabelGBC.x(x);
            for (int y = 0; y <= _maxY; y++) {
                addGridLabel(chessLabelGBC.y(y));
            }
        }
        JFrame testFrame = new JFrame(getClass().getSimpleName());
        testFrame.getContentPane().add(_testPanel);
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.pack();
        testFrame.setVisible(true);
    }

    /**
	 * This method fills the grid with labels, using calls to the add() method. This is the only method to override
	 * in order to extend the example.
	 */
    protected void fill() {
        EasyGBC gbc = STANDARD_GBC.clone();
        add("initial", gbc.x(1).y(1));
        add("fill(NONE).goDown", gbc.fill(NONE).goDown());
        add("anchor(WEST).goDown(2)", gbc.anchor(WEST).goDown(2));
        add("y(5).center()", gbc.y(5).center());
        add("fill(HORIZONTAL).goRight()", gbc.fill(HORIZONTAL).goRight());
        add("fillBoth().goRight(2)", gbc.fillBoth().goRight(2));
        add("height(2).goUp().goUp()", gbc.height(2).goUp().goUp());
        add("north().goLeft()", gbc.north().goLeft());
        add("horizontalFill().x(2)", gbc.horizontalFill().x(2));
        add("east().verticalFill().width(2).x(5)", gbc.east().verticalFill().width(2).x(5));
        add("x(1).y(7).size(1).fill(NONE).northWest()", gbc.x(1).y(7).size(1).fill(NONE).northWest());
        add("goRight().padX(20)", gbc.goRight().padX(20));
        add("goRight().padY(40)", gbc.goRight().padY(40));
        add("goRight().fill(BOTH).noPad().insets(new Insets(3,7,12,18))", gbc.goRight().fill(BOTH).padX(0).padY(0).insets(new Insets(3, 7, 12, 18)));
        add("goRight().insets(3)", gbc.goRight().insets(3));
        add("goUp(2).noFill()", gbc.goUp(2).noFill());
        add("default", new EasyGBC());
    }

    private void addGridLabel(EasyGBC gbc) {
        JLabel axisLabel = new JLabel();
        axisLabel.setOpaque(false);
        axisLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        _testPanel.add(axisLabel, gbc);
    }

    private static JLabel createAxisLabel(int axisPosition) {
        JLabel axisLabel = new JLabel("" + axisPosition, JLabel.CENTER);
        axisLabel.setBackground(axisPosition % 2 == 1 ? Color.WHITE : Color.LIGHT_GRAY);
        axisLabel.setForeground(Color.BLACK);
        axisLabel.setOpaque(true);
        axisLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        return axisLabel;
    }

    /**
	 * Adds a label containing the given text, using the given EasyGBC. The mouselistener, label number and tooltips
	 * providing the highlighting and self-explaining behaviour are added automatically.
	 *
	 * @param text				the text to display in the label.
	 * @param constraints the constraints used to display the label in the grid.
	 */
    protected final void add(String text, EasyGBC constraints) {
        if (constraints.gridx > _maxX) {
            _maxX = constraints.gridx;
        }
        if (constraints.gridy > _maxY) {
            _maxY = constraints.gridy;
        }
        JLabel label = new JLabel(++_labelCounter + " : " + text, JLabel.CENTER);
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        label.addMouseListener(new DeltaLabelEnhancer(_lastDefinedLabel, label));
        label.setToolTipText(constraints.toString());
        Statut.NORMAL.set(label);
        _lastDefinedLabel = label;
        _testPanel.add(label, constraints);
    }

    private enum Statut {

        NORMAL(Color.DARK_GRAY), CURRENT_FROM(Color.BLUE), CURRENT_TO(Color.RED);

        private final Color _color;

        Statut(Color color) {
            _color = color;
        }

        public void set(@Nullable JLabel label) {
            if (label != null) {
                label.setBackground(_color);
            }
        }
    }

    private class DeltaLabelEnhancer extends MouseAdapter {

        private final JLabel _previousLabel;

        private final JLabel _label;

        public DeltaLabelEnhancer(JLabel previousLabel, JLabel label) {
            _previousLabel = previousLabel;
            _label = label;
        }

        public void mouseClicked(MouseEvent e) {
            Statut.NORMAL.set(_currentFrom);
            Statut.NORMAL.set(_currentTo);
            _currentFrom = _previousLabel;
            _currentTo = _label;
            Statut.CURRENT_FROM.set(_currentFrom);
            Statut.CURRENT_TO.set(_currentTo);
        }
    }
}
