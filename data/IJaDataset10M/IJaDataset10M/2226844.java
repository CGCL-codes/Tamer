package net.grinder.plugin.http;

import HTTPClient.HTTPResponse;
import net.grinder.plugininterface.PluginException;

/**
 * When used with the HTTPClient implementation, string beans that
 * implement this interface will be passed the HTTPResponse object
 * after each test.
 *
 * @author Philip Aston
 * @version $Revision: 916 $
 */
public interface HTTPClientResponseListener {

    void handleResponse(HTTPResponse response) throws PluginException;
}
