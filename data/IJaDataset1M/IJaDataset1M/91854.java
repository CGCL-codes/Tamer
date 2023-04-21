package au.vermilion.desktop.parameters;

import au.vermilion.desktop.CueObj;
import au.vermilion.desktop.ParameterBase;

/**
 * A parameter created to represent a finite number of states. This is stored as
 * a value between 00 and 99. The states are represented by a list of strings for
 * display, which must be passed in to construct the parameter.
 */
public class ParameterSwitch extends ParameterBase {

    /**
     * The value part of the parameter, from 00 to 99.
     */
    public short value;

    /**
     * The state names of the parameter, an array of strings.
     */
    public String[] states;

    /**
     * This constructor creates a parameter.
     * @param name A name for this parameter.
     * @param isTrack Indicates whether the parameter is global.
     * @param stateNames The names of the states for this switch.
     */
    public ParameterSwitch(String paramName, int paramID, int parentID, String[] stateNames) {
        super(paramName, paramID, PARAMETER_TYPE_SWITCH, parentID);
        states = stateNames;
    }

    @Override
    public void putValue(CueObj source) {
        value = source.cueVal;
    }

    @Override
    public CueObj getValue() {
        CueObj cueObj = new CueObj();
        cueObj.cueCmd = 0;
        cueObj.cueVal = value;
        return cueObj;
    }
}
