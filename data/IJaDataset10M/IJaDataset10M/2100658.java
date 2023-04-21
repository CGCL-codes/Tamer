package mvt.tools.ode;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import mvt.tools.*;
import mvt.graphics.*;
import math.objects.*;
import math.numerics.*;
import math.exceptions.*;

/** Creates and lays out all the components in the input
 * panel for the 1D ODE Integtator.
 */
public class ODE1DInput extends InputPanel {

    private JComboBox stepType;

    private TextBoxPanel steppingBox;

    private TextBoxPanel functionBox;

    private TextBoxPanel tMinBox;

    private TextBoxPanel tMaxBox;

    private TextBoxPanel depVarICBox;

    private JComboBox odeType;

    private JButton plotButton;

    private JButton clearButton;

    private JButton viewNumbersButton;

    private JCheckBox overlayButton;

    /** This method adds the components to the panel and
     *  lays out those components.  In order to alter the
     *  input panel layout, subclass this panel and then
     *  override this method.
     */
    public void addComponents() {
        stepType = createStepSizeType();
        steppingBox = createStepSizeBox();
        functionBox = createDerivativeBox("y", "t", "sqrt(y)-t^2");
        tMinBox = createMinDomainBox("t", "0.0");
        tMaxBox = createMaxDomainBox("2.0");
        depVarICBox = createICBox("y", "1.0");
        odeType = createODEType();
        plotButton = createPlotButton();
        clearButton = createClearButton();
        viewNumbersButton = createViewNumbersButton();
        overlayButton = createOverlayButton();
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.add(functionBox);
        topPanel.add(tMinBox);
        topPanel.add(tMaxBox);
        topPanel.add(depVarICBox);
        middlePanel.add(new JLabel("Solver Type:"));
        middlePanel.add(odeType);
        middlePanel.add(stepType);
        middlePanel.add(steppingBox);
        middlePanel.add(overlayButton);
        bottomPanel.add(plotButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(viewNumbersButton);
        setLayout(new GridLayout(3, 1));
        add(topPanel);
        add(middlePanel);
        add(bottomPanel);
    }

    /** This method adds the default button listeners to the
     * plot and clear buttons.  To change this behavior, subclass
     * this panel and then override this method.
     */
    public void addListeners() {
        if (plotButton != null) plotButton.addActionListener(new PlotButtonListener());
        if (clearButton != null) clearButton.addActionListener(new ClearButtonListener());
        if (viewNumbersButton != null) viewNumbersButton.addActionListener(new ViewNumberListener());
        if (stepType != null) stepType.addActionListener(new StepSizeTypeListener(steppingBox));
        if (odeType != null) odeType.addActionListener(new ODETypeListener(stepType, steppingBox));
    }

    public void setVariables(VariableSet vars) {
        Variable indVar = vars.first();
        Variable depVar = vars.last();
        functionBox.setLabel("d" + depVar.toString() + "/d" + indVar.toString() + "=");
        tMinBox.setLabel(indVar.toString() + ": from");
        depVarICBox.setLabel("initial " + depVar.toString() + ":");
    }

    public Map getInput() throws SyntaxException {
        Hashtable data = new Hashtable();
        data.put("DYDT", functionBox.getFunction());
        data.put("TMIN", new Double(tMinBox.getDoubleValue()));
        data.put("TMAX", new Double(tMaxBox.getDoubleValue()));
        data.put("YINIT", new Double(depVarICBox.getDoubleValue()));
        data.put("METHOD", odeType.getSelectedItem());
        data.put("OVERLAY", new Boolean(overlayButton.isSelected()));
        if (!(odeType.getSelectedItem().equals("Adaptive RK4"))) if (stepType.getSelectedItem().equals("Step Size")) data.put("STEP_SIZE", new Double(steppingBox.getDoubleValue())); else data.put("NUM_STEPS", new Integer(steppingBox.getIntegerValue()));
        return data;
    }
}
