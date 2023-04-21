package simtools.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import simtools.data.DataException;
import simtools.shapes.PlotShape.Curve;

public class TimeCurveStatisticPanel extends CurveStatisticsPanel {

    public static SimpleDateFormat dateTimeFormatter;

    static {
        dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
    }

    public TimeCurveStatisticPanel(Curve c, PlotInformationDialog p) {
        super(c, p);
    }

    protected void updateStatistics() {
        try {
            curve.shape.computeStatistics(plotInformation.getXmin(), plotInformation.getXmax());
            localNbPoints.setText(new Long(curve.shape.getLocalNbPoints()).toString());
            localMinx.setText(dateTimeFormatter.format(new Date((long) curve.shape.getLocalRangeMinPoint().x)));
            localMiny.setText("Y = " + curve.shape.getLocalRangeMinPoint().y);
            localMaxx.setText(dateTimeFormatter.format(new Date((long) curve.shape.getLocalRangeMaxPoint().x)));
            localMaxy.setText("Y = " + curve.shape.getLocalRangeMaxPoint().y);
            mean.setText(new Double(curve.shape.getLocalRangeMean()).toString());
            integral.setText(new Double(curve.shape.getLocalIntegralValue()).toString());
            deviation.setText(new Double(curve.shape.getLocalDeviationValue()).toString());
            plotInformation.getHeaderPanel().clearAllMessages();
        } catch (DataException e) {
            localNbPoints.setText("");
            localMinx.setText("");
            localMiny.setText("");
            localMaxx.setText("");
            localMaxy.setText("");
            mean.setText("");
            integral.setText("");
            deviation.setText("");
            plotInformation.getHeaderPanel().displayWarning(PlotInformationDialog.resources.getString("noStatisticsAvailable"));
        }
    }
}
