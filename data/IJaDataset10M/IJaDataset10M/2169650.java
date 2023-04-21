package org.helianto.core.repository;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import java.util.Collection;
import org.helianto.core.Identity;
import org.helianto.core.filter.classic.UserBackedFilter;
import org.helianto.core.repository.base.AbstractFilterDao;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Fernandes de Castro
 */
public class AbstractFilterDaoTests {

    /**
	 * Check the call to filter
	 * Check the call to persistenceStrategy
	 * Check the result
	 */
    @Test
    public void find() {
        Collection<Identity> resultList = new ArrayList<Identity>();
        expect(mockFilter.createCriteriaAsString()).andReturn("WHERECLAUSE");
        replay(mockFilter);
        expect(mockPersistenceStrategy.find("SELECTCLAUSE where WHERECLAUSE")).andReturn(resultList);
        replay(mockPersistenceStrategy);
        assertSame(resultList, stubDao.find(mockFilter));
        verify(mockPersistenceStrategy);
        verify(mockFilter);
    }

    AbstractFilterDao<Identity> stubDao;

    UserBackedFilter mockFilter;

    StringBuilder selectBuilder = new StringBuilder("SELECTCLAUSE ");

    PersistenceStrategy<Identity> mockPersistenceStrategy;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setUp() {
        mockFilter = createMock(UserBackedFilter.class);
        mockPersistenceStrategy = createMock(PersistenceStrategy.class);
        stubDao = new AbstractFilterDao(Identity.class) {

            @Override
            protected PersistenceStrategy getPersistenceStrategy() {
                return mockPersistenceStrategy;
            }

            @Override
            protected StringBuilder getSelectBuilder() {
                return selectBuilder;
            }
        };
    }

    public void tearDown() {
        reset(mockPersistenceStrategy);
        reset(mockFilter);
    }
}
