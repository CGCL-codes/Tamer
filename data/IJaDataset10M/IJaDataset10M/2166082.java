package org.vardb.search.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.*;
import java.math.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.vardb.CConstants;
import org.vardb.resources.*;
import org.vardb.resources.dao.CDisease;
import org.vardb.resources.dao.CFamily;
import org.vardb.resources.dao.CPathogen;
import org.vardb.search.CSearchResults;
import org.vardb.search.CFilter;
import org.vardb.sequences.ISequence;
import org.vardb.util.CStringHelper;

public class CSearchDaoImpl extends HibernateDaoSupport implements ISearchDao {

    @SuppressWarnings("unchecked")
    public List<CSuggestion> getSuggestions(String chars) {
        HibernateTemplate template = super.getHibernateTemplate();
        chars = chars.toLowerCase() + "%";
        StringBuilder buffer = new StringBuilder();
        buffer.append("select suggestion\n");
        buffer.append("from CSuggestion as suggestion\n");
        buffer.append("where suggestion.lowercase like :chars\n");
        String sql = buffer.toString();
        List<CSuggestion> suggestions = (List<CSuggestion>) template.findByNamedParam(sql, "chars", chars);
        return suggestions;
    }

    @SuppressWarnings("unchecked")
    public CSearchResults searchResources(final String search, final int start, final int limit, final String sort, final CConstants.SortDir dir) {
        HibernateTemplate template = super.getHibernateTemplate();
        try {
            Session session = template.getSessionFactory().getCurrentSession();
            FullTextSession fullTextSession = Search.createFullTextSession(session);
            String[] fields = new String[] { "identifier", "name", "description", "notes", "html", "antigenicvariation", "lifecycle", "distribution" };
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
            org.apache.lucene.search.Query luceneQuery = parser.parse(search);
            org.hibernate.search.FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, CPathogen.class, CFamily.class, CDisease.class);
            fullTextQuery.setFirstResult(start);
            fullTextQuery.setMaxResults(limit);
            if (sort != null) fullTextQuery.setSort(new Sort(new SortField(sort, dir.getReverse())));
            List<Object> resources = fullTextQuery.list();
            CSearchResults results = new CSearchResults(search);
            results.setStart(start);
            results.setPagesize(limit);
            results.addAll(resources);
            results.setTotal(fullTextQuery.getResultSize());
            return results;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public CSearchResults search(String search) {
        CSearchResults results = new CSearchResults(search);
        search = "%" + search + "%";
        List<Object> objects = new ArrayList<Object>();
        objects.addAll(searchDiseases(search));
        objects.addAll(searchPathogens(search));
        objects.addAll(searchFamilies(search));
        if (objects.size() >= results.getPagesize()) objects = objects.subList(0, results.getPagesize());
        results.getResults().addAll(objects);
        results.setTotal(results.getResults().size());
        return results;
    }

    @SuppressWarnings("unchecked")
    private List<CDisease> searchDiseases(String search) {
        HibernateTemplate template = super.getHibernateTemplate();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select disease\n");
        buffer.append("from CDisease as disease\n");
        buffer.append("where disease.name like :search\n");
        buffer.append("or disease.description like :search\n");
        buffer.append("or disease.distribution like :search\n");
        buffer.append("or disease.host like :search\n");
        buffer.append("or disease.pathogenesis like :search\n");
        buffer.append("or disease.symptoms like :search\n");
        buffer.append("or disease.diagnosis like :search\n");
        buffer.append("or disease.prevention like :search\n");
        buffer.append("or disease.treatment like :search\n");
        buffer.append("or disease.vaccines like :search\n");
        buffer.append("or disease.history like :search\n");
        buffer.append("or disease.notes like :search\n");
        String sql = buffer.toString();
        List<CDisease> diseases = (List<CDisease>) template.findByNamedParam(sql, "search", search);
        return diseases;
    }

    @SuppressWarnings("unchecked")
    private List<CPathogen> searchPathogens(String search) {
        HibernateTemplate template = super.getHibernateTemplate();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select pathogen\n");
        buffer.append("from CPathogen as pathogen\n");
        buffer.append("where pathogen.name like :search\n");
        buffer.append("or pathogen.description like :search\n");
        buffer.append("or pathogen.distribution like :search\n");
        buffer.append("or pathogen.lifecycle like :search\n");
        buffer.append("or pathogen.notes like :search\n");
        String sql = buffer.toString();
        List<CPathogen> pathogens = (List<CPathogen>) template.findByNamedParam(sql, "search", search);
        return pathogens;
    }

    @SuppressWarnings("unchecked")
    private List<CFamily> searchFamilies(String search) {
        HibernateTemplate template = super.getHibernateTemplate();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select family\n");
        buffer.append("from CFamily as family\n");
        buffer.append("where family.name like :search\n");
        buffer.append("or family.alias like :search\n");
        buffer.append("or family.description like :search\n");
        buffer.append("or family.mechanism like :search\n");
        buffer.append("or family.expression like :search\n");
        buffer.append("or family.introns like :search\n");
        buffer.append("or family.protein like :search\n");
        buffer.append("or family.location like :search\n");
        buffer.append("or family.function like :search\n");
        buffer.append("or family.ligands like :search\n");
        buffer.append("or family.notes like :search\n");
        String sql = buffer.toString();
        List<CFamily> familys = (List<CFamily>) template.findByNamedParam(sql, "search", search);
        return familys;
    }

    public void indexPathogens(List<CPathogen> pathogens) {
        HibernateTemplate template = super.getHibernateTemplate();
        Session session = template.getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        for (CPathogen obj : pathogens) {
            fullTextSession.index(obj);
        }
    }

    public void indexFamilies(List<CFamily> families) {
        HibernateTemplate template = super.getHibernateTemplate();
        Session session = template.getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        for (CFamily obj : families) {
            fullTextSession.index(obj);
        }
    }

    public void indexDiseases(List<CDisease> diseases) {
        HibernateTemplate template = super.getHibernateTemplate();
        Session session = template.getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        for (CDisease obj : diseases) {
            fullTextSession.index(obj);
        }
    }

    public void indexSequences(List<CIndexedSequence> sequences) {
        HibernateTemplate template = super.getHibernateTemplate();
        Session session = template.getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        for (CIndexedSequence obj : sequences) {
            fullTextSession.index(obj);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CIndexedSequence> getIndexedSequences() {
        HibernateTemplate template = super.getHibernateTemplate();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        buffer.append("from CIndexedSequence as sequence\n");
        String sql = buffer.toString();
        List<CIndexedSequence> sequences = (List<CIndexedSequence>) template.find(sql);
        return sequences;
    }

    public void addSearch(CSearch search) {
        HibernateTemplate template = super.getHibernateTemplate();
        template.save(search);
    }

    public void updateSearch(CSearch search) {
        HibernateTemplate template = super.getHibernateTemplate();
        template.update(search);
    }

    @SuppressWarnings("unchecked")
    public List<CSearch> getSearches(String username) {
        HibernateTemplate template = super.getHibernateTemplate();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select search\n");
        buffer.append("from CSearch as search\n");
        buffer.append("where search.username=:username\n");
        buffer.append("order by search.id\n");
        String sql = buffer.toString();
        List<CSearch> list = (List<CSearch>) template.findByNamedParam(sql, "username", username);
        return list;
    }

    public CSearch getSearch(String id) {
        HibernateTemplate template = super.getHibernateTemplate();
        CSearch search = (CSearch) template.get(CSearch.class, id);
        return search;
    }

    public void deleteSearch(String id) {
        HibernateTemplate template = super.getHibernateTemplate();
        CSearch search = (CSearch) template.get(CSearch.class, id);
        template.delete(search);
    }
}
