package de.sonivis.tool.mwapiconnector.datamodel.extension.tests;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.dao.hibernate.ContentElementDAO;
import de.sonivis.tool.core.datamodel.dao.hibernate.GroupingElementDAO;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;
import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.extension.proxy.IGroupingElement;
import de.sonivis.tool.core.datamodel.extension.proxy.IRevisionElement;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;
import de.sonivis.tool.core.datamodel.proxy.IInfoSpaceItem;
import de.sonivis.tool.core.tests.AbstractEmptyDatabaseTestCase;
import de.sonivis.tool.mwapiconnector.datamodel.extension.Category;
import de.sonivis.tool.mwapiconnector.datamodel.extension.CategoryLink;
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.IArticle;
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.ICategory;

/**
 * Test for the {@link CategoryLink} class, an extension of the SONIVIS:Data Model.
 * 
 * @author Andreas Erber
 * @version $Revision: 1417 $, $Date: 2010-01-28 14:24:56 +0000 (Do, 28 Jan 2010) $
 */
public class CategorizedByTest extends AbstractEmptyDatabaseTestCase {

    /**
	 * Class logging.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategorizedByTest.class);

    /**
	 * A {@link RevisionElement} as required for constructing a {@link CategoryLink} instance.
	 */
    private RevisionElement rev = null;

    /**
	 * A {@link Category} as required for constructing a {@link CategoryLink} instance.
	 */
    private Category cat = null;

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractEmptyDatabaseTestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.rev = new RevisionElement(this.infoSpace, 1L, "Title", "Some test text", Calendar.getInstance().getTime());
        this.cat = new Category(this.infoSpace, 2L, "Category title");
        Session s = null;
        Transaction tx = null;
        try {
            s = ModelManager.getInstance().getCurrentSession();
            tx = s.beginTransaction();
            s.save(this.rev);
            s.save(this.cat);
            s.flush();
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            he.printStackTrace();
            fail("Hibernate problem occurred.");
            throw he;
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        } finally {
            s.close();
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractEmptyDatabaseTestCase#tearDown()
	 */
    @Override
    protected void tearDown() {
        if (this.infoSpace.getName().equals(TEST_INFOSPACE_NAME)) {
            Session s = null;
            Transaction tx = null;
            try {
                s = ModelManager.getInstance().getCurrentSession();
                tx = s.beginTransaction();
                s.update(this.cat);
                s.update(this.rev);
                s.delete(this.cat);
                s.delete(this.rev);
                s.flush();
                tx.commit();
            } catch (final HibernateException he) {
                tx.rollback();
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Exception occurred when trying to delete test entities - transactionwas rolled back. InfoSpace and possibly several contained entities were not deleted. Must be deleted by hand.");
                }
                he.printStackTrace();
                throw he;
            } catch (final CannotConnectToDatabaseException e) {
                fail("Persistence store is not available.");
            } finally {
                s.close();
            }
        }
        this.cat = null;
        this.rev = null;
        super.tearDown();
    }

    /**
	 * Test method for {@link CategoryLink#CategorizedBy(InfoSpace, IRevisionElement, ICategory)} .
	 */
    public final void testCategorizedByInfoSpaceREVCAT() {
        CategoryLink<IRevisionElement, ICategory> catBy = null;
        try {
            catBy = new CategoryLink<IRevisionElement, ICategory>(this.infoSpace, this.rev, this.cat);
        } catch (final Exception e) {
            fail("Exception during construction of CategoryLink from minimal constructor: " + e.getClass().getName());
        }
        assertNotNull(catBy);
        assertTrue(catBy.getSource() instanceof IRevisionElement);
        assertTrue(catBy.getTarget() instanceof ICategory);
        assertEquals(catBy.getType(), CategoryLink.class);
    }

    /**
	 * Test method for {@link CategoryLink#CategorizedBy(InfoSpace, IRevisionElement, ICategory)} .
	 */
    public final void testCategorizedByInfoSpaceREVCATfromDB() {
        final ContentElementDAO revDAO = new ContentElementDAO();
        EventList<IContentElement> revisions = null;
        try {
            revisions = revDAO.findByType(this.infoSpace, RevisionElement.class);
        } catch (final CannotConnectToDatabaseException e1) {
            fail("Persistence store is not available");
        }
        assertNotNull(revisions);
        assertFalse(revisions.isEmpty());
        assertTrue(revisions.size() == 1);
        final GroupingElementDAO catDAO = new GroupingElementDAO();
        EventList<IGroupingElement> categories = null;
        try {
            categories = catDAO.findByType(this.infoSpace, ICategory.class);
        } catch (final CannotConnectToDatabaseException e1) {
            fail("Persistence store is not available");
        }
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertTrue(categories.size() == 1);
        assertTrue(categories.get(0) instanceof ICategory);
        assertTrue(categories.get(0) instanceof IGroupingElement);
        assertTrue(categories.get(0) instanceof IContentElement);
        assertTrue(categories.get(0) instanceof IInfoSpaceItem);
        assertFalse(categories.get(0) instanceof IArticle);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Type of category: " + categories.get(0).getClass().getCanonicalName());
        }
        CategoryLink<IRevisionElement, ICategory> catBy = null;
        try {
            catBy = new CategoryLink<IRevisionElement, ICategory>(this.infoSpace, (IRevisionElement) revisions.get(0), (Category) categories.get(0));
        } catch (final Exception e) {
            fail("Exception during construction of CategoryLink from minimal constructor with instances form DB: " + e.getClass().getName());
        }
        assertNotNull(catBy);
        assertTrue(catBy.getSource() instanceof IRevisionElement);
        assertTrue(catBy.getTarget() instanceof ICategory);
        assertEquals(catBy.getType(), CategoryLink.class);
    }

    /**
	 * Test method for
	 * {@link CategoryLink#CategorizedBy(InfoSpace, Collection, Set, IRevisionElement, ICategory)} .
	 */
    public final void testCategorizedByInfoSpaceCollectionOfInfoSpaceItemPropertyOfQSetOfGraphComponentREVCAT() {
        CategoryLink<IRevisionElement, ICategory> catBy = null;
        try {
            catBy = new CategoryLink<IRevisionElement, ICategory>(this.infoSpace, null, null, this.rev, this.cat);
        } catch (final Exception e) {
            fail("Exception during construction of CategoryLink from full constructor: " + e.getClass().getName());
        }
        assertNotNull(catBy);
        assertTrue(catBy.getSource() instanceof IRevisionElement);
        assertTrue(catBy.getTarget() instanceof ICategory);
        assertEquals(catBy.getType(), CategoryLink.class);
    }
}
