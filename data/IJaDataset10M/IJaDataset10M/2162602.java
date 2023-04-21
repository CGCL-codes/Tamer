package edu.ucdavis.cs.dblp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import edu.ucdavis.cs.dblp.data.DblpPubDao;
import edu.ucdavis.cs.dblp.data.Publication;
import edu.ucdavis.cs.dblp.experts.SolrSearchServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
@ContextConfiguration(locations = { "/spring/dblpApplicationContext.xml" })
public class ContentServiceTest {

    private static final Logger logger = Logger.getLogger(SolrSearchServiceTest.class);

    @Resource(name = "contentService")
    private ContentService contentService;

    @Resource(name = "dblpPubDaoImpl")
    private DblpPubDao pubDao;

    @Test
    public void searchAndVerify() {
        String[] testIds = new String[] { "conf/isi/AbbasiC07", "conf/3dpvt/HussainON04", "conf/isi/AbbasiC05", "conf/RelMiCS/SamingerBKM06", "journals/aamas/AkninePS04", "journals/tcad/AntreichS87", "conf/3dim/BoehnenF05", "conf/isi/AbbasiC07" };
        for (String testStr : testIds) {
            Publication testPub = pubDao.findById(testStr);
            assertTrue("pub not accepted by any contentservice - " + testPub.getKey(), contentService.accepts(testPub));
            contentService.retrieveAll(testPub);
            logger.info("after retrieval: " + testPub);
            assertThat(testPub.getContent(), is(notNullValue()));
            assertThat(testPub.getContent().getAbstractText(), is(notNullValue()));
        }
    }
}
