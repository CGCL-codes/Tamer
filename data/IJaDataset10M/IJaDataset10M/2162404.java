package au.com.lastweekend.openjaws.plugins;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.lastweekend.openjaws.api.Readings;
import au.com.lastweekend.openjaws.api.WeatherPlugin;
import au.com.lastweekend.openjaws.api.WeatherPluginException;
import au.com.lastweekend.openjaws.api.WeatherStation;
import au.com.lastweekend.openjaws.api.WeatherStationException;
import au.com.lastweekend.openjaws.api.WeatherStation.HistoryIterator;

/**
 * Retrieves historical readings from the station.
 * 
 * Sets a marker value in the stationValues map if it has supplied a reading. This marker is used by other plugins to skip
 * processing for historical readings. {@link #isHistoricalReading(Readings)}
 * 
 * Once all the available historical readings have been exhausted this plugin does nothing.
 * 
 * 
 * @author grant@lastweekend.com.au
 * @version $Id: codetemplates.xml,v 1.2 2005/01/22 06:53:11 ggardner Exp $
 */
public class HistoricalReadingsPlugin extends DecoratedWeatherPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricalReadingsPlugin.class);

    static final String STATION_VALUE = HistoricalReadingsPlugin.class.getCanonicalName();

    private WeatherStation ws;

    private HistoryIterator histIterator = null;

    public static boolean isHistoricalReading(Readings readings) {
        return readings.getStationValues().containsKey(STATION_VALUE);
    }

    public HistoricalReadingsPlugin(WeatherStation ws, WeatherPlugin decorated) {
        super(decorated);
        this.ws = ws;
    }

    @Override
    protected void preProcess(Readings readings) throws WeatherPluginException {
        try {
            if (histIterator == null) {
                Date lastReadingDate = readings.getReadingTime();
                LOG.info(String.format("Fetching history since %tc", lastReadingDate));
                histIterator = ws.historyIterator(lastReadingDate);
            }
            if (histIterator.hasNext()) {
                histIterator.next(readings);
                readings.putStationValue(STATION_VALUE, Boolean.TRUE);
                LOG.info(String.format("Processing historical reading for %tc", readings.getReadingTime()));
            }
        } catch (WeatherStationException e) {
            throw new WeatherPluginException("Exception processing history");
        }
    }

    @Override
    protected void postProcess(Readings readings) throws WeatherPluginException {
        readings.removeStationValue(STATION_VALUE);
    }
}
