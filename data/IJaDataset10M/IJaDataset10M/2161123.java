package org.petsoar.actions.storefront;

import org.petsoar.pets.Pet;
import org.petsoar.search.Searcher;
import org.petsoar.search.SearcherAware;
import java.util.ArrayList;

public class StoreFrontSearch extends AbstractPaginatableAction implements SearcherAware {

    private String query;

    private Searcher searcher;

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Pet getPet() {
        return (Pet) getPets().get(0);
    }

    protected Class getType() {
        return Pet.class;
    }

    public String execute() {
        try {
            setPets(searcher.search(query));
            return SUCCESS;
        } catch (Throwable e) {
            setPets(new ArrayList());
            addFieldError("query", "Invalid query");
            return INPUT;
        }
    }
}
