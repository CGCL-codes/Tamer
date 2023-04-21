package org.monet.kernel.components;

import java.util.HashMap;
import org.monet.kernel.exceptions.SystemException;

public abstract class Component {

    protected static String NAME;

    protected Boolean isStarted;

    protected Component() {
        this.isStarted = false;
    }

    public abstract HashMap<Integer, Boolean> getSupportedFeatures();

    public abstract void start() throws SystemException;

    public abstract void stop() throws SystemException;
}
