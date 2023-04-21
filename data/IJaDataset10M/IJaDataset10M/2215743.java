package ui.event;

import kits.info.TdInfo;

/**
 * @author egolan
 *
 */
public final class ConnectionEvent {

    private final TdInfo tdInfo;

    public ConnectionEvent(final TdInfo tdInfo) {
        super();
        this.tdInfo = tdInfo;
    }

    public TdInfo getTdInfo() {
        return this.tdInfo;
    }
}
