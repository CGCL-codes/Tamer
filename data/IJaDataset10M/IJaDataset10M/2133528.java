package core.interfaces.listeners;

import core.Podcast;

/**
 * An object must implement this interface if wants to be warned when new items
 * are found.
 * @author Mario
 */
public interface INewElementListener {

    /**
     * Method called by the podcast information retriever.
     * Write here the code you want to be executed when new elements are found.
     * @param podcast The podcast containing new items. The new items found are 
     * available in the availableList. Plase note that an element is a new
     * item unless it is downloaded or dropped.
     */
    public void newElementFound(Podcast podcast);
}
