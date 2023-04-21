package orcajo.azada.chart.models;

import orcajo.azada.chart.setting.ChartSetting;
import orcajo.azada.chart.util.DataSetProvider;

class StackedArea extends OverlayArea {

    StackedArea(ChartSetting setting, DataSetProvider provider) {
        super(setting, provider);
    }

    protected boolean isStacked() {
        return true;
    }
}
