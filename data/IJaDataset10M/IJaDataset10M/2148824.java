package org.jzonic.yawiki.commands.admin;

import org.jzonic.yawiki.repository.*;

/**
 *
 * @author  Administrator
 */
public class PageInfoPair {

    private PageInfo localInfo;

    private PageInfo remoteInfo;

    public PageInfoPair(PageInfo localInfo, PageInfo remoteInfo) {
        this.remoteInfo = remoteInfo;
        this.localInfo = localInfo;
    }

    public PageInfo getRemoteInfo() {
        return remoteInfo;
    }

    public PageInfo getLocalInfo() {
        return localInfo;
    }
}
