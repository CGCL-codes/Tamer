package net.sourceforge.blogentis.prefs;

import java.util.List;
import net.sourceforge.blogentis.turbine.BlogRunData;
import org.apache.commons.configuration.Configuration;

/**
 * @author abas
 */
public interface ISetting {

    /**
     * Get the name of the setting. tha name is used for identifying settings in
     * a request-response cycle, they are usually the parameter names used.
     * 
     * @return a string that identifies thsi setting.
     */
    public String getName();

    /**
     * Get the user-visible title of the configuration setting
     * 
     * @return the name of the setting.
     */
    public String getTitle();

    /**
     * Get the description of the configuration setting
     * 
     * @return a description of the effects of this setting.
     */
    public String getDescription();

    /**
     * Render the input elements of the setting. No name or description should
     * appear here, they are managed by other means.
     * 
     * @param data
     *            the BlogRunData of the current request
     * @param conf
     *            the configuration from which to pull the settings.
     * @return an object whose toString() method will be called and used as
     *         HTML.
     */
    public String render(BlogRunData data, Configuration conf);

    /**
     * Parse the setting from the request and store it into the passed
     * configuration. If a validation error occurs, the
     * 
     * @param data
     *            the BlogRunData of the current request
     * @param conf
     *            the configuration into which the settings will be set.
     */
    public void parseFromRequest(BlogRunData data, Configuration conf);

    public List getMessages(BlogRunData data);
}
