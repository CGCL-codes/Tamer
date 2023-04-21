package com.phloc.commons.log.ctx;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.phloc.commons.log.format.LogFormatterBasicString;

/**
 * Dynamic context representing the current hostname.
 *
 * @author philip
 */
public final class LogContextDynamicHostname extends AbstractLogContextDynamic<String> {

    /** the cached host name */
    private String m_sHostname = null;

    /**
   * Constructor.
   *
   * @param bEnabled
   *          The default enabled state of the context. May not be
   *          <code>null</code>.
   */
    public LogContextDynamicHostname(final Boolean bEnabled) {
        super(bEnabled.booleanValue(), "The hostname on which the message occured.", "[" + LogFormatterBasicString.PATTERN + "]", String.class);
    }

    public String getContextValue() {
        if (m_sHostname == null) {
            try {
                m_sHostname = InetAddress.getLocalHost().getHostName();
            } catch (final UnknownHostException e) {
                m_sHostname = "localhost";
            }
        }
        return m_sHostname;
    }
}
