package org.agilebracket.net.ip.matcher;

import org.agilebracket.net.ip.NetAddrIp;

/**
 *
 */
public interface IpMatcher {

    public boolean matches(NetAddrIp ipBlock);
}
