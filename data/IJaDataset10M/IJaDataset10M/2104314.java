package org.specrunner.reuse.impl;

import java.util.HashMap;
import org.specrunner.reuse.IReusable;
import org.specrunner.reuse.IReusableManager;

/**
 * Default reusable manager implementation.
 * 
 * @author Thiago Santos
 * 
 */
@SuppressWarnings("serial")
public class ReusableManagerImpl extends HashMap<String, IReusable> implements IReusableManager {

    @Override
    public void remove(String name) {
        remove(get(name));
    }
}
