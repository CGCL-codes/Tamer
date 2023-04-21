package org.rjam.gui.analysis.subchart;

import org.rjam.gui.beans.Field;

public class VolumeStackedView extends SubChartView {

    public VolumeStackedView() {
        super("Volume", Field.FLD_NAME_COUNT, 1, false);
    }
}
