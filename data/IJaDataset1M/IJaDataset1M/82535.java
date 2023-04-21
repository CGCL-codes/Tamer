package kfschmidt.quickvol;

import java.util.Properties;

public class QuickVolPreferences extends Properties {

    public QuickVolPreferences() {
        setProperty("stackviewerplugins", "Outline=kfschmidt.quickvol.plugins.PenTool,OutlineEraser=kfschmidt.quickvol.plugins.EraserPenTool,Large Brush=kfschmidt.quickvol.plugins.LargeBrush,Small Brush=kfschmidt.quickvol.plugins.SmallBrush,Eraser=kfschmidt.quickvol.plugins.Eraser,Image Registration=kfschmidt.quickvol.plugins.ManualRegistration");
    }
}
