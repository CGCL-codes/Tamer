package org.mbari.vars.annotation.ui.dispatchers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.mbari.util.Dispatcher;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.qt.TimeSource;
import org.mbari.vcr.qt.VCR;
import org.mbari.vcr.timer.DefaultMonitoringVCR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quicktime.std.movies.Movie;

/**
 *
 * @author brian
 */
public class MovieChangeListener4VCR implements PropertyChangeListener {

    private final Dispatcher vcrDispatcher;

    private final IVCR defaultVcr;

    private final Dispatcher timeSourceDispatcher;

    private static final Logger log = LoggerFactory.getLogger(MovieChangeListener4VCR.class);

    /**
     * @param vcrDispatcher Should be PredefinedDispatcher.VCR.getDispatcher()
     * @param defaultVcr Should be PredefinedDispatcher.VCR.getDefaultValue()
     * @param timeSourceDispatcher The source of the timecode (either runtime or timecodetrack)
     */
    public MovieChangeListener4VCR(Dispatcher vcrDispatcher, IVCR defaultVcr, Dispatcher timeSourceDispatcher) {
        this.vcrDispatcher = vcrDispatcher;
        this.defaultVcr = defaultVcr;
        this.timeSourceDispatcher = timeSourceDispatcher;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Movie movie = (Movie) evt.getNewValue();
        if (movie != null) {
            vcrDispatcher.setValueObject(defaultVcr);
            try {
                IVCR vcr = new DefaultMonitoringVCR(new VCR(movie, (TimeSource) timeSourceDispatcher.getValueObject()));
                vcrDispatcher.setValueObject(vcr);
            } catch (Exception e) {
                log.error("Error occurred while creating VCR", e);
                vcrDispatcher.setValueObject(defaultVcr);
            }
        }
    }
}
