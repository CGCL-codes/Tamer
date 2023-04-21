package org.gamenet.application.mm8leveleditor.control;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import org.gamenet.application.mm8leveleditor.data.mm6.indoor.LightSource;

public class LightSourceControl extends JPanel {

    private LightSource lightSource = null;

    public LightSourceControl(LightSource srcLightSource) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.lightSource = srcLightSource;
    }

    public Object getLightSource() {
        return lightSource;
    }
}
