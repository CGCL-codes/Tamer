package soma.rest.training.jerseySpringJDO.dao;

import java.util.Collection;
import javax.annotation.Resource;
import javax.jdo.PersistenceManagerFactory;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import soma.rest.training.jerseySpringJDO.model.Message;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/rootContext.xml", "classpath:/rootContext-dev.xml" })
public class LoggerTest {

    final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Autowired
    private LocalServiceTestHelper helper;

    @Autowired
    private PersistenceManagerFactory pmf;

    @Resource
    private MessageDAO messageDAOImpl;

    @Before
    public void setup() throws Exception {
        helper.setUp();
        logger.debug("=========   setUp start");
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testMessageDAOSearch() {
        Collection<Message> msgs = messageDAOImpl.search("test1");
        logger.debug(" ========= Collection messages  size =  " + msgs.size());
        logger.debug(" ========= Collection messages  toString =  " + msgs.toString());
    }

    @Test
    public void testMessageDAO() {
        Message message1 = new Message();
        message1.setId("1");
        message1.setText("test1");
        logger.debug("========= create message1   =   " + message1.toString());
        messageDAOImpl.create(message1);
        logger.debug("=========  message1   =   create method  ok ");
        logger.debug("=========  message1   =   " + messageDAOImpl.get("1"));
        Message message2 = new Message();
        message2.setId("2");
        message2.setText("test1");
        logger.debug("========= save message2   =   " + message2.toString());
        messageDAOImpl.save(message2);
        logger.debug("=========  message2   =   save method ok ");
        logger.debug("=========  message2   =   " + messageDAOImpl.get("2"));
        Message message3 = new Message();
        message3.setId("3");
        message3.setText("test1");
        logger.debug("========= create message3   =   " + message3.toString());
        messageDAOImpl.create(message3);
        logger.debug("=========  message3   =   create method ok ");
        logger.debug("=========  message3   =   " + messageDAOImpl.get("3"));
        logger.debug(" ========= testMessageDAO get method message id is < 1 >  ");
        Message message = messageDAOImpl.get("1");
        logger.debug(" =========  message   =   " + message.toString());
        Assert.assertNotNull(message);
        logger.debug(" =========  Assert.assertNotNull(message); ok ");
        logger.debug(" ========= testMessageDAO deleteMsg  message ");
        messageDAOImpl.deleteMsg(message);
        logger.debug(" ========= testMessageDAO deleteMsg  message ok  ");
    }
}
