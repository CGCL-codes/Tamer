package org.drftpd.tests;

import org.drftpd.GlobalContext;
import org.drftpd.master.ConnectionManager;

public class DummyConnectionManager extends ConnectionManager {

    public void setGlobalContext(GlobalContext gctx) {
        _gctx = gctx;
    }
}
