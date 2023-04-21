package org.jfree.beans.events;

import java.util.EventListener;
import org.jfree.beans.AbstractCategoryChart;

/**
 * A listener for item clicks on a {@link AbstractCategoryChart}.
 */
public interface CategoryItemClickListener extends EventListener {

    /**
     * Called when the user clicks on an item within a category chart.
     * 
     * @param event  the event.
     */
    public void onCategoryItemClick(CategoryItemClickEvent event);
}
