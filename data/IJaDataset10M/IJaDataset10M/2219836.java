package alvis.search;

import java.util.List;
import alvis.dtd.migrate.Lemma;
import alvis.dtd.migrate.NamedEntity;
import alvis.dtd.migrate.Term;

public class QueryResponse {

    private List<Lemma> lemmas;

    private List<Term> terms;

    private List<NamedEntity> namedEntity;

    private String newQuery;

    public List<Lemma> getLemmas() {
        return lemmas;
    }

    public void setLemmas(List<Lemma> lemmas) {
        this.lemmas = lemmas;
    }

    public List<NamedEntity> getNamedEntity() {
        return namedEntity;
    }

    public void setNamedEntity(List<NamedEntity> namedEntity) {
        this.namedEntity = namedEntity;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public QueryResponse(List<Lemma> lemmas, List<Term> terms, List<NamedEntity> namedEntity, String newQuery) {
        super();
        this.lemmas = lemmas;
        this.terms = terms;
        this.namedEntity = namedEntity;
        this.newQuery = newQuery;
    }

    public String getNewQuery() {
        return newQuery;
    }
}
