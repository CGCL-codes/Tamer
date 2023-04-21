package com.igentics.horsepower.form;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import rossi.dfp.calc.RpnCalcStateMachine;
import com.igentics.horsepower.HorsePowerCanvas;

/**
 * @author williams
 */
public class EditOptionsForm extends Form {

    private ChoiceGroup _angleModeChoice;

    private ChoiceGroup _displayTypeChoice;

    public EditOptionsForm(String title) {
        super(title);
        _angleModeChoice = new ChoiceGroup("Angle Mode", Choice.EXCLUSIVE, new java.lang.String[] { "Degrees", "Radians", "Gradians" }, new Image[] { null, null, null });
        _displayTypeChoice = new ChoiceGroup("Display Type", Choice.EXCLUSIVE, new java.lang.String[] { "Red LED", "LCD" }, new Image[] { null, null });
        append(_angleModeChoice);
        append(_displayTypeChoice);
    }

    public void setOptions(RpnCalcStateMachine engine, HorsePowerCanvas canvas) {
        _angleModeChoice.setSelectedIndex(engine.getAngleMode(), true);
        _displayTypeChoice.setSelectedIndex(canvas.getDisplayType(), true);
    }

    public void getOptions(RpnCalcStateMachine engine, HorsePowerCanvas canvas) {
        engine.setAngleMode(_angleModeChoice.getSelectedIndex());
        canvas.setDisplayType(_displayTypeChoice.getSelectedIndex());
    }
}
