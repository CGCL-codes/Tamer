package edu.unibi.agbi.dawismd.database.searchqueries;

import java.util.ArrayList;

public class GeneOntology extends AbstractSearchQueries {

    public static final String[] gene_ontology_accession = { "select a.acc from go_term a where a.acc like 'GO:%' and a.acc like :input" };

    public static final String[] gene_ontology_name = { "select a.acc from go_term a where a.acc like 'GO:%' and a.name like :input" };

    public static final String[] gene_ontology_synonyms = { "select a.termSynonym from go_term_synonym a join a.termId b where b.acc like 'GO:%' and a.termSynonym like :input" };

    @SuppressWarnings("unchecked")
    public void setGeneOntologyAccession(String accession) {
        RESULTS = new ArrayList<Object[]>(SESSION.createQuery("select a.acc, a.name from go_term a where a.acc like :accession").setParameter("accession", accession + "%").list());
        SESSION.close();
    }

    @SuppressWarnings("unchecked")
    public void setGeneOntologyName(String name) {
        RESULTS = new ArrayList<Object[]>(SESSION.createQuery("select a.acc, a.name from go_term a where a.name like :name").setParameter("name", name + "%").list());
        SESSION.close();
    }

    @SuppressWarnings("unchecked")
    public void setGeneOntologySynonyms(String synonyms) {
        RESULTS = new ArrayList<Object[]>(SESSION.createQuery("select b.acc, b.name from go_term_synonym a join a.termId b where a.termSynonym like :synonyms").setParameter("synonyms", synonyms + "%").list());
        SESSION.close();
    }
}
