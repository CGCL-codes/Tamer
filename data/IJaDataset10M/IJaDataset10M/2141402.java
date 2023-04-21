package net.spy.digg;

/**
 * A topic for stories.
 */
public interface Topic {

    /**
	 * Get the name of this topic's container.
	 */
    String getContainerName();

    /**
	 * Get the name of this topic.
	 */
    String getName();

    /**
	 * Get the short name of this topic.
	 */
    String getShortName();
}
