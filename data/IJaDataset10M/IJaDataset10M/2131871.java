package com.entelience.objects.portal;

import com.entelience.objects.chart.TwoDDatum;
import com.entelience.objects.chart.ChartGridLine;
import com.entelience.util.NumberHelper;
import com.entelience.util.Logs;
import org.apache.log4j.Logger;
import java.util.Date;

/**
 * Class to specify time-series data.
 */
public class TimeSeries extends com.entelience.objects.chart.TimeSeries implements PortalObject, java.io.Serializable {

    static final long serialVersionUID = -2444664892011857914L;

    protected static final Logger _logger = Logs.getLogger();

    /**
     * Don't use this one please. (it is needed to be javabean compliant)
     */
    public TimeSeries() {
        super();
    }

    public TimeSeries(int nbYGrids, boolean gridFrom0) {
        super(nbYGrids, gridFrom0);
    }

    @Override
    public int getType() {
        return ObjectType.CHART.getCode();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.CHART;
    }

    private String timeScale;

    private Date firstDate;

    private Date lastDate;

    /**
     */
    public void setMode(double mode, String modeName) throws Exception {
        _logger.trace("Setting time serie mode to (" + mode + ", " + modeName + ")");
        if (getValues() != null) {
            for (Object value : getValues()) {
                if (value instanceof TwoDDatum) {
                    ((TwoDDatum) value).setYvalue(((TwoDDatum) value).getYvalue() / mode);
                    ((TwoDDatum) value).resetOverLabel();
                }
            }
        }
        preCalculate();
        if ("time".equals(modeName)) {
            setDefaultUnit(false);
            double divisor = NumberHelper.getTimeDivisor((long) getMaxYValue());
            for (Object value : getValues()) {
                if (value instanceof TwoDDatum) {
                    ((TwoDDatum) value).setOverLabel(NumberHelper.formatDuration((long) ((TwoDDatum) value).getYvalue(), 2));
                    ((TwoDDatum) value).setYvalue(((TwoDDatum) value).getYvalue() / divisor);
                }
            }
            setMaxYLabel(NumberHelper.formatDuration((long) getMaxYValue(), 2));
            setMinYLabel(NumberHelper.formatDuration((long) getMinYValue(), 2));
            preCalculate();
            for (ChartGridLine cgl : getYgrid()) {
                cgl.setLabel(NumberHelper.formatDuration((long) (cgl.getValue() * divisor), 2));
            }
            setAverageYLabel(NumberHelper.formatDuration((long) (getAverageYValue() * divisor), 2));
        } else {
            setMaxYLabel(NumberHelper.formatDoubleToIntString(new Double(getMaxYValue())));
            setMinYLabel(NumberHelper.formatDoubleToIntString(new Double(getMinYValue())));
            preCalculate();
            for (ChartGridLine cgl : getYgrid()) {
                cgl.setLabel(NumberHelper.formatDoubleToString(new Double(cgl.getValue()), 1));
            }
            setAverageYLabel(NumberHelper.formatDoubleToIntString(new Double(getAverageYValue())));
        }
    }

    public void removeXLabels() {
    }

    public void setTimeScale(String timeScale) {
        this.timeScale = timeScale;
    }

    public String getTimeScale() {
        return timeScale;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    /**
     * required for javabean compliance
     *
     */
    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public void addDatum(Date date, double yValue, double xValue, String xLabel, String mouse) {
        super.addDatum(yValue, xValue, xLabel, mouse);
        if (firstDate == null) firstDate = date; else firstDate = firstDate.before(date) ? firstDate : date;
        if (lastDate == null) lastDate = date; else lastDate = lastDate.after(date) ? lastDate : date;
    }
}
