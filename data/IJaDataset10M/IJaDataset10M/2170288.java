package com.idna.dm.dao.sp;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dm-dataAccess.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
@Ignore
public class SearchDecisionDaoIntegrationTest {

    @Autowired
    private SearchDecisionDao searchDecisionDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUpdateSearchDecision() {
        String searchId = "677D9499-97B2-456C-850C-000077876C88";
        Integer outcomeId = 1;
        searchDecisionDao.updateSearchDecision(searchId, outcomeId);
    }
}
