package de.sonivis.tool.ontology.view.views.searchView;

import java.util.Comparator;

public class RankComparator implements Comparator<SearchResultRow> {

    @Override
    public int compare(SearchResultRow left, SearchResultRow right) {
        if (left.getRank() < right.getRank()) return 1;
        if (left.getRank() == right.getRank()) return 0;
        if (left.getRank() > right.getRank()) return -1;
        return 0;
    }
}
