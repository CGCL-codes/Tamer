package com.ivis.xprocess.ui.charts.gantt;

import com.ivis.xprocess.core.WorkPackage;

public interface GanttContentProvider {

    /**
     * @param parent
     * @return an iterable of all the children in the parent
     */
    public Iterable<? extends WorkPackage> getChildren(WorkPackage parent);

    /**
     * @param parent
     * @return true if the parent has any children
     */
    public boolean hasChildren(WorkPackage parent);
}
