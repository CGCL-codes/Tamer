package org.xblackcat.rojac.gui;

import java.util.Deque;
import java.util.LinkedList;

import static org.xblackcat.rojac.service.options.Property.VIEW_NAVIGATION_HISTORY_SIZE;

/**
 * @author xBlackCat
 */
class NavigationHistoryTracker {
    private final Deque<NavigationHistoryItem> history = new LinkedList<>();
    private final Deque<NavigationHistoryItem> historyForward = new LinkedList<>();

    private NavigationHistoryItem currentItem = null;

    public void addHistoryItem(NavigationHistoryItem item) {
        if (currentItem != null && currentItem.equals(item)) {
            // Do not make duplicates
            return;
        }

        int maxSize = VIEW_NAVIGATION_HISTORY_SIZE.get();

        if (currentItem != null) {
            history.offerLast(currentItem);
        }
        historyForward.clear();

        while (history.size() > maxSize) {
            history.pollFirst();
        }

        currentItem = item;
    }

    public boolean canGoBack() {
        return !history.isEmpty();
    }

    public boolean canGoForward() {
        return !historyForward.isEmpty();
    }

    public NavigationHistoryItem goBack() {
        NavigationHistoryItem previous = history.pollLast();

        if (previous == null) {
            throw new IllegalStateException("History queue is empty!");
        }

        if (currentItem != null) {
            historyForward.offerFirst(currentItem);
        }
        currentItem = previous;

        return currentItem;
    }

    public NavigationHistoryItem goForward() {
        NavigationHistoryItem next = historyForward.pollFirst();

        if (next == null) {
            throw new IllegalStateException("History forward queue is empty!");
        }

        if (currentItem != null) {
            history.offerLast(currentItem);
        }
        currentItem = next;

        return currentItem;
    }
}
