package net.sourceforge.retriever.collector;

import java.util.List;

/**
 * Holds data about a resource that is ready to be consumed.
 */
public interface CollectEvent {

    /**
	 * Returns a list of resources ready to be consumed.
	 * 
	 * @return A list of resources ready to be consumed.
	 */
    List<Resource> getCollectedResources();
}
