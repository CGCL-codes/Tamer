package search.example.srch;

import search.example.srch.impl.BooleanClauseOccur;

public interface BooleanQuery extends Query {

    public void add(Query query, BooleanClauseOccur occur);

    public void addExactTerm(String field, String value);

    public void addRequiredTerm(String field, String value);

    public void addTerm(String field, String value) throws SearchException;
}
