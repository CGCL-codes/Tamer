package se.antimon.colourcontrols;

import java.awt.Color;
import se.antimon.colourcontrols.ColourControl.ColorSet;

public interface ColorEventListener {

    public void newColor(ColorSet colorSet, Color color);
}
