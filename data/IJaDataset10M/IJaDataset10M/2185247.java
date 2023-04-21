package org.vaadin.pagingcomponent.strategy.impl;

import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.strategy.PageChangeStrategy;

/**
 * Strategy for an even number of buttons page.
 */
public final class EvenPageChangeStrategy extends PageChangeStrategy {

    public EvenPageChangeStrategy(ComponentsManager manager) {
        super(manager);
    }

    @Override
    protected int calculatePageNumberWhereStartTheIteration(int currentPage, int buttonPageMargin) {
        return currentPage > manager.getPreviousPage() ? currentPage - buttonPageMargin + 1 : currentPage - buttonPageMargin;
    }
}
