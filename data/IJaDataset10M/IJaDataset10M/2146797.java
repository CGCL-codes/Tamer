package org.icy.plugins;

import org.icy.core.message.Observer;
import org.icy.exceptions.IcySetupException;

public interface Plugin extends Observer {

    public String getName();

    public void load() throws IcySetupException;

    public void unload() throws IcySetupException;

    public void activate() throws IcySetupException;

    public void deactivate() throws IcySetupException;
}
