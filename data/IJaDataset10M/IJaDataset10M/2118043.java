package com.gantzgulch.samples.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetStore {

    private Map<Long, Widget> widgets = new HashMap<Long, Widget>();

    public WidgetStore() {
    }

    public void clear() {
        widgets.clear();
    }

    public void add(Widget widget) {
        if (widget == null || widget.getId() == null) {
            throw new IllegalArgumentException("Widget must be non-null with a non-null ID.");
        }
        if (findById(widget.getId()) != null) {
            throw new IllegalArgumentException("Widget ID already exists in the store.");
        }
        widgets.put(widget.getId(), widget);
    }

    /**
	 * Remove a widget from the store.
	 * 
	 * @param id of the widget to remove
	 * @return true if the widget existed in the store
	 */
    public boolean removeById(Long id) {
        return widgets.remove(id) != null;
    }

    public int getWidgetCount() {
        return widgets.size();
    }

    public Widget findById(Long id) {
        return widgets.get(id);
    }

    public List<Widget> getSortedWidgets(Comparator<Widget> comparator) {
        List<Widget> sortedList = new ArrayList<Widget>(widgets.values());
        Collections.sort(sortedList, comparator);
        return sortedList;
    }
}
