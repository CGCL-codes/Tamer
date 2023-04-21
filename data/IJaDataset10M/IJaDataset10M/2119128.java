package com.l2jserver.gameserver.instancemanager;

import java.util.logging.Logger;

public class HellboundManager {

    private static final Logger _log = Logger.getLogger(HellboundManager.class.getName());

    private HellboundManager() {
        _log.info(getClass().getSimpleName() + ": Initializing");
        init();
    }

    private void init() {
        _log.info(getClass().getSimpleName() + ": Mode: dummy");
        if (isLocked()) _log.info(getClass().getSimpleName() + ": State: locked"); else _log.info(getClass().getSimpleName() + ": State: unlocked");
    }

    /**
	 * Returns true if Hellbound is locked
	 */
    public boolean isLocked() {
        return false;
    }

    public static final HellboundManager getInstance() {
        return SingletonHolder._instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final HellboundManager _instance = new HellboundManager();
    }
}
