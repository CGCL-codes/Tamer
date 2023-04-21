package edu.csula.coolstatela.search.fts;

import java.util.List;

public interface Searcher {

    public List<Integer> search(String queryString);
}
