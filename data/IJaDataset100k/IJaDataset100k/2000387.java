package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;

public class WorkItemList {

    private final List<WorkItemData> m_workItems;

    private final int m_sumDurations;

    public WorkItemList(final List<WorkItemData> workItems) {
        m_workItems = workItems;
        int sumDurations = 0;
        for (final WorkItemData workItem : m_workItems) {
            sumDurations += workItem.getDuration();
        }
        m_sumDurations = sumDurations;
    }

    public List<WorkItemData> getWorkItems() {
        return m_workItems;
    }

    public int getSumDurations() {
        return m_sumDurations;
    }
}
