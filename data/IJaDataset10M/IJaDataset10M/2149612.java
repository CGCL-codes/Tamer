package mvt.tools.vectorfield;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import mvt.tools.*;
import mvt.graphics.*;
import math.objects.*;
import math.numerics.*;
import math.exceptions.*;

/** Creates and lays out all the components in the input
 * panel for the 2D ODE Vector Field Plotter.
 */
public class ODEvf2DInput extends InputPanel {

    private TextBoxPanel function1Box;

    private TextBoxPanel function2Box;

    private TextBoxPanel xMinBox;

    private TextBoxPanel xMaxBox;

    private TextBoxPanel yMinBox;

    private TextBoxPanel yMaxBox;

    private JButton plotButton;

    private JButton clearButton;

    private JComboBox fieldType;

    /** This method adds the components to the panel and
     *  lays out those components.  In order to alter the
     *  input panel layout, subclass this panel and then
     *  override this method.
     */
    public void addComponents() {
        function1Box = createDerivativeBox("x", "t", "-y+x");
        function2Box = createDerivativeBox("y", "t", "x");
        xMinBox = createMinDomainBox("x", "-1.0");
        xMaxBox = createMaxDomainBox("1.0");
        yMinBox = createMinDomainBox("y", "-1.0");
        yMaxBox = createMaxDomainBox("1.0");
        plotButton = createPlotButton();
        clearButton = createClearButton();
        fieldType = createVectorFieldType();
        JPanel topLeftPanel = new JPanel();
        JPanel bottomLeftPanel = new JPanel();
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        JPanel bottomRightPanel = new JPanel();
        JPanel topRightPanel = new JPanel();
        JPanel leftPanel = new JPanel(new BorderLayout());
        topLeftPanel.add(function1Box);
        topLeftPanel.add(xMinBox);
        topLeftPanel.add(xMaxBox);
        bottomLeftPanel.add(function2Box);
        bottomLeftPanel.add(yMinBox);
        bottomLeftPanel.add(yMaxBox);
        leftPanel.add(topLeftPanel, BorderLayout.NORTH);
        leftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        topRightPanel.add(plotButton);
        topRightPanel.add(clearButton);
        bottomRightPanel.add(new JLabel("Field type:"));
        bottomRightPanel.add(fieldType);
        rightPanel.add(topRightPanel);
        rightPanel.add(bottomRightPanel);
        add(leftPanel);
        add(rightPanel);
    }

    /** This method adds the default button listeners to the
     * plot and clear buttons.  To change this behavior, subclass
     * this panel and then override this method.
     */
    public void addListeners() {
        if (plotButton != null) plotButton.addActionListener(new PlotButtonListener());
        if (clearButton != null) clearButton.addActionListener(new ClearButtonListener());
    }

    public void setVariables(VariableSet vars) {
        Variable[] var = vars.toArray();
        function1Box.setLabel("d" + var[1] + "/d" + var[0] + "=");
        function2Box.setLabel("d" + var[2] + "/d" + var[0] + "=");
        xMinBox.setLabel(var[1] + ": from");
        yMinBox.setLabel(var[2] + ": from");
    }

    public Map getInput() throws SyntaxException {
        Hashtable data = new Hashtable();
        data.put("DXDT", function1Box.getFunction());
        data.put("DYDT", function2Box.getFunction());
        data.put("XMIN", new Double(xMinBox.getDoubleValue()));
        data.put("XMAX", new Double(xMaxBox.getDoubleValue()));
        data.put("YMIN", new Double(yMinBox.getDoubleValue()));
        data.put("YMAX", new Double(yMaxBox.getDoubleValue()));
        data.put("OVERLAY", Boolean.FALSE);
        data.put("FIELD_TYPE", new Boolean(fieldType.getSelectedIndex() == 0));
        return data;
    }
}
