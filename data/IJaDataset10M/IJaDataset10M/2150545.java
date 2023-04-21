package org.systemsbiology.apps.corragui.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.systemsbiology.apps.corragui.client.data.inclusionList.InclusionListSummary;

@SuppressWarnings("serial")
public class InclusionListSummaries implements Serializable {

    private List<InclusionListSummary> summaries;

    public InclusionListSummaries() {
        summaries = new ArrayList<InclusionListSummary>();
    }

    public void addSummary(InclusionListSummary summary) {
        summaries.add(summary);
    }

    public int getSummaryCount() {
        return summaries.size();
    }

    public InclusionListSummary getSummary(int index) {
        if (index < 0 || index >= summaries.size()) throw new IndexOutOfBoundsException();
        return (InclusionListSummary) summaries.get(index);
    }

    public InclusionListSummary getSummaryByListName(String listName) {
        for (int i = 0; i < summaries.size(); i++) {
            if (listName.equals(getSummary(i).getListName())) return getSummary(i);
        }
        return null;
    }

    public void deleteSummary(InclusionListSummary summary) {
        summaries.remove(summary);
    }

    public void deleteAllSummaries() {
        summaries.clear();
    }
}
