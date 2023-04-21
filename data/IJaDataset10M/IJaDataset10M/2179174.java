package de.sonivis.tool.textmining.representation.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.datamodel.Actor;
import de.sonivis.tool.core.datamodel.ActorContentElementRelation;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.ContextRelation;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.dao.IActorContentElementRelationDAO;
import de.sonivis.tool.core.datamodel.dao.IContextRelationDAO;
import de.sonivis.tool.core.datamodel.dao.IGenericDAO;
import de.sonivis.tool.core.datamodel.dao.IInfoSpaceItemDAO;
import de.sonivis.tool.core.datamodel.dao.hibernate.AbstractGenericDAO;
import de.sonivis.tool.core.datamodel.dao.hibernate.ActorContentElementRelationDAO;
import de.sonivis.tool.core.datamodel.dao.hibernate.ContextRelationDAO;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;
import de.sonivis.tool.core.datamodel.extension.Created;
import de.sonivis.tool.core.datamodel.extension.proxy.IGroupingElement;
import de.sonivis.tool.core.datamodel.networkloader.NetworkFilter;
import de.sonivis.tool.core.eventhandling.INetworkFilter;
import de.sonivis.tool.mwapiconnector.datamodel.extension.RevisionOfPage;
import de.sonivis.tool.textmining.core.eventlists.EventListFilter;
import de.sonivis.tool.textmining.datamodel.extension.TermPartOfRevision;

/**
 * This class initializes a terms list by creating an event list of
 * {@link TermAttributes}s. The list depends on the defined network filter.
 * 
 * @author Janette Lehmann
 * @version $Revision: 1488 $, $Date: 2009-08-26 21:51:50 +0000 (Mi, 26 Aug
 *          2009) $
 */
public final class ContentTermAttributesListLoading {

    /**
	 * Logger at {@value} .
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentTermAttributesListLoading.class);

    /**
	 * Get current information space
	 */
    private InfoSpace infoSpace = null;

    /**
	 * Current network filter.
	 */
    private static INetworkFilter networkFilterOfList = null;

    /**
	 * Event list of type {@link TermAttributes} including all data of the
	 * selected information space.
	 */
    private static final EventList<TermAttributes> CONTENT_TERM_LIST_ALL = new BasicEventList<TermAttributes>();

    /**
	 * Event list of type {@link TermAttributes} including the data depending on
	 * the selected filter.
	 */
    private static final FilterList<TermAttributes> CONTENT_TERM_LIST_FILTERED = new FilterList<TermAttributes>(CONTENT_TERM_LIST_ALL);

    /**
	 * Instance of this class.
	 */
    private static final ContentTermAttributesListLoading INSTANCE = new ContentTermAttributesListLoading();

    /**
	 * Default Constructor.
	 */
    public ContentTermAttributesListLoading() {
    }

    /**
	 * Returns an instance of this class.
	 * 
	 * @return {@link ContentTermAttributesListLoading} instance
	 */
    public static synchronized ContentTermAttributesListLoading getInstance() {
        return INSTANCE;
    }

    /**
	 * Depend on the network filter an array list of terms and additional data
	 * will be returned.
	 * 
	 * @return Array list of term attributes.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    public EventList<TermAttributes> getContentAnalysisData() throws CannotConnectToDatabaseException {
        if (infoSpace == null || infoSpace != ModelManager.getInstance().getCurrentInfoSpace()) {
            loadContentAnalysisData();
        }
        boolean reloadList = false;
        final INetworkFilter networkFilterNew = ModelManager.getInstance().getCurrentFilter();
        if (networkFilterNew == null) {
            networkFilterOfList = new NetworkFilter(null, new ArrayList<IGroupingElement>(), new ArrayList<IGroupingElement>(), new Date(0), new Date(), 20, 5);
            reloadList = true;
        } else if (networkFilterOfList == null || networkFilterOfList.equals(networkFilterNew)) {
            networkFilterOfList = networkFilterNew;
            reloadList = true;
        }
        if (reloadList) {
            setFilter();
        }
        return CONTENT_TERM_LIST_FILTERED;
    }

    /**
	 * Loads the content analysis data of the selected information space.
	 * 
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    private void loadContentAnalysisData() throws CannotConnectToDatabaseException {
        infoSpace = ModelManager.getInstance().getCurrentInfoSpace();
        CONTENT_TERM_LIST_ALL.clear();
        if (!ModelManager.getInstance().testCurrentDbConnection()) {
            LOGGER.error("No database connection is given...");
            CONTENT_TERM_LIST_ALL.clear();
            return;
        }
        LOGGER.info("Load content analysis data...");
        final List<ContextRelation> iaListTerms = loadTerms();
        final HashMap<Long, String[]> hmActors = loadActors();
        final HashMap<Long, String[]> hmPages = loadPages();
        LOGGER.info("Create list of content analysis data...");
        for (final Iterator<ContextRelation> i = iaListTerms.iterator(); i.hasNext(); ) {
            final ContextRelation ctTermRev = i.next();
        }
        LOGGER.info("List (" + CONTENT_TERM_LIST_ALL.size() + " elements) of content analysis data created.");
    }

    /**
	 * Loads context entities representing term revision relations.
	 * 
	 * The {@link List} returned will have target {@link ContextRelation}s fully
	 * initialized but not the source {@link ContextRelation}s.
	 * 
	 * @return A {@link List} of {@link ContextRelation} entities of type
	 *         {@link TextMiningInfoSpaceItemTypeConstants#EXT_TERM_PART_OF_REVISION}
	 *         having their target {@link ContextRelation}s initialized but not
	 *         their source {@link ContextRelation}s.
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    @SuppressWarnings("unchecked")
    private List<ContextRelation> loadTerms() throws CannotConnectToDatabaseException {
        LOGGER.info("Load a list of terms...");
        List<ContextRelation> iaList = null;
        final Session session = ModelManager.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            final Criteria crit = session.createCriteria(ContextRelation.class);
            crit.add(Restrictions.eq(AbstractGenericDAO.INFOSPACE_FIELD, infoSpace));
            crit.add(Restrictions.eq(AbstractGenericDAO.TYPE_FIELD, TermPartOfRevision.class));
            crit.setFetchMode(ContextRelationDAO.SOURCE_FIELD, FetchMode.JOIN);
            crit.setFetchMode(ContextRelationDAO.TARGET_FIELD, FetchMode.JOIN);
            crit.setFetchMode(IInfoSpaceItemDAO.PROPERTIES_FIELD, FetchMode.JOIN);
            iaList = crit.list();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            LOGGER.error("Exception occurred during query for terms. Transaction was rolled back.", he);
        } finally {
            session.close();
        }
        LOGGER.info("..." + iaList.size() + " terms loaded");
        return iaList;
    }

    /**
	 * Creates a hash map including the actor name and id of each revision.
	 * 
	 * The {@link List} returned will have {@link Actor}s fully initialized but
	 * not the {@link ContentElement}s.
	 * 
	 * @return A {@link HashMap} of names and IDs of {@link Actor}s
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    @SuppressWarnings("unchecked")
    private HashMap<Long, String[]> loadActors() throws CannotConnectToDatabaseException {
        LOGGER.info("Load a list of actors...");
        final HashMap<Long, String[]> hmActors = new HashMap<Long, String[]>();
        List<ActorContentElementRelation> iaList = null;
        final Session session = ModelManager.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            final Criteria crit = session.createCriteria(Created.class);
            crit.add(Restrictions.eq(AbstractGenericDAO.INFOSPACE_FIELD, infoSpace));
            crit.createCriteria(ActorContentElementRelationDAO.ACTOR_FIELD, CriteriaSpecification.LEFT_JOIN);
            crit.setFetchMode(IActorContentElementRelationDAO.CONTENTELEMENT_FIELD, FetchMode.JOIN);
            iaList = crit.list();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            LOGGER.error("Exception occurred during query for all knowledges. Transaction was rolled back.", he);
        } finally {
            session.close();
        }
        for (final Iterator<ActorContentElementRelation> i = iaList.iterator(); i.hasNext(); ) {
            final ActorContentElementRelation actorContentElementRelation = i.next();
            hmActors.put(actorContentElementRelation.getContentElement().getSerialId(), new String[] { actorContentElementRelation.getActor().getName(), actorContentElementRelation.getActor().getSerialId().toString() });
        }
        LOGGER.info("..." + hmActors.size() + " actors loaded");
        return hmActors;
    }

    /**
	 * Creates a hash map including the page name and id of each revision.
	 * 
	 * The {@link List} returned will have target {@link ContextRelation}s fully
	 * initialized but not the source {@link ContextRelation}s.
	 * 
	 * @return A {@link HashMap} of names and IDs of {@link ContentElement}s
	 * @throws CannotConnectToDatabaseException
	 *             if persistence store is not available.
	 */
    @SuppressWarnings("unchecked")
    private HashMap<Long, String[]> loadPages() throws CannotConnectToDatabaseException {
        LOGGER.info("Load a list of pages...");
        final HashMap<Long, String[]> hmPages = new HashMap<Long, String[]>();
        List<ContextRelation> iaList = null;
        final Session session = ModelManager.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            final Criteria crit = session.createCriteria(RevisionOfPage.class);
            crit.add(Restrictions.eq(IGenericDAO.INFOSPACE_FIELD, infoSpace));
            crit.setFetchMode(IContextRelationDAO.SOURCE_FIELD, FetchMode.JOIN);
            crit.setFetchMode(IContextRelationDAO.TARGET_FIELD, FetchMode.JOIN);
            iaList = crit.list();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            LOGGER.error("Exception occurred during query for terms. Transaction was rolled back.", he);
        } finally {
            session.close();
        }
        for (final Iterator<ContextRelation> i = iaList.iterator(); i.hasNext(); ) {
            final ContextRelation context = i.next();
            hmPages.put(context.getSource().getSerialId(), new String[] { context.getTarget().getTitle(), context.getTarget().getSerialId().toString() });
        }
        LOGGER.info("..." + hmPages.size() + " pages loaded");
        return hmPages;
    }

    /**
	 * Set the new filter definitions, saved in
	 * {@link ContentTermAttributesListLoading#networkFilterOfList}.
	 */
    private void setFilter() {
        final CompositeMatcherEditor<TermAttributes> cmeAllFilter = new CompositeMatcherEditor<TermAttributes>();
        cmeAllFilter.setMode(CompositeMatcherEditor.AND);
        final CompositeMatcherEditor<TermAttributes> cmePage = new CompositeMatcherEditor<TermAttributes>();
        cmePage.setMode(CompositeMatcherEditor.OR);
        final EventListFilter<ETermAttributes> trfPage = new EventListFilter<ETermAttributes>(ETermAttributes.PAGE_ID);
        for (final Iterator<IGroupingElement> i = networkFilterOfList.getPages().iterator(); i.hasNext(); ) {
            final String pageID = String.valueOf(i.next().getSerialId());
            final TextMatcherEditor<TermAttributes> tmeFilter = new TextMatcherEditor<TermAttributes>(trfPage);
            tmeFilter.setMode(TextMatcherEditor.EXACT);
            tmeFilter.setFilterText(new String[] { pageID });
            cmePage.getMatcherEditors().add(tmeFilter);
        }
        cmeAllFilter.getMatcherEditors().add(cmePage);
        CONTENT_TERM_LIST_FILTERED.setMatcherEditor(cmeAllFilter);
    }
}
