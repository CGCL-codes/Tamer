package org.gamenet.application.mm8leveleditor.control;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.gamenet.application.mm8leveleditor.data.mm6.indoor.MapOutlineLine;

public class MapOutlineLineControl extends JPanel {

    private MapOutlineLine mapOutlineLine = null;

    public MapOutlineLineControl(MapOutlineLine srcMapOutlineLine) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.mapOutlineLine = srcMapOutlineLine;
        this.add(new JLabel("Offset: " + String.valueOf(mapOutlineLine.getMapOutlineLineOffset())));
    }

    public Object getMapOutlineLine() {
        return mapOutlineLine;
    }
}
