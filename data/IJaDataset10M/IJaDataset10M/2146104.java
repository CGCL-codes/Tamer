package com.vmware.spring.workshop.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.vmware.spring.workshop.dao.finder.IdentifiedInstanceFinder;
import com.vmware.spring.workshop.dao.finder.IdentifiedValuesListFinder;
import com.vmware.spring.workshop.dao.finder.InstanceFinder;
import com.vmware.spring.workshop.model.Identified;
import com.vmware.spring.workshop.model.Located;

/**
 * @author lgoldstein
 */
@TransactionConfiguration
public abstract class AbstractDaoTestSupport extends AbstractTransactionalJUnit4SpringContextTests {

    public static final String DEFAULT_TEST_CONTEXT = "classpath:META-INF/daoContext.xml";

    protected AbstractDaoTestSupport() {
        super();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T extends Identified, DAO extends IdentifiedCommonOperationsDao<T>> List<T> runIdentifiedByIdFinder(final DAO dao) {
        return runIdentifiedInstanceFinderTest(dao, (IdentifiedInstanceFinder) BY_ID_FINDER);
    }

    protected <T extends Identified, DAO extends IdentifiedCommonOperationsDao<T>> List<T> runIdentifiedInstanceFinderTest(final DAO dao, final IdentifiedInstanceFinder<T, DAO> finder) {
        return runInstanceFinderTest(dao, finder);
    }

    protected <ID extends Serializable, T, DAO extends CommonOperationsDao<T, ID>> List<T> runInstanceFinderTest(final DAO dao, final InstanceFinder<ID, T, DAO> finder) {
        final Iterable<T> valuesList = dao.findAll();
        Assert.assertNotNull("No current instances", valuesList);
        final List<T> result = new ArrayList<T>();
        for (final T srcValue : valuesList) {
            final T foundValue = finder.findInstance(dao, srcValue);
            Assert.assertNotNull("No match for " + srcValue, foundValue);
            Assert.assertEquals("Mismatched instances", srcValue, foundValue);
            result.add(srcValue);
        }
        Assert.assertFalse("No results extracted", CollectionUtils.isEmpty(result));
        return result;
    }

    protected <T extends Identified & Located, DAO extends IdentifiedCommonOperationsDao<T>> void runLocatedInstanceFinderTest(final DAO dao, final IdentifiedValuesListFinder<T, DAO, String> finder) {
        final Iterable<? extends T> valsList = dao.findAll();
        Assert.assertNotNull("No current locatable values", valsList);
        for (final T value : valsList) {
            final String location = value.getLocation();
            Assert.assertFalse("No location for " + value, StringUtils.isBlank(location));
            final int len = location.length(), subLen = Math.min(Long.SIZE, len);
            final String qryLocation = location.substring(location.length() - subLen).toUpperCase();
            final Collection<? extends T> qryList = finder.findMatches(dao, qryLocation);
            Assert.assertFalse("No matches found for " + qryLocation, CollectionUtils.isEmpty(qryList));
            final Collection<T> clcList = new ArrayList<T>();
            for (final T entity : valsList) {
                final String entityLocation = entity.getLocation();
                if (StringUtils.containsIgnoreCase(entityLocation, qryLocation)) clcList.add(entity);
            }
            Assert.assertEquals("Mismatched sizes for " + value, clcList.size(), qryList.size());
            Assert.assertTrue("Missing query results for " + value, qryList.containsAll(clcList));
        }
    }

    protected static final IdentifiedInstanceFinder<Identified, IdentifiedCommonOperationsDao<Identified>> BY_ID_FINDER = new IdentifiedInstanceFinder<Identified, IdentifiedCommonOperationsDao<Identified>>() {

        @Override
        public Identified findInstance(IdentifiedCommonOperationsDao<Identified> dao, Identified sourceInstance) {
            if (sourceInstance == null) return null; else return dao.findOne(sourceInstance.getId());
        }
    };
}
