package org.appspy.client.common;

import org.appspy.client.common.config.CollectorConfig;
import org.appspy.core.data.CollectedData;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public interface Collector {

    public CollectedData beginCollection(CollectedData collectedData);

    public void endCollection();

    public void init(CollectorConfig config);

    public void destroy();
}
