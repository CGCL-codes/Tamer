package ui.controller.action;

import message.MessageId;
import query.framework.query.SearchQuery;
import ui.view.component.SearchUI;

public class SearchAction implements Action {

    private final SearchUI searchUI;

    private final SearchQuery searchQuery;

    public SearchAction(SearchUI searchUI, SearchQuery searchQuery) {
        this.searchUI = searchUI;
        this.searchQuery = searchQuery;
    }

    public void execute() {
        searchQuery.setCriteria(searchUI.getCriteria());
        searchUI.setResults(searchQuery.results());
    }

    public MessageId messageId() {
        return MessageId.search;
    }
}
