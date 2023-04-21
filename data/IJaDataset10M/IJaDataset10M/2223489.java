package ui.control.buttonControl;

import input.InputVariables;
import output.OutputConsole;
import ui.dashboard.ControlPanel;
import ui.dashboard.DisplayPanel;
import ui.dashboard.OutputPanel;
import ui.exceptions.ControlButtonCustomizeException;

public interface ButtonPanelInterface {

    public abstract void addControlButton(AbstractControlButton button) throws ControlButtonCustomizeException;

    public abstract AbstractControlButton[] getAbstractControlButton();

    int getButtonSet();

    public void setInputVariables(InputVariables variable);

    public InputVariables getInputVariables();

    public void setOutputConsole(OutputConsole output);

    public OutputConsole getOutputConsole();

    public DisplayPanel getDisplayPanel();

    public void setDisplayPanel(DisplayPanel displayPanel);

    public OutputPanel getOutPanel();

    public void setOutPanel(OutputPanel outPanel);

    public ControlPanel getControlPanel();

    public void setControlPanel(ControlPanel controlPanel);
}
