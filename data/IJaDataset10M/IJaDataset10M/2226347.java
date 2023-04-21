package net.sf.gridarta.gui.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for classes providing per-item tooltip text for {@link GList}
 * instances.
 * @author Andreas Kirschbaum
 */
public interface ToolTipProvider<T> {

    /**
     * Returns the tooltip text for an element.
     * @param element the element
     * @return the tooltip text or <code>null</code> to clear the tooltip text
     */
    @Nullable
    String getToolTipText(@NotNull final T element);
}
