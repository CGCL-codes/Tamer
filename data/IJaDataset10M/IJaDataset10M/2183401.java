package com.googlecode.janrain4j.springframework;

import static com.googlecode.janrain4j.conf.Config.Builder.build;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import com.googlecode.janrain4j.conf.Config;
import com.googlecode.janrain4j.conf.ConfigHolder;

/**
 * {@link InitializingBean} that sets up a {@link Config} and exposes it to the 
 * {@link ConfigHolder}.
 * 
 * <p>Usage examples (using property placeholder values):
 * <pre>
 * &lt;bean class="com.googlecode.janrain4j.springframework.Janrain4jConfigurer"
 *       p:partnerApiKey="${janrain.partnerApiKey.}"
 *       p:apiKey="${janrain.apiKey}" 
 *       p:applicationID="${janrain.applicationID}" 
 *       p:applicationDomain="${janrain.applicationDomain}" 
 *       p:tokenUrl="${janrain.tokenUrl}" /&gt;
 * 
 * &lt;bean class="com.googlecode.janrain4j.springframework.Janrain4jConfigurer"
 *       p:partnerApiKey="${janrain.partnerApiKey.}"
 *       p:apiKey="${janrain.apiKey.}"
 *       p:applicationID="${janrain.applicationID.}"
 *       p:applicationDomain="${janrain.applicationDomain.}" 
 *       p:tokenUrl="${janrain.tokenUrl.}" 
 *       p:languagePreference="${janrain.languagePreference}" 
 *       p:proxyHost="${janrain.proxyHost}" 
 *       p:proxyPort="${janrain.proxyPort}" 
 *       p:proxyUsername="${janrain.proxyUsername}" 
 *       p:proxyPassword="${janrain.proxyPassword}" 
 *       p:connectTimeout="${janrain.connectTimeout}" 
 *       p:readTimeout="${janrain.readTimeout}" 
 *       p:setStatusProviderNames-ref="setStatusProviderNames"
 *       p:activityProviderNames-ref="activityProviderNames" /&gt;
 * </pre>
 * 
 * @author Marcel Overdijk
 * @since 1.1
 */
public class Janrain4jConfigurer implements InitializingBean {

    private Config config = null;

    public Janrain4jConfigurer() {
        config = build();
    }

    public void afterPropertiesSet() throws Exception {
        ConfigHolder.setConfig(config);
    }

    /**
     * Sets the Janrain Partner API key.
     *
     * @param partnerApiKey Your Janrain Partner API key.
     */
    public void setPartnerApiKey(String partnerApiKey) {
        config.partnerApiKey(partnerApiKey);
    }

    /**
     * Sets the Janrain API key.
     *
     * @param apiKey Your Janrain API key.
     */
    public void setApiKey(String apiKey) {
        config.apiKey(apiKey);
    }

    /**
     * Sets the Janrain application ID.
     * 
     * @param applicationID The application ID.
     */
    public void setApplicationID(String applicationID) {
        config.applicationID(applicationID);
    }

    /**
     * Sets the Janrain application domain.
     * 
     * @param applicationDomain The application domain.
     */
    public void setApplicationDomain(String applicationDomain) {
        config.applicationDomain(applicationDomain);
    }

    /**
     * Sets the token url.
     * 
     * @param tokenUrl The token url.
     */
    public void setTokenUrl(String tokenUrl) {
        config.tokenUrl(tokenUrl);
    }

    /**
     * Sets the language preference.
     * 
     * @param languagePreference The language preference.
     */
    public void setLanguagePreference(String languagePreference) {
        config.languagePreference(languagePreference);
    }

    /**
     * Sets the proxy host.
     * 
     * @param host The proxy host.
     */
    public void setProxyHost(String host) {
        config.proxyHost(host);
    }

    /**
     * Sets the proxy port.
     * 
     * @param port The proxy port.
     */
    public void setProxyPort(int port) {
        config.proxyPort(port);
    }

    /**
     * Sets the proxy username.
     * 
     * @param username The proxy username.
     */
    public void setProxyUsername(String username) {
        config.proxyUsername(username);
    }

    /**
     * Sets the proxy password.
     * 
     * @param password The proxy password.
     */
    public void setProxyPassword(String password) {
        config.proxyPassword(password);
    }

    /**
     * Set the connect timeout, in milliseconds, to be used when opening the 
     * communications link to the resource.
     * <p>
     * 0 implies that the option is disabled (i.e., timeout of infinity).
     * </p>
     * 
     * @param timeout The connect timeout value in milliseconds.
     */
    public void setConnectTimeout(int timeout) {
        config.connectTimeout(timeout);
    }

    /**
     * Sets the read timeout, in milliseconds, to be used when reading from the 
     * input steam.
     * <p>
     * 0 implies that the option is disabled (i.e., timeout of infinity).
     * </p>
     * 
     * @param timeout The read timeout value in milliseconds.
     */
    public void setReadTimeout(int timeout) {
        config.readTimeout(timeout);
    }

    /**
     * Sets the provider names which support the <code>set_status</code> API call.
     * 
     * @param providerNames The provider names.
     * @since 1.1
     */
    public void setSetStatusProviderNames(List<String> providerNames) {
        config.setStatusProviderNames(providerNames);
    }

    /**
     * Sets the provider names which support the <code>activity</code> API call.
     * 
     * @param providerNames The provider names.
     * @since 1.1
     */
    public void setActivityProviderNames(List<String> providerNames) {
        config.activityProviderNames(providerNames);
    }
}
