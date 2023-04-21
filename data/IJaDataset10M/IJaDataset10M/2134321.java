package config;

import joj.web.ControllerConfig;
import joj.web.urls.URLMapper;

/**
 * @author Jason Miller (heinousjay@gmail.com)
 *
 */
public abstract class NotConfig implements ControllerConfig {

    public URLMapper mapURLs() {
        return new URLMapper() {

            {
                register("/:controller/:action");
            }
        };
    }
}
