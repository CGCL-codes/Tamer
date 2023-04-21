package de.fzj.roctopus;

import de.fzj.roctopus.exceptions.RoctopusException;

/**
 * a Site can have a number of Storage(s) associated with it
 * 
 * @author roger
 */
public interface Storage extends Locatable {

    public Site getSite();

    public StorageType getType();

    public File asFile(String path) throws RoctopusException;
}
