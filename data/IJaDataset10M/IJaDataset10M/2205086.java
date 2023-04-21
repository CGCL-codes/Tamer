package com.mgnutella.core.modules.search;

import com.mgnutella.core.modules.Module;

/**
 *  @author pardus
 */
public class SearchManager implements Module {

    public static final String name = "SearchManager";

    public void startUp() {
    }

    public void shutDown() {
    }

    public void freeze() {
    }

    public void disSolve() {
    }

    public boolean isFrozen() {
        return true;
    }

    public void restart() {
    }

    public void checkMicroSystems() {
    }

    public String getModuleName() {
        return name;
    }

    public boolean isReady() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
