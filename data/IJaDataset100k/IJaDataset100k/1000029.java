package net.sf.gridarta.gui.utils.tabbedpanel;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners interested in {@link ButtonLists} related events.
 * @author Andreas Kirschbaum
 */
public interface ButtonListsListener {

    /**
     * Called whenever thee selected tab has changed.
     * @param prevTab the previously selected tab
     * @param tab the newly selected tab
     */
    void tabChanged(@Nullable Tab prevTab, @Nullable Tab tab);
}
