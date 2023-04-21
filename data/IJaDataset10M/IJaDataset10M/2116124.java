package org.sqlexp.controls.accessibility;

/**
 * Control capable of refreshing its content.
 * @author Matthieu Réjou
 */
public interface IRefreshControl {

    /**
	 * Returns true if a refresh action can be performed.
	 * @return true if user can refresh control centent
	 */
    boolean canRefresh();

    /**
	 * Refresh control content (typically on content).
	 */
    void refresh();
}
