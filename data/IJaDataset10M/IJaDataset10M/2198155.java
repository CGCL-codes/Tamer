package brightwell.tools;

import brightwell.analyser.Tool;

public class IsoPlot3D extends Tool {

    private String[] comboBoxEntries = { "entry 1", "entry 2", "entry 3" };

    /**
   * This function indicates Brightwell, if this tool needs a neural net. If a
   * neural net is needed, return true. If no neural net is needed return false.
   * Brightwell will ask for this value, when the run button is pressed. In case
   * a tool need a neural net, Brightwell will check if a net is selected or
   * created, and only proceeds, if so.
   * @return   true/false
   */
    public boolean needsNet() {
        return false;
    }

    /**
   * This function returns the name displayed in the tabbed pane. 
   */
    public String getToolName() {
        return "IsoPlot3d";
    }

    /**
   * This function returns a description of the tool which is displayed, when
   * the mouse is placed over a tool name for some seconds.
   */
    public String getToolDescription() {
        return "3-diemensional Isoperiodic plot";
    }

    /**
   * Initialising the Tool.
   *
   * <ul>
   *  <li> setup initial variables, if needed </li>
   *  <li> setup input panel if needed</li>
   *  <li> ... </li>
   * </ul>
   *
   * This function is called only onve, at the startup of Brightwell.
   *
   */
    public void init() {
        addComboBox("my selection", comboBoxEntries, 1);
        addDouble("my double value", -1.2, 1.5, 0.5);
        addInteger("my integer value", -10, 5, -2);
        addString("my string value", "some initial string");
        addCheckBox("my check box", false);
        setToolPriority(-5);
    }

    /**
   * This function is called after all initialisation is done. In this function
   * the main analysis routine is processed. It is called, when the user presses
   * the run-button, and if the check for a net has been positive.
   * Include your analysis here.
   */
    public void doAnalysis() {
    }
}
