package org.pustefixframework.extension;

import java.util.List;
import org.pustefixframework.config.directoutputservice.DirectOutputPageRequestConfig;

/**
 * Extension for a {@link DirectOutputPageRequestConfigExtensionPoint}.  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public interface DirectOutputPageRequestConfigExtension extends Extension {

    /**
     * Returns the list of {@link DirectOutputPageRequestConfig}s this extension provides.
     * 
     * @return list of page configurations
     */
    List<DirectOutputPageRequestConfig> getDirectOutputPageRequestConfigs();
}
