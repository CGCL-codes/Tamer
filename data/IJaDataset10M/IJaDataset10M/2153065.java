package ch.kwa.ee.model.transkription;

import ch.kwa.ee.source.SearchDefinition;

public class MatchElement extends ContainerElement {

    SearchDefinition fromSearch;

    public SearchDefinition getFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(SearchDefinition fromSearch) {
        this.fromSearch = fromSearch;
    }
}
