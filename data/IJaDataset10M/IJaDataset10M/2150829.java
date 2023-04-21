package org.fh.auge.chart.desc;

import java.util.List;
import org.fh.auge.chart.model.Data;
import org.fh.auge.chart.model.series.Series;

public interface Indicator {

    public void setInput(Data d);

    public List<Series> createSeries();

    public String getName();
}
