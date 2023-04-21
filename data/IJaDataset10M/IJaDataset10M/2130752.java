package org.tmapi.core;

import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * Base class for all test factories.
 * 
 * @author TMAPI <a href="http://www.tmapi.org">tmapi.org</a>
 * @author Kal Ahmed
 * @version $Rev: 66 $ - $Date: 2008-08-20 07:26:30 -0400 (Wed, 20 Aug 2008) $
 */
public class TopicMapSystenFactoryTestBase extends TopicMapSystemFactory {

    TopicMapSystenFactoryTestBase() {
    }

    @Override
    public boolean getFeature(String featureName) throws FeatureNotRecognizedException {
        return false;
    }

    @Override
    public Object getProperty(String propertyName) {
        return null;
    }

    @Override
    public boolean hasFeature(String featureName) {
        return false;
    }

    @Override
    public TopicMapSystem newTopicMapSystem() throws TMAPIException {
        return null;
    }

    @Override
    public void setFeature(String featureName, boolean enable) throws FeatureNotSupportedException, FeatureNotRecognizedException {
    }

    @Override
    public void setProperty(String propertyName, Object value) {
    }
}
