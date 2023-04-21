package jhomenet.ui.panel.plot;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.apache.log4j.Logger;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Second;
import javax.measure.unit.Unit;
import jhomenet.commons.GeneralApplicationContext;
import jhomenet.commons.hw.sensor.*;
import jhomenet.commons.hw.data.*;

/**
 * The default value sensor plot panel.
 * 
 * @author David Irwin (jhomenet at gmail dot com)
 */
public class ValueSensorPlot extends AbstractPlot {

    /**
	 * Define the logging object.
	 */
    private static Logger logger = Logger.getLogger(ValueSensorPlot.class);

    /**
	 * Reference to the preferred unit.
	 */
    private final Unit preferredUnit;

    /**
	 * Default constructor.
	 * 
	 * @param preferredUnit
	 */
    public ValueSensorPlot(ValueSensor sensor, PlotPanel plotPanel, GeneralApplicationContext serverContext) {
        super((Sensor) sensor, plotPanel, serverContext);
        this.preferredUnit = sensor.getPreferredDataUnit();
        for (int channel = 0; channel < sensor.getNumChannels(); channel++) {
            TimeSeries ts = new TimeSeries(buildTimeSeriesKey(sensor, channel), org.jfree.data.time.Second.class);
            timeseriesCollection.addSeries(ts);
        }
    }

    /**
	 * @see jhomenet.ui.panel.plot.AbstractPlot#addDataInternal(org.jfree.data.time.TimeSeries, jhomenet.commons.hw.data.AbstractHardwareData)
	 */
    @Override
    void addDataInternal(TimeSeries ts, HardwareData data) {
        ts.addOrUpdate(new Second(data.getTimestamp()), ((HardwareValueData) data).getDataObject().getValue());
    }

    /**
	 * @see jhomenet.ui.panel.plot.AbstractPlot#modifyData(jhomenet.commons.hw.data.AbstractHardwareData)
	 */
    @Override
    protected final HardwareData modifyData(HardwareData data) {
        return ((HardwareValueData) data).convertDataUnit(preferredUnit);
    }

    /**
	 * Get the desired Y-label.
	 */
    @Override
    protected String getYLabel() {
        return "(" + preferredUnit.toString() + ")";
    }

    /**
	 * Receives property change events.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        logger.debug("Received property change event: " + propertyName);
        if (propertyName.equals(ValueSensor.PROPERTYNAME_CURRENTDATA)) {
            Object obj = propertyChangeEvent.getNewValue();
            if (obj instanceof HardwareValueData) {
                HardwareValueData data = (HardwareValueData) obj;
                logger.debug("New data: " + data.toString());
                plotData(data);
            } else if (obj instanceof List) {
                List<HardwareData> dataList = (List<HardwareData>) obj;
                logger.debug("New data list");
                plotData(dataList);
            }
        }
        super.propertyChange(propertyChangeEvent);
    }
}
