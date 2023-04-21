package de.sonivis.tool.core.tests.datamodel.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.ModelManager;
import de.sonivis.tool.core.datamodel.InfoSpaceItem;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.dao.hibernate.InfoSpaceItemPropertyDAO;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;
import de.sonivis.tool.core.datamodel.extension.Human;
import de.sonivis.tool.core.tests.AbstractEmptyDatabaseTestCase;

/**
 * Test class for the {@link InfoSpaceItemPropertyDAO} class.
 * 
 * @author Andreas Erber
 * @version $Revision: 1558 $, $Date: 2010-03-07 13:32:37 -0500 (Sun, 07 Mar 2010) $
 */
public class TestInfoSpaceItemPropertyDAO extends AbstractEmptyDatabaseTestCase {

    /**
	 * Class logging.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestInfoSpaceItemPropertyDAO.class);

    /**
	 * An {@link InfoSpaceItem} the properties are associated with.
	 */
    private InfoSpaceItem actor;

    private final String nameProp1 = "Country";

    private final String nameProp2 = "Weight";

    private final String nameProp3 = "HashTest";

    private final String valProp1 = "USA";

    private final Double valProp2 = 80.5D;

    private final HashSet<String> valProp3 = new HashSet<String>();

    private final String valProp3S1 = "HashTest1";

    private final String valProp3S2 = "HashTest2";

    private InfoSpaceItemProperty<String> prop1 = null;

    private InfoSpaceItemProperty<Double> prop2 = null;

    private InfoSpaceItemProperty<HashSet<String>> prop3 = null;

    /**
	 * Representative of the class under test.
	 */
    private InfoSpaceItemPropertyDAO isipDAO;

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractEmptyDatabaseTestCase#setUp()
	 */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        this.isipDAO = new InfoSpaceItemPropertyDAO();
        this.actor = new Human(this.infoSpace, "Douglas Adams", null);
        valProp3.add(valProp3S1);
        valProp3.add(valProp3S2);
        this.prop1 = new InfoSpaceItemProperty<String>(this.nameProp1, this.valProp1);
        this.prop2 = new InfoSpaceItemProperty<Double>(this.nameProp2, this.valProp2);
        this.prop3 = new InfoSpaceItemProperty<HashSet<String>>(this.nameProp3, this.valProp3);
        this.actor.addProperty(this.prop1);
        this.actor.addProperty(this.prop2);
        this.actor.addProperty(this.prop3);
        Session s = null;
        Transaction tx = null;
        try {
            s = ModelManager.getInstance().getCurrentSession();
            tx = s.beginTransaction();
            s.save(this.actor);
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to save Actor entity - transaction was rolled back.", he);
            }
            he.printStackTrace();
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
    protected final void tearDown() {
        if (this.infoSpace.getName().equals(AbstractEmptyDatabaseTestCase.TEST_INFOSPACE_NAME)) {
            Session s = null;
            Transaction tx = null;
            try {
                s = ModelManager.getInstance().getCurrentSession();
                tx = s.beginTransaction();
                s.update(this.actor);
                s.delete(this.actor);
                s.flush();
                tx.commit();
            } catch (final HibernateException he) {
                tx.rollback();
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Exception in tearDown() - transaction was rolled back!", he);
                }
                he.printStackTrace();
                throw he;
            } catch (final CannotConnectToDatabaseException e) {
                fail("Persistence store is not available.");
            } finally {
                s.close();
            }
        }
        this.actor = null;
        this.isipDAO = null;
        super.tearDown();
    }

    /**
	 * Test method for {@link InfoSpaceItemPropertyDAO#findByInfoSpaceItem(InfoSpaceItem)}.
	 */
    @SuppressWarnings("unchecked")
    public final void testFindByInfoSpaceItem() {
        Session s = null;
        Transaction tx = null;
        List<InfoSpaceItemProperty<?>> resultSQL = null;
        try {
            s = ModelManager.getInstance().getCurrentSession();
            tx = s.beginTransaction();
            resultSQL = s.createSQLQuery("SELECT * FROM infospaceitemproperty WHERE infospaceitem_id = " + this.actor.getSerialId()).addEntity(InfoSpaceItemProperty.class).list();
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception when querying for findByInfoSpaceItem - transaction was rolled back!", he);
            }
            he.printStackTrace();
            throw he;
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        } finally {
            s.close();
        }
        assertNotNull(resultSQL);
        assertFalse(resultSQL.isEmpty());
        List<InfoSpaceItemProperty<?>> result = null;
        try {
            result = this.isipDAO.findByInfoSpaceItem(this.actor);
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        }
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(this.compareResultSets(resultSQL, result));
    }

    @SuppressWarnings("unchecked")
    public final void testComplexProperty() {
        List<InfoSpaceItemProperty<?>> result = null;
        try {
            result = this.isipDAO.findByInfoSpaceItem(this.actor);
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        }
        assertTrue(result.contains(prop3));
        for (final InfoSpaceItemProperty<?> prop : result) {
            if (prop.getName().equals(nameProp3)) {
                assertTrue(prop.getValue() instanceof HashSet<?>);
                assertTrue(((HashSet<String>) prop.getValue()).contains(valProp3S1));
                assertTrue(((HashSet<String>) prop.getValue()).contains(valProp3S2));
            }
        }
    }

    /**
	 * Compare two result sets ({@link List}s actually) of {@link InfoSpaceItemProperty} entities.
	 * <p>
	 * The result sets are compared based on the serialization IDs of the contained
	 * {@link InfoSpaceItemProperty} entities. This allowes to compare lists of lazy initialized
	 * {@link InfoSpaceItemProperty}s simply in the basis of the surrogate primary database keys.
	 * </p>
	 * 
	 * @param set1
	 *            {@link List} of {@link InfoSpaceItemProperty} proxies, partly or fully initialized
	 *            entities, or <code>null</code>.
	 * @param set2
	 *            {@link List} of {@link InfoSpaceItemProperty} proxies, partly or fully initialized
	 *            entities, or <code>null</code>.
	 * @return <code>true</code> if both {@link List}s contain the same number of items and each
	 *         serialization ID occurs in both, <code>false</code> otherwise
	 */
    private boolean compareResultSets(final List<InfoSpaceItemProperty<?>> set1, final List<InfoSpaceItemProperty<?>> set2) {
        if (set1 == null || set2 == null) {
            return false;
        }
        if (set1.size() != set2.size()) {
            return false;
        }
        final List<Long> idList1 = new ArrayList<Long>();
        for (final InfoSpaceItemProperty<?> isip : set1) {
            idList1.add(isip.getSerialId());
        }
        final List<Long> idList2 = new ArrayList<Long>();
        for (final InfoSpaceItemProperty<?> isip : set2) {
            idList2.add(isip.getSerialId());
        }
        for (final Long id : idList1) {
            if (!idList2.contains(id)) {
                return false;
            }
        }
        for (final InfoSpaceItemProperty<?> prop : set1) {
            if (!set2.contains(prop)) {
                return false;
            }
        }
        return true;
    }
}
