package volantis.mcs.example.utilities;

import com.volantis.mcs.utilities.ChartData;
import com.volantis.mcs.utilities.ChartValues;

public class ExampleChartLegend implements ChartData {

    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    ChartValues labels = new ChartValues("one, two");

    public ChartValues getData() {
        return null;
    }

    public ChartValues getLabels() {
        return labels;
    }
}
