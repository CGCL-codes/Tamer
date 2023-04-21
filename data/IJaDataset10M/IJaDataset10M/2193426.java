package org.nakedobjects.runtime.system.internal;

import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;

public class NakedObjectsTimeZoneInitializer {

    public static final Logger LOG = Logger.getLogger(NakedObjectsTimeZoneInitializer.class);

    public void initTimeZone(NakedObjectConfiguration configuration) {
        final String timeZoneSpec = configuration.getString(ConfigurationConstants.ROOT + "timezone");
        if (timeZoneSpec != null) {
            TimeZone timeZone;
            timeZone = TimeZone.getTimeZone(timeZoneSpec);
            TimeZone.setDefault(timeZone);
            LOG.info("time zone set to " + timeZone);
        }
        LOG.debug("time zone is " + TimeZone.getDefault());
    }
}
