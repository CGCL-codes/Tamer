package org.s3b.search.query.expansion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.corrib.jonto.taxonomy.Taxonomies;
import org.corrib.jonto.taxonomy.TaxonomyContext;
import org.corrib.jonto.taxonomy.TaxonomyEntry;
import org.corrib.jonto.taxonomy.TaxonomyStorage;
import org.s3b.search.config.Configuration;
import org.s3b.search.history.ExpandedHistoryInfo;
import org.s3b.search.query.QueryObject;
import org.s3b.search.query.QueryParameter;
import org.s3b.search.query.QueryParameterEntry;
import org.s3b.search.query.QueryParameterType;
import org.s3b.search.query.ResultObject;
import org.s3b.search.query.impl.QueryParameterEntryImpl;
import org.s3b.search.query.impl.QueryParameterImpl;
import org.s3b.search.resource.MidResource;

/**
 * Implementation of <code>QueryExpansion</code> interface for the Taxonomy based expansion
 * of query parameters. It processes given query creating new parameter entries based on taxonomies going through taxonomy tree up or down
 * Use <code>setDirection(boolean direction)</code> to set the direction with:
 * TaxonomyQueryExpansion.UP or TaxonomyQueryExpansion.DOWN 
 * @author  Lukasz Porwol
 */
public class TaxonomyQueryExpansion implements QueryExpansion {

    @SuppressWarnings("unused")
    public static final String UP = "up";

    /**
	 * use to set the direction of expansion to down the hierarchy of taxonomy - that is more precise
	 * 
	 */
    public static final String DOWN = "down";

    /**
	 * the direction in with to expand query
	 * one of: TaxonomyQueryExpansion.UP, TaxnonomyQueryExpansion.DOWN
	 */
    private String direction;

    /**
	 * <code>QueryObject</code> to expand
	 */
    private QueryObject queryObject;

    private List<QueryParameterEntry> queryList;

    /**
	 * <code>ResultObject</code> associated with the query
	 */
    @SuppressWarnings("unchecked")
    private ResultObject resultObject;

    public MidResource m2resource = null;

    /**
	 * @param _queryObject <code>QueryObject</code> to expand/process
	 * @param _resultObject <code>ResultObject</code> associated with the query
	 */
    @SuppressWarnings("unchecked")
    public TaxonomyQueryExpansion(QueryObject _queryObject, ResultObject _resultObject, List<QueryParameterEntry> _queryList, MidResource mresource) {
        this.queryObject = _queryObject;
        this.resultObject = _resultObject;
        this.queryList = _queryList;
        this.m2resource = mresource;
    }

    public QueryObject processQuery(boolean expand, boolean precise) {
        QueryObject result = queryObject;
        for (QueryParameter qp : result.getParameters()) {
            if (!qp.getType().isPersonType()) {
                QueryParameter newqp = processParameter(qp, expand, precise);
                if (newqp != null) {
                    if (newqp.getType().equals(qp.getType())) {
                        intersectValues(qp, newqp);
                    } else result.addParameter(newqp, false);
                }
            }
        }
        result.reloadParameters();
        return result;
    }

    /**
	 * Intersects QueryParameterEntries of two given QueryParameters of the taxonomy type. <b>NOTE:</b>
	 * The resulting QueryParameterEntries are returned in the <code>qp</code> argument as its properties
	 * @param qp <tt>QueryParameter</tt> in which intersected results should be returned
	 * @param otherQp <tt>QueryParameter</tt> whose entries to intersect with <code>qp's</code> entries
	 */
    private void intersectValues(QueryParameter qp, QueryParameter otherQp) {
        List<QueryParameterEntry> newqpes = new ArrayList<QueryParameterEntry>();
        for (QueryParameterEntry newqpe : otherQp.getValues()) {
            if (!containsTaxonomy(qp, newqpe.getTaxonomy())) newqpes.add(newqpe);
        }
        qp.addValues(newqpes);
    }

    /**
	 * Check whether given <tt>QueryParameter</tt> contains <tt>QueryParameterEntry</tt> that has a Taxonomy property set
	 * to <code>taxonomy</code> argument
	 * @param qp QueryParameter to search in
	 * @param taxonomy TaxonomyEntry to match
	 * @return <tt>true</tt> if the given <code>taxonomy</code> is in QueryParameterEntries of the <code>qp</code> argument
	 * otherwise it returns <tt>false</tt>
	 */
    private boolean containsTaxonomy(QueryParameter qp, TaxonomyEntry taxonomy) {
        assert (taxonomy != null);
        try {
            for (QueryParameterEntry qpe : qp.getValues()) {
                if (qpe.getTaxonomy() != null) {
                    if (qpe.getTaxonomy().getContext() != taxonomy.getContext()) {
                        return false;
                    }
                    if (qpe.getTaxonomy().equals(taxonomy)) return true;
                }
            }
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

    /**
	 * Process single parameter of the query. Creates new entries for this parameter
	 * 
	 * @param qp
	 *            QueryParameter to process
	 * @param expand
	 *            <tt>true</true> if the given query should be expanded using wordnet, <tt>false</tt> if just 
	 * a lookup whether query contains string matching author
	 * @return 
	 * @return New <tt>QueryParameter</code> of taxonomy type (QPT_TAXONOMY_QP) if the query matches entries in taxonomies 
	 */
    private QueryParameter processParameter(QueryParameter qp, boolean expand, boolean precise) {
        List<QueryParameterEntry> newqpes = new ArrayList<QueryParameterEntry>();
        boolean found = false;
        QueryParameter newqp = new QueryParameterImpl();
        newqp.setType(QueryParameterType.TAXONOMY);
        List<QueryParameterEntry> valuesToRemove = new ArrayList<QueryParameterEntry>();
        if (expand) {
            if (!m2resource.status.contains("<br />Expanding classes using Control Vocabulary...")) m2resource.status.add("<br />Expanding classes using Control Vocabulary...");
            if (qp.getType().isTaxonomyType()) {
                for (QueryParameterEntry qpe : qp.getValues()) {
                    newqpes.addAll(processEntry(qpe, precise));
                    valuesToRemove.add(qpe);
                }
            }
        } else {
            if (!m2resource.status.contains("<br />Matching classes using Control Vocabulary...")) m2resource.status.add("<br />Matching classes using Control Vocabulary...");
            for (QueryParameterEntry qpe : qp.getValues()) {
                List<QueryParameterEntry> tempQpe = matchTaxonomy(qpe);
                for (QueryParameterEntry qpe2 : tempQpe) {
                    if (newqpes.size() != 0) {
                        found = false;
                        for (QueryParameterEntry qpe3 : newqpes) {
                            if (qpe2.getTaxonomy().getLabel() == qpe3.getTaxonomy().getLabel()) found = true;
                        }
                        if (found != true) {
                            newqpes.add(qpe2);
                        }
                    } else {
                        newqpes.add(qpe2);
                    }
                }
            }
            for (QueryParameterEntry qpr : valuesToRemove) {
                qp.removeValue(qpr);
                newqpes.remove(qpr);
            }
        }
        newqp.clearValues();
        newqp.addValues(newqpes);
        if (newqp.getValues().size() > 0) {
            ExpandedHistoryInfo ehi = new ExpandedHistoryInfo(qp.getValues(), newqpes, newqp.getType());
            resultObject.addHistoryInfo(ehi);
            return newqp;
        } else return null;
    }

    /**
	 * Finds taxonomy entries matching query string if there are any
	 * 
	 * @param qpe
	 *            <tt>QueryParameterEntry</tt> with string to lookup
	 * @return List of <tt>QueryParameterEntries</tt> containing found taxonomy entries
	 */
    private List<QueryParameterEntry> matchTaxonomy(QueryParameterEntry qpe) {
        List<QueryParameterEntry> result = new ArrayList<QueryParameterEntry>();
        String value = qpe.getStringValue();
        Double rank = qpe.getRanking();
        if (rank == Double.NaN || rank == null) rank = 0.0D;
        Set<TaxonomyEntry> taxEntries = new HashSet<TaxonomyEntry>();
        Collection<TaxonomyContext<TaxonomyEntry>> contexts = Taxonomies.getInstance().getContexts();
        for (TaxonomyContext<TaxonomyEntry> context : contexts) {
            Collection<TaxonomyEntry> new1 = context.listMatchingByLabelAndContext(value);
            taxEntries.addAll(new1);
        }
        for (TaxonomyEntry taxEntry : taxEntries) {
            QueryParameterEntry newqpe = new QueryParameterEntryImpl(value);
            newqpe.setTaxonomy(taxEntry);
            newqpe.setRanking((rank == 0) ? m2resource.taxBoost : rank * m2resource.taxBoost);
            String newInfo = qpe.TellMeAbout() + taxEntry.getLabel() + " has been retrieved as classification of " + value + " using Taxonomies within JONTO ";
            newqpe.AddInfo(newInfo);
            result.add(newqpe);
        }
        queryObject.modifyQueryAppend(queryList, result, qpe, false);
        return result;
    }

    /**
	 * Process single <tt>QueryParameterEntry</tt> expanding it with Taxonomy hierarchy according to
	 * <code>directory</code> property. <b>NOTE:</b> If qpe has null taxonomy set assertion failed will be thrown 
	 * @param qpe <tt>QueryParameterEntry</tt> to be expanded
	 * @return List of expanded <tt>QueryParameterEntries</tt> according to WordNet
	 */
    private List<QueryParameterEntry> processEntry(QueryParameterEntry qpe, boolean precise) {
        List<QueryParameterEntry> result = new ArrayList<QueryParameterEntry>();
        TaxonomyEntry taxEntry = qpe.getTaxonomy();
        assert (taxEntry != null);
        Double rank = qpe.getRanking();
        if (rank == Double.NaN || rank == null) {
            rank = 0.0D;
        }
        if ((getDirection() == UP) || (precise)) {
            TaxonomyEntry parent = TaxonomyStorage.getInstance().getParent(taxEntry);
            if (parent != null) {
                QueryParameterEntry newqpe = new QueryParameterEntryImpl(parent.getLabel());
                newqpe.setRanking((rank == 0) ? m2resource.taxBoost : m2resource.taxBoost * rank);
                newqpe.setTaxonomy(parent);
                String newInfo = qpe.TellMeAbout() + parent.getLabel() + " has been retrieved as parent of " + taxEntry.getLabel() + " using Taxonomies within JONTO ";
                newqpe.AddInfo(newInfo);
                result.add(newqpe);
            }
        } else if ((getDirection() == DOWN) || (!precise)) {
            try {
                List<TaxonomyEntry> taxChildren = TaxonomyStorage.getInstance().syncChildren(taxEntry);
                for (TaxonomyEntry taxChild : taxChildren) {
                    QueryParameterEntry newqpe = new QueryParameterEntryImpl(taxChild.getLabel());
                    newqpe.setRanking((rank == 0) ? m2resource.taxBoost : m2resource.taxBoost * rank);
                    newqpe.setTaxonomy(taxChild);
                    String newInfo = qpe.TellMeAbout() + taxChild.getLabel() + " has been retrieved as child of " + taxEntry.getLabel() + " using Taxonomies within JONTO ";
                    newqpe.AddInfo(newInfo);
                    result.add(newqpe);
                }
            } catch (NullPointerException e) {
            }
        }
        if (precise) {
            if ((Configuration.PrecisionTaxonomyQueryOverwrite)) queryObject.modifyQueryOverwrite(queryList, result, qpe, false);
            if ((Configuration.PrecisionTaxonomyQueryAppend)) queryObject.modifyQueryAppend(queryList, result, qpe, false);
        } else {
            if ((Configuration.RecallTaxonomyQueryOverwrite)) queryObject.modifyQueryOverwrite(queryList, result, qpe, false);
            if ((Configuration.RecallTaxonomyQueryAppend)) queryObject.modifyQueryAppend(queryList, result, qpe, false);
        }
        return result;
    }

    /**
	 * @return The direction in with the expansion will go 
	 * TaxonomyQueryExpansion.UP -> up the taxonomy hierarchy tree (generalization)
	 * TaxonomyQueryExpansion.DOWN -> down the taxonomy hierarchy tree (more precise)
	 */
    public String getDirection() {
        return direction;
    }

    /**
	 * @param direction The direction in with the expansion will go 
	 * TaxonomyQueryExpansion.UP -> up the taxonomy hierarchy tree (generalization)
	 * TaxonomyQueryExpansion.DOWN -> down the taxonomy hierarchy tree (more precise)
	 */
    public void setDirection(String _direction) {
        this.direction = _direction;
    }

    public QueryObject getQueryObject() {
        return queryObject;
    }

    /**
	 * @param queryObject the queryObject to set
	 */
    public void setQueryObject(QueryObject queryObject) {
        this.queryObject = queryObject;
    }

    @SuppressWarnings("unchecked")
    public ResultObject getResultObject() {
        return resultObject;
    }

    /**
	 * @param resultObject the result object to set
	 */
    @SuppressWarnings("unchecked")
    public void setResultObject(ResultObject _resultObject) {
        this.resultObject = _resultObject;
    }
}
